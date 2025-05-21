package service;

import dao.VehicleDao;
import model.Vehicle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class VehicleService {
    private final VehicleDao vehicleDao; // DAO bağlantısı

    public VehicleService(Connection connection) {
        this.vehicleDao = new VehicleDao(connection);
    }

    // Yeni bir araç ekler
    public boolean addVehicle(String type, String brand, String model, double price, String category) {
        try {
            // Geçerli araç tipi kontrolü yapılır
            if (!type.equalsIgnoreCase("Car") &&
                    !type.equalsIgnoreCase("Motorcycle") &&
                    !type.equalsIgnoreCase("Helicopter")) {
                System.out.println("Geçersiz araç tipi! Sadece Car, Motorcycle veya Helicopter olabilir.");
                return false;
            }

            Vehicle vehicle = new Vehicle(0, type, brand, model, price, category);
            vehicleDao.save(vehicle);
            System.out.println("Araç başarıyla eklendi.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tüm araçları listeler
    public List<Vehicle> listAllVehicles() {
        try {
            return vehicleDao.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // Belirli türe ait, kiralanabilir araçları listeler
    public List<Vehicle> listAvailableVehiclesByType(String type) {
        try {
            return vehicleDao.findAvailableByType(type);
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
