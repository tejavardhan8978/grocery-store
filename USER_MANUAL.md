# Grocery Store Management System - User Manual

## Table of Contents

1. [System Overview](#system-overview)
2. [System Requirements](#system-requirements)
3. [Installation and Setup](#installation-and-setup)
4. [Getting Started](#getting-started)
5. [User Roles and Access](#user-roles-and-access)
6. [User Guide](#user-guide)
7. [Administrator Guide](#administrator-guide)
8. [Employee Guide](#employee-guide)
9. [Troubleshooting](#troubleshooting)
10. [Support and Maintenance](#support-and-maintenance)

## System Overview

The Grocery Store Management System is a comprehensive web-based application designed to manage grocery store operations including product inventory, customer management, order processing, and administrative functions. The system supports multiple user roles with different access levels and provides a complete e-commerce solution for grocery stores.

### Key Features
- Product catalog management with categories and inventory tracking
- User registration and authentication system
- Shopping cart and checkout functionality
- Order management and tracking
- Administrative dashboard and reporting
- Role-based access control (Customer, Employee, Admin)
- Responsive web design for desktop and mobile devices

## System Requirements

### Hardware Requirements
- **Processor**: Intel Core i3 or equivalent (minimum), Intel Core i5 or better (recommended)
- **RAM**: 4GB (minimum), 8GB or more (recommended)
- **Storage**: 5GB free disk space (minimum), 10GB (recommended)
- **Network**: Broadband internet connection for development and deployment

### Software Requirements
- **Operating System**: Windows 10/11, macOS 10.15+, or Linux Ubuntu 18.04+
- **Java Development Kit (JDK)**: Version 17 or higher
- **Database**: PostgreSQL 12+ or MySQL 8+
- **Web Browser**: Chrome 90+, Firefox 88+, Safari 14+, or Edge 90+
- **Development Tools** (for setup):
  - Apache Maven 3.6+
  - Git 2.30+
  - IDE (IntelliJ IDEA, Eclipse, or Visual Studio Code)

## Installation and Setup

### Prerequisites Setup

#### 1. Install Java Development Kit (JDK)
1. Download JDK 17 or higher from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
2. Install following platform-specific instructions
3. Verify installation:
   ```bash
   java -version
   javac -version
   ```

#### 2. Install Maven
1. Download Apache Maven from [maven.apache.org](https://maven.apache.org/download.cgi)
2. Extract to a directory (e.g., `C:\apache-maven-3.9.0` on Windows)
3. Add Maven bin directory to system PATH
4. Verify installation:
   ```bash
   mvn -version
   ```

#### 3. Install Database
**Option A: PostgreSQL (Recommended)**
1. Download PostgreSQL from [postgresql.org](https://www.postgresql.org/download/)
2. Install with default settings
3. Note the password for the `postgres` user
4. Database will be automatically created on first run under the directory `/db_test/`.

**Option B: MySQL**
1. Download MySQL from [mysql.com](https://dev.mysql.com/downloads/)
2. Install MySQL Server and Workbench
3. Database will be automatically created on first run under the directory `/db_test/`.

#### 4. Install Git
1. Download Git from [git-scm.com](https://git-scm.com/downloads)
2. Install with default settings
3. Verify installation:
   ```bash
   git --version
   ```

### Application Setup

#### 1. Clone the Repository
```bash
git clone https://github.com/tejavardhan8978/grocery-store.git
cd grocery-store
```

#### 2. Configure Database Connection
1. Open `src/main/resources/application.properties`
2. Update database configuration:

**For PostgreSQL:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/grocery_store
spring.datasource.username=postgres
spring.datasource.password=your_password_here
spring.datasource.driver-class-name=org.postgresql.Driver
```

**For MySQL:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/grocery_store
spring.datasource.username=root
spring.datasource.password=your_password_here
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

#### 3. Install Dependencies and Build
```bash
mvn clean install
```

#### 4. Run Database Migrations
The application will automatically create database tables on first run using JPA/Hibernate.
The database can also be accessed through the browser by navigating to:
```
localhost:8081/h2-console
```

#### 5. Start the Application
```bash
mvn spring-boot:run
```

#### 6. Access the Application
Open your web browser and navigate to: `http://localhost:8081`

### Initial Data Setup

The application includes a data initializer that creates sample users and categories on first startup:

**Default Admin Account:**
- Email: `admin@grocerystore.com`
- Password: `admin123`

**Default Employee Account:**
- Email: `jane.smith@grocerystore.com`
- Password: `employee123`

**Default Customer Account:**
- Email: `john.doe@example.com`
- Password: `password123`

## Getting Started

### First Time Setup

1. **Access the Application**: Navigate to `http://localhost:8081`
2. **Login as Administrator**: Use the default admin credentials
3. **Change Default Passwords**: Immediately change all default passwords for security
4. **Add Product Categories**: Set up your store's product categories
5. **Add Initial Products**: Add your inventory items
6. **Configure Store Settings**: Set up store locations and policies

### Basic Navigation

- **Home Page**: Featured products and store information
- **Products**: Browse and search the product catalog
- **Categories**: Filter products by category
- **Cart**: View and manage shopping cart items
- **Orders**: View order history (logged-in users)
- **Profile**: Manage user account information
- **Admin Panel**: Administrative functions (admin/employee only)

## User Roles and Access

### Guest Users
- Browse products and categories
- View product details
- Limited access (cannot add to cart or purchase)

### Registered Customers
- All guest user capabilities
- Create account and login
- Add products to cart
- Place and track orders
- Manage profile information
- View order history

### Employees
- All customer capabilities
- Access to inventory management
- View all customer orders
- Update product stock levels
- Toggle product active status
- Basic administrative functions

### Administrators
- All employee capabilities
- Full system administration
- User management and role assignment
- Product and category management
- System configuration
- Toggle between admin and user modes

## User Guide

### Account Management

#### Creating an Account
1. Click "Register" in the top navigation
2. Fill in required information:
   - First Name and Last Name
   - Email Address (used for login)
   - Password (minimum 6 characters)
   - Phone Number
3. Click "Create Account"
4. You will be automatically logged in

#### Logging In
1. Click "Login" in the top navigation
2. Enter your email and password
3. Click "Sign In"

#### Managing Profile
1. Click on your name in the top navigation
2. Select "Profile"
3. Update your information as needed
4. Click "Update Profile" to save changes

### Shopping Experience

#### Browsing Products
1. Navigate to "Products" in the main menu
2. Use filters to narrow your search:
   - **Search**: Enter product name or keywords
   - **Category**: Select from dropdown menu
   - **Price Range**: Set minimum and maximum prices
   - **Sort By**: Name, Price, or Stock quantity
   - **Sort Order**: Ascending or Descending
3. Click "Apply Filters" to update results

#### Product Details
1. Click on any product to view detailed information
2. View product specifications:
   - Name and description
   - Price
   - Stock availability
   - Category information
   - Product image
3. Related products are shown at the bottom

#### Adding to Cart
1. From product details page, select quantity
2. Click "Add to Cart"
3. Cart icon in navigation shows item count
4. Continue shopping or proceed to checkout

#### Managing Cart
1. Click cart icon in navigation
2. Review cart contents:
   - Product details and images
   - Quantities (adjustable)
   - Individual and total prices
3. Options available:
   - **Update Quantity**: Use +/- buttons or enter number
   - **Remove Item**: Click "Remove" button
   - **Clear Cart**: Remove all items at once
   - **Continue Shopping**: Return to products
   - **Proceed to Checkout**: Begin order process

#### Checkout Process
1. From cart, click "Proceed to Checkout"
2. Verify order summary and items
3. Enter delivery information:
   - Store location for pickup/delivery
   - Billing address (optional)
   - Special instructions or notes
4. Click "Place Order"
5. System processes payment (simulated)
6. Receive order confirmation with order number

#### Order Tracking
1. Navigate to "Orders" in main menu
2. View order history with details:
   - Order number and date
   - Order status (Active, Delivered, Cancelled)
   - Items ordered and quantities
   - Total amount
3. Use filters to find specific orders:
   - Search by product name
   - Filter by status
   - Date range selection
   - Sort options

### Safety and Best Practices

#### Account Security
- Use strong, unique passwords
- Do not share login credentials
- Log out when using shared computers
- Report suspicious activity immediately

#### Shopping Guidelines
- Verify product details before adding to cart
- Check stock availability for time-sensitive items
- Review cart contents before checkout
- Keep order confirmation numbers for reference

## Administrator Guide

### Admin Mode Toggle
Administrators can switch between user and admin modes:
1. Look for the toggle button in the top navigation (admin users only)
2. Click to switch between "User Mode" and "Admin Mode"
3. Admin mode provides access to administrative functions

### Dashboard Access
1. Enable admin mode
2. Click "Admin Dashboard" or navigate to `/admin/dashboard`
3. Overview includes:
   - System statistics
   - Recent activity
   - Quick action buttons

### User Management

#### Viewing Customers
1. Navigate to Admin Dashboard
2. Click "Customer Management"
3. View customer list with:
   - User information (name, email, phone)
   - Account status and roles
   - Registration date
4. Use search and pagination to find specific users

#### Managing User Roles
1. From customer list, click "View Details" for any user
2. Available role actions:
   - **Make Employee**: Grant employee privileges
   - **Remove Employee**: Revoke employee privileges
   - **Make Admin**: Grant administrator privileges (use caution)
   - **Remove Admin**: Revoke administrator privileges
3. **Delete Customer**: Permanently remove user account
   - Cannot delete your own account
   - Use with extreme caution as this is irreversible

### Product Management

#### Adding New Products
1. Navigate to Products page
2. Click "Add Product" (admin/employee only)
3. Fill in product information:
   - **Name**: Product display name (required)
   - **SKU**: Unique product identifier (required)
   - **Category**: Select from existing categories (required)
   - **Price**: Product price in dollars (required)
   - **Initial Quantity**: Starting stock level (required)
   - **Reorder Level**: Low stock threshold (optional)
   - **Description**: Product description (optional)
   - **Image URL**: Path to product image (optional)
4. Click "Add Product" to save

#### Editing Products
1. Navigate to any product details page
2. Click "Edit Product" (admin/employee only)
3. Modify any product information
4. Click "Update Product" to save changes

#### Managing Product Status
From product details page (admin/employee only):
- **Toggle Active Status**: Enable/disable product visibility
- **Update Stock Quantity**: Adjust inventory levels
- **Quick Stock Update**: Enter new quantity directly

#### Product Categories
Product categories help organize the inventory:
- Categories are created and managed through the admin interface
- Each product must be assigned to a category
- Categories can include images and descriptions
- Products can be filtered by category on the storefront

### Inventory Management

#### Stock Monitoring
- View current stock levels for all products
- Identify low-stock items based on reorder levels
- Track stock changes through order processing
- Update inventory quantities as needed

#### Reorder Alerts
- Set reorder levels for automatic low-stock alerts
- Monitor products approaching zero stock
- Plan inventory replenishment based on sales patterns

### Order Management

#### Viewing All Orders
1. Navigate to Orders section
2. Admin/employee view shows all customer orders
3. Filter and search capabilities:
   - Search by customer name or product
   - Filter by order status
   - Date range filtering
   - Sort by various criteria

#### Order Status Management
Available order statuses:
- **Active**: Order placed and being processed
- **Delivered**: Order completed successfully
- **Cancelled**: Order cancelled by customer or admin

### System Configuration

#### Store Settings
- Configure store locations for pickup/delivery
- Set operational policies
- Manage system-wide settings

#### Image Management
- Product images are served from `/images/products/` local directory
- Category images from `/images/categories/` directory
- Use relative paths when setting image URLs (e.g., `/images/products/milk.jpg`)

## Employee Guide

### Employee Access
Employees have enhanced privileges compared to regular customers:
- Access to inventory management functions
- Ability to view all customer orders
- Product management capabilities
- Stock level management

### Daily Operations

#### Stock Management
1. Monitor product stock levels
2. Update quantities as inventory changes
3. Toggle product availability
4. Alert management of low stock items

#### Order Processing
1. View all customer orders
2. Monitor order status
3. Assist with order fulfillment
4. Update order status as needed

#### Customer Support
1. Access customer order history
2. Assist with order issues
3. Process returns or cancellations
4. Provide product information

### Inventory Tasks

#### Regular Stock Updates
- Perform regular inventory counts
- Update system quantities to match physical inventory
- Report discrepancies to management
- Monitor reorder levels and stock alerts

#### Product Maintenance
- Ensure product information is accurate
- Update product descriptions and images
- Manage product categories
- Remove discontinued items

## Troubleshooting

### Common Issues

#### Cannot Access Application
**Problem**: Browser shows "Unable to connect" error
**Solutions**:
1. Verify application is running: Check console for "Started GroceryStoreApplication"
2. Check correct URL: `http://localhost:8081`
3. Verify port availability: No other applications using port 8080
4. Check firewall settings: Allow Java application through firewall

#### Database Connection Errors
**Problem**: Application fails to start with database errors
**Solutions**:
1. Verify database is running
2. Check connection settings in `application.properties`
3. Ensure database file `database.mv.db` exists
4. Verify username/password credentials
5. Check database permissions

#### Login Issues
**Problem**: Cannot login with correct credentials
**Solutions**:
1. Verify email address is correct (case sensitive)
2. Check password (case sensitive)
3. Clear browser cache and cookies
4. Try different browser
5. Contact administrator to reset password

#### Cart/Checkout Problems
**Problem**: Items not adding to cart or checkout failing
**Solutions**:
1. Ensure you are logged in (not guest user)
2. Check product stock availability
3. Verify browser JavaScript is enabled
4. Clear browser cache
5. Try different browser

#### Permission Denied Errors
**Problem**: Cannot access admin or employee functions
**Solutions**:
1. Verify your user role with administrator
2. Ensure you are logged in with correct account
3. Check if admin mode is enabled (for admin users)
4. Contact administrator to verify permissions

### Performance Issues

#### Slow Page Loading
**Solutions**:
1. Check internet connection speed
2. Clear browser cache and cookies
3. Disable browser extensions temporarily
4. Try different browser
5. Contact system administrator

#### Application Running Slowly
**Solutions**:
1. Check system resources (RAM, CPU usage)
2. Restart the application
3. Check database performance
4. Review application logs for errors

### Error Messages

#### "Product with SKU already exists"
- Each product must have a unique SKU
- Use different SKU or update existing product

#### "Insufficient stock for product"
- Cannot order more items than available in stock
- Check product availability or reduce quantity

#### "Cart is empty"
- Add products to cart before attempting checkout
- Ensure cart items haven't expired or been removed

#### "Access denied. Admin privileges required"
- Function requires administrator access
- Contact admin to grant proper permissions

### Getting Help

#### Log Files
Application logs are available in the console output and can help diagnose issues:
1. Check console output for error messages
2. Look for stack traces indicating specific problems
3. Note timing of errors for troubleshooting

#### System Information
When reporting issues, provide:
- Operating system and version
- Browser type and version
- Error messages (exact text)
- Steps to reproduce the problem
- Screenshots if applicable

## Support and Maintenance

### Regular Maintenance Tasks

#### Weekly Tasks
1. **Backup Database**: Create regular database backups
2. **Review System Logs**: Check for errors or unusual activity
3. **Monitor Disk Space**: Ensure adequate storage space
4. **Update Inventory**: Reconcile system inventory with physical stock

#### Monthly Tasks
1. **Review User Accounts**: Remove inactive or suspicious accounts
2. **Analyze Performance**: Review system performance metrics
3. **Update Product Catalog**: Add new products, remove discontinued items
4. **Security Review**: Check user permissions and access logs

#### Quarterly Tasks
1. **System Updates**: Apply security patches and updates
2. **Backup Strategy Review**: Verify backup procedures are working
3. **Performance Optimization**: Review and optimize database queries
4. **User Training**: Provide refresher training for staff

### Security Guidelines

#### Password Policies
- Minimum 8 characters with mixed case, numbers, and symbols
- Regular password changes (every 90 days)
- No password reuse
- Secure password storage practices

#### Access Control
- Regular review of user permissions
- Immediate removal of access for departed employees
- Principle of least privilege
- Regular audit of admin accounts

#### Data Protection
- Regular database backups stored securely
- Encryption of sensitive data
- Secure transmission protocols
- Compliance with data protection regulations

### System Updates

#### Application Updates
1. Create database backup before updates
2. Test updates in development environment
3. Schedule updates during low-usage periods
4. Verify all functions work after update
5. Roll back if issues occur

#### Database Maintenance
1. Regular database optimization
2. Index maintenance for performance
3. Archive old data as needed
4. Monitor database growth and plan capacity

### Contact Information

#### Technical Support
For technical issues or questions:
- Review this manual first
- Check troubleshooting section
- Contact system administrator
- Escalate to development team if needed

#### Training and Documentation
For additional training or documentation:
- Reference this user manual
- Request hands-on training sessions
- Access online help resources
- Contact the development team for specific guidance

### System Specifications

#### Current Version
- Application Version: 1.0
- Java Version: 17+
- Spring Boot Version: 3.x
- Database: PostgreSQL/MySQL compatible

#### Supported Browsers
- Google Chrome 90+
- Mozilla Firefox 88+
- Microsoft Edge 90+
- Safari 14+ (macOS only)

#### API Endpoints
The system includes REST API endpoints for integration:
- Product management: `/api/products/`
- Order processing: `/api/orders/`
- User management: `/api/users/`

---

## Conclusion

This Grocery Store Management System provides a comprehensive solution for managing grocery store operations. Regular maintenance, proper user training, and adherence to security guidelines will ensure optimal system performance and user satisfaction.

For additional support or questions not covered in this manual, contact the development team.
