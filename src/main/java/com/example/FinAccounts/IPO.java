package com.example.FinAccounts;
/**
 * Advanced Database Development
 * April 23, 2025
 * IPO.java
 * @author Jacob Whitney
 */

// Imports
import java.net.InetAddress;
import java.sql.*;
import java.util.Scanner;

/**
 * Custom methods for managing
 * input, process, output operations
 */
public class IPO {
    private static final Scanner sc = new Scanner(System.in);
    private static String ip;
    private static String un;
    private static String pw;
    private static String db = "finaccts";

    // Processor Methods
    /**
     * Call all functions required to start the program
     */
    public static void getProgramStart() throws SQLException {
        System.out.println(printWelcome());
        connectNewDatabase();
        /*System.out.print(printMainMenu());
        while (true) {
            if (getMainMenuSwitch()) {
                System.out.print(printMainMenu());
                continue;
            } else {
                break;
            }
        }*/
        setValuesInTable();
        System.out.println(getValueFromTable());
    }

    /**
     * Try method used for all database updates
     * @param jbdcAddress Address that should be connected to
     * @param query Query that should be attempted on database
     * @return boolean
     */
    public static boolean tryDbUpdate(String jbdcAddress, String query) {
        try(Connection conn = DriverManager.getConnection(jbdcAddress, un, pw);
            Statement stmt = conn.createStatement();
        ) {
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            System.out.println("> Could not connect to database");
            System.out.println("Exception caught: " + e);
            return false;
        }
    }

    /**
     * Try method used for all database queries
     * @param jbdcAddress Address that should be connected to
     * @param query Query that should be attempted on database
     * @return ResultSet
     */
    public static ResultSet tryDbQuery(String jbdcAddress, String query) {
        try(Connection conn = DriverManager.getConnection(jbdcAddress, un, pw);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
        ) {
            stmt.executeUpdate(query);
            return rs;
        } catch (SQLException e) {
            System.out.println("> Could not connect to database");
            System.out.println("Exception caught: " + e);
            return null;
        }
    }

    /**
     * Instructions and user input to connect to
     * the user's database
     * @return boolean
     */
    public static boolean connectNewDatabase() {
        // Get database credentials from user
        /*System.out.print("Enter your database server's IP address: ");
        ip = sc.nextLine();
        System.out.print("Enter your MySQL username: ");
        un = sc.nextLine();
        System.out.print("Enter your MySQL password: ");
        pw = sc.nextLine();*/

        // Temp shortcut
        System.out.println("!** Temporarily skipping user entered creds **!\n");
        ip = "172.24.161.23";
        un = "jacobdb";
        pw = "3U8#1:ysCT.q_!!tzSei";
        // End temp shortcut

        // Connect to database using user inputs
        if (validateIpAddress(ip) && validateUsername(un)) {
            String jbdcAddress = "jdbc:mysql://" + ip + ":3306";
            String query = "CREATE DATABASE IF NOT EXISTS " + db;
            if (tryDbUpdate(jbdcAddress, query)) {
                query = """
                        CREATE TABLE IF NOT EXISTS accounts (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    name VARCHAR(50) NOT NULL,
                                    type VARCHAR(50),
                                    balance DOUBLE
                                );""";
                if (updateDB(query)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Send update query to database
     * @param query String containing query to update database
     * @return boolean
     */
    public static boolean updateDB(String query) {
        String jbdcAddress = "jdbc:mysql://" + ip + ":3306/" + db;
        if (tryDbUpdate(jbdcAddress, query)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Send update query to database
     * @param query String containing query to update database
     * @return result
     */
    public static String queryDB(String query) {
        String jbdcAddress = "jdbc:mysql://" + ip + ":3306/" + db;
        ResultSet rs = tryDbQuery(jbdcAddress, query);
        StringBuilder result = new StringBuilder();
        while(true) {
            try {
                assert rs != null;
                if (!rs.next()) {
                    break;
                } else {
                    result.append(rs.getInt("id")).append("\n");
                    result.append(rs.getString("name")).append("\n");
                    result.append(rs.getString("type")).append("\n");
                    result.append(rs.getString("balance")).append("\n");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return result.toString(); // Likely to change to return results from query (string or other)
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
     * Print welcome message and instructions
     */
    public static String printWelcome() {
        String welcome = printHorizontalRule() +
                "Welcome to your Financial Accounts application!\n" +
                printHorizontalRule() +
                "To get started, first enter credentials to\n" +
                "connect to your local MySQL database\n" +
                printHorizontalRule();
        return welcome;
    }

    /**
     * Prints main menu with instructions
     * @return String
     */
    public static String printMainMenu() {
        String menu = "\n\n" +
                printHorizontalRule() +
                "\s** MENU **\n" +
                printHorizontalRule() +
                "c - Create an account\n" +
                "p - Print all accounts' details\n" +
                "u - Update an account\n" +
                "d - Delete an account\n" +
                "q - Quit\n" +
                printHorizontalRule() +
                "\nEnter the letter of the option you want to use: ";

        return menu;
    }

    /**
     * Print heading and instructions for create page
     * @return page
     */
    public static String printCreatePage() {
        String page = "\n\n" +
                printHorizontalRule() +
                "\s** CREATE ACCOUNT **\n" +
                printHorizontalRule() +
                "\nBegin by entering the details of the account.\n";
        return page;
    }

    /**
     * Print heading and instructions for read page
     * @return page
     */
    public static String printReadPage() {
        String page = "\n\n" +
                printHorizontalRule() +
                "\s** FINANCIAL ACCOUNTS **\n" +
                printHorizontalRule() +
                printAccHeadings(); // Need to add query of values to be printed
        return page;
    }

    /**
     * Print heading and instructions for update page
     * @return page
     */
    public static String printUpdatePage() {
        String page = "\n\n" +
                printHorizontalRule() +
                "\s** UPDATE ACCOUNT **\n" +
                printHorizontalRule() +
                "\n";
        return page;
    }

    /**
     * Print heading and instructions for delete page
     * @return page
     */
    public static String printDeletePage() {
        String page = "\n\n" +
                printHorizontalRule() +
                "\s** DELETE ACCOUNT **\n" +
                printHorizontalRule() +
                "\n";
        return page;
    }

    /**
     * Print heading and instructions for quit page
     * @return page
     */
    public static String printQuitPage() {
        String page = "\n\n" +
                printHorizontalRule() +
                "\s** EXITING FINANCIAL ACCOUNTS **\n" +
                printHorizontalRule() +
                "\nGoodbye!\n";
        return page;
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
    public static boolean getMainMenuSwitch() {
        String input = sc.nextLine();

        switch (input) {
            case "c":
                System.out.println(printCreatePage());
                // Functions for Create Account menu and interactions
                return true;
            case "p":
                System.out.println(printReadPage());
                return true;
            case "u":
                System.out.println(printUpdatePage());
                return true;
            case "d":
                System.out.println(printDeletePage());
                return true;
            case "q":
                System.out.println(printQuitPage());
                return false;
            default:
                System.out.println("> Invalid input, please try again");
                return true;
        }
    }

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

    /**
     * Confirms that name is not too short or too long
     * @param name string to be validated
     * @return boolean
     */
    public static boolean validateName(String name) {
        if (name.length() < 2) {
            System.out.println("> Please enter a name with 2 characters or more");
            return false;
        } else if (name.length() > 50) {
            System.out.println("> Please enter a name with 50 characters or less");
            return false;
        }
        return true;
    }

    /**
     * Check if name already exists in accounts table
     * @param name string to be checked against names in accounts table
     * @return boolean
     */
    public static boolean checkDuplicateNames(String name) {
        /*String listName = "";
        int tableSize = list.getListSize(); // SELECT COUNT(*)
        if (listSize > 0) {
            for (int i = 0; i < listSize; i++) {
                listName = list.getCharacter(i).getName();
                if (listName.equals(name)) {
                    System.out.println("> The name, " + name + ", already exists, please enter a new one");
                    return false;
                }
            }
        }*/
        return true;
    }

    public static boolean setValuesInTable() {
        String query = "INSERT INTO accounts (name, type, balance) " +
                "VALUES " +
                "('groceries', 'spending', 100.25)," +
                "('Spain trip', 'saving', 583.12)," +
                "('eat out', 'spending', 75);";
        if (updateDB(query)) {
            return true;
        } else {
            return false;
        }
    }
    public static String getValueFromTable() {
        String query = "SELECT * FROM accounts";
        String result = queryDB(query);
        return result;
    }

}
