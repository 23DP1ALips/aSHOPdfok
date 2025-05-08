package ashop.models;

public class CartItem {
    private Product product;
    private double quantity; // Changed to double to handle fractional weights

    public CartItem(Product product, double quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Getters
    public Product getProduct() { return product; }
    public double getQuantity() { return quantity; }

    // Setters
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return product.getId() + "," + quantity;
    }

    public String getFormattedQuantity() {
        return product.getType().equals("weight") ? 
            String.format("%.2f kg", quantity) : 
            String.format("%d units", (int) quantity);
    }
}