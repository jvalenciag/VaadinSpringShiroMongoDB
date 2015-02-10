package com.jvg.samples.crud;

import java.util.Collection;

import com.jvg.samples.ResetButtonForTextField;
import com.jvg.samples.backend.DataService;
import com.jvg.samples.backend.data.Product;

import com.vaadin.data.Container;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid.SelectionModel;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.tylproject.vaadin.addon.MongoContainer;
import ru.xpoft.vaadin.VaadinView;

import javax.annotation.PostConstruct;

/**
 * A view for performing create-read-update-delete operations on products.
 *
 * See also {@link SampleCrudLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
@Component
@Scope("prototype")
@VaadinView(SampleCrudView.VIEW_NAME)
public class SampleCrudView extends CssLayout implements View {

    public static final String VIEW_NAME = "Inventory";
    private ProductGrid grid;
    private ProductForm form;

    @Autowired
    DataService dataService;

    private SampleCrudLogic viewLogic;
    private Button newProduct;

    @PostConstruct
    public void Init() {
        setSizeFull();
        addStyleName("crud-view");
        HorizontalLayout topLayout = createTopBar();

        viewLogic = new SampleCrudLogic(this,dataService);

        grid = new ProductGrid((Container.Indexed)dataService.getProductsContainer());
        grid.addSelectionListener(event -> viewLogic.rowSelected(grid.getSelectedRow()));

        form = new ProductForm(viewLogic,dataService);
        form.setCategories(dataService.getAllCategories());

        VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.addComponent(topLayout);
        barAndGridLayout.addComponent(grid);
        barAndGridLayout.setMargin(true);
        barAndGridLayout.setSpacing(true);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.setExpandRatio(grid, 1);
        barAndGridLayout.setStyleName("crud-main-layout");

        addComponent(barAndGridLayout);
        addComponent(form);

        viewLogic.init();
    }

    public HorizontalLayout createTopBar() {
        TextField filter = new TextField();
        filter.setStyleName("filter-textfield");
        filter.setInputPrompt("Filter");
        ResetButtonForTextField.extend(filter);
        filter.setImmediate(true);
        filter.addTextChangeListener(event -> grid.setFilter(event.getText()));

        newProduct = new Button("New product");
        newProduct.addStyleName(ValoTheme.BUTTON_PRIMARY);
        newProduct.setIcon(FontAwesome.PLUS_CIRCLE);
        newProduct.addClickListener(event -> viewLogic.newProduct());

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setSpacing(true);
        topLayout.setWidth("100%");
        topLayout.addComponent(filter);
        topLayout.addComponent(newProduct);
        topLayout.setComponentAlignment(filter, Alignment.MIDDLE_LEFT);
        topLayout.setExpandRatio(filter, 1);
        topLayout.setStyleName("top-bar");
        return topLayout;
    }

    @Override
    public void enter(ViewChangeEvent event) {
        viewLogic.enter(event.getParameters());
    }

    public void showError(String msg) {
        Notification.show(msg, Type.ERROR_MESSAGE);
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg, Type.TRAY_NOTIFICATION);
    }

    public void setNewProductEnabled(boolean enabled) {
        newProduct.setEnabled(enabled);
    }

    public void clearSelection() {
        grid.getSelectionModel().reset();
    }

    public void selectRow(Product row) {
        ((SelectionModel.Single) grid.getSelectionModel()).select(row);
    }

    public Product getSelectedRow() {
        return grid.getSelectedRow();
    }

    public void editProduct(Product product) {
        if (product != null) {
            form.addStyleName("visible");
            form.setEnabled(true);
        } else {
            form.removeStyleName("visible");
            form.setEnabled(false);
        }
        form.editProduct(product);
    }

    public void showProducts(Collection<Product> products) {

        MongoContainer<Product> mongoContainer = (MongoContainer<Product>)grid.getContainerDataSource();
        mongoContainer.refresh();
        //table.refreshRowCache();
    }

    public void refreshProduct(Product product) {
        grid.refresh(product);
        grid.scrollTo(product);
    }

    public void removeProduct(Product product) {
        grid.remove(product);
    }

}
