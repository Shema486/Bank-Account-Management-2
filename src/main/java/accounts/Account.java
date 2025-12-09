package accounts;
import customers.Customer;
import exceptions.InsufficientFundException;
import interfaces.Depositable;
import interfaces.Transactable;
import interfaces.Withdrawable;

import java.io.Serializable;


public abstract class Account implements Transactable, Depositable, Withdrawable, Serializable {
    // Static field for generating unique account IDs (e.g., ACC001)
    private static int accountCounter = 0;

    // Private fields (Encapsulation)
    private String accountNumber;
    private Customer customer; // Composition: Account HAS-A Customer
    protected double balance; // Protected so subclasses can access/modify directly for withdraw
    private String status;

    // Constructor
    public Account(Customer customer, double initialBalance) {
        accountCounter++;
        this.accountNumber = String.format("ACC%03d",accountCounter);
        this.customer = customer;
        this.balance = initialBalance;
        this.status = "ACTIVE";
    }

    //Getter
    public String getStatus() {return status;}
    public double getBalance() {return balance;}
    public Customer getCustomer() {return customer;}
    public String getAccountNumber() {return accountNumber;}

    //Abstract method
    public abstract void displayAccountDetails();
    public abstract String getAccountType();

    //Methods of deposit and withdraw
    public  boolean deposit(double amount){
        if(amount <= 0) return false;
        this.balance += amount ;
            return true;
    };
    public   abstract boolean withdraw(double amount);
    @Override
    public boolean processTransaction(double amount, String type) {
        try {
            if (type.equalsIgnoreCase("DEPOSIT")) {
                return deposit(amount);
            }
            else if (type.equalsIgnoreCase("WITHDRAW")) {
                return withdraw(amount); // may throw exception
            }
            else {
                return false;
            }
        } catch (InsufficientFundException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }



}
/*
deposit
{
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited $" + amount + ". New balance: $" + balance);
        } else {
            System.out.println("Deposit amount must be positive!");
        }
    }
withdraw
{
        if (amount > 0 && balance - amount >= 0) {  // For simple account
            balance -= amount;
            System.out.println("Withdrew $" + amount + ". New balance: $" + balance);
        } else {
            System.out.println("Withdrawal failed! Not enough balance.");
        }

    }*/