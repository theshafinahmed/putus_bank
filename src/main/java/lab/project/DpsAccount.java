package lab.project;

public class DpsAccount extends Account {
    private final double monthlyInstallment;

    public DpsAccount(String accountNumber, String name, String pin, double initialBalance, double monthlyInstallment) {
        super(accountNumber, name, pin, initialBalance);
        this.monthlyInstallment = monthlyInstallment;
    }

    @Override
    public String getAccountType() {
        return "DPS Account";
    }

    @Override
    public void applyInterest() {
        // DPS interest is typically applied at the end of the term, which is complex for this model.
        addTransaction("Interest for DPS is applied at maturity.");
    }

    public double getMonthlyInstallment() {
        return monthlyInstallment;
    }

    public void makeInstallment() {
        deposit(this.monthlyInstallment);
        addTransaction("DPS installment paid: " + this.monthlyInstallment);
    }
}
