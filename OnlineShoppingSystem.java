import javax.swing.*;
import java.util.Scanner;

     // Main class representing an Online Shopping System
 public class OnlineShoppingSystem {
    public static void main(String[] args) {

        // Create instances of necessary classes
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        ShoppingCart shoppingCart = new ShoppingCart();

        boolean dataSaved = false;// Flag to track if data has been saved

        Scanner scanner = new Scanner(System.in);

        // Create a User
        User currentUser;
        boolean loginSuccessful;

        do {// User authentication loop
            System.out.print("Enter username: ");
            String username = scanner.next();
            System.out.print("Enter password: ");
            String password = scanner.next();
            currentUser = new User(username, password);

            // Set the current user in the manager
            shoppingManager.setCurrentUser(currentUser);

            // Check if the username and password are correct
            loginSuccessful = authenticateUser(currentUser);

            if (!loginSuccessful) {
                System.out.println("Login failed..");
                System.out.println("Please enter the correct username and password.");
            }

        } while (!loginSuccessful);
        System.out.println();
        System.out.println("Login successful!");

        int choice;
        // Main menu loop
        do {
            displayMenuOptions();
            // Validate the choice input
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();  // Consume the invalid input
            }
            choice = scanner.nextInt();

            switch (choice) {// Switch statement for menu options
                case 1:
                    addNewProduct(scanner, shoppingManager, shoppingCart);
                    break;
                case 2:
                    deleteProduct(scanner, shoppingManager);
                    break;
                case 3:
                    shoppingManager.printProductList();
                    break;
                case 4:
                    saveProductsToFile(shoppingManager);
                    dataSaved = true;
                    break;
                case 5:
                    loadProductsFromFile(shoppingManager, dataSaved);
                    break;
                case 6:
                    openGUI(scanner, shoppingManager, shoppingCart);
                    break;

                case 7:
                    System.out.println("Exiting the program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }

        } while (choice != 7);// Adjusted loop condition to match menu options


        scanner.close();
    }

    private static void displayMenuOptions() {// Method to display menu options
        System.out.println("\nMenu Options:");
        System.out.println();
        System.out.println("1. Add a new product");
        System.out.println("2. Delete a product");
        System.out.println("3. Print the list of products");
        System.out.println("4. Save products to a file");
        System.out.println("5. Load products from a file");
        System.out.println("6. Open GUI");
        System.out.println("7. Exit");
        System.out.println();
        System.out.print("Enter your choice: ");
    }

    // Method to add a new product
    private static void addNewProduct(Scanner scanner, WestminsterShoppingManager shoppingManager, ShoppingCart shoppingCart) {
        System.out.println("Adding a new product:");
        System.out.println("1. Electronics");
        System.out.println("2. Clothing");

        int productTypeChoice;

        // Validate the productTypeChoice input
        while (true) {
            System.out.print("Enter product type (1 for Electronics, 2 for Clothing): ");
            if (scanner.hasNextInt()) {
                productTypeChoice = scanner.nextInt();
                if (productTypeChoice == 1 || productTypeChoice == 2) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 1 or 2.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();  // Consume the invalid input
            }
        }

        String productID;
        boolean validProductID;

        do {
            System.out.print("Enter product ID: ");
            productID = scanner.next();

            // Validate the product ID format
            validProductID = isValidProductID(productID);

            if (!validProductID) {
                System.out.println("Invalid product ID format. Please follow the specified format.(1234XX");

            }

        } while (!validProductID);

        System.out.print("Enter product name: ");
        String productName = scanner.next();
        System.out.print("Enter available items: ");

        // Validate the availableItems input
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();  // Consume the invalid input
        }
        int availableItems = scanner.nextInt();

        System.out.print("Enter price: ");

        // Validate the price input
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid price.");
            scanner.next();  // Consume the invalid input
        }
        double price = scanner.nextDouble();

        if (productTypeChoice == 1) {
            // Adding a new Electronics product
            System.out.print("Enter brand: ");
            String brand = scanner.next();
            System.out.print("Enter warranty period (Months): ");

            // Validate the warrantyPeriod input
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();  // Consume the invalid input
            }
            int warrantyPeriod = scanner.nextInt();

            Electronics electronicsProduct = new Electronics(productID, productName, availableItems, price, brand, warrantyPeriod);
            shoppingManager.addProduct(electronicsProduct);
            // Add the product to the shopping cart
            shoppingCart.addProduct(electronicsProduct);

        } else if (productTypeChoice == 2) {
            // Adding a new Clothing product
            System.out.print("Enter size (S, M, L, XL, XXL): ");
            String size;

            // Validate the size input
            while (true) {
                size = scanner.next().toUpperCase();  // Convert the input to uppercase to handle both lower and upper case

                if (isValidClothingSize(size)) {
                    break;
                } else {
                    System.out.println("Invalid size. Please enter S, M, L, XL, or XXL.");
                }
            }

            System.out.print("Enter color: ");
            String color = scanner.next();

            Clothing clothingProduct = new Clothing(productID, productName, availableItems, price, size, color);
            shoppingManager.addProduct(clothingProduct);
        } else {
            System.out.println("Invalid product type choice. Please enter 1 or 2.");
            return;
        }

    }


    private static boolean isValidClothingSize(String size) {// Method to check if a clothing size is valid
        // Define valid clothing sizes
        String[] validSizes = {"S", "M", "L", "XL", "XXL"};

        // Check if the entered size is in the validSizes array
        for (String validSize : validSizes) {
            if (validSize.equals(size)) {
                return true;
            }
        }

        // If the entered size is not in the validSizes array, return false
        return false;
    }
    private static boolean isValidProductID(String productID) {// Method to check if a product ID is valid
        // Check if the product ID is not null and has the correct length
        if (productID != null && productID.length() == 6) {
            // Check if the first four characters are numbers
            boolean firstFourDigitsAreNumbers = productID.substring(0, 4).matches("\\d+");

            // Check if the last character is two capital letters
            boolean lastCharacterIsLetters = Character.isUpperCase(productID.charAt(5));

            return firstFourDigitsAreNumbers && lastCharacterIsLetters;
        }

        return false;
    }

    private static void deleteProduct(Scanner scanner, WestminsterShoppingManager shoppingManager) {
        System.out.println("Deleting a product:");
        System.out.print("Enter product ID to delete: ");
        String deleteProductID = scanner.next();

        // Call the deleteProduct method in WestminsterShoppingManager
        shoppingManager.deleteProduct(deleteProductID);
    }



    private static void saveProductsToFile(WestminsterShoppingManager shoppingManager) {
        String defaultFileName = "product_list.txt";

        // Call the saveToFile method in WestminsterShoppingManager with the default filename
        shoppingManager.saveToFile(defaultFileName);

    }

    private static void loadProductsFromFile(WestminsterShoppingManager shoppingManager, boolean dataSaved) {
        // Check if data has been saved before attempting to load
        if (dataSaved) {
            String defaultFileName = "product_list.txt"; // Provide a default filename

            System.out.println("Loading data from file...");

            // Call the loadData method in WestminsterShoppingManager with the default filename
            shoppingManager.loadData(defaultFileName);

        } else {
            System.out.println("Error: Data must be saved before loading.");

        }
    }


    private static void openGUI(Scanner scanner, WestminsterShoppingManager shoppingManager, ShoppingCart shoppingCart) {
        // Prompt user for username and password
        System.out.print("Enter username for GUI: ");
        String username = scanner.next();
        System.out.print("Enter password for GUI: ");
        String password = scanner.next();

        // Authenticate the user
        User user = new User(username, password);
        if (authenticateUser(user)) {
            SwingUtilities.invokeLater(() -> new OnlineShoppingGUI(shoppingManager, shoppingCart, username));
            System.out.println("\nGUI successfully opened!");
        } else {
            System.out.println("Authentication failed. GUI cannot be opened.");
        }
    }


    private static boolean authenticateUser(User user) {// Method to authenticate a user

        String validUsername = "user123";
        String validPassword = "password123";

        return validUsername.equals(user.getUsername()) && validPassword.equals(user.getPassword());
    }

}
