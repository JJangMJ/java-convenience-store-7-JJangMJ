package store.domain;

import java.io.IOException;
import java.util.List;
import store.file.ProductFileReader;

public class Inventory {
    private final List<Product> products;

    public Inventory(ProductFileReader reader) throws IOException {
        this.products = reader.readAllItems();
    }
}
