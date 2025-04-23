package app.db.dao.production;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.db.Connect;
import app.db.pojo.production.SupplyOrder;


public class SupplyOrderDao {
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
}
