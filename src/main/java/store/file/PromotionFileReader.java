package store.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.domain.discount.Promotion;
import store.domain.discount.PromotionType;

public class PromotionFileReader {
    private static final String PROMOTION_FILE_PATH = "src/main/resources/promotions.md";

    public List<Promotion> readAllPromotions() throws IOException {
        List<Promotion> promotions = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(PROMOTION_FILE_PATH))) {
            skipHeader(br);
            String line;
            int lineNumber = 2; // 헤더를 제외한 첫 번째 라인의 번호
            while ((line = br.readLine()) != null) {
                promotions.add(parseLine(line, lineNumber));
                lineNumber++;
            }
        }
        return promotions;
    }

    private void skipHeader(BufferedReader br) throws IOException {
        br.readLine();
    }

    private Promotion parseLine(String line, int lineNumber) {
        String[] tokens = line.split(",");
        validateLineFormat(tokens, lineNumber);
        String name = tokens[0].trim();
        int buy = parseInteger(tokens[1].trim(), lineNumber, "구매 수량");
        int get = parseInteger(tokens[2].trim(), lineNumber, "무료 수량");
        LocalDate startDate = LocalDate.parse(tokens[3].trim());
        LocalDate endDate = LocalDate.parse(tokens[4].trim());
        PromotionType promotionType = PromotionType.fromBuyGet(buy, get);
        return new Promotion(name, promotionType, startDate, endDate);
    }

    private void validateLineFormat(String[] tokens, int lineNumber) {
        if (tokens.length != 5) {
            throw new IllegalArgumentException("[ERROR] " + lineNumber + "번째 라인의 형식이 잘못되었습니다.");
        }
    }

    private int parseInteger(String value, int lineNumber, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] " + lineNumber + "번째 라인의 " + fieldName + "이(가) 올바르지 않습니다: " + value);
        }
    }
}