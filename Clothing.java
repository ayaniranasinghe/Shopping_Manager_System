import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Clothing extends Product {

    private String size;
    private String color;

    // Define valid sizes
    private static final Set<String> VALID_SIZES = new HashSet<>(Arrays.asList("S", "M", "L", "XL", "XXL"));

    public Clothing(String productID, String productName, int availableItems, double price, String size, String color) {
        super(productID, productName, availableItems, price);
        setSize(size);
        setColor(color);
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        if (isValidSize(size)) {
            this.size = size.toUpperCase(); // Convert to uppercase to handle case-insensitivity
        } else {
            throw new IllegalArgumentException("Invalid size: " + size);
        }
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color; // Assign color without validation
    }

    private boolean isValidSize(String size) {
        return size != null && !size.isEmpty() && VALID_SIZES.contains(size.toUpperCase());
    }
}
