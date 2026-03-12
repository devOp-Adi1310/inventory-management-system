# ✨ Smart Inventory & Supply Chain Management System

A full-stack enterprise-grade inventory management solution built with **Java Spring Boot**, **MySQL**, and **RESTful API** architecture. This system optimizes supply chain operations through real-time tracking, automated low-stock alerts, and data-driven reporting.

## 🚀 Key Features

* **Real-time Inventory Tracking:** Manage products with unique SKUs and track stock levels dynamically.
* **Secure Data Pattern:** Implements the **DTO (Data Transfer Object)** pattern to protect sensitive database structures from the frontend.
* **Advanced Pagination:** Optimized for high-scale data handling using Spring Data JPA Pagination.
* **Supply Chain Analytics:** Instant CSV report generation for inventory audits and reorder optimization.
* **Low-Stock Intelligence:** Automated alerts system to prevent out-of-stock scenarios.
* **Modern Soft-UI Dashboard:** Fully responsive, aesthetic frontend with smooth CSS animations.

## 🛠️ Tech Stack

* **Backend:** Java 17, Spring Boot 3, Spring Data JPA, Lombok
* **Database:** MySQL (Optimized with B-Tree Indexing)
* **Frontend:** HTML5, CSS3 (Modern Flexbox/Grid), Vanilla JavaScript
* **Architecture:** REST API, DTO Pattern, Global Exception Handling

## 📊 Database Optimization
The system utilizes customized SQL indexing on product SKUs to ensure $O(1)$ or $O(\log n)$ lookup speeds, even as the inventory grows to thousands of items.

## 📦 How to Run Locally

1.  **Clone the project:**
    ```bash
    git clone [https://github.com/devOp-Adi1310/inventory-management-system.git](https://github.com/devOp-Adi1310/inventory-management-system.git)
    ```
2.  **Configure MySQL:**
    Update `src/main/resources/application.properties` with your MySQL username and password.
3.  **Run the application:**
    ```bash
    ./mvnw spring-boot:run
    ```
4.  **Access the Dashboard:**
    Open `http://localhost:8080` in your browser.

---
Created by **Aditya** - *Computer Science & Engineering Student*