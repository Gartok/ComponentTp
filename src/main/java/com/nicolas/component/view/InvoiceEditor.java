package com.nicolas.component.view;

import com.nicolas.component.entity.Delivery;
import com.nicolas.component.entity.Invoice;
import com.nicolas.component.repository.DeliveryRepository;
import com.nicolas.component.repository.InvoiceRepository;
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
public class InvoiceEditor extends VerticalLayout implements AbstractEditor {

    private InvoiceRepository repository;

    private Invoice invoice;

    DateField invoiceDate = new DateField("Date de création");

    Button save = new Button("Save", FontAwesome.SAVE);
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", FontAwesome.TRASH_O);
    CssLayout actions = new CssLayout(save, cancel, delete);

    @Autowired
    public InvoiceEditor(InvoiceRepository repository) {
        this.repository = repository;

        addComponents(this.invoiceDate,  this.actions);

        // Configure and style components
        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        // wire action buttons to save, delete and reset
        save.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                InvoiceEditor.this.repository.save(invoice);
            }
        });

        delete.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                InvoiceEditor.this.repository.delete(invoice);
            }
        });
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                editInvoice(invoice);
            }
        });
        setVisible(false);
    }

    public final void editInvoice(Invoice invoice) {
        final boolean persisted = invoice.getId() != 0;
        if (persisted) {
            // Find fresh entity for editing
            this.invoice = repository.findOne(invoice.getId());
        }
        else {
            this.invoice = invoice;
        }

        cancel.setVisible(persisted);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        BeanFieldGroup.bindFieldsUnbuffered(invoice, this);

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