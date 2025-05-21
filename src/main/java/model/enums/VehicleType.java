package model.enums;

// Araç tiplerini temsil eden enum
public enum VehicleType {
    CAR,
    MOTORCYCLE,
    HELICOPTER;

    // Girilen string geçerli bir araç tipi mi kontrol eder
    public static boolean isValid(String type) {
        try {
            VehicleType.valueOf(type.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
