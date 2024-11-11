package store.domain.product;

public class Product {
    private final String name;
    private final int price;
    private final String promotionName;
    private int quantity;

    public Product(String name, int price, int quantity, String promotionName) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotionName = promotionName;
    }

    public boolean isSameItem(String itemName, int quantity, String promotionName) {
        return this.name.equals(itemName) && quantity <= this.quantity && this.promotionName.equals(promotionName);
    }

    public void reduceStock(int quantity) {
        this.quantity -= quantity;
    }

    public boolean hasPromotion() {
        return !promotionName.isEmpty();
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotionName() {
        return promotionName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("- ").append(name).append(" ").append(String.format("%,d", price)).append("원 ");
        if (quantity > 0) {
            sb.append(String.format("%,d개 ", quantity));
        } else {
            sb.append("재고 없음 ");
        }
        if (hasPromotion()) {
            sb.append(promotionName);
        }
        return sb.toString().trim(); // 문자열 끝의 공백 제거
    }
}