package com.mykosan;

import com.mykosan.utils.AuthUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            URL fxmlUrl = getClass().getResource("/views/login.fxml");
            if (fxmlUrl == null) {
                logger.error("File login.fxml tidak ditemukan di /views");
                throw new RuntimeException("Gagal menemukan login.fxml");
            }
            Parent root = FXMLLoader.load(fxmlUrl);
            logger.info("Berhasil memuat login.fxml");

            String preferredSize = "medium";
            int width = 600;
            int height = 400;

            if ("small".equals(preferredSize)) {
                width = 400;
                height = 300;
            } else if ("large".equals(preferredSize)) {
                width = 1024;
                height = 768;
            }

            Scene scene = new Scene(root, width, height);

            try {
                URL cssUrl = getClass().getResource("/css/login.css");
                if (cssUrl != null) {
                    scene.getStylesheets().add(cssUrl.toExternalForm());
                    logger.info("Berhasil memuat login.css");
                } else {
                    logger.warn("File login.css tidak ditemukan di /css");
                }
            } catch (Exception e) {
                logger.error("Gagal memuat stylesheet: {}", e.getMessage());
            }

            try {
                Image icon = new Image(getClass().getResourceAsStream("/images/kosan.jpg"));
                if (!icon.isError()) {
                    primaryStage.getIcons().add(icon);
                    logger.info("Berhasil memuat icon.png");
                } else {
                    logger.warn("Gambar icon.png tidak valid");
                }
            } catch (Exception e) {
                logger.error("Gagal memuat icon aplikasi: {}", e.getMessage());
            }

            primaryStage.setTitle("Login Aplikasi Kosan");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(400);
            primaryStage.setMinHeight(300);
            primaryStage.setResizable(true);
            primaryStage.centerOnScreen();
            primaryStage.show();
            logger.info("Aplikasi berhasil dimulai");

        } catch (Exception e) {
            logger.error("Gagal memulai aplikasi: {}", e.getMessage(), e);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        // Uji BCrypt secara langsung
        String testPassword = "ahmad123";
        String testHash = "$2a$10$4qGkKAJ3M/VZqXNDtyoa4eiZsLCHJv02G9GPlyuasEFXc2tNwUFbe";
        boolean isValid = AuthUtil.checkPassword(testPassword, testHash);
        logger.info("Uji BCrypt - Password: [{}], Hash: {}, Berhasil: {}",
                testPassword.replaceAll(".", "*"), testHash.substring(0, 10) + "...", isValid);
        if (!isValid) {
            logger.error("Uji BCrypt gagal! Periksa library jbcrypt atau hash di database.");
        }
        launch(args);
    }
}