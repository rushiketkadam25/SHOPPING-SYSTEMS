-- ====================================================================
-- AURA COMMERCE - Seed & Demo Data
-- Database: online_shopping
-- ====================================================================

USE `online_shopping`;

SET FOREIGN_KEY_CHECKS = 0;

-- 1. Insert Categories
INSERT INTO `category` (`category_id`, `name`) VALUES
(1, 'Laptops & Computers'),
(2, 'Smartphones & Tablets'),
(3, 'Audio & Headphones'),
(4, 'Computer Peripherals'),
(5, 'Smart Wearables')
ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);

-- 2. Insert Suppliers
INSERT INTO `supplier` (`supplier_id`, `name`, `contact_email`, `phone`, `address`) VALUES
(1, 'Tech Suppliers Pvt Ltd', 'contact@techsuppliers.com', '+91 9876543210', 'Plot 42, Electronic City Phase 1, Bengaluru, Karnataka'),
(2, 'Global Hardware Dist', 'support@globalhardware.in', '+91 9811223344', 'Building 7, SEEPZ, Andheri East, Mumbai, Maharashtra'),
(3, 'Acoustics Sound Corp', 'orders@acousticssound.com', '+91 9723456789', 'Sector 62, Noida, Uttar Pradesh'),
(4, 'Prime Peripherals Hub', 'sales@primeperipherals.com', '+91 9900112233', 'Nehru Place, New Delhi, Delhi')
ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);

-- 3. Insert Products
INSERT INTO `product` (`product_id`, `name`, `description`, `category_id`, `supplier_id`, `stock_qty`, `price`) VALUES
(1, 'MacBook Pro 16 M3 Max', 'Apple M3 Max 36GB Unified Memory 1TB SSD Space Black', 1, 1, 15, 349900.00),
(2, 'Dell XPS 15 OLED', 'Intel Core i9 13th Gen 32GB RAM 1TB SSD RTX 4070', 1, 1, 22, 219999.00),
(3, 'iPhone 15 Pro Max 256GB', 'Titanium finish A17 Pro Chip 48MP Camera Natural Titanium', 2, 2, 40, 159900.00),
(4, 'Samsung Galaxy S24 Ultra', 'Snapdragon 8 Gen 3 12GB RAM 512GB Storage Titanium Gray', 2, 2, 35, 129999.00),
(5, 'Sony WH-1000XM5 ANC', 'Industry Leading Wireless Noise Cancelling Headphones Silver', 3, 3, 50, 29990.00),
(6, 'Apple AirPods Pro 2 USB-C', 'MagSafe Case USB-C with Active Noise Cancellation', 3, 3, 65, 24900.00),
(7, 'Keychron Q1 Pro Wireless', 'Custom Mechanical Keyboard QMK/VIA Aluminum Body Red Switches', 4, 4, 30, 16999.00),
(8, 'Logitech MX Master 3S', 'Performance Wireless Mouse Ergonomic Quiet Clicks 8K DPI', 4, 4, 85, 9495.00),
(9, 'Apple Watch Ultra 2', 'Rugged Titanium Case with Orange Ocean Band GPS + Cellular', 5, 2, 20, 89900.00),
(10, 'Samsung Galaxy Watch 6 Classic', 'Rotating Bezel Super AMOLED Sapphire Crystal Black 47mm', 5, 2, 28, 36999.00)
ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);

-- 4. Insert Customers
INSERT INTO `customer` (`customer_id`, `name`, `email`, `phone`, `gender`) VALUES
(1, 'Aarav Sharma', 'aarav.sharma@example.com', '+91 9123456780', 'M'),
(2, 'Priya Patel', 'priya.patel@example.com', '+91 9234567891', 'F'),
(3, 'Rohit Verma', 'rohit.verma@example.com', '+91 9345678902', 'M'),
(4, 'Ananya Iyer', 'ananya.iyer@example.com', '+91 9456789013', 'F'),
(5, 'Vikram Singh', 'vikram.singh@example.com', '+91 9567890124', 'M')
ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);

-- 5. Insert Addresses
INSERT INTO `address` (`address_id`, `customer_id`, `street`, `city`, `state`, `pincode`) VALUES
(1, 1, 'Flat 402, Skyline Residency, Bandra West', 'Mumbai', 'Maharashtra', '400050'),
(2, 2, 'House 18, 5th Main Road, Indiranagar', 'Bengaluru', 'Karnataka', '560038'),
(3, 3, 'B-34, Vasant Kunj Pocket 2', 'New Delhi', 'Delhi', '110070'),
(4, 4, 'Plot 88, Jubilee Hills Road No 36', 'Hyderabad', 'Telangana', '500033'),
(5, 5, 'Bungalow 12, Koregaon Park North Main Rd', 'Pune', 'Maharashtra', '411001')
ON DUPLICATE KEY UPDATE `street`=VALUES(`street`);

-- 6. Insert Shipping Records
INSERT INTO `shipping` (`shipment_id`, `address_id`, `carrier`, `tracking_no`, `status`) VALUES
(1, 1, 'BlueDart Express', 'TRK992019', 'Delivered'),
(2, 2, 'Delhivery Logistics', 'TRK884120', 'In Transit'),
(3, 3, 'DTDC Express', 'TRK771234', 'Shipped'),
(4, 4, 'FedEx Priority', 'TRK665432', 'Pending'),
(5, 5, 'BlueDart Express', 'TRK559871', 'Delivered')
ON DUPLICATE KEY UPDATE `tracking_no`=VALUES(`tracking_no`);

-- 7. Insert Orders
INSERT INTO `orders` (`order_id`, `customer_id`, `shipment_id`, `total_amount`, `status`) VALUES
(1, 1, 1, 374800.00, 'Delivered'),
(2, 2, 2, 189890.00, 'In Transit'),
(3, 3, 3, 26494.00, 'Shipped'),
(4, 4, 4, 129999.00, 'Processing'),
(5, 5, 5, 89900.00, 'Delivered')
ON DUPLICATE KEY UPDATE `total_amount`=VALUES(`total_amount`);

-- 8. Insert Order Items
INSERT INTO `order_item` (`order_id`, `product_id`, `quantity`, `unit_price`) VALUES
(1, 1, 1, 349900.00),
(1, 6, 1, 24900.00),
(2, 3, 1, 159900.00),
(2, 5, 1, 29990.00),
(3, 7, 1, 16999.00),
(3, 8, 1, 9495.00),
(4, 4, 1, 129999.00),
(5, 9, 1, 89900.00)
ON DUPLICATE KEY UPDATE `quantity`=VALUES(`quantity`);

-- 9. Insert Payments
INSERT INTO `payment` (`payment_id`, `order_id`, `amount`, `payment_type`, `status`) VALUES
(1, 1, 374800.00, 'Credit Card (HDFC)', 'Completed'),
(2, 2, 189890.00, 'UPI (Google Pay)', 'Completed'),
(3, 3, 26494.00, 'Net Banking (ICICI)', 'Completed'),
(4, 4, 129999.00, 'Debit Card (SBI)', 'Completed'),
(5, 5, 89900.00, 'Cash on Delivery', 'Completed')
ON DUPLICATE KEY UPDATE `amount`=VALUES(`amount`);

-- 10. Insert Reviews
INSERT INTO `review` (`review_id`, `customer_id`, `product_id`, `rating`, `comment`) VALUES
(1, 1, 1, 5, 'Unbelievable computing speed and battery life! The Space Black finish is gorgeous.'),
(2, 2, 3, 5, 'The titanium feel is lightweight and sleek. Battery easily lasts 1.5 days.'),
(3, 3, 7, 4, 'Solid mechanical keyboard with great tactile typing experience and key travel.'),
(4, 4, 5, 5, 'Best active noise cancellation in the market, very comfortable for long flights.'),
(5, 5, 9, 5, 'Ultra bright display and rugged build. Perfect companion for mountaineering and running.')
ON DUPLICATE KEY UPDATE `rating`=VALUES(`rating`);

SET FOREIGN_KEY_CHECKS = 1;
