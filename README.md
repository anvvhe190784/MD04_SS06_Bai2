# MD04_SS06_Bai2 - Hệ thống Microservices Y Tế (Medical Microservices System)

## 📌 Tổng quan hệ thống
Hệ thống microservices phục vụ quản lý y tế bao gồm các dịch vụ độc lập kết nối qua Service Registry (Eureka Server):
1. **`medical-discovery-server` (Bài 1)**: Trạm điều hướng y tế - Service Registry đóng vai trò trung tâm tiếp nhận đăng ký, quản lý trạng thái các microservices tại port `8761`.
2. **`patient-service` (Bài 2)**: Dịch vụ quản lý bệnh nhân - Kết nối PostgreSQL (`patient_db`) và tự động đăng ký làm Eureka Client tại port `8081`.

---

## 🗂️ Cấu trúc thư mục dự án
```text
MD04_SS06_Bai2/
├── .gitignore
├── README.md
├── medical-discovery-server/                 # [Bài 1] Eureka Discovery Server (Port 8761)
│   ├── pom.xml
│   ├── .gitignore
│   └── src/
│       ├── main/
│       │   ├── java/com/medical/discoveryserver/
│       │   │   └── MedicalDiscoveryServerApplication.java
│       │   └── resources/
│       │       └── application.properties
│       └── test/
│           └── java/com/medical/discoveryserver/
│               └── MedicalDiscoveryServerApplicationTests.java
└── patient-service/                          # [Bài 2] Patient Management Service (Port 8081)
    ├── pom.xml
    ├── .gitignore
    └── src/
        ├── main/
        │   ├── java/com/medical/patientservice/
        │   │   ├── PatientServiceApplication.java
        │   │   ├── controller/
        │   │   │   └── PatientController.java
        │   │   ├── dto/
        │   │   │   └── PatientRequest.java
        │   │   ├── entity/
        │   │   │   └── Patient.java
        │   │   ├── repository/
        │   │   │   └── PatientRepository.java
        │   │   └── service/
        │   │       ├── PatientService.java
        │   │       └── impl/
        │   │           └── PatientServiceImpl.java
        │   └── resources/
        │       └── application.properties
        └── test/
            ├── java/com/medical/patientservice/
            │   ├── PatientServiceApplicationTests.java
            │   └── PatientControllerTest.java
            └── resources/
                └── application.properties
```

---

## ⚙️ Cấu hình chi tiết các Service

### 1. `medical-discovery-server` (`application.properties`)
```properties
server.port=8761
spring.application.name=medical-discovery-server

# Server không tự đăng ký chính mình
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false

# Tắt self-preservation để Server cập nhật trạng thái các service nhanh hơn
eureka.server.enable-self-preservation=false
```

### 2. `patient-service` (`application.properties`)
```properties
server.port=8081
spring.application.name=patient-service

# Kết nối PostgreSQL (Cần tạo sẵn DB patient_db)
spring.datasource.url=jdbc:postgresql://localhost:5432/patient_db
spring.datasource.username=postgres
spring.datasource.password=admin
spring.jpa.hibernate.ddl-auto=update

# Đăng ký vào Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

---

## 📋 Chi tiết Entity Patient & REST API

### Thực thể `Patient`
- `id` (`Long`, Primary Key, GeneratedValue IDENTITY)
- `fullName` (`String`): Họ và tên bệnh nhân
- `dateOfBirth` (`LocalDate`): Ngày sinh bệnh nhân
- `gender` (`String`): Giới tính
- `phoneNumber` (`String`): Số điện thoại liên lạc
- `address` (`String`): Địa chỉ thường trú
- `medicalHistory` (`String` TEXT): Tiền sử bệnh lý (dị ứng thuốc, bệnh nền...)

### API Endpoints
- **Thêm mới bệnh nhân**: `POST /api/v1/patients`
- **Lấy danh sách bệnh nhân**: `GET /api/v1/patients`
- **Lấy chi tiết bệnh nhân theo ID**: `GET /api/v1/patients/{id}`

#### Request Body mẫu (POST `/api/v1/patients`):
```json
{
  "fullName": "Nguyễn Văn A",
  "dateOfBirth": "1995-08-20",
  "gender": "Nam",
  "phoneNumber": "0987654321",
  "address": "123 Giải Phóng, Hai Bà Trưng, Hà Nội",
  "medicalHistory": "Tiền sử hen suyễn, dị ứng phấn hoa"
}
```

#### Response mẫu (HTTP 201 Created):
```json
{
  "id": 1,
  "fullName": "Nguyễn Văn A",
  "dateOfBirth": "1995-08-20",
  "gender": "Nam",
  "phoneNumber": "0987654321",
  "address": "123 Giải Phóng, Hai Bà Trưng, Hà Nội",
  "medicalHistory": "Tiền sử hen suyễn, dị ứng phấn hoa"
}
```

---

## 🚀 Hướng dẫn khởi chạy toàn bộ hệ thống

### Bước 1: Khởi động Service Registry (`medical-discovery-server`)
Mở terminal tại thư mục `medical-discovery-server`:
```bash
mvn spring-boot:run
```
Truy cập giao diện Eureka Dashboard tại: 👉 [http://localhost:8761](http://localhost:8761)

### Bước 2: Đảm bảo database PostgreSQL đã tồn tại
```sql
CREATE DATABASE patient_db;
```

### Bước 3: Khởi động `patient-service`
Mở một terminal khác tại thư mục `patient-service`:
```bash
mvn spring-boot:run
```

### Bước 4: Kiểm tra trạng thái kết nối
1. F5 lại trang Dashboard [http://localhost:8761](http://localhost:8761).
2. Tại bảng **Instances currently registered with Eureka**, bạn sẽ thấy `PATIENT-SERVICE` hiển thị trạng thái `UP (1) - localhost:patient-service:8081`.
3. Dùng cURL / Postman gửi request POST đến `http://localhost:8081/api/v1/patients` để kiểm tra lưu bệnh nhân thành công vào DB.
