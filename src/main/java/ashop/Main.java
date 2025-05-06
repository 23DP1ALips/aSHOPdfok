package ashop;

import ashop.models.CartItem;
import ashop.models.Product;
import ashop.models.User;
import ashop.utils.AuthUtils;
import ashop.utils.FileUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = AuthUtils.getCurrentUser();
    private static List<CartItem> currentCart = new ArrayList<>();

    public static void main(String[] args) {
        showMainMenu();
    }

    private static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("\n".repeat(50));
        }
    }
    
    private static void showMainMenu() {
        while (true) {
            clearConsole();
            
            // Main Menu
            System.out.println("╔══════════════════════════════════════════════╗");
            System.out.println("║                 aSHOPdfok MENU               ║");
            System.out.println("╠══════════════════════════════════════════════╣");
            
            if (currentUser == null) {
                // Guest Menu
                System.out.println("║ 1. Login                                     ║");
                System.out.println("║ 2. Login as guest                            ║");
                System.out.println("║ 3. Register                                  ║");
                System.out.println("║ 4. Exit                                      ║");
                System.out.println("╚══════════════════════════════════════════════╝");
                
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                        AuthUtils.login();
                        currentUser = AuthUtils.getCurrentUser();
                        if (currentUser != null) {
                            showMainMenu(); // Go to products for logged users
                        }
                        break;
                    case 2:
                        currentUser = new User("guest", "", 2); // Create guest session
                        showProductListWithOptions();
                        currentUser = null; // Clear guest session
                        break;
                    case 3:
                        AuthUtils.register();
                        break;
                    case 4:
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice!");
                }
            } else {
                System.out.println("║ Welcome, " + String.format("%-36s", currentUser.getUsername()) + "║");
                System.out.println("╠══════════════════════════════════════════════╣");
                System.out.println("║ 1. Browse Products                           ║");
                System.out.println("║ 2. View Cart                                 ║");
                System.out.println("║ 3. Checkout                                  ║");
                if (currentUser.isAdmin()) {
                    System.out.println("║ 4. Admin Panel                               ║");
                }
                System.out.println("║ 0. Logout                                    ║");
                System.out.println("╚══════════════════════════════════════════════╝");
                
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                        showProductListWithOptions();
                        break;
                    case 2:
                        viewCart();
                        break;
                    case 3:
                        checkout();
                        break;
                    case 4:
                        if (currentUser.isAdmin()) {
                            adminPanel();
                        }
                        break;
                    case 0:
                        AuthUtils.logout();
                        currentUser = null;
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            }
        }
    }

    private static void showProductListWithOptions() {
        List<Product> products = FileUtils.readProducts();
        boolean isFiltered = false;
        boolean isSearched = false;
        
        while (true) {
            clearConsole();
            
            // Header
            System.out.println("╔═════════════════════════════════════════════════════════════╗");
            System.out.println("║                       PRODUCT CATALOG                       ║");
            System.out.println("╠═════════════════════════════════════════════════════════════╣");
            
            // Options menu
            if (!(isFiltered || isSearched)){
                System.out.println("║ Options: (S)ort  (F)ilter  (Se)arch  (B)ack                 ║");
            }
            if (isFiltered) {
                System.out.println("║ Options: (S)ort  (F)ilter (C)lear filters  (Se)arch  (B)ack ║");
            }
            if (isSearched) {
                System.out.println("║ Options: (S)ort  (F)ilter (R)eset search  (Se)arch  (B)ack  ║");
            }

            System.out.println("╠═════════════════════════════════════════════════════════════╣");
            
            // Column
            System.out.printf("║ %-4s %-20s %-10s %-8s %-4s  ║%n", 
                             "ID", "  PRODUCT NAME", "  CATEGORY", "    PRICE", "      STOCK");

            // Product listing
            if (products.isEmpty()) {
                System.out.println("╠═════════════════════════════════════════════════════════════╣");
                System.out.println("║                    No products available                    ║");
                System.out.println("╚═════════════════════════════════════════════════════════════╝");
            } else {
                System.out.println("╠══════╦════════════════════╦══════════╦════════════╦═════════╣");
                for (Product product : products) {
                    System.out.printf("║ %-4d ║ %-18s ║ %-8s ║  $%-6.2f   ║   %-5d ║%n",
                        product.getId(),
                        product.getName(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getQuantity());
                }
                System.out.println("╚══════╩════════════════════╩══════════╩════════════╩═════════╝");
            }

            
            // User input
            if (!currentUser.isGuest()) {
                System.out.println("\n  Enter PRODUCT ID to add to cart");
                System.out.println("  Or select an option from the menu above");
            } else {
                System.out.println("\n  Select an option from the menu above");
            }
            System.out.print("\n  Your choice: ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.equals("b")) {
                return;
            } else if (input.equals("s")) {
                products = sortProducts(products);
            } else if (input.equals("f")) {
                List<Product> filtered = filterProducts(products);
                isFiltered = !filtered.equals(FileUtils.readProducts());
                products = filtered;
            } else if (input.equals("c") && isFiltered) {
                products = FileUtils.readProducts();
                isFiltered = false;
                System.out.println("Filters cleared! Press any key to continue...");
                scanner.nextLine();
            } else if (input.equals("se")) {
                List<Product> searched = searchProducts(products);
                isSearched = !searched.equals(FileUtils.readProducts());
                products = searched;
            } else if (input.equals("r") && isSearched) {
                products = FileUtils.readProducts();
                isSearched = false;
                System.out.println("Search reset! Press any key to continue...");
                scanner.nextLine();
            } else if (!currentUser.isGuest()) {
                try {
                    int productId = Integer.parseInt(input);
                    products.stream()
                        .filter(p -> p.getId() == productId)
                        .findFirst()
                        .ifPresentOrElse(
                            Main::addToCart,
                            () -> {
                                System.out.println("Product not found! Press any key...");
                                scanner.nextLine();
                            }
                        );
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Press any key...");
                    scanner.nextLine();
                }
            } else {
                System.out.println("Invalid option! Press any key...");
                scanner.nextLine();
            }
        }
    }

    private static void addToCart(Product product) {
        System.out.print("Enter quantity for " + product.getName() + " (Available: " + product.getQuantity() + "): ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        
        if (quantity <= 0) {
            System.out.println("Quantity must be positive!");
            return;
        }
        
        if (quantity > product.getQuantity()) {
            System.out.println("Not enough stock available!");
            return;
        }
        
        Optional<CartItem> existingItem = currentCart.stream()
            .filter(item -> item.getProduct().getId() == product.getId())
            .findFirst();
        
        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            currentCart.add(new CartItem(product, quantity));
        }
        
        System.out.println(quantity + " " + product.getName() + "(s) added to cart!");
    }

    private static void viewCart() {
        clearConsole();
        if (currentCart.isEmpty()) {
            System.out.println("Your cart is empty!");
            return;
        }
        
        System.out.println("\n=== Your Cart ===");
        System.out.println(String.format("%-4s %-15s %-10s %-10s %-10s", 
            "ID", "Name", "Price", "Qty", "Total"));
        System.out.println(String.format("%-4s %-15s %-10s %-10s %-10s", 
            "--", "----", "-----", "---", "-----"));
        
        double grandTotal = 0;
        for (CartItem item : currentCart) {
            Product p = item.getProduct();
            double itemTotal = item.getTotalPrice();
            grandTotal += itemTotal;
            
            System.out.println(String.format("%-4d %-15s %-10.2f %-10d %-10.2f",
                p.getId(),
                p.getName(),
                p.getPrice(),
                item.getQuantity(),
                itemTotal));
        }
        
        System.out.println("\nGrand Total: $" + String.format("%.2f", grandTotal));
        System.out.println("\n1. Checkout\n2. Continue Shopping\n0. Back");
        System.out.print("Choose option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                checkout();
                break;
            case 2:
                // Continue shopping
                break;
            case 0:
                // Go back
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    private static void checkout() {
        if (currentCart.isEmpty()) {
            System.out.println("Your cart is empty!");
            return;
        }
        
        FileUtils.saveCart(currentUser.getUsername(), currentCart);
        
        List<Product> allProducts = FileUtils.readProducts();
        for (CartItem item : currentCart) {
            allProducts.stream()
                .filter(p -> p.getId() == item.getProduct().getId())
                .findFirst()
                .ifPresent(p -> p.setQuantity(p.getQuantity() - item.getQuantity()));
        }
        FileUtils.writeProducts(allProducts);
        
        System.out.println("\n=== Checkout Complete ===");
        System.out.println("Thank you for your purchase, " + currentUser.getUsername() + "!");
        System.out.println("Total items purchased: " + currentCart.size());
        System.out.println("Total amount: $" + String.format("%.2f", 
            currentCart.stream().mapToDouble(CartItem::getTotalPrice).sum()));
        
        currentCart.clear();
    }

    private static List<Product> sortProducts(List<Product> products) {
        System.out.println("\n=== Sort Options ===");
        System.out.println("1. Name (A-Z)");
        System.out.println("2. Name (Z-A)");
        System.out.println("3. Price (< low to high)");
        System.out.println("4. Price (> high to low)");
        System.out.println("5. Quantity (< low to high)");
        System.out.println("6. Quantity (> high to low)");
        System.out.println("7. Category (A-Z)");
        System.out.println("8. Category (Z-A)");
        System.out.print("Choose sorting option: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                products.sort(Comparator.comparing(Product::getName));
                break;
            case 2:
                products.sort(Comparator.comparing(Product::getName).reversed());
                break;
            case 3:
                products.sort(Comparator.comparing(Product::getPrice));
                break;
            case 4:
                products.sort(Comparator.comparing(Product::getPrice).reversed());
                break;
            case 5:
                products.sort(Comparator.comparing(Product::getQuantity));
                break;
            case 6:
                products.sort(Comparator.comparing(Product::getQuantity).reversed());
                break;
            case 7:
                products.sort(Comparator.comparing(Product::getCategory));
                break;
            case 8:
                products.sort(Comparator.comparing(Product::getCategory).reversed());
                break;
            default:
                System.out.println("Invalid choice. No sorting applied.");
        }
        
        System.out.println("Products sorted successfully!");
        return products;
    }

    private static List<Product> filterProducts(List<Product> products) {
        List<String> categories = products.stream()
            .map(Product::getCategory)
            .distinct()
            .collect(Collectors.toList());
        
        System.out.println("\n=== Filter by Category ===");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i+1) + ". " + categories.get(i));
        }
        System.out.println("0. Clear filters (show all)");
        System.out.print("Choose category to filter (1-" + categories.size() + "): ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice == 0) {
            System.out.println("Showing all products");
            return FileUtils.readProducts();
        } else if (choice > 0 && choice <= categories.size()) {
            String selectedCategory = categories.get(choice-1);
            return products.stream()
                .filter(p -> p.getCategory().equals(selectedCategory))
                .collect(Collectors.toList());
        } else {
            System.out.println("Invalid choice. No filter applied.");
            return products;
        }
    }

    private static List<Product> searchProducts(List<Product> products) {
        System.out.print("\nEnter search term: ");
        String term = scanner.nextLine().toLowerCase();
        
        return products.stream()
            .filter(p -> p.getName().toLowerCase().contains(term) || 
                        p.getCategory().toLowerCase().contains(term))
            .collect(Collectors.toList());
    }

    private static void adminPanel() {
        System.out.println("\n=== Admin Panel ===");
        System.out.println("1. Add New Product");
        System.out.println("2. View All Orders");
        System.out.println("0. Back");
        System.out.print("Choose option: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                addNewProduct();
                break;
            case 2:
                viewAllOrders();
                break;
            case 0:
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    private static void addNewProduct() {
        System.out.println("\n=== Add New Product ===");
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        
        List<Product> products = FileUtils.readProducts();
        int newId = products.stream().mapToInt(Product::getId).max().orElse(0) + 1;
        
        Product newProduct = new Product(newId, name, category, price, quantity);
        FileUtils.addProduct(newProduct);
        System.out.println("Product added successfully!");
    }

    private static void viewAllOrders() {
        // Implementation for viewing all orders
        System.out.println("Order history functionality would go here");
    }
}