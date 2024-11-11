package store.view;

import java.util.ArrayList;
import java.util.List;
import store.domain.product.Product;
import store.domain.receipt.Receipt;
import store.domain.receipt.ReceiptItem;

public class OutputView {

    public void printWelcomeGreeting() {
        System.out.println("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n");
    }

    public void printAllItems(List<Product> inventoryItems) {
        for (Product inventoryItem : inventoryItems) {
            System.out.println(inventoryItem.toString());
        }
        System.out.println();
    }

    public void printReceipt(Receipt receipt) {
        printReceiptHeader();
        printPurchasedItems(receipt.getReceiptItems());
        printFreeItems(receipt.getReceiptItems());
        printReceiptFooter(receipt);
    }

    private void printReceiptHeader() {
        System.out.println("=============W 편의점=============");
    }

    private void printPurchasedItems(List<ReceiptItem> receiptItems) {
        System.out.printf("%-17s %s %8s%n", "상품명", "수량", "금액");
        for (ReceiptItem item : receiptItems) {
            System.out.printf("%-18s %d %11s%n", item.getItemName(), item.getQuantity(), String.format("%,d원", item.getTotalPrice()));
        }
        System.out.println();
    }

    private void printFreeItems(List<ReceiptItem> receiptItems) {
        List<ReceiptItem> freeItems = new ArrayList<>();
        for (ReceiptItem item : receiptItems) {
            if (item.getPromotionDiscount() > 0) {
                int freeQuantity = item.getPromotionDiscount() / item.getPrice();
                if (freeQuantity > 0) {
                    ReceiptItem freeItem = new ReceiptItem(item.getItemName(), freeQuantity, item.getPrice(), 0);
                    freeItem.setPromotionDiscount(0); // 무료 아이템은 할인 금액 없음
                    freeItems.add(freeItem);
                }
            }
        }

        System.out.println("=============증\t정===============");
        for (ReceiptItem freeItem : freeItems) {
            System.out.printf("%-18s %d%n", freeItem.getItemName(), freeItem.getQuantity());
        }
        System.out.println("===================================");

    }

    private void printReceiptFooter(Receipt receipt) {
        System.out.printf("%-15s %15s%n", "총구매액", String.format("%,d원", receipt.getOriginalTotal()));
        System.out.printf("%-15s %15s%n", "행사할인", formatDiscount(receipt.getPromotionDiscount()));
        System.out.printf("%-14s %15s%n", "멤버십할인", formatDiscount(receipt.getMembershipDiscount()));
        System.out.printf("%-16s %15s%n", "내실돈", String.format("%,d원", receipt.getFinalAmount()));
        System.out.println();
    }

    private String formatDiscount(int discount) {
        return String.format("-%s원", String.format("%,d", discount));
    }
}