package com.jvg.samples.crud;

import com.jvg.MyUI;
import com.jvg.samples.backend.DataService;
import com.jvg.samples.backend.data.Product;

import com.vaadin.server.Page;
import org.bson.types.ObjectId;

/**
 * This class provides an interface for the logical operations between the CRUD
 * view, its parts like the product editor form and the data source, including
 * fetching and saving products.
 *
 * Having this separate from the view makes it easier to test various parts of
 * the system separately, and to e.g. provide alternative views for the same
 * data.
 */
public class SampleCrudLogic {

    private SampleCrudView view;
    private DataService dataService;

    public SampleCrudLogic(SampleCrudView simpleCrudView, DataService ds) {
        view = simpleCrudView;
        dataService = ds;
    }

    public void init() {
        editProduct(null);
        // Hide and disable if not admin
        //TODO move permissions strings to constants
        if (!MyUI.get().getAccessControl().isUserPermitted("product:create")) {
            view.setNewProductEnabled(false);
        }

        view.showProducts(dataService.getAllProducts());
    }

    public void cancelProduct() {
        setFragmentParameter("");
        view.clearSelection();
        view.editProduct(null);
    }

    /**
     * Update the fragment without causing navigator to change view
     */
    private void setFragmentParameter(String productId) {
        String fragmentParameter;
        if (productId == null || productId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = productId;
        }

        Page page = MyUI.get().getPage();
        page.setUriFragment("!" + SampleCrudView.VIEW_NAME + "/"
                + fragmentParameter, false);
    }

    public void enter(String productId) {
        if (productId != null && !productId.isEmpty()) {
            if (productId.equals("new")) {
                newProduct();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    ObjectId pid = new ObjectId(productId);
                    Product product = findProduct(pid);
                    view.selectRow(product);
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    private Product findProduct(ObjectId productId) {
        return dataService.getProductById(productId);
    }

    public void saveProduct(Product product) {
        view.showSaveNotification(product.getProductName() + " ("
                + product.getId() + ") updated");
        view.clearSelection();
        view.editProduct(null);
        view.refreshProduct(product);
        setFragmentParameter("");
    }

    public void deleteProduct(Product product) {
        dataService.deleteProduct(product.getId());
        view.showSaveNotification(product.getProductName() + " ("
                + product.getId() + ") removed");

        view.clearSelection();
        view.editProduct(null);
        view.removeProduct(product);
        setFragmentParameter("");
    }

    public void editProduct(Product product) {
        if (product == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(product.getId() + "");
        }
        view.editProduct(product);
    }

    public void newProduct() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editProduct(new Product());
    }

    public void rowSelected(Product product) {
        if (MyUI.get().getAccessControl().isUserPermitted("product:edit")) {
            view.editProduct(product);
        }
    }
}
