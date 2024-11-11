package store.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.domain.product.Inventory;
import store.domain.product.SelectedProduct;
import store.domain.transaction.Cashier;
import store.domain.user.Customer;
import store.domain.discount.Promotion;
import store.domain.discount.Promotions;
import store.domain.receipt.Receipt;
import store.file.ProductFileReader;
import store.file.PromotionFileReader;
import store.view.InputView;
import store.view.OutputView;

public class Store {
    private final InputView inputView;
    private final OutputView outputView;
    private final Inventory inventory;
    private final Promotions promotions;
    private final Cashier cashier;
    private final Customer customer;

    public Store() {
        this.inputView = new InputView();
        this.outputView = new OutputView();
        this.inventory = loadItems();
        this.promotions = loadPromotions();
        this.cashier = new Cashier(promotions, inventory.getAllProducts());
        this.customer = new Customer();
    }

    private Promotions loadPromotions() {
        try {
            PromotionFileReader promotionFileReader = new PromotionFileReader();
            List<Promotion> promotionList = promotionFileReader.readAllPromotions();
            return new Promotions(promotionList);
        } catch (IOException e) {
            System.out.println("[ERROR] 프로모션 목록을 불러오는 데 실패했습니다.");
            System.exit(1);
            return null; // Unreachable but required by compiler
        }
    }

    private Inventory loadItems() {
        try {
            ProductFileReader itemFileReader = new ProductFileReader();
            return new Inventory(itemFileReader);
        } catch (IOException e) {
            System.out.println("[ERROR] 상품 목록을 불러오는 데 실패했습니다.");
            System.exit(1);
            return null; // Unreachable but required by compiler
        }
    }

    public void run() {
        boolean continueShopping = true;
        while (continueShopping) {
            try {
                printWelcomeGreeting();
                showItems();
                Receipt receipt = purchaseProcess();
                printReceipt(receipt);
                continueShopping = askForAdditionalPurchase();
            } catch (IllegalArgumentException | IllegalStateException e) {
                handleException(e);
            }
        }
    }

    private Receipt purchaseProcess() {
        while (true) {
            try {
                List<SelectedProduct> selectedItems = inputView.readSelectedProducts();
                Receipt receipt = customer.purchase(selectedItems, inventory, cashier);
                return receipt;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void handleException(Exception e) {
        String message = e.getMessage();
        if (message.startsWith("[ERROR] 현재")) {
            // 메시지에서 상품명과 수량 추출
            // 예시: [ERROR] 현재 콜라 4개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
            String[] parts = message.split(" ");
            if (parts.length >= 5) {
                String itemName = parts[2];
                String quantityStr = parts[3].replace("개는", "").replace("프로모션", "")
                        .replace("할인이", "").replace("적용되지", "").replace("않습니다.", "");
                try {
                    int quantity = Integer.parseInt(quantityStr);
                    boolean payFullPrice = inputView.askToPayFullPrice(itemName, quantity);
                    if (payFullPrice) {
                        // 프로모션이 적용되지 않는 수량을 정가로 구매
                        System.out.println("프로모션 할인이 적용되지 않는 수량은 정가로 결제됩니다.");
                        // 정가로 구매할 수량을 별도로 처리하기 위해 SelectedItem을 생성
                        SelectedProduct fullPriceItem = new SelectedProduct(itemName, quantity);
                        // 기존 구매 리스트에 추가
                        List<SelectedProduct> newSelectedItems = new ArrayList<>();
                        newSelectedItems.add(fullPriceItem);
                        // 재구매 프로세스 호출
                        try {
                            Receipt fullPriceReceipt = customer.purchase(newSelectedItems, inventory, cashier);
                            // 기존 영수증과 합산
                            // 여기서는 간단히 별도의 영수증으로 출력
                            outputView.printReceipt(fullPriceReceipt);
                        } catch (IllegalArgumentException | IllegalStateException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } else {
                        // 프로모션 할인이 적용되지 않는 수량을 제외하고 결제
                        System.out.println("프로모션 할인이 적용되지 않는 수량은 제외하고 결제됩니다.");
                        // 프로모션 할인이 적용되지 않는 수량을 제외하기 위해 재입력 유도
                        // 현재 구조에서는 루프가 계속되어 재입력을 유도함
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("[ERROR] 잘못된 수량 정보입니다.");
                }
            }
        } else {
            // 기타 예외 처리
            System.out.println(e.getMessage());
        }
    }

    private void printReceipt(Receipt receipt) {
        outputView.printReceipt(receipt);
    }

    private void printWelcomeGreeting() {
        outputView.printWelcomeGreeting();
    }

    private void showItems() {
        outputView.printAllItems(inventory.getAllProducts());
    }

    private boolean askForAdditionalPurchase() {
        return inputView.askForAdditionalPurchase();
    }
}