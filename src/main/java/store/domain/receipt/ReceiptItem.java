package store.domain.receipt;

public class ReceiptItem {
    private String itemName;
    private int quantity;
    private int price;
    private int totalPrice;
    private boolean promotionApplied;
    private int promotionDiscount;
    private String promotionName;

    public ReceiptItem(String itemName, int quantity, int price, int totalPrice) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
        this.promotionApplied = false;
        this.promotionDiscount = 0;
        this.promotionName = "";
    }

    public void addQuantity(int additionalQuantity) {
        this.quantity += additionalQuantity;
        updateTotalPrice();
    }

    public void updateTotalPrice() {
        this.totalPrice = price * quantity;
    }

    // Getter 및 Setter 메서드

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public boolean canApplyPromotion() {
        return promotionApplied;
    }

    public void setCanApplyPromotion(boolean promotionApplied) {
        this.promotionApplied = promotionApplied;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public void setPromotionDiscount(int promotionDiscount) {
        this.promotionDiscount = promotionDiscount;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }
}