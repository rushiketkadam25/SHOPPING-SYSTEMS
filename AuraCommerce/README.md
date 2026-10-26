# 🛍️ AuraCommerce — Enterprise Cloud POS, ERP & E-Commerce Desktop Suite

AuraCommerce is a Java Swing enterprise desktop application featuring a FlatLaf Look & Feel, remote MySQL connectivity, role-based access control, atomic transaction handling, and business intelligence analytics.

---

## 📁 Project Directory Structure

```
AuraCommerce/
│
├── 📂 src/
│   └── 📂 main/
│       ├── 📂 java/
│       │   └── 📂 com/
│       │       └── 📂 shopping/
│       │           ├── 📄 Main.java                 # Application entry point, sidebar navigation & KPI dashboard
│       │           ├── 📄 LoginFrame.java           # Authentication portal with 1-click role logins
│       │           ├── 📄 UserSession.java          # In-memory session manager & Role definitions
│       │           ├── 📄 UIUtils.java              # Design system tokens, color palette & widget helpers
│       │           ├── 📄 ModernTableRenderer.java  # Custom table cell renderer with colored status badges
│       │           ├── 📄 DBConnection.java         # JDBC MySQL connection factory & config reader/writer
│       │           ├── 📄 ConnectionDialog.java     # GUI modal dialog for dynamic MySQL remote config
│       │           ├── 📄 ProductPanel.java         # Admin product catalog, filtering & inventory CRUD
│       │           ├── 📄 OrderPanel.java           # Admin order management & multi-table atomic checkout
│       │           ├── 📄 CustomerPanel.java        # Customer CRM accounts & verified customer reviews
│       │           ├── 📄 CustomerStorefront.java   # Customer self-service shopping & live order tracking
│       │           ├── 📄 SupplierPanel.java        # Supplier partner portal for restocking & pricing
│       │           ├── 📄 ShipperPanel.java         # Logistics fleet portal for order dispatch & tracking
│       │           └── 📄 ReportsPanel.java         # Executive BI reporting engine & dynamic SQL runner
│       └── 📂 resources/
│           └── 📄 db_config.properties              # Database connection properties
│
├── 📂 database/
│   ├── 📄 schema.sql                                # Complete MySQL DDL: 10 tables, constraints & 4 views
│   └── 📄 seed_data.sql                             # Initial demo data (products, categories, users, orders)
│
├── 📂 lib/
│   ├── 📄 flatlaf-3.4.1.jar                         # FlatLaf modern dark/light Swing theme library
│   └── 📄 mysql-connector-j-8.3.0.jar              # Official MySQL JDBC Driver
│
├── 📂 bin/                                          # Compiled bytecode classes
├── 📄 pom.xml                                       # Maven build descriptor (Shade plugin for fat JAR)
├── 📄 build.gradle                                  # Gradle build alternative
├── 📄 build.bat                                     # 1-Click Windows compile script
├── 📄 run.bat                                       # 1-Click Windows execution script
├── 📄 db_config.properties                          # Active runtime DB configuration
└── 📄 README.md                                     # Project documentation
```

---

## 👥 Role-Based Access Control (RBAC)

The application includes 4 dedicated portal interfaces:

| Role | Default Username | Default Password | Features & Scope |
| :--- | :--- | :--- | :--- |
| **👑 System Administrator** | `shop_admin` | `Admin@123` | Full access to Product Catalog, Atomic Checkout, Orders, Customers, Reviews, and Analytics. |
| **👤 Customer / Buyer** | `customer` | `Cust@123` | Storefront browsing, real-time stock lookup, placing orders, live parcel tracking, and writing reviews. |
| **🏭 Supplier Partner** | `inventory_manager` | `Inv@123` | Supplier-specific product catalog, restocking stock quantities, and updating wholesale prices. |
| **🚚 Logistics Fleet Shipper** | `shipper` | `Ship@123` | Fleet dispatch table, customer destination viewing, tracking number generation, and delivery status updates. |

---

## 🗄️ Database Architecture (`online_shopping`)

The application connects to a MySQL database named `online_shopping`.

### Core Tables:
1. **`category`**: Product categories (`category_id`, `name`).
2. **`supplier`**: Supplier profiles (`supplier_id`, `name`, `contact_email`, `phone`, `address`).
3. **`product`**: Inventory master (`product_id`, `name`, `description`, `category_id`, `supplier_id`, `stock_qty`, `price`).
4. **`customer`**: Registered customer accounts (`customer_id`, `name`, `email`, `phone`, `registered_on`, `gender`).
5. **`address`**: Customer delivery destinations (`address_id`, `customer_id`, `street`, `city`, `state`, `pincode`).
6. **`shipping`**: Dispatch records (`shipment_id`, `address_id`, `carrier`, `tracking_no`, `status`, `shipment_date`).
7. **`orders`**: Transaction orders (`order_id`, `customer_id`, `shipment_id`, `total_amount`, `status`, `order_date`).
8. **`order_item`**: Order line items (`order_id`, `product_id`, `quantity`, `unit_price`).
9. **`payment`**: Settlement records (`payment_id`, `order_id`, `amount`, `payment_type`, `status`, `payment_date`).
10. **`review`**: Customer feedback (`review_id`, `customer_id`, `product_id`, `rating`, `comment`, `review_date`).

### Predefined Analytical Views:
- **`customer_order_view`**: Aggregates customers, orders, logistics carriers, and shipment tracking numbers.
- **`product_view`**: Combines products, categories, suppliers, and available inventory.
- **`payment_view`**: Tracks payments, methods (Credit Card, UPI, Net Banking), and customer accounts.
- **`shipping_view`**: Complete logistics dispatch overview linking carrier, tracking, destination, and recipient.

---

## 🚀 How to Build and Run

### Option 1: 1-Click Windows Batch Scripts (Zero Configuration)
1. **Compile**: Double-click [`build.bat`](file:///c:/Users/Yuvraj/Desktop/copy%20th/AuraCommerce/build.bat) or run:
   ```cmd
   build.bat
   ```
2. **Run**: Double-click [`run.bat`](file:///c:/Users/Yuvraj/Desktop/copy%20th/AuraCommerce/run.bat) or run:
   ```cmd
   run.bat
   ```

### Option 2: Apache Maven
```bash
# Compile and build executable fat JAR
mvn clean package

# Run the shaded JAR
java -jar target/auracommerce-1.0.0.jar
```

### Option 3: Manual Compilation via Command Line
```cmd
# Compile source files into bin/
javac -cp "lib\flatlaf-3.4.1.jar;lib\mysql-connector-j-8.3.0.jar" -d bin src\main\java\com\shopping\*.java

# Launch the Application
java -cp "bin;lib\flatlaf-3.4.1.jar;lib\mysql-connector-j-8.3.0.jar;src\main\resources" com.shopping.Main
```

---

## ⚙️ Database Configuration

Database credentials are automatically read from [`db_config.properties`](file:///c:/Users/Yuvraj/Desktop/copy%20th/AuraCommerce/db_config.properties):
```properties
database=online_shopping
password=12345678
port=3306
host=127.0.0.1
username=root
```
*Note: You can also change the database host, port, database name, and credentials directly at runtime using the **⚙️ DB Config** button in the top navigation bar or the login screen.*
