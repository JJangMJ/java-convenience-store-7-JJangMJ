package store.domain;

public class ReceiptItem {
    private String productName;
    private int productQuantity;
    private int productPrice;
    private int totalPrice;

    public ReceiptItem(String productName, int productQuantity, int productPrice, int totalPrice) {
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
        this.totalPrice = totalPrice;
    }
}
