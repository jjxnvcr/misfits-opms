package app.db.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import app.db.Connect;
import app.db.pojo.sales.SaleItem;

public class Dash {
    public static int getDailyTotalTransactionCount() {
        try (Connection conn = Connect.openConnection()) {
            String sql = "SELECT COUNT(*) AS TotalSales FROM Sales.SalesTransaction WHERE TransactionDate = getDate()";

            ResultSet stmt = conn.createStatement().executeQuery(sql);

            if (stmt.next()) {
                return stmt.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static double getDailyTotalRevenue() {
        try (Connection conn = Connect.openConnection()) {
            String sql = "SELECT * FROM VW_DailyRevenue";
            
            ResultSet stmt = conn.createStatement().executeQuery(sql);

            if (stmt.next()) {
                return stmt.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static double getWeeklyTotalRevenue() {
        try (Connection conn = Connect.openConnection()) {
            String sql = "SELECT * FROM VW_WeeklyRevenue";

            ResultSet stmt = conn.createStatement().executeQuery(sql);

            if (stmt.next()) {
                return stmt.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static double getMonthlyTotalRevenue() {
        try (Connection conn = Connect.openConnection()) {
            String sql = "SELECT * FROM VW_MonthlyRevenue";

            ResultSet stmt = conn.createStatement().executeQuery(sql);

            if (stmt.next()) {
                return stmt.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    public static double getAverageTransactionSpending() {
        try (Connection conn = Connect.openConnection()) {
            String sql = "SELECT * FROM VW_AverageTransactionSpending";

            ResultSet stmt = conn.createStatement().executeQuery(sql);

            if (stmt.next()) {
                return stmt.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static List<SaleItem> getTopSellingItems(int limit) {
        try (Connection conn = Connect.openConnection()) {
            String sql = "SELECT TOP " + limit + " * FROM VW_TopSellingItems ORDER BY TotalQuantity DESC";

            List<SaleItem> saleItems = new ArrayList<>();

            ResultSet res = conn.createStatement().executeQuery(sql);
            while (res.next()) {
                saleItems.add(new SaleItem(
                    res.getInt("SaleItemID"),
                    res.getInt("ItemID"),
                    res.getDouble("ItemPrice"),
                    res.getInt("TotalQuantity"),
                    res.getString("MeasurementSystem"),
                    res.getDouble("NumericSize"),
                    res.getString("AlphaSize")
                ));
            }

            return saleItems;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int getTotalItemInStockCount() {
        try (Connection conn = Connect.openConnection()) {
            String sql = "SELECT COUNT(*) FROM Sales.SaleItem WHERE ItemQuantity > 0";

            ResultSet stmt = conn.createStatement().executeQuery(sql);

            if (stmt.next()) {
                return stmt.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static int getTotalCustomerCount() {
        try (Connection conn = Connect.openConnection()) {
            String sql = "SELECT COUNT(*) FROM Customer.Customer";

            ResultSet stmt = conn.createStatement().executeQuery(sql);

            if (stmt.next()) {
                return stmt.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static int getTotalItemOutOfStockCount() {
        try (Connection conn = Connect.openConnection()) {
            String sql = "SELECT COUNT(*) FROM Sales.SaleItem WHERE ItemQuantity = 0";

            ResultSet stmt = conn.createStatement().executeQuery(sql);

            if (stmt.next()) {
                return stmt.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static double getInventoryValue() {
        try (Connection conn = Connect.openConnection()) {
            String sql = "SELECT SUM(ItemPrice * ItemQuantity) FROM Sales.SaleItem";

            ResultSet stmt = conn.createStatement().executeQuery(sql);

            if (stmt.next()) {
                return stmt.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static Map<Date, Double> getSaleTrendData() {
        try (Connection conn = Connect.openConnection()) {
            String sql = "SELECT * FROM VW_SaleTrend";

            ResultSet stmt = conn.createStatement().executeQuery(sql);

            Map<Date, Double> saleTrend = new LinkedHashMap<>();

            while (stmt.next()) {
                saleTrend.put(stmt.getDate("TransactionDate"), stmt.getDouble("TotalRevenue"));
            }

            return saleTrend;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Map<String, Integer> getPaymentTypesCount() {
        try (Connection conn = Connect.openConnection()) {
            String sql = "SELECT PaymentType, COUNT(*) FROM Sales.SalesTransaction GROUP BY PaymentType";

            ResultSet stmt = conn.createStatement().executeQuery(sql);

            Map<String, Integer> paymentTypes = new LinkedHashMap<>();

            while (stmt.next()) {
                paymentTypes.put(stmt.getString("PaymentType"), stmt.getInt(2));
            }

            return paymentTypes;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int getDeliveries(String status) {
        try (Connection conn = Connect.openConnection()) {
            String sql = "SELECT COUNT(*) FROM Sales.Delivery INNER JOIN Sales.SalesTransaction ON Sales.Delivery.TransactionID = Sales.SalesTransaction.TransactionID WHERE DeliveryStatus = '" + status + "'";

            ResultSet stmt = conn.createStatement().executeQuery(sql);

            if (stmt.next()) {
                return stmt.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static List<Date> getReportDateRange() {
        try (Connection conn = Connect.openConnection()) {
            String sql = "SELECT DATEFROMPARTS(YEAR(TransactionDate), MONTH(TransactionDate), 1) AS Monthly FROM Sales.SalesTransaction GROUP BY YEAR(TransactionDate), MONTH(TransactionDate) ORDER BY Monthly";

            ResultSet stmt = conn.createStatement().executeQuery(sql);

            List<Date> reportDateRange = new ArrayList<>();

            while (stmt.next()) {
                reportDateRange.add(stmt.getDate("Monthly"));
            }

            return reportDateRange;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
