package app.db.dao.sales;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.db.Connect;
import app.db.pojo.sales.Delivery;

public class DeliveryDao {
    public static void addDelivery(Delivery delivery) throws SQLException {
        Connection conn = Connect.openConnection();

        if (delivery.getPaidAmount() != null) {
            String sql = "INSERT INTO Sales.Delivery (TransactionID, DeliveryAddress, DeliveryDate, DeliveryStatus, PaidAmount) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, delivery.getTransactionId());
            stmt.setString(2, delivery.getDeliveryAddress());
            stmt.setTimestamp(3, delivery.getDeliveryDate());
            stmt.setString(4, delivery.getDeliveryStatus());
            stmt.setDouble(5, delivery.getPaidAmount());
            stmt.executeUpdate();
        } else {
            String sql = "INSERT INTO Sales.Delivery (TransactionID, DeliveryAddress, DeliveryDate, DeliveryStatus) VALUES (?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, delivery.getTransactionId());
            stmt.setString(2, delivery.getDeliveryAddress());
            stmt.setTimestamp(3, delivery.getDeliveryDate());
            stmt.setString(4, delivery.getDeliveryStatus());
            stmt.executeUpdate();
        }
    }

    public static void updateDelivery(Delivery delivery) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "UPDATE Sales.Delivery SET TransactionID = ?, DeliveryAddress = ?, DeliveryDate = ?, DeliveryStatus = ?, PaidAmount = ? WHERE DeliveryID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, delivery.getTransactionId());
        stmt.setString(2, delivery.getDeliveryAddress());
        stmt.setTimestamp(3, delivery.getDeliveryDate());
        stmt.setString(4, delivery.getDeliveryStatus());
        stmt.setDouble(5, delivery.getPaidAmount());
        stmt.setInt(6, delivery.getDeliveryId());
        stmt.executeUpdate();
    }

    public static void deleteDelivery(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "DELETE FROM Sales.Delivery WHERE DeliveryID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    public static Delivery getDeliveryByTransactionId(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Sales.Delivery WHERE TransactionID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            return new Delivery(
                res.getInt("DeliveryID"), 
                res.getInt("TransactionID"), 
                res.getString("DeliveryAddress"), 
                res.getTimestamp("DeliveryDate"), 
                res.getString("DeliveryStatus"), 
                res.getDouble("PaidAmount"));        
            }

        return null;
    }

    public static List<Delivery> getAllDeliveries() throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Sales.Delivery";
        PreparedStatement stmt = conn.prepareStatement(sql);

        ResultSet res = stmt.executeQuery();
        List<Delivery> deliveries = new ArrayList<>();

        while (res.next()) {
            deliveries.add(new Delivery(
                res.getInt("DeliveryID"), 
                res.getInt("TransactionID"), 
                res.getString("DeliveryAddress"), 
                res.getTimestamp("DeliveryDate"), 
                res.getString("DeliveryStatus"), 
                res.getDouble("PaidAmount")));        
            }

        return deliveries;
    }

    public static List<Delivery> getAllDeliveriesOrderBy(String orderBy) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Sales.Delivery ORDER BY " + orderBy;
        List<Delivery> deliveries = new ArrayList<>();

        ResultSet res = conn.createStatement().executeQuery(sql);
        while (res.next()) {
            deliveries.add(new Delivery(
                res.getInt("DeliveryID"),
                res.getInt("TransactionID"), 
                res.getString("DeliveryAddress"), 
                res.getTimestamp("DeliveryDate"), 
                res.getString("DeliveryStatus"), 
                res.getDouble("PaidAmount")));        
            }

        return deliveries;
    }

    public static List<Delivery> getDeliveriesByCustomerId(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Sales.Delivery WHERE TransactionID IN (SELECT TransactionID FROM Sales.SalesTransaction WHERE CustomerID = ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();
        List<Delivery> deliveries = new ArrayList<>();

        while (res.next()) {
            deliveries.add(new Delivery(
                res.getInt("DeliveryID"), 
                res.getInt("TransactionID"),
                res.getString("DeliveryAddress"), 
                res.getTimestamp("DeliveryDate"), 
                res.getString("DeliveryStatus"), 
                res.getDouble("PaidAmount")));        
            }

        return deliveries;
    }

    public static List<Delivery> getDeliveriesByCustomerIdOrderBy(int id, String orderBy) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Sales.Delivery WHERE TransactionID IN (SELECT TransactionID FROM Sales.SalesTransaction WHERE CustomerID = ?) ORDER BY " + orderBy;
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();
        List<Delivery> deliveries = new ArrayList<>();

        while (res.next()) {
            deliveries.add(new Delivery(
                res.getInt("DeliveryID"), 
                res.getInt("TransactionID"),
                res.getString("DeliveryAddress"), 
                res.getTimestamp("DeliveryDate"), 
                res.getString("DeliveryStatus"), 
                res.getDouble("PaidAmount")));        
            }

        return deliveries;
    }

    public static List<Delivery> getDeliveriesByDeliveryStatus(String status) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Sales.Delivery WHERE DeliveryStatus = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, status);

        ResultSet res = stmt.executeQuery();
        List<Delivery> deliveries = new ArrayList<>();

        while (res.next()) {
            deliveries.add(new Delivery(
                res.getInt("DeliveryID"), 
                res.getInt("TransactionID"),
                res.getString("DeliveryAddress"), 
                res.getTimestamp("DeliveryDate"), 
                res.getString("DeliveryStatus"), 
                res.getDouble("PaidAmount")));        
            }

        return deliveries;
    }

    public static List<Delivery> getDeliveriesByDeliveryStatusOrderBy(String status, String orderBy) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Sales.Delivery WHERE DeliveryStatus = ? ORDER BY " + orderBy;
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, status);

        ResultSet res = stmt.executeQuery();
        List<Delivery> deliveries = new ArrayList<>();

        while (res.next()) {
            deliveries.add(new Delivery(
                res.getInt("DeliveryID"), 
                res.getInt("TransactionID"),
                res.getString("DeliveryAddress"), 
                res.getTimestamp("DeliveryDate"), 
                res.getString("DeliveryStatus"), 
                res.getDouble("PaidAmount")));        
            }

        return deliveries;
    }

    public static List<Delivery> getDeliveriesByCustomerIdByDeliveryStatus(int id, String status) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Sales.Delivery WHERE TransactionID IN (SELECT TransactionID FROM Sales.SalesTransaction WHERE CustomerID = ?) AND DeliveryStatus = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.setString(2, status);

        ResultSet res = stmt.executeQuery();
        List<Delivery> deliveries = new ArrayList<>();

        while (res.next()) {
            deliveries.add(new Delivery(
                res.getInt("DeliveryID"), 
                res.getInt("TransactionID"),
                res.getString("DeliveryAddress"), 
                res.getTimestamp("DeliveryDate"), 
                res.getString("DeliveryStatus"), 
                res.getDouble("PaidAmount")));        
            }

        return deliveries;
    }

    public static List<Delivery> getDeliveriesByCustomerIdByDeliveryStatusOrderBy(int id, String status, String orderBy) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Sales.Delivery WHERE TransactionID IN (SELECT TransactionID FROM Sales.SalesTransaction WHERE CustomerID = ?) AND DeliveryStatus = ? ORDER BY " + orderBy;
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.setString(2, status);

        ResultSet res = stmt.executeQuery();
        List<Delivery> deliveries = new ArrayList<>();

        while (res.next()) {
            deliveries.add(new Delivery(
                res.getInt("DeliveryID"), 
                res.getInt("TransactionID"),
                res.getString("DeliveryAddress"), 
                res.getTimestamp("DeliveryDate"), 
                res.getString("DeliveryStatus"), 
                res.getDouble("PaidAmount")));        
            }

        return deliveries;
    }
}
