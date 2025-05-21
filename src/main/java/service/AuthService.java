package service;

import dao.UserDao;
import model.User;
import util.PasswordUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class AuthService {
    private final UserDao userDao; // Kullanıcı DAO sınıfı

    public AuthService(Connection connection) {
        this.userDao = new UserDao(connection);
    }

    // Yeni kullanıcı kaydı oluşturur
    public boolean register(String name, String email, String password, String role, LocalDate birthdate) {
        try {
            // E-posta zaten kayıtlı mı kontrol edilir
            if (userDao.findByEmail(email).isPresent()) {
                System.out.println("Bu e-posta zaten kayıtlı.");
                return false;
            }

            // Şifre SHA-256 ile hashlenir
            String hash = PasswordUtil.hashPassword(password);
            User user = new User(0, name, email, hash, role.toUpperCase(), birthdate);
            userDao.save(user);
            System.out.println("Kayıt başarılı.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Giriş işlemi yapar ve kullanıcıyı döner (başarılıysa)
    public Optional<User> login(String email, String password) {
        try {
            Optional<User> userOpt = userDao.findByEmail(email);
            if (userOpt.isPresent()) {
                String inputHash = PasswordUtil.hashPassword(password);
                if (userOpt.get().getPasswordHash().equals(inputHash)) {
                    System.out.println("Giriş başarılı. Hoş geldiniz " + userOpt.get().getName());
                    return userOpt;
                } else {
                    System.out.println("Hatalı şifre.");
                }
            } else {
                System.out.println("Kullanıcı bulunamadı.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
