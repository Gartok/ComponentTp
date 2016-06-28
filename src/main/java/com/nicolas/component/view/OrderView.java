package com.nicolas.component.view;

import com.nicolas.component.entity.Order;
import com.nicolas.component.repository.OrderRepository;
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
@SpringView(name = OrderView.VIEW_NAME)
public class OrderView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "order";

    @Autowired
    OrderRepository orderRepository;

    Grid grid;

    @Autowired
    private OrderEditor editor;

    private Button addNewBtn;

    public OrderView() {
        this.grid = new Grid();
        this.addNewBtn = new Button("Nouvelle Commande", FontAwesome.PLUS);
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
                editor.editOrder((Order) grid.getSelectedRow());
            }
        });

        addNewBtn.addClickListener(e -> editor.editOrder(new Order()));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listOrders();
        });

        listOrders();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // the view is constructed in the ini   t() method()
    }

    private void listOrders() {
        grid.setContainerDataSource(
                new BeanItemContainer<Order>(Order.class, this.orderRepository.findAll()));
    }
}
