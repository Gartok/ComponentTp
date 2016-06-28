package com.nicolas.component.view;

import com.nicolas.component.entity.Delivery;
import com.nicolas.component.entity.Invoice;
import com.nicolas.component.entity.Order;
import com.nicolas.component.repository.DeliveryRepository;
import com.nicolas.component.repository.InvoiceRepository;
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
public class DeliveryEditor extends VerticalLayout implements AbstractEditor {

    private DeliveryRepository repository;

    private Delivery delivery;

    DateField deliveryDate = new DateField("Date de création");

    private ComboBox orderSelect;
    private ComboBox invoiceSelect;

    Button save = new Button("Save", FontAwesome.SAVE);
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", FontAwesome.TRASH_O);
    CssLayout actions = new CssLayout(save, cancel, delete);

    @Autowired
    public DeliveryEditor(DeliveryRepository repository, InvoiceRepository invoiceRepository,
                          OrderRepository orderRepository) {
        this.repository = repository;

        this.orderSelect = new ComboBox("Commande", orderRepository.findAll());
        this.invoiceSelect = new ComboBox("Facture", invoiceRepository.findAll());

        addComponents(this.orderSelect, this.invoiceSelect, this.deliveryDate,  this.actions);

        // Configure and style components
        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        // wire action buttons to save, delete and reset
        save.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                delivery.setInvoice((Invoice) DeliveryEditor.this.invoiceSelect.getValue());
                delivery.setOrder((Order) DeliveryEditor.this.orderSelect.getValue());

                DeliveryEditor.this.repository.save(delivery);
            }
        });

        delete.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                DeliveryEditor.this.repository.delete(delivery);
            }
        });
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                editDelivery(delivery);
            }
        });
        setVisible(false);
    }

    public final void editDelivery(Delivery delivery) {
        final boolean persisted = delivery.getId() != 0;
        if (persisted) {
            // Find fresh entity for editing
            this.delivery = repository.findOne(delivery.getId());
        }
        else {
            this.delivery = delivery;
        }

        cancel.setVisible(persisted);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        BeanFieldGroup.bindFieldsUnbuffered(delivery, this);

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