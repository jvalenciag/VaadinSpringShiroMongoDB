package com.jvg.samples.crud;

import com.jvg.samples.backend.data.Availability;
import com.jvg.samples.backend.data.Product;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.MethodProperty;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToEnumConverter;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderer.HtmlRenderer;
import org.tylproject.vaadin.addon.MongoContainer;

import java.util.Locale;

/**
 * Grid of products, handling the visual presentation and filtering of a set of
 * items. This version uses an in-memory data source that is suitable for small
 * data sets.
 */
public class ProductGrid extends Grid {

    private StringToEnumConverter availabilityConverter = new StringToEnumConverter() {
        @Override
        public String convertToPresentation(Enum availability,
                java.lang.Class<? extends String> targetType, Locale locale)
                throws Converter.ConversionException {
            String text = super.convertToPresentation(availability, targetType,
                    locale);

            String color = "";
            if (availability == Availability.AVAILABLE) {
                color = "#2dd085";
            } else if (availability == Availability.COMING) {
                color = "#ffc66e";
            } else if (availability == Availability.DISCONTINUED) {
                color = "#f54993";
            }

            String iconCode = "<span class=\"v-icon\" style=\"font-family: "
                    + FontAwesome.CIRCLE.getFontFamily() + ";color:" + color
                    + "\">&#x"
                    + Integer.toHexString(FontAwesome.CIRCLE.getCodepoint())
                    + ";</span>";

            return iconCode + " " + text;
        };
    };

    public ProductGrid(Container.Indexed container) {
        setSizeFull();

        setSelectionMode(SelectionMode.SINGLE);
        GeneratedPropertyContainer gcont = new GeneratedPropertyContainer(container);
        gcont.removeContainerProperty("id");
        setContainerDataSource(gcont);
        setColumnOrder(/*"id",*/ "productName", "price", "availability",
                "stockCount", "category");
        this.
        /*getColumn("id").setConverter(new Converter<String, ObjectId >() {

            @Override
            public ObjectId convertToModel(String value, Class<? extends ObjectId> targetType, Locale locale) throws ConversionException {
                return new ObjectId(value);
            }

            @Override
            public String convertToPresentation(ObjectId value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                return value.toString();
            }

            @Override
            public Class<ObjectId> getModelType() {
                return ObjectId.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
        });*/
        //getColumn("id").setMinimumWidth(10.0);
        //getColumn("id").setMaximumWidth(24.0);

        // Show empty stock as "-"
        getColumn("stockCount").setConverter(new StringToIntegerConverter() {
            @Override
            public String convertToPresentation(Integer value,
                    java.lang.Class<? extends String> targetType, Locale locale)
                    throws Converter.ConversionException {
                if (value == 0) {
                    return "-";
                }

                return super.convertToPresentation(value, targetType, locale);
            }
        });

        // Add an traffic light icon in front of availability
        getColumn("availability").setConverter(availabilityConverter)
                .setRenderer(new HtmlRenderer());

        // Add " €" automatically after price
        getColumn("price").setConverter(new EuroConverter());

        // Show categories as a comma separated list
        getColumn("category").setConverter(new CollectionToStringConverter());

        // Align columns using a style generator and theme rule until #15438
        setCellStyleGenerator(new CellStyleGenerator() {

            @Override
            public String getStyle(CellReference cellReference) {
                if (cellReference.getPropertyId().equals("price")
                        || cellReference.getPropertyId().equals("stockCount")) {
                    return "align-right";
                }
                return null;
            }
        });
    }

    /**
     * Filter the grid based on a search string that is searched for in the
     * product name, availability and category columns.
     *
     * @param filterString
     *            string to look for
     */
    public void setFilter(String filterString) {
        getContainer().removeAllContainerFilters();
        if (filterString.length() > 0) {
            SimpleStringFilter nameFilter = new SimpleStringFilter(
                    "productName", filterString, true, false);
            SimpleStringFilter availabilityFilter = new SimpleStringFilter(
                    "availability", filterString, true, false);
            SimpleStringFilter categoryFilter = new SimpleStringFilter(
                    "category", filterString, true, false);
            getContainer().addContainerFilter(
                    new Or(nameFilter, availabilityFilter, categoryFilter));
        }

    }

    public MongoContainer<Product> getContainer() {
        return (MongoContainer<Product>)((GeneratedPropertyContainer)super.getContainerDataSource()).getWrappedContainer();
    }

    @Override
    public Product getSelectedRow() throws IllegalStateException {
        if(super.getSelectedRow()==null) return null;
        return (Product) getContainer().getItem(super.getSelectedRow()).getBean();
    }

    /*public void setProducts(Collection<Product> products) {
        getContainer().removeAllItems();
        getContainer().addAll(products);
    }*/

    public void refresh(Product product) {
        // We avoid updating the whole table through the backend here so we can
        // get a partial update for the grid
        BeanItem<Product> item = getContainer().getItem(product.getId());
        if (item != null) {
            // Updated product
            MethodProperty p = (MethodProperty) item.getItemProperty("id");
            p.fireValueChange();
        } else {
            // New product
            getContainer().addEntity(product);
        }
    }

    public void remove(Product product) {
        getContainer().removeItem(product);
    }
}
