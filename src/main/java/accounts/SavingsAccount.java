package accounts;


import customers.Customer;
import customers.PremiumCustomer;
import exceptions.InsufficientFundException;

import java.io.Serializable;

// US-1: SavingsAccount
public class SavingsAccount extends Account implements Serializable {
    private static final long serialVersionUID = 1L;


    private double interestRate;
    private double minimumBalance;

    public SavingsAccount(Customer customer, double balance) {
        super(customer, balance);
        this.minimumBalance=500;
        this.interestRate=3.5;
    }

    @Override
    public void displayAccountDetails() {
        System.out.println("---------------------------------------------------");
        System.out.println("ACC NO       : " + getAccountNumber());
        System.out.println("CUSTOMER NAME: " + getCustomer().getName());
        System.out.println("TYPE         : " + getAccountType());
        System.out.printf("BALANCE      : $%.2f%n", getBalance());
        System.out.println("STATUS       : " + getStatus());
        System.out.printf("Interest Rate: %.2f%%%n", interestRate);
        System.out.printf("Min Balance  : $%.2f%n", minimumBalance);
        System.out.printf("Interest Earned Annually: $%.2f%n", calculateInterestEarned());
    }

    @Override
    public String getAccountType() {
        return "Saving";}
    @Override
    public boolean withdraw(double amount) {
        if (balance - amount >= minimumBalance) {
            balance -= amount;
            System.out.println("SavingAccount: Withdrew $" + amount +
                    ". New balance: $" + balance);
            return true;
        } else {
            throw new InsufficientFundException(
                    "Withdrawal denied! Minimum balance of $" + minimumBalance + " must be maintained."
            );
        }
    }


    public double calculateInterestEarned(){
        return this.balance * interestRate/100;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }
}