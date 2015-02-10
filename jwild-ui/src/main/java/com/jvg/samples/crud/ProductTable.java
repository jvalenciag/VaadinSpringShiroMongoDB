package com.jvg.samples.crud;

import com.jvg.samples.backend.data.Availability;
import com.jvg.samples.backend.data.Product;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.logging.log4j.LogManager;
import org.tylproject.vaadin.addon.MongoContainer;

//import com.vaadin.data.util.BeanItemContainer;

/**
 * Table of products, handling the visual presentation and filtering of a set of
 * items. This version uses an in-memory data source that is suitable for small
 * data sets.
 */
public class ProductTable extends Table {

    private Container container;
    private ColumnGenerator availabilityGenerator = (source, itemId, columnId) -> {
        Property property = source.getItem(itemId)
                .getItemProperty(columnId);
        if (property != null) {
            Availability availability = (Availability) property.getValue();
            String color = "";
            if (availability == Availability.AVAILABLE) {
                color = "#2dd085";
            } else if (availability == Availability.COMING) {
                color = "#ffc66e";
            } else if (availability == Availability.DISCONTINUED) {
                color = "#f54993";
            }

            String iconCode = "<span class=\"v-icon\" style=\"font-family: "
                    + FontAwesome.CIRCLE.getFontFamily()
                    + ";color:"
                    + color
                    + "\">&#x"
                    + Integer
                            .toHexString(FontAwesome.CIRCLE.getCodepoint())
                    + ";</span>";

            Label label = new Label(iconCode + " " + property.getValue(),
                    ContentMode.HTML);
            label.setSizeUndefined();
            return label;
        }
        return null;
    };

    public ProductTable(Container container) {
        this.container = container;
        setSizeFull();
        addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);

        setContainerDataSource(container);

        setVisibleColumns( "productName", "price", "availability",
                "stockCount", "category");
        setColumnHeaders( "Product", "Price", "Availability", "Stock",
                "Categories");
        setColumnCollapsingAllowed(true);
        setColumnCollapsed("integerProperty", true);
        setColumnCollapsed("bigDecimalProperty", true);

        setColumnWidth("id", 50);
        setColumnAlignment("price", Align.RIGHT);
        setColumnAlignment("stockCount", Align.RIGHT);
        setSelectable(true);
        setImmediate(true);
        // Add an traffic light icon in front of availability
        addGeneratedColumn("availability", availabilityGenerator);
        //addGeneratedColumn("prices",pricesGenerator);
        // Add " €" automatically after price
        setConverter("price", new EuroConverter());
        //setConverter("prices", new EuroConverter());
        // Show categories as a comma separated list
        setConverter("category", new CollectionToStringConverter());
        setRowHeaderMode(RowHeaderMode.INDEX);
    }

    @Override
    protected String formatPropertyValue(Object rowId, Object colId,
            Property property) {
        if (colId.equals("stockCount")) {
            Integer stock = (Integer) property.getValue();
            if (stock.equals(0)) {
                return "-";
            } else {
                return stock.toString();
            }
        }
        return super.formatPropertyValue(rowId, colId, property);
    }

    /**
     * Filter the table based on a search string that is searched for in the
     * product name, availability and category columns.
     * 
     * @param filterString
     *            string to look for
     */
    public void setFilter(String filterString) {
        LogManager.getLogger(this.getClass()).info("FilterString : "+filterString);
        getContainer().removeAllContainerFilters();
        if (filterString.length() > 0) {
            SimpleStringFilter nameFilter = new SimpleStringFilter(
                    "productName", filterString, true, false);
            SimpleStringFilter availabilityFilter = new SimpleStringFilter(
                    "availability", filterString, true, false);
            /*SimpleStringFilter categoryFilter = new SimpleStringFilter(
                    "category", filterString, true, false);*/
            //getContainer().addContainerFilter(nameFilter);
            getContainer().addContainerFilter(new Or(nameFilter, availabilityFilter/*, categoryFilter*/));
        }
    }

    @Override
    public Product getValue() {
        if(super.getValue()==null) return null;
        return (Product) ((BeanItem)container.getItem(super.getValue())).getBean();
    }

    private MongoContainer<Product> getContainer() {
        return (MongoContainer<Product>) super.getContainerDataSource();
    }
}
