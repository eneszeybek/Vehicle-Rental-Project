Araç Kiralama Uygulaması (Java + PostgreSQL)

Bu proje, terminal tabanlı bir araç kiralama uygulamasıdır. Kullanıcılar kayıt olabilir, giriş yapabilir, araçları listeleyebilir ve kiralayabilir. Yönetici (admin) kullanıcılar sistem üzerinden araç tanımlayabilir.

🚀 Özellikler

Kullanıcı kayıt ve giriş (şifreler SHA-256 ile şifrelenir)

Admin arayüzü: Araç tanımlama ve listeleme

Müşteri arayüzü:

Araçları listeleme (sayfalı)

Araç tipine göre filtreleme

Kiralama işlemi (süre ve yaş koşullarına göre)

Kiralama geçmişini görüntüleme

Terminal üzerinden menülü yapı

Katmanlı mimari (Model, DAO, Service, Main)

PostgreSQL veritabanı kullanımı (JDBC üzerinden bağlantı)

🧱 Teknolojiler

Java 21

PostgreSQL 15+

JDBC

Maven (bağımlılık yönetimi)

🛠 Kurulum Adımları

PostgreSQL Veritabanı Oluştur:

```java
CREATE DATABASE vehicle_rental;
```

Tabloları Oluştur:

```java
-- Kullanıcılar tablosu
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    role VARCHAR(20) NOT NULL,
    birthdate DATE NOT NULL
);

-- Araçlar tablosu
CREATE TABLE vehicles (
    id SERIAL PRIMARY KEY,
    type VARCHAR(20) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    price NUMERIC(15, 2) NOT NULL,
    category VARCHAR(50) NOT NULL
);

-- Kiralamalar tablosu
CREATE TABLE rentals (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    vehicle_id INTEGER REFERENCES vehicles(id),
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    duration_type VARCHAR(20) NOT NULL,
    deposit NUMERIC(15, 2) NOT NULL
);
```

⚠️ Veritabanı Bağlantı Ayarı

`App.java` dosyasında aşağıdaki satırda yer alan veritabanı bilgilerini kendi sisteminize göre güncelleyin:

```java
DriverManager.getConnection("jdbc:postgresql://localhost:5432/vehicle_rental", "postgres", "1234");
```

🧪 Admin Giriş Bilgileri

`App.java` dosyasında aşağıdaki satırlarda yer alan admin giriş bilgilerini tercihinize göre güncelleyin:

```java
private static final String ADMIN_EMAIL = "admin";
private static final String ADMIN_PASSWORD = "admin";
```

🔐 Kurallar ve Koşullar

Kurumsal kullanıcılar sadece aylık kiralama yapabilir.

Fiyatı 2 milyon TL'yi aşan araçlar sadece 30 yaş üstü kullanıcılar tarafından kiralanabilir ve bu kullanıcılar araç bedelinin %10'u kadar depozito öder.

Kiralama süreleri: HOURLY, DAILY, WEEKLY, MONTHLY

👤 Rol Bazlı Erişim

ADMIN → Araç ekleyebilir ve tüm araçları görebilir.

CUSTOMER, CORPORATE → Kiralama yapabilir, araçları filtreleyebilir, geçmişi görebilir.

📁 Proje Yapısı

```
src/
└── main/
    └── java/
        ├── main/
        │   └── App.java
        ├── model/
        │   ├── User.java
        │   ├── Vehicle.java
        │   ├── Rental.java
        │   └── enums/
        │       ├── Role.java
        │       ├── DurationType.java
        │       └── VehicleType.java
        ├── dao/
        │   ├── UserDao.java
        │   ├── VehicleDao.java
        │   └── RentalDao.java
        ├── service/
        │   ├── AuthService.java
        │   ├── VehicleService.java
        │   └── RentalService.java
        └── util/
            └── PasswordUtil.java
```
