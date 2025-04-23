package app.software.category.form;

import java.sql.SQLException;

import app.components.PopupDialog;
import app.db.dao.production.CategoryDao;
import app.db.pojo.production.Category;
import app.software.category.listing.CategoryEntry;
import app.software.category.listing.CategoryList;

import app.utils.DialogType;

public class CategoryDeleteConfirmation extends PopupDialog {
    public CategoryDeleteConfirmation(CategoryEntry categoryEntry) {
        super("Delete Category");
        setDialogType(DialogType.CONFIRMATION);
        setMessage("Would you like to delete this category?");
        setCloseButtonAction(() -> dispose());
        setConfirmButtonAction(() -> {
            dispose();
            try {
                CategoryDao.deleteCategory(((Category) categoryEntry.getPojo()).getCategoryId());

                PopupDialog notif = new PopupDialog("Category Deleted");
                notif.setDialogType(DialogType.NOTIFICATION);
                notif.setMessage("Category has been successfully deleted!");
                notif.setCloseButtonAction(() -> notif.dispose());
                notif.display();

                ((CategoryList) categoryEntry.getOwner()).loadCategory();

            } catch (SQLException e) {
                PopupDialog error = new PopupDialog("Unable to Delete Category");
                error.setDialogType(DialogType.ERROR);
                error.setMessage("Something unexpected happened. Unable to delete category.\n\n Error: " + e.getMessage());
                error.setCloseButtonAction(() -> error.dispose());
                error.display();
            }
        });

        display();
    }
}
