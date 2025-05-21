package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    // SHA-256 algoritması ile bir şifreyi hash'leyen yardımcı metod
    public static String hashPassword(String password) {
        try {
            // SHA-256 algoritması nesnesi oluştur
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Şifreyi byte dizisine dönüştürüp hash işlemi uygula
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Hash sonuçlarını hexadecimal string olarak birleştir
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = String.format("%02x", b); // byte'i 2 basamaklı hex string'e çevir
                hexString.append(hex);
            }

            return hexString.toString(); // Sonuç: SHA-256 ile hash'lenmiş şifre

        } catch (NoSuchAlgorithmException e) {
            // SHA-256 algoritması JVM'de tanımlı değilse exception fırlatılır
            throw new RuntimeException("SHA-256 algoritması bulunamadı!", e);
        }
    }
}
