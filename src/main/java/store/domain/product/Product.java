package store.domain.product;

public class Product {
    private final String name;
    private final int price;
    private int quantity;
    private final String promotionName;

    public Product(String name, int price, int quantity, String promotionName) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotionName = promotionName;
    }

    public boolean hasPromotion() {
        return !promotionName.isEmpty();
    }

    @Override
    public String toString() {
        return formatBasicInfo() + formatQuantityInfo() + formatPromotionInfo();
    }

    private String formatBasicInfo() {
        return "- " + name + " " + String.format("%,d", price) + "원 ";
    }

    private String formatQuantityInfo() {
        if (quantity > 0) {
            return String.format("%,d개 ", quantity);
        }
        return "재고 없음 ";
    }

    private String formatPromotionInfo() {
        if (hasPromotion()) {
            return promotionName;
        }
        return "";
    }
}
