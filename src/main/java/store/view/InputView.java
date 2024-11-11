package store.view;


import camp.nextstep.edu.missionutils.Console;
import java.util.ArrayList;
import java.util.List;
import store.domain.SelectedProduct;

public class InputView {

    public List<SelectedProduct> readSelectedProducts() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String input = Console.readLine();
        System.out.println();
        return parseSelectedProducts(input);
    }

    private List<SelectedProduct> parseSelectedProducts(String input) {
        if (!input.matches("(\\[[가-힣]+-\\d+\\])(,\\[[가-힣]+-\\d+\\])*")) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
        String[] productTokens = input.split(",");
        List<SelectedProduct> selectedProducts = new ArrayList<>();
        for (String productToken : productTokens) {
            SelectedProduct item = parseSingleSelectedItem(productToken.trim());
            selectedProducts.add(item);
        }
        return selectedProducts;
    }

    private SelectedProduct parseSingleSelectedItem(String productToken) {
        if (!productToken.startsWith("[") || !productToken.endsWith("]")) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
        String content = productToken.substring(1, productToken.length() - 1);
        String[] productInfo = content.split("-");
        if (productInfo.length != 2) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
        String productName = productInfo[0].trim();
        int productQuantity = parseQuantity(productInfo[1].trim());
        if (productQuantity < 0) {
            throw new IllegalArgumentException("[ERROR] 잘못된 수량입니다. 다시 입력해 주세요.");
        }
        return new SelectedProduct(productName, productQuantity);
    }

    private int parseQuantity(String quantityStr) {
        try {
            return Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 잘못된 수량입니다. 다시 입력해 주세요.");
        }
    }
}
