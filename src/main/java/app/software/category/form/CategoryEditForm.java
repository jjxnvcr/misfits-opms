package app.software.category.form;

import java.sql.SQLException;

import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.extras.components.FlatTextField;

import app.components.Form;
import app.components.PopupDialog;
import app.db.dao.production.CategoryDao;

import app.db.pojo.production.Category;
import app.software.category.listing.CategoryList;
import app.utils.DialogType;
import app.utils.Palette;
import net.miginfocom.swing.MigLayout;

public class CategoryEditForm extends Form {
    private FlatTextField nameField;

    public CategoryEditForm(CategoryList owner) {
        super();

        setLayout(new MigLayout("align 50% 10%, fillx", "[50%]3%[50%]"));

        setHeader("Edit Category");
        getHeader().setForeground(Palette.GREEN.color());

        Category category = (Category) owner.getActiveCategoryEntry().getPojo();

        FlatLabel nameLabel = createFieldLabel("Category Name");
        nameField = createField(category.getCategoryName());

        add(getHeader(), "span, grow, gapbottom 5%, gapleft 5%");
        add(nameLabel, "wrap, span, grow, gapleft 5%, gapright 5%");
        add(nameField, "wrap, span, grow, gapleft 5%, gapright 5%, gapbottom .5%");

        add(getFeedback(), "span, grow, gapbottom 2.5%, gapleft 5%, gapright 5%");

        add(createCancelButton(() -> {
            owner.getActionPanel().getDefaultButton().doClick();
        }), "grow, gapbottom 5%, gapleft 5%");

        add(createConfirmButton("Edit", () -> {
            
            PopupDialog confirm = new PopupDialog("Add Category");
            confirm.setDialogType(DialogType.CONFIRMATION);
            confirm.setMessage("Are you sure you want to add " + nameField.getText() + " as a category?");
            confirm.setCloseButtonAction(() -> confirm.dispose());
            confirm.setConfirmButtonAction(() -> {
                try {
                    CategoryDao.updateCategory(new Category(category.getCategoryId(), nameField.getText().trim().isBlank() ? category.getCategoryName() : nameField.getText()));

                    confirm.dispose();

                    PopupDialog notif = new PopupDialog("Category Edited");
                    notif.setDialogType(DialogType.NOTIFICATION);
                    notif.setMessage("Category has been successfully edited!");
                    notif.setCloseButtonAction(() -> notif.dispose());
                    notif.display();

                    owner.loadCategory();
                    owner.getOwner().removeActionView();
                } catch (SQLException e) {
                    PopupDialog error = new PopupDialog("Unable to Edit Category");
                    error.setDialogType(DialogType.ERROR);
                    error.setMessage("Something unexpected happened. Unable to edit category.\n\nError: " + e.getMessage());
                    error.setCloseButtonAction(() -> error.dispose());
                    error.display();
                }
            });

            confirm.display();  

        }), "grow, gapbottom 5%, gapright 5%");
    }
}
