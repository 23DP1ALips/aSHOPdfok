package ashop;

import ashop.models.User;
import ashop.utils.AuthUtils;
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
                System.out.println("2. Register");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                        AuthUtils.login();
                        currentUser = AuthUtils.getCurrentUser();
                        break;
                    case 2:
                        AuthUtils.register();
                        break;
                    case 3:
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice.");
                }
            } else {
                System.out.println("Welcome, " + currentUser.getUsername() + "!");
                System.out.println("1. View Products");
                System.out.println("2. Sort Products");
                System.out.println("3. View Cart");
                System.out.println("4. Checkout");
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
                        viewCart();
                        break;
                    case 4:
                        checkout();
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
        // Produktu pārskate
        System.out.println("Viewing products...");
    }

    private static void sortProducts() {
        // Produktu sortēšana
        System.out.println("Sorting products...");
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