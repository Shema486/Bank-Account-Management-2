import utils.ValidationUtils;
import accounts.Account;
import accounts.CheckingAccount;
import accounts.SavingsAccount;
import customers.Customer;
import customers.PremiumCustomer;
import customers.RegularCustomer;
import processTransaction.AccountManager;
import processTransaction.Transaction;
import transactionHistory.TransactionManager;


import java.util.InputMismatchException;
import java.util.Scanner;


public class Main {
    static Scanner scanner = new Scanner(System.in);
     static AccountManager accountManager = new AccountManager();
    static TransactionManager transactionManager = new TransactionManager();
    static ValidationUtils validationUtils = new ValidationUtils();

    public static void main(String[] args) {
        // 1. Initialize data for testing (US-1 and US-4 requirement)
        initializeData();


        // 2. Main menu loop (US-5)
        int choice = 0;
        do {
            displayMenu();
            try {
                System.out.print("Enter your choice (1-6): ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        // US-2: Create Account
                        handleCreateAccount();
                        break;
                    case 2:
                        // US-1: View All Accounts
                        accountManager.viewAllAccounts();
                        accountManager.getTotalBalance();
                        break;
                    case 3:
                        // US-3: Process Transaction
                        handleProcessTransaction();
                        break;
                    case 4:
                        // US-4: View Transaction History
                        handleViewHistory();
                        break;
                    case 5:
                        handleTransfer();
                        break;
                    case 6:
                        System.out.println("\nThank you for using the Bank Account Management System. Goodbye!");
                        break;
                    default:
                        // Input Validation (US-5 Acceptance Criteria)
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            } catch (InputMismatchException e) {
                // Input Validation for non-integer inputs (US-5 Acceptance Criteria)
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
                choice = 0;
            }
        } while (choice != 6);

        scanner.close();
    }

    // --- Helper Methods ---

    private static void displayMenu() {
            System.out.println("\n=============================================");
            System.out.println("      BANK ACCOUNT MANAGEMENT SYSTEM MENU    ");
            System.out.println("=============================================");
            System.out.println("1. Create New Account ");
            System.out.println("2. View All Accounts ");
            System.out.println("3. Process Transaction (Deposit/Withdraw) ");
            System.out.println("4. Process Account Transfer");
            System.out.println("5. View Transaction History ");
            System.out.println("6. Exit ");
            System.out.println("=============================================");

    }

    // Helper to create initial accounts and transactions for testing
    private static void initializeData() {
        Customer c1 = new RegularCustomer("Alice",64,"555-555","111-RWANDA");
        Customer c2 = new RegularCustomer("SHEMA",54,"544-335","122-BURUNDI");
        Customer c3 = new RegularCustomer("Bruce",24,"522-522","333-CAMERON");
        Customer c4 = new PremiumCustomer("Ange",30,"115-511","334-CANADA");
        Customer c5 = new PremiumCustomer("Peace",54,"235-445","443-USA");

        Account acc1 = new SavingsAccount(c1,5000);
        Account acc2 = new SavingsAccount(c2,2000);
        Account acc3 = new SavingsAccount(c3,1000);
        Account acc4 = new CheckingAccount(c4,10000);
        Account acc5 = new CheckingAccount(c5,8000);

        accountManager.addAccount(acc1);
        accountManager.addAccount(acc2);
        accountManager.addAccount(acc3);
        accountManager.addAccount(acc4);
        accountManager.addAccount(acc5);
        // Create initial Transactions (US-4 initial data)
        acc1.deposit(500.00); // Balance 2000
        transactionManager.addTransaction(new Transaction(acc1.getAccountNumber(), "DEPOSIT", 500.00, acc1.getBalance()));

        System.out.println("--- Initial Test Data Loaded Successfully ---");
    }

    // 1. Get Source Account
    private static void handleTransfer(){
        System.out.println("\n--- PROCESS ACCOUNT TRANSFER ---");
        //System.out.println("Enter Source Account (e.g ACC001)");
        String sourceAccNum = validationUtils.getStringInput("Enter SOURCE Account Number (From): (e.g ACC001)");
        Account sourceAccount = accountManager.findAccount(sourceAccNum);
        if (sourceAccount == null){
            System.out.println("Error: Source Account not found.");
            return;
        }
        // 2. Get Target Account
       // System.out.print("Enter TARGET Account Number (To): ");
        String targetAccNum = validationUtils.getStringInput("Enter TARGET Account Number (To):(e.g ACC002) ");
        Account targetAccount = accountManager.findAccount(targetAccNum);

        if (targetAccount == null) {
            System.out.println("Error: Target Account not found.");
            return;
        }

        if (sourceAccount.getAccountNumber().equalsIgnoreCase(targetAccount.getAccountNumber())) {
            System.out.println("Error: Cannot transfer funds to the same account.");
            return;
        }
        // 3. Get Amount
        System.out.print("Enter Transfer Amount: $");
        double transferAmount = validationUtils.getDoubleInput("Enter Transfer Amount: $",500); // Assumes your helper method for input validation

        if (transferAmount <= 0) {
            System.out.println("Error: Transfer amount must be positive.");
            return;
        }
        // --- 4. EXECUTION ---
        // Attempt Withdrawal FIRST (Triggers all validation rules)
        System.out.println("Attempting withdrawal from source...");
        if (sourceAccount.withdraw(transferAmount)) {
            // Withdrawal successful, now process deposit
            System.out.println("Withdrawal successful. Processing deposit to target...");

            // Deposit will almost always succeed unless amount is negative (already checked)
            if (targetAccount.deposit(transferAmount)) {
                // --- 5. RECORD TRANSACTIONS ---

                // Record Withdrawal Transaction
                Transaction withdrawalTxn = new Transaction(sourceAccNum, "WITHDRAW", transferAmount, sourceAccount.getBalance());
                transactionManager.addTransaction(withdrawalTxn);

                // Record Deposit Transaction
                Transaction depositTxn = new Transaction(targetAccNum, "DEPOSIT", transferAmount, targetAccount.getBalance());
                transactionManager.addTransaction(depositTxn);

                System.out.printf("\nSUCCESS: Transfer of $%,.2f complete.\n", transferAmount);
                System.out.printf("  Source Balance (%s): %s\n", sourceAccNum, sourceAccount.getBalance());
                System.out.printf("  Target Balance (%s): %s\n", targetAccNum, targetAccount.getBalance());

            } else {
                // This should ideally never happen for a positive amount, but handles exceptions
                System.out.println("Withdrawal succeeded, but deposit failed. Balance restored.");
                // In a real system, you would Roll back the withdrawal here.
                // For simplicity, we assume the deposit success.
                sourceAccount.deposit(transferAmount); // Rollback attempt
            }
        } else {
            // Withdrawal failed due to insufficient funds, minimum balance, or overdraft limit
            System.out.println("TRANSFER FAILED: Withdrawal from source account was rejected (Check account rules/limits).");
        }
    }
    // Logic for Menu Option 1 (US-2)
    private static void handleCreateAccount() {
        System.out.println("‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗");
        System.out.println("CREATE NEW ACCOUNT");
        System.out.println("‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗\n");
//        System.out.print("ENTER CUSTOMER NAME: ");
        String name = validationUtils.getStringInput("ENTER CUSTOMER NAME: ");

//        System.out.print("ENTER AGE: ");
        int age = validationUtils.getIntInput("Enter Customer Age: ",18,70);
        if (age == -1) return;

//        System.out.print("ENTER CONTACT: ");
        String contact = validationUtils.getStringInput("ENTER CONTACT: ");

//        System.out.print("ENTER ADDRESS: ");
        String address = validationUtils.getStringInput("ENTER ADDRESS: ");

        System.out.println("\n-------CUSTOMER TYPE:-----");
        System.out.println("1.Regular Customer (Standard banking service) ");
        System.out.println("2.Premium Customer (Enhanced benefits, min balance) ");
//        System.out.print("select type (1-2): ");
        int customerType = validationUtils.getIntInput(" select type (1-2): ",1,2);
        scanner.nextLine();

        Customer customer;
        if (customerType ==1){
            customer = new RegularCustomer(name,age,contact,address);
        }else {
            customer =new PremiumCustomer(name,age,contact,address);
        }
        System.out.println("\nACCOUNT TYPE:\n");
        System.out.println("1. Saving Account (Interest:3.5%, Min Balance: $500)");
        System.out.println("2. Checking Account (Overdraft: $1000, Monthly fee: $10)");
//        System.out.print("\select type (1-2): ");
        int accountType = validationUtils.getIntInput("select type (1-2): ",1,2);

//        System.out.print("Enter initial deposit amount: $");
        double amountDeposited = validationUtils.getDoubleInput("Enter initial deposit amount: $",500);

        Account account;
        if (accountType == 1){
            account = new SavingsAccount(customer,amountDeposited);
        }
        else {
            account = new CheckingAccount(customer,amountDeposited);
        }
        accountManager.addAccount(account);
        account.displayAccountDetails();
        System.out.println("\n ✅ CREATION OF YOUR ACCOUNT IS SUCCESSFULLY COMPLETE");
        validationUtils.enterToContinue();
    }

    // Logic for Menu Option 3 (US-3)
    private static void handleProcessTransaction() {
        System.out.println("‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗");
        System.out.println("PROCESS TRANSACTION");
        System.out.println("‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗\n");

//        System.out.print("Enter Account number (e.g, ACC001)");
        String accNumber = validationUtils.getStringInput("Enter Account number (e.g, ACC001): ");

        //to get that account from AccountManager (array)
        Account account =accountManager.findAccount(accNumber);
        if (account ==null){
            System.out.println("Account does not exist");
            validationUtils.enterToContinue();
            return;
        }
        System.out.println("Account details......... ");
        System.out.println("Customer name: " + account.getCustomer().getName());
        System.out.println("Account type: "+ account.getAccountType());
        System.out.println("Current Balance: $" + account.getBalance());

        System.out.println("\nTransaction type:");
        System.out.println("1.Deposit");
        System.out.println("2.withdraw");
//        System.out.print("select type (1-2): ");
        int type = validationUtils.getIntInput(" select type (1-2): ",1,2);
        String  transactionType = type==1 ? "DEPOSIT" : "WITHDRAW";

//        System.out.print("Enter amount for transaction:" );
        double amountForTransaction = validationUtils.getDoubleInput("Enter amount for transaction: $",500);
        double initialBalance = account.getBalance();
        double balanceAfter = type ==1 ? initialBalance+amountForTransaction : initialBalance-amountForTransaction;

        System.out.println("‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗");
        System.out.println("Transaction conformation");
        System.out.println("‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗\n");

        Transaction transaction = new Transaction(accNumber,transactionType,amountForTransaction,balanceAfter);
        System.out.println("Transaction ID:" +transaction.getTransactionId());
        System.out.println("AccountNumber:" + transaction.getAccountNumber());
        System.out.println("Amount: " +transaction.getAmount());
        System.out.println("Previous balance: " +initialBalance);
        System.out.println("New balance: " + transaction.getBalanceAfter());
        System.out.println("Date/time: " + transaction.getTimestamp());

//        System.out.println("\nConfirm transaction? (Y/N): ");
        String confirm = validationUtils.getStringInput("\nConfirm transaction? (Y/N): ");

        if (confirm.equalsIgnoreCase("y")){
            boolean accept = account.processTransaction(amountForTransaction,transactionType);
            if (accept){
                transactionManager.addTransaction(transaction);
                System.out.println("\n ===========Transaction completed successfully============");
            }else {
                System.out.println("Transaction failed. try again");
            }
        }
        else {
            System.out.println("\nTransaction denied");
        }
        validationUtils.enterToContinue();
    }

    // Logic for Menu Option 4 (US-4)
    private static void handleViewHistory() {
        System.out.println("‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗");
        System.out.println("View transaction history");
        System.out.println("‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗‗\n");

//        System.out.print("Enter Account (e.g,ACC001): ");
        String accNum = validationUtils.getStringInput("Enter Account (e.g,ACC001): ");

        Account account = accountManager.findAccount(accNum);
        if(account==null){
            System.out.println("\nAccount not found");
           validationUtils.enterToContinue();
           return;
        }
        System.out.println("\nAccount: "+accNum+"-"+ account.getCustomer().getName());
        System.out.println("Account type: " + account.getAccountNumber());
        System.out.println("Current balance: " + account.getBalance());
        System.out.println("TXN ID | DATE/TIME |  TYPE  |  AMOUNT  |  BALANCE");
        transactionManager.viewTransactionsByAccount(accNum);
        System.out.println("\nTotal transaction: "+ transactionManager.getTransactionCount());
        System.out.println("Total Deposit: " + transactionManager.calculateTotalDeposit(accNum));
        System.out.println("Total withdraw: " + transactionManager.calculateTotalWithdraw(accNum));
    }
}