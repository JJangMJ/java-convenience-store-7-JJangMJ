package store.domain.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import store.file.ProductFileReader;

public class Inventory {
    private final List<Product> products;

    public Inventory(ProductFileReader reader) throws IOException {
        this.products = reader.readAllProducts();
    }

    public List<Product> matchItems(List<SelectedProduct> selectedProducts) {
        List<Product> matchedItems = new ArrayList<>();
        for (SelectedProduct selectedProduct : selectedProducts) {
            for (Product product : products) {
                if (product.getName().equals(selectedProduct.getItemName())) {
                    matchedItems.add(product);
                }
            }
        }
        return matchedItems;
    }

    public List<Product> getAllItems() {
        return Collections.unmodifiableList(products);
    }
}