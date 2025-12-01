package processTransaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;

public class Transaction {
    // Static field for generating unique transaction IDs (e.g., TXN001)
    private static int transactionCounter = 0;

    // Fields
    private String transactionId;
    private String accountNumber;
    private String type;        // DEPOSIT or WITHDRAWAL
    private double amount;
    private double balanceAfter;
    private String timestamp;

    // Constructor
    public Transaction(String accountNumber, String type, double amount, double balanceAfter) {
        // Auto-generate unique ID
        transactionCounter++;
        this.transactionId = String.format("TXN%04d", transactionCounter);

        // Record details
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;

        // Record timestamp (Date/Time formatting)
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = now.format(formatter);
    }

    // Method to display details (US-4 requirement)
    public void displayTransactionDetails() {
        String sign = type.equalsIgnoreCase("DEPOSIT") ? "+" : "-";
        System.out.printf("| %-7s | %-19s | %-12s | %-10s | %s%.2f | %s%.2f |\n",
                transactionId, timestamp, accountNumber, type,
                sign, amount, "", balanceAfter);
    }

    // Getters
    public String getTransactionId() { return transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public String getTimestamp() { return timestamp; }
}