package model;

import java.time.LocalDate;

public class User {
    private int id;                  // Kullanıcının sistemdeki benzersiz ID'si
    private String name;            // Kullanıcının adı
    private String email;           // Giriş için kullanılan e-posta
    private String passwordHash;    // SHA-256 ile şifrelenmiş parola
    private String role;            // CUSTOMER, CORPORATE gibi roller
    private LocalDate birthdate;    // Doğum tarihi (yaş kontrolü için)

    public User() {}

    public User(int id, String name, String email, String passwordHash, String role, LocalDate birthdate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.birthdate = birthdate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDate getBirthdate() { return birthdate; }
    public void setBirthdate(LocalDate birthdate) { this.birthdate = birthdate; }
}
