package ashop;

import ashop.models.CartItem;
import ashop.models.Product;
import ashop.models.User;
import ashop.utils.AuthUtils;
import ashop.utils.DisplayUtils;
import ashop.utils.FileUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    private static void showMainMenu() {
        while (true) {
            clearConsole();
            
            System.out.println("╔══════════════════════════════════════════════╗");
            System.out.println("║                 aSHOPdfok MENU               ║");
            System.out.println("╠══════════════════════════════════════════════╣");
            
            if (currentUser == null) {
                System.out.println("║ 1. Login                                     ║");
                System.out.println("║ 2. Login as guest                            ║");
                System.out.println("║ 3. Register                                  ║");
                System.out.println("║ 4. Exit                                      ║");
                System.out.println("╚══════════════════════════════════════════════╝");
                
                System.out.print("Your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                        AuthUtils.login(Main::clearConsole);
                        currentUser = AuthUtils.getCurrentUser();
                        if (currentUser != null) {
                            continue; // Go back to show the appropriate menu
                        }
                        break;
                    case 2:
                        currentUser = new User("guest", "", 2);
                        showProductListWithOptions();
                        currentUser = null;
                        break;
                    case 3:
                        AuthUtils.register();
                        break;
                    case 4:
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice!");
                        scanner.nextLine();
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
                
                System.out.print("Your choice: ");
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
                        scanner.nextLine();
                }
            }
        }
    }

    private static void showProductListWithOptions() {
        List<Product> products = FileUtils.readProducts();
        boolean isFiltered = false;
        boolean isSearched = false;
        
        while (true) {
            DisplayUtils.clearConsole();
            
            System.out.println("╔═══════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                              PRODUCT CATALOG                                  ║");
            System.out.println("╠═══════════════════════════════════════════════════════════════════════════════╣");
            
            String options = "Options: " + 
                (!(isFiltered || isSearched) ? "(S)ort  (F)ilter  (Se)arch  (B)ack" :
                isFiltered ? "(S)ort  (F)ilter (C)lear filters  (Se)arch  (B)ack" :
                "(S)ort  (F)ilter (R)eset search  (Se)arch  (B)ack");
    
            System.out.println("║ " + String.format("%-78s", options) + "║");
            System.out.println("╠══════╦══════════════════════╦════════════╦════════════╦═══════════════════════╣");
            System.out.printf("║ %-4s ║ %-20s ║ %-10s ║ %-10s ║ %-21s ║%n", 
                "ID", "PRODUCT NAME", "CATEGORY", "PRICE", "STOCK");
            if (!products.isEmpty()) {System.out.println("╠══════╬══════════════════════╬════════════╬════════════╬═══════════════════════╣");}
            
            if (products.isEmpty()) {
                System.out.println("╠══════╩══════════════════════╩════════════╩════════════╩═══════════════════════╣");
                System.out.println("║                              No products available!                           ║");
                System.out.println("╚═══════════════════════════════════════════════════════════════════════════════╝");
            } else {
                for (Product product : products) {
                    String name = product.getName();
                    String category = product.getCategory();
                    String price = product.getFormattedPrice();
                    String stock = product.getFormattedQuantity();
                    
                    if (name.length() > 20) name = name.substring(0, 17) + "...";
                    if (category.length() > 10) category = category.substring(0, 7) + "...";
                    if (stock.length() > 21) stock = stock.substring(0, 18) + "...";
                    
                    System.out.printf("║ %-4d ║ %-20s ║ %-10s ║ %-10s ║ %-21s ║%n",
                        product.getId(), name, category, price, stock);
                }
            }
            if (!products.isEmpty()) {System.out.println("╚══════╩══════════════════════╩════════════╩════════════╩═══════════════════════╝");}
            
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
                    DisplayUtils.printBoxedMessage("Invalid input! Press any key to retry.");
                    scanner.nextLine();
                }
            } else {
                System.out.println("╔══════════════════════════════════════════════╗");
                System.out.println("║       Invalid option! Press any key...       ║");
                System.out.println("╚══════════════════════════════════════════════╝");
                scanner.nextLine();
            }
        }
    }

    private static void addToCart(Product product) {
        System.out.print("Enter " + (product.getType().equals("weight") ? "weight (kg)" : "quantity") + 
                        " for " + product.getName() + " (" + product.getFormattedQuantity() + "): ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        
        if (amount <= 0) {
            DisplayUtils.printBoxedMessage("Amount should be positive! Press any key to retry...");
            scanner.nextLine();
            return;
        }
        
        if (amount > product.getQuantity()) {
            System.out.println("╔══════════════════════════════════════════════╗");
            System.out.println("║ Not enough stock available!                  ║");
            System.out.println("║ Available: " + String.format("%-32s", product.getFormattedQuantity()) + "║");
            System.out.println("╚══════════════════════════════════════════════╝");
            System.out.println("Press any key to continue...");
            scanner.nextLine();
            return;
        }
        
        int quantityToAdd = product.getType().equals("weight") ? (int) Math.ceil(amount) : (int) amount;
        
        Optional<CartItem> existingItem = currentCart.stream()
            .filter(item -> item.getProduct().getId() == product.getId())
            .findFirst();
        
        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantityToAdd);
        } else {
            currentCart.add(new CartItem(product, quantityToAdd));
        }
        
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.printf("║ Added: %-5.2f %-3s of %-3s to cart!   ║\n",
            amount,
            (product.getType().equals("weight") ? "kg" : "units"),
            product.getName());
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println("Press any key to continue...");
        scanner.nextLine();
    }

    private static void checkout() {
        clearConsole();
        if (currentCart.isEmpty()) {
            System.out.println("╔══════════════════════════════════════════════════╗");
            System.out.println("║   Your cart is empty! Press any key to continue  ║");
            System.out.println("╚══════════════════════════════════════════════════╝");
            scanner.nextLine();
            return;
        }
        
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║           Confirm your order?                ║");
        System.out.println("║ 1. Yes, proceed with checkout                ║");
        System.out.println("║ 2. No, return to cart                        ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.print("Your choice: ");
        int confirm = scanner.nextInt();
        scanner.nextLine();
        
        if (confirm != 1) {
            return;
        }
        
        try {
            FileUtils.saveCart(currentUser.getUsername(), currentCart);
            
            List<Product> allProducts = FileUtils.readProducts();
            for (CartItem item : currentCart) {
                allProducts.stream()
                    .filter(p -> p.getId() == item.getProduct().getId())
                    .findFirst()
                    .ifPresent(p -> p.setQuantity(p.getQuantity() - item.getQuantity()));
            }
            FileUtils.writeProducts(allProducts);
            
            clearConsole();
            System.out.println("╔══════════════════════════════════════════════╗");
            System.out.println("║           CHECKOUT COMPLETE!                 ║");
            System.out.println("╠══════════════════════════════════════════════╣");
            System.out.println("║ Thank you for your purchase, " + String.format("%-15s", currentUser.getUsername()) + " ║");
            System.out.println("║ Total items purchased: " + String.format("%-22d", currentCart.size()) + "║");
            System.out.printf("║ Total amount: $%-28.2f  ║\n", 
                currentCart.stream().mapToDouble(CartItem::getTotalPrice).sum());
            System.out.println("╚══════════════════════════════════════════════╝");
            
            currentCart.clear();
            System.out.println("Press any key to continue...");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("╔══════════════════════════════════════════════╗");
            System.out.println("║   Error during checkout! Please try again.   ║");
            System.out.println("╚══════════════════════════════════════════════╝");
            System.out.println("Press any key to continue...");
            scanner.nextLine();
        }
    }

    private static List<Product> sortProducts(List<Product> products) {
        clearConsole();
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
        clearConsole();
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

    private static void viewCart() {
        clearConsole();
        
        if (currentCart.isEmpty()) {
            System.out.println("╔══════════════════════════════════════════════╗");
            System.out.println("║             YOUR CART IS EMPTY               ║");
            System.out.println("╠══════════════════════════════════════════════╣");
            System.out.println("║                                              ║");
            System.out.println("║     Add some products to get started!        ║");
            System.out.println("║                                              ║");
            System.out.println("╚══════════════════════════════════════════════╝");
            System.out.println("Press any key to continue...");
            scanner.nextLine();
            return;
        }
    
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                       YOUR CART                           ║");
        System.out.println("╠══════╦══════════════════════╦══════════╦══════════╦═══════╣");
        System.out.println("║  ID  ║      PRODUCT         ║  PRICE   ║  QTY     ║ TOTAL ║");
        System.out.println("╠══════╬══════════════════════╬══════════╬══════════╬═══════╣");
    
        double grandTotal = 0;
        for (CartItem item : currentCart) {
            Product p = item.getProduct();
            double itemTotal = item.getTotalPrice();
            grandTotal += itemTotal;
            
            System.out.printf("║ %4d ║ %-20s ║ %8.2f ║ %-8s ║ %5.2f ║\n",
                p.getId(),
                p.getName().length() > 20 ? p.getName().substring(0, 17) + "..." : p.getName(),
                p.getPrice(),
                item.getFormattedQuantity(),
                itemTotal);
        }
    
        System.out.println("╠══════╩══════════════════════╩══════════╩══════════╩═══════╣");
        System.out.printf("║ %-47s %8.2f  ║%n", "GRAND TOTAL:", grandTotal);
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
    
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║ 1. Checkout                                  ║");
        System.out.println("║ 2. Continue Shopping                         ║");
        System.out.println("║ 0. Back to Main Menu                         ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.print("Your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                checkout();
                break;
            case 2:
                showProductListWithOptions();
                break;
            case 0:
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    private static void viewAllOrders() {
        clearConsole();
        System.out.println("╔═════════════════════════════════════════════════════════════╗");
        System.out.println("║                    ALL ORDERS HISTORY                       ║");
        System.out.println("╚═════════════════════════════════════════════════════════════╝");
    
        try (BufferedReader br = new BufferedReader(new FileReader(FileUtils.getCartsFilePath()))) {
            String line;
            String currentUser = null;
            double userTotal = 0;
            int orderCount = 0;
    
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String username = parts[0];
                    int productId = Integer.parseInt(parts[1]);
                    double quantity = Double.parseDouble(parts[2]);
    
                    if (!username.equals(currentUser)) {
                        if (currentUser != null) {
                            System.out.println("╠═════════════════════════════════════════════════════════════╣");
                            System.out.printf("║ %-45s %13.2f ║%n", "Total for " + currentUser + ":", userTotal);
                            System.out.println("╚═════════════════════════════════════════════════════════════╝");
                            System.out.println();
                        }
                        currentUser = username;
                        userTotal = 0;
                        orderCount++;
                        System.out.println("╔═════════════════════════════════════════════════════════════╗");
                        System.out.printf("║ Order #%-2d for %-45s ║%n", orderCount, username);
                        System.out.println("╠══════╦════════════════════╦══════════╦══════════╦═══════════╣");
                        System.out.printf("║ %-4s ║ %-18s ║ %-8s ║ %-8s ║ %-9s ║%n", 
                                        "ID", "Product", "Type", "Qty", "Price");
                        System.out.println("╠══════╬════════════════════╬══════════╬══════════╬═══════════╣");
                    }
    
                    Product product = FileUtils.readProducts().stream()
                        .filter(p -> p.getId() == productId)
                        .findFirst()
                        .orElse(null);
    
                    if (product != null) {
                        double itemTotal = product.getPrice() * quantity;
                        userTotal += itemTotal;
                        System.out.printf("║ %-4d ║ %-18s ║ %-8s ║ %-8.2f ║ $%-8.2f ║%n",
                            product.getId(),
                            product.getName(),
                            product.getType(),
                            quantity,
                            itemTotal);
                    }
                }
            }
    
            if (currentUser != null) {
                System.out.println("╠═════════════════════════════════════════════════════════════╣");
                System.out.printf("║ %-45s %13.2f ║%n", "Total for " + currentUser + ":", userTotal);
                System.out.println("╚═════════════════════════════════════════════════════════════╝");
            } else {
                System.out.println("║                 No orders found in system                   ║");
                System.out.println("╚═════════════════════════════════════════════════════════════╝");
            }
        } catch (IOException e) {
            System.out.println("║                Error reading order history!                 ║");
            System.out.println("╚═════════════════════════════════════════════════════════════╝");
        }
    
        System.out.println("\nPress any key to return to admin panel...");
        scanner.nextLine();
        adminPanel(); // Return to admin panel instead of menu
    }

    private static List<Product> searchProducts(List<Product> products) {
        clearConsole();
        System.out.print("\nEnter search term: ");
        String term = scanner.nextLine().toLowerCase();
        
        return products.stream()
            .filter(p -> p.getName().toLowerCase().contains(term) || 
                        p.getCategory().toLowerCase().contains(term))
            .collect(Collectors.toList());
    }

    // Change from private to public
public static void adminPanel() {
    clearConsole();
    System.out.println("╔══════════════════════════════════════════════╗");
    System.out.println("║                 ADMIN PANEL                  ║");
    System.out.println("╠══════════════════════════════════════════════╣");
    System.out.println("║ 1. Add New Product                           ║");
    System.out.println("║ 2. View All Orders                           ║");
    System.out.println("║ 0. Back                                      ║");
    System.out.println("╚══════════════════════════════════════════════╝");
    
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
        double quantity = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.print("Is this product sold by weight? (y/n): ");
        String typeInput = scanner.nextLine().toLowerCase();
        String type = typeInput.startsWith("y") ? "weight" : "unit";
        
        List<Product> products = FileUtils.readProducts();
        int newId = products.stream().mapToInt(Product::getId).max().orElse(0) + 1;
        
        Product newProduct = new Product(newId, name, category, price, quantity, type);
        FileUtils.addProduct(newProduct);
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║         Product added successfully!          ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println("Press any key to continue...");
        scanner.nextLine();
    }
}