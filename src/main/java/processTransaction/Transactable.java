package processTransaction;

public interface Transactable {
    boolean processTransaction(double amount, String type);
}
