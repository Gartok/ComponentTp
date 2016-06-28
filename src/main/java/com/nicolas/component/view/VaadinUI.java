package com.nicolas.component.view;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by nico on 23/06/16.
 */
@SpringUI
@Theme("valo")
public class VaadinUI extends UI implements View{

    @Autowired
    SpringViewProvider viewProvider;

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.setSpacing(true);

        final CssLayout navigationBar = new CssLayout();
        navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        navigationBar.addComponent(createNavigationButton("Accueil",
                DefaultView.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("Client",
                CustomerView.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("Commande",
                OrderView.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("Détail Commande",
                OrderDetailView.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("Produit",
                ProductView.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("Facture",
                InvoiceView.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("Livraison",
                DeliveryView.VIEW_NAME));
        root.addComponent(navigationBar);

        final Panel viewContainer = new Panel();
        viewContainer.setSizeFull();
        root.addComponent(viewContainer);
        root.setExpandRatio(viewContainer, 1.0f);

        Navigator navigator = new Navigator(this, viewContainer);
        navigator.addProvider(viewProvider);
        setContent(root);

    }

    private Button createNavigationButton(String caption, final String viewName) {
        Button button = new Button(caption);
        button.addStyleName(ValoTheme.BUTTON_SMALL);
        // If you didn't choose Java 8 when creating the project, convert this to an anonymous listener class
        button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
        return button;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
