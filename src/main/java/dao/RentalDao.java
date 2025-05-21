package dao;

import model.Rental;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RentalDao {
    private final Connection connection; // Veritabanı bağlantısı

    public RentalDao(Connection connection) {
        this.connection = connection;
    }

    // Kiralama verisini veritabanına kaydeder
    public void save(Rental rental) throws SQLException {
        String sql = "INSERT INTO rentals (user_id, vehicle_id, start_date, end_date, duration_type, deposit) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, rental.getUserId());
            stmt.setInt(2, rental.getVehicleId());
            stmt.setTimestamp(3, Timestamp.valueOf(rental.getStartDate()));
            stmt.setTimestamp(4, Timestamp.valueOf(rental.getEndDate()));
            stmt.setString(5, rental.getDurationType());
            stmt.setDouble(6, rental.getDeposit());
            stmt.executeUpdate();
        }
    }

    // Belirli bir kullanıcıya ait geçmiş kiralamaları, araç bilgileriyle birlikte getirir
    public List<String> findRentalHistoryWithVehicle(int userId) throws SQLException {
        List<String> history = new ArrayList<>();
        String sql = """
            SELECT r.id, v.brand, v.model, v.type, r.start_date, r.end_date, r.duration_type, r.deposit
            FROM rentals r
            JOIN vehicles v ON r.vehicle_id = v.id
            WHERE r.user_id = ?
            ORDER BY r.start_date DESC
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Kiralama geçmişini formatlı şekilde oluşturur
                    String line = String.format(
                            "Kiralama ID: %d | %s %s (%s) | %s → %s | Süre: %s | Depozito: ₺%.2f",
                            rs.getInt("id"),
                            rs.getString("brand"),
                            rs.getString("model"),
                            rs.getString("type"),
                            rs.getTimestamp("start_date").toLocalDateTime(),
                            rs.getTimestamp("end_date").toLocalDateTime(),
                            rs.getString("duration_type"),
                            rs.getDouble("deposit")
                    );
                    history.add(line);
                }
            }
        }
        return history;
    }
}