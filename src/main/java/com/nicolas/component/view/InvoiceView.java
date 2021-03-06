package com.nicolas.component.view;

import com.nicolas.component.entity.Delivery;
import com.nicolas.component.entity.Invoice;
import com.nicolas.component.repository.DeliveryRepository;
import com.nicolas.component.repository.InvoiceRepository;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by nico on 23/06/16.
 */
@SpringView(name = InvoiceView.VIEW_NAME)
public class InvoiceView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "invoice";

    @Autowired
    InvoiceRepository invoiceRepository;

    Grid grid;

    @Autowired
    private InvoiceEditor editor;

    private Button addNewBtn;

    public InvoiceView() {
        this.grid = new Grid();
        this.addNewBtn = new Button("Nouvelle Facture", FontAwesome.PLUS);
    }

    @PostConstruct
    protected void init() {
        HorizontalLayout actions = new HorizontalLayout(addNewBtn);
        VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);
        addComponent(mainLayout);

        actions.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        grid.setHeight(300, Unit.PIXELS);
        grid.setWidth(100, Unit.PERCENTAGE);

        grid.addSelectionListener(e -> {
            if (e.getSelected().isEmpty()) {
                editor.setVisible(false);
            }
            else {
                editor.editInvoice((Invoice) grid.getSelectedRow());
            }
        });

        addNewBtn.addClickListener(e -> editor.editInvoice(new Invoice()));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listInvoices();
        });

        listInvoices();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // the view is constructed in the ini   t() method()
    }

    private void listInvoices() {
        grid.setContainerDataSource(
                new BeanItemContainer<Invoice>(Invoice.class, this.invoiceRepository.findAll()));
    }
}
