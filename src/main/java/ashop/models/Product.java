package ashop.models;

public class Product {
    private int id;
    private String name;
    private String category;
    private double price;
    private double quantity;
    private String type;

    public Product(int id, String name, String category, double price, double quantity, String type) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public double getQuantity() { return quantity; }
    public String getType() { return type; }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + category + "," + price + "," + quantity + "," + type;
    }

    public String getFormattedPrice() {
        return type.equals("weight") ? String.format("$%.2f/kg", price) : String.format("$%.2f", price);
    }

    public String getFormattedQuantity() {
        if (type.equals("weight")) {
            return (quantity % 1 == 0 ? String.format("%.0f", quantity) : String.format("%.1f", quantity)) + " kg available";
        } else {
            return (quantity % 1 == 0 ? String.format("%.0f", quantity) : String.format("%.1f", quantity)) + " units available";
        }
    }
}