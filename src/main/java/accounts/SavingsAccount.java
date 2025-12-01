package accounts;


import customers.Customer;
import customers.PremiumCustomer;
// US-1: SavingsAccount
public class SavingsAccount extends Account{

    private double interestRate;
    private double minimumBalance;

    public SavingsAccount(Customer customer, double balance) {
        super(customer, balance);
        this.minimumBalance=500;
        this.interestRate=3.5;
    }

    @Override
    public void displayAccountDetails() {
        System.out.println("ACC NO: " + getAccountNumber() + " | CUSTOMER NAME: " + getCustomer().getName()
                + " | TYPE: " + getAccountType() + " | BALANCE: " + getBalance()
                + " | STATUS: " + getStatus());
        System.out.println("    | Interest Rate: " + (interestRate)+"%" + "% | Min Balance: $" + minimumBalance);
        System.out.println("Interest annually: "+calculateInterestEarned());
    }
    @Override
    public String getAccountType() {
        return "Saving";}
    @Override
    public boolean withdraw(double amount) {
        // Check if withdrawal stays above minimum balance
        if (balance - amount >= minimumBalance) {
            balance -= amount;
            System.out.println("SavingAccount: Withdrew $" + amount +
                    ". New balance: $" + balance);
            return true;
        } else {
            System.out.println("Withdrawal denied! Minimum balance of $" +
                    minimumBalance + " must be maintained.");
            return false;
        }
    }
    public double calculateInterestEarned(){
        return this.balance * interestRate/100;
    }
}