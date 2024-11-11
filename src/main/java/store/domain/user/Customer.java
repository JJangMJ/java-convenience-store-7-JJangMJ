package store.domain.user;

import java.util.List;
import store.domain.product.Product;
import store.domain.transaction.Cashier;
import store.domain.product.Inventory;
import store.domain.product.SelectedProduct;
import store.domain.receipt.Receipt;

public class Customer {
    private Receipt receipt;

    public Receipt purchase(List<SelectedProduct> selectedProducts, Inventory inventory, Cashier cashier) {
        List<Product> matchedProducts = inventory.matchItems(selectedProducts);
    }
}
