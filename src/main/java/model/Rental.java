package model;

import java.time.LocalDateTime;

public class Rental {
    private int id;                   // Kiralama işleminin ID'si
    private int userId;               // Kiralayan kullanıcı ID'si
    private int vehicleId;            // Kiralanan araç ID'si
    private LocalDateTime startDate; // Kiralamanın başlangıç tarihi
    private LocalDateTime endDate;   // Kiralamanın bitiş tarihi
    private String durationType;     // HOURLY, DAILY, WEEKLY, MONTHLY gibi
    private double deposit;          // Depozito miktarı

    public Rental() {}

    public Rental(int id, int userId, int vehicleId, LocalDateTime startDate,
                  LocalDateTime endDate, String durationType, double deposit) {
        this.id = id;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.durationType = durationType;
        this.deposit = deposit;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public String getDurationType() { return durationType; }
    public void setDurationType(String durationType) { this.durationType = durationType; }

    public double getDeposit() { return deposit; }
    public void setDeposit(double deposit) { this.deposit = deposit; }
}