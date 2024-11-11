package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.ArrayList;
import java.util.List;
import store.domain.product.SelectedProduct;
import store.domain.user.UserResponse;

public class InputView {

    private UserResponse readUserResponse(String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = Console.readLine().trim();
            try {
                return UserResponse.fromString(input);
            } catch (IllegalArgumentException e) {
                System.out.println("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
            }
        }
    }

    public List<SelectedProduct> readSelectedProducts() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String input = Console.readLine();
        System.out.println();
        return parseSelectedItems(input);
    }

    public boolean askForMembershipDiscount() {
        UserResponse response = readUserResponse("멤버십 할인을 받으시겠습니까? (Y/N)");
        System.out.println();
        return response == UserResponse.YES;
    }

    public boolean askToAddFreeItems(String itemName, int freeQuantity) {
        String prompt = String.format("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)%n", itemName, freeQuantity);
        UserResponse response = readUserResponse(prompt);
        return response == UserResponse.YES;
    }

    public boolean askToPayFullPrice(String itemName, int quantity) {
        String prompt = String.format("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)%n", itemName, quantity);
        UserResponse response = readUserResponse(prompt);
        return response == UserResponse.YES;
    }

    public boolean askForAdditionalPurchase() {
        UserResponse response = readUserResponse("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        System.out.println();
        return response == UserResponse.YES;
    }

    private List<SelectedProduct> parseSelectedItems(String input) {
        // 수정된 정규식: 한글과 숫자를 포함하도록 변경
        if (!input.matches("(\\[[가-힣]+-\\d+\\])(,\\[[가-힣]+-\\d+\\])*")) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        String[] itemTokens = input.split(",");
        List<SelectedProduct> selectedItems = new ArrayList<>();

        for (String itemToken : itemTokens) {
            SelectedProduct item = parseSingleSelectedItem(itemToken.trim());
            selectedItems.add(item);
        }
        return selectedItems;
    }

    private SelectedProduct parseSingleSelectedItem(String itemToken) {
        if (!itemToken.startsWith("[") || !itemToken.endsWith("]")) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
        String content = itemToken.substring(1, itemToken.length() - 1);
        String[] itemInfo = content.split("-");
        if (itemInfo.length != 2) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
        String itemName = itemInfo[0].trim();
        int quantity = parseQuantity(itemInfo[1].trim());
        if (quantity <= 0) {
            throw new IllegalArgumentException("[ERROR] 잘못된 수량입니다. 다시 입력해 주세요.");
        }
        return new SelectedProduct(itemName, quantity);
    }

    private int parseQuantity(String quantityStr) {
        try {
            return Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 잘못된 수량입니다. 다시 입력해 주세요.");
        }
    }
}