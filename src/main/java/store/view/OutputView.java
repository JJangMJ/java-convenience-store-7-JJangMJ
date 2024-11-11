package store.view;

import java.util.List;
import store.domain.product.Product;

public class OutputView {

    public void printWelcomeGreeting() {
        System.out.println("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n");
    }

    public void printAllProducts(List<Product> products) {
        for (Product product : products) {
            System.out.println(product.toString());
        }
        System.out.println();
    }
}
