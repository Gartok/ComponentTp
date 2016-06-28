package com.nicolas.component.view;

import com.nicolas.component.entity.Order;
import com.nicolas.component.entity.OrderDetail;
import com.nicolas.component.entity.Product;
import com.nicolas.component.repository.CustomerRepository;
import com.nicolas.component.repository.OrderDetailRepository;
import com.nicolas.component.repository.OrderRepository;
import com.nicolas.component.repository.ProductRepository;
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
public class OrderDetailEditor extends VerticalLayout implements AbstractEditor {

    private OrderDetailRepository repository;

    private OrderDetail orderDetail;

    DateField date = new DateField("Date de création");

    private ComboBox orderSelect;

    private ComboBox productSelect;
    private TextField quantity =  new TextField("Ville");

    Button save = new Button("Save", FontAwesome.SAVE);
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", FontAwesome.TRASH_O);
    CssLayout actions = new CssLayout(save, cancel, delete);

    @Autowired
    public OrderDetailEditor(OrderDetailRepository repository, ProductRepository productRepository,
                             OrderRepository orderRepository) {
        this.repository = repository;
        this.quantity.setConverter(Integer.class);

        this.productSelect = new ComboBox("Product", productRepository.findAll());
        this.orderSelect = new ComboBox("Commande", orderRepository.findAll());

        addComponents(this.productSelect, this.orderSelect, this.quantity, this.date, this.actions);

        // Configure and style components
        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        // wire action buttons to save, delete and reset
        save.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                orderDetail.setProduct((Product) OrderDetailEditor.this.productSelect.getValue());
                orderDetail.setOrder((Order) OrderDetailEditor.this.orderSelect.getValue());

                OrderDetailEditor.this.repository.save(orderDetail);
            }
        });

        delete.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                OrderDetailEditor.this.repository.delete(orderDetail);
            }
        });
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                editOrderDetail(orderDetail);
            }
        });
        setVisible(false);
    }

    public final void editOrderDetail(OrderDetail orderDetail) {
        final boolean persisted = orderDetail.getId() != 0;
        if (persisted) {
            // Find fresh entity for editing
            this.orderDetail = repository.findOne(orderDetail.getId());
        }
        else {
            this.orderDetail = orderDetail;
        }

        cancel.setVisible(persisted);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        BeanFieldGroup.bindFieldsUnbuffered(orderDetail, this);

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