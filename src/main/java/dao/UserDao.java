package dao;

import model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class UserDao {
    private final Connection connection; // Veritabanı bağlantısı

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    // Yeni kullanıcı kaydeder
    public void save(User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, password_hash, role, birthdate) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());
            stmt.setString(4, user.getRole());
            stmt.setDate(5, Date.valueOf(user.getBirthdate()));
            stmt.executeUpdate();
        }
    }

    // E-posta adresine göre kullanıcıyı bulur
    public Optional<User> findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password_hash"),
                            rs.getString("role"),
                            rs.getDate("birthdate").toLocalDate()
                    );
                    return Optional.of(user);
                }
                return Optional.empty();
            }
        }
    }
}

