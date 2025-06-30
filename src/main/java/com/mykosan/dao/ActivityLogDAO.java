package com.mykosan.dao;

import com.mykosan.model.ActivityLog;
import com.mykosan.utils.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogDAO {
    private static final Logger logger = LoggerFactory.getLogger(ActivityLogDAO.class);

    // Formatter untuk timestamp dengan fraksi detik opsional
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm:ss")
            .optionalStart()
            .appendFraction(java.time.temporal.ChronoField.NANO_OF_SECOND, 0, 9, true)
            .optionalEnd()
            .toFormatter();

    // Nama kolom ID, akan diperiksa secara dinamis
    private String idColumnName = "id";

    public ActivityLogDAO() {
        // Periksa skema tabel saat inisialisasi
        checkTableSchema();
    }

    private void checkTableSchema() {
        try (Connection conn = DBUtil.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getColumns(null, null, "activity_log", null);
            boolean tableExists = false;
            boolean idColumnFound = false;

            while (rs.next()) {
                tableExists = true;
                String columnName = rs.getString("COLUMN_NAME");
                logger.debug("Kolom ditemukan di tabel activity_log: {}", columnName);
                if (columnName.equalsIgnoreCase("id") || columnName.equalsIgnoreCase("log_id")) {
                    idColumnName = columnName;
                    idColumnFound = true;
                    logger.info("Menggunakan nama kolom ID: {}", idColumnName);
                }
            }

            if (!tableExists) {
                logger.warn("Tabel activity_log tidak ditemukan. Membuat tabel baru.");
                createTable(conn);
            } else if (!idColumnFound) {
                logger.error("Kolom id atau log_id tidak ditemukan di tabel activity_log.");
                throw new SQLException("Kolom id atau log_id tidak ditemukan di tabel activity_log.");
            }
        } catch (SQLException e) {
            logger.error("Gagal memeriksa skema tabel activity_log: {}", e.getMessage(), e);
        }
    }

    private void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE activity_log (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "action TEXT, " +
                "timestamp TEXT)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
            logger.info("Tabel activity_log berhasil dibuat.");
            idColumnName = "id"; // Pastikan menggunakan 'id' setelah tabel dibuat
        }
    }

    public List<ActivityLog> getAllLogs() throws SQLException {
        List<ActivityLog> logs = new ArrayList<>();
        String sql = String.format("SELECT %s, user_id, action, timestamp FROM activity_log ORDER BY timestamp DESC", idColumnName);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LocalDateTime timestamp = null;
                String timestampStr = rs.getString("timestamp");
                if (timestampStr != null && !timestampStr.isEmpty()) {
                    try {
                        // Bersihkan .0000 di akhir jika ada
                        timestampStr = timestampStr.replaceAll("\\.0000$", "");
                        timestamp = LocalDateTime.parse(timestampStr);
                    } catch (DateTimeParseException e) {
                        logger.error("Gagal parsing timestamp: {}. Menggunakan null.", timestampStr, e);
                        timestamp = null;
                    }
                } else {
                    logger.warn("Timestamp kosong atau null untuk log ID: {}", rs.getInt(idColumnName));
                }

                ActivityLog log = new ActivityLog(
                        rs.getInt(idColumnName),
                        rs.getInt("user_id") == 0 ? null : rs.getInt("user_id"),
                        rs.getString("action"),
                        timestamp
                );
                logs.add(log);
            }
        } catch (SQLException e) {
            logger.error("Gagal mengambil log aktivitas: {}", e.getMessage(), e);
            throw new SQLException("Gagal mengambil log aktivitas: " + e.getMessage(), e);
        }

        logger.info("Berhasil mengambil {} log aktivitas.", logs.size());
        return logs;
    }

    public void insertLog(ActivityLog log) {
        String sql = String.format("INSERT INTO activity_log (user_id, action, timestamp) VALUES (?, ?, ?)");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (log.getUserId() != null) {
                stmt.setInt(1, log.getUserId());
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }
            stmt.setString(2, log.getAction());
            if (log.getTimestamp() != null) {
                stmt.setString(3, log.getTimestamp().format(TIMESTAMP_FORMATTER));
            } else {
                stmt.setNull(3, java.sql.Types.VARCHAR);
            }

            stmt.executeUpdate();
            logger.info("Berhasil menyisipkan log: UserID={}, Action={}, Timestamp={}",
                    log.getUserId(), log.getAction(), log.getTimestamp());
        } catch (SQLException e) {
            logger.error("Gagal menyisipkan log: {}", e.getMessage(), e);
        }
    }
}