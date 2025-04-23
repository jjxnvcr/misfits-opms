package app.db.dao.production;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import app.db.Connect;

import app.db.pojo.production.Item;

public class ItemDao {
    public static void addItem(Item item) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "INSERT INTO Production.Item (CategoryID, ItemName, ItemDescription, ItemColor) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, item.getCategoryId());
        stmt.setString(2, item.getItemName());
        stmt.setString(3, item.getItemDescription());
        stmt.setString(4, item.getItemColor());
        stmt.executeUpdate();
    }

    public static void updateItem(Item item) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "UPDATE Production.Item SET CategoryID = ?, ItemName = ?, ItemDescription = ?, ItemColor = ? WHERE ItemID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, item.getCategoryId());
        stmt.setString(2, item.getItemName());
        stmt.setString(3, item.getItemDescription());
        stmt.setString(4, item.getItemColor());
        stmt.setInt(5, item.getItemId());
        stmt.executeUpdate();
    }

    public static void deleteItem(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "DELETE FROM Production.Item WHERE ItemID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
    
    public static Item getItemById(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Production.Item WHERE ItemID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            return new Item(
                res.getInt("ItemID"), 
                res.getInt("CategoryID"), 
                res.getString("ItemName"), 
                res.getString("ItemDescription"), 
                res.getString("ItemColor")
            );
        }
        return null;
    }

    public static Item getLatestItem() throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT TOP 1 * FROM Production.Item ORDER BY ItemID DESC";
        PreparedStatement stmt = conn.prepareStatement(sql);

        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            return new Item(
                res.getInt("ItemID"), 
                res.getInt("CategoryID"), 
                res.getString("ItemName"), 
                res.getString("ItemDescription"), 
                res.getString("ItemColor")
            );
        }
        return null;
    }

    public static List<Item> getAllItems() throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Production.Item";
        List<Item> items = new ArrayList<>();
        ResultSet res = conn.createStatement().executeQuery(sql);
        
        while (res.next()) {
            items.add(new Item(
                res.getInt("ItemID"), 
                res.getInt("CategoryID"), 
                res.getString("ItemName"), 
                res.getString("ItemDescription"), 
                res.getString("ItemColor")
                ));
        }

        return items;
    }

    public static List<Item> getAllItemsOrderBy(String orderBy) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Production.Item ORDER BY " + orderBy;
        List<Item> items = new ArrayList<>();
        ResultSet res = conn.createStatement().executeQuery(sql);
        
        while (res.next()) {
            items.add(new Item(
                res.getInt("ItemID"), 
                res.getInt("CategoryID"), 
                res.getString("ItemName"), 
                res.getString("ItemDescription"), 
                res.getString("ItemColor")
                ));
        }

        return items;
    }

    public static List<Item> getAllItemsSearchBy(String search) throws SQLException {
        LinkedHashMap<String, String> columnValuePairs = new LinkedHashMap<>();

        String[] parseSearch = search.split(":");

        for (int i = 0; i < parseSearch.length; i++) {
            columnValuePairs.put(parseSearch[i].split("=")[0].substring(1), parseSearch[i].split("=")[1].trim());
        }

        String firstKey = columnValuePairs.keySet().stream().findFirst().get();
        String secondKey = columnValuePairs.keySet().stream().skip(1).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(1).findFirst().get();
        String thirdKey = columnValuePairs.keySet().stream().skip(2).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(2).findFirst().get();

        String sql = String.format("SELECT * FROM Production.Item WHERE (%s %s %s)", 
            firstKey + " LIKE ?",
            secondKey.isBlank() ? "" : "AND " + secondKey + " LIKE ?",
            thirdKey.isBlank() ? "" : "AND " + thirdKey + " LIKE ?"
            );

        Connection conn = Connect.openConnection();

        List<Item> items = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, "%" + columnValuePairs.get(firstKey) + "%");

        if (!secondKey.isBlank()) {
            stmt.setString(2, "%" + columnValuePairs.get(secondKey) + "%");
        } else if (!thirdKey.isBlank()) {
            stmt.setString(3, "%" + columnValuePairs.get(thirdKey) + "%");
        }

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            items.add(new Item(
                res.getInt("ItemID"), 
                res.getInt("CategoryID"), 
                res.getString("ItemName"), 
                res.getString("ItemDescription"), 
                res.getString("ItemColor")
                ));
        }

        return items;
    }

    public static List<Item> getAllItemsSearchByOrderBy(String search, String orderBy) throws SQLException {
        LinkedHashMap<String, String> columnValuePairs = new LinkedHashMap<>();

        String[] parseSearch = search.split(":");

        for (int i = 0; i < parseSearch.length; i++) {
            columnValuePairs.put(parseSearch[i].split("=")[0].substring(1), parseSearch[i].split("=")[1].trim());
        }

        String firstKey = columnValuePairs.keySet().stream().findFirst().get();
        String secondKey = columnValuePairs.keySet().stream().skip(1).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(1).findFirst().get();
        String thirdKey = columnValuePairs.keySet().stream().skip(2).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(2).findFirst().get();

        String sql = String.format("SELECT * FROM Production.Item WHERE (%s %s %s) ORDER BY %s", 
            firstKey + " LIKE ?",
            secondKey.isBlank() ? "" : "AND " + secondKey + " LIKE ?",
            thirdKey.isBlank() ? "" : "AND " + thirdKey + " LIKE ?",
            orderBy
            );

        Connection conn = Connect.openConnection();

        List<Item> items = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, "%" + columnValuePairs.get(firstKey) + "%");

        if (!secondKey.isBlank()) {
            stmt.setString(2, "%" + columnValuePairs.get(secondKey) + "%");
        } else if (!thirdKey.isBlank()) {
            stmt.setString(3, "%" + columnValuePairs.get(thirdKey) + "%");
        }

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            items.add(new Item(
                res.getInt("ItemID"), 
                res.getInt("CategoryID"), 
                res.getString("ItemName"), 
                res.getString("ItemDescription"), 
                res.getString("ItemColor")
                ));
        }

        return items;
    }

    public static List<Item> getAllItemsByCategory(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Production.Item WHERE CategoryID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        List<Item> items = new ArrayList<>();

        while (res.next()) {
            items.add(new Item(
                res.getInt("ItemID"), 
                res.getInt("CategoryID"), 
                res.getString("ItemName"),  
                res.getString("ItemDescription"), 
                res.getString("ItemColor")
                ));
        }

        return items;
    }

    public static List<Item> getAllItemsByCategoryOrderBy(int id, String orderBy) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Production.Item WHERE CategoryID = ? ORDER BY " + orderBy;
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        List<Item> items = new ArrayList<>();

        while (res.next()) {
            items.add(new Item(
                res.getInt("ItemID"), 
                res.getInt("CategoryID"), 
                res.getString("ItemName"),  
                res.getString("ItemDescription"), 
                res.getString("ItemColor")
                ));
        }

        return items;
    }

    
    public static List<Item> getAllItemsByCategorySearchBy(int id, String search) throws SQLException {
        LinkedHashMap<String, String> columnValuePairs = new LinkedHashMap<>();

        String[] parseSearch = search.split(":");

        for (int i = 0; i < parseSearch.length; i++) {
            columnValuePairs.put(parseSearch[i].split("=")[0].substring(1), parseSearch[i].split("=")[1].trim());
        }

        String firstKey = columnValuePairs.keySet().stream().findFirst().get();
        String secondKey = columnValuePairs.keySet().stream().skip(1).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(1).findFirst().get();
        String thirdKey = columnValuePairs.keySet().stream().skip(2).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(2).findFirst().get();

        String sql = String.format("SELECT * FROM Production.Item WHERE (CategoryID = ? AND %s %s %s)",
            firstKey + " LIKE ?",
            secondKey.isBlank() ? "" : "AND " + secondKey + " LIKE ?",
            thirdKey.isBlank() ? "" : "AND " + thirdKey + " LIKE ?"
            );

        Connection conn = Connect.openConnection();

        List<Item> items = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.setString(2, "%" + columnValuePairs.get(firstKey) + "%");

        if (!secondKey.isBlank()) {
            stmt.setString(3, "%" + columnValuePairs.get(secondKey) + "%");
        } else if (!thirdKey.isBlank()) {
            stmt.setString(4, "%" + columnValuePairs.get(thirdKey) + "%");
        }

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            items.add(new Item(
                res.getInt("ItemID"), 
                res.getInt("CategoryID"), 
                res.getString("ItemName"), 
                res.getString("ItemDescription"), 
                res.getString("ItemColor")
                ));
        }

        return items;
    }

    public static List<Item> getAllItemsByCategorySearchByOrderBy(int id, String search, String orderBy) throws SQLException {
        LinkedHashMap<String, String> columnValuePairs = new LinkedHashMap<>();

        String[] parseSearch = search.split(":");

        for (int i = 0; i < parseSearch.length; i++) {
            columnValuePairs.put(parseSearch[i].split("=")[0].substring(1), parseSearch[i].split("=")[1].trim());
        }

        String firstKey = columnValuePairs.keySet().stream().findFirst().get();
        String secondKey = columnValuePairs.keySet().stream().skip(1).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(1).findFirst().get();
        String thirdKey = columnValuePairs.keySet().stream().skip(2).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(2).findFirst().get();

        String sql = String.format("SELECT * FROM Production.Item WHERE (CategoryID = ? AND %s %s %s) ORDER BY %s",
            firstKey + " LIKE ?",
            secondKey.isBlank() ? "" : "AND " + secondKey + " LIKE ?",
            thirdKey.isBlank() ? "" : "AND " + thirdKey + " LIKE ?",
            orderBy
            );

        Connection conn = Connect.openConnection();

        List<Item> items = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.setString(2, "%" + columnValuePairs.get(firstKey) + "%");

        if (!secondKey.isBlank()) {
            stmt.setString(3, "%" + columnValuePairs.get(secondKey) + "%");
        } else if (!thirdKey.isBlank()) {
            stmt.setString(4, "%" + columnValuePairs.get(thirdKey) + "%");
        }

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            items.add(new Item(
                res.getInt("ItemID"), 
                res.getInt("CategoryID"), 
                res.getString("ItemName"), 
                res.getString("ItemDescription"), 
                res.getString("ItemColor")
                ));
        }

        return items;
    }

    public static int getTotalTransactions(int id) throws SQLException {
        Connection conn = Connect.openConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT TotalTransactions FROM VW_ItemPurchased WHERE ItemID = ?");

        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            return res.getInt("TotalTransactions");
        }
        return 0;
    }

    public static int getTotalPurchases(int id) throws SQLException {
        Connection conn = Connect.openConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT TotalPurchases FROM VW_ItemPurchased WHERE ItemID = ?");

        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            return res.getInt("TotalPurchases");
        }
        return 0;
    }
}
