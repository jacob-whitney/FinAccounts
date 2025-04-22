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
import java.util.ArrayList;
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
        System.out.print(printMainMenu());
        while (true) {
            if (getMainMenuSwitch()) {
                System.out.print(printMainMenu());
                continue;
            } else {
                break;
            }
        }
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
     * Instructions and user input to connect to
     * the user's database
     * @return boolean
     */
    public static boolean connectNewDatabase() {
        // Get database credentials from user
        System.out.print("Enter your database server's IP address: ");
        ip = sc.nextLine();
        System.out.print("Enter your MySQL username: ");
        un = sc.nextLine();
        System.out.print("Enter your MySQL password: ");
        pw = sc.nextLine();

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
    public static ArrayList<String> queryDB(String query) {
        String jbdcAddress = "jdbc:mysql://" + ip + ":3306/" + db;
        ArrayList<String> result = new ArrayList<>();

        try(Connection conn = DriverManager.getConnection(jbdcAddress, un, pw);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
        ) {
            while(rs.next()) {
                result.add(rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println("> Could not connect to database");
            System.out.println("Exception caught: " + e);
        }

        return result;
    }

    /**
     * Get number of records in table
     * @return integer
     */
    public static int getTableSize() {
        ArrayList<String> result = queryDB("select count(*) from accounts");
        return Integer.parseInt(result.get(0));
    }

    /**
     * Process for creating account from user inputs
     * @return boolean
     */
    public static boolean createAccount() {
        String name = getValidName(inputValue("name"));
        String type = getValidAcctType(inputValue("type"));
        double balance = getValidBalance(inputValue("balance"));

        String query = "INSERT INTO accounts (name, type, balance) " +
                "VALUES ('" + name + "', '" + type + "', " + balance + ")";
        return updateDB(query);
    }

    /**
     * Return account records as a string
     * @return String
     */
    public static String readAccountsTable() {
        int tableSize = getTableSize();
        StringBuilder acctRecords = new StringBuilder();

        if (tableSize > 0) {
            String query = "SELECT id FROM accounts";
            ArrayList<String> ids = queryDB(query);
            query = "Select name FROM accounts";
            ArrayList<String> names = queryDB(query);
            query = "Select type FROM accounts";
            ArrayList<String> types = queryDB(query);
            query = "Select balance FROM accounts";
            ArrayList<String> balances = queryDB(query);

            acctRecords.append(printAccHeadings());
            for (int i = 0; i < tableSize; i++) {
                acctRecords.append(
                        ids.get(i) + " | " +
                        names.get(i) + " | " +
                        types.get(i) + " | " +
                        balances.get(i) + "\n"
                );
            }
        } else {
            acctRecords.append("\s(Empty List)\n");
        }

        return acctRecords.toString();
    }

    /**
     * Make changes to existing account record in database
     * @return boolean
     */
    public static boolean updateAccount() {
        System.out.println(readAccountsTable());
        System.out.println("Enter the id of the account you would like to update.");
        String id = String.valueOf(getValidId(inputValue("id")));
        System.out.println("Which attribute would you like to update?");
        String attribute = getValidAttribute(inputValue("attribute"));
        System.out.println("What would you like to change the " + attribute + " to?");
        String value = "";

        switch (attribute) {
            case "name":
                value = getValidName(inputValue("name"));
                break;
            case "type":
                value = getValidAcctType(inputValue("type"));
                break;
            case "balance":
                value = String.valueOf(getValidBalance(inputValue("balance")));
                break;
            default:
                System.out.println("> Account could not be updated, try again.");
                return false;
        }

        String query = "UPDATE accounts SET " + attribute + " = '" + value + "' WHERE id = " + id + ";";
        updateDB(query);
        return true;
    }

    /**
     * Delete account by id
     * @return boolean
     */
    public static boolean deleteAccount() {
        System.out.println(readAccountsTable());
        System.out.println("Enter the id of the account you would like to delete.");
        String id = String.valueOf(getValidId(inputValue("id")));

        String query = "DELETE FROM accounts WHERE id = " + id + ";";
        updateDB(query);
        return true;
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
                printHorizontalRule() + "\n" +
                readAccountsTable();
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
        String headings = "ID | Name       | Account Type | Balance\n" +
                printHorizontalRule();
        return headings;
    }

    // User Input Methods
    public static boolean getMainMenuSwitch() {
        String input = sc.nextLine();

        switch (input) {
            case "c":
                System.out.println(printCreatePage());
                createAccount();
                return true;
            case "p":
                System.out.println(printReadPage());
                return true;
            case "u":
                System.out.println(printUpdatePage());
                updateAccount();
                return true;
            case "d":
                System.out.println(printDeletePage());
                deleteAccount();
                return true;
            case "q":
                System.out.println(printQuitPage());
                return false;
            default:
                System.out.println("> Invalid input, please try again");
                return true;
        }
    }

    /**
     * User is instructed to input a specific Account attribute
     * @param field The label for which field will be input
     * @return String
     */
    public static String inputValue(String field) {
        boolean loop = true;
        while (loop) {
            switch (field) {
                case "id":
                    System.out.print("Account ID: ");
                    loop = false;
                    break;
                case "name":
                    System.out.println();
                    System.out.print("Account Name: ");
                    loop = false;
                    break;
                case "type":
                    System.out.println();
                    System.out.print("Is this a spending, saving, or investment account? ");
                    loop = false;
                    break;
                case "balance":
                    System.out.println();
                    System.out.print("Current Balance: ");
                    loop = false;
                    break;
                case "attribute":
                    System.out.println();
                    System.out.print("Enter the name, type, or balance of the account: ");
                    loop = false;
                    break;
                default:
                    System.out.println();
                    System.out.println("Not a valid field, try again");
            }
        }
        return sc.nextLine();
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
     * Returns ID value that has been validated
     * completely
     * @param id String to be checked
     * @return integer
     */
    public static int getValidId(String id) {
        while (true) {
            if (validateId(id)) {
                if (checkIdExists(id)) {
                    break;
                } else {
                    id = inputValue("id");
                }
            } else {
                id = inputValue("id");
            }
        }
        return Integer.parseInt(id);
    }

    /**
     * Check that ID entered is a valid integer
     * @param id integer to be validated
     * @return boolean
     */
    public static boolean validateId(String id) {
        // Check that it's integer
        try {
            int intId = Integer.parseInt(id);
            if (intId > 0) {
                return true;
            } else {
                System.out.println("> Please enter a positive integer");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("> Please enter an integer");
            return false;
        }
    }

    /**
     * Confirms ID exists in table
     * @param id Integer to be checked
     * @return boolean
     */
    public static boolean checkIdExists(String id) {
        int intId = Integer.parseInt(id);
        int tableId;
        int tableSize = getTableSize();
        if (tableSize > 0) {
            ArrayList<String> ids = queryDB("SELECT id FROM accounts");
            for (int i = 0; i < tableSize; i++) {
                tableId = Integer.parseInt(ids.get(i));
                if (tableId == intId) {
                    return true;
                }
            }
        }
        System.out.println("> This ID does not exist, try again");
        return false;
    }

    /**
     * Returns name value that has been validated
     * completely and is ready to be added as a record
     * @param name String to be validated
     * @return valid String
     */
    public static String getValidName(String name) {
        while (true) {
            if (validateName(name)) {
                if (checkDuplicateNames(name)) {
                    break;
                } else {
                    name = inputValue("name");
                }
            } else {
                name = inputValue("name");
            }
        }

        return name;
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
        String tableName = "";
        int tableSize = getTableSize();
        if (tableSize > 0) {
            ArrayList<String> result = queryDB("SELECT name FROM accounts");
            for (int i = 0; i < tableSize; i++) {
                tableName = result.get(i);
                if (tableName.equals(name)) {
                    System.out.println("> The name, " + name + ", already exists, please enter a new one");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns account type value that has been validated
     * completely and is ready to be added as a record
     * @param acctType String to be validated
     * @return valid String
     */
    public static String getValidAcctType(String acctType) {
        while (true) {
            if (validateAcctType(acctType)) {
                break;
            } else {
                acctType = inputValue("type");
            }
        }
        return acctType;
    }

    /**
     * Confirms that account type is one of the valid options
     * @param acctType string to be validated
     * @return boolean
     */
    public static boolean validateAcctType(String acctType) {
        switch (acctType) {
            case "spending":
                return true;
            case "saving":
                return true;
            case "investment":
                return true;
            default:
                System.out.println("> Invalid account type, please try again");
                return false;
        }
    }

    /**
     * Returns balance value that has been validated
     * completely and is ready to be added as a record
     * @param balance String to be validated
     * @return valid Double
     */
    public static double getValidBalance(String balance) {
        while (true) {
            if (validateBalance(balance)) {
                break;
            } else {
                balance = inputValue("balance");
            }
        }
        return Double.parseDouble(balance);
    }

    /**
     * Confirms balance is a valid double
     * @param balance string to be validated as double
     * @return boolean
     */
    public static boolean validateBalance(String balance) {
        try {
            double doubleBalance = Double.parseDouble(balance);
            if (doubleBalance > 0) {
                return true;
            } else {
                System.out.println("> Please enter a valid balance");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("> Please enter a valid number with two decimal places");
            return false;
        }
    }

    /**
     * Returns attribute value that has been validated
     * completely and is ready to be added as a record
     * @param attribute String to be validated
     * @return valid String
     */
    public static String getValidAttribute(String attribute) {
        while (true) {
            if (validateAttribute(attribute)) {
                break;
            } else {
                attribute = inputValue("attribute");
            }
        }
        return attribute;
    }

    /**
     * Confirms that attribute is one of the valid options
     * @param attribute String to be validated
     * @return boolean
     */
    public static boolean validateAttribute(String attribute) {
        switch (attribute) {
            case "name":
                return true;
            case "type":
                return true;
            case "balance":
                return true;
            default:
                System.out.print("> Please enter a valid field");
                return false;

        }
    }
}
