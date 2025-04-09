package ashop.utils;

import ashop.models.Product;
import ashop.models.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static final String USERS_FILE = "src/main/resources/users.csv";
    private static final String PRODUCTS_FILE = "src/main/resources/products.csv";

    // User related operations
    public static List<User> readUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    users.add(new User(parts[0], parts[1], Integer.parseInt(parts[2])));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
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

    public static void registerUser(User newUser) {
        List<User> users = readUsers();
        users.add(newUser);
        writeUsers(users);
    }

    public static User findUserByUsername(String username) {
        List<User> users = readUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    // Produkti asdfafafaf
    public static List<Product> readProducts() {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PRODUCTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    products.add(new Product(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2],
                        Double.parseDouble(parts[3]),
                        Integer.parseInt(parts[4])
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
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

    public static void addProduct(Product product) {
        List<Product> products = readProducts();
        products.add(product);
        writeProducts(products);
    }
}