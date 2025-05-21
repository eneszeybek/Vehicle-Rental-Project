package service;

import dao.RentalDao;
import model.Rental;
import model.User;
import model.Vehicle;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class RentalService {
    private final RentalDao rentalDao; // DAO katmanı

    public RentalService(Connection connection) {
        this.rentalDao = new RentalDao(connection);
    }

    // Kiralama işlemi gerçekleştiren metod
    public boolean rentVehicle(User user, Vehicle vehicle, String durationType,
                               LocalDateTime startDate, LocalDateTime endDate) {
        try {
            // Kullanıcının yaşını hesapla (güncel yıl - doğum yılı)
            int age = LocalDateTime.now().getYear() - user.getBirthdate().getYear();

            // Kurumsal kullanıcılar sadece aylık kiralama yapabilir
            if (user.getRole().equalsIgnoreCase("CORPORATE") &&
                    !durationType.equalsIgnoreCase("MONTHLY")) {
                System.out.println("Kurumsal kullanıcılar sadece aylık kiralama yapabilir.");
                return false;
            }

            double deposit = 0;
            // Eğer araç fiyatı 2 milyon TL'den fazlaysa yaş ve depozito kontrolü yapılır
            if (vehicle.getPrice() > 2_000_000) {
                if (age < 30) {
                    System.out.println("Bu araç yalnızca 30 yaşından büyük kullanıcılar tarafından kiralanabilir.");
                    return false;
                }
                // Depozito: araç fiyatının %10'u
                deposit = vehicle.getPrice() * 0.10;
                System.out.printf("Bu araç için ₺%.2f depozito alınacaktır.%n", deposit);
            }

            // Kiralama nesnesi oluşturulur
            Rental rental = new Rental(
                    0,
                    user.getId(),
                    vehicle.getId(),
                    startDate,
                    endDate,
                    durationType.toUpperCase(),
                    deposit
            );

            // Kiralama bilgisi veritabanına kaydedilir
            rentalDao.save(rental);
            System.out.println("Kiralama işlemi başarıyla tamamlandı.");
            return true;

        } catch (SQLException e) {
            // Veritabanı hatası durumunda işlem başarısız olur
            e.printStackTrace();
            return false;
        }
    }

    // Kullanıcının geçmiş kiralama kayıtlarını araç bilgileri ile birlikte getirir
    public List<String> getFormattedRentalHistory(int userId) {
        try {
            return rentalDao.findRentalHistoryWithVehicle(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of("Geçmiş alınamadı.");
        }
    }
}

