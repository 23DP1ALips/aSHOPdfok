package ashop.utils;

public class DisplayUtils {
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printBoxedMessage(String message) {
        String border = "╔══════════════════════════════════════════════╗";
        String emptyLine = "║                                            ║";
        
        System.out.println(border);
        System.out.println(emptyLine);
        
        // Split message if too long
        if (message.length() > 40) {
            String[] parts = message.split("(?<=\\G.{40})");
            for (String part : parts) {
                System.out.println("║ " + String.format("%-42s", part) + "║");
            }
        } else {
            System.out.println("║ " + String.format("%-42s", message) + "║");
        }
        
        System.out.println(emptyLine);
        System.out.println("╚══════════════════════════════════════════════╝");
    }

    public static void printSectionHeader(String title) {
        String border = "╔══════════════════════════════════════════════╗";
        System.out.println(border);
        System.out.println("║ " + String.format("%-42s", title) + "║");
        System.out.println("╚══════════════════════════════════════════════╝");
    }
}