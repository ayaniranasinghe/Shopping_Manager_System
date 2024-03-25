import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OnlineShoppingGUI {
    // Variables for managing shopping
    private WestminsterShoppingManager shoppingManager;
    private ShoppingCart shoppingCart;
    private List<Purchase> purchaseHistory;

    // GUI components
    private JFrame frame;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextArea productDetailsTextArea;
    private JButton addToCartButton;
    private JButton viewShoppingCartButton;
    private String userId;
    private String selectedProductType = "All";
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JScrollPane cartScrollPane;


    public OnlineShoppingGUI(WestminsterShoppingManager shoppingManager, ShoppingCart shoppingCart, String userId) {//constructor
        this.shoppingManager = shoppingManager;
        this.shoppingCart = shoppingCart;
        this.userId = userId;
        this.purchaseHistory = new ArrayList<>();
        initialize();
    }

    public static void main(String[] args) { // Main method to launch the GUI
        SwingUtilities.invokeLater(() -> {
            try {
                WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
                ShoppingCart shoppingCart = new ShoppingCart();
                new OnlineShoppingGUI(shoppingManager, shoppingCart, "user123");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initialize() {// Initialize the GUI
        setupMainFrame();
        setupProductTable();
        setupProductComboBox();
        setupDetailsPanel();
        setupCartTable();
        setupListeners();

        frame.setVisible(true);
        updateProductTable();
    }

    private void setupMainFrame() {// Set up the main frame
        frame = new JFrame("Westminster Shopping Centre");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Set up the product table
    private void setupProductTable() {
        // Create a table model with column names
        tableModel = new DefaultTableModel(new Object[]{"Product ID", "Product Name", "Category", "Price", "Information"}, 0);
        // Create a JTable with the created model
        productTable = new JTable(tableModel);

        // Set a custom cell renderer to handle quantity-based highlighting
        productTable.setDefaultRenderer(Object.class, new QuantityRenderer());

        // Set up a row sorter for the table
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        productTable.setRowSorter(sorter);

        // Create a scroll pane and add the product table to it
        JScrollPane scrollPane = new JScrollPane(productTable);

        // Add the scroll pane to the main frame's center position
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
    }


    // Custom cell renderer to handle quantity-based highlighting
    class QuantityRenderer extends DefaultTableCellRenderer {
        // Override method for rendering table cell components
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            // Call the superclass method to get the default rendering
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Get the available quantity for the current row
            int availableQuantity = getAvailableQuantityForRow(row);

            // Determine the background color based on available quantity
            Color rowColor = (availableQuantity < 3) ? Color.RED : table.getBackground();
            component.setBackground(rowColor);

            // Return the modified component
            return component;
        }

        // Method to retrieve the available quantity for a specific row
        private int getAvailableQuantityForRow(int row) {
            // Get the product ID from the table model at the specified row
            String productId = (String) tableModel.getValueAt(row, 0);

            // Find the corresponding product using the shopping manager
            Product product = shoppingManager.findProductById(productId);

            // Return the available quantity or 0 if the product is not found
            return (product != null) ? product.getAvailableItems() : 0;
        }
    }



    // Set up the product category selection combo box
    private void setupProductComboBox() {
        // Create a JComboBox with product categories
        JComboBox<String> productTypeComboBox = new JComboBox<>(new String[]{"All", "Electronics", "Clothes"});

        // Add an ActionListener to the combo box for category selection changes
        productTypeComboBox.addActionListener(e -> {
            // Update the selected product type based on the combo box selection
            selectedProductType = (String) productTypeComboBox.getSelectedItem();

            // Update the product table to display products of the selected type
            updateProductTable();
        });

        // Create a panel for center-aligned components
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(new JLabel("Select Product Category:"));
        centerPanel.add(productTypeComboBox);

        // Create a panel for right-aligned components (e.g., shopping cart button)
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        viewShoppingCartButton = new JButton("Shopping Cart");
        rightPanel.add(viewShoppingCartButton);

        // Create a main panel with a BorderLayout to house the center and right panels
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);

        // Add the main panel to the frame's content pane in the NORTH region
        frame.getContentPane().add(panel, BorderLayout.NORTH);
    }


    // Set up the panel for displaying selected product details and adding to cart
    private void setupDetailsPanel() {
        // Create a JTextArea for displaying product details
        productDetailsTextArea = new JTextArea(10, 30);

        // Create a JButton for adding the selected product to the shopping cart
        addToCartButton = new JButton("Add to Shopping Cart");

        // Add an ActionListener to the button for handling add to cart action
        addToCartButton.addActionListener(e -> addToCartAction());

        // Create a panel for arranging components vertically using BoxLayout
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        // Add a JLabel for indicating selected product details
        detailsPanel.add(new JLabel("Selected Product Details:"));

        // Add the JTextArea for displaying product details
        detailsPanel.add(productDetailsTextArea);

        // Add vertical glue for flexible spacing
        detailsPanel.add(Box.createVerticalGlue());

        // Set an empty border to provide some spacing
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Create a panel for center-aligned components (e.g., add to cart button)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addToCartButton);

        // Add the button panel to the main details panel
        detailsPanel.add(buttonPanel);

        // Add the main details panel to the frame's content pane in the SOUTH region
        frame.getContentPane().add(detailsPanel, BorderLayout.SOUTH);
    }

    // Set up the table for displaying the shopping cart
    private void setupCartTable() {
        // Create a table model with columns for "Product", "Quantity", and "Price"
        cartTableModel = new DefaultTableModel(new Object[]{"Product", "Quantity", "Price"}, 0);

        // Create a JTable using the cart table model
        cartTable = new JTable(cartTableModel);

        // Create a scroll pane for the shopping cart table
        cartScrollPane = new JScrollPane(cartTable);
    }

    // Set up event listeners for buttons and table selections
    private void setupListeners() {
        // Add an action listener to the "Add to Shopping Cart" button
        addToCartButton.addActionListener(e -> addToCartAction());

        // Add an action listener to the "Shopping Cart" button
        viewShoppingCartButton.addActionListener(e -> viewShoppingCart());

        // Add a selection listener to the product table
        productTable.getSelectionModel().addListSelectionListener(e -> {
            // Get the selected row in the product table
            int selectedRow = productTable.getSelectedRow();

            // Check if a valid row is selected
            if (selectedRow != -1) {
                // Get the product ID from the selected row
                String productId = (String) productTable.getValueAt(selectedRow, 0);

                // Find the corresponding product in the shopping manager
                Product selectedProduct = shoppingManager.findProductById(productId);

                // Update the product details panel with the selected product
                updateProductDetailsPanel(selectedProduct);
            }
        });
    }

    // Handle the action when the "Add to Shopping Cart" button is clicked
    private void addToCartAction() {
        // Get the selected row in the product table
        int selectedRow = productTable.getSelectedRow();

        // Check if a valid row is selected
        if (selectedRow != -1) {
            // Get the product ID from the selected row
            String productId = (String) productTable.getValueAt(selectedRow, 0);

            // Check for existing purchase in the purchase history
            Purchase existingPurchase = purchaseHistory.stream()
                    .filter(purchase -> purchase.productId.equals(productId))
                    .findFirst()
                    .orElse(null);

            // If the product is already in the purchase history
            if (existingPurchase != null) {
                // Increment the quantity of the existing purchase
                existingPurchase.quantity++;
            } else {
                // If the product is not in the purchase history, add a new purchase
                shoppingManager.addToShoppingCart(productId, shoppingCart);
                Purchase purchase = new Purchase(productId, selectedProductType, 1);
                purchaseHistory.add(purchase);
            }

            // Update the display of the shopping cart details
            updateShoppingCartDisplay();

            // Update the shopping cart table with the latest data
            updateShoppingCartTable();
        } else {
            // If no product is selected, show an error message
            JOptionPane.showMessageDialog(frame, "Please select a product to add to the shopping cart.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isFirstPurchase() {
        return purchaseHistory.size() == 1;
    }

    // Check if the user is eligible for a category discount based on the selected product type
    private boolean hasCategoryDiscount() {
        // Count the number of purchases with the same product type as the currently selected type
        long categoryCount = purchaseHistory.stream()
                .filter(purchase -> selectedProductType.equals(purchase.getProductType()))
                .count();

        // Return true if the count is greater than or equal to 3 (eligible for discount), otherwise false
        return categoryCount >= 3;
    }


    //  row to the shopping cart table with information about the product and purchase
    private void addRowToCartTable(Product product, Purchase purchase) {
        // Construct the data for the row, including product name, ID, information, quantity, and price
        Object[] rowData = {
                product.getProductName() + " (ID: " + product.getProductID() + ")\n" + getProductInformation(product),
                purchase.getQuantity(),
                product.getPrice()
        };

        // Add the constructed row data to the cart table model
        cartTableModel.addRow(rowData);

        // Set the row height of the cart table to accommodate larger content
        cartTable.setRowHeight(100);
    }

    // Update the product details panel with information about the selected product
    private void updateProductDetailsPanel(Product product) {
        // Create a StringBuilder to construct the details text
        StringBuilder details = new StringBuilder("Product Details:\n");

        // Append basic product information
        details.append("Product ID: ").append(product.getProductID()).append("\n");
        details.append("Product Name: ").append(product.getProductName()).append("\n");
        details.append("Category: ").append(getProductCategory(product)).append("\n");
        details.append("Price: $").append(product.getPrice()).append("\n");

        // Retrieve and append available quantity information
        int availableQuantity = product.getAvailableItems();
        details.append("Available Quantity: ").append(availableQuantity).append("\n");

        // Check product type and append specific details for Electronics or Clothing
        if (product instanceof Electronics) {
            details.append("Brand: ").append(((Electronics) product).getBrand()).append("\n");
            details.append("Warranty: ").append(((Electronics) product).getWarrantyPeriod()).append("\n");
        } else if (product instanceof Clothing) {
            details.append("Color: ").append(((Clothing) product).getColor()).append("\n");
            details.append("Size: ").append(((Clothing) product).getSize()).append("\n");
        }

        // Set the constructed details as the text for the productDetailsTextArea
        productDetailsTextArea.setText(details.toString());
    }


    // Determine and return the category of the given product
    private String getProductCategory(Product product) {
        // Check if the product is an instance of Electronics
        if (product instanceof Electronics) {
            // If true, return the category as "Electronics"
            return "Electronics";
        } else if (product instanceof Clothing) {
            // If the product is an instance of Clothing, return the category as "Clothes"
            return "Clothes";
        } else {
            // If the product is not Electronics or Clothing, return the category as "Unknown"
            return "Unknown";
        }
    }


    // Update the display of the shopping cart details in the JTextArea
    private void updateShoppingCartDisplay() {
        // Set to keep track of unique product IDs in the shopping cart
        Set<String> uniqueProductIds = new HashSet<>();

        // StringBuilder to construct the shopping cart details text
        StringBuilder cartDetails = new StringBuilder("Shopping Cart:\n");

        // Iterate through each product in the shopping cart
        for (Product product : shoppingCart.getProductList()) {
            // Get the product ID
            String productId = product.getProductID();

            // Check if the product ID is not already processed (unique)
            if (!uniqueProductIds.contains(productId)) {
                // Append product details to the cartDetails StringBuilder
                cartDetails.append("Product ID: ").append(productId).append("\n");
                cartDetails.append("Product Name: ").append(product.getProductName()).append("\n");
                cartDetails.append("Price: $").append(product.getPrice()).append("\n");
                cartDetails.append("--------------------\n");

                // Add the product ID to the set of unique product IDs
                uniqueProductIds.add(productId);
            }
        }

        // Set the constructed shopping cart details text to the JTextArea
        productDetailsTextArea.setText(cartDetails.toString());
    }


    // Display the shopping cart, including total cost and discount details, in a new JFrame
    private void viewShoppingCart() {
        // Update the shopping cart table to ensure it reflects the latest data
        updateShoppingCartTable();

        // Create a new JFrame for the shopping cart display
        JFrame cartFrame = new JFrame("Shopping Cart");
        cartFrame.setSize(600, 400);
        cartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add the shopping cart table (JScrollPane) to the content pane of the JFrame
        cartFrame.getContentPane().add(cartScrollPane);

        // Check if there are purchases in the purchase history
        if (!purchaseHistory.isEmpty()) {
            // Calculate the total cost with discounts
            double totalCost = calculateTotalCostWithDiscounts();

            // Get discount details as a formatted string
            String discountDetails = getDiscountDetails();

            // Create a JTextArea to display the final cost message
            String finalCostMessage = String.format("Original Cost: $%.2f\n\n%s\n\nFinal Cost: $%.2f", calculateTotalCost(), discountDetails, totalCost);
            JTextArea finalCostTextArea = new JTextArea(finalCostMessage);
            finalCostTextArea.setEditable(false);

            // Add the final cost JTextArea to the south region of the content pane
            cartFrame.getContentPane().add(finalCostTextArea, BorderLayout.SOUTH);
        }

        // Set the JFrame as visible
        cartFrame.setVisible(true);
    }

    private double calculateTotalCost() {
        return shoppingCart.calculateTotalCost();
    }

    // Calculate the total cost with applied discounts
    private double calculateTotalCostWithDiscounts() {
        // Calculate the original total cost
        double totalCost = calculateTotalCost();

        // Check if it is the user's first purchase and apply the first purchase discount if applicable
        if (isFirstPurchase()) {
            double firstPurchaseDiscount = calculateFirstPurchaseDiscount();
            totalCost -= firstPurchaseDiscount;
        }

        // Check if the user is eligible for a category discount and apply the category discount if applicable
        if (hasCategoryDiscount()) {
            double categoryDiscount = calculateCategoryDiscount();
            totalCost -= categoryDiscount;
        }

        // Return the total cost after applying discounts
        return totalCost;
    }


    // Generate and return a string summarizing the applied discounts
    private String getDiscountDetails() {
        // Create a StringBuilder to store discount details
        StringBuilder discountDetails = new StringBuilder("Discount Details:\n");

        // Check if it is the user's first purchase and include the first purchase discount details if applicable
        if (isFirstPurchase()) {
            double firstPurchaseDiscount = calculateFirstPurchaseDiscount();
            discountDetails.append("First Purchase Discount: 10% (").append(firstPurchaseDiscount).append(")\n");
        }

        // Check if the user is eligible for a category discount and include the category discount details if applicable
        if (hasCategoryDiscount()) {
            double categoryDiscount = calculateCategoryDiscount();
            discountDetails.append("Category Discount (Three or more items of the same category): 20% (").append(categoryDiscount).append(")\n");
        }

        // Return the generated discount details as a string
        return discountDetails.toString();
    }


    private double calculateFirstPurchaseDiscount() {
        return calculateTotalCost() * 0.1;
    }

    private double calculateCategoryDiscount() {
        return calculateTotalCost() * 0.2;
    }

    // Update the shopping cart table by clearing existing rows and adding new rows based on the purchase history
    private void updateShoppingCartTable() {
        // Clear existing rows in the cart table model
        cartTableModel.setRowCount(0);

        // Iterate through the purchase history
        for (Purchase purchase : purchaseHistory) {
            // Find the corresponding product in the shopping manager using the product ID from the purchase
            Product product = shoppingManager.findProductById(purchase.productId);

            // Check if the product is found
            if (product != null) {
                // Add a new row to the cart table with information about the product and purchase
                addRowToCartTable(product, purchase);
            }
        }
    }

    // Get additional information about the product based on its type (Electronics or Clothing)
    private String getProductInformation(Product product) {
        // Check if the product is an instance of Electronics
        if (product instanceof Electronics) {
            // Return a formatted string with brand and warranty information
            return "Brand: " + ((Electronics) product).getBrand() + ", Warranty: " + ((Electronics) product).getWarrantyPeriod();
        }
        // Check if the product is an instance of Clothing
        else if (product instanceof Clothing) {
            // Return a formatted string with color and size information
            return "Color: " + ((Clothing) product).getColor() + ", Size: " + ((Clothing) product).getSize();
        }
        // If the product type is unknown, return an empty string
        else {
            return "";
        }
    }

    // Update the product table based on the selected product type
    private void updateProductTable() {
        // Get the list of products from the shopping manager
        List<Product> productList = shoppingManager.getProductList();
        // Clear existing rows in the table model
        tableModel.setRowCount(0);

        // Iterate through each product in the list
        for (Product product : productList) {
            String category;
            String information;

            // Check the type of the product
            if (product instanceof Electronics) {
                // If it's an Electronics product, set the category and information accordingly
                category = "Electronics";
                information = "Brand: " + ((Electronics) product).getBrand() + ", Warranty: " + ((Electronics) product).getWarrantyPeriod();
            } else if (product instanceof Clothing) {
                // If it's a Clothing product, set the category and information accordingly
                category = "Clothes";
                information = "Color: " + ((Clothing) product).getColor() + ", Size: " + ((Clothing) product).getSize();
            } else {
                // If the type is unknown, set category and information as "Unknown" and an empty string
                category = "Unknown";
                information = "";
            }

            // Check if the product should be displayed based on the selected product type
            if (("All".equals(selectedProductType)) ||
                    (product instanceof Electronics && "Electronics".equals(selectedProductType)) ||
                    (product instanceof Clothing && "Clothes".equals(selectedProductType))) {

                // Get the available quantity of the product
                int availableQuantity = getAvailableQuantity(product);
                // Determine the row color based on available quantity
                Color rowColor = (availableQuantity < 3) ? Color.RED : productTable.getBackground();

                // Create an array with data for each column in the table
                Object[] rowData = {
                        product.getProductID(),
                        product.getProductName(),
                        category,
                        product.getPrice(),
                        information
                };
                // Add the row data to the table model
                tableModel.addRow(rowData);

                // Set the background and foreground colors for the selected row
                int lastRowIndex = tableModel.getRowCount() - 1;
                productTable.setSelectionBackground(rowColor);
                productTable.setSelectionForeground(productTable.getForeground());
            }
        }

        // Set up row sorting for the product table
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        productTable.setRowSorter(sorter);
        productTable.setAutoCreateRowSorter(true);

        // Set preferred column widths and row height for better display
        productTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        productTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        productTable.setRowHeight(80);
    }

    private int getAvailableQuantity(Product product) {
        return product.getAvailableItems();
    }

    /*
      The Purchase class represents a purchase of a specific product.
      It includes information such as the product ID, product type, and quantity.
     */
    private static class Purchase {
        // Product ID of the purchased item
        private String productId;
        // Type or category of the purchased product
        private String productType;
        // Quantity of the purchased product
        private int quantity;

        /*
          Constructs a Purchase object with the specified product ID, product type, and quantity.

          @param productId   The unique identifier of the purchased product.
          @param productType The type or category of the purchased product.
         * @param quantity    The quantity of the purchased product.
         */
        public Purchase(String productId, String productType, int quantity) {
            this.productId = productId;
            this.productType = productType;
            this.quantity = quantity;
        }

        /*
         Gets the type or category of the purchased product.

          @return The product type.
         */
        public String getProductType() {
            return productType;
        }

        /*
          Gets the quantity of the purchased product.

          @return The quantity.
         */
        public int getQuantity() {
            return quantity;
        }
    }

}