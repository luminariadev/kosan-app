package com.mykosan.controller;

import com.mykosan.dao.UserDAO;
import com.mykosan.model.User;
import com.mykosan.utils.AuthUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UserDAO userDAO = new UserDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        passwordField.textProperty().addListener((obs, oldVal, newVal) ->
                logger.info("Password field updated, length: {}", newVal.length())
        );
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        logger.info("Mencoba login: username={}, passwordLength={}", username, password.length());

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username dan Password wajib diisi.");
            return;
        }

        User user = userDAO.getUserByUsername(username);
        if (user == null || !AuthUtil.checkPassword(password, user.getPassword())) {
            errorLabel.setText("Username atau Password salah.");
            return;
        }

        logger.info("Login berhasil: {} ({})", user.getFullName(), user.getRole());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login Berhasil");
        alert.setHeaderText(null);
        alert.setContentText("Selamat datang, " + user.getFullName() + "!");
        alert.showAndWait();

        try {
            String fxmlPath = user.getRole().equalsIgnoreCase("admin")
                    ? "/views/dashboard.fxml"
                    : "/views/user.fxml";
            String cssPath = user.getRole().equalsIgnoreCase("admin")
                    ? "/css/dashboard.css"
                    : "/css/user.css";
            String windowTitle = user.getRole().equalsIgnoreCase("admin")
                    ? "Admin Dashboard"
                    : "User Dashboard";

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // ⬇️ Pastikan controller diambil dan user diset setelah UI siap
            if (!user.getRole().equalsIgnoreCase("admin")) {
                UserController userController = loader.getController();
                Platform.runLater(() -> {
                    try {
                        userController.setUser(user);
                        logger.info("UserController berhasil menerima user: {}", user.getUsername());
                    } catch (Exception e) {
                        logger.error("Gagal memanggil setUser(): {}", e.getMessage(), e);
                        errorLabel.setText("Gagal mengatur tampilan pengguna.");
                    }
                });
            }

            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().clear();
            URL cssUrl = getClass().getResource(cssPath);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }
            stage.setScene(scene);
            stage.setTitle(windowTitle);
            stage.show();

        } catch (Exception e) {
            logger.error("Gagal membuka dashboard: {}", e.getMessage(), e);
            errorLabel.setText("Terjadi kesalahan membuka tampilan.");
        }
    }
}
