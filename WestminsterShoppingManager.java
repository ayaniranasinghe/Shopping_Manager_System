import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Define the ShoppingManager interface
interface ShoppingManager {
    int MAX_ALLOWED_PRODUCTS = 50;

    void addProduct(Product product);
    void deleteProduct(String productID);
    void printProductList();
    void saveToFile(String fileName);
    void loadData(String fileName);
    List<Product> getProductList();
}

// Define the WestminsterShoppingManager class implementing ShoppingManager interface
class WestminsterShoppingManager implements ShoppingManager {
    private List<Product> productList;
    private Map<String, Integer> userPurchaseHistory;
    private User currentUser;

    public WestminsterShoppingManager() {
        this.productList = new ArrayList<>();
        this.userPurchaseHistory = new HashMap<>();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    // Method to add a product to the productList
    public void addProduct(Product product) {
        // Check for duplicate product IDs before adding a new product
        if (isDuplicateProductID(product.getProductID())) {
            System.out.println("Error: Duplicate product ID. Please use a unique product ID.");
            return;
        }

        // Continue adding the product if the product ID is not a duplicate
        if (productList.size() < MAX_ALLOWED_PRODUCTS) {
            productList.add(product);
            System.out.println("Product added successfully.");
        } else {
            System.out.println("Cannot add more products. Maximum limit reached.");
        }
    }

    // Helper method to check for duplicate product IDs
    private boolean isDuplicateProductID(String productID) {
        // Check for duplicate product IDs
        for (Product existingProduct : productList) {
            if (existingProduct.getProductID().equals(productID)) {
                return true; // Found a product with the same ID
            }
        }
        return false; // No duplicate ID found
    }

    // Method to delete a product from the productList
    public void deleteProduct(String productID) {
        Product productToRemove = null;

        for (Product product : productList) {
            if (product.getProductID().equals(productID)) {
                productToRemove = product;
                break; // Exit the loop after finding the product
            }
        }

        if (productToRemove != null) {
            productList.remove(productToRemove);
            System.out.println("Product deleted successfully.");

            // Print the number of available products after deletion
            System.out.println("Number of available products: " + productList.size());
        } else {
            System.out.println("Product not found with ID: " + productID);
        }
    }

    // Method to print the product list in a formatted manner
    public void printProductList() {
        // Order the product list alphabetically by product ID
        productList.sort(Comparator.comparing(Product::getProductID));

        System.out.printf("%-10s %-20s %-15s %-20s %-15s %-15s %-15s\n",
                "ID", "Name", "Type", "Available Items", "Price", "Brand/Size", "Warranty/Color");
        System.out.println("----------------------------------------------------------------------------------------------");

        for (Product product : productList) {
            if (product instanceof Electronics) {
                // If the product is an instance of Electronics, print relevant details
                Electronics electronicsProduct = (Electronics) product;
                System.out.printf("%-10s %-20s %-15s %-18d %-15.2f %-15s %-15d\n",
                        electronicsProduct.getProductID(), electronicsProduct.getProductName(), "Electronics",
                        electronicsProduct.getAvailableItems(), electronicsProduct.getPrice(),
                        electronicsProduct.getBrand(), electronicsProduct.getWarrantyPeriod());
            } else if (product instanceof Clothing) {
                // If the product is an instance of Clothing, print relevant details
                Clothing clothingProduct = (Clothing) product;
                System.out.printf("%-10s %-20s %-15s %-18d %-15.2f %-15s %-15s\n",
                        clothingProduct.getProductID(), clothingProduct.getProductName(), "Clothing",
                        clothingProduct.getAvailableItems(), clothingProduct.getPrice(),
                        clothingProduct.getSize(), clothingProduct.getColor());
            }
        }
    }

    // Method to save the product list to a file
    @Override
    public void saveToFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Product product : productList) {
                if (product instanceof Electronics) {
                    // If the product is an instance of Electronics, write relevant details to the file
                    Electronics electronicsProduct = (Electronics) product;
                    writer.println("Product Type: Electronics");
                    writer.println("Product ID: " + electronicsProduct.getProductID());
                    writer.println("Product Name: " + electronicsProduct.getProductName());
                    writer.println("Available Items: " + electronicsProduct.getAvailableItems());
                    writer.println("Price: $" + electronicsProduct.getPrice());
                    writer.println("Brand: " + electronicsProduct.getBrand());
                    writer.println("Warranty Period: " + electronicsProduct.getWarrantyPeriod() + " months");
                } else if (product instanceof Clothing) {
                    // If the product is an instance of Clothing, write relevant details to the file
                    Clothing clothingProduct = (Clothing) product;
                    writer.println("Product Type: Clothing");
                    writer.println("Product ID: " + clothingProduct.getProductID());
                    writer.println("Product Name: " + clothingProduct.getProductName());
                    writer.println("Available Items: " + clothingProduct.getAvailableItems());
                    writer.println("Price: $" + clothingProduct.getPrice());
                    writer.println("Size: " + clothingProduct.getSize());
                    writer.println("Color: " + clothingProduct.getColor());
                } else {
                    writer.println("Unknown product type: " + product.getClass().getSimpleName());
                }

                writer.println("--------------------");
            }
            System.out.println("Product list saved to the file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving product list to file.");
            e.printStackTrace();
        }
    }

    // Method to load product data from a file
    @Override
    public void loadData(String fileName) {
        System.out.println("---Loading Data---");
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Process each line as needed
                System.out.println(line);
            }
            System.out.println("Data loaded successfully from the file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error loading data from file.");
            e.printStackTrace();
        }
    }

    // Method to get an unmodifiable view of the product list
    @Override
    public List<Product> getProductList() {
        return Collections.unmodifiableList(productList);
    }

    // Method to add a product to the shopping cart
    public void addToShoppingCart(String productId, ShoppingCart shoppingCart) {
        Product product = findProductById(productId);

        // Check if the product is not null and add it to the cart
        if (product != null) {
            shoppingCart.addProduct(product);
        }
    }

    // Method to find a product by its ID
    public Product findProductById(String productId) {
        for (Product product : productList) {
            if (product.getProductID().equals(productId)) {
                return product;
            }
        }
        return null; // Product with the given ID not found
    }
}
