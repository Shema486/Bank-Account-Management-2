package processTransaction;

import accounts.Account;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Assuming 'accounts.Account' and 'customers.RegularCustomer' exist,
// though RegularCustomer is not used here.

public class AccountManager implements Serializable{

    // Composition: Holds a List of Account objects (Good use of abstraction!)
    private List<Account> accounts;

    public AccountManager() {
        this.accounts = new ArrayList<>();
    }

    // Method for US-2 (Create Account)
    public void addAccount(Account newAccount) {
        this.accounts.add(newAccount);
    }

    // Method for US-3 (Process Transaction) - DSA: Linear Search
    // CORRECTED LOGIC: The return null must be outside the loop.
    public Account findAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equalsIgnoreCase(accountNumber)) {
                return account; // Return immediately if found
            }
        }
        // Only return null if the entire list has been searched and the account wasn't found.
        return null;
    }

    // Method to calculate total balance (Method un-nested)
    public double getTotalBalance() {
        double total = 0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return total;
    }

    // Method for US-1 (View Accounts) (Method un-nested)
    public void viewAllAccounts() {
        // Uses the correct List method (.size())
        if (accounts.size() == 0) {
            System.out.println("No accounts in the bank.");
            return;
        }

        System.out.println("----- All Bank Accounts -----");
        // Uses the idiomatic enhanced for loop for iteration
        for (Account account : accounts) {
            // Assuming Account has a displayAccountDetails() method
            account.displayAccountDetails();
        }
        System.out.println("Total accounts: " + getAccountCount());
        System.out.println("Total Bank balance: $" + getTotalBalance());
    }

    // Getter (Method un-nested)
    public int getAccountCount() {
        // Uses the correct List method (.size())
        return accounts.size();
    }

//    public void saveAccountToFile(String fileName){
//        try {
//            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
//            oos.writeObject(accounts);
//            oos.writeInt(accounts.size());
//            System.out.println("Account saved successfully......!");
//        }catch (IOException e){
//            System.out.println("Error try again" + e.getMessage());
//        }
//    }
//    public  void  printAccountFromFileToConsole(String fileName){
//        try {
//            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
//            accounts = (List<Account>) ois.readObject();
////            int accountCount =accounts.size();
////            accountCount = ois.readInt();
//
//            System.out.println("Accounts loaded from file!");
//        }catch (FileNotFoundException e){
//            System.out.println("No previous accounts found, starting fresh.");
//        }
//        catch (IOException | ClassNotFoundException e){
//            System.out.println("Error uuu" + e.getMessage());
//        }
//    }
}