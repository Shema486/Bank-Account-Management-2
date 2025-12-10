package accounts;

import customers.Customer;
import customers.PremiumCustomer;
import exceptions.OverdraftExceededException;

import java.io.Serializable;


public class CheckingAccount extends Account implements Serializable {
    private static final long serialVersionUID = 1L;


    private double overdraftLimit;
    private double monthlyFee;

    public CheckingAccount(Customer customer, double initialBalance) {
        super(customer, initialBalance);
        this.overdraftLimit = 1000;
        this.monthlyFee =10;
    }

    //Menu after creating checkingAccount
    @Override
    public void displayAccountDetails() {
        System.out.println("---------------------------------------------------");
        System.out.println("ACC NO       : " + getAccountNumber());
        System.out.println("CUSTOMER NAME: " + getCustomer().getName());
        System.out.println("TYPE         : " + getAccountType());
        System.out.printf("BALANCE      : $%.2f%n", getBalance());
        System.out.println("STATUS       : " + getStatus());
        System.out.printf("Overdraft Limit: $%.2f%n", overdraftLimit);

        String feeStatus = (getCustomer() instanceof PremiumCustomer pc && pc.hasWaivedFees()) ?
                "$0.00 (WAIVED)" : "$" + String.format("%,.2f", monthlyFee);
        System.out.println("Monthly Fee  : " + feeStatus);
    }




    //return type of account
    @Override
    public String getAccountType() {return "Checking";}

    @Override
    public boolean withdraw(double amount) throws OverdraftExceededException {
        double newBalance = this.balance - amount;

        // Allow overdraft up to -1000
        if (newBalance < -overdraftLimit) {
            throw new OverdraftExceededException(
                    "Withdrawal denied! Overdraft limit of $" + overdraftLimit + " exceeded.");
        }

        this.balance = newBalance;
        System.out.println("Withdrawal successful. New balance: $" + this.balance);
        return true;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public String appyMonthlyFee(){
        balance -= monthlyFee;
        return "New balance after fee: $" + balance;
    }
}