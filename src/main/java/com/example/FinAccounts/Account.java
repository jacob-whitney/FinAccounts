package com.example.FinAccounts;
/**
 * Advanced Database Development
 * April 23, 2025
 * Account.java
 * @author Jacob Whitney
 */

/**
 * Contains attributes and methods for
 * creating and updating Account objects
 */
public class Account {
    // Attributes
    private int id;
    private String name;
    private String type;
    private double balance;

    // Constructors
    /**
     * Initialize an account object with
     * all attributes passed as parameters
     * @param id Unique identifying integer
     * @param name Unique string to identify account
     * @param type What kind of account it is
     * @param balance Current dollar balance of account
     */
    public Account(int id, String name, String type, double balance) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.balance = balance;
    }

    // Getters and Setters
    /**
     * ID getter
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * ID setter
     * @param id Unique identifying integer
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Name getter
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Name setter
     * @param name Unique string to identify account
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Type getter
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Type setter
     * @param type What kind of account it is
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Balance getter
     * @return balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Balance setter
     * @param balance Current dollar balance of account
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
