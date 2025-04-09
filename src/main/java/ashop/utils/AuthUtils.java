package ashop.utils;

import ashop.models.User;
import java.util.Scanner;

public class AuthUtils {
    private static User currentUser = null;
    private static Scanner scanner = new Scanner(System.in);

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    public static void login() {
        System.out.println("=== Login ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = FileUtils.findUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            System.out.println("Login successful! Welcome, " + username + "!");
            System.out.println("You are logged in as " + (user.isAdmin() ? "Admin" : "Regular User"));
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    public static void register() {
        System.out.println("=== Register ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        
        if (FileUtils.findUserByUsername(username) != null) {
            System.out.println("Username already exists.");
            return;
        }

        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        // Pirmais lietotājs ir admins (1), parējie parasti (0)
        int userType = FileUtils.readUsers().isEmpty() ? 1 : 0;
        
        User newUser = new User(username, password, userType);
        FileUtils.registerUser(newUser);
        System.out.println("Registration successful! You can now login.");
    }

    public static void logout() {
        currentUser = null;
        System.out.println("Logged out successfully.");
    }
}