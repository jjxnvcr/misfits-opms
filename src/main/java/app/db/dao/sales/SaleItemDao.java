package app.db.dao.sales;

import java.util.ArrayList;
import java.util.List;

import app.db.Connect;
import app.db.pojo.sales.SaleItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SaleItemDao {
    public static void addSaleItem(SaleItem saleItem) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "INSERT INTO Sales.SaleItem (ItemID, ItemPrice, ItemQuantity, MeasurementSystem, NumericSize, AlphaSize) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, saleItem.getItemId());
        stmt.setDouble(2, saleItem.getItemPrice());
        stmt.setInt(3, saleItem.getItemQuantity());
        stmt.setString(4, saleItem.getMeasurementSystem());
        stmt.setDouble(5, saleItem.getNumericSize());
        stmt.setString(6, saleItem.getAlphaSize());
        stmt.executeUpdate();
    }

    public static void updateSaleItem(SaleItem saleItem) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "UPDATE Sales.SaleItem SET ItemPrice = ?, ItemQuantity = ?, MeasurementSystem = ?, NumericSize = ?, AlphaSize = ? WHERE SaleItemID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setDouble(1, saleItem.getItemPrice());
        stmt.setInt(2, saleItem.getItemQuantity());
        stmt.setString(3, saleItem.getMeasurementSystem());
        stmt.setDouble(4, saleItem.getNumericSize());
        stmt.setString(5, saleItem.getAlphaSize());
        stmt.setInt(6, saleItem.getSaleItemId());
        stmt.executeUpdate();
    }

    public static void deleteSaleItem(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "DELETE FROM Sales.SaleItem WHERE SaleItemID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    public static SaleItem getSaleItem(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Sales.SaleItem WHERE SaleItemID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            return new SaleItem(
                res.getInt("SaleItemID"),
                res.getInt("ItemID"),
                res.getDouble("ItemPrice"),
                res.getInt("ItemQuantity"),
                res.getString("MeasurementSystem"),
                res.getDouble("NumericSize"),
                res.getString("AlphaSize")
            );
        }
        return null;
    }

    public static List<SaleItem> getAllSaleItemsByItemId(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM VW_SaleItemInfo WHERE ItemID = ?";
        List<SaleItem> saleItems = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            saleItems.add(new SaleItem(
                res.getInt("SaleItemID"),
                res.getInt("ItemID"),
                res.getString("CategoryName"),
                res.getString("ItemName"),
                res.getString("ItemDescription"),
                res.getString("ItemColor"),
                res.getDouble("ItemPrice"),
                res.getInt("ItemQuantity"),
                res.getString("MeasurementSystem"),
                res.getDouble("NumericSize"),
                res.getString("AlphaSize")
            ));
        }

        return saleItems;
    }

    public static List<SaleItem> getAllSaleItems() throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM VW_SaleItemInfo";
        List<SaleItem> saleItems = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            saleItems.add(new SaleItem(
                res.getInt("SaleItemID"),
                res.getInt("ItemID"),
                res.getString("CategoryName"),
                res.getString("ItemName"),
                res.getString("ItemDescription"),
                res.getString("ItemColor"),
                res.getDouble("ItemPrice"),
                res.getInt("ItemQuantity"),
                res.getString("MeasurementSystem"),
                res.getDouble("NumericSize"),
                res.getString("AlphaSize")
            ));
        }
        return saleItems;
    }

    public static List<SaleItem> getAllSaleItemsOrderBy(String orderBy) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM VW_SaleItemInfo ORDER BY " + orderBy;
        List<SaleItem> saleItems = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            saleItems.add(new SaleItem(
                res.getInt("SaleItemID"),
                res.getInt("ItemID"),
                res.getString("CategoryName"),
                res.getString("ItemName"),
                res.getString("ItemDescription"),
                res.getString("ItemColor"),
                res.getDouble("ItemPrice"),
                res.getInt("ItemQuantity"),
                res.getString("MeasurementSystem"),
                res.getDouble("NumericSize"),
                res.getString("AlphaSize")
            ));
        }
        return saleItems;
    }
    
    public static List<SaleItem> getSaleItemsByCategory(String category) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM VW_SaleItemInfo WHERE CategoryName = ?";
        List<SaleItem> saleItems = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, category);

        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            saleItems.add(new SaleItem(
                res.getInt("SaleItemID"),
                res.getInt("ItemID"),
                res.getString("CategoryName"),
                res.getString("ItemName"),
                res.getString("ItemDescription"),
                res.getString("ItemColor"),
                res.getDouble("ItemPrice"),
                res.getInt("ItemQuantity"),
                res.getString("MeasurementSystem"),
                res.getDouble("NumericSize"),
                res.getString("AlphaSize")
            ));
        }
        return saleItems;
    }
    
    public static List<SaleItem> getSaleItemsByCategoryOrderBy(String category, String orderBy) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM VW_SaleItemInfo WHERE CategoryName = ? ORDER BY " + orderBy;
        List<SaleItem> saleItems = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, category);

        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            saleItems.add(new SaleItem(
                res.getInt("SaleItemID"),
                res.getInt("ItemID"),
                res.getString("CategoryName"),
                res.getString("ItemName"),
                res.getString("ItemDescription"),
                res.getString("ItemColor"),
                res.getDouble("ItemPrice"),
                res.getInt("ItemQuantity"),
                res.getString("MeasurementSystem"),
                res.getDouble("NumericSize"),
                res.getString("AlphaSize")
            ));
        }
        return saleItems;
    }
    
    public static int getTotalTransactions(int id) throws SQLException {
        Connection conn = Connect.openConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT TotalTransactions FROM VW_SaleItemPurchased WHERE SaleItemID = ?");

        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            return res.getInt("TotalTransactions");
        }
        return 0;
    }

    public static int getTotalPurchases(int id) throws SQLException {
        Connection conn = Connect.openConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT TotalPurchases FROM VW_SaleItemPurchased WHERE SaleItemID = ?");

        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            return res.getInt("TotalPurchases");
        }
        return 0;
    }
}
 