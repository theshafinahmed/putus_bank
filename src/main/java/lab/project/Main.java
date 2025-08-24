package lab.project;

import java.util.Scanner;

public class Main {
    private static Bank bank = new Bank();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nWelcome to PutusBank CLI");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Admin Login");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createAccount();
                    break;
                case "2":
                    login();
                    break;
                case "3":
                    adminLogin();
                    break;
                case "4":
                    System.out.println("Thank you for using PutusBank. Goodbye!");
                    bank.saveAccounts();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void createAccount() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Create a 4-digit PIN: ");
        String pin = scanner.nextLine();
        if (!pin.matches("\\d{4}")) {
            System.out.println("Invalid PIN format. Please enter a 4-digit number.");
            return;
        }
        System.out.print("Enter initial deposit amount: ");
        double balance = 0;
        try {
            balance = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Defaulting to 0.");
        }

        Account account = bank.createAccount(name, pin, balance);
        System.out.println("Account created successfully!");
        System.out.println("Your account number is: " + account.getAccountNumber());
    }

    public static void login() {
        System.out.print("Enter account number: ");
        String accNumber = scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        Account account = bank.login(accNumber, pin);
        if (account != null) {
            loggedInMenu(account);
        } else {
            System.out.println("Invalid account number or PIN.");
        }
    }

    public static void loggedInMenu(Account account) {
        while (true) {
            System.out.println("\nWelcome, " + account.getName());
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Transaction History");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("Your current balance is: " + account.getBalance());
                    break;
                case "2":
                    deposit(account);
                    break;
                case "3":
                    withdraw(account);
                    break;
                case "4":
                    transfer(account);
                    break;
                case "5":
                    System.out.println("--- Transaction History ---");
                    account.getTransactionHistory().forEach(System.out::println);
                    break;
                case "6":
                    return; // Logout
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void deposit(Account account) {
        System.out.print("Enter amount to deposit: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            account.deposit(amount);
            bank.saveAccounts();
            System.out.println("Deposit successful.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
        }
    }

    public static void withdraw(Account account) {
        System.out.print("Enter amount to withdraw: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (account.withdraw(amount)) {
                bank.saveAccounts();
                System.out.println("Withdrawal successful.");
            } else {
                System.out.println("Insufficient balance or invalid amount.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
        }
    }

    public static void transfer(Account account) {
        System.out.print("Enter recipient's account number: ");
        String toAccNumber = scanner.nextLine();
        System.out.print("Enter amount to transfer: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (bank.transfer(account, toAccNumber, amount)) {
                System.out.println("Transfer successful.");
            } else {
                System.out.println("Transfer failed. Check recipient account number and your balance.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
        }
    }

    public static void adminLogin() {
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();

        if ("admin".equals(username) && "admin123".equals(password)) {
            adminMenu();
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    public static void adminMenu() {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. List All Accounts");
            System.out.println("2. Logout");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("--- All Bank Accounts ---");
                    bank.getAllAccounts().forEach(acc -> {
                        System.out.println("Name: " + acc.getName() + ", Account Number: " + acc.getAccountNumber() + ", Balance: " + acc.getBalance());
                    });
                    break;
                case "2":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
