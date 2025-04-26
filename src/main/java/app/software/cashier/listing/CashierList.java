package app.software.cashier.listing;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import app.components.Page;
import app.components.PopupDialog;
import app.components.ScrollList;
import app.components.ScrollView;
import app.db.dao.sales.SaleItemDao;
import app.db.pojo.column.Column;
import app.db.pojo.sales.SaleItem;
import app.software.cashier.CashierPage;
import app.utils.DialogType;
import app.utils.Sort;
import net.miginfocom.swing.MigLayout;

public class CashierList extends Page {
    private Map<Column, Sort> sortMap = new LinkedHashMap<>();
    private String sortCategory;
    private ScrollView scrollView = new ScrollView(3);
    private CashierPage owner;

    public CashierList(CashierPage owner) {
        super(new MigLayout("fillx", "[grow]"), false);

        this.owner = owner;

        add(new CashierSortPanel(this), "wrap");
        add(new ScrollList(scrollView), "height 100%, grow");

        loadSaleItems();
    }
    
    public void loadSaleItems() {
        scrollView.removeAll();

        try {
            for (SaleItem saleItem: stringifySortMap().isBlank() ? SaleItemDao.getAllSaleItems() : SaleItemDao.getAllSaleItemsOrderBy(stringifySortMap())) {
                scrollView.add(new SaleItemEntry(this, saleItem), "grow");
            }
        } catch (SQLException e) {
            PopupDialog error = new PopupDialog("Unable to load Sale Items");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load sale items\n\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        scrollView.revalidate();
        scrollView.repaint();
    }

    public void loadCategoryItems() {
        scrollView.removeAll();

        List<SaleItem> saleItems = new ArrayList<>();

        try {
            if (sortCategory.equals("All")) {
                saleItems = stringifySortMap().isBlank() ? SaleItemDao.getAllSaleItems() : SaleItemDao.getAllSaleItemsOrderBy(stringifySortMap());
            } else {
                saleItems = stringifySortMap().isBlank() ? SaleItemDao.getSaleItemsByCategory(sortCategory) : SaleItemDao.getSaleItemsByCategoryOrderBy(sortCategory, stringifySortMap());
            }
        } catch (Exception e) {
            PopupDialog error = new PopupDialog("Unable to load Sale Items");
            error.setDialogType(DialogType.ERROR);
            error.setMessage("Unable to load sale items\n\n" + e.getMessage());
            error.setCloseButtonAction(() -> error.dispose());
            error.display();
            return;
        }

        for (SaleItem saleItem: saleItems) {
            scrollView.add(new SaleItemEntry(this, saleItem), "grow");
        }

        scrollView.revalidate();
        scrollView.repaint();
    }

    public void addSort(Column column, Sort sort) {
        sortMap.put(column, sort);
    }

    public void removeSort(Column column) {
        sortMap.remove(column);
    }

    public String stringifySortMap() {
        return sortMap.entrySet().stream()
        .map(entry -> entry.getKey().getName() + " " + entry.getValue().getName())
        .collect(Collectors.joining(", "));
    }

    public void setSortCategory(String sortCategory) {
        this.sortCategory = sortCategory;
    }

    public String getSortCategory() {
        return sortCategory;
    }

    public CashierPage getOwner() {
        return owner;
    }
}
