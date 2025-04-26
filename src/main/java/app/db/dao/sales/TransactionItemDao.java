package app.db.dao.sales;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import app.db.Connect;
import app.db.pojo.sales.TransactionItem;

public class TransactionItemDao {
    public static void addTransactionItem(TransactionItem transactionItem) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "INSERT INTO Sales.TransactionItem (TransactionID, SaleItemID, Quantity) VALUES (?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, transactionItem.getTransactionId());
        stmt.setInt(2, transactionItem.getSaleItemId());
        stmt.setInt(3, transactionItem.getQuantity());

        stmt.executeUpdate();
    }
}
