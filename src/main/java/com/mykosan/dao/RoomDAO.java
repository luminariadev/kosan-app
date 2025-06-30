package com.mykosan.dao;

import com.mykosan.model.Room;
import com.mykosan.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_name"),
                        rs.getInt("price"),
                        rs.getBoolean("is_occupied")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil data kamar: " + e.getMessage(), e);
        }
        return rooms;
    }

    public Room getRoomById(int roomId) {
        String sql = "SELECT * FROM rooms WHERE room_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_name"),
                        rs.getInt("price"),
                        rs.getBoolean("is_occupied")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil kamar berdasarkan ID: " + e.getMessage(), e);
        }
        return null;
    }

    public boolean insertRoom(Room room) {
        String sql = "INSERT INTO rooms (room_name, price, is_occupied) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, room.getRoomName());
            ps.setInt(2, room.getPrice());
            ps.setBoolean(3, room.isOccupied());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menambahkan kamar: " + e.getMessage(), e);
        }
    }

    public boolean updateRoom(Room room) {
        String sql = "UPDATE rooms SET room_name = ?, price = ?, is_occupied = ? WHERE room_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, room.getRoomName());
            ps.setInt(2, room.getPrice());
            ps.setBoolean(3, room.isOccupied());
            ps.setInt(4, room.getRoomId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Gagal memperbarui kamar: " + e.getMessage(), e);
        }
    }

    public boolean deleteRoom(int roomId) {
        String sql = "DELETE FROM rooms WHERE room_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menghapus kamar: " + e.getMessage(), e);
        }
    }
}
