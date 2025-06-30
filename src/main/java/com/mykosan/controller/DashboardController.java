package com.mykosan.controller;

import com.mykosan.dao.*;
import com.mykosan.model.*;
import com.mykosan.utils.AuthUtil;
import com.mykosan.utils.DBUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class DashboardController {
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @FXML private TabPane mainTabPane;
    @FXML private Button addUserButton;
    @FXML private Button editUserButton;
    @FXML private Button deleteUserButton;
    @FXML private Button addRoomButton;
    @FXML private Button editRoomButton;
    @FXML private Button recordPaymentButton;
    @FXML private Button addExpenseButton;
    @FXML private Button recordSharedPaymentButton;
    @FXML private Button generateReportButton;
    @FXML private TableView<User> userTable;
    @FXML private TableView<Room> roomTable;
    @FXML private TableView<MonthlyPayment> paymentTable;
    @FXML private TableView<SharedExpense> expenseTable;
    @FXML private TableView<SharedPayment> sharedPaymentTable;
    @FXML private TextField paymentSearch;
    @FXML private ComboBox<String> expenseType;
    @FXML private ComboBox<String> reportMonth;
    @FXML private TextArea reportOutput;
    @FXML private ListView<String> activityLog;
    @FXML private Button logoutButton;

    private final ObservableList<User> userList = FXCollections.observableArrayList();
    private final ObservableList<Room> roomList = FXCollections.observableArrayList();
    private final ObservableList<MonthlyPayment> paymentList = FXCollections.observableArrayList();
    private final ObservableList<SharedExpense> expenseList = FXCollections.observableArrayList();
    private final ObservableList<SharedPayment> sharedPaymentList = FXCollections.observableArrayList();
    private final ObservableList<String> activityLogList = FXCollections.observableArrayList();

    private final UserDAO userDAO = new UserDAO();
    private final RoomDAO roomDAO = new RoomDAO();
    private final MonthlyPaymentDAO paymentDAO = new MonthlyPaymentDAO();
    private final SharedExpenseDAO expenseDAO = new SharedExpenseDAO();
    private final SharedPaymentDAO sharedPaymentDAO = new SharedPaymentDAO();
    private final ActivityLogDAO activityLogDAO = new ActivityLogDAO();

    private Stage stage;

    @FXML
    public void initialize() {
        initUserTable();
        initRoomTable();
        initPaymentTable();
        initExpenseTable();
        initSharedPaymentTable();
        initExpenseTypeComboBox();
        initReportMonthComboBox();
        initActivityLog();

        loadDataFromDB();

        paymentSearch.textProperty().addListener((obs, oldVal, newVal) -> filterPayments(newVal));
        logger.info("DashboardController diinisialisasi pada {}", LocalDateTime.now());

        if (stage != null) {
            stage.widthProperty().addListener((obs, oldVal, newVal) -> adjustLayout(newVal.doubleValue()));
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void adjustLayout(double width) {
        mainTabPane.setPrefWidth(Math.min(width - 40, 900));
    }

    private void initUserTable() {
        TableColumn<User, String> fullNameCol = new TableColumn<>("Nama Lengkap");
        fullNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFullName() != null ? cellData.getValue().getFullName() : "-"));
        fullNameCol.setPrefWidth(200);
        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername() != null ? cellData.getValue().getUsername() : "-"));
        usernameCol.setPrefWidth(150);
        TableColumn<User, String> contactCol = new TableColumn<>("Kontak");
        contactCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContact() != null ? cellData.getValue().getContact() : "-"));
        contactCol.setPrefWidth(150);
        TableColumn<User, String> roleCol = new TableColumn<>("Peran");
        roleCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole() != null ? cellData.getValue().getRole() : "-"));
        roleCol.setPrefWidth(100);

        userTable.getColumns().setAll(fullNameCol, usernameCol, contactCol, roleCol);
        userTable.setItems(userList);
        userTable.setPlaceholder(new Label("Tidak ada data pengguna."));
        userTable.refresh();
    }

    private void initRoomTable() {
        TableColumn<Room, String> roomNameCol = new TableColumn<>("Nomor Kamar");
        roomNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoomName() != null ? cellData.getValue().getRoomName() : "-"));
        roomNameCol.setPrefWidth(200);
        TableColumn<Room, Integer> priceCol = new TableColumn<>("Harga/Bulan");
        priceCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()));
        priceCol.setPrefWidth(150);
        TableColumn<Room, String> occupiedCol = new TableColumn<>("Status");
        occupiedCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isOccupied() ? "Terisi" : "Kosong"));
        occupiedCol.setPrefWidth(100);

        roomTable.getColumns().setAll(roomNameCol, priceCol, occupiedCol);
        roomTable.setItems(roomList);
        roomTable.setPlaceholder(new Label("Tidak ada data kamar."));
        roomTable.refresh();
    }

    private void initPaymentTable() {
        TableColumn<MonthlyPayment, String> tenantCol = new TableColumn<>("Penghuni");
        tenantCol.setCellValueFactory(cellData -> {
            User user = userDAO.getUserById(cellData.getValue().getUserId());
            return new SimpleStringProperty(user != null && user.getFullName() != null ? user.getFullName() : "-");
        });
        tenantCol.setPrefWidth(200);
        TableColumn<MonthlyPayment, String> monthCol = new TableColumn<>("Bulan");
        monthCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMonth() != null ? cellData.getValue().getMonth() : "-"));
        monthCol.setPrefWidth(100);
        TableColumn<MonthlyPayment, Integer> amountCol = new TableColumn<>("Jumlah");
        amountCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAmount()));
        amountCol.setPrefWidth(150);
        TableColumn<MonthlyPayment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus() != null ? cellData.getValue().getStatus() : "-"));
        statusCol.setPrefWidth(100);
        TableColumn<MonthlyPayment, String> paidDateCol = new TableColumn<>("Tanggal Bayar");
        paidDateCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getPaidDate() != null ?
                        cellData.getValue().getPaidDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "-"));
        paidDateCol.setPrefWidth(150);

        paymentTable.getColumns().setAll(tenantCol, monthCol, amountCol, statusCol, paidDateCol);
        paymentTable.setItems(paymentList);
        paymentTable.setPlaceholder(new Label("Tidak ada data pembayaran."));
        paymentTable.refresh();
    }

    private void initExpenseTable() {
        TableColumn<SharedExpense, String> titleCol = new TableColumn<>("Judul");
        titleCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle() != null ? cellData.getValue().getTitle() : "-"));
        titleCol.setPrefWidth(200);
        TableColumn<SharedExpense, Integer> amountCol = new TableColumn<>("Jumlah");
        amountCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAmount()));
        amountCol.setPrefWidth(150);
        TableColumn<SharedExpense, String> dueDateCol = new TableColumn<>("Jatuh Tempo");
        dueDateCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDueDate() != null ?
                        cellData.getValue().getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "-"));
        dueDateCol.setPrefWidth(150);

        expenseTable.getColumns().setAll(titleCol, amountCol, dueDateCol);
        expenseTable.setItems(expenseList);
        expenseTable.setPlaceholder(new Label("Tidak ada data tagihan bersama."));
        expenseTable.refresh();
    }

    private void initSharedPaymentTable() {
        TableColumn<SharedPayment, String> userCol = new TableColumn<>("Penghuni");
        userCol.setCellValueFactory(cellData -> {
            User user = userDAO.getUserById(cellData.getValue().getUserId());
            return new SimpleStringProperty(user != null && user.getFullName() != null ? user.getFullName() : "-");
        });
        userCol.setPrefWidth(200);
        TableColumn<SharedPayment, String> expenseCol = new TableColumn<>("Tagihan");
        expenseCol.setCellValueFactory(cellData -> {
            SharedExpense expense = expenseDAO.getExpenseById(cellData.getValue().getExpenseId());
            return new SimpleStringProperty(expense != null && expense.getTitle() != null ? expense.getTitle() : "-");
        });
        expenseCol.setPrefWidth(200);
        TableColumn<SharedPayment, Integer> amountCol = new TableColumn<>("Jumlah Bayar");
        amountCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPaidAmount()));
        amountCol.setPrefWidth(150);
        TableColumn<SharedPayment, String> paidDateCol = new TableColumn<>("Tanggal Bayar");
        paidDateCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getPaidDate() != null ?
                        cellData.getValue().getPaidDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "-"));
        paidDateCol.setPrefWidth(150);

        sharedPaymentTable.getColumns().setAll(userCol, expenseCol, amountCol, paidDateCol);
        sharedPaymentTable.setItems(sharedPaymentList);
        sharedPaymentTable.setPlaceholder(new Label("Tidak ada data pembayaran bersama."));
        sharedPaymentTable.refresh();
    }

    private void initExpenseTypeComboBox() {
        expenseType.setItems(FXCollections.observableArrayList("Listrik", "Air", "WiFi", "Lainnya"));
        expenseType.getSelectionModel().selectFirst();
    }

    private void initReportMonthComboBox() {
        reportMonth.setItems(FXCollections.observableArrayList(
                "Januari", "Februari", "Maret", "April", "Mei", "Juni",
                "Juli", "Agustus", "September", "Oktober", "November", "Desember"));
        reportMonth.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);
    }

    private void initActivityLog() {
        activityLog.setItems(activityLogList);
        activityLog.setPlaceholder(new Label("Tidak ada log aktivitas."));
        activityLog.refresh();
    }

    private void loadDataFromDB() {
        loadUsers();
        loadRooms();
        loadPayments();
        loadExpenses();
        loadSharedPayments();
        loadActivityLog();
        // Refresh semua tabel setelah pemuatan data
        userTable.refresh();
        roomTable.refresh();
        paymentTable.refresh();
        expenseTable.refresh();
        sharedPaymentTable.refresh();
        activityLog.refresh();
    }

    private void loadUsers() {
        userList.clear();
        userList.addAll(userDAO.getAllUsers());
        logger.info("Memuat {} pengguna dari database", userList.size());
        for (User user : userList) {
            logger.info("Pengguna: ID={}, Nama={}, Username={}", user.getUserId(), user.getFullName(), user.getUsername());
        }
    }

    private void loadRooms() {
        roomList.clear();
        roomList.addAll(roomDAO.getAllRooms());
        logger.info("Memuat {} kamar dari database", roomList.size());
        for (Room room : roomList) {
            logger.info("Kamar: ID={}, Nama={}, Harga={}", room.getRoomId(), room.getRoomName(), room.getPrice());
        }
    }

    private void loadPayments() {
        try {
            paymentList.clear();
            paymentList.addAll(paymentDAO.getAllPayments());
            logger.info("Memuat {} pembayaran dari database", paymentList.size());
            for (MonthlyPayment payment : paymentList) {
                logger.info("Pembayaran: ID={}, UserID={}, Bulan={}", payment.getPaymentId(), payment.getUserId(), payment.getMonth());
            }
        } catch (Exception e) {
            logger.error("Gagal memuat pembayaran: {}", e.getMessage(), e);
            showErrorAlert("Gagal memuat pembayaran: " + e.getMessage());
        }
    }

    private void loadExpenses() {
        expenseList.clear();
        expenseList.addAll(expenseDAO.getAllExpenses());
        logger.info("Memuat {} tagihan dari database", expenseList.size());
        for (SharedExpense expense : expenseList) {
            logger.info("Tagihan: ID={}, Judul={}, Jumlah={}", expense.getExpenseId(), expense.getTitle(), expense.getAmount());
        }
    }

    private void loadSharedPayments() {
        sharedPaymentList.clear();
        sharedPaymentList.addAll(sharedPaymentDAO.getAllSharedPayments());
        logger.info("Memuat {} pembayaran bersama dari database", sharedPaymentList.size());
        for (SharedPayment payment : sharedPaymentList) {
            logger.info("Pembayaran Bersama: ID={}, ExpenseID={}, UserID={}", payment.getPaymentId(), payment.getExpenseId(), payment.getUserId());
        }
    }

    private void loadActivityLog() {
        activityLogList.clear();
        try {
            for (ActivityLog log : activityLogDAO.getAllLogs()) {
                String logEntry = String.format("[%s] %s",
                        log.getTimestamp() != null ? log.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "-",
                        log.getAction() != null ? log.getAction() : "-");
                activityLogList.add(logEntry);
            }
            logger.info("Memuat {} log aktivitas dari database", activityLogList.size());
            for (String log : activityLogList) {
                logger.info("Log: {}", log);
            }
        } catch (SQLException e) {
            logger.error("Gagal memuat log aktivitas: {}", e.getMessage(), e);
            showErrorAlert("Gagal memuat log aktivitas: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddUser() {
        Dialog<User> dialog = createUserDialog(null);
        Optional<User> result = dialog.showAndWait();
        result.ifPresent(user -> {
            if (userDAO.getUserByUsername(user.getUsername()) != null) {
                showErrorAlert("Username sudah digunakan!");
                return;
            }
            if (userDAO.insertUser(user)) {
                userList.add(user);
                logActivity("Penghuni baru ditambahkan: " + user.getFullName());
                showInfoAlert("Penghuni berhasil ditambahkan!");
                userTable.refresh();
            } else {
                showErrorAlert("Gagal menambahkan penghuni!");
            }
        });
    }

    @FXML
    private void handleEditUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showErrorAlert("Pilih penghuni terlebih dahulu!");
            return;
        }
        Dialog<User> dialog = createUserDialog(selected);
        Optional<User> result = dialog.showAndWait();
        result.ifPresent(user -> {
            if (!user.getUsername().equals(selected.getUsername()) && userDAO.getUserByUsername(user.getUsername()) != null) {
                showErrorAlert("Username sudah digunakan!");
                return;
            }
            user.setUserId(selected.getUserId());
            if (userDAO.updateUser(user)) {
                userList.set(userList.indexOf(selected), user);
                logActivity("Data penghuni diedit: " + user.getFullName());
                showInfoAlert("Penghuni berhasil diedit!");
                userTable.refresh();
            } else {
                showErrorAlert("Gagal mengedit penghuni!");
            }
        });
    }

    @FXML
    private void handleDeleteUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showErrorAlert("Pilih penghuni terlebih dahulu!");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Hapus penghuni " + selected.getFullName() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                if (userDAO.deleteUser(selected.getUserId())) {
                    userList.remove(selected);
                    logActivity("Penghuni dihapus: " + selected.getFullName());
                    showInfoAlert("Penghuni berhasil dihapus!");
                    userTable.refresh();
                } else {
                    showErrorAlert("Gagal menghapus penghuni!");
                }
            }
        });
    }

    @FXML
    private void handleAddRoom() {
        Dialog<Room> dialog = createRoomDialog(null);
        Optional<Room> result = dialog.showAndWait();
        result.ifPresent(room -> {
            if (roomDAO.insertRoom(room)) {
                roomList.add(room);
                logActivity("Kamar baru ditambahkan: " + room.getRoomName());
                showInfoAlert("Kamar berhasil ditambahkan!");
                roomTable.refresh();
            } else {
                showErrorAlert("Gagal menambahkan kamar!");
            }
        });
    }

    @FXML
    private void handleEditRoom() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showErrorAlert("Pilih kamar terlebih dahulu!");
            return;
        }
        Dialog<Room> dialog = createRoomDialog(selected);
        Optional<Room> result = dialog.showAndWait();
        result.ifPresent(room -> {
            room.setRoomId(selected.getRoomId());
            if (roomDAO.updateRoom(room)) {
                roomList.set(roomList.indexOf(selected), room);
                logActivity("Data kamar diedit: " + room.getRoomName());
                showInfoAlert("Kamar berhasil diedit!");
                roomTable.refresh();
            } else {
                showErrorAlert("Gagal mengedit kamar!");
            }
        });
    }

    @FXML
    private void handleRecordPayment() {
        Dialog<MonthlyPayment> dialog = createPaymentDialog(null);
        Optional<MonthlyPayment> result = dialog.showAndWait();
        result.ifPresent(payment -> {
            if (paymentDAO.insertPayment(payment)) {
                paymentList.add(payment);
                logActivity("Pembayaran baru dicatat untuk " + getUserNameById(payment.getUserId()));
                showInfoAlert("Pembayaran berhasil dicatat!");
                paymentTable.refresh();
            } else {
                showErrorAlert("Gagal mencatat pembayaran!");
            }
        });
    }

    @FXML
    private void handleAddExpense() {
        Dialog<SharedExpense> dialog = createExpenseDialog(null);
        Optional<SharedExpense> result = dialog.showAndWait();
        result.ifPresent(expense -> {
            if (expenseDAO.insertExpense(expense)) {
                expenseList.add(expense);
                logActivity("Tagihan baru ditambahkan: " + expense.getTitle());
                showInfoAlert("Tagihan berhasil ditambahkan!");
                expenseTable.refresh();
            } else {
                showErrorAlert("Gagal menambahkan tagihan!");
            }
        });
    }

    @FXML
    private void handleRecordSharedPayment() {
        Dialog<SharedPayment> dialog = createSharedPaymentDialog(null);
        Optional<SharedPayment> result = dialog.showAndWait();
        result.ifPresent(payment -> {
            if (sharedPaymentDAO.insertSharedPayment(payment)) {
                sharedPaymentList.add(payment);
                logActivity("Pembayaran bersama dicatat untuk " + getExpenseTitleById(payment.getExpenseId()));
                showInfoAlert("Pembayaran bersama berhasil dicatat!");
                sharedPaymentTable.refresh();
            } else {
                showErrorAlert("Gagal mencatat pembayaran bersama!");
            }
        });
    }

    @FXML
    private void handleGenerateReport() {
        String selectedMonth = reportMonth.getValue();
        if (selectedMonth == null) {
            showErrorAlert("Pilih bulan terlebih dahulu!");
            return;
        }
        StringBuilder report = new StringBuilder();
        report.append("Laporan Keuangan untuk ").append(selectedMonth).append(" 2025\n\n");
        double totalPayment = 0, totalExpense = 0;
        String monthPattern = selectedMonth + " 2025";

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT u.full_name, mp.month, mp.amount, mp.status FROM monthly_payments mp " +
                            "JOIN users u ON mp.user_id = u.user_id WHERE mp.month = ?");
            ps.setString(1, monthPattern);
            ResultSet rs = ps.executeQuery();
            report.append("Pembayaran Penghuni:\n");
            while (rs.next()) {
                report.append("- ").append(rs.getString("full_name")).append(": Rp").append(rs.getInt("amount"))
                        .append(" (").append(rs.getString("status")).append(")\n");
                totalPayment += rs.getInt("amount");
            }
            report.append("Total Pembayaran: Rp").append(totalPayment).append("\n\n");

            ps = conn.prepareStatement("SELECT title, amount FROM shared_expenses WHERE strftime('%m', due_date) = ?");
            ps.setString(1, String.format("%02d", reportMonth.getItems().indexOf(selectedMonth) + 1));
            rs = ps.executeQuery();
            report.append("Tagihan Bersama:\n");
            while (rs.next()) {
                report.append("- ").append(rs.getString("title")).append(": Rp").append(rs.getInt("amount")).append("\n");
                totalExpense += rs.getInt("amount");
            }
            report.append("Total Tagihan: Rp").append(totalExpense).append("\n\n");
            report.append("Keuntungan Bersih: Rp").append(totalPayment - totalExpense);
        } catch (SQLException e) {
            logger.error("Gagal membuat laporan: {}", e.getMessage(), e);
            showErrorAlert("Gagal membuat laporan: " + e.getMessage());
            return;
        }
        reportOutput.setText(report.toString());
        logActivity("Laporan keuangan untuk " + selectedMonth + " dibuat");
        showInfoAlert("Laporan berhasil dibuat!");
    }

    private void filterPayments(String query) {
        if (query == null || query.isEmpty()) {
            paymentTable.setItems(paymentList);
        } else {
            ObservableList<MonthlyPayment> filtered = FXCollections.observableArrayList();
            for (MonthlyPayment payment : paymentList) {
                User user = userDAO.getUserById(payment.getUserId());
                Room room = roomDAO.getRoomById(payment.getRoomId());
                if ((user != null && user.getFullName().toLowerCase().contains(query.toLowerCase())) ||
                        (room != null && room.getRoomName().toLowerCase().contains(query.toLowerCase()))) {
                    filtered.add(payment);
                }
            }
            paymentTable.setItems(filtered);
        }
        paymentTable.refresh();
    }

    private Dialog<User> createUserDialog(User user) {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle(user == null ? "Tambah Penghuni" : "Edit Penghuni");
        ButtonType saveButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField fullNameField = new TextField(user != null ? user.getFullName() : "");
        TextField usernameField = new TextField(user != null ? user.getUsername() : "");
        TextField emailField = new TextField(user != null ? user.getEmail() : "");
        TextField contactField = new TextField(user != null ? user.getContact() : "");
        ComboBox<String> roleCombo = new ComboBox<>(FXCollections.observableArrayList("user", "admin"));
        roleCombo.setValue(user != null ? user.getRole() : "user");
        PasswordField passwordField = new PasswordField();

        grid.add(new Label("Nama Lengkap:"), 0, 0);
        grid.add(fullNameField, 1, 0);
        grid.add(new Label("Username:"), 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Kontak:"), 0, 3);
        grid.add(contactField, 1, 3);
        grid.add(new Label("Peran:"), 0, 4);
        grid.add(roleCombo, 1, 4);
        grid.add(new Label("Password:"), 0, 5);
        grid.add(passwordField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String rawPassword = passwordField.getText();
                String finalPassword;

                if (rawPassword.isEmpty() && user != null) {
                    finalPassword = user.getPassword(); // gunakan password lama
                } else {
                    finalPassword = AuthUtil.hashPassword(rawPassword); // hash baru
                }

                return new User(
                        user != null ? user.getUserId() : 0,
                        fullNameField.getText(),
                        usernameField.getText(),
                        emailField.getText(),
                        contactField.getText(),
                        roleCombo.getValue(),
                        finalPassword
                );
            }
            return null;
        });


        return dialog;
    }

    private Dialog<Room> createRoomDialog(Room room) {
        Dialog<Room> dialog = new Dialog<>();
        dialog.setTitle(room == null ? "Tambah Kamar" : "Edit Kamar");
        ButtonType saveButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(saveButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField roomNameField = new TextField(room != null ? room.getRoomName() : "");
        TextField priceField = new TextField(room != null ? String.valueOf(room.getPrice()) : "");
        CheckBox occupiedCheck = new CheckBox("Terisi");
        occupiedCheck.setSelected(room != null && room.isOccupied());

        grid.add(new Label("Nomor Kamar:"), 0, 0);
        grid.add(roomNameField, 1, 0);
        grid.add(new Label("Harga:"), 0, 1);
        grid.add(priceField, 1, 1);
        grid.add(new Label("Status:"), 0, 2);
        grid.add(occupiedCheck, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    return new Room(
                            0,
                            roomNameField.getText(),
                            Integer.parseInt(priceField.getText()),
                            occupiedCheck.isSelected()
                    );
                } catch (NumberFormatException e) {
                    showErrorAlert("Harga harus berupa angka!");
                    return null;
                }
            }
            return null;
        });

        return dialog;
    }

    private Dialog<MonthlyPayment> createPaymentDialog(MonthlyPayment payment) {
        Dialog<MonthlyPayment> dialog = new Dialog<>();
        dialog.setTitle(payment == null ? "Catat Pembayaran" : "Edit Pembayaran");
        ButtonType saveButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ComboBox<User> userCombo = new ComboBox<>(FXCollections.observableArrayList(userList));
        userCombo.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getFullName());
            }
        });
        userCombo.setButtonCell(new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getFullName());
            }
        });
        if (payment != null) {
            userCombo.setValue(userDAO.getUserById(payment.getUserId()));
        }

        ComboBox<Room> roomCombo = new ComboBox<>(FXCollections.observableArrayList(roomList));
        roomCombo.setCellFactory(lv -> new ListCell<Room>() {
            @Override
            protected void updateItem(Room item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getRoomName());
            }
        });
        roomCombo.setButtonCell(new ListCell<Room>() {
            @Override
            protected void updateItem(Room item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getRoomName());
            }
        });
        if (payment != null) {
            roomCombo.setValue(roomDAO.getRoomById(payment.getRoomId()));
        }

        TextField monthField = new TextField(payment != null ? payment.getMonth() : "");
        TextField amountField = new TextField(payment != null ? String.valueOf(payment.getAmount()) : "");
        ComboBox<String> statusCombo = new ComboBox<>(FXCollections.observableArrayList("pending", "lunas"));
        statusCombo.setValue(payment != null ? payment.getStatus() : "pending");
        DatePicker paidDatePicker = new DatePicker(payment != null && payment.getPaidDate() != null ? payment.getPaidDate().toLocalDate() : null);

        grid.add(new Label("Penghuni:"), 0, 0);
        grid.add(userCombo, 1, 0);
        grid.add(new Label("Kamar:"), 0, 1);
        grid.add(roomCombo, 1, 1);
        grid.add(new Label("Bulan:"), 0, 2);
        grid.add(monthField, 1, 2);
        grid.add(new Label("Jumlah:"), 0, 3);
        grid.add(amountField, 1, 3);
        grid.add(new Label("Status:"), 0, 4);
        grid.add(statusCombo, 1, 4);
        grid.add(new Label("Tanggal Bayar:"), 0, 5);
        grid.add(paidDatePicker, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    LocalDateTime paidDate = paidDatePicker.getValue() != null ? paidDatePicker.getValue().atStartOfDay() : null;
                    return new MonthlyPayment(
                            0,
                            userCombo.getValue().getUserId(),
                            roomCombo.getValue().getRoomId(),
                            monthField.getText(),
                            Integer.parseInt(amountField.getText()),
                            statusCombo.getValue(),
                            paidDate
                    );
                } catch (NumberFormatException e) {
                    showErrorAlert("Jumlah harus berupa angka!");
                    return null;
                }
            }
            return null;
        });

        return dialog;
    }

    private Dialog<SharedExpense> createExpenseDialog(SharedExpense expense) {
        Dialog<SharedExpense> dialog = new Dialog<>();
        dialog.setTitle(expense == null ? "Tambah Tagihan" : "Edit Tagihan");
        ButtonType saveButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField titleField = new TextField(expense != null ? expense.getTitle() : "");
        TextField amountField = new TextField(expense != null ? String.valueOf(expense.getAmount()) : "");
        DatePicker dueDatePicker = new DatePicker(expense != null && expense.getDueDate() != null ? expense.getDueDate() : null);

        grid.add(new Label("Judul:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Jumlah:"), 0, 1);
        grid.add(amountField, 1, 1);
        grid.add(new Label("Jatuh Tempo:"), 0, 2);
        grid.add(dueDatePicker, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    return new SharedExpense(
                            0,
                            titleField.getText(),
                            Integer.parseInt(amountField.getText()),
                            dueDatePicker.getValue()
                    );
                } catch (NumberFormatException e) {
                    showErrorAlert("Jumlah harus berupa angka!");
                    return null;
                }
            }
            return null;
        });

        return dialog;
    }

    private Dialog<SharedPayment> createSharedPaymentDialog(SharedPayment payment) {
        Dialog<SharedPayment> dialog = new Dialog<>();
        dialog.setTitle(payment == null ? "Catat Pembayaran Bersama" : "Edit Pembayaran Bersama");
        ButtonType saveButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ComboBox<SharedExpense> expenseCombo = new ComboBox<>(FXCollections.observableArrayList(expenseList));
        expenseCombo.setCellFactory(lv -> new ListCell<SharedExpense>() {
            @Override
            protected void updateItem(SharedExpense item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTitle());
            }
        });
        expenseCombo.setButtonCell(new ListCell<SharedExpense>() {
            @Override
            protected void updateItem(SharedExpense item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTitle());
            }
        });
        if (payment != null) {
            expenseCombo.setValue(expenseDAO.getExpenseById(payment.getExpenseId()));
        }

        ComboBox<User> userCombo = new ComboBox<>(FXCollections.observableArrayList(userList));
        userCombo.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getFullName());
            }
        });
        userCombo.setButtonCell(new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getFullName());
            }
        });
        if (payment != null) {
            userCombo.setValue(userDAO.getUserById(payment.getUserId()));
        }

        TextField amountField = new TextField(payment != null ? String.valueOf(payment.getPaidAmount()) : "");
        DatePicker paidDatePicker = new DatePicker(payment != null && payment.getPaidDate() != null ? payment.getPaidDate().toLocalDate() : null);

        grid.add(new Label("Tagihan:"), 0, 0);
        grid.add(expenseCombo, 1, 0);
        grid.add(new Label("Penghuni:"), 0, 1);
        grid.add(userCombo, 1, 1);
        grid.add(new Label("Jumlah:"), 0, 2);
        grid.add(amountField, 1, 2);
        grid.add(new Label("Tanggal Bayar:"), 0, 3);
        grid.add(paidDatePicker, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    LocalDateTime paidDate = paidDatePicker.getValue() != null ? paidDatePicker.getValue().atStartOfDay() : null;
                    return new SharedPayment(
                            0,
                            expenseCombo.getValue().getExpenseId(),
                            userCombo.getValue().getUserId(),
                            Integer.parseInt(amountField.getText()),
                            paidDate
                    );
                } catch (NumberFormatException e) {
                    showErrorAlert("Jumlah harus dibayar dalam angka!");
                    return null;
                }
            }
            return null;
        });

        return dialog;
    }

    @FXML
    private void handleLogout() {
        try {
            logActivity("Admin logout");
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
            logger.info("Admin logout berhasil");
        } catch (Exception e) {
            logger.error("Gagal logout: {}", e.getMessage(), e);
            showErrorAlert("Gagal logout: " + e.getMessage());
        }
    }

    private void logActivity(String message) {
        ActivityLog log = new ActivityLog(0, null, message, LocalDateTime.now());
        activityLogDAO.insertLog(log);
        activityLogList.add(String.format("[%s] %s", log.getTimestamp(), message));
    }

    private String getUserNameById(int id) {
        User user = userDAO.getUserById(id);
        return user != null ? user.getFullName() : "-";
    }

    private String getExpenseTitleById(int id) {
        SharedExpense expense = expenseDAO.getExpenseById(id);
        return expense != null ? expense.getTitle() : "-";
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}