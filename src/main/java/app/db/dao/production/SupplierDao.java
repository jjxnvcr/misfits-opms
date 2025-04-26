package app.db.dao.production;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import app.db.Connect;
import app.db.pojo.production.Supplier;

public class SupplierDao {
    public static void addSupplier(Supplier supplier) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "INSERT INTO Production.Supplier (SupplierName, ContactNumber) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, supplier.getSupplierName());
        stmt.setString(2, supplier.getContactNumber());
        stmt.executeUpdate();
    }

    public static void updateSupplier(Supplier supplier) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "UPDATE Production.Supplier SET SupplierName = ?, ContactNumber = ? WHERE SupplierID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, supplier.getSupplierName());
        stmt.setString(2, supplier.getContactNumber());
        stmt.setInt(3, supplier.getSupplierId());
        stmt.executeUpdate();
    }

    public static void deleteSupplier(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "DELETE FROM Production.Supplier WHERE SupplierID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    public static Supplier getSupplierById(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Production.Supplier WHERE SupplierID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            return new Supplier(res.getInt("SupplierID"), res.getString("SupplierName"), res.getString("ContactNumber"));
        }
        return null;
    }

    public static Supplier getSupplierByName(String name) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Production.Supplier WHERE SupplierName = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);

        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            return new Supplier(res.getInt("SupplierID"), res.getString("SupplierName"), res.getString("ContactNumber"));
        }
        return null;
    }

    public static Supplier getLatestSupplier() throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT TOP 1 * FROM Production.Supplier ORDER BY SupplierID DESC";
        ResultSet res = conn.createStatement().executeQuery(sql);
        while (res.next()) {
            return new Supplier(res.getInt("SupplierID"), res.getString("SupplierName"), res.getString("ContactNumber"));
        }
        return null;
    }

    public static List<Supplier> getAllSuppliers() throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Production.Supplier";
        List<Supplier> suppliers = new ArrayList<>();

        ResultSet res = conn.createStatement().executeQuery(sql);
        while (res.next()) {
            suppliers.add(new Supplier(res.getInt("SupplierID"), res.getString("SupplierName"), res.getString("ContactNumber")));
        }

        return suppliers;
    }

    public static List<Supplier> getAllSuppliersOrderBy(String orderBy) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Production.Supplier ORDER BY " + orderBy;
        List<Supplier> suppliers = new ArrayList<>();

        ResultSet res = conn.createStatement().executeQuery(sql);
        while (res.next()) {
            suppliers.add(new Supplier(res.getInt("SupplierID"), res.getString("SupplierName"), res.getString("ContactNumber")));
        }

        return suppliers;
    }

    public static List<Supplier> getAllSuppliersSearchBy(String search) throws SQLException {
        LinkedHashMap<String, String> columnValuePairs = new LinkedHashMap<>();

        String[] parseSearch = search.split(":");

        for (int i = 0; i < parseSearch.length; i++) {
            columnValuePairs.put(parseSearch[i].split("=")[0].substring(1), parseSearch[i].split("=")[1].trim());
        }

        String firstKey = columnValuePairs.keySet().stream().findFirst().get();
        String secondKey = columnValuePairs.keySet().stream().skip(1).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(1).findFirst().get();
        String thirdKey = columnValuePairs.keySet().stream().skip(2).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(2).findFirst().get();

        String sql = String.format("SELECT * FROM Production.Supplier WHERE (%s %s %s)",
                firstKey + " LIKE ?",
                secondKey.isBlank() ? "" : "AND " + secondKey + " LIKE ?",
                thirdKey.isBlank() ? "" : "AND " + thirdKey + " LIKE ?"
        );

        Connection conn = Connect.openConnection();

        List<Supplier> suppliers = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, "%" + columnValuePairs.get(firstKey) + "%");

        if (!secondKey.isBlank()) {
            stmt.setString(2, "%" + columnValuePairs.get(secondKey) + "%");
        } else if (!thirdKey.isBlank()) {
            stmt.setString(3, "%" + columnValuePairs.get(thirdKey) + "%");
        }

        ResultSet res = stmt.executeQuery();
        while (res.next()) {            
            suppliers.add(new Supplier(res.getInt("SupplierID"), res.getString("SupplierName"), res.getString("ContactNumber")));
        }

        return suppliers;
    }

    public static List<Supplier> getAllSuppliersSearchByOrderBy(String search, String orderBy) throws SQLException {
        LinkedHashMap<String, String> columnValuePairs = new LinkedHashMap<>();

        String[] parseSearch = search.split(":");

        for (int i = 0; i < parseSearch.length; i++) {
            columnValuePairs.put(parseSearch[i].split("=")[0].substring(1), parseSearch[i].split("=")[1].trim());
        }

        String firstKey = columnValuePairs.keySet().stream().findFirst().get();
        String secondKey = columnValuePairs.keySet().stream().skip(1).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(1).findFirst().get();
        String thirdKey = columnValuePairs.keySet().stream().skip(2).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(2).findFirst().get();

        String sql = String.format("SELECT * FROM Production.Supplier WHERE (%s %s %s) ORDER BY %s",
                firstKey + " LIKE ?",
                secondKey.isBlank() ? "" : "AND " + secondKey + " LIKE ?",
                thirdKey.isBlank() ? "" : "AND " + thirdKey + " LIKE ?",
                orderBy
        );
        
        Connection conn = Connect.openConnection();

        List<Supplier> suppliers = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, "%" + columnValuePairs.get(firstKey) + "%");

        if (!secondKey.isBlank()) {
            stmt.setString(2, "%" + columnValuePairs.get(secondKey) + "%");
        } else if (!thirdKey.isBlank()) {
            stmt.setString(3, "%" + columnValuePairs.get(thirdKey) + "%");
        }

        ResultSet res = stmt.executeQuery();
        while (res.next()) {            
            suppliers.add(new Supplier(res.getInt("SupplierID"), res.getString("SupplierName"), res.getString("ContactNumber")));
        }

        return suppliers;
    }

    public static int getSupplierSupplyOrderCount(int id, String status) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "EXEC PROC_GetSupplierSupplyOrderCount @SupplierID = ?, @OrderStatus = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.setString(2, status);

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            return res.getInt(1);
        }

        return 0;
    }

    public static int getSupplierSupplyItemCount(int id, String status) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "EXEC PROC_GetSupplierSupplyItemCount @SupplierID = ?, @OrderStatus = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.setString(2, status);

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            return res.getInt(1);
        }

        return 0;
    }
}
