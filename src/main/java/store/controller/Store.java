package store.controller;

import java.io.IOException;
import store.domain.Inventory;
import store.domain.Receipt;
import store.file.ProductFileReader;
import store.view.InputView;
import store.view.OutputView;

public class Store {
    private final InputView inputView;
    private final OutputView outputView;
    private final Inventory inventory;

    public Store() {
        this.inputView = new InputView();
        this.outputView = new OutputView();
        this.inventory = loadProducts();
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
        return null;
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
