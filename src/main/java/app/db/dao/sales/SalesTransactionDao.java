package app.db.dao.sales;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.db.Connect;
import app.db.pojo.sales.SalesTransaction;
import app.db.pojo.sales.TransactionItem;

public class SalesTransactionDao {
    public static void addSalesTransaction(SalesTransaction salesTransaction) throws SQLException {
        Connection conn = Connect.openConnection();

        if (salesTransaction.getTransactionDate() != null) {
            String sql = "INSERT INTO Sales.SalesTransaction (CustomerID, TransactionDate, PaymentType, EWalletType, BankName, MobileNumber, AccountNumber, AccountName, ReferenceNumber) VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, salesTransaction.getCustomerId());
            stmt.setTimestamp(2, salesTransaction.getTransactionDate());
            stmt.setString(3, salesTransaction.getPaymentType());
            stmt.setString(4, salesTransaction.geteWalletType());
            stmt.setString(5, salesTransaction.getBankName());
            stmt.setString(6, salesTransaction.getMobileNumber());
            stmt.setString(7, salesTransaction.getAccountNumber());
            stmt.setString(8, salesTransaction.getAccountName());
            stmt.setString(9, salesTransaction.getReferenceNumber());
            stmt.executeUpdate();
        } else {
            String sql = "INSERT INTO Sales.SalesTransaction (CustomerID, PaymentType, EWalletType, BankName, MobileNumber, AccountNumber, AccountName, ReferenceNumber) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, salesTransaction.getCustomerId());
            stmt.setString(2, salesTransaction.getPaymentType());
            stmt.setString(3, salesTransaction.geteWalletType());
            stmt.setString(4, salesTransaction.getBankName());
            stmt.setString(5, salesTransaction.getMobileNumber());
            stmt.setString(6, salesTransaction.getAccountNumber());
            stmt.setString(7, salesTransaction.getAccountName());
            stmt.setString(8, salesTransaction.getReferenceNumber());
            stmt.executeUpdate();
        }
    }

    public static void updateSalesTransaction(SalesTransaction salesTransaction) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "UPDATE Sales.SalesTransaction SET CustomerID = ?, TransactionDate = ?, PaymentType = ?, EWalletType = ?, BankName = ?, MobileNumber = ?, AccountNumber = ?, AccountName = ?, ReferenceNumber = ? WHERE TransactionID = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, salesTransaction.getCustomerId());
        stmt.setTimestamp(2, salesTransaction.getTransactionDate());
        stmt.setString(3, salesTransaction.getPaymentType());
        stmt.setString(4, salesTransaction.geteWalletType());
        stmt.setString(5, salesTransaction.getBankName());
        stmt.setString(6, salesTransaction.getMobileNumber());
        stmt.setString(7, salesTransaction.getAccountNumber());
        stmt.setString(8, salesTransaction.getAccountName());
        stmt.setString(9, salesTransaction.getReferenceNumber());
        stmt.setInt(10, salesTransaction.getTransactionId());
        stmt.executeUpdate();
    }

    public static void deleteSalesTransaction(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "DELETE FROM Sales.SalesTransaction WHERE TransactionID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    public static SalesTransaction getSalesTransaction(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Sales.SalesTransaction WHERE TransactionID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            return new SalesTransaction(
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
            );
        }

        return null;
    }

    public static SalesTransaction getLatestSalesTransaction() throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT TOP 1 * FROM Sales.SalesTransaction ORDER BY TransactionID DESC";
        ResultSet res = conn.createStatement().executeQuery(sql);
        while (res.next()) {
            return new SalesTransaction(
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
            );
        }
        return null;
    }

    public static List<SalesTransaction> getAllSalesTransactions() throws SQLException {
        Connection conn = Connect.openConnection();

        List<SalesTransaction> sales = new ArrayList<>();

        String sql = "SELECT * FROM Sales.SalesTransaction";
        ResultSet res = conn.createStatement().executeQuery(sql);

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

    public static List<SalesTransaction> getAllSalesTransactionsOrderBy(String orderBy) throws SQLException {
        Connection conn = Connect.openConnection();

        List<SalesTransaction> sales = new ArrayList<>();

        String sql = "SELECT * FROM Sales.SalesTransaction ORDER BY " + orderBy;
        ResultSet res = conn.createStatement().executeQuery(sql);

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
    
    public static List<TransactionItem> getSalesTransactionItemsById(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Sales.TransactionItem WHERE TransactionID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        List<TransactionItem> items = new ArrayList<>();

        while (res.next()) {
            items.add(new TransactionItem(
                res.getInt("TransactionItemID"),
                res.getInt("TransactionID"),
                res.getInt("SaleItemID"),
                res.getInt("Quantity")
            ));
        }

        return items;
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

    public static int getTransactionItemPurchases(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT COUNT(*) FROM Sales.TransactionItem WHERE TransactionID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        if (res.next()) {
            return res.getInt(1);
        }
        return 0;
    }
}
