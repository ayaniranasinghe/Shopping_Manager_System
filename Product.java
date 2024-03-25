// Abstract class representing a generic product
public abstract class Product {
    // Common attributes for all products
    private final String productID;
    private String productName;
    private int availableItems;
    private double price;

    // Constructor to initialize common attributes
    public Product(String productID, String productName, int availableItems, double price) {
        this.productID = productID;
        this.setProductName(productName);
        this.setAvailableItems(availableItems);
        this.setPrice(price);
    }

    // Getter for product ID
    public String getProductID() {
        return productID;
    }

    // Getter and setter for product name with validation
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        // Validate and set the product name
        if (productName != null && !productName.isEmpty()) {
            this.productName = productName;
        } else {
            throw new IllegalArgumentException("Invalid productName: " + productName);
        }
    }

    // Getter and setter for available items with validation
    public int getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(int availableItems) {
        // Validate and set the available items
        if (availableItems >= 0) {
            this.availableItems = availableItems;
        } else {
            throw new IllegalArgumentException("Invalid availableItems: " + availableItems);
        }
    }

    // Getter and setter for price with validation
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        // Validate and set the price
        if (price >= 0) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Invalid price: " + price);
        }
    }

    // Default implementations for abstract methods
    // This method can be overridden in subclasses
    public String getBrand() {
        return "N/A";
    }

    public int getWarrantyPeriod() {
        return 0;
    }
}
