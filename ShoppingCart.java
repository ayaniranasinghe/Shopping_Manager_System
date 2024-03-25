import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<Product> productList;

    // Constructor to initialize the productList
    public ShoppingCart() {
        this.productList = new ArrayList<>();
    }

    // Getter for productList
    public List<Product> getProductList() {
        return productList;
    }

    // Method to add a product to the cart
    public void addProduct(Product product) {
        productList.add(product);
    }

    // Method to calculate the total cost of products in the cart
    public double calculateTotalCost() {
        double totalCost = 0;

        for (Product product : productList) {
            totalCost += product.getPrice();
        }

        return totalCost;
    }

}
