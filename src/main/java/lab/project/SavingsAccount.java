package lab.project;

public class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 0.02; // 2% interest

    public SavingsAccount(String accountNumber, String name, String pin, double initialBalance) {
        super(accountNumber, name, pin, initialBalance);
    }

    @Override
    public String getAccountType() {
        return "Savings Account";
    }

    @Override
    public void applyInterest() {
        double interest = getBalance() * INTEREST_RATE;
        deposit(interest);
        addTransaction("Interest applied: " + interest);
    }
}
