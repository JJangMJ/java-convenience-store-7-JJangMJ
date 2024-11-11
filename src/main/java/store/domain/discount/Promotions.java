package store.domain.discount;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Promotions {
    private final Map<String, Promotion> promotions;

    public Promotions(List<Promotion> promotionList) {
        this.promotions = new HashMap<>();
        for (Promotion promotion : promotionList) {
            this.promotions.put(promotion.getName(), promotion);
        }
    }

    // 프로모션 이름으로 프로모션을 검색하는 메서드
    public Promotion getPromotionByName(String promotionName) {
        Promotion promotion = promotions.get(promotionName);
        if (promotion != null && promotion.isActive(LocalDate.now())) {
            return promotion;
        }
        return null;
    }
}