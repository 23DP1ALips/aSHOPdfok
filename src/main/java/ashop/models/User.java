package ashop.models;

public class User {
    private String username;
    private String password;
    private int userType; // 1 for admin, 0 for regular

    public User(String username, String password, int userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getUserType() {
        return userType;
    }

    public boolean isAdmin() {
        return userType == 1;
    }

    @Override
    public String toString() {
        return username + "," + password + "," + userType;
    }
}