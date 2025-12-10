package processTransaction;

import accounts.Account;
import accounts.CheckingAccount;
import accounts.SavingsAccount;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Assuming 'accounts.Account' and 'customers.RegularCustomer' exist,
// though RegularCustomer is not used here.

public class AccountManager  {

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
        if (accounts.isEmpty()) {
            System.out.println("No accounts in the bank.");
            return;
        }

        // Table header
        System.out.println("\n================ All Bank Accounts ================");
        System.out.printf("%-8s | %-20s | %-10s | %-12s | %-8s | %-12s | %-12s%n",
                "ACC NO", "CUSTOMER NAME", "TYPE", "BALANCE", "STATUS", "INTEREST", "MIN/OD LIMIT");
        System.out.println("-------------------------------------------------------------------------------");

        for (Account acc : accounts) {
            if (acc instanceof SavingsAccount sa) {
                System.out.printf("%-8s | %-20s | %-10s | $%-11.2f | %-8s | %-9.2f%% | $%-11.2f%n",
                        sa.getAccountNumber(),
                        sa.getCustomer().getName(),
                        sa.getAccountType(),
                        sa.getBalance(),
                        sa.getStatus(),
                        sa.getInterestRate(),
                        sa.getMinimumBalance());
            } else if (acc instanceof CheckingAccount ca) {
                System.out.printf("%-8s | %-20s | %-10s | $%-11.2f | %-8s | %-10s | $%-11.2f%n",
                        ca.getAccountNumber(),
                        ca.getCustomer().getName(),
                        ca.getAccountType(),
                        ca.getBalance(),
                        ca.getStatus(),
                        "-",  // No interest for checking
                        ca.getOverdraftLimit());
            }
        }

        System.out.println("===============================================================");
        System.out.println("Total accounts: " + getAccountCount());
        System.out.printf("Total Bank balance: $%,.2f%n", getTotalBalance());
    }


    // Getter (Method un-nested)
    public int getAccountCount() {
        // Uses the correct List method (.size())
        return accounts.size();
    }
    public  void saveAccount(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Account.dat"));
            oos.writeObject(accounts);
            oos.close();
            System.out.println("Account saved successfully");
        }catch (Exception e){
            System.out.println("Error saving Account" + e.getMessage());
        }
    }
    public void loadAccount() {
        try {
            File file = new File("Account.dat");
            if (!file.exists())
                return;

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            accounts = (List<Account>) ois.readObject();
            ois.close();
            System.out.println("Accounts loaded successfully!");

            //  AFTER loading accounts, restore the counter
            if (!accounts.isEmpty()) {
                String lastAccNo = accounts.get(accounts.size() - 1).getAccountNumber();
                int num = Integer.parseInt(lastAccNo.substring(3)); // ACC006 → 6
                Account.setAccountCounter(num);
            }

        } catch (Exception e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
    }

}