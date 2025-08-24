**Bank Management System – Project Plan (CLI, Java, Minimal Implementation)**

---

### 1. Project Overview

This project is a simple, command-line interface (CLI) based Bank Management System written in Java. The goal is to simulate basic banking operations in a minimal, beginner-friendly environment without using external frameworks or complex technologies.

---

### 2. Core Objectives

1. Provide account creation and authentication features.
2. Allow basic banking operations (deposit, withdrawal, transfer, balance inquiry).
3. Ensure minimal structure with clear separation of responsibilities between classes.
4. Keep the data saved in a .dat file.

---

### 3. System Features

#### 3.1 User Account Management

* **Create Account**: New users can create an account with name, account number, PIN, and initial balance.
* **View Account Details**: Account holder can view their information after logging in.

#### 3.2 Authentication

* Login with account number and PIN.
* Logout functionality to return to main menu.

#### 3.3 Transactions

* **Deposit**: Add money to account balance.
* **Withdraw**: Deduct money if sufficient balance exists.
* **Transfer**: Move funds between two accounts if balance is sufficient.

#### 3.4 Balance Inquiry

* Display current account balance.

#### 3.5 Exit

* Gracefully terminate the program.

---

### 4. Optional Enhancements

* **Transaction History**: Maintain a list of recent transactions per account.
* **Close Account**: Remove an account from the system.
* **Admin Mode**: Allow an administrator to list all accounts.

---

### 5. CLI Flow Example

#### Main Menu

```
Welcome to TinyBank CLI
1. Create Account
2. Login
3. Exit
```

#### Logged-in Menu

```
Welcome, <UserName>
1. Check Balance
2. Deposit
3. Withdraw
4. Transfer
5. Transaction History
6. Logout
```

---

### 6. Technical Design

#### 6.1 Classes

1. **Main**

   * Handles CLI loop and user input.
   * Navigates between menus.

2. **Bank**

   * Manages a list of `Account` objects.
   * Provides methods for account creation, search, and transfer.

3. **Account**

   * Attributes:

     * `String accountNumber`
     * `String name`
     * `String pin`
     * `double balance`
     * `List<String> transactionHistory` (optional)
   * Methods:

     * `deposit(double amount)`
     * `withdraw(double amount)`
     * `transfer(Account target, double amount)`
     * `getBalance()`

---

### 7. Development Notes

* Use `ArrayList<Account>` inside `Bank` class to store all accounts.
* Keep authentication simple (compare account number + PIN).
* Use loops and menus for CLI navigation.
* File-based persistence (all data stores on a file).

---