package store.domain.discount;

public enum PromotionType {
    BUY_ONE_GET_ONE("1+1", 1, 1),
    BUY_TWO_GET_ONE("2+1", 2, 1);

    private final String description;
    private final int requiredQuantity;
    private final int freeQuantity;

    PromotionType(String description, int requiredQuantity, int freeQuantity) {
        this.description = description;
        this.requiredQuantity = requiredQuantity;
        this.freeQuantity = freeQuantity;
    }

    public static PromotionType fromBuyGet(int buy, int get) {
        for (PromotionType type : PromotionType.values()) {
            if (type.requiredQuantity == buy && type.freeQuantity == get) {
                return type;
            }
        }
        throw new IllegalArgumentException("[ERROR] 알 수 없는 프로모션 유형입니다: " + buy + "+" + get);
    }

    public String getDescription() {
        return description;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }
}
