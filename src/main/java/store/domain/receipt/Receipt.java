package store.domain.receipt;

import java.util.List;

public class Receipt {
    private final List<ReceiptItem> receiptItems;
    private final int originalTotal;
    private final int promotionDiscount;
    private final int membershipDiscount;
    private final int finalAmount;

    public Receipt(List<ReceiptItem> receiptItems, int originalTotal, int promotionDiscount, int membershipDiscount, int finalAmount) {
        this.receiptItems = receiptItems;
        this.originalTotal = originalTotal;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
        this.finalAmount = finalAmount;
    }

    public List<ReceiptItem> getReceiptItems() {
        return receiptItems;
    }

    public int getOriginalTotal() {
        return originalTotal;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getFinalAmount() {
        return finalAmount;
    }
}
