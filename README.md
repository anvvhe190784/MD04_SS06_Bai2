# MD04_SS06_Bai2 - Patient Management Service (patient-service)

## [Bài tập 2 - Khá] Service Quản lý Bệnh nhân (Patient-Service)

### 1. Mục tiêu
- **Kiến thức**: Hiểu cách một Microservice kết nối cơ sở dữ liệu quan hệ (PostgreSQL) thông qua Spring Data JPA và tự động đăng ký vào Service Registry (Eureka Server).
- **Kỹ năng**: Khởi tạo project `patient-service`, cấu hình JPA PostgreSQL, triển khai Entity `Patient`, xây dựng API RESTful và đăng ký Eureka Client.

---

### 2. Cấu trúc thư mục dự án
```text
MD04_SS06_Bai2/
├── .gitignore
├── README.md
└── patient-service/
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

### 3. Thực thể Patient (Entity)
| Thuộc tính | Kiểu dữ liệu | Mô tả |
| :--- | :--- | :--- |
| `id` | `Long` | Khóa chính, tự sinh (Primary Key, Identity) |
| `fullName` | `String` | Họ và tên bệnh nhân |
| `dateOfBirth` | `LocalDate` | Ngày sinh bệnh nhân |
| `gender` | `String` | Giới tính |
| `phoneNumber` | `String` | Số điện thoại liên lạc |
| `address` | `String` | Địa chỉ thường trú |
| `medicalHistory`| `String` (TEXT) | Tiền sử bệnh lý (dị ứng, bệnh nền...) |

---

### 4. Cấu hình chi tiết (`application.properties`)
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

> **Lưu ý về mật khẩu PostgreSQL**:
> Cấu hình mặc định theo bài thực hành là `123456`. Nếu PostgreSQL trên máy bạn sử dụng mật khẩu khác (ví dụ `admin`), hãy cập nhật dòng `spring.datasource.password` cho phù hợp.

---

### 5. Chi tiết API nghiệp vụ

#### ➕ Thêm mới bệnh nhân
- **Endpoint**: `POST /api/v1/patients`
- **Headers**: `Content-Type: application/json`
- **Request Body mẫu**:
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

- **Response Mẫu (HTTP 201 Created)**:
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

#### 📋 Lấy danh sách bệnh nhân
- **Endpoint**: `GET /api/v1/patients`
- **Response**: Trả về mảng danh sách tất cả các bệnh nhân đã lưu.

---

### 6. Hướng dẫn chạy & Kiểm thử

1. **Khởi động Eureka Server (Bài 1)**:
   Chạy project `medical-discovery-server` tại port `8761`.

2. **Đảm bảo database PostgreSQL đã tồn tại**:
   ```sql
   CREATE DATABASE patient_db;
   ```

3. **Khởi chạy `patient-service`**:
   Tại thư mục `patient-service`:
   ```bash
   mvn spring-boot:run
   ```

4. **Kiểm tra Eureka Dashboard**:
   Truy cập [http://localhost:8761](http://localhost:8761), tại mục **Instances currently registered with Eureka**, bạn sẽ thấy `PATIENT-SERVICE` với IP/port `8081` đã được đăng ký thành công!

5. **Gọi API thêm mới bệnh nhân (cURL / Postman)**:
   ```bash
   curl -X POST http://localhost:8081/api/v1/patients \
     -H "Content-Type: application/json" \
     -d '{
       "fullName": "Nguyễn Văn A",
       "dateOfBirth": "1995-08-20",
       "gender": "Nam",
       "phoneNumber": "0987654321",
       "address": "Hà Nội",
       "medicalHistory": "Không có tiền sử bệnh lý"
     }'
   ```

---

### 7. Hướng dẫn đẩy lên GitHub
Tại thư mục `MD04_SS06_Bai2`:
```bash
git init
git add .
git commit -m "feat: setup patient-service with PostgreSQL and Eureka Client"
git branch -M main
git remote add origin https://github.com/anvvhe190784/MD04_SS06_Bai2.git
git push -u origin main
```
