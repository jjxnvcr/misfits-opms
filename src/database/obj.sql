CREATE VIEW VW_DailyRevenue
AS
    SELECT SUM(ti.Quantity * si.ItemPrice) AS TotalRevenue
    FROM Sales.TransactionItem ti
    INNER JOIN Sales.SaleItem si
    ON ti.SaleItemID = si.SaleItemID
    INNER JOIN Sales.SalesTransaction st
    ON ti.TransactionID = st.TransactionID
    INNER JOIN Sales.Delivery d
    ON st.TransactionID = d.TransactionID
    WHERE CAST(st.TransactionDate AS DATE) = CAST(GETDATE() AS DATE) AND d.DeliveryStatus = 'Delivered'
GO

CREATE VIEW VW_WeeklyRevenue
AS 
    SELECT SUM(ti.Quantity * si.ItemPrice) AS TotalRevenue
    FROM Sales.TransactionItem ti
    INNER JOIN Sales.SaleItem si
    ON ti.SaleItemID = si.SaleItemID
    INNER JOIN Sales.SalesTransaction st
    ON ti.TransactionID = st.TransactionID
    INNER JOIN Sales.Delivery d
    ON st.TransactionID = d.TransactionID
    WHERE CAST(st.TransactionDate AS DATE) >= DATEADD(DAY, 1 - DATEPART(WEEKDAY, GETDATE()), CAST(GETDATE() AS DATE)) AND CAST(st.TransactionDate AS DATE) < DATEADD(DAY, 8 - DATEPART(WEEKDAY, GETDATE()), CAST(GETDATE() AS DATE)) AND d.DeliveryStatus = 'Delivered'
GO

CREATE VIEW VW_MonthlyRevenue
AS
    SELECT SUM(ti.Quantity * si.ItemPrice) AS TotalRevenue
    FROM Sales.TransactionItem ti
    INNER JOIN Sales.SaleItem si
    ON ti.SaleItemID = si.SaleItemID
    INNER JOIN Sales.SalesTransaction st
    ON ti.TransactionID = st.TransactionID
    WHERE YEAR(st.TransactionDate) = YEAR(GETDATE()) AND MONTH(st.TransactionDate) = MONTH(GETDATE()) AND st.TransactionID IN (SELECT TransactionID FROM Sales.Delivery WHERE DeliveryStatus = 'Delivered')

GO

CREATE VIEW VW_TopSellingItems
AS
    SELECT si.SaleItemID, si.ItemID, si.ItemPrice, si.MeasurementSystem, si.NumericSize, si.AlphaSize, SUM(ti.Quantity) AS TotalQuantity
    FROM Sales.SaleItem si
    INNER JOIN Sales.TransactionItem ti
    ON si.SaleItemID = ti.SaleItemID
    WHERE ti.TransactionID IN (SELECT TransactionID FROM Sales.Delivery WHERE DeliveryStatus = 'Delivered')
    GROUP BY si.SaleItemID, si.ItemID, si.ItemPrice, si.MeasurementSystem, si.NumericSize, si.AlphaSize

GO

CREATE VIEW VW_SalesTransactionTotal
AS
    SELECT ti.TransactionID, SUM(ti.Quantity * si.ItemPrice) AS TotalAmount
    FROM Sales.TransactionItem ti
    JOIN Sales.SaleItem si
    ON si.SaleItemID = ti.SaleItemID
    GROUP BY ti.TransactionID

GO  

CREATE VIEW VW_SaleTrend
AS
    SELECT CAST(st.TransactionDate AS DATE) AS TransactionDate, SUM(stt.TotalAmount) AS TotalRevenue
    FROM Sales.SalesTransaction st
    INNER JOIN VW_SalesTransactionTotal stt
    ON st.TransactionID = stt.TransactionID
    GROUP BY st.TransactionDate
    ORDER BY st.TransactionDate
GO

CREATE VIEW VW_AverageTransactionSpending
AS
    SELECT SUM(ti.Quantity * si.ItemPrice) / COUNT(DISTINCT st.TransactionID) AS AverageSpending
    FROM Sales.TransactionItem ti
    JOIN Sales.SaleItem si
    ON ti.SaleItemID = si.SaleItemID
    JOIN Sales.SalesTransaction st
    ON ti.TransactionID = st.TransactionID

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

CREATE VIEW VW_ItemAvailableStock
AS
    SELECT i.ItemID, s.TotalStock - SUM(si.ItemQuantity) AS AvailableStock
    FROM Production.Item i
    INNER JOIN Production.Stock s
    ON i.ItemID = s.ItemID
    INNER JOIN Sales.SaleItem si
    ON i.ItemID = si.ItemID
    GROUP BY i.ItemID, s.TotalStock

GO

CREATE VIEW VW_SaleItemInfo
AS
    SELECT si.SaleItemID, i.ItemID,  c.CategoryName, i.ItemName, i.ItemDescription, si.ItemPrice, si.ItemQuantity, i.ItemColor, si.MeasurementSystem, si.NumericSize, si.AlphaSize
    FROM Production.Item i
    JOIN Production.Category c
    ON i.CategoryID = c.CategoryID
    JOIN Sales.SaleItem si
    ON i.ItemID = si.ItemID
    WHERE c.CategoryName IN ('Shoes', 'Pants', 'Skirt', 'Shorts', 'Jeans')

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

CREATE TRIGGER Sales.TR_OnTransactionCreate
ON Sales.TransactionItem
AFTER INSERT
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION
            UPDATE si
            SET si.ItemQuantity = si.ItemQuantity - i.Quantity
            FROM Sales.SaleItem si
                INNER JOIN inserted i
                ON si.SaleItemID = i.SaleItemID
        COMMIT TRANSACTION
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION
        DECLARE @ErrorMsg nvarchar(400)
        SET @ErrorMsg = ERROR_MESSAGE()
        RAISERROR (@ErrorMsg, 16, 1)
    END CATCH
END

GO

CREATE TRIGGER Sales.TR_OnTransactionCancel
ON Sales.SalesTransaction
INSTEAD OF DELETE
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION
            UPDATE si
            SET si.ItemQuantity = si.ItemQuantity + ti.Quantity
            FROM Sales.SaleItem si
                INNER JOIN Sales.TransactionItem ti
                ON si.SaleItemID = ti.SaleItemID
                INNER JOIN deleted d
                ON ti.TransactionID = d.TransactionID

            UPDATE dlv
            SET dlv.DeliveryStatus = 'Cancelled'
            FROM Sales.Delivery dlv
                INNER JOIN deleted d
                ON dlv.TransactionID = d.TransactionID

            DELETE ti
            FROM Sales.TransactionItem ti
                INNER JOIN deleted d 
                ON ti.TransactionID = d.TransactionID;

           DELETE st
            FROM Sales.SalesTransaction st
                INNER JOIN deleted d 
                ON st.TransactionID = d.TransactionID;
        COMMIT TRANSACTION
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION
        DECLARE @ErrorMsg nvarchar(400)
        SET @ErrorMsg = ERROR_MESSAGE()
        RAISERROR (@ErrorMsg, 16, 1)
    END CATCH
END

GO

CREATE TRIGGER Sales.TR_OnDeliveryFailure
ON Sales.Delivery
AFTER UPDATE
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION
            IF EXISTS (
                SELECT 1
                FROM inserted i
                INNER JOIN deleted d
                ON i.DeliveryID = d.DeliveryID
                WHERE d.DeliveryStatus = 'Pending' AND i.DeliveryStatus = 'Failed'
            )
            BEGIN
                UPDATE si
                SET si.ItemQuantity = si.ItemQuantity + ti.Quantity
                FROM Sales.SaleItem si
                    INNER JOIN Sales.TransactionItem ti
                    ON si.SaleItemID = ti.SaleItemID
                    INNER JOIN inserted i
                    ON ti.TransactionID = i.TransactionID
            END
        COMMIT TRANSACTION
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION
        DECLARE @ErrorMsg nvarchar(400)
        SET @ErrorMsg = ERROR_MESSAGE()
        RAISERROR (@ErrorMsg, 16, 1)
    END CATCH
END

GO

CREATE TRIGGER Sales.TR_OnOrderForDelivery
ON Sales.Delivery
AFTER UPDATE
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION
            IF EXISTS (
                SELECT 1
                FROM inserted i
                INNER JOIN deleted d
                ON d.DeliveryID = i.DeliveryID
                WHERE d.DeliveryStatus = 'Shipped' AND i.DeliveryStatus = 'Out for Delivery'
            )
            BEGIN
                UPDATE d
                SET d.DeliveryDate = getDate()
                FROM Sales.Delivery d
                INNER JOIN inserted i
                ON d.DeliveryID = i.DeliveryID
            END
        COMMIT TRANSACTION
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION
        DECLARE @ErrorMsg nvarchar(400)
        SET @ErrorMsg = ERROR_MESSAGE()
        RAISERROR (@ErrorMsg, 16, 1)
    END CATCH
END

GO

CREATE TRIGGER Production.TR_OnOrderArrival
ON Production.SupplyOrder
AFTER UPDATE
AS
IF (UPDATE (OrderStatus))
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION
            IF EXISTS (
                SELECT 1
                FROM inserted i
                INNER JOIN deleted d
                ON i.OrderID = d.OrderID
                WHERE d.OrderStatus = 'Pending' AND i.OrderStatus = 'Delivered'
            )
            BEGIN
                UPDATE s
                SET s.TotalStock = s.TotalStock + soi.Quantity
                FROM Production.Stock s
                    INNER JOIN Production.SupplyOrderItem soi
                    ON s.ItemID = soi.ItemID
            END
        COMMIT TRANSACTION
    END TRY 
    BEGIN CATCH
        ROLLBACK TRANSACTION
        DECLARE @ErrorMsg nvarchar(400)
        SET @ErrorMsg = ERROR_MESSAGE()
        RAISERROR (@ErrorMsg, 16, 1)
    END CATCH
END

GO

CREATE TRIGGER Production.TR_OnSupplierDelete
ON Production.Supplier
INSTEAD OF DELETE
AS
BEGIN
    BEGIN TRY
        BEGIN TRANSACTION
            UPDATE so
            SET so.OrderStatus = 'Cancelled'
            FROM Production.SupplyOrder so
            WHERE so.SupplierID IN (SELECT SupplierID FROM deleted)
            AND so.OrderStatus = 'Pending'

            DELETE FROM Production.Supplier
            WHERE SupplierID IN (SELECT SupplierID FROM deleted)
        COMMIT TRANSACTION
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION
        DECLARE @ErrorMsg nvarchar(400)
        SET @ErrorMsg = ERROR_MESSAGE()
        RAISERROR (@ErrorMsg, 16, 1)
    END CATCH
END