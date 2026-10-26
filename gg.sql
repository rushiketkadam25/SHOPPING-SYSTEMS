-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: online_shopping
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address` (
  `address_id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `street` varchar(100) NOT NULL,
  `city` varchar(50) NOT NULL,
  `state` varchar(50) NOT NULL,
  `country` varchar(50) NOT NULL,
  `pincode` varchar(10) NOT NULL,
  PRIMARY KEY (`address_id`),
  KEY `fk_address_customer` (`customer_id`),
  CONSTRAINT `fk_address_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,1,'MG Road','Mumbai','Maharashtra','India','400001'),(2,2,'FC Road','Pune','Maharashtra','India','411004'),(3,3,'Ring Road','Surat','Gujarat','India','395003'),(4,4,'Park Street','Kolkata','West Bengal','India','700016'),(5,5,'Brigade Road','Bengaluru','Karnataka','India','560001'),(6,1,'Test Street','Mumbai','Maharashtra','India','000000');
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `cart_id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `created_on` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`cart_id`),
  KEY `fk_cart_customer` (`customer_id`),
  CONSTRAINT `fk_cart_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (1,1,'2026-10-13 22:58:01'),(2,2,'2026-10-13 22:58:01'),(3,3,'2026-10-13 22:58:01'),(4,4,'2026-10-13 22:58:01'),(5,5,'2026-10-13 22:58:01');
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_item`
--

DROP TABLE IF EXISTS `cart_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_item` (
  `cart_id` int NOT NULL,
  `product_id` int NOT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`cart_id`,`product_id`),
  KEY `fk_cart_item_product` (`product_id`),
  CONSTRAINT `fk_cart_item_cart` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`cart_id`),
  CONSTRAINT `fk_cart_item_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`),
  CONSTRAINT `chk_cart_quantity` CHECK ((`quantity` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_item`
--

LOCK TABLES `cart_item` WRITE;
/*!40000 ALTER TABLE `cart_item` DISABLE KEYS */;
INSERT INTO `cart_item` VALUES (1,1,1),(2,2,2),(3,3,1),(4,4,1),(5,5,2);
/*!40000 ALTER TABLE `cart_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (5,'Accessories'),(3,'Books'),(2,'Clothing'),(1,'Electronics'),(4,'Home Appliances');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credit_card`
--

DROP TABLE IF EXISTS `credit_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `credit_card` (
  `payment_id` int NOT NULL,
  `card_number` varchar(19) NOT NULL,
  `cvv` varchar(4) NOT NULL,
  `expiry_date` date NOT NULL,
  PRIMARY KEY (`payment_id`),
  CONSTRAINT `fk_card_payment` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`payment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credit_card`
--

LOCK TABLES `credit_card` WRITE;
/*!40000 ALTER TABLE `credit_card` DISABLE KEYS */;
INSERT INTO `credit_card` VALUES (1,'4111111111111001','123','2028-12-31'),(2,'4111111111111002','234','2029-11-30'),(3,'4111111111111003','345','2028-10-31'),(4,'4111111111111004','456','2030-09-30'),(5,'4111111111111005','567','2029-08-31');
/*!40000 ALTER TABLE `credit_card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `customer_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(80) NOT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `registered_on` date NOT NULL DEFAULT (curdate()),
  `gender` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `uq_customer_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'Aarav Sharma','aarav@gmail.com','9876543210','2026-10-13',NULL),(2,'Priya Patel','priya@gmail.com','9876500002','2026-10-13',NULL),(3,'Rahul Mehta','rahul@gmail.com','9876500003','2026-10-13',NULL),(4,'Sneha Shah','sneha@gmail.com','9876500004','2026-10-13',NULL),(5,'Arjun Verma','arjun@gmail.com','9876500005','2026-10-13',NULL),(6,'TCL Customer','tclcustomer@gmail.com','9876543210','2026-10-18',NULL);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `customer_order_view`
--

DROP TABLE IF EXISTS `customer_order_view`;
/*!50001 DROP VIEW IF EXISTS `customer_order_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `customer_order_view` AS SELECT 
 1 AS `customer_id`,
 1 AS `name`,
 1 AS `order_id`,
 1 AS `total_amount`,
 1 AS `status`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `customer_view`
--

DROP TABLE IF EXISTS `customer_view`;
/*!50001 DROP VIEW IF EXISTS `customer_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `customer_view` AS SELECT 
 1 AS `customer_id`,
 1 AS `name`,
 1 AS `email`,
 1 AS `phone`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `digital_wallet`
--

DROP TABLE IF EXISTS `digital_wallet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `digital_wallet` (
  `payment_id` int NOT NULL,
  `provider` varchar(50) NOT NULL,
  `wallet_ref` varchar(100) NOT NULL,
  PRIMARY KEY (`payment_id`),
  CONSTRAINT `fk_wallet_payment` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`payment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `digital_wallet`
--

LOCK TABLES `digital_wallet` WRITE;
/*!40000 ALTER TABLE `digital_wallet` DISABLE KEYS */;
INSERT INTO `digital_wallet` VALUES (6,'Paytm','WALLET001'),(7,'PhonePe','WALLET002'),(8,'Google Pay','WALLET003'),(9,'Paytm','WALLET004'),(10,'PhonePe','WALLET005');
/*!40000 ALTER TABLE `digital_wallet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `order_id` int NOT NULL,
  `product_id` int NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`order_id`,`product_id`),
  KEY `fk_order_item_product` (`product_id`),
  CONSTRAINT `fk_order_item_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `fk_order_item_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`),
  CONSTRAINT `chk_order_item_price` CHECK ((`unit_price` >= 0)),
  CONSTRAINT `chk_order_item_quantity` CHECK ((`quantity` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES (1,1,1,55000.00),(2,2,2,799.00),(3,3,1,499.00),(4,4,1,2499.00),(5,5,2,1999.00);
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `shipment_id` int DEFAULT NULL,
  `order_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `total_amount` decimal(10,2) NOT NULL DEFAULT '0.00',
  `status` varchar(30) NOT NULL DEFAULT 'Pending',
  PRIMARY KEY (`order_id`),
  KEY `fk_orders_customer` (`customer_id`),
  KEY `fk_orders_shipment` (`shipment_id`),
  CONSTRAINT `fk_orders_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  CONSTRAINT `fk_orders_shipment` FOREIGN KEY (`shipment_id`) REFERENCES `shipping` (`shipment_id`),
  CONSTRAINT `chk_order_amount` CHECK ((`total_amount` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,1,1,'2026-10-13 22:58:01',55000.00,'Processing'),(2,2,2,'2026-10-13 22:58:01',1598.00,'Shipped'),(3,3,3,'2026-10-13 22:58:01',499.00,'Delivered'),(4,4,4,'2026-10-13 22:58:01',2499.00,'Shipped'),(5,5,5,'2026-10-13 22:58:01',3998.00,'Delivered'),(6,1,NULL,'2026-10-17 21:39:51',0.00,'Pending');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `payment_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `payment_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `payment_type` varchar(30) NOT NULL,
  `status` varchar(30) NOT NULL DEFAULT 'Pending',
  PRIMARY KEY (`payment_id`),
  KEY `fk_payment_order` (`order_id`),
  CONSTRAINT `fk_payment_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `chk_payment_amount` CHECK ((`amount` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` VALUES (1,1,1.00,'2026-10-13 22:58:01','Credit Card','Completed'),(2,2,1598.00,'2026-10-13 22:58:01','Credit Card','Completed'),(3,3,499.00,'2026-10-13 22:58:01','Credit Card','Pending'),(4,4,2499.00,'2026-10-13 22:58:01','Credit Card','Completed'),(5,5,3998.00,'2026-10-13 22:58:01','Credit Card','Completed'),(6,1,100.00,'2026-10-13 22:58:01','Digital Wallet','Completed'),(7,2,50.00,'2026-10-13 22:58:01','Digital Wallet','Completed'),(8,3,20.00,'2026-10-13 22:58:01','Digital Wallet','Pending'),(9,4,75.00,'2026-10-13 22:58:01','Digital Wallet','Completed'),(10,5,100.00,'2026-10-13 22:58:01','Digital Wallet','Completed');
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `payment_view`
--

DROP TABLE IF EXISTS `payment_view`;
/*!50001 DROP VIEW IF EXISTS `payment_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `payment_view` AS SELECT 
 1 AS `payment_id`,
 1 AS `order_id`,
 1 AS `amount`,
 1 AS `payment_type`,
 1 AS `status`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `category_id` int NOT NULL,
  `supplier_id` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `stock_qty` int NOT NULL DEFAULT '0',
  `price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`product_id`),
  KEY `fk_product_category` (`category_id`),
  KEY `fk_product_supplier` (`supplier_id`),
  CONSTRAINT `fk_product_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`),
  CONSTRAINT `fk_product_supplier` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`supplier_id`),
  CONSTRAINT `chk_product_price` CHECK ((`price` >= 0)),
  CONSTRAINT `chk_product_stock` CHECK ((`stock_qty` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,1,1,'Laptop','Portable computer',100,1500.00),(2,2,2,'T-Shirt','Cotton T-shirt',100,799.00),(3,3,3,'SQL Book','Database textbook',50,599.00),(4,4,4,'Mixer','Kitchen mixer',15,2499.00),(5,5,5,'Headphones','Wireless headphones',30,1999.00);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `product_view`
--

DROP TABLE IF EXISTS `product_view`;
/*!50001 DROP VIEW IF EXISTS `product_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `product_view` AS SELECT 
 1 AS `product_id`,
 1 AS `name`,
 1 AS `price`,
 1 AS `stock_qty`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `review_id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `product_id` int NOT NULL,
  `rating` int NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `review_date` date NOT NULL DEFAULT (curdate()),
  PRIMARY KEY (`review_id`),
  UNIQUE KEY `uq_customer_product_review` (`customer_id`,`product_id`),
  KEY `fk_review_product` (`product_id`),
  CONSTRAINT `fk_review_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  CONSTRAINT `fk_review_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`),
  CONSTRAINT `chk_review_rating` CHECK ((`rating` between 1 and 5))
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES (1,1,1,5,'Excellent product','2026-10-13'),(2,2,2,4,'Good quality','2026-10-13'),(3,3,3,5,'Very useful book','2026-10-13'),(4,4,4,3,'Average product','2026-10-13');
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shipping`
--

DROP TABLE IF EXISTS `shipping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shipping` (
  `shipment_id` int NOT NULL AUTO_INCREMENT,
  `address_id` int NOT NULL,
  `carrier` varchar(50) DEFAULT NULL,
  `tracking_no` varchar(50) DEFAULT NULL,
  `status` varchar(30) NOT NULL DEFAULT 'Pending',
  PRIMARY KEY (`shipment_id`),
  UNIQUE KEY `tracking_no` (`tracking_no`),
  KEY `fk_shipping_address` (`address_id`),
  CONSTRAINT `fk_shipping_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shipping`
--

LOCK TABLES `shipping` WRITE;
/*!40000 ALTER TABLE `shipping` DISABLE KEYS */;
INSERT INTO `shipping` VALUES (1,1,'BlueDart','TRK1001','Shipped'),(2,2,'DTDC','TRK1002','Shipped'),(3,3,'Delhivery','TRK1003','Pending'),(4,4,'IndiaPost','TRK1004','Shipped'),(5,5,'BlueDart','TRK1005','Delivered');
/*!40000 ALTER TABLE `shipping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `shipping_view`
--

DROP TABLE IF EXISTS `shipping_view`;
/*!50001 DROP VIEW IF EXISTS `shipping_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `shipping_view` AS SELECT 
 1 AS `shipment_id`,
 1 AS `address_id`,
 1 AS `carrier`,
 1 AS `tracking_no`,
 1 AS `status`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier` (
  `supplier_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(80) NOT NULL,
  `contact_email` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`supplier_id`),
  UNIQUE KEY `contact_email` (`contact_email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier`
--

LOCK TABLES `supplier` WRITE;
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
INSERT INTO `supplier` VALUES (1,'Tech Suppliers','tech@example.com'),(2,'Fashion Traders','fashion@example.com'),(3,'Book Distributors','books@example.com'),(4,'Home Essentials','home@example.com'),(5,'Accessory World','accessories@example.com');
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `customer_order_view`
--

/*!50001 DROP VIEW IF EXISTS `customer_order_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `customer_order_view` AS select `c`.`customer_id` AS `customer_id`,`c`.`name` AS `name`,`o`.`order_id` AS `order_id`,`o`.`total_amount` AS `total_amount`,`o`.`status` AS `status` from (`customer` `c` join `orders` `o` on((`c`.`customer_id` = `o`.`customer_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `customer_view`
--

/*!50001 DROP VIEW IF EXISTS `customer_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `customer_view` AS select `customer`.`customer_id` AS `customer_id`,`customer`.`name` AS `name`,`customer`.`email` AS `email`,`customer`.`phone` AS `phone` from `customer` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `payment_view`
--

/*!50001 DROP VIEW IF EXISTS `payment_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `payment_view` AS select `payment`.`payment_id` AS `payment_id`,`payment`.`order_id` AS `order_id`,`payment`.`amount` AS `amount`,`payment`.`payment_type` AS `payment_type`,`payment`.`status` AS `status` from `payment` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `product_view`
--

/*!50001 DROP VIEW IF EXISTS `product_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `product_view` AS select `product`.`product_id` AS `product_id`,`product`.`name` AS `name`,`product`.`price` AS `price`,`product`.`stock_qty` AS `stock_qty` from `product` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `shipping_view`
--

/*!50001 DROP VIEW IF EXISTS `shipping_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `shipping_view` AS select `shipping`.`shipment_id` AS `shipment_id`,`shipping`.`address_id` AS `address_id`,`shipping`.`carrier` AS `carrier`,`shipping`.`tracking_no` AS `tracking_no`,`shipping`.`status` AS `status` from `shipping` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-25 18:14:36
