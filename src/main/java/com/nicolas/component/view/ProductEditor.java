package com.nicolas.component.view;

import com.nicolas.component.entity.Customer;
import com.nicolas.component.entity.Product;
import com.nicolas.component.repository.CustomerRepository;
import com.nicolas.component.repository.ProductRepository;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by nico on 23/06/16.
 */
@SpringComponent
@UIScope
public class ProductEditor extends VerticalLayout implements AbstractEditor {

    @Autowired
    private ProductRepository repository;

    private Product product;

    TextField name = new TextField("Nom");
    TextField description = new TextField("Description");
    TextField price = new TextField("Prix");

    Button save = new Button("Save", FontAwesome.SAVE);
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", FontAwesome.TRASH_O);
    CssLayout actions = new CssLayout(save, cancel, delete);

    public ProductEditor() {
        addComponents(this.name, this.description, this.price,  this.actions);

        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        // wire action buttons to save, delete and reset
        save.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                ProductEditor.this.repository.save(product);
            }
        });

        delete.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                ProductEditor.this.repository.delete(product);
            }
        });
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                editProduct(product);
            }
        });
        setVisible(false);
    }

    public final void editProduct(Product product) {
        final boolean persisted = product.getId() != 0;
        if (persisted) {
            // Find fresh entity for editing
            this.product = repository.findOne(product.getId());
        }
        else {
            this.product = product;
        }

        cancel.setVisible(persisted);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        BeanFieldGroup.bindFieldsUnbuffered(product, this);

        setVisible(true);

        // A hack to ensure the whole form is visible
        save.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        save.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }
}