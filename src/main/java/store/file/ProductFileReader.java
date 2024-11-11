package store.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import store.domain.Product;

public class ProductFileReader {
    private static final String PRODUCT_FILE_PATH = "src/main/resources/products.md";

    public List<Product> readAllProducts() throws IOException {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(PRODUCT_FILE_PATH))) {
            br.readLine(); // 헤더 스킵
            int lineNumber = 2; // 헤더를 제외한 첫 번째 라인의 번호

            String line;
            while ((line = br.readLine()) != null) {
                products.add(parseProductLine(line, lineNumber));
                lineNumber++;
            }
        }
        return products;
    }

    private Product parseProductLine(String line, int lineNumber) {
        String[] tokens = line.split(",");
        validateTokenLength(tokens, lineNumber);

        String name = tokens[0].trim();
        int price = parsePrice(tokens[1].trim(), lineNumber);
        int quantity = parseQuantity(tokens[2].trim(), lineNumber);
        String promotionName = parsePromotion(tokens);

        return new Product(name, price, quantity, promotionName);
    }

    private void validateTokenLength(String[] tokens, int lineNumber) {
        if (tokens.length < 3 || tokens.length > 4) {
            throw new IllegalArgumentException("[ERROR] " + lineNumber + "번째 라인의 형식이 잘못되었습니다.");
        }
    }

    private int parsePrice(String priceStr, int lineNumber) {
        try {
            return Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] " + lineNumber + "번째 라인의 가격이 올바르지 않습니다: " + priceStr);
        }
    }

    private int parseQuantity(String quantityStr, int lineNumber) {
        if (quantityStr.equals("재고없음")) {
            return 0;
        }
        try {
            return Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] " + lineNumber + "번째 라인의 수량이 올바르지 않습니다: " + quantityStr);
        }
    }

    private String parsePromotion(String[] tokens) {
        if (tokens.length == 4 && !tokens[3].trim().equalsIgnoreCase("null")) {
            return tokens[3].trim();
        }
        return "";
    }
}