# ShopWave — E-Commerce Web Application

A full-stack e-commerce platform built with Java Spring Boot.

## Tech Stack
- Java 17
- Spring Boot 3.2
- Spring Security 6
- Hibernate / JPA
- MySQL 8
- Thymeleaf
- Maven

## Features
- Role-based authentication (Admin / Customer / Delivery Partner)
- Product and category management
- Shopping cart with size selection for fashion products
- Multi-step checkout with delivery slot selection
- Automated estimated delivery date calculation
- Order tracking with visual progress bar
- Delivery partner portal with exclusive order assignment
- Return and exchange request workflow
- Printable invoice generation
- Admin dashboard with live statistics

## Setup Instructions

### Prerequisites
- Java 17
- MySQL 8
- Maven

### Steps
1. Clone the repository
   git clone https://github.com/YOUR_USERNAME/ShopWave.git

2. Create MySQL database
   CREATE DATABASE shop;

3. Update application.properties with your MySQL credentials

4. Run the application
   mvn spring-boot:run

5. Open browser at http://localhost:8084

## Default Credentials
- Admin: admin@shopwave.com / admin123
- Customer: Register a new account
- Delivery Partner: Register a new account

## Screenshots
## Landing Page
![Landing Page](screenshots/landing.png)

## Customer Login
![Customer Login](screenshots/customer_login.png)

## Home Page
![Home Page](screenshots/home.png)

## New Arrivals
![New Arrivals](screenshots/new_arrivals.png)

## Product 
![Product](screenshots/product_details.png)

## My Cart
![My Cart](screenshots/my_cart.png)

## Delivery Slot
![Delivery Slot](screenshots/delivery_slot_window.png)

## Payment Methods
![Payment Methods](screenshots/payment_method.png)

## Checkout
![Checkout](screenshots/checkout_order_review.png)

## Bill/Invoice
![Bill/Invoice](screenshots/order_bill.png)

## Track Order
![Track Order](screenshots/order_track.png)

## Admin Login
![Admin Login](screenshots/admin_login.png)

## Admin Dashboard
![Admin Dashboard](screenshots/admin_dashboard.png)

## Category Management
![Category Management](screenshots/category_management.png)

## Product Management
![Product Management](screenshots/product_management.png)

## View Orders
![View Orders](screenshots/orders.png)

## Assign Delivery
![Assign Delivery](screenshots/Assign_Delivery.png)

## Delivery Login
![Delivery Login](screenshots/delivery_login.png)

## Delivery Details
![Delivery Details](screenshots/delivery_orders.png)
