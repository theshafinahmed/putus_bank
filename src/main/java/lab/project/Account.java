package lab.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String accountNumber;
    protected String name;
    protected String pin;
    protected double balance;
    protected List<String> transactionHistory;

    public Account(String accountNumber, String name, String pin, double initialBalance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.pin = pin;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
        addTransaction("Account created with initial balance: " + initialBalance);
    }

    public abstract String getAccountType();
    public abstract void applyInterest();

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public boolean validatePin(String pin) {
        return this.pin.equals(pin);
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public double getBalance() {
        return balance;
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            addTransaction("Deposited: " + amount);
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            addTransaction("Withdrew: " + amount);
            return true;
        }
        return false;
    }

    protected final void addTransaction(String transaction) {
        this.transactionHistory.add(transaction);
    }

    public void addTransaction(String message, double amount) {
        this.transactionHistory.add(message + amount);
    }
}
