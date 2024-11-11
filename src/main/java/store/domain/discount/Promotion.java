package store.domain.discount;

import java.time.LocalDate;
import store.domain.receipt.ReceiptItem;
import store.view.InputView;

public class Promotion {
    private final String name;
    private final PromotionType promotionType;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(String name, PromotionType promotionType, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.promotionType = promotionType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isActive(LocalDate currentDate) {
        return !currentDate.isBefore(startDate) && !currentDate.isAfter(endDate);
    }

    public int calculateDiscount(ReceiptItem item, InputView inputView) {
        int requiredQuantity = this.getPromotionType().getRequiredQuantity();
        int freeQuantity = this.getPromotionType().getFreeQuantity();
        int setSize = requiredQuantity + freeQuantity;

        int applicablePromotionSets = item.getProductQuantity() / setSize;
        int remainingQuantity = item.getProductQuantity() % setSize;

        int discount = applicablePromotionSets * freeQuantity * item.getProductPrice();

        if (remainingQuantity >= requiredQuantity) {
            boolean addFreeItems = inputView.askToAddFreeItems(item.getProductName(), freeQuantity);
            if (addFreeItems) {
                item.addQuantity(freeQuantity);
                discount += freeQuantity * item.getPrice();
            }
        }
        return discount;
    }

    public String getName() {
        return name;
    }

    public PromotionType getPromotionType() {
        return promotionType;
    }
}