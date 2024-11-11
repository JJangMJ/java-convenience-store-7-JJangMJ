package store.controller;

import java.io.IOException;
import java.util.List;
import store.domain.transaction.Cashier;
import store.domain.user.Customer;
import store.domain.product.Inventory;
import store.domain.receipt.Receipt;
import store.domain.product.SelectedProduct;
import store.file.ProductFileReader;
import store.view.InputView;
import store.view.OutputView;

public class Store {
    private final InputView inputView;
    private final OutputView outputView;
    private final Inventory inventory;
    private final Customer customer;
    private final Cashier cashier;

    public Store() {
        this.inputView = new InputView();
        this.outputView = new OutputView();
        this.inventory = loadProducts();
        this.customer = new Customer();
        this.cashier = new Cashier();
    }

    public void run() {
        boolean continueShopping = true;
        while (continueShopping) {
            try {
                printWelcomeGreeting();
                showProducts();
                purchaseProcess();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private Receipt purchaseProcess() {
        while (true) {
            List<SelectedProduct> selectedProducts = inputView.readSelectedProducts();
            customer.purchase(selectedProducts, inventory, cashier);
        }
    }

    private Inventory loadProducts() {
        try {
            ProductFileReader productFileReader = new ProductFileReader();
            return new Inventory(productFileReader);
        } catch (IOException e) {
            System.out.println("[ERROR] 상품 목록을 불러오는 데 실패했습니다.");
            return null;
        }
    }

    private void printWelcomeGreeting() {
        outputView.printWelcomeGreeting();
    }

    private void showProducts() {
        outputView.printAllProducts(inventory.getProducts());
    }
}
