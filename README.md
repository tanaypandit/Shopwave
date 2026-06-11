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
![Landing Page](Screenshots/landing.png)

## Customer Login
![Customer Login](Screenshots/customer_login.png)

## Home Page
![Home Page](Screenshots/home.png)

## New Arrivals
![New Arrivals](Screenshots/new_arrivals.png)

## Product 
![Product](Screenshots/product_details.png)

## My Cart
![My Cart](Screenshots/my_cart.png)

## Delivery Slot
![Delivery Slot](Screenshots/delivery_slot_window.png)

## Payment Methods
![Payment Methods](Screenshots/payment_method.png)

## Checkout
![Checkout](Screenshots/checkout_order_review.png)

## Bill/Invoice
![Bill/Invoice](Screenshots/order_bill.png)

## Track Order
![Track Order](Screenshots/order_track.png)

## Admin Login
![Admin Login](Screenshots/admin_login.png)

## Admin Dashboard
![Admin Dashboard](Screenshots/admin_dashboard.png)

## Category Management
![Category Management](Screenshots/category_management.png)

## Product Management
![Product Management](Screenshots/product_management.png)

## View Orders
![View Orders](Screenshots/orders.png)

## Assign Delivery
![Assign Delivery](Screenshots/Assign_Delivery.png)

## Delivery Login
![Delivery Login](Screenshots/delivery_login.png)

## Delivery Details
![Delivery Details](Screenshots/delivery_orders.png)
