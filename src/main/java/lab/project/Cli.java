package lab.project;

import java.util.List;
import java.util.Scanner;

public class Cli {
    private Bank bank;
    private Scanner scanner;

    public Cli(Bank bank) {
        this.bank = bank;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
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

    private void createAccount() {
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

        System.out.print("Choose account type (savings, current, dps, fixeddeposit): ");
        String accountType = scanner.nextLine();

        int termMonths = 0;
        double monthlyInstallment = 0;

        if ("dps".equalsIgnoreCase(accountType)) {
            System.out.print("Enter monthly installment amount: ");
            try {
                monthlyInstallment = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Defaulting to 0.");
            }
        } else if ("fixeddeposit".equalsIgnoreCase(accountType)) {
            System.out.print("Enter term in months (e.g., 6, 12): ");
            try {
                termMonths = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid term. Defaulting to 0.");
            }
        }

        Account account = bank.createAccount(name, pin, balance, accountType, termMonths, monthlyInstallment);
        if (account != null) {
            System.out.println("Account created successfully!");
            System.out.println("Your account number is: " + account.getAccountNumber());
        } else {
            System.out.println("Invalid account type specified.");
        }
    }

    private void login() {
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

    private void loggedInMenu(Account account) {
        while (true) {
            System.out.println("\nWelcome, " + account.getName() + " (" + account.getAccountType() + ")");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Transaction History");
            System.out.println("6. Update PIN");
            System.out.println("7. Close Account");

            // Account-specific options
            if (account instanceof DpsAccount) {
                System.out.println("8. Make DPS Installment");
            } else if (account instanceof FixedDepositAccount) {
                System.out.println("8. Check Maturity Status");
            }

            System.out.println("9. Logout");
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
                    updatePin(account);
                    break;
                case "7":
                    if (closeAccount(account)) {
                        return; // Account closed, so log out.
                    }
                    break;
                case "8":
                    if (account instanceof DpsAccount) {
                        ((DpsAccount) account).makeInstallment();
                        bank.saveAccounts();
                        System.out.println("Installment paid successfully.");
                    } else if (account instanceof FixedDepositAccount) {
                        System.out.println(((FixedDepositAccount) account).getMaturityStatus());
                    }
                    break;
                case "9":
                    return; // Logout
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void deposit(Account account) {
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

    private void withdraw(Account account) {
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

    private void transfer(Account account) {
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

    private void updatePin(Account account) {
        System.out.print("Enter your current PIN: ");
        String oldPin = scanner.nextLine();
        if (account.validatePin(oldPin)) {
            System.out.print("Enter your new 4-digit PIN: ");
            String newPin = scanner.nextLine();
            if (newPin.matches("\\d{4}")) {
                account.setPin(newPin);
                bank.saveAccounts();
                System.out.println("PIN updated successfully.");
            } else {
                System.out.println("Invalid PIN format. Please enter a 4-digit number.");
            }
        } else {
            System.out.println("Incorrect current PIN.");
        }
    }

    private boolean closeAccount(Account account) {
        System.out.print("Are you sure you want to close your account? This cannot be undone. (yes/no): ");
        String confirmation = scanner.nextLine();
        if ("yes".equalsIgnoreCase(confirmation)) {
            System.out.print("Enter your PIN to confirm: ");
            String pin = scanner.nextLine();
            if (account.validatePin(pin)) {
                bank.deleteAccount(account.getAccountNumber());
                System.out.println("Account closed successfully.");
                return true;
            }
            System.out.println("Incorrect PIN. Account not closed.");
        }
        return false;
    }

    private void adminLogin() {
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

    private void adminMenu() {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. List All Accounts");
            System.out.println("2. Delete Account");
            System.out.println("3. Search for Account");
            System.out.println("4. Apply Interest to All Accounts");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    listAllAccounts();
                    break;
                case "2":
                    deleteAccount();
                    break;
                case "3":
                    searchForAccount();
                    break;
                case "4":
                    bank.applyInterestToAllAccounts();
                    System.out.println("Interest applied to all applicable accounts.");
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void listAllAccounts() {
        System.out.println("--- All Bank Accounts ---");
        bank.getAllAccounts().forEach(acc -> {
            System.out.println("Name: " + acc.getName() + ", Account Number: " + acc.getAccountNumber() + ", Type: " + acc.getAccountType() + ", Balance: " + acc.getBalance());
        });
    }

    private void deleteAccount() {
        System.out.print("Enter account number to delete: ");
        String accNumber = scanner.nextLine();
        if (bank.deleteAccount(accNumber)) {
            System.out.println("Account deleted successfully.");
        } else {
            System.out.println("Account not found.");
        }
    }

    private void searchForAccount() {
        System.out.print("Enter name or account number to search: ");
        String query = scanner.nextLine();
        List<Account> results = bank.searchAccounts(query);
        if (results.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            System.out.println("--- Search Results ---");
            results.forEach(acc -> {
                System.out.println("Name: " + acc.getName() + ", Account Number: " + acc.getAccountNumber() + ", Type: " + acc.getAccountType() + ", Balance: " + acc.getBalance());
            });
        }
    }
}
