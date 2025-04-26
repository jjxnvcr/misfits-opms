package app.db.dao.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import app.db.Connect;
import app.db.pojo.customer.Customer;

public class CustomerDao {

    public static void addCustomer(Customer customer) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "INSERT INTO Customer.Customer (FirstName, LastName, ContactNumber, Street, Barangay, City, Province) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, customer.getFirstName());
        stmt.setString(2, customer.getLastName());
        stmt.setString(3, customer.getContactNumber());
        stmt.setString(4, customer.getStreet());
        stmt.setString(5, customer.getBarangay());
        stmt.setString(6, customer.getCity());
        stmt.setString(7, customer.getProvince());
        stmt.executeUpdate();
    }

    public static void updateCustomer(Customer customer) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "UPDATE Customer.Customer SET FirstName = ?, LastName = ?, ContactNumber = ?, Street = ?, Barangay = ?, City = ?, Province = ? WHERE CustomerID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, customer.getFirstName());
        stmt.setString(2, customer.getLastName());
        stmt.setString(3, customer.getContactNumber());
        stmt.setString(4, customer.getStreet());
        stmt.setString(5, customer.getBarangay());
        stmt.setString(6, customer.getCity());
        stmt.setString(7, customer.getProvince());
        stmt.setInt(8, customer.getCustomerId());
        stmt.executeUpdate();
    }

    public static void deleteCustomer(int id) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "DELETE FROM Customer.Customer WHERE CustomerID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    public static Customer getLatestCustomer() {
        try (Connection conn = Connect.openConnection()) {
            String sql = "SELECT TOP 1 * FROM Customer.Customer ORDER BY CustomerID DESC";
            ResultSet res = conn.createStatement().executeQuery(sql);
            while (res.next()) {
                return new Customer(
                    res.getInt("CustomerID"),
                    res.getString("FirstName"),
                    res.getString("LastName"),
                    res.getString("ContactNumber"),
                    res.getString("Street"),
                    res.getString("Barangay"),
                    res.getString("City"),
                    res.getString("Province")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Customer getCustomerById(int id) {
        try (Connection conn = Connect.openConnection()) {
            String sql = "SELECT * FROM Customer.Customer WHERE CustomerID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                return new Customer(    
                    res.getInt("CustomerID"),
                    res.getString("FirstName"),
                    res.getString("LastName"),
                    res.getString("ContactNumber"), 
                    res.getString("Street"),
                    res.getString("Barangay"),
                    res.getString("City"),
                    res.getString("Province")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Customer getCustomerByName(String firstName, String lastName) throws SQLException {
        Connection conn = Connect.openConnection();
    
        String sql = "SELECT * FROM Customer.Customer WHERE FirstName = ? AND LastName = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, firstName);
        stmt.setString(2, lastName);

        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            return new Customer(
                res.getInt("CustomerID"),
                res.getString("FirstName"),
                res.getString("LastName"),
                res.getString("ContactNumber"),
                res.getString("Street"),
                res.getString("Barangay"),
                res.getString("City"),
                res.getString("Province")
            );
        }

        return null;
    }

    public static List<Customer> getAllCustomers() {
        try (Connection conn = Connect.openConnection()) {
            String sql = "SELECT * FROM Customer.Customer";
            List<Customer> customers = new ArrayList<>();
            ResultSet res = conn.createStatement().executeQuery(sql);
            
            while (res.next()) {
                customers.add(new Customer(
                    res.getInt("CustomerID"),
                    res.getString("FirstName"),
                    res.getString("LastName"),
                    res.getString("ContactNumber"),
                    res.getString("Street"),
                    res.getString("Barangay"),
                    res.getString("City"),
                    res.getString("Province")
                ));
            }

            return customers;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Customer> getAllCustomersOrderBy(String orderBy) {
        try (Connection conn = Connect.openConnection()) {
            String sql = "SELECT * FROM Customer.Customer ORDER BY " + orderBy;
            List<Customer> customers = new ArrayList<>();
            ResultSet res = conn.createStatement().executeQuery(sql);
            
            while (res.next()) {
                customers.add(new Customer(
                    res.getInt("CustomerID"),
                    res.getString("FirstName"),
                    res.getString("LastName"),
                    res.getString("ContactNumber"),
                    res.getString("Street"),
                    res.getString("Barangay"),
                    res.getString("City"),
                    res.getString("Province")
                ));
            }

            return customers;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Customer> getAllCustomersSearchBy(String search) throws SQLException {
        LinkedHashMap<String, String> columnValuePairs = new LinkedHashMap<>();

        String[] parseSearch = search.split(":");

        for (int i = 0; i < parseSearch.length; i++) {
            columnValuePairs.put(parseSearch[i].split("=")[0].substring(1), parseSearch[i].split("=")[1].trim());
        }

        String firstKey = columnValuePairs.keySet().stream().findFirst().get();
        String secondKey = columnValuePairs.keySet().stream().skip(1).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(1).findFirst().get();
        String thirdKey = columnValuePairs.keySet().stream().skip(2).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(2).findFirst().get();
        String fourthKey = columnValuePairs.keySet().stream().skip(3).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(3).findFirst().get();
        String fifthKey = columnValuePairs.keySet().stream().skip(4).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(4).findFirst().get();
        String sixthKey = columnValuePairs.keySet().stream().skip(5).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(5).findFirst().get();
        String seventhKey = columnValuePairs.keySet().stream().skip(6).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(6).findFirst().get();

        String sql = String.format("SELECT * FROM Customer.Customer WHERE (%s %s %s %s %s %s %s)", 
            firstKey + " LIKE ?",
            secondKey.isBlank() ? "" : "AND " + secondKey + " LIKE ?",
            thirdKey.isBlank() ? "" : "AND " + thirdKey + " LIKE ?",
            fourthKey.isBlank() ? "" : "AND " + fourthKey + " LIKE ?",
            fifthKey.isBlank() ? "" : "AND " + fifthKey + " LIKE ?",
            sixthKey.isBlank() ? "" : "AND " + sixthKey + " LIKE ?",
            seventhKey.isBlank() ? "" : "AND " + seventhKey + " LIKE ?"
            );

        Connection conn = Connect.openConnection();

        List<Customer> customers = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, "%" + columnValuePairs.get(firstKey) + "%");

        if (!secondKey.isBlank()) {
            stmt.setString(2, "%" + columnValuePairs.get(secondKey) + "%");
        } else if (!thirdKey.isBlank()) {
            stmt.setString(3, "%" + columnValuePairs.get(thirdKey) + "%");
        } else if (!fourthKey.isBlank()) {
            stmt.setString(4, "%" + columnValuePairs.get(fourthKey) + "%");
        } else if (!fifthKey.isBlank()) {
            stmt.setString(5, "%" + columnValuePairs.get(fifthKey) + "%");
        } else if (!sixthKey.isBlank()) {
            stmt.setString(6, "%" + columnValuePairs.get(sixthKey) + "%");
        } else if (!seventhKey.isBlank()) {
            stmt.setString(7, "%" + columnValuePairs.get(seventhKey) + "%");
        }

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            customers.add(new Customer(
                res.getInt("CustomerID"),
                res.getString("FirstName"),
                res.getString("LastName"),
                res.getString("ContactNumber"),
                res.getString("Street"),
                res.getString("Barangay"),
                res.getString("City"),
                res.getString("Province")
            ));
        }

        return customers;
    }

    public static List<Customer> getAllCustomersSearchByOrderBy(String search, String orderBy) throws SQLException {
        LinkedHashMap<String, String> columnValuePairs = new LinkedHashMap<>();

        String[] parseSearch = search.split(":");

        for (int i = 0; i < parseSearch.length; i++) {
            columnValuePairs.put(parseSearch[i].split("=")[0].substring(1), parseSearch[i].split("=")[1].trim());
        }
        String firstKey = columnValuePairs.keySet().stream().findFirst().get();
        String secondKey = columnValuePairs.keySet().stream().skip(1).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(1).findFirst().get();
        String thirdKey = columnValuePairs.keySet().stream().skip(2).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(2).findFirst().get();
        String fourthKey = columnValuePairs.keySet().stream().skip(3).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(3).findFirst().get();
        String fifthKey = columnValuePairs.keySet().stream().skip(4).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(4).findFirst().get();
        String sixthKey = columnValuePairs.keySet().stream().skip(5).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(5).findFirst().get();
        String seventhKey = columnValuePairs.keySet().stream().skip(6).findFirst().isEmpty() ? "" : columnValuePairs.keySet().stream().skip(6).findFirst().get();

        String sql = String.format("SELECT * FROM Customer.Customer WHERE (%s %s %s %s %s %s %s) ORDER BY %s", 
            firstKey + " LIKE ?",
            secondKey.isBlank() ? "" : "AND " + secondKey + " LIKE ?",
            thirdKey.isBlank() ? "" : "AND " + thirdKey + " LIKE ?",
            fourthKey.isBlank() ? "" : "AND " + fourthKey + " LIKE ?",
            fifthKey.isBlank() ? "" : "AND " + fifthKey + " LIKE ?",
            sixthKey.isBlank() ? "" : "AND " + sixthKey + " LIKE ?",
            seventhKey.isBlank() ? "" : "AND " + seventhKey + " LIKE ?",
            orderBy
            );

        Connection conn = Connect.openConnection();

        List<Customer> customers = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, "%" + columnValuePairs.get(firstKey) + "%");

        if (!secondKey.isBlank()) {
            stmt.setString(2, "%" + columnValuePairs.get(secondKey) + "%");
        } else if (!thirdKey.isBlank()) {
            stmt.setString(3, "%" + columnValuePairs.get(thirdKey) + "%");
        } else if (!fourthKey.isBlank()) {
            stmt.setString(4, "%" + columnValuePairs.get(fourthKey) + "%");
        } else if (!fifthKey.isBlank()) {
            stmt.setString(5, "%" + columnValuePairs.get(fifthKey) + "%");
        } else if (!sixthKey.isBlank()) {
            stmt.setString(6, "%" + columnValuePairs.get(sixthKey) + "%");
        } else if (!seventhKey.isBlank()) {
            stmt.setString(7, "%" + columnValuePairs.get(seventhKey) + "%");
        }

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            customers.add(new Customer(
                res.getInt("CustomerID"),
                res.getString("FirstName"),
                res.getString("LastName"),
                res.getString("ContactNumber"),
                res.getString("Street"),
                res.getString("Barangay"),
                res.getString("City"),
                res.getString("Province")
            ));
        }

        return customers;
    }

    public static double getCustomerTotalSpending(int id) throws SQLException {
        Connection conn = Connect.openConnection();
        
        String sql = "SELECT TotalSpending FROM VW_CustomerSpending WHERE CustomerID = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        if (res.next()) {
            return res.getDouble(1);
        }
        return 0;
    }

    public static double getCustomerTotalSpending(int id, String status) throws SQLException {
        Connection conn = Connect.openConnection();
        
        String sql = "SELECT TotalSpending FROM VW_CustomerSpending WHERE CustomerID = ? AND DeliveryStatus = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.setString(2, status);

        ResultSet res = stmt.executeQuery();

        if (res.next()) {
            return res.getDouble(1);
        }
        return 0;
    }

    public static int getCustomerDeliveryCount(int id, String status) throws SQLException {
        Connection conn = Connect.openConnection();

        String sql = "SELECT DeliveryStatusCount FROM VW_CustomerDeliveryStatusInfo WHERE CustomerID = ? AND DeliveryStatus = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.setString(2, status);

        ResultSet res = stmt.executeQuery();

        if (res.next()) {
            return res.getInt("DeliveryStatusCount");
        }
        return 0;
    }
}


