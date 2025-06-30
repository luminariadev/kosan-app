package com.mykosan.controller;

import com.mykosan.dao.*;
import com.mykosan.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label contactLabel;
    @FXML private Label paymentStatusLabel;
    @FXML private Label roomLabel;
    @FXML private Label dueNotificationLabel;
    @FXML private TableView<MonthlyPayment> monthlyPaymentsTable;
    @FXML private TableColumn<MonthlyPayment, String> monthColumn;
    @FXML private TableColumn<MonthlyPayment, Integer> amountColumn;
    @FXML private TableColumn<MonthlyPayment, String> statusColumn;
    @FXML private TableColumn<MonthlyPayment, String> paidDateColumn;
    @FXML private TableColumn<MonthlyPayment, Void> actionColumn;
    @FXML private TableView<SharedExpense> sharedExpensesTable;
    @FXML private TableColumn<SharedExpense, String> sharedTitleColumn;
    @FXML private TableColumn<SharedExpense, Integer> sharedAmountColumn;
    @FXML private TableColumn<SharedExpense, String> sharedDueDateColumn;
    @FXML private TableColumn<SharedExpense, String> contributionColumn;
    @FXML private TableColumn<SharedExpense, Void> sharedActionColumn;
    @FXML private TableView<PaymentRecord> paymentHistoryTable;
    @FXML private TableColumn<PaymentRecord, String> paymentTypeColumn;
    @FXML private TableColumn<PaymentRecord, String> paymentDescriptionColumn;
    @FXML private TableColumn<PaymentRecord, Integer> paymentAmountColumn;
    @FXML private TableColumn<PaymentRecord, String> paymentDateColumn;
    @FXML private Button logoutButton;
    @FXML private Button printReceiptButton;

    private User currentUser;
    private final MonthlyPaymentDAO monthlyPaymentDAO = new MonthlyPaymentDAO();
    private final SharedExpenseDAO sharedExpenseDAO = new SharedExpenseDAO();
    private final SharedPaymentDAO sharedPaymentDAO = new SharedPaymentDAO();
    private final RoomDAO roomDAO = new RoomDAO();
    private final ActivityLogDAO activityLogDAO = new ActivityLogDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupMonthlyPaymentsTable();
        setupSharedExpensesTable();
        setupPaymentHistoryTable();
    }

    public void setUser(User user) {
        this.currentUser = user;
        loadProfile();
        loadMonthlyPayments();
        loadSharedExpenses();
        loadPaymentHistory();
        checkDuePayments();
        logActivity("User login");
    }

    private void setupMonthlyPaymentsTable() {
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        paidDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPaidDate() != null ?
                        cellData.getValue().getPaidDate().format(dateFormatter) : ""));
        actionColumn.setCellFactory(col -> new TableCell<MonthlyPayment, Void>() {
            private final Button payButton = new Button("Bayar");
            {
                payButton.setOnAction(event -> {
                    MonthlyPayment payment = getTableView().getItems().get(getIndex());
                    handlePayMonthlyPayment(payment);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView().getItems().get(getIndex()).getStatus().equals("LUNAS")) {
                    setGraphic(null);
                } else {
                    setGraphic(payButton);
                }
            }
        });
    }

    private void setupSharedExpensesTable() {
        sharedTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        sharedAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        sharedDueDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDueDate() != null ?
                        cellData.getValue().getDueDate().format(dateFormatter) : ""));
        contributionColumn.setCellValueFactory(cellData -> {
            List<SharedPayment> payments = sharedPaymentDAO.getAllSharedPayments();
            int contribution = payments.stream()
                    .filter(p -> p.getExpenseId() == cellData.getValue().getExpenseId() &&
                            p.getUserId() == currentUser.getUserId())
                    .mapToInt(SharedPayment::getPaidAmount)
                    .sum();
            return new SimpleStringProperty(String.format("Rp %,d", contribution));
        });
        sharedActionColumn.setCellFactory(col -> new TableCell<SharedExpense, Void>() {
            private final Button payButton = new Button("Bayar");
            {
                payButton.setOnAction(event -> {
                    SharedExpense expense = getTableView().getItems().get(getIndex());
                    handlePaySharedExpense(expense);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    List<SharedPayment> payments = sharedPaymentDAO.getAllSharedPayments();
                    boolean hasPaid = payments.stream()
                            .anyMatch(p -> p.getExpenseId() == getTableView().getItems().get(getIndex()).getExpenseId() &&
                                    p.getUserId() == currentUser.getUserId());
                    setGraphic(hasPaid ? null : payButton);
                }
            }
        });
    }

    private void setupPaymentHistoryTable() {
        paymentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        paymentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private void loadProfile() {
        nameLabel.setText(currentUser.getFullName());
        emailLabel.setText(currentUser.getEmail());
        contactLabel.setText(currentUser.getContact());
        paymentStatusLabel.setText(getLatestPaymentStatus());
        Room room = roomDAO.getRoomById(getUserRoomId());
        roomLabel.setText(room != null ? room.getRoomName() : "Tidak Ada Kamar");
    }

    private String getLatestPaymentStatus() {
        List<MonthlyPayment> payments = monthlyPaymentDAO.getAllPayments().stream()
                .filter(p -> p.getUserId() == currentUser.getUserId())
                .toList();
        return payments.isEmpty() ? "Tidak Ada Tagihan" :
                payments.get(payments.size() - 1).getStatus();
    }

    private int getUserRoomId() {
        List<MonthlyPayment> payments = monthlyPaymentDAO.getAllPayments().stream()
                .filter(p -> p.getUserId() == currentUser.getUserId())
                .toList();
        return payments.isEmpty() ? 0 : payments.get(0).getRoomId();
    }

    private void loadMonthlyPayments() {
        List<MonthlyPayment> payments = monthlyPaymentDAO.getAllPayments().stream()
                .filter(p -> p.getUserId() == currentUser.getUserId())
                .toList();
        monthlyPaymentsTable.setItems(FXCollections.observableArrayList(payments));
    }

    private void loadSharedExpenses() {
        List<SharedExpense> expenses = sharedExpenseDAO.getAllExpenses();
        sharedExpensesTable.setItems(FXCollections.observableArrayList(expenses));
    }

    private void loadPaymentHistory() {
        List<PaymentRecord> records = new ArrayList<>();
        List<MonthlyPayment> monthlyPayments = monthlyPaymentDAO.getAllPayments().stream()
                .filter(p -> p.getUserId() == currentUser.getUserId() && p.getPaidDate() != null)
                .toList();
        for (MonthlyPayment mp : monthlyPayments) {
            records.add(new PaymentRecord(
                    "Bulanan",
                    mp.getMonth(),
                    mp.getAmount(),
                    mp.getPaidDate().format(dateFormatter)
            ));
        }
        List<SharedPayment> sharedPayments = sharedPaymentDAO.getAllSharedPayments().stream()
                .filter(sp -> sp.getUserId() == currentUser.getUserId())
                .toList();
        for (SharedPayment sp : sharedPayments) {
            SharedExpense expense = sharedExpenseDAO.getExpenseById(sp.getExpenseId());
            records.add(new PaymentRecord(
                    "Bersama",
                    expense != null ? expense.getTitle() : "Unknown",
                    sp.getPaidAmount(),
                    sp.getPaidDate().format(dateFormatter)
            ));
        }
        paymentHistoryTable.setItems(FXCollections.observableArrayList(records));
    }

    private void checkDuePayments() {
        LocalDate today = LocalDate.now();
        List<MonthlyPayment> dueMonthlyPayments = monthlyPaymentDAO.getAllPayments().stream()
                .filter(p -> p.getUserId() == currentUser.getUserId() &&
                        p.getStatus().equals("BELUM LUNAS"))
                .toList();
        List<SharedExpense> dueSharedExpenses = sharedExpenseDAO.getAllExpenses().stream()
                .filter(e -> e.getDueDate() != null && !e.getDueDate().isAfter(today))
                .filter(e -> sharedPaymentDAO.getAllSharedPayments().stream()
                        .noneMatch(sp -> sp.getExpenseId() == e.getExpenseId() &&
                                sp.getUserId() == currentUser.getUserId()))
                .toList();
        if (!dueMonthlyPayments.isEmpty() || !dueSharedExpenses.isEmpty()) {
            StringBuilder message = new StringBuilder("Tagihan jatuh tempo: ");
            for (MonthlyPayment mp : dueMonthlyPayments) {
                message.append(mp.getMonth()).append(", ");
            }
            for (SharedExpense se : dueSharedExpenses) {
                message.append(se.getTitle()).append(", ");
            }
            dueNotificationLabel.setText(message.toString());
        }
    }

    private void handlePayMonthlyPayment(MonthlyPayment payment) {
        payment.setStatus("LUNAS");
        payment.setPaidDate(LocalDateTime.now());
        monthlyPaymentDAO.updatePayment(payment);
        loadMonthlyPayments();
        loadPaymentHistory();
        checkDuePayments();
        paymentStatusLabel.setText(getLatestPaymentStatus());
        logActivity("Pembayaran bulanan: " + payment.getMonth());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pembayaran Berhasil");
        alert.setContentText("Tagihan untuk " + payment.getMonth() + " telah dibayar.");
        alert.showAndWait();
    }

    private void handlePaySharedExpense(SharedExpense expense) {
        SharedPayment payment = new SharedPayment(
                0, expense.getExpenseId(), currentUser.getUserId(),
                expense.getAmount() / 2, LocalDateTime.now());
        sharedPaymentDAO.insertSharedPayment(payment);
        loadSharedExpenses();
        loadPaymentHistory();
        checkDuePayments();
        logActivity("Pembayaran tagihan bersama: " + expense.getTitle());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pembayaran Berhasil");
        alert.setContentText("Kontribusi untuk " + expense.getTitle() + " telah dibayar.");
        alert.showAndWait();
    }

    @FXML
    private void handlePrintReceipt() {
        PaymentRecord selectedPayment = paymentHistoryTable.getSelectionModel().getSelectedItem();
        if (selectedPayment == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Pilih Pembayaran");
            alert.setContentText("Pilih pembayaran dari riwayat untuk mencetak bukti.");
            alert.showAndWait();
            return;
        }

        String latexContent = generateLatexReceipt(selectedPayment);
        try (FileWriter writer = new FileWriter("receipt_" + selectedPayment.getDescription() + ".tex")) {
            writer.write(latexContent);
            logger.info("Berhasil menghasilkan file LaTeX untuk bukti pembayaran: {}", selectedPayment.getDescription());
        } catch (IOException e) {
            logger.error("Gagal menghasilkan file LaTeX: {}", e.getMessage(), e);
        }
    }

    private String generateLatexReceipt(PaymentRecord payment) {
        return "\\documentclass{article}\n" +
                "\\usepackage{geometry}\n" +
                "\\geometry{a4paper, margin=1in}\n" +
                "\\usepackage{noto}\n" +
                "\\begin{document}\n" +
                "\\begin{center}\n" +
                "\\textbf{Bukti Pembayaran Kos}\n" +
                "\\vspace{1cm}\n" +
                "\\end{center}\n" +
                "\\noindent\n" +
                "\\textbf{Nama:} " + currentUser.getFullName() + " \\\\\n" +
                "\\textbf{Tipe Pembayaran:} " + payment.getType() + " \\\\\n" +
                "\\textbf{Deskripsi:} " + payment.getDescription() + " \\\\\n" +
                "\\textbf{Jumlah:} Rp " + String.format("%,d", payment.getAmount()) + " \\\\\n" +
                "\\textbf{Tanggal Pembayaran:} " + payment.getDate() + " \\\\\n" +
                "\\vspace{1cm}\n" +
                "\\noindent\n" +
                "Terima kasih atas pembayaran Anda.\n" +
                "\\end{document}";
    }

    @FXML
    private void handleLogout() {
        try {
            logActivity("User logout");
            URL fxmlUrl = getClass().getResource("/views/login.fxml");
            if (fxmlUrl == null) {
                logger.error("File login.fxml tidak ditemukan");
                return;
            }
            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/login.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Login Aplikasi Kosan");
            stage.show();
            logger.info("Pengguna logout");
        } catch (Exception e) {
            logger.error("Gagal logout: {}", e.getMessage(), e);
        }
    }

    private void logActivity(String action) {
        ActivityLog log = new ActivityLog(0, currentUser.getUserId(), action, LocalDateTime.now());
        activityLogDAO.insertLog(log);
    }

    public static class PaymentRecord {
        private final String type;
        private final String description;
        private final int amount;
        private final String date;

        public PaymentRecord(String type, String description, int amount, String date) {
            this.type = type;
            this.description = description;
            this.amount = amount;
            this.date = date;
        }

        public String getType() { return type; }
        public String getDescription() { return description; }
        public int getAmount() { return amount; }
        public String getDate() { return date; }
    }
}