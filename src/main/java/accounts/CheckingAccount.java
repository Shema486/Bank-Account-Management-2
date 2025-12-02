package accounts;

import customers.Customer;
import customers.PremiumCustomer;
import exceptions.OverdraftExceededException;


public class CheckingAccount extends Account{

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
            double fee = this.monthlyFee;
            String feeDetails = "Monthly Fee: $" + String.format("%,.2f", fee);

            // Check for premium status to waive fee
            if (getCustomer() instanceof PremiumCustomer) {
                PremiumCustomer pc = (PremiumCustomer) getCustomer();
                if (pc.hasWaivedFees()) {

                    feeDetails = "Monthly Fee: $0.00 (WAIVED - Premium Customer)";
                }
            }
            System.out.println("ACC NO: " + getAccountNumber() + " | CUSTOMER NAME: " + getCustomer().getName()
                    + " | TYPE: " + getAccountType() + " | BALANCE: " + getBalance()
                    + " | STATUS: " + getStatus());
            System.out.println("    | Overdraft Limit: $" + String.format("%,.2f", overdraftLimit) + " | " + feeDetails);
            System.out.println("    |fee applied :" +appyMonthlyFee());

    }


    //return type of account
    @Override
    public String getAccountType() {return "Checking";}

    @Override
    public boolean withdraw(double amount)throws OverdraftExceededException {
        // Check if withdrawal stays within overdraft limit
        if (this.balance - amount >= overdraftLimit) {
            throw new OverdraftExceededException("Withdrawal denied! Overdraft limit of $1000 exceeded.");
        } else {
            this.balance -= amount;
            System.out.println("Withdrawal successful. New balance: $" + this.balance);
            return true;
        }
    }

    public String appyMonthlyFee(){
        balance -= monthlyFee;
        return "New balance after fee: $" + balance;
    }
}