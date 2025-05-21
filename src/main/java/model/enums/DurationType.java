package model.enums;

// Kiralama süresi türlerini temsil eden enum
public enum DurationType {
    HOURLY,
    DAILY,
    WEEKLY,
    MONTHLY;

    // Girilen string geçerli bir süre türü mü kontrol eder
    public static boolean isValid(String type) {
        try {
            DurationType.valueOf(type.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}