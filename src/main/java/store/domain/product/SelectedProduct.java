package store.domain.product;

public class SelectedProduct {
    private String productName;
    private int productQuantity;

    public SelectedProduct(String productName, int productQuantity) {
        this.productName = productName;
        this.productQuantity = productQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }
}
