package ashop;

import ashop.models.Product;
import ashop.models.User;
import ashop.utils.AuthUtils;
import ashop.utils.FileUtils;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

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
                        break;
                    case 2:
                        // Login as guest
                        currentUser = new User("guest", "", 2); // Type 2 for guest
                        System.out.println("Logged in as Guest. You can view products but cannot purchase.");
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
                System.out.println("2. Sort Products");
                
                // Shows cart for only registred users
                if (currentUser.getUserType() != 2) {
                    System.out.println("3. View Cart");
                    System.out.println("4. Checkout");
                }
                
                if (currentUser.isAdmin()) {
                    System.out.println("5. Admin Panel (You have admin privileges)");
                }
                System.out.println("0. Logout");
                System.out.print("Choose an option: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                        viewProducts();
                        break;
                    case 2:
                        sortProducts();
                        break;
                    case 3:
                        if (currentUser.getUserType() != 2) {
                            viewCart();
                        } else {
                            System.out.println("Invalid choice!");
                        }
                        break;
                    case 4:
                        if (currentUser.getUserType() != 2) {
                            checkout();
                        } else {
                            System.out.println("Invalid choice!");
                        }
                        break;
                    case 5:
                        if (currentUser.isAdmin()) {
                            addProduct();
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

    private static void viewProducts() {
        List<Product> products = FileUtils.readProducts();
        if (products.isEmpty()) {
            System.out.println("No products available.");
        } else {
            System.out.println("\n=== Product List ===");
            System.out.println("ID\tName\tCategory\tPrice\tQuantity");
            for (Product product : products) {
                System.out.printf("%d\t%s\t%s\t%.2f\t%d%n",
                    product.getId(),
                    product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getQuantity());
            }
        }
    }

    private static void sortProducts() {
        System.out.println("\n=== Sort Products ===");
        System.out.println("1. Sort by Name (A-Z)");
        System.out.println("2. Sort by Price (Low-High)");
        System.out.println("3. Sort by Category");
        System.out.print("Choose sorting option: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        List<Product> products = FileUtils.readProducts();
        
        switch (choice) {
            case 1:
                products.sort(Comparator.comparing(Product::getName));
                break;
            case 2:
                products.sort(Comparator.comparing(Product::getPrice));
                break;
            case 3:
                products.sort(Comparator.comparing(Product::getCategory));
                break;
            default:
                System.out.println("Invalid choice. Defaulting to name sort.");
                products.sort(Comparator.comparing(Product::getName));
        }
        
        System.out.println("\n=== Sorted Products ===");
        System.out.println("ID\tName\tCategory\tPrice\tQuantity");
        for (Product product : products) {
            System.out.printf("%d\t%s\t%s\t%.2f\t%d%n",
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getQuantity());
        }
    }

    private static void viewCart() {
        // Groza pārskate
        System.out.println("Viewing cart...");
    }

    private static void checkout() {
        // Izraksts / apmaksa
        System.out.println("Checking out...");
    }

    private static void addProduct() {
        // pievienošana
        System.out.println("Adding new product...");
    }
}