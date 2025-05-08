package ashop.utils;

import ashop.models.CartItem;
import ashop.models.Product;
import ashop.models.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    // Update file paths to be relative to project root
    private static final String USERS_FILE = "src/main/resources/users.csv";
    private static final String PRODUCTS_FILE = "src/main/resources/products.csv";
    private static final String CARTS_FILE = "src/main/resources/carts.csv";

    // User operations
    public static List<User> readUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(USERS_FILE);
        
        try {
            // Create file and directory if they don't exist
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                // Add default admin user
                users.add(new User("admin", "admin123", 1));
                writeUsers(users);
                return users;
            }

            // Read existing users
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        users.add(new User(parts[0], parts[1], Integer.parseInt(parts[2])));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users file: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    public static void registerUser(User newUser) {
        try {
            List<User> users = readUsers();
            users.add(newUser);
            writeUsers(users);
        } catch (Exception e) {
            System.err.println("Error registering user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void writeUsers(List<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                bw.write(user.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCartsFilePath() {
        return CARTS_FILE;
    }

    // Keep only one findUserByUsername method
    public static User findUserByUsername(String username) {
        try {
            List<User> users = readUsers();
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    return user;
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding user: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Product operations
    public static List<Product> readProducts() {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PRODUCTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String type = parts.length > 5 ? parts[5] : "unit";
                    products.add(new Product(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2],
                        Double.parseDouble(parts[3]),
                        Double.parseDouble(parts[4]),
                        type
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

    public static void addProduct(Product newProduct) {
        try {
            List<Product> products = readProducts();
            products.add(newProduct);
            writeProducts(products);
        } catch (Exception e) {
            System.err.println("Error adding product: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void writeProducts(List<Product> products) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PRODUCTS_FILE))) {
            for (Product product : products) {
                bw.write(product.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Cart operations
    public static void saveCart(String username, List<CartItem> cart) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CARTS_FILE, true))) {
            for (CartItem item : cart) {
                bw.write(username + "," + item.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<CartItem> loadCart(String username) {
        List<CartItem> cart = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CARTS_FILE))) {
            String line;
            List<Product> allProducts = readProducts();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].equals(username)) {
                    int productId = Integer.parseInt(parts[1]);
                    double quantity = Double.parseDouble(parts[2]);
                    allProducts.stream()
                        .filter(p -> p.getId() == productId)
                        .findFirst()
                        .ifPresent(p -> cart.add(new CartItem(p, quantity)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cart;
    }
}