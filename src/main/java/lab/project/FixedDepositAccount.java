package lab.project;

import java.util.concurrent.TimeUnit;

public class FixedDepositAccount extends Account {
    private static final double INTEREST_RATE = 0.05; // 5% interest
    private final int termMonths;
    private final long creationTimestamp;

    public FixedDepositAccount(String accountNumber, String name, String pin, double initialBalance, int termMonths) {
        super(accountNumber, name, pin, initialBalance);
        this.termMonths = termMonths;
        this.creationTimestamp = System.currentTimeMillis();
    }

    @Override
    public String getAccountType() {
        return "Fixed Deposit Account";
    }

    @Override
    public void applyInterest() {
        if (isMatured()) {
            double interest = getBalance() * INTEREST_RATE * termMonths / 12.0;
            deposit(interest);
            addTransaction("Maturity interest applied: " + interest);
        } else {
            addTransaction("Interest can only be applied at maturity.");
        }
    }

    @Override
    public boolean withdraw(double amount) {
        if (!isMatured()) {
            System.out.println("Account has not matured. A penalty will be applied (forfeiture of interest).");
            // Simple penalty: just return the principal amount
            if (amount <= getBalance()) {
                balance -= amount;
                addTransaction("Early withdrawal with penalty: " + amount);
                return true;
            }
            return false;
        } else {
            return super.withdraw(amount);
        }
    }

    public boolean isMatured() {
        long currentTime = System.currentTimeMillis();
        long termMillis = TimeUnit.DAYS.toMillis((long) termMonths * 30); // Approximate
        return (currentTime - creationTimestamp) >= termMillis;
    }

    public String getMaturityStatus() {
        if (isMatured()) {
            return "Your fixed deposit has matured. You can withdraw the full amount or apply interest.";
        } else {
            long termMillis = TimeUnit.DAYS.toMillis((long) termMonths * 30);
            long remainingMillis = (creationTimestamp + termMillis) - System.currentTimeMillis();
            long remainingDays = TimeUnit.MILLISECONDS.toDays(remainingMillis);
            return "Your fixed deposit will mature in approximately " + remainingDays + " days.";
        }
    }
}
