package lab.project;

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Cli cli = new Cli(bank);
        cli.run();
    }
}