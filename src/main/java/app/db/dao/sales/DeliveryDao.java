package app.db.dao.sales;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import app.db.Connect;
import app.db.pojo.sales.Delivery;

public class DeliveryDao {
    public static void addDelivery(Delivery delivery) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "INSERT INTO Sales.Delivery (TransactionID, DeliveryAddress, DeliveryDate, DeliveryStatus, PaidAmount) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, delivery.getTransactionId());
        stmt.setString(2, delivery.getDeliveryAddress());
        stmt.setTimestamp(3, delivery.getDeliveryDate());
        stmt.setString(4, delivery.getDeliveryStatus());
        stmt.setDouble(5, delivery.getPaidAmount());
        stmt.executeUpdate();
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
}
