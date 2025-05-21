package model;

public class Vehicle {
    private int id;             // Araç ID'si (benzersiz)
    private String type;        // Araç tipi (Car, Motorcycle, Helicopter)
    private String brand;       // Marka
    private String model;       // Model
    private double price;       // Araç fiyatı
    private String category;    // Araç kategorisi (Ekonomi, Lüks vb.)

    public Vehicle() {}

    public Vehicle(int id, String type, String brand, String model, double price, String category) {
        this.id = id;
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.category = category;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}