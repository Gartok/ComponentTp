package com.nicolas.component.view;

import com.nicolas.component.entity.Customer;
import com.nicolas.component.entity.Order;
import com.nicolas.component.repository.CustomerRepository;
import com.nicolas.component.repository.OrderRepository;
import com.vaadin.annotations.Theme;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nico on 23/06/16.
 */
@SpringComponent
@UIScope
public class OrderEditor extends VerticalLayout implements AbstractEditor {

    private OrderRepository orderRepository;
    private CustomerRepository customerRepository;

    private Order order;

    DateField date = new DateField("Date de création");

    private ComboBox userSelect;

    Button save = new Button("Save", FontAwesome.SAVE);
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", FontAwesome.TRASH_O);
    CssLayout actions = new CssLayout(save, cancel, delete);

    @Autowired
    public OrderEditor(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;

        this.userSelect = new ComboBox("Utilisateur", this.customerRepository.findAll());

        addComponents(this.date, this.userSelect, this.actions);

        // Configure and style components
        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        // wire action buttons to save, delete and reset
        save.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                order.setCustomer((Customer) OrderEditor.this.userSelect.getValue());
                order.setDateCreated(date.getValue());

                OrderEditor.this.orderRepository.save(order);
            }
        });

        delete.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                OrderEditor.this.orderRepository.delete(order);
            }
        });
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                editOrder(order);
            }
        });
        setVisible(false);
    }

    public final void editOrder(Order order) {
        final boolean persisted = order.getId() != 0;
        if (persisted) {
            // Find fresh entity for editing
            this.order = orderRepository.findOne(order.getId());
        }
        else {
            this.order = order;
        }

        cancel.setVisible(persisted);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        BeanFieldGroup.bindFieldsUnbuffered(order, this);

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