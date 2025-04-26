-- =============================================
-- Create Schemas (Run if not already created)
-- =============================================
-- CREATE SCHEMA Sales;
-- GO
-- CREATE SCHEMA Production;
-- GO
-- CREATE SCHEMA Customer;
-- GO

-- =============================================
-- Create Tables (Run if not already created)
-- =============================================
-- Execute the CREATE TABLE statements provided in the prompt first.


-- =============================================
-- INSERT Data into Production.Category (Approx 15 Records)
-- =============================================
PRINT 'Inserting Categories...';
INSERT INTO Production.Category (CategoryName) VALUES
('T-Shirt'), ('Blouse'), ('Dress'), ('Pants'), ('Jeans'),
('Shorts'), ('Skirt'), ('Jacket'), ('Hoodie'), ('Sweater'),
('Cardigan'), ('Polo Shirt'), ('Shoes'), ('Bag'), ('Accessories');
GO

-- =============================================
-- INSERT Data into Production.Supplier (Approx 35 Records)
-- =============================================
PRINT 'Inserting Suppliers...';
INSERT INTO Production.Supplier (SupplierName, ContactNumber) VALUES
('Bulacan Ukay Hub', '09171110011'), ('Meycauayan Bale Central', '09282220022'),
('Malolos Thrift Goods', '09983330033'), ('SJDM Surplus', '09154440044'),
('Baliuag RTW Source', '09065550055'), ('Marilao Garments', '09276660066'),
('Sta Maria Clothing', '09187770077'), ('Pandi Textile Traders', '09398880088'),
('Bocaue Fashion Outlet', '09459990099'), ('Guiguinto Apparel', '09171010111'),
('Plaridel Imports Co.', '09202020222'), ('Calumpit Vintage', '09953030333'),
('Hagonoy Fashion Finds', '09164040444'), ('Japan Surplus Bulacan', '09085050555'),
('Korea Ukay Network PH', '09256060666'), ('US Bale Goods Luzon', '09177070777'),
('Santos RTW Wholesaler', '09288080888'), ('Reyes Textile Recycling', '09989090999'),
('Cruz Garment Solutions', '09151212112'), ('Garcia Apparel Trading', '09062323223'),
('Mendoza Clothing Imports', '09273434334'), ('Flores Fashion Hub', '09184545445'),
('Gonzales Bulk Deals', '09395656556'), ('Castillo Vintage Supply', '09456767667'),
('Bautista Ukay Source', '09177878778'), ('Villanueva Secondhand', '09208989889'),
('Fernandez Thrift Co.', '09959090990'), ('Lopez RTW Express', '09161121112'),
('Martin Preloved Clothes', '09082232223'), ('De Leon Bale Breakers', '09253343334'),
('Soriano Surplus Central', '09174454445'), ('Salazar Clothing Network', '09285565556'),
('Mercado Ukay Express', '09986676667'), ('Aquino Textile Hub', '09157787778'),
('Santiago Garments Inc.', '09068898889');
GO

-- =============================================
-- INSERT Data into Customer.Customer (Approx 35 Records - Bulacan Focus)
-- =============================================
PRINT 'Inserting Customers (Bulacan)...';
INSERT INTO Customer.Customer (FirstName, LastName, ContactNumber, Street, Barangay, City, Province) VALUES
('Juan', 'Dela Cruz', '09171234501', '12 Rizal St', 'Poblacion', 'Malolos', 'Bulacan'),
('Maria', 'Santos', '09287654302', '45 Bonifacio Ave', 'Banga 1st', 'Plaridel', 'Bulacan'),
('Jose', 'Garcia', '09981112203', 'Unit 10, Rosewood Apt', 'Caingin', 'Malolos', 'Bulacan'),
('Anna', 'Reyes', '09159876504', 'Sitio Libis', 'Lolomboy', 'Bocaue', 'Bulacan'),
('Paolo', 'Cruz', '09065551105', 'Blk 5 Lot 8, Greenfields Subd', 'Bagbaguin', 'Santa Maria', 'Bulacan'),
('Sofia', 'Ramos', '09273334406', '101 Acacia Lane', 'Maysan', 'Meycauayan', 'Bulacan'),
('Miguel', 'Mendoza', '09187778807', '9 Sampaguita St', 'San Juan', 'Balagtas', 'Bulacan'),
('Isabella', 'Gonzales', '09391212108', 'Phase 3, Northwinds Vill', 'Tungkong Mangga', 'San Jose del Monte', 'Bulacan'),
('Carlos', 'Flores', '09456789009', '22 Narra Road', 'Longos', 'Malolos', 'Bulacan'),
('Bea', 'Castillo', '09178889910', 'House 15, Villa Esperanza', 'Ibayo', 'Marilao', 'Bulacan'),
('Rafael', 'Bautista', '09201122311', 'Purok 3', 'Pulong Buhangin', 'Santa Maria', 'Bulacan'),
('Angela', 'Villanueva', '09952233412', '77 MacArthur Highway', 'Saluysoy', 'Meycauayan', 'Bulacan'),
('Luis', 'Fernandez', '09163344513', 'Unit 5B, Tower Condo', 'Mojon', 'Malolos', 'Bulacan'),
('Clara', 'Lopez', '09084455614', '18 Dahlia St, Camella Homes', 'Sto. Cristo', 'San Jose del Monte', 'Bulacan'),
('Antonio', 'Martin', '09255566715', 'Near Municipal Hall', 'Poblacion', 'Guiguinto', 'Bulacan'),
('Gabriela', 'De Leon', '09176677816', 'Sitio Sulok', 'San Jose', 'Paombong', 'Bulacan'),
('Andres', 'Soriano', '09287788917', '456 Provincial Road', 'San Sebastian', 'Hagonoy', 'Bulacan'),
('Teresa', 'Salazar', '09988899018', 'Zone 5, Riverside Comp.', 'Iba O Este', 'Calumpit', 'Bulacan'),
('Ricardo', 'Mercado', '09159900119', '11 Cattleya St', 'Poblacion', 'Pulilan', 'Bulacan'),
('Pilar', 'Aquino', '09061122320', 'Lot 8, Phase 2, Golden Hills', 'Muzon', 'San Jose del Monte', 'Bulacan'),
('Felipe', 'Santiago', '09272233421', 'House 7, Mountain View Est', 'San Manuel', 'San Jose del Monte', 'Bulacan'),
('Elena', 'Diaz', '09183344522', 'Unit 2B, Cedar Apartelle', 'Poblacion', 'Baliuag', 'Bulacan'),
('Ramon', 'Navarro', '09394455623', '90 Gov. Halili Ave', 'Turo', 'Bocaue', 'Bulacan'),
('Catalina', 'Pascual', '09455566724', 'Purok 7, Sitio Maligaya', 'Lias', 'Marilao', 'Bulacan'),
('Eduardo', 'Jimenez', '09176677825', '55 Cruz Compound', 'Bulihan', 'Malolos', 'Bulacan'),
('Victoria', 'Torres', '09207788926', '12 Santan St', 'Sta. Clara', 'Santa Maria', 'Bulacan'),
('Manuel', 'Vargas', '09958899027', 'Apt 3, Rosas Bldg', 'Bagong Nayon', 'Baliuag', 'Bulacan'),
('Lourdes', 'Chavez', '09169900128', 'Sitio Gitna', 'Tabang', 'Guiguinto', 'Bulacan'),
('Benito', 'Romero', '09081122329', 'Near Barangay Hall', 'Panginay', 'Balagtas', 'Bulacan'),
('Anita', 'Gutierrez', '09252233430', '143 National Road', 'Sto. Niño', 'Meycauayan', 'Bulacan'),
('Gregorio', 'Francisco', '09173344531', '88 Paradise Farm', 'Gaya-gaya', 'San Jose del Monte', 'Bulacan'),
('Consuelo', 'Ignacio', '09284455632', '10 Yellowbell St', 'Catmon', 'Malolos', 'Bulacan'),
('Mateo', 'Ocampo', '09985566733', 'Lot 14, Block 2, Maryhomes', 'Perez', 'Meycauayan', 'Bulacan'),
('Patricia', 'Abad', '09156677834', 'Purok 1, Looban Area', 'Wakas', 'Bocaue', 'Bulacan'),
('Julio', 'Del Rosario', '09067788935', 'Unit 1, Sunshine Apt', 'Prenza 1', 'Marilao', 'Bulacan');
GO

-- =============================================
-- INSERT Data into Production.Item (Approx 40 Records for Variety)
-- =============================================
PRINT 'Inserting Items...';
-- Assuming CategoryIDs are 1 to 15 based on the order above
INSERT INTO Production.Item (CategoryID, ItemName, ItemDescription, ItemColor) VALUES
(1, 'Vintage Band T-Shirt', 'Slightly faded print, soft cotton', 'Black'),
(1, 'Plain Crew Neck T-Shirt', 'Good condition, basic wear', 'White'),
(1, 'Graphic Print Tee', 'Modern design, small stain on back', 'Gray'),
(1, 'Striped Ringer Tee', 'Retro style, good condition', 'White/Red'),
(2, 'Floral Chiffon Blouse', 'Lightweight, ruffled sleeves', 'Pink'),
(2, 'Silk Button-Down Blouse', 'Elegant, like new', 'Navy'),
(2, 'Cotton Peasant Blouse', 'Embroidered details, comfortable fit', 'Beige'),
(2, 'Lace Trim Camisole', 'Adjustable straps, delicate', 'Cream'),
(3, 'Summer Maxi Dress', 'Flowy, spaghetti straps', 'Yellow Floral'),
(3, 'Denim Shirt Dress', 'Button-front, with belt', 'Blue Denim'),
(3, 'Little Black Dress', 'Classic cocktail dress, knee-length', 'Black'),
(3, 'Wrap Dress', 'Polka dot print, flattering fit', 'Red/White Polka'),
(4, 'Khaki Chino Pants', 'Straight cut, minor wear', 'Khaki'),
(4, 'Wide Leg Trousers', 'High-waisted, linen blend', 'Cream'),
(4, 'Cotton Jogger Pants', 'Elastic waist and cuffs, comfortable', 'Black'),
(5, 'Slim Fit Denim Jeans', 'Slightly distressed, stretchable', 'Dark Blue'),
(5, 'Mom Jeans', 'High-waisted, vintage style', 'Light Wash Blue'),
(5, 'Black Skinny Jeans', 'Basic, good condition', 'Black'),
(6, 'High-Waist Denim Shorts', 'Frayed hem, good condition', 'Black Denim'),
(6, 'Cargo Shorts', 'Multiple pockets, durable cotton', 'Olive Green'),
(6, 'Printed Dolphin Shorts', 'Lightweight, casual wear', 'Blue Pattern'),
(7, 'Plaid Mini Skirt', 'A-line cut, wool blend', 'Red Plaid'),
(7, 'Pleated Midi Skirt', 'Flowy, elastic waistband', 'Maroon'),
(7, 'Denim Pencil Skirt', 'Knee-length, back slit', 'Dark Denim'),
(8, 'Denim Jacket', 'Classic trucker style, worn-in look', 'Medium Blue'),
(8, 'Windbreaker Jacket', 'Lightweight, hooded', 'Blue/Green'),
(8, 'Faux Leather Biker Jacket', 'Zippered details, edgy style', 'Black'),
(9, 'Pullover Hoodie', 'Fleece lined, front pocket', 'Gray Heather'),
(9, 'Zip-Up Hoodie', 'Basic design, good condition', 'Navy'),
(10, 'Cable Knit Sweater', 'Warm, wool blend', 'Cream'),
(10, 'V-Neck Pullover Sweater', 'Fine knit, small hole near cuff', 'Dark Green'),
(11, 'Long Cardigan', 'Open front, soft knit', 'Gray'),
(11, 'Button-Up Cardigan', 'Cropped style, pastel color', 'Light Blue'),
(12, 'Striped Polo Shirt', 'Classic fit, cotton pique', 'White/Blue Stripe'),
(12, 'Plain Polo Shirt', 'Basic design, slightly faded', 'Red'),
(13, 'Canvas Sneakers', 'Low top, used condition', 'White'),
(13, 'Leather Ankle Boots', 'Side zip, minor scuffs', 'Brown'),
(14, 'Canvas Tote Bag', 'Graphic print, sturdy', 'Natural'),
(14, 'Leather Crossbody Bag', 'Adjustable strap, vintage look', 'Black'),
(15, 'Woven Belt', 'Braided design, fits most', 'Brown'),
(15, 'Silk Scarf', 'Abstract pattern', 'Multicolor');
GO

-- =============================================
-- INSERT Data into Production.Stock (Approx 40 Records - Initial Stock Levels)
-- =============================================
INSERT INTO Production.Stock (ItemID, TotalStock) VALUES
(1, 10), (2, 20), (3, 15), (4, 8), (5, 5),
(6, 12), (7, 18), (8, 7), (9, 3), (10, 9),
(11, 6), (12, 11), (13, 14), (14, 10), (15, 25),
(16, 4), (17, 16), (18, 5), (19, 12), (20, 8),
(21, 3), (22, 7), (23, 9), (24, 6), (25, 2),
(26, 4), (27, 3), (28, 5), (29, 6), (30, 4),
(31, 8), (32, 7), (33, 5), (34, 9), (35, 11),
(36, 3), (37, 4), (38, 6), (39, 10), (40, 20),
(41, 15);
GO

-- =============================================
-- INSERT Data into Production.SupplyOrder (Approx 35 Records)
-- =============================================
PRINT 'Inserting Supply Orders...';
-- Assuming SupplierIDs are 1 to 35
INSERT INTO Production.SupplyOrder (SupplierID, OrderDate, OrderStatus) VALUES
(1, DATEADD(month, -6, GETDATE()), 'Delivered'), (2, DATEADD(month, -6, GETDATE()), 'Delivered'),
(3, DATEADD(month, -5, GETDATE()), 'Delivered'), (4, DATEADD(month, -5, GETDATE()), 'Delivered'),
(5, DATEADD(month, -5, GETDATE()), 'Delivered'), (6, DATEADD(month, -4, GETDATE()), 'Delivered'),
(7, DATEADD(month, -4, GETDATE()), 'Delivered'), (8, DATEADD(month, -4, GETDATE()), 'Delivered'),
(9, DATEADD(month, -3, GETDATE()), 'Delivered'), (10, DATEADD(month, -3, GETDATE()), 'Delivered'),
(11, DATEADD(month, -3, GETDATE()), 'Delivered'), (12, DATEADD(month, -3, GETDATE()), 'Delivered'),
(13, DATEADD(month, -2, GETDATE()), 'Delivered'), (14, DATEADD(month, -2, GETDATE()), 'Delivered'),
(15, DATEADD(month, -2, GETDATE()), 'Delivered'), (16, DATEADD(month, -2, GETDATE()), 'Delivered'),
(17, DATEADD(month, -1, GETDATE()), 'Delivered'), (18, DATEADD(month, -1, GETDATE()), 'Delivered'),
(19, DATEADD(month, -1, GETDATE()), 'Delivered'), (20, DATEADD(month, -1, GETDATE()), 'Delivered'),
(21, DATEADD(week, -3, GETDATE()), 'Delivered'), (22, DATEADD(week, -3, GETDATE()), 'Delivered'),
(23, DATEADD(week, -2, GETDATE()), 'Delivered'), (24, DATEADD(week, -2, GETDATE()), 'Delivered'),
(25, DATEADD(week, -2, GETDATE()), 'Delivered'), (26, DATEADD(week, -1, GETDATE()), 'Delivered'),
(27, DATEADD(week, -1, GETDATE()), 'Delivered'), (28, DATEADD(week, -1, GETDATE()), 'Pending'),
(29, DATEADD(day, -5, GETDATE()), 'Pending'), (30, DATEADD(day, -4, GETDATE()), 'Pending'),
(31, DATEADD(day, -3, GETDATE()), 'Pending'), (32, DATEADD(day, -3, GETDATE()), 'Pending'),
(1, DATEADD(day, -2, GETDATE()), 'Pending'), -- Repeat supplier 1
(5, DATEADD(day, -2, GETDATE()), 'Pending'), -- Repeat supplier 5
(10, DATEADD(day, -1, GETDATE()), 'Pending'); -- Repeat supplier 10
GO

-- =============================================
-- INSERT Data into Production.SupplyOrderItem (Approx 70 Records)
-- =============================================
PRINT 'Inserting Supply Order Items...';
-- Assuming OrderIDs 1-35 and ItemIDs 1-41
INSERT INTO Production.SupplyOrderItem (OrderID, ItemID, OrderItemCost, Quantity) VALUES
(1, 1, 75.00, 20), (1, 5, 150.00, 10), (1, 15, 80.00, 25),
(2, 2, 50.00, 30), (2, 8, 180.00, 15),
(3, 3, 80.00, 25), (3, 12, 100.00, 20), (3, 20, 90.00, 30),
(4, 4, 120.00, 15), (4, 16, 100.00, 18),
(5, 6, 110.00, 12), (5, 18, 250.00, 8), (5, 25, 120.00, 15),
(6, 7, 95.00, 22), (6, 21, 180.00, 10),
(7, 9, 160.00, 14), (7, 26, 130.00, 15), (7, 30, 150.00, 12),
(8, 10, 100.00, 18), (8, 28, 160.00, 30),
(9, 11, 140.00, 16), (9, 31, 170.00, 10), (9, 35, 60.00, 20),
(10, 13, 115.00, 20), (10, 32, 70.00, 25),
(11, 14, 85.00, 28), (11, 36, 55.00, 40), (11, 40, 40.00, 50),
(12, 17, 125.00, 15), (12, 1, 75.00, 15),
(13, 19, 90.00, 12), (13, 4, 120.00, 10), (13, 38, 65.00, 30),
(14, 22, 190.00, 10), (14, 7, 95.00, 20),
(15, 23, 170.00, 15), (15, 11, 140.00, 12), (15, 41, 35.00, 60),
(16, 24, 165.00, 11), (16, 14, 85.00, 25),
(17, 27, 70.00, 13), (17, 17, 125.00, 10), (17, 33, 130.00, 18),
(18, 29, 155.00, 14), (18, 20, 90.00, 25),
(19, 34, 50.00, 18), (19, 23, 170.00, 8), (19, 37, 200.00, 10),
(20, 39, 60.00, 25), (20, 26, 130.00, 10),
(21, 2, 50.00, 12), (21, 29, 155.00, 10),
(22, 3, 80.00, 35), (22, 31, 170.00, 15),
(23, 5, 150.00, 50), (23, 36, 55.00, 20),
(24, 6, 110.00, 20), (24, 39, 60.00, 18),
(25, 8, 180.00, 15), (25, 40, 40.00, 30),
(26, 9, 160.00, 10), (26, 1, 75.00, 15),
(27, 10, 100.00, 12), (27, 4, 120.00, 10),
(28, 11, 140.00, 15), (28, 7, 95.00, 17),
(29, 13, 115.00, 8), (29, 10, 100.00, 9),
(30, 14, 85.00, 20), (30, 16, 100.00, 11),
(31, 17, 125.00, 22), (31, 19, 90.00, 18),
(32, 21, 180.00, 16), (32, 22, 190.00, 6),
(33, 24, 165.00, 9), (33, 25, 120.00, 14),
(34, 28, 160.00, 11), (34, 30, 150.00, 13),
(35, 32, 70.00, 13), (35, 34, 50.00, 25);
GO

-- =============================================
-- INSERT Data into Sales.SaleItem (Approx 50 Records for Stock/Unsold Variety)
-- Represents items IN STOCK for sale. Some will be sold, some not. Some have Qty > 1.
-- =============================================
PRINT 'Inserting Sale Items (Stock)...';
-- Assuming ItemIDs 1-41 exist in Production.Item
-- Price should be higher than cost. Quantity represents stock.
INSERT INTO Sales.SaleItem (ItemID, ItemPrice, ItemQuantity, MeasurementSystem, NumericSize, AlphaSize) VALUES
-- T-Shirts (ItemID 1, 2, 3, 4)
(1, 150.00, 2, 'Alpha', NULL, 'M'), -- SaleItemID 1 (Sold 1)
(1, 150.00, 1, 'Alpha', NULL, 'L'), -- SaleItemID 2 (Unsold)
(2, 100.00, 5, 'Alpha', NULL, 'L'), -- SaleItemID 3 (Sold 2+1+1+1=5)
(2, 100.00, 2, 'Alpha', NULL, 'M'), -- SaleItemID 4 (Unsold)
(3, 160.00, 3, 'Alpha', NULL, 'S'), -- SaleItemID 5 (Sold 1+1+1=3)
(4, 140.00, 1, 'Alpha', NULL, 'M'), -- SaleItemID 6 (Unsold)
-- Blouses (ItemID 5, 6, 7, 8)
(5, 300.00, 1, 'Alpha', NULL, 'S'), -- SaleItemID 7 (Sold 1)
(5, 300.00, 1, 'Alpha', NULL, 'M'), -- SaleItemID 8 (Sold 1)
(6, 220.00, 2, 'Alpha', NULL, 'L'), -- SaleItemID 9 (Sold 1)
(7, 200.00, 1, 'Alpha', NULL, 'M'), -- SaleItemID 10 (Sold 1)
(8, 250.00, 1, 'Alpha', NULL, 'S'), -- SaleItemID 11 (Unsold)
-- Dresses (ItemID 9, 10, 11, 12)
(9, 350.00, 1, 'Alpha', NULL, 'M'), -- SaleItemID 12 (Sold 1)
(10, 280.00, 1, 'Alpha', NULL, 'M'), -- SaleItemID 13 (Sold 1)
(11, 400.00, 2, 'Alpha', NULL, 'S'), -- SaleItemID 14 (Sold 1+1=2)
(12, 320.00, 1, 'Alpha', NULL, 'L'), -- SaleItemID 15 (Unsold)
-- Pants/Jeans (ItemID 13, 14, 15, 16, 17, 18)
(13, 200.00, 1, 'US', 30, NULL), -- SaleItemID 16 (Sold 1)
(14, 280.00, 2, 'Alpha', NULL, 'L'), -- SaleItemID 17 (Sold 1 + 1 = 2)
(15, 250.00, 1, 'Alpha', NULL, 'M'), -- SaleItemID 18 (Sold 1)
(16, 380.00, 1, 'US', 34, NULL), -- SaleItemID 19 (Sold 1)
(17, 350.00, 2, 'US', 31, NULL), -- SaleItemID 20 (Sold 1)
(18, 360.00, 1, 'US', 28, NULL), -- SaleItemID 21 (Unsold)
-- Shorts (ItemID 19, 20, 21)
(19, 180.00, 2, 'US', 30, NULL), -- SaleItemID 22 (Sold 1)
(20, 200.00, 1, 'US', 32, NULL), -- SaleItemID 23 (Sold 1)
(21, 150.00, 1, 'Alpha', NULL, 'M'), -- SaleItemID 24 (Unsold)
-- Skirts (ItemID 22, 23, 24)
(22, 250.00, 1, 'Alpha', NULL, 'S'), -- SaleItemID 25 (Sold 1)
(23, 280.00, 1, 'Alpha', NULL, 'M'), -- SaleItemID 26 (Sold 1)
(24, 260.00, 1, 'US', 28, NULL), -- SaleItemID 27 (Unsold)
-- Jackets (ItemID 25, 26, 27)
(25, 500.00, 1, 'Alpha', NULL, 'M'), -- SaleItemID 28 (Sold 1)
(26, 300.00, 1, 'Alpha', NULL, 'L'), -- SaleItemID 29 (Sold 1)
(27, 600.00, 1, 'Alpha', NULL, 'S'), -- SaleItemID 30 (Sold 1)
-- Hoodies/Sweaters (ItemID 28, 29, 30, 31)
(28, 350.00, 1, 'Alpha', NULL, 'M'), -- SaleItemID 31 (Sold 1)
(29, 300.00, 1, 'Alpha', NULL, 'L'), -- SaleItemID 32 (Sold 1)
(30, 400.00, 1, 'Alpha', NULL, 'M'), -- SaleItemID 33 (Sold 1)
(31, 320.00, 2, 'Alpha', NULL, 'S'), -- SaleItemID 34 (Sold 1)
-- Cardigans (ItemID 32, 33)
(32, 280.00, 1, 'Alpha', NULL, 'L'), -- SaleItemID 35 (Sold 1)
(33, 250.00, 1, 'Alpha', NULL, 'S'), -- SaleItemID 36 (Sold 1)
-- Polo Shirts (ItemID 34, 35)
(34, 150.00, 1, 'Alpha', NULL, 'M'), -- SaleItemID 37 (Sold 1)
(35, 120.00, 2, 'Alpha', NULL, 'L'), -- SaleItemID 38 (Sold 1)
-- Shoes (ItemID 36, 37)
(36, 450.00, 1, 'US', 8, NULL), -- SaleItemID 39 (Sold 1)
(37, 550.00, 2, 'EU', 39, NULL), -- SaleItemID 40 (Sold 1+1=2)
-- Bags (ItemID 38, 39)
(38, 200.00, 1, NULL, NULL, NULL), -- SaleItemID 41 (Sold 1)
(39, 350.00, 3, NULL, NULL, NULL), -- SaleItemID 42 (Sold 1+1+1=3)
-- Accessories (ItemID 40, 41)
(40, 80.00, 5, NULL, NULL, NULL), -- SaleItemID 43 (Sold 2+1+1+1=5)
(41, 120.00, 2, NULL, NULL, NULL), -- SaleItemID 44 (Sold 1+1=2)
-- Add a few more completely unsold items
(1, 155.00, 1, 'Alpha', NULL, 'S'), -- SaleItemID 45 (Unsold T-Shirt)
(13, 210.00, 1, 'US', 31, NULL), -- SaleItemID 46 (Unsold Chinos)
(25, 510.00, 1, 'Alpha', NULL, 'L'), -- SaleItemID 47 (Unsold Jacket)
(36, 460.00, 1, 'US', 9, NULL), -- SaleItemID 48 (Unsold Sneakers)
-- (38, 190.00, 1, NULL, NULL, NULL), -- SaleItemID 49 (Duplicate Tote Bag - REMOVED based on previous correction)
(7, 210.00, 1, 'Alpha', NULL, 'L'); -- SaleItemID 49 (New - Unsold Peasant Blouse Large)
GO
-- Note: SaleItemIDs are assumed sequential starting from 1. Adjust if needed based on actual execution.

-- =============================================
-- INSERT Data into Sales.SalesTransaction (Approx 45 Records for Multiple Transactions)
-- =============================================
PRINT 'Inserting Sales Transactions...';
-- Assuming CustomerIDs 1 to 35
-- Let's make Customers 1, 5, 10, 15, 20, 25, 30 have multiple transactions
INSERT INTO Sales.SalesTransaction (CustomerID, TransactionDate, PaymentType, EWalletType, BankName, MobileNumber, AccountNumber, AccountName, ReferenceNumber) VALUES
-- Customer 1 (Juan Dela Cruz) - 3 Transactions
(1, DATEADD(day, -20, GETDATE()), 'EWallet', 'GCash', NULL, '09171234501', NULL, 'Juan DC', 'REF2001'), -- TID 1
(1, DATEADD(day, -10, GETDATE()), 'Cash', NULL, NULL, NULL, NULL, NULL, NULL), -- TID 2
(1, DATEADD(day, -2, GETDATE()), 'Online Bank', NULL, 'BDO', NULL, '001234567890', 'Juan Dela Cruz', 'BANKREF001'), -- TID 3
-- Customer 2 (Maria Santos) - 1 Transaction
(2, DATEADD(day, -19, GETDATE()), 'Cash', NULL, NULL, NULL, NULL, NULL, NULL), -- TID 4
-- Customer 3 (Jose Garcia) - 1 Transaction
(3, DATEADD(day, -18, GETDATE()), 'EWallet', 'Maya', NULL, '09981112203', NULL, 'Jose G', 'REF2002'), -- TID 5
-- Customer 4 (Anna Reyes) - 1 Transaction
(4, DATEADD(day, -18, GETDATE()), 'Cash', NULL, NULL, NULL, NULL, NULL, NULL), -- TID 6
-- Customer 5 (Paolo Cruz) - 2 Transactions
(5, DATEADD(day, -17, GETDATE()), 'Online Bank', NULL, 'BPI', NULL, '9876543210', 'Paolo Cruz', 'BANKREF002'), -- TID 7
(5, DATEADD(day, -5, GETDATE()), 'EWallet', 'GCash', NULL, '09065551105', NULL, 'Paolo C', 'REF2003'), -- TID 8
-- Customer 6 (Sofia Ramos) - 1 Transaction
(6, DATEADD(day, -16, GETDATE()), 'Cash', NULL, NULL, NULL, NULL, NULL, NULL), -- TID 9
-- Customer 7 (Miguel Mendoza) - 1 Transaction
(7, DATEADD(day, -15, GETDATE()), 'EWallet', 'GCash', NULL, '09187778807', NULL, 'Miguel M', 'REF2004'), -- TID 10
-- Customer 8 (Isabella Gonzales) - 1 Transaction
(8, DATEADD(day, -15, GETDATE()), 'Cash', NULL, NULL, NULL, NULL, NULL, NULL), -- TID 11
-- Customer 9 (Carlos Flores) - 1 Transaction
(9, DATEADD(day, -14, GETDATE()), 'Online Bank', NULL, 'Metrobank', NULL, '1122334455', 'Carlos Flores', 'BANKREF003'), -- TID 12
-- Customer 10 (Bea Castillo) - 2 Transactions
(10, DATEADD(day, -14, GETDATE()), 'EWallet', 'Maya', NULL, '09178889910', NULL, 'Bea C', 'REF2005'), -- TID 13
(10, DATEADD(day, -3, GETDATE()), 'Cash', NULL, NULL, NULL, NULL, NULL, NULL), -- TID 14
-- Customer 11 (Rafael Bautista) - 1 Transaction
(11, DATEADD(day, -13, GETDATE()), 'Cash', NULL, NULL, NULL, NULL, NULL, NULL), -- TID 15
-- Customer 12 (Angela Villanueva) - 1 Transaction
(12, DATEADD(day, -12, GETDATE()), 'EWallet', 'GCash', NULL, '09952233412', NULL, 'Angela V', 'REF2006'), -- TID 16
-- Customer 13 (Luis Fernandez) - 1 Transaction
(13, DATEADD(day, -12, GETDATE()), 'Cash', NULL, NULL, NULL, NULL, NULL, NULL), -- TID 17
-- Customer 14 (Clara Lopez) - 1 Transaction
(14, DATEADD(day, -11, GETDATE()), 'Online Bank', NULL, 'Landbank', NULL, '5566778899', 'Clara Lopez', 'BANKREF004'), -- TID 18
-- Customer 15 (Antonio Martin) - 2 Transactions
(15, DATEADD(day, -10, GETDATE()), 'EWallet', 'Maya', NULL, '09255566715', NULL, 'Antonio M', 'REF2007'), -- TID 19
(15, DATEADD(day, -1, GETDATE()), 'EWallet', 'GCash', NULL, '09255566715', NULL, 'Antonio M', 'REF2008'), -- TID 20
-- Customer 16 (Gabriela De Leon) - 1 Transaction
(16, DATEADD(day, -9, GETDATE()), 'Cash', NULL, NULL, NULL, NULL, NULL, NULL), -- TID 21
-- Customer 17 (Andres Soriano) - 1 Transaction
(17, DATEADD(day, -9, GETDATE()), 'Online Bank', NULL, 'PNB', NULL, '2233445566', 'Andres Soriano', 'BANKREF005'), -- TID 22
-- Customer 18 (Teresa Salazar) - 1 Transaction
(18, DATEADD(day, -8, GETDATE()), 'EWallet', 'GCash', NULL, '09988899018', NULL, 'Teresa S', 'REF2009'), -- TID 23
-- Customer 19 (Ricardo Mercado) - 1 Transaction
(19, DATEADD(day, -8, GETDATE()), 'Cash', NULL, NULL, NULL, NULL, NULL, NULL), -- TID 24
-- Customer 20 (Pilar Aquino) - 2 Transactions
(20, DATEADD(day, -7, GETDATE()), 'EWallet', 'Maya', NULL, '09061122320', NULL, 'Pilar A', 'REF2010'), -- TID 25
(20, DATEADD(day, -2, GETDATE()), 'Online Bank', NULL, 'Security Bank', NULL, '6677889900', 'Pilar Aquino', 'BANKREF006'), -- TID 26
-- Customer 21 (Felipe Santiago) - 1 Transaction
(21, DATEADD(day, -6, GETDATE()), 'Cash', NULL, NULL, NULL, NULL, NULL, NULL), -- TID 27
-- Customer 22 (Elena Diaz) - 1 Transaction
(22, DATEADD(day, -6, GETDATE()), 'EWallet', 'GCash', NULL, '09183344522', NULL, 'Elena D', 'REF2011'), -- TID 28
-- Customer 23 (Ramon Navarro) - 1 Transaction
(23, DATEADD(day, -5, GETDATE()), 'Cash', NULL, NULL, NULL, NULL, NULL, NULL), -- TID 29
-- Customer 24 (Catalina Pascual) - 1 Transaction
(24, DATEADD(day, -5, GETDATE()), 'Online Bank', NULL, 'UnionBank', NULL, '1010101010', 'Catalina Pascual', 'BANKREF007'), -- TID 30
-- Customer 25 (Eduardo Jimenez) - 2 Transactions
(25, DATEADD(day, -4, GETDATE()), 'Cash', NULL, NULL, NULL, NULL, NULL, NULL), -- TID 31
(25, DATEADD(day, -1, GETDATE()), 'EWallet', 'Maya', NULL, '09176677825', NULL, 'Eduardo J', 'REF2012'), -- TID 32
-- Customer 26 (Victoria Torres) - 1 Transaction
(26, DATEADD(day, -4, GETDATE()), 'EWallet', 'GCash', NULL, '09207788926', NULL, 'Victoria T', 'REF2013'), -- TID 33
-- Customer 27 (Manuel Vargas) - 1 Transaction
(27, DATEADD(day, -3, GETDATE()), 'Cash', NULL, NULL, NULL, NULL, NULL, NULL), -- TID 34
-- Customer 28 (Lourdes Chavez) - 1 Transaction
(28, DATEADD(day, -3, GETDATE()), 'Online Bank', NULL, 'BDO', NULL, '1112223334', 'Lourdes Chavez', 'BANKREF008'), -- TID 35
-- Customer 29 (Benito Romero) - 1 Transaction
(29, DATEADD(day, -2, GETDATE()), 'EWallet', 'Maya', NULL, '09081122329', NULL, 'Benito R', 'REF2014'), -- TID 36
-- Customer 30 (Anita Gutierrez) - 2 Transactions
(30, DATEADD(day, -2, GETDATE()), 'Cash', NULL, NULL, NULL, NULL, NULL, NULL), -- TID 37
(30, GETDATE(), 'EWallet', 'GCash', NULL, '09252233430', NULL, 'Anita G', 'REF2015'), -- TID 38
-- Customer 31 (Gregorio Francisco) - 1 Transaction
(31, DATEADD(day, -1, GETDATE()), 'Cash', NULL, NULL, NULL, NULL, NULL, NULL), -- TID 39
-- Customer 32 (Consuelo Ignacio) - 1 Transaction
(32, DATEADD(day, -1, GETDATE()), 'Online Bank', NULL, 'BPI', NULL, '1212121212', 'Consuelo Ignacio', 'BANKREF009'), -- TID 40
-- Customer 33 (Mateo Ocampo) - 1 Transaction
(33, GETDATE(), 'EWallet', 'Maya', NULL, '09985566733', NULL, 'Mateo O', 'REF2016'), -- TID 41
-- Customer 34 (Patricia Abad) - 1 Transaction
(34, GETDATE(), 'Cash', NULL, NULL, NULL, NULL, NULL, NULL), -- TID 42
-- Customer 35 (Julio Del Rosario) - 1 Transaction
(35, GETDATE(), 'EWallet', 'GCash', NULL, '09067788935', NULL, 'Julio DR', 'REF2017'); -- TID 43
GO
-- Note: TransactionIDs (TID) are assumed sequential starting from 1.

-- =============================================
-- INSERT Data into Sales.TransactionItem (Approx 70-80 Records)
-- Linking Transactions (1-43) to SaleItems (1-49), with varying quantities and item reuse
-- =============================================
PRINT 'Inserting Transaction Items...';
INSERT INTO Sales.TransactionItem (TransactionID, SaleItemID, Quantity) VALUES
-- TID 1 (Juan)
(1, 7, 1), (1, 22, 1),
-- TID 2 (Juan)
(2, 1, 1), (2, 43, 2), -- Buys 2 belts
-- TID 3 (Juan)
(3, 19, 1),
-- TID 4 (Maria)
(4, 3, 2), -- Buys 2 T-shirts (L)
-- TID 5 (Jose)
(5, 13, 1), (5, 30, 1),
-- TID 6 (Anna)
(6, 9, 1),
-- TID 7 (Paolo)
(7, 14, 1), -- Buys LBD (Qty 1/2 used)
-- TID 8 (Paolo)
(8, 37, 1), (8, 44, 1), -- Buys Polo, Scarf (Qty 1/2 used)
-- TID 9 (Sofia)
(9, 17, 1), -- Buys Wide Leg Trousers (Qty 1/2 used)
-- TID 10 (Miguel)
(10, 31, 1),
-- TID 11 (Isabella)
(11, 5, 1), -- Buys Graphic Tee (Qty 1/3 used)
-- TID 12 (Carlos)
(12, 28, 1),
-- TID 13 (Bea)
(13, 40, 1), -- Buys Boots (Qty 1/2 used)
-- TID 14 (Bea)
(14, 26, 1), (14, 42, 1), -- Buys Skirt, Bag (Qty 1/3 used)
-- TID 15 (Rafael)
(15, 16, 1),
-- TID 16 (Angela)
(16, 35, 1),
-- TID 17 (Luis)
(17, 3, 1), -- Buys T-Shirt (L) (Qty 3/5 used)
-- TID 18 (Clara)
(18, 14, 1), -- Buys LBD (Qty 2/2 used)
-- TID 19 (Antonio)
(19, 20, 1), (19, 34, 1),
-- TID 20 (Antonio)
(20, 8, 1), (20, 43, 1), -- Buys Blouse, Belt (Qty 3/5 used)
-- TID 21 (Gabriela)
(21, 12, 1),
-- TID 22 (Andres)
(22, 29, 1),
-- TID 23 (Teresa)
(23, 5, 1), -- Buys Graphic Tee (Qty 2/3 used)
-- TID 24 (Ricardo)
(24, 33, 1),
-- TID 25 (Pilar)
(25, 41, 1),
-- TID 26 (Pilar)
(26, 10, 1), -- Buys Peasant Blouse (Sold 1/1)
-- TID 27 (Felipe)
(27, 38, 1),
-- TID 28 (Elena)
(28, 40, 1), -- Buys Boots (Qty 2/2 used)
-- TID 29 (Ramon)
(29, 3, 1), -- Buys T-Shirt (L) (Qty 4/5 used)
-- TID 30 (Catalina)
(30, 25, 1),
-- TID 31 (Eduardo)
(31, 17, 1), -- Buys Wide Leg Trousers (Qty 2/2 used)
-- TID 32 (Eduardo)
(32, 42, 1), -- Buys Bag (Qty 2/3 used)
-- TID 33 (Victoria)
(33, 5, 1), -- Buys Graphic Tee (Qty 3/3 used)
-- TID 34 (Manuel)
(34, 32, 1),
-- TID 35 (Lourdes)
(35, 44, 1), -- Buys Scarf (Qty 2/2 used)
-- TID 36 (Benito)
(36, 23, 1),
-- TID 37 (Anita)
(37, 43, 1), -- Buys Belt (Qty 4/5 used)
-- TID 38 (Anita)
(38, 3, 1), -- Buys T-Shirt (L) (Qty 5/5 used) -> Now Sold Out
-- TID 39 (Gregorio)
(39, 39, 1),
-- TID 40 (Consuelo)
(40, 18, 1),
-- TID 41 (Mateo)
(41, 42, 1), -- Buys Bag (Qty 3/3 used) -> Now Sold Out
-- TID 42 (Patricia)
(42, 43, 1), -- Buys Belt (Qty 5/5 used) -> Now Sold Out
-- TID 43 (Julio)
(43, 36, 1);
GO
-- Note: Check SaleItem quantities vs total sold quantity for plausibility.

-- =============================================
-- INSERT Data into Sales.Delivery (Approx 45 Records)
-- Linked to SalesTransaction (1-43). Address copied from Customer.
-- UPDATED: Includes 'Cancelled' as a possible status.
-- =============================================
PRINT 'Inserting Deliveries...';
-- Generate delivery records, copying address from customer, setting status
-- For simplicity, creating a delivery record for most transactions. Status varies.
INSERT INTO Sales.Delivery (TransactionID, DeliveryAddress, DeliveryDate, DeliveryStatus, PaidAmount)
SELECT
    st.TransactionID,
    CONCAT(c.Street, ', ', c.Barangay, ', ', c.City, ', ', c.Province),
    DATEADD(day, (RAND(CHECKSUM(NEWID())) * 3) + 1, st.TransactionDate), -- Delivery 1-4 days after transaction
    CASE ABS(CHECKSUM(NEWID())) % 6 -- Random status (0-5 for 6 statuses) -- MODIFIED from % 5
        WHEN 0 THEN 'Delivered'
        WHEN 1 THEN 'Shipped'
        WHEN 2 THEN 'Out for Delivery'
        WHEN 3 THEN 'Pending'
        WHEN 4 THEN 'Failed'
        ELSE 'Cancelled' -- Added Cancelled status
    END,
    CASE -- Add paid amount roughly for 'Cash' transactions simulating COD
        -- Assume 'Cancelled' or 'Failed' deliveries wouldn't have PaidAmount recorded yet
        WHEN st.PaymentType = 'Cash' AND (ABS(CHECKSUM(NEWID())) % 6) NOT IN (4, 5) THEN
            (SELECT SUM(si.ItemPrice * ti.Quantity) FROM Sales.TransactionItem ti JOIN Sales.SaleItem si ON ti.SaleItemID = si.SaleItemID WHERE ti.TransactionID = st.TransactionID)
        ELSE NULL
    END
FROM Sales.SalesTransaction st
JOIN Customer.Customer c ON st.CustomerID = c.CustomerID
WHERE st.TransactionID <= 43; -- Ensure we only create deliveries for inserted transactions
GO

PRINT 'Finished inserting data.';