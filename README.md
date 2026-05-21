# Mini Food Ordering System - Server

Backend cho hệ thống đặt món ăn nội bộ theo kiến trúc dịch vụ (ShopeeFood mini). Mỗi miền nghiệp vụ chạy như một service Spring Boot riêng và giao tiếp qua REST API.

## Thành viên

- Trần Long Vũ (22717471)
- Hồ Quang Nhân (22715701)

## Phân công

- Trần Long Vũ: service-user, service-payment_notification
- Hồ Quang Nhân: service-food, service-order

## Dịch vụ và API

### User Service (service-user)

- POST /register
- POST /login
- GET /users

Ghi chú: xác thực đơn giản (JWT tùy chọn), lưu in-memory hoặc H2.

### Food Service (service-food)

- GET /foods
- POST /foods
- PUT /foods/{id}
- DELETE /foods/{id}

Ghi chú: không cần auth phức tạp, có dữ liệu seed sẵn.

### Order Service (service-order)

- POST /orders
- GET /orders

Ghi chú: khi tạo đơn, gọi Food Service để lấy thông tin món và User Service để xác thực người dùng.

### Payment + Notification (service-payment_notification)

- POST /payments

Ghi chú: cập nhật trạng thái đơn hàng bằng cách gọi Order Service, sau đó gửi thông báo (console log hoặc REST call).

## Kiến trúc

- Service-Based Architecture
- Mỗi tính năng là một Spring Boot service độc lập
- Giao tiếp qua REST API (HTTP)
- API Gateway (tùy chọn)

## Triển khai LAN (Demo)

Mỗi service chạy trên một máy khác nhau trong cùng LAN:

- User Service: 192.168.x.x:8081
- Food Service: 192.168.x.x:8082
- Order Service: 192.168.x.x:8083
- Payment Service: 192.168.x.x:8084

Lưu ý: cấu hình IP LAN thật và CORS. Không dùng localhost chéo máy.

## Chạy dịch vụ

Từ thư mục từng service:

```bash
./mvnw spring-boot:run
```

Trên Windows (PowerShell):

```bash
./mvnw.cmd spring-boot:run
```

## Kịch bản test demo bắt buộc

1. Người dùng đăng ký và đăng nhập
2. Xem danh sách món
3. Thêm vào giỏ và tạo đơn
4. Thanh toán
5. Nhận thông báo