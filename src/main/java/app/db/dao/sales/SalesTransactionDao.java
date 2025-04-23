package app.db.dao.sales;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.db.Connect;
import app.db.pojo.sales.SalesTransaction;

public class SalesTransactionDao {
    public static List<SalesTransaction> getSalesTransactionsByCustomerId(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        List<SalesTransaction> sales = new ArrayList<>();

        String sql = "SELECT * FROM Sales.SalesTransaction WHERE CustomerID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            sales.add(new SalesTransaction(
                res.getInt("TransactionID"),
                res.getInt("CustomerID"),
                res.getTimestamp("TransactionDate"),
                res.getString("PaymentType"),
                res.getString("EWalletType"),
                res.getString("BankName"),
                res.getString("MobileNumber"),
                res.getString("AccountNumber"),
                res.getString("AccountName"),
                res.getString("ReferenceNumber")
            ));
        }

        return sales;
    }

    public static List<SalesTransaction> getSalesTransactionsByCustomerIdOrderBy(int id, String orderBy) throws SQLException {
        Connection conn = Connect.openConnection();


        List<SalesTransaction> sales = new ArrayList<>();

        String sql = "SELECT * FROM Sales.SalesTransaction WHERE CustomerID = ? ORDER BY " + orderBy;
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            sales.add(new SalesTransaction(
                res.getInt("TransactionID"),
                res.getInt("CustomerID"),
                res.getTimestamp("TransactionDate"),
                res.getString("PaymentType"),
                res.getString("EWalletType"),
                res.getString("BankName"),
                res.getString("MobileNumber"),
                res.getString("AccountNumber"),
                res.getString("AccountName"),
                res.getString("ReferenceNumber")
            ));
        }

        return sales;
    }
    public static double getTransactionTotal(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT TotalAmount FROM VW_SalesTransactionTotal WHERE TransactionID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        if (res.next()) {
            return res.getDouble("TotalAmount");
        }
        return 0;
    }
}
