CREATE VIEW VW_SalesTransactionTotal
AS
    SELECT ti.TransactionID, SUM(ti.Quantity * si.ItemPrice) AS TotalAmount
    FROM Sales.TransactionItem ti
    JOIN Sales.SaleItem si
    ON si.SaleItemID = ti.SaleItemID
    GROUP BY ti.TransactionID

GO

CREATE VIEW VW_CustomerDeliveryStatusInfo
AS
    SELECT d.DeliveryStatus, c.CustomerID, COUNT(*) AS DeliveryStatusCount
    FROM Sales.Delivery d
    JOIN Sales.SalesTransaction st
    ON d.TransactionID = st.TransactionID
    JOIN Customer.Customer c
    ON st.CustomerID = c.CustomerID
    WHERE c.CustomerID = 1
    GROUP BY d.DeliveryStatus, c.CustomerID

GO

CREATE VIEW VW_CustomerSpending
AS
    SELECT d.DeliveryStatus, c.CustomeriD, SUM(stt.TotalAmount) AS TotalSpending
    FROM VW_SalesTransactionTotal stt
    JOIN Sales.SalesTransaction st
    ON st.TransactionID = stt.TransactionID
    JOIN Sales.Delivery d
    ON d.TransactionID = st.TransactionID
    JOIN Customer.Customer c
    ON c.CustomerID = st.CustomerID
    GROUP BY d.DeliveryStatus, c.CustomerID

GO

CREATE VIEW VW_ItemInfo
AS 
    SELECT i.ItemID, c.CategoryID, c.CategoryName, i.ItemName, i.ItemDescription, i.ItemColor
    FROM Production.Item i
    JOIN Production.Category c
    ON i.CategoryID = c.CategoryID

GO

CREATE VIEW VW_SaleItemInfo
AS
    SELECT si.SaleItemID, i.ItemID,  c.CategoryName, i.ItemName, i.ItemDescription, si.ItemPrice, si.ItemQuantity, i.ItemColor, si.MeasurementSystem, si.NumericSize, si.AlphaSize
    FROM Production.Item i
    JOIN Production.Category c
    ON i.CategoryID = c.CategoryID
    JOIN Sales.SaleItem si
    ON i.ItemID = si.ItemID

GO

CREATE VIEW VW_SaleItemPurchased
AS
    SELECT SaleItemID, COUNT(TransactionID) AS TotalTransactions, SUM(Quantity) AS TotalPurchases
    FROM Sales.TransactionItem
    GROUP BY SaleItemID

GO
    
CREATE VIEW VW_ItemPurchased
AS
    SELECT i.ItemID, SUM(TotalTransactions) AS TotalTransactions, SUM(TotalPurchases) AS ToTalPurchases
    FROM VW_SaleItemPurchased
    LEFT JOIN Sales.SaleItem si
    ON si.SaleItemID = VW_SaleItemPurchased.SaleItemID
    LEFT JOIN Production.Item i
    ON i.ItemID = si.ItemID
    GROUP BY i.ItemID

GO

CREATE VIEW VW_CategoryItemTotalPrice
AS
    SELECT c.CategoryID, SUM(si.ItemPrice) AS TotalPrice
    FROM Production.Category c
    JOIN Production.Item i
    ON c.CategoryID = i.CategoryID
    JOIN Sales.SaleItem si
    ON i.ItemID = si.ItemID
    GROUP BY c.CategoryID

GO

CREATE VIEW VW_SupplyOrderTotal
AS
    SELECT so.OrderID, SUM(soi.Quantity * soi.OrderItemCost) AS TotalAmount
    FROM Production.SupplyOrder so
    JOIN Production.SupplyOrderItem soi
    ON so.OrderID = soi.OrderID
    GROUP BY so.OrderID

GO

CREATE PROCEDURE PROC_GetSupplierSupplyItemCount
    @SupplierID int,
    @OrderStatus nvarchar(100)
AS
    SELECT SUM(soi.Quantity) AS TotalSupplyItems
    FROM Production.Supplier s
    JOIN Production.SupplyOrder so
    ON s.SupplierID = so.SupplierID
    JOIN Production.SupplyOrderItem soi
    ON so.OrderID = soi.OrderID
    WHERE s.SupplierID = @SupplierID AND so.OrderStatus = @OrderStatus

GO

CREATE PROCEDURE PROC_GetSupplierSupplyOrderCount
    @SupplierID int,
    @OrderStatus nvarchar(100)
AS
    SELECT COUNT(*) AS TotalSupplyOrders
    FROM Production.SupplyOrder
    WHERE SupplierID = @SupplierID AND OrderStatus = @OrderStatus

GO