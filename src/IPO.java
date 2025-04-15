/**
 * Advanced Database Development
 * April 23, 2025
 * IPO.java
 * @author Jacob Whitney
 */

// Imports
import java.io.Console;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Custom methods for managing
 * input, process, output operations
 */
public class IPO {
    private static final Scanner sc = new Scanner(System.in);
    private static final Console console = System.console();

    // Processor Methods
    public static void getProgramStart() {

    }

    /**
     * Instructions and user input to connect to
     * the user's database
     * @return boolean
     */
    public static boolean connectNewDatabase() {
        // Check that program is run in a terminal
        if (console == null) {
            System.out.println("> No console available. Run this from a terminal");
            return false;
        } else {
            // Get database credentials from user
            System.out.print("Enter your database server's IP address: ");
            String ip = sc.nextLine();
            System.out.print("Enter your MySQL username: ");
            String username = sc.nextLine();
            System.out.print("Enter your MySQL password: ");
            char[] passwordChar = console.readPassword();
            String password = new String(passwordChar);

            // Clear sensitive data
            java.util.Arrays.fill(passwordChar, ' ');

            // Connect to database using user inputs
            if (validateIpAddress(ip) && validateUsername(username)) {
                String jbdcAddress = "jdbc:mysql://" + ip + ":3306";
                try (
                        Connection conn = DriverManager.getConnection(jbdcAddress, username, password);
                        Statement stmt = conn.createStatement();
                ) {
                    stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS finaccts;");
                    return true;
                } catch (SQLException e) {
                    System.out.println("> Could not connect to database");
                    System.out.println("Exception caught: " + e);
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    // Print Methods
    /**
     * Print line of dashes for use in menus and
     * instructions
     * @return String
     */
    public static String printHorizontalRule() {
        return "----------------------------------------------------------------\n";
    }

    /**
     * Prints main menu with instructions
     * @return String
     */
    public static String printMainMenu() {
        String menu = printHorizontalRule();
        menu = menu + "Menu\n" +
                printHorizontalRule() +
                "c - Create an account\n" +
                "p - Print all accounts' details\n" +
                "u - Update an account\n" +
                "d - Delete an account\n" +
                "q - Quit\n\n" +
                printHorizontalRule() +
                "Enter the letter of the option you want to use: ";

        return menu;
    }

    /**
     * Print headings for table of accounts
     * @return headings
     */
    public static String printAccHeadings() {
        String headings = printHorizontalRule() +
                "ID    | Name           | Account Type  | Balance\n" +
                printHorizontalRule();
        return headings;
    }

    // User Input Methods


    // Validation Methods

    /**
     * Check that IP Address is valid for connecting
     * @param ipAddress the address to be validated
     * @return boolean
     */
    public static boolean validateIpAddress(String ipAddress) {
        try {
            InetAddress.getByName(ipAddress);
            return true;
        } catch (Exception e) {
            System.out.println("> IP address, " + ipAddress + ", is invalid");
            return false;
        }
    }

    /**
     * Check that username is valid for MySQL
     * @param username the string to be validated
     * @return boolean
     */
    public static boolean validateUsername(String username) {
        String regex = "[a-zA-Z0-9_]{1,32}$";
        if (username.matches(regex)) {
            return true;
        } else {
            System.out.println("> Username, " + username + ", is invalid");
            return false;
        }
    }

}
