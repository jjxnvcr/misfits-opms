package app.db.dao.production;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.db.Connect;
import app.db.pojo.production.SupplyOrder;
import app.db.pojo.production.SupplyOrderItem;

public class SupplyOrderDao {
    public static void addSupplyOrder(SupplyOrder supplyOrder) throws SQLException {
        Connection conn = Connect.openConnection();

        if (supplyOrder.getOrderDate() != null) {
            String sql = "INSERT INTO Production.SupplyOrder (SupplierID, OrderDate, OrderStatus) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, supplyOrder.getSupplierId());
            stmt.setTimestamp(2, supplyOrder.getOrderDate());
            stmt.setString(3, supplyOrder.getOrderStatus());
            stmt.executeUpdate();
        } else {
            String sql = "INSERT INTO Production.SupplyOrder (SupplierID, OrderStatus) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, supplyOrder.getSupplierId());
            stmt.setString(2, supplyOrder.getOrderStatus());
            stmt.executeUpdate();
        }
    }

    public static void updateSupplyOrder(SupplyOrder supplyOrder) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "UPDATE Production.SupplyOrder SET SupplierID = ?, OrderDate = ?, OrderStatus = ? WHERE OrderID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, supplyOrder.getSupplierId());
        stmt.setTimestamp(2, supplyOrder.getOrderDate());
        stmt.setString(3, supplyOrder.getOrderStatus());
        stmt.setInt(4, supplyOrder.getOrderId());
        stmt.executeUpdate();
    }

    public static void deleteSupplyOrder(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "DELETE FROM Production.SupplyOrder WHERE OrderID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    public static SupplyOrder getLatestSupplyOrder() throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT TOP 1 * FROM Production.SupplyOrder ORDER BY OrderID DESC";

        ResultSet res = conn.createStatement().executeQuery(sql);
        while (res.next()) {
            return new SupplyOrder(
                res.getInt("OrderID"),
                res.getInt("SupplierID"),
                res.getTimestamp("OrderDate"),
                res.getString("OrderStatus")
            );
        }

        return null;
    }

    public static List<SupplyOrder> getAllSupplyOrders() throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Production.SupplyOrder";
        List<SupplyOrder> orders = new ArrayList<>();

        ResultSet res = conn.createStatement().executeQuery(sql);
        while (res.next()) {
            orders.add(new SupplyOrder(
                res.getInt("OrderID"),
                res.getInt("SupplierID"),
                res.getTimestamp("OrderDate"),
                res.getString("OrderStatus")
            ));
        }

        return orders;
    }

    public static List<SupplyOrder> getAllSupplyOrdersOrderBy(String orderBy) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Production.SupplyOrder ORDER BY " + orderBy;
        List<SupplyOrder> orders = new ArrayList<>();

        ResultSet res = conn.createStatement().executeQuery(sql);
        while (res.next()) {
            orders.add(new SupplyOrder(
                res.getInt("OrderID"),
                res.getInt("SupplierID"),    
                res.getTimestamp("OrderDate"),
                res.getString("OrderStatus")
            ));
        }

        return orders;
    }
    
    public static List<SupplyOrder> getSupplyOrdersBySupplierId(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        List<SupplyOrder> orders = new ArrayList<>();

        String sql = "SELECT * FROM Production.SupplyOrder WHERE SupplierID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            orders.add(new SupplyOrder(
                res.getInt("OrderID"),
                res.getInt("SupplierID"),
                res.getTimestamp("OrderDate"),
                res.getString("OrderStatus")
            ));
        }

        return orders;
    }

    public static List<SupplyOrder> getSupplyOrdersBySupplierIdOrderBy(int id, String orderBy) throws SQLException {
        Connection conn = Connect.openConnection();

        List<SupplyOrder> orders = new ArrayList<>();

        String sql = "SELECT * FROM Production.SupplyOrder WHERE SupplierID = ? ORDER BY " + orderBy;
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            orders.add(new SupplyOrder(
                res.getInt("OrderID"),
                res.getInt("SupplierID"),
                res.getTimestamp("OrderDate"),
                res.getString("OrderStatus")
            ));
        }

        return orders;
    }

    public static List<SupplyOrder> getSupplyOrdersByOrderStatus(String status) throws SQLException {
        Connection conn = Connect.openConnection();

        List<SupplyOrder> orders = new ArrayList<>();

        String sql = "SELECT * FROM Production.SupplyOrder WHERE OrderStatus = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, status);

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            orders.add(new SupplyOrder(
                res.getInt("OrderID"),
                res.getInt("SupplierID"),
                res.getTimestamp("OrderDate"),
                res.getString("OrderStatus")
            ));
        }

        return orders;
    }

    public static List<SupplyOrder> getSupplyOrdersBySupplierIdByOrderStatus(int id, String status) throws SQLException {
        Connection conn = Connect.openConnection();

        List<SupplyOrder> orders = new ArrayList<>();

        String sql = "SELECT * FROM Production.SupplyOrder WHERE SupplierID = ? AND OrderStatus = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.setString(2, status);

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            orders.add(new SupplyOrder(
                res.getInt("OrderID"),
                res.getInt("SupplierID"),
                res.getTimestamp("OrderDate"),
                res.getString("OrderStatus")
            ));
        }

        return orders;
    }
    
    public static List<SupplyOrder> getSupplyOrdersByOrderStatusOrderBy(String status, String orderBy) throws SQLException {
        Connection conn = Connect.openConnection();

        List<SupplyOrder> orders = new ArrayList<>();

        String sql = "SELECT * FROM Production.SupplyOrder WHERE OrderStatus = ? ORDER BY " + orderBy;
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, status);

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            orders.add(new SupplyOrder(
                res.getInt("OrderID"),
                res.getInt("SupplierID"),
                res.getTimestamp("OrderDate"),
                res.getString("OrderStatus")
            ));
        }

        return orders;
    }

    public static List<SupplyOrder> getSupplyOrdersBySupplierIdByOrderStatusOrderBy(int id, String status, String orderBy) throws SQLException {
        Connection conn = Connect.openConnection();

        List<SupplyOrder> orders = new ArrayList<>();

        String sql = "SELECT * FROM Production.SupplyOrder WHERE SupplierID = ? AND OrderStatus = ? ORDER BY " + orderBy;
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.setString(2, status);

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            orders.add(new SupplyOrder(
                res.getInt("OrderID"),
                res.getInt("SupplierID"),
                res.getTimestamp("OrderDate"),
                res.getString("OrderStatus")
            ));
        }

        return orders;
    }
    
    public static double getOrderTotal(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT TotalAmount FROM VW_SupplyOrderTotal WHERE OrderID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        if (res.next()) {
            return res.getDouble(1);
        }
        return 0;
    }

    public static int getOrderTotalItems(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT SUM(Quantity) FROM Production.SupplyOrderItem WHERE OrderID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        if (res.next()) {
            return res.getInt(1);
        }
        return 0;
    }

    public static List<SupplyOrderItem> getSupplyOrderItemsById(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        List<SupplyOrderItem> items = new ArrayList<>();

        String sql = "SELECT * FROM Production.SupplyOrderItem WHERE OrderID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            items.add(new SupplyOrderItem(
                res.getInt("OrderID"),
                res.getInt("ItemID"),
                res.getDouble("OrderItemCost"),
                res.getInt("Quantity")
            ));
        }

        return items;
    }
}
