package app.software.glance;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.PieSeries.PieSeriesRenderStyle;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.XYStyler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;

import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import com.formdev.flatlaf.extras.components.FlatLabel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import app.components.Page;
import app.components.PopupDialog;
import app.components.ScrollList;
import app.db.Connect;
import app.db.dao.Dash;
import app.db.dao.production.ItemDao;
import app.db.pojo.column.DeliveryStatus;
import app.db.pojo.column.Measurement;
import app.db.pojo.sales.SaleItem;
import app.software.App;
import app.software.cashier.view.CartScrollView;
import app.utils.ClientProperty;
import app.utils.DialogType;
import app.utils.Iconify;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class GlancePage extends Page {
    private java.sql.Date reportDate;

    public GlancePage(App owner) {
        super(new MigLayout("insets 0, fillx", "[70%][30%]"), false);

        Page reportPage = new Page(new MigLayout("insets 0, fillx", "[15%][85%]"), false);

        FlatComboBox<String> reportDateComboBox = new FlatComboBox<>();
        reportDateComboBox.setOpaque(false);
        reportDateComboBox.setBackground(Palette.CRUST.color());
        reportDateComboBox.setForeground(Palette.TEXT.color());
        reportDateComboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ClientProperty.setProperty(reportDateComboBox, "arc", "15");
        ClientProperty.setProperty(reportDateComboBox, "popupBackground", Palette.CRUST.varString());
        ClientProperty.setProperty(reportDateComboBox, "buttonBackground", null);
        ClientProperty.setProperty(reportDateComboBox, "buttonStyle", "none");
        ClientProperty.setProperty(reportDateComboBox, "arrowType", "triangle");
        ClientProperty.setProperty(reportDateComboBox, "maximumRowCount", "3");
        ClientProperty.setProperty(reportDateComboBox, "padding", "10, 5, 10, 5");

        Dash.getReportDateRange().forEach(d -> reportDateComboBox.addItem("" + d));

        reportDateComboBox.addActionListener(e -> reportDate = java.sql.Date.valueOf(reportDateComboBox.getSelectedItem().toString()));
        reportDateComboBox.setSelectedIndex(reportDateComboBox.getItemCount() - 1);

        FlatButton reportButton = new FlatButton();
        reportButton.setText("Generate Sales Report (XLSX)");
        reportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ClientProperty.setProperty(reportButton, "arc", "10");
        reportButton.setBackground(Palette.CRUST.color());
        reportButton.setForeground(Palette.TEXT.color());
        reportButton.addActionListener(e -> generateSalesDateReport());

        reportPage.add(reportButton, "growy");
        reportPage.add(reportDateComboBox, "grow");

        Page page1 = new Page(new MigLayout("insets 0, fillx, wrap 5"), false);
        Page page2 = new Page(new MigLayout("insets 0, fillx", "[50%][50%]"), false);

        Map<java.sql.Date, Double> saleTrendData = Dash.getSaleTrendData();

        page1.add(reportPage, "span, grow, wrap");

        page1.add(createChart(new ArrayList<>(saleTrendData.keySet()), new ArrayList<>(saleTrendData.values())), "span, grow, wrap");

        page1.add(createCard("Total Customers", String.format("%,d", Dash.getTotalCustomerCount()), Palette.PEACH.color()), "grow");

        page1.add(createCard("Daily Transactions", String.format("%,d", Dash.getDailyTotalTransactionCount()), Palette.MAROON.color()), "grow");

        page1.add(createCard("Average Sale Spending", String.format("%,.2f", Dash.getAverageTransactionSpending()), Palette.MAROON.color(), new Iconify("peso", Palette.MAROON.color())), "grow");

        page1.add(createCard("Inventory Value", String.format("%,.2f", Dash.getInventoryValue()), Palette.BLUE.color(), new Iconify("peso", Palette.BLUE.color())), "grow");

        page1.add(createCard("Items in Stock", String.format("%,d", Dash.getTotalItemInStockCount()), Palette.BLUE.color()), "grow");

        page1.add(createCard("Items Out of Stock", String.format("%,d", Dash.getTotalItemOutOfStockCount()), Palette.BLUE.color()), "grow");

        page1.add(createCard("Pending Deliveries", String.format("%,d", Dash.getDeliveries(DeliveryStatus.Pending.name())), Palette.YELLOW.color()), "grow");

        page1.add(createCard("Shipped Deliveries", String.format("%,d", Dash.getDeliveries(DeliveryStatus.Shipped.name())), Palette.YELLOW.color()), "grow");

        page1.add(createCard("Out for Delivery", String.format("%,d", Dash.getDeliveries("Out for Delivery")), Palette.YELLOW.color()), "span, grow");

        page2.add(createCard("Daily Revenue", String.format("%,.2f", Dash.getDailyTotalRevenue()), Palette.GREEN.color(), new Iconify("peso", Palette.GREEN.color())), "grow");

        page2.add(createCard("Weekly Revenue", String.format("%,.2f", Dash.getWeeklyTotalRevenue()), Palette.GREEN.color(), new Iconify("peso", Palette.GREEN.color())), "grow, wrap");

        page2.add(createCard("Monthly Revenue", String.format("%,.2f", Dash.getMonthlyTotalRevenue()), Palette.GREEN.color(), new Iconify("peso", Palette.GREEN.color())), "span, grow, wrap");

        page2.add(createList(Dash.getTopSellingItems(10)), "height 55%, span, grow");

        page2.add(createPie(Dash.getPaymentTypesCount()), "span, grow");

        add(page1, "grow");
        add(page2, "grow");
    }

    public Page createCard(String text, String value, Color foreground) {
        Page page = new Page(new MigLayout("fillx, wrap"));
        page.setArc(10);
        page.setBackground(Palette.MANTLE);
        page.darkenBackground(3);

        FlatLabel textLabel = new FlatLabel();
        textLabel.setText(text);
        textLabel.setForeground(Palette.SURFACE2.color());
        textLabel.setFont(textLabel.getFont().deriveFont(textLabel.getFont().getSize() + 2));
        textLabel.setHorizontalAlignment(FlatLabel.RIGHT);

        FlatLabel valueLabel = new FlatLabel();
        valueLabel.setText(value);
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 16));
        valueLabel.setForeground(foreground);
        valueLabel.setHorizontalAlignment(FlatLabel.RIGHT);

        page.add(textLabel, "growx");
        page.add(valueLabel, "growx");

        return page;
    }

    public Page createCard(String text, String value, Color foreground, Iconify icon) {
        Page page = new Page(new MigLayout("fillx, wrap"));
        page.setArc(10);
        page.setBackground(Palette.MANTLE);
        page.darkenBackground(3);

        FlatLabel textLabel = new FlatLabel();
        textLabel.setText(text);
        textLabel.setForeground(Palette.SURFACE2.color());
        textLabel.setFont(textLabel.getFont().deriveFont(textLabel.getFont().getSize() + 2));
        textLabel.setHorizontalAlignment(FlatLabel.RIGHT);

        FlatLabel valueLabel = new FlatLabel();
        valueLabel.setText(value);
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 16));
        valueLabel.setForeground(foreground);
        valueLabel.setHorizontalAlignment(FlatLabel.RIGHT);
        valueLabel.setIcon(icon.derive(valueLabel.getFont().getSize(), valueLabel.getFont().getSize()));
        valueLabel.setIconTextGap(5);

        page.add(textLabel, "growx");
        page.add(valueLabel, "growx");

        return page;
    }

    public Page createChart(List<Date> date, List<Double> sales) {
        Page page = new Page(new MigLayout("fill"));
        page.setArc(10);
        page.setBackground(Palette.MANTLE);
        page.darkenBackground(3);

        FlatLabel label = new FlatLabel();
        label.setText("Sales Trend");
        label.setForeground(Palette.SURFACE2.color());
        label.setFont(label.getFont().deriveFont(16f));

        XYChart chart = new XYChartBuilder().title("Sales Trend").xAxisTitle("Date").yAxisTitle("Sales - PHP (₱)").theme(ChartTheme.Matlab).build();

        XYStyler chartStyle = chart.getStyler();

        chartStyle.setAxisTitleFont(page.getFont().deriveFont(14f));
        chartStyle.setAxisTickLabelsFont(page.getFont().deriveFont(12f));
        chartStyle.setAntiAlias(true);
        
        chartStyle.setXAxisTickLabelsColor(Palette.SUBTEXT0.color());
        chartStyle.setYAxisTickLabelsColor(Palette.SUBTEXT0.color());
        chartStyle.setPlotBackgroundColor(Palette.TRANSPARENT.color());
        chartStyle.setChartFontColor(Palette.TEXT.color());
        chartStyle.setChartBackgroundColor(page.getBackground());

        chartStyle.setCursorColor(Palette.PINK.color());
        chartStyle.setCursorFontColor(Palette.TEXT.color());
        chartStyle.setCursorBackgroundColor(Palette.CRUST.color());

        chartStyle.setChartTitleVisible(false);
        chartStyle.setCursorLineWidth(2);
        chartStyle.setCursorEnabled(true);
        chartStyle.setPlotGridLinesColor(Palette.SURFACE1.color());
        chartStyle.setSeriesColors(new Color[] {Palette.PEACH.color()});

        chartStyle.setPlotBorderVisible(false); 
        chartStyle.setLegendVisible(false);
        chartStyle.setXAxisLabelRotation(45);
        chartStyle.setZoomResetByDoubleClick(true);
        chartStyle.setZoomEnabled(true);
        chartStyle.setChartButtonBackgroundColor(Palette.BLUE.color());
        chartStyle.setChartButtonFont(getFont().deriveFont(12f));
        chartStyle.setChartButtonFontColor(Palette.MANTLE.color());
        chartStyle.setChartButtonBorderColor(Palette.TRANSPARENT.color());
        chartStyle.setDatePattern("yyyy-MM-dd");

        chart.addSeries("Sales", date, sales);

        XChartPanel<XYChart> chartPanel = new XChartPanel<>(chart);
        chartPanel.setOpaque(false);

        page.add(label, "align 50%, wrap");
        page.add(chartPanel, "grow");

        return page;
    }

    public Page createPie(Map<String, Integer> data) {
        Page page = new Page(new MigLayout("fill"));
        page.setArc(10);
        page.setBackground(Palette.MANTLE);
        page.darkenBackground(3);
        
        FlatLabel label = new FlatLabel();
        label.setText("Payment Type Distribution");
        label.setForeground(Palette.SURFACE2.color());
        label.setFont(label.getFont().deriveFont(14f));

        PieChart chart = new PieChartBuilder().title("Payment Method Distribution").build();
        PieStyler chartStyle = chart.getStyler();
        chartStyle.setLegendVisible(true);
        chartStyle.setChartBackgroundColor(page.getBackground());
        chartStyle.setPlotBackgroundColor(Palette.TRANSPARENT.color());
        chartStyle.setChartFontColor(Palette.TEXT.color());
        chartStyle.setLegendFont(page.getFont().deriveFont(12f));
        chartStyle.setChartTitleVisible(false);
        chartStyle.setPlotBorderVisible(false);
        chartStyle.setLegendBackgroundColor(Palette.TRANSPARENT.color());
        chartStyle.setLegendBorderColor(Palette.TRANSPARENT.color());
        chartStyle.setSeriesColors(new Color[] {Palette.BLUE.color(), Palette.MAUVE.color(), Palette.TEAL.color()});
        chartStyle.setPlotContentSize(1);
        chartStyle.setAntiAlias(true);
        chartStyle.setDefaultSeriesRenderStyle(PieSeriesRenderStyle.Donut);
        chartStyle.setDonutThickness(.7);
        chartStyle.setLabelsFontColorAutomaticEnabled(true);
        chartStyle.setLabelsFontColor(page.getBackground());
        chartStyle.setLabelsFont(page.getFont().deriveFont(13f));
        

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            chart.addSeries(entry.getKey(), entry.getValue());
        }

        XChartPanel<PieChart> chartPanel = new XChartPanel<>(chart);
        chartPanel.setBackground(Palette.TRANSPARENT.color());

        page.add(label, "align 50%, wrap");
        page.add(chartPanel, "grow");

        return page;
    }
    
    public Page createList(List<SaleItem> items) {
        Page page = new Page(new MigLayout("fill", "[50%][50%]"));

        page.setArc(10);
        page.setBackground(Palette.MANTLE);
        page.darkenBackground(3);

        FlatLabel label = new FlatLabel();
        label.setText("Top Selling Items");
        label.setForeground(Palette.SURFACE2.color());

        FlatLabel quantityLabel = new FlatLabel();
        quantityLabel.setText("Sold");
        quantityLabel.setForeground(Palette.SURFACE2.color());

        CartScrollView view = new CartScrollView();

        for (SaleItem item : items) {
            Page itemPage = new Page(new MigLayout("insets 0, fillx", "[90%][10%]"), false);

            FlatLabel name = new FlatLabel();
            name.setText(items.indexOf(item) + 1 + ". ");
            name.setForeground(Palette.TEXT.color());
            try {
                DecimalFormat sizeFormat = new DecimalFormat();
                sizeFormat.setMinimumFractionDigits(0);
                sizeFormat.setMaximumFractionDigits(1);

                name.setText(name.getText() + ItemDao.getItemById(item.getItemId()).getItemName() + " (" + (item.getMeasurementSystem() == null ? "One Size" : item.getMeasurementSystem().equals(Measurement.Alpha.name()) ? item.getAlphaSize() : item.getMeasurementSystem() + " " + sizeFormat.format(item.getNumericSize())) + ")");
            } catch (SQLException e) {
                name.setText("Unknown");
            }

            FlatLabel quantity = new FlatLabel();
            quantity.setText(String.format("%,d", item.getItemQuantity()));
            quantity.setForeground(Palette.SUBTEXT0.color());

            itemPage.add(name, "growx");
            itemPage.add(quantity, "growx");

            view.add(itemPage, "growx");
        }
        page.add(label, "align 20%, pushx");
        page.add(quantityLabel, "align 80%, pushx, wrap");
        page.add(new ScrollList(view), "span, grow");

        return page;
    }

public void generateSalesDateReport() {
    Workbook workbook = new XSSFWorkbook();
    String[] sheetNames = {
        "Monthly Sales Trend",
        "Monthly Sales Transaction",
        "Monthly Most Sold Items"
    };

    try (Connection conn = Connect.openConnection()) {
        String sql = "EXEC PROC_GetMonthlySaleTrend @TransactionDate=?";
            
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setDate(1, reportDate);

        ResultSet salesTrend = stmt.executeQuery();

        sql = "EXEC PROC_GetMonthlySalesTransaction @TransactionDate=?";

        stmt = conn.prepareStatement(sql);
        stmt.setDate(1, reportDate);

        ResultSet salesTransaction = stmt.executeQuery();

        sql = "EXEC PROC_GetMonthlyTopSellingItems @TransactionDate=?";

        stmt = conn.prepareStatement(sql);
        stmt.setDate(1, reportDate);

        ResultSet mostSoldItems = stmt.executeQuery();
        List<ResultSet> resultSets = Arrays.asList(salesTrend, salesTransaction, mostSoldItems);

        for (ResultSet rs: resultSets) {
            try {
                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();
                if (colCount == 0) continue;

                Sheet sheet = workbook.createSheet(sheetNames[resultSets.indexOf(rs)]);

                Row header = sheet.createRow(0);

                for (int j = 1; j <= colCount; j++) {
                    sheet.autoSizeColumn(j - 1);
                    header.createCell(j - 1).setCellValue(meta.getColumnLabel(j));
                }

                int rowIndex = 1;

                while (rs.next()) {
                    Row row = sheet.createRow(rowIndex++);
                    for (int j = 1; j <= colCount; j++) {
                        CellStyle style = workbook.createCellStyle();
                        style.setAlignment(HorizontalAlignment.LEFT);

                        Cell cell = row.createCell(j - 1);
                        cell.setCellStyle(style);

                        switch (meta.getColumnType(j)) {
                            case Types.INTEGER:
                                cell.setCellValue(rs.getInt(j));
                                break;
                            case Types.DOUBLE:
                            case Types.DECIMAL:
                            case Types.NUMERIC:
                                cell.setCellValue(rs.getDouble(j));
                                break;
                            case Types.DATE:
                            case Types.TIMESTAMP:
                                Date date = rs.getDate(j);
                                cell.setCellValue(date != null ? date.toString() : "");
                                break;
                            default:
                                String val = rs.getString(j);
                                cell.setCellValue(val != null ? val : "");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    try {
        JFileChooser directoryChooser = new JFileChooser();
        directoryChooser.setDialogTitle("Select Directory");
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        directoryChooser.setAcceptAllFileFilterUsed(false);

        if (directoryChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File directory = directoryChooser.getSelectedFile();
            try (FileOutputStream fileOutput = new FileOutputStream(directory.getPath() + "/report_" + new SimpleDateFormat("yyyy-MM").format(reportDate) + ".xlsx")) {
                workbook.write(fileOutput);
            }
            workbook.close();

            PopupDialog notif = new PopupDialog("Sales Report Generated");
            notif.setDialogType(DialogType.NOTIFICATION);
            notif.setMessage("Report has been successfully exported!");
            notif.setCloseButtonAction(() -> notif.dispose());
            notif.display();
        }
    } catch (Exception e) {
        PopupDialog error = new PopupDialog("Unable to Export Report");
        error.setDialogType(DialogType.ERROR);
        error.setMessage("Unable to export report\n\n" + e.getMessage());
        error.setCloseButtonAction(() -> error.dispose());
        error.display();
    }
}

}
