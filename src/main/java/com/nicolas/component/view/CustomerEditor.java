package com.nicolas.component.view;

import com.nicolas.component.entity.Customer;
import com.nicolas.component.entity.Order;
import com.nicolas.component.repository.CustomerRepository;
import com.nicolas.component.repository.OrderRepository;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by nico on 23/06/16.
 */
@SpringComponent
@UIScope
public class CustomerEditor extends VerticalLayout implements AbstractEditor {

    private CustomerRepository repository;

    private Customer customer;

    TextField name = new TextField("Nom Complet");
    TextField address = new TextField("Adresse");
    TextField postalCode = new TextField("Code Postal");
    TextField city = new TextField("Ville");
    TextField phone = new TextField("Téléphone");
    TextField email = new TextField("Email");

    Button save = new Button("Save", FontAwesome.SAVE);
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", FontAwesome.TRASH_O);
    CssLayout actions = new CssLayout(save, cancel, delete);

    @Autowired
    public CustomerEditor(CustomerRepository customerRepository) {
        this.repository = customerRepository;

        addComponents(this.name, this.address, this.postalCode, this.city,
                this.phone, this.email,  this.actions);

        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        // wire action buttons to save, delete and reset
        save.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                CustomerEditor.this.repository.save(customer);
            }
        });
        delete.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                CustomerEditor.this.repository.delete(customer);
            }
        });
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                editCustomer(customer);
            }
        });

        setVisible(false);
    }

    public final void editCustomer(Customer customer) {
        final boolean persisted = customer.getId() != 0;
        if (persisted) {
            // Find fresh entity for editing
            this.customer = repository.findOne(customer.getId());
        }
        else {
            this.customer = customer;
        }

        cancel.setVisible(persisted);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        BeanFieldGroup.bindFieldsUnbuffered(customer, this);

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