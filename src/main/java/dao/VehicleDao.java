package dao;

import model.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDao {
    private final Connection connection; // Veritabanı bağlantısı

    public VehicleDao(Connection connection) {
        this.connection = connection;
    }

    // Yeni araç verisini veritabanına kaydeder
    public void save(Vehicle vehicle) throws SQLException {
        String sql = "INSERT INTO vehicles (type, brand, model, price, category) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, vehicle.getType());
            stmt.setString(2, vehicle.getBrand());
            stmt.setString(3, vehicle.getModel());
            stmt.setDouble(4, vehicle.getPrice());
            stmt.setString(5, vehicle.getCategory());
            stmt.executeUpdate();
        }
    }

    // Belirli bir araç türünde, şu anda kiralanmamış araçları listeler
    public List<Vehicle> findAvailableByType(String type) throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = """
            SELECT * FROM vehicles v
            WHERE NOT EXISTS (
                SELECT 1 FROM rentals r
                WHERE r.vehicle_id = v.id
                AND r.end_date > now()
            )
            AND v.type ILIKE ?
            ORDER BY v.id
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vehicles.add(resultSetToVehicle(rs));
                }
            }
        }
        return vehicles;
    }

    // Tüm araçları listeler
    public List<Vehicle> findAll() throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles ORDER BY id";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vehicles.add(resultSetToVehicle(rs));
            }
        }
        return vehicles;
    }

    // ResultSet nesnesinden Vehicle nesnesi üretir
    private Vehicle resultSetToVehicle(ResultSet rs) throws SQLException {
        return new Vehicle(
                rs.getInt("id"),
                rs.getString("type"),
                rs.getString("brand"),
                rs.getString("model"),
                rs.getDouble("price"),
                rs.getString("category")
        );
    }
}
