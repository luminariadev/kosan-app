package com.mykosan.utils;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthUtil {
    private static final Logger logger = LoggerFactory.getLogger(AuthUtil.class);

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        try {
            logger.info("Verifikasi password - Plain: [{}], Length: {}, Hash: {}",
                    plainPassword.replaceAll(".", "*"), plainPassword.length(), hashedPassword.substring(0, 10) + "...");
            boolean isValid = BCrypt.checkpw(plainPassword, hashedPassword);
            logger.info("Verifikasi selesai - Berhasil: {}", isValid);
            return isValid;
        } catch (Exception e) {
            logger.error("Gagal memverifikasi password: {}", e.getMessage(), e);
            return false;
        }
    }

    public static String hashPassword(String plainPassword) {
        try {
            String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
            logger.info("Berhasil menghash password");
            return hashed;
        } catch (Exception e) {
            logger.error("Gagal menghash password: {}", e.getMessage(), e);
            throw new RuntimeException("Gagal menghash password", e);
        }
    }
}