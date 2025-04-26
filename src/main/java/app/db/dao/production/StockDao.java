package app.db.dao.production;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import app.db.Connect;

public class StockDao {
    public static void updateStock(int id, int quantity) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "UPDATE Production.Stock SET TotalStock = ? WHERE StockID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, quantity);
        stmt.setInt(2, id);
        stmt.executeUpdate();
    }

    public static int getItemTotalStock(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT TotalStock FROM Production.Stock WHERE ItemID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            return res.getInt(1);
        }
        return 0;
    }

    public static int getItemAvailableStock(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT AvailableStock FROM VW_ItemAvailableStock WHERE ItemID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            return res.getInt(1);
        }
        return 0;
    }
}
