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
        
        // Split message
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
    
    public static void displayAsciiArt() {
        String asciiArt1 = """
                     _____ __  ______  ____      ______      __    
              ____ _/ ___// / / / __ \\/ __ \\____/ / __/___  / /__    
             / __ `/\\__ \\/ /_/ / / / / /_/ / __  / /_/ __ \\/ //_/    
            / /_/ /___/ / __  / /_/ / ____/ /_/ / __/ /_/ / ,<       
            \\__,_//____/_/ /_/\\____/_/    \\__,_/_/  \\____/_/|_|      
            """;

        String asciiArt2 = """
                     _____ __  ______  ____      ______      __    
              ____ _/ ___// / / / __ \\/ __ \\____/ / __/___  / /__    
             / __ `/\\__ \\/ /_/ / / / / /_/ / __  / /_/ __ \\/ //_/    
            / /_/ /___/ / __  / /_/ / ____/ /_/ / __/ /_/ / ,<     _
            \\__,_//____/_/ /_/\\____/_/    \\__,_/_/  \\____/_/|_|   (_)   
            """;
        String asciiArt3 = """
                    _____ __  ______  ____      ______      __    
             ____ _/ ___// / / / __ \\/ __ \\____/ / __/___  / /__    
            / __ `/\\__ \\/ /_/ / / / / /_/ / __  / /_/ __ \\/ //_/    
           / /_/ /___/ / __  / /_/ / ____/ /_/ / __/ /_/ / ,<     _    _
           \\__,_//____/_/ /_/\\____/_/    \\__,_/_/  \\____/_/|_|   (_)  (_) 
            """;
        String asciiArt4 = """
                    _____ __  ______  ____      ______      __    
             ____ _/ ___// / / / __ \\/ __ \\____/ / __/___  / /__    
            / __ `/\\__ \\/ /_/ / / / / /_/ / __  / /_/ __ \\/ //_/    
           / /_/ /___/ / __  / /_/ / ____/ /_/ / __/ /_/ / ,<     _    _    _
           \\__,_//____/_/ /_/\\____/_/    \\__,_/_/  \\____/_/|_|   (_)  (_)  (_)
            """;

        System.out.println(asciiArt1);
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clearConsole();
        System.out.println(asciiArt2);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clearConsole();
        System.out.println(asciiArt3);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clearConsole();
        System.out.println(asciiArt4);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clearConsole();
        System.out.println(asciiArt3);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clearConsole();
        System.out.println(asciiArt2);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clearConsole();
        System.out.println(asciiArt3);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clearConsole();
        System.out.println(asciiArt4);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clearConsole();
        System.out.println(asciiArt3);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clearConsole();
        System.out.println(asciiArt2);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clearConsole();
        System.out.println(asciiArt1);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clearConsole();
    }
}