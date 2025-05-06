package ashop;

import ashop.models.Product;
import ashop.models.User;
import ashop.utils.AuthUtils;
import ashop.utils.FileUtils;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = AuthUtils.getCurrentUser();

    public static void main(String[] args) {
        showMainMenu();
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== Grocery Store ===");
            if (currentUser == null) {
                System.out.println("1. Login");
                System.out.println("2. Login as Guest");
                System.out.println("3. Register");
                System.out.println("4. Exit");
                System.out.print("Choose an option: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                        AuthUtils.login();
                        currentUser = AuthUtils.getCurrentUser();
                        if (currentUser != null) {
                            showProductListWithOptions();
                        }
                        break;
                        case 2:
                        currentUser = new User("guest", "", 2);
                        System.out.println("Logged in as Guest. You can view products but cannot purchase.");
                        showProductListWithOptions(); // This will return to main menu when done
                        currentUser = null; // Ensure guest is logged out after returning
                        break;
                    case 3:
                        AuthUtils.register();
                        break;
                    case 4:
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice.");
                }
            } else {
                System.out.println("Welcome, " + currentUser.getUsername() + "!");
                System.out.println("1. View Products");
                System.out.println("2. View Cart");
                System.out.println("3. Checkout");
                if (currentUser.isAdmin()) {
                    System.out.println("4. Admin Panel");
                }
                System.out.println("0. Logout");
                System.out.print("Choose an option: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                        showProductListWithOptions();
                        break;
                    case 2:
                        if (!currentUser.isGuest()) {
                            viewCart();
                        } else {
                            System.out.println("Guests cannot view cart.");
                        }
                        break;
                    case 3:
                        if (!currentUser.isGuest()) {
                            checkout();
                        } else {
                            System.out.println("Guests cannot checkout.");
                        }
                        break;
                    case 4:
                        if (currentUser.isAdmin()) {
                            adminPanel();
                        } else {
                            System.out.println("Invalid choice!");
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
        
        while (true) {
            // Clear console for better UX (optional)
            System.out.print("\033[H\033[2J");
            System.out.flush();
            
            System.out.println("\n=== Product List ===");
            System.out.println("(S)ort | (F)ilter" + (isFiltered ? " | (C)lear filter" : "") + " | (Search) | (B)ack");
            System.out.println(String.format("%-4s %-15s %-15s %-10s %-10s", 
                "ID", "Name", "Category", "Price", "Quantity"));
            System.out.println(String.format("%-4s %-15s %-15s %-10s %-10s", 
                "--", "----", "--------", "-----", "--------"));
            
            if (products.isEmpty()) {
                System.out.println("No products available.");
            } else {
                for (Product product : products) {
                    System.out.println(String.format("%-4d %-15s %-15s %-10.2f %-10d",
                        product.getId(),
                        product.getName(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getQuantity()));
                }
            }
            
            System.out.print("\nEnter option: ");
            String option = scanner.nextLine().trim().toLowerCase();
            
            if (option.equals("b")) {
                return; // Exit the method completely
            } else if (option.equals("s")) {
                products = sortProducts(products);
            } else if (option.equals("f")) {
                List<Product> filtered = filterProducts(products);
                isFiltered = !filtered.equals(FileUtils.readProducts());
                products = filtered;
            } else if (option.equals("c") && isFiltered) {
                products = FileUtils.readProducts();
                isFiltered = false;
                System.out.println("Filters cleared!");
            } else if (option.equals("search")) {
                products = searchProducts(products);
            } else {
                System.out.println("Invalid option! Press any key to continue...");
                scanner.nextLine();
            }
        }
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

    private static void viewCart() {
        System.out.println("Viewing cart...");
        // Implement cart functionality here
    }

    private static void checkout() {
        System.out.println("Checking out...");
        // Implement checkout functionality here
    }

    private static void adminPanel() {
        System.out.println("Admin panel...");
        // Implement admin functionality here
    }
}