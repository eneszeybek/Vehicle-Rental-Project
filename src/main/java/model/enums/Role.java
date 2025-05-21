package model.enums;

// Kullanıcı rollerini temsil eden enum
public enum Role {
    ADMIN,
    CUSTOMER,
    CORPORATE;

    // String ifadeyi enum'a dönüştürür (büyük/küçük harf duyarsız)
    public static Role fromString(String value) {
        return Role.valueOf(value.toUpperCase());
    }
}