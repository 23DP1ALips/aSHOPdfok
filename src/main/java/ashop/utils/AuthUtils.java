package ashop.utils;

import ashop.Main;
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

    public static boolean isGuest() {
        return currentUser != null && currentUser.isGuest();
    }

    public static void login(Runnable clearFunction) {
        clearFunction.run();
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║                     LOGIN                    ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║                                              ║");
        System.out.print("║ Username: ");
        String username = scanner.nextLine();
        System.out.println("║                                              ║");
        System.out.print("║ Password: ");
        String password = scanner.nextLine();
        System.out.println("║                                              ║");
        System.out.println("╚══════════════════════════════════════════════╝");
    
        User user = FileUtils.findUserByUsername(username);
        
        if (user == null) {
            clearFunction.run();
            System.out.println("╔══════════════════════════════════════════════╗");
            System.out.println("║          User does not exist!                ║");
            System.out.println("╚══════════════════════════════════════════════╝");
            System.out.println("Press any key to continue...");
            scanner.nextLine();
            return;
        }
    
        if (!user.getPassword().equals(password)) {
            clearFunction.run();
            System.out.println("╔══════════════════════════════════════════════╗");
            System.out.println("║          Incorrect password!                 ║");
            System.out.println("╚══════════════════════════════════════════════╝");
            System.out.println("Press any key to continue...");
            scanner.nextLine();
            return;
        }
    
        currentUser = user;
        clearFunction.run();
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║ Welcome to aSHOPdfok, " + String.format("%-23s", user.getUsername()) + "║");
        System.out.println("║ Logged in as " + String.format("%-32s", (user.isAdmin() ? "Admin" : "Regular User")) + "║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println("Press any key to continue to menu...");
        scanner.nextLine();
    }

    public static void register() {
        DisplayUtils.clearConsole();
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║                   REGISTER                   ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║                                              ║");
        System.out.print("║ Username: ");
        String username = scanner.nextLine();
        System.out.println("║                                              ║");
        System.out.print("║ Password: ");
        String password = scanner.nextLine();
        System.out.println("║                                              ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        
        if (FileUtils.findUserByUsername(username) != null) {
            System.out.println("╔══════════════════════════════════════════════╗");
            System.out.println("║          Username already exists!            ║");
            System.out.println("╚══════════════════════════════════════════════╝");
            System.out.println("Press any key to continue...");
            scanner.nextLine();
            return;
        }
        
        if (password.length() < 4) {
            System.out.println("╔══════════════════════════════════════════════╗");
            System.out.println("║   Password must be at least 4 characters!    ║");
            System.out.println("╚══════════════════════════════════════════════╝");
            System.out.println("Press any key to continue...");
            scanner.nextLine();
            return;
        }
        
        int userType = FileUtils.readUsers().isEmpty() ? 1 : 0;
        User newUser = new User(username, password, userType);
        FileUtils.registerUser(newUser);
        
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║           Registration successful!           ║");
        System.out.println("║              You can now login               ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println("Press any key to continue...");
        scanner.nextLine();
    }

    public static void logout() {
        DisplayUtils.clearConsole();
        currentUser = null;
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║           Logged out successfully!           ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println("Press any key to continue...");
        scanner.nextLine();
    }
}