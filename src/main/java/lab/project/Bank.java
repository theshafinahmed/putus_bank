package lab.project;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Bank {
    private List<Account> accounts;
    private static final String DATA_FILE = "bank.dat";

    @SuppressWarnings("unchecked")
    public Bank() {
        this.accounts = new ArrayList<>();
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
                accounts = (List<Account>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading data from file. Starting with a clean slate.");
                this.accounts = new ArrayList<>();
            }
        } else {
            System.out.println("No existing data file found. Starting with a new bank.");
        }
    }

    public void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            System.err.println("Error saving accounts: " + e.getMessage());
        }
    }

    public Account createAccount(String name, String pin, double initialBalance, String accountType, int termMonths, double monthlyInstallment) {
        String accountNumber = String.valueOf(1000 + accounts.size());
        Account newAccount = null;
        switch (accountType.toLowerCase()) {
            case "savings":
                newAccount = new SavingsAccount(accountNumber, name, pin, initialBalance);
                break;
            case "current":
                newAccount = new CurrentAccount(accountNumber, name, pin, initialBalance);
                break;
            case "dps":
                newAccount = new DpsAccount(accountNumber, name, pin, initialBalance, monthlyInstallment);
                break;
            case "fixeddeposit":
                newAccount = new FixedDepositAccount(accountNumber, name, pin, initialBalance, termMonths);
                break;
            default:
                return null;
        }
        accounts.add(newAccount);
        saveAccounts();
        return newAccount;
    }

    public boolean deleteAccount(String accountNumber) {
        Account account = findAccount(accountNumber);
        if (account != null) {
            accounts.remove(account);
            saveAccounts();
            return true;
        }
        return false;
    }

    public List<Account> searchAccounts(String query) {
        return accounts.stream()
                .filter(acc -> acc.getName().toLowerCase().contains(query.toLowerCase()) || acc.getAccountNumber().equals(query))
                .collect(Collectors.toList());
    }

    public void applyInterestToAllAccounts() {
        for (Account account : accounts) {
            account.applyInterest();
        }
        saveAccounts();
    }

    public Account findAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    public Account login(String accountNumber, String pin) {
        Account account = findAccount(accountNumber);
        if (account != null && account.validatePin(pin)) {
            return account;
        }
        return null;
    }

    public boolean transfer(Account fromAccount, String toAccountNumber, double amount) {
        Account toAccount = findAccount(toAccountNumber);
        if (toAccount == null) {
            System.out.println("Recipient account not found.");
            return false;
        }

        if (fromAccount.withdraw(amount)) {
            toAccount.deposit(amount);
            fromAccount.addTransaction("Transferred to " + toAccountNumber + ": ", amount);
            toAccount.addTransaction("Received from " + fromAccount.getAccountNumber() + ": ", amount);
            saveAccounts();
            return true;
        }
        return false;
    }

    public List<Account> getAllAccounts() {
        return accounts;
    }
}
