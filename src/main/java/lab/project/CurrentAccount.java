package lab.project;

public class CurrentAccount extends Account {

    public CurrentAccount(String accountNumber, String name, String pin, double initialBalance) {
        super(accountNumber, name, pin, initialBalance);
    }

    @Override
    public String getAccountType() {
        return "Current Account";
    }

    @Override
    public void applyInterest() {
        // Current accounts typically do not earn interest.
        addTransaction("No interest applied for current account type.");
    }
}
