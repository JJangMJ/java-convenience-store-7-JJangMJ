package store.domain.transaction;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.domain.discount.Promotion;
import store.domain.discount.Promotions;
import store.domain.product.Product;
import store.domain.product.SelectedProduct;
import store.domain.receipt.Receipt;
import store.domain.receipt.ReceiptItem;
import store.view.InputView;

public class Cashier {
    private final Promotions promotions;
    private final List<Product> products;
    private final InputView inputView;
    private int originalTotal;

    public Cashier(Promotions promotions, List<Product> products) {
        this.promotions = promotions;
        this.products = products;
        this.inputView = new InputView();
    }

    public Receipt checkOut(List<SelectedProduct> selectedProducts, List<Product> matchedProducts) {
        List<ReceiptItem> receiptItems = createReceiptItems(selectedProducts, matchedProducts);
        originalTotal = calculateTotalPrice(receiptItems);
        int promotionDiscount = calculatePromotionDiscount(receiptItems);
        int membershipDiscount = 0;
        if (inputView.askForMembershipDiscount()) {
            membershipDiscount = calculateMembershipDiscount(receiptItems);
        }
        int finalAmount = originalTotal - promotionDiscount - membershipDiscount;
        return new Receipt(receiptItems, originalTotal, promotionDiscount, membershipDiscount, finalAmount);
    }

    private int calculatePromotionDiscount(List<ReceiptItem> receiptItems) {
        int totalDiscount = 0;
        for (ReceiptItem item : receiptItems) {
            if (!item.canApplyPromotion()) {
                continue;
            }
            String promotionName = item.getPromotionName();
            Promotion promotion = promotions.getPromotionByName(promotionName);
            if (!promotion.isActive(LocalDate.from(DateTimes.now()))) {
                continue;
            }
            int freeQuantity = promotion.getPromotionType().getFreeQuantity();
            int requiredQuantity = promotion.getPromotionType().getRequiredQuantity();
            int setSize = requiredQuantity + freeQuantity;
            int remainingQuantity = item.getQuantity() % setSize;
            int discount = promotion.calculateDiscount(item, inputView);
            totalDiscount += discount;
            item.setPromotionDiscount(discount);
            for (Product product : products) {
                if ((remainingQuantity >= requiredQuantity) && product.isSameProduct(item.getItemName(), product.getQuantity() - item.getQuantity(), item.getPromotionName())) {
                    originalTotal += item.getPrice();
                    product.reduceStock(freeQuantity);
                }
            }
        }
        return totalDiscount;
    }

    private int calculateMembershipDiscount(List<ReceiptItem> receiptItems) {
        int noPromotionTotal = 0;
        for (ReceiptItem item : receiptItems) {
            if (!item.canApplyPromotion()) {
                noPromotionTotal += item.getTotalPrice();
            }
        }
        int discount = (int) (noPromotionTotal * 0.3);
        if (discount > 8000) {
            discount = 8000;
        }
        return discount;
    }

    private List<ReceiptItem> createReceiptItems(List<SelectedProduct> selectedProducts, List<Product> matchedProducts) {
        Map<String, List<Product>> inventoryItems = mapInventoryItems(matchedProducts);
        List<ReceiptItem> receiptItems = new ArrayList<>();
        for (SelectedProduct selectedProduct : selectedProducts) {
            ReceiptItem receiptItem = createReceiptItem(selectedProduct, inventoryItems);
            receiptItems.add(receiptItem);
        }
        return receiptItems;
    }

    private Map<String, List<Product>> mapInventoryItems(List<Product> matchedProduct) {
        Map<String, List<Product>> products = new HashMap<>();
        for (Product product : matchedProduct) {
            products.computeIfAbsent(product.getName(), k -> new ArrayList<>()).add(product);
        }
        return products;
    }


    private ReceiptItem createReceiptItem(SelectedProduct selectedProduct, Map<String, List<Product>> products) {
        String itemName = selectedProduct.getProductName();
        int quantityNeeded = selectedProduct.getProductQuantity();
        List<Product> availableProducts = products.get(itemName);

        int totalPrice = 0;
        boolean canApplyPromotion = false;
        int totalQuantity = 0;
        String promotionName = "";

        for (Product product : availableProducts) {
            if (quantityNeeded <= 0) {
                break;
            }
            if (product.getQuantity() <= 0) {
                continue;
            }
            int quantityToSell = Math.min(product.getQuantity(), quantityNeeded);
            if (product.hasPromotion()) {
                if (product.getQuantity() < quantityToSell) {
                    if (inputView.askToPayFullPrice(product.getName(), product.getQuantity())) {
                        product.reduceStock(quantityToSell);
                        return new ReceiptItem(itemName, product.getQuantity(), product.getPrice(),
                                product.getPrice() * quantityToSell);
                    }
                }
            }
            product.reduceStock(quantityToSell);
            totalPrice += product.getPrice() * quantityToSell;
            totalQuantity += quantityToSell;
            quantityNeeded -= quantityToSell;

            if (product.hasPromotion()) {
                canApplyPromotion = true;
                promotionName = product.getPromotionName();
            }
        }

        int unitPrice = totalPrice / totalQuantity;
        ReceiptItem receiptItem = new ReceiptItem(itemName, totalQuantity, unitPrice, totalPrice);
        receiptItem.setCanApplyPromotion(canApplyPromotion);
        receiptItem.setPromotionName(promotionName);
        return receiptItem;
    }

    private int calculateTotalPrice(List<ReceiptItem> receiptItems) {
        int totalPrice = 0;
        for (ReceiptItem item : receiptItems) {
            totalPrice += item.getTotalPrice();
        }
        return totalPrice;
    }
}