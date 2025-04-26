CREATE SCHEMA Sales
GO

CREATE SCHEMA Production
GO

CREATE SCHEMA Customer
GO

CREATE TABLE Customer.Customer
(
    CustomerID int IDENTITY(1,1) PRIMARY KEY,
    FirstName nvarchar(255) NOT NULL,
    LastName nvarchar(255) NOT NULL,
    ContactNumber varchar(11) NOT NULL,
    Street nvarchar(MAX),
    Barangay nvarchar(MAX),
    City nvarchar(MAX),
    Province nvarchar(MAX)
)

CREATE TABLE Production.Supplier
(
    SupplierID int IDENTITY(1,1) PRIMARY KEY,
    SupplierName nvarchar(255) NOT NULL,
    ContactNumber varchar(11) NOT NULL
)

CREATE TABLE Production.Category
(
    CategoryID int IDENTITY(1,1) PRIMARY KEY,
    CategoryName nvarchar(255) NOT NULL
)

CREATE TABLE Production.Item
(
    ItemID int IDENTITY(1,1) PRIMARY KEY,
    CategoryID int FOREIGN KEY REFERENCES Production.Category(CategoryID) ON DELETE SET NULL,
    ItemName nvarchar(255) NOT NULL,
    ItemDescription nvarchar(500),
    ItemColor varchar(50)
)

CREATE TABLE Production.Stock (
	StockID int IDENTITY(1,1) PRIMARY KEY,
	ItemID int FOREIGN KEY REFERENCES Production.Item(ItemID) ON DELETE CASCADE,
	TotalStock int NOT NULL CHECK (TotalStock >= 0)
)

CREATE TABLE Production.SupplyOrder
(
    OrderID int IDENTITY(1,1) PRIMARY KEY,
    SupplierID int FOREIGN KEY REFERENCES Production.Supplier(SupplierID) ON DELETE SET NULL,
    OrderDate datetime DEFAULT getDate(),
    OrderStatus varchar(50) NOT NULL CHECK (OrderStatus IN ('Cancelled', 'Pending', 'Delivered'))
)

CREATE TABLE Production.SupplyOrderItem
(
    OrderItemID int IDENTITY(1,1) PRIMARY KEY,
    OrderID int FOREIGN KEY REFERENCES Production.SupplyOrder(OrderID) ON DELETE CASCADE,
    ItemID int FOREIGN KEY REFERENCES Production.Item(ItemID) ON DELETE SET NULL,
    OrderItemCost numeric(10,2) NOT NULL CHECK (OrderItemCost > 0),
    Quantity int NOT NULL CHECK (Quantity > 0)
)

CREATE TABLE Sales.SaleItem
(
    SaleItemID int IDENTITY(1,1) PRIMARY KEY,
    ItemID int NOT NULL FOREIGN KEY REFERENCES Production.Item(ItemID) ON DELETE CASCADE,
    ItemPrice numeric(10,2) NOT NULL CHECK (ItemPrice > 0),
    ItemQuantity int NOT NULL CHECK (ItemQuantity >= 0),
    MeasurementSystem varchar(10) CHECK (MeasurementSystem IN ('Alpha', 'US', 'EU', 'UK')),
    NumericSize numeric(3, 1),
    AlphaSize varchar(10)
)

CREATE TABLE Sales.SalesTransaction
(
    TransactionID int IDENTITY(1,1) PRIMARY KEY,
    CustomerID int FOREIGN KEY REFERENCES Customer.Customer(CustomerID) ON DELETE SET NULL,
    TransactionDate datetime DEFAULT getDate(),
    PaymentType varchar(20) NOT NULL CHECK (PaymentType IN ('Cash', 'EWallet', 'Online Bank')),
    EWalletType varchar(20) CHECK (EWalletType IN ('Maya', 'GCash')),
    BankName varchar(100),
    MobileNumber varchar(11),
    AccountNumber varchar(255),
    AccountName varchar(255),
    ReferenceNumber varchar(255)
)

CREATE TABLE Sales.TransactionItem
(
    TransactionItemID int IDENTITY(1,1) PRIMARY KEY,
    TransactionID int FOREIGN KEY REFERENCES Sales.SalesTransaction(TransactionID) ON DELETE CASCADE,
    SaleItemID int FOREIGN KEY REFERENCES Sales.SaleItem(SaleItemID) ON DELETE SET NULL,
    Quantity int NOT NULL CHECK (Quantity > 0)
)

CREATE TABLE Sales.Delivery
( 
    DeliveryID int IDENTITY(1,1) PRIMARY KEY,
    TransactionID int FOREIGN KEY REFERENCES Sales.SalesTransaction(TransactionID) ON DELETE SET NULL,
    DeliveryAddress nvarchar(MAX) NOT NULL,
    DeliveryDate datetime DEFAULT getDate(),
    DeliveryStatus varchar(50) NOT NULL CHECK (DeliveryStatus IN ('Cancelled', 'Pending', 'Shipped', 'Out for Delivery', 'Delivered', 'Failed')),
    PaidAmount numeric(10,2) NULL
)