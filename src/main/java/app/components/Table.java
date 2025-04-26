package app.components;

import java.awt.Color;
import java.awt.Component;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import app.utils.ClientProperty;
import app.utils.Palette;

class CustomTableRenderer extends DefaultTableCellRenderer {
    private final Color cellForeground;

    public CustomTableRenderer(Color cellForeground) {
        super();
        this.cellForeground = cellForeground;

        setOpaque(false);
        setHorizontalAlignment(CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setBorder(noFocusBorder);
        setForeground(cellForeground);
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}

public class Table extends JTable {
    public Table(Vector<Vector<String>> data, Vector<String> column) {
        super(data, column);
        
        JTableHeader tableHeader = getTableHeader();
        TableColumnModel tableColumnModel = getColumnModel();
        CustomTableRenderer cellRenderer = new CustomTableRenderer(Palette.SUBTEXT0.color());

        setRowHeight(35);
        setDragEnabled(false);
        setOpaque(false);
        setShowHorizontalLines(true);
        setShowVerticalLines(false);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setDefaultRenderer(Object.class, cellRenderer);
        setFillsViewportHeight(true);
        setEnabled(false);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBorder(null);
        headerRenderer.setOpaque(false);
        headerRenderer.setForeground(Palette.TEXT.color());
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setBackground(Palette.TRANSPARENT.color());
        getTableHeader().setForeground(Palette.TEXT.color());
        getTableHeader().setDefaultRenderer(cellRenderer);

        ClientProperty.setProperty(tableHeader, "cellMargins", "5,5,5,5");
        ClientProperty.setProperty(tableHeader, "height", "34");
        ClientProperty.setProperty(tableHeader, "separatorColor", Palette.SURFACE1.varString());
        ClientProperty.setProperty(tableHeader, "font", "Inter, bold");

        ClientProperty.setProperty(this, "cellMargins", "3,3,3,3");
        ClientProperty.setProperty(this, "rowHeight", "28");
        ClientProperty.setProperty(this, "gridColor", Palette.SURFACE1.varString());

        for (int x = 0; x < tableColumnModel.getColumnCount(); x++) {
            tableColumnModel.getColumn(x).setPreferredWidth(120);
        }
    }
}
