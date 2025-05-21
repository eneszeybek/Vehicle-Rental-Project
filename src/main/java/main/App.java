package main;

import model.User;
import model.Vehicle;
import service.AuthService;
import service.RentalService;
import service.VehicleService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {

    // Admin bilgileri sabit tanımlanır
    private static final String ADMIN_EMAIL = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    // Veritabanı bağlantısı kurulur
    private static Connection connect() throws Exception {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/vehicle_rental", "postgres", "1234");
    }

    public static void main(String[] args) {
        try (Connection conn = connect(); Scanner scanner = new Scanner(System.in)) {
            AuthService authService = new AuthService(conn);
            Optional<User> loggedInUser = Optional.empty();

            while (true) {
                // Ana menü
                System.out.println("\n==============================");
                System.out.println("   VEHICLE RENTAL SYSTEM");
                System.out.println("==============================");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choice: ");

                int choice = Integer.parseInt(scanner.nextLine());

                if (choice == 1) {
                    // Kayıt ekranı
                    System.out.print("Adınız: ");
                    String name = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Şifre: ");
                    String password = scanner.nextLine();
                    System.out.print("Müşteri Türü (1: CUSTOMER, 2: CORPORATE): ");
                    String role = scanner.nextLine().equals("2") ? "CORPORATE" : "CUSTOMER";
                    System.out.print("Doğum Tarihi (YYYY-MM-DD): ");
                    LocalDate birthdate = LocalDate.parse(scanner.nextLine());

                    authService.register(name, email, password, role, birthdate);

                } else if (choice == 2) {
                    // Giriş ekranı
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Şifre: ");
                    String password = scanner.nextLine();

                    // Admin girişi kontrolü
                    if (email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {
                        System.out.println("Admin girişi başarılı.");
                        runAdminPanel(scanner, conn);
                        continue;
                    }

                    // Müşteri girişi
                    Optional<User> userOpt = authService.login(email, password);
                    if (userOpt.isPresent()) {
                        User user = userOpt.get();
                        runCustomerPanel(scanner, conn, user);
                    }

                } else if (choice == 3) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Admin paneli menüsü
    private static void runAdminPanel(Scanner scanner, Connection conn) {
        VehicleService vehicleService = new VehicleService(conn);

        while (true) {
            System.out.println("\n--- ADMIN PANEL ---");
            System.out.println("1. Araç Ekle");
            System.out.println("2. Araçları Listele");
            System.out.println("3. Çıkış");
            System.out.print("Seçim: ");
            int adminChoice = Integer.parseInt(scanner.nextLine());

            if (adminChoice == 1) {
                // Araç ekleme
                System.out.print("Tip (Car / Motorcycle / Helicopter): ");
                String type = scanner.nextLine();
                System.out.print("Marka: ");
                String brand = scanner.nextLine();
                System.out.print("Model: ");
                String model = scanner.nextLine();
                System.out.print("Fiyat: ");
                double price = Double.parseDouble(scanner.nextLine());
                System.out.print("Kategori: ");
                String category = scanner.nextLine();

                vehicleService.addVehicle(type, brand, model, price, category);

            } else if (adminChoice == 2) {
                // Araçları listele
                vehicleService.listAllVehicles()
                        .forEach(v -> System.out.println(v.getId() + " - " + v.getBrand() + " " + v.getModel() + " (" + v.getType() + ") - ₺" + v.getPrice()));

            } else {
                break;
            }
        }
    }

    // Müşteri paneli menüsü
    private static void runCustomerPanel(Scanner scanner, Connection conn, User user) {
        VehicleService vehicleService = new VehicleService(conn);
        RentalService rentalService = new RentalService(conn);

        while (true) {
            System.out.println("\n--- MÜŞTERİ PANELİ ---");
            System.out.println("1. Araçları Listele (Sayfalı)");
            System.out.println("2. Araçları Filtrele (Tipine göre)");
            System.out.println("3. Araç Kirala");
            System.out.println("4. Kiralama Geçmişim");
            System.out.println("5. Çıkış");
            System.out.print("Seçim: ");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 1) {
                System.out.print("Sayfa numarası girin: ");
                int page = Integer.parseInt(scanner.nextLine());
                int pageSize = 5;
                vehicleService.listAllVehicles().stream()
                        .skip((page - 1) * pageSize)
                        .limit(pageSize)
                        .forEach(v -> System.out.println(v.getId() + " - " + v.getBrand() + " " + v.getModel() + " (" + v.getType() + ") - ₺" + v.getPrice()));

            } else if (choice == 2) {
                System.out.print("Filtrelemek istediğiniz araç tipi (Car / Motorcycle / Helicopter): ");
                String type = scanner.nextLine();
                List<Vehicle> filtered = vehicleService.listAvailableVehiclesByType(type);
                if (filtered.isEmpty()) {
                    System.out.println("Uygun araç bulunamadı.");
                } else {
                    filtered.forEach(v -> System.out.println(v.getId() + " - " + v.getBrand() + " " + v.getModel() + " (" + v.getType() + ") - ₺" + v.getPrice()));
                }

            } else if (choice == 3) {
                System.out.print("Kiralamak istediğiniz araç ID'si: ");
                int vehicleId = Integer.parseInt(scanner.nextLine());
                Vehicle vehicle = vehicleService.listAllVehicles().stream()
                        .filter(v -> v.getId() == vehicleId)
                        .findFirst()
                        .orElse(null);
                if (vehicle == null) {
                    System.out.println("Araç bulunamadı.");
                    continue;
                }
                System.out.print("Kiralama süresi tipi (HOURLY, DAILY, WEEKLY, MONTHLY): ");
                String durationType = scanner.nextLine();
                System.out.print("Başlangıç tarihi (YYYY-MM-DD HH:MM): ");
                DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("yyyy-MM-dd['T'][' ']HH:mm");
                LocalDateTime start = LocalDateTime.parse(scanner.nextLine(), formatterStart);
                System.out.print("Bitiş tarihi (YYYY-MM-DD HH:MM): ");
                DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("yyyy-MM-dd['T'][' ']HH:mm");
                LocalDateTime end = LocalDateTime.parse(scanner.nextLine(), formatterEnd);

                rentalService.rentVehicle(user, vehicle, durationType, start, end);

            } else if (choice == 4) {
                rentalService.getFormattedRentalHistory(user.getId()).forEach(System.out::println);

            } else {
                break;
            }
        }
    }
}
