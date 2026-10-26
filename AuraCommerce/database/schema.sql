-- ====================================================================
-- AURA COMMERCE - Enterprise Cloud POS & ERP Database Schema
-- Database Name: online_shopping
-- Compatible with: MySQL 5.7+ / MySQL 8.0+ / MariaDB 10.3+
-- ====================================================================

CREATE DATABASE IF NOT EXISTS `online_shopping`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE `online_shopping`;

-- Disable foreign key checks for clean script execution
SET FOREIGN_KEY_CHECKS = 0;

-- 1. DROP EXISTING VIEWS
DROP VIEW IF EXISTS `customer_order_view`;
DROP VIEW IF EXISTS `product_view`;
DROP VIEW IF EXISTS `payment_view`;
DROP VIEW IF EXISTS `shipping_view`;

-- 2. DROP EXISTING TABLES
DROP TABLE IF EXISTS `review`;
DROP TABLE IF EXISTS `payment`;
DROP TABLE IF EXISTS `order_item`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `shipping`;
DROP TABLE IF EXISTS `address`;
DROP TABLE IF EXISTS `customer`;
DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `supplier`;
DROP TABLE IF EXISTS `category`;

SET FOREIGN_KEY_CHECKS = 1;

-- ====================================================================
-- TABLE DEFINITIONS
-- ====================================================================

-- Category Table
CREATE TABLE `category` (
    `category_id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Supplier Table
CREATE TABLE `supplier` (
    `supplier_id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(150) NOT NULL,
    `contact_email` VARCHAR(150),
    `phone` VARCHAR(30),
    `address` TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Product Table
CREATE TABLE `product` (
    `product_id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(150) NOT NULL,
    `description` TEXT,
    `category_id` INT,
    `supplier_id` INT,
    `stock_qty` INT NOT NULL DEFAULT 0,
    `price` DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    INDEX `idx_product_category` (`category_id`),
    INDEX `idx_product_supplier` (`supplier_id`),
    CONSTRAINT `fk_product_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT `fk_product_supplier` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`supplier_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Customer Table
CREATE TABLE `customer` (
    `customer_id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(150) NOT NULL,
    `email` VARCHAR(150) UNIQUE,
    `phone` VARCHAR(30),
    `registered_on` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `gender` VARCHAR(10) DEFAULT 'Other'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Customer Address Table
CREATE TABLE `address` (
    `address_id` INT AUTO_INCREMENT PRIMARY KEY,
    `customer_id` INT,
    `street` VARCHAR(255),
    `city` VARCHAR(100),
    `state` VARCHAR(100),
    `pincode` VARCHAR(20),
    INDEX `idx_address_customer` (`customer_id`),
    CONSTRAINT `fk_address_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Shipping / Logistics Table
CREATE TABLE `shipping` (
    `shipment_id` INT AUTO_INCREMENT PRIMARY KEY,
    `address_id` INT,
    `carrier` VARCHAR(100) DEFAULT 'Standard',
    `tracking_no` VARCHAR(100),
    `status` VARCHAR(50) DEFAULT 'Pending',
    `shipment_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_shipping_address` (`address_id`),
    CONSTRAINT `fk_shipping_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Orders Table
CREATE TABLE `orders` (
    `order_id` INT AUTO_INCREMENT PRIMARY KEY,
    `customer_id` INT,
    `shipment_id` INT,
    `total_amount` DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    `status` VARCHAR(50) DEFAULT 'Processing',
    `order_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_orders_customer` (`customer_id`),
    INDEX `idx_orders_shipment` (`shipment_id`),
    CONSTRAINT `fk_orders_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `fk_orders_shipment` FOREIGN KEY (`shipment_id`) REFERENCES `shipping` (`shipment_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Order Item Line Items
CREATE TABLE `order_item` (
    `order_id` INT NOT NULL,
    `product_id` INT NOT NULL,
    `quantity` INT NOT NULL DEFAULT 1,
    `unit_price` DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    PRIMARY KEY (`order_id`, `product_id`),
    CONSTRAINT `fk_orderitem_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_orderitem_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Payment Transactions
CREATE TABLE `payment` (
    `payment_id` INT AUTO_INCREMENT PRIMARY KEY,
    `order_id` INT,
    `amount` DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    `payment_type` VARCHAR(50) DEFAULT 'Cash',
    `status` VARCHAR(50) DEFAULT 'Completed',
    `payment_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_payment_order` (`order_id`),
    CONSTRAINT `fk_payment_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Product Reviews & Ratings
CREATE TABLE `review` (
    `review_id` INT AUTO_INCREMENT PRIMARY KEY,
    `customer_id` INT,
    `product_id` INT,
    `rating` INT CHECK (`rating` >= 1 AND `rating` <= 5),
    `comment` TEXT,
    `review_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_review_customer` (`customer_id`),
    INDEX `idx_review_product` (`product_id`),
    CONSTRAINT `fk_review_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_review_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ====================================================================
-- BUSINESS INTELLIGENCE & ANALYTICS VIEWS
-- ====================================================================

-- 1. Customer Orders Master View
CREATE OR REPLACE VIEW `customer_order_view` AS
SELECT 
    o.order_id,
    c.customer_id,
    c.name AS customer_name,
    c.email AS customer_email,
    o.order_date,
    o.total_amount,
    o.status AS order_status,
    s.carrier,
    s.tracking_no,
    s.status AS shipment_status
FROM orders o
LEFT JOIN customer c ON o.customer_id = c.customer_id
LEFT JOIN shipping s ON o.shipment_id = s.shipment_id;

-- 2. Product Catalog & Stock View
CREATE OR REPLACE VIEW `product_view` AS
SELECT 
    p.product_id,
    p.name AS product_name,
    p.description,
    p.price,
    p.stock_qty,
    c.name AS category_name,
    s.name AS supplier_name,
    s.contact_email AS supplier_email
FROM product p
LEFT JOIN category c ON p.category_id = c.category_id
LEFT JOIN supplier s ON p.supplier_id = s.supplier_id;

-- 3. Payment Transactions View
CREATE OR REPLACE VIEW `payment_view` AS
SELECT 
    p.payment_id,
    p.order_id,
    o.customer_id,
    c.name AS customer_name,
    p.amount,
    p.payment_type,
    p.status AS payment_status,
    p.payment_date
FROM payment p
LEFT JOIN orders o ON p.order_id = o.order_id
LEFT JOIN customer c ON o.customer_id = c.customer_id;

-- 4. Logistics & Dispatch View
CREATE OR REPLACE VIEW `shipping_view` AS
SELECT 
    s.shipment_id,
    s.tracking_no,
    s.carrier,
    s.status AS shipping_status,
    s.shipment_date,
    o.order_id,
    c.name AS customer_name,
    CONCAT(a.street, ', ', a.city, ', ', a.state, ' - ', a.pincode) AS delivery_address
FROM shipping s
LEFT JOIN orders o ON s.shipment_id = o.shipment_id
LEFT JOIN customer c ON o.customer_id = c.customer_id
LEFT JOIN address a ON s.address_id = a.address_id;
