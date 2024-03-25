// Subclass of Product representing electronic products
public class Electronics extends Product {
    // Additional attributes specific to Electronics
    private String brand;
    private int warrantyPeriod;

    // Constructor to initialize attributes of Electronics and call the superclass constructor
    public Electronics(String productID, String productName, int availableItems, double price, String brand, int warrantyPeriod) {
        // Call the constructor of the superclass (Product) to initialize common attributes
        super(productID, productName, availableItems, price);

        // Initialize attributes specific to Electronics
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }

    // Getter for the brand of the electronic product
    public String getBrand() {
        return brand;
    }

    // Getter for the warranty period of the electronic product
    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }
}
