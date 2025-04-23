package app.db.dao.production;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.db.Connect;
import app.db.pojo.production.Category;

public class CategoryDao {
    public static void addCategory(Category category) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "INSERT INTO Production.Category (CategoryName) VALUES (?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, category.getCategoryName());
        stmt.executeUpdate();
    }

    public static void updateCategory(Category category) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "UPDATE Production.Category SET CategoryName = ? WHERE CategoryID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, category.getCategoryName());
        stmt.setInt(2, category.getCategoryId());
        stmt.executeUpdate();
    }

    public static void deleteCategory(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "DELETE FROM Production.Category WHERE CategoryID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    public static Category getCategoryById(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Production.Category WHERE CategoryID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            return new Category(
                res.getInt("CategoryID"),
                res.getString("CategoryName")
            );
        }

        return null;
    }

    public static Category getCategoryByName(String name) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Production.Category WHERE CategoryName = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);

        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            return new Category(
                res.getInt("CategoryID"),
                res.getString("CategoryName")
            );
        }

        return null;
    }

    public static Category getLatestCategory() throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT TOP 1 * FROM Production.Category ORDER BY CategoryID DESC";
        ResultSet res = conn.createStatement().executeQuery(sql);
        while (res.next()) {
            return new Category(
                res.getInt("CategoryID"),
                res.getString("CategoryName")
            );
        }

        return null;
    }

    public static List<Category> getAllCategories() throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Production.Category";
        List<Category> categories = new ArrayList<>();
        ResultSet res = conn.createStatement().executeQuery(sql);

        while (res.next()) {
            categories.add(new Category(
                res.getInt("CategoryID"),
                res.getString("CategoryName")
            ));
        }

        return categories;
    }

    public static List<Category> getAllCategoriesOrderBy(String orderBy) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT * FROM Production.Category ORDER BY " + orderBy;
        List<Category> categories = new ArrayList<>();
        ResultSet res = conn.createStatement().executeQuery(sql);

        while (res.next()) {
            categories.add(new Category(
                res.getInt("CategoryID"),
                res.getString("CategoryName")
            ));
        }

        return categories;
    }

    public static int getCategoryItemsCount(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT COUNT(*) FROM Production.Item WHERE CategoryID = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            return res.getInt(1);
        }
        return 0;
    }

    public static double getCategoryItemTotalPrice(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT TotalPrice FROM VW_CategoryItemTotalPrice WHERE CategoryID = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            return res.getDouble("TotalPrice");
        }
        return 0;
    }
}
