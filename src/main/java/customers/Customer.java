package customers;

public abstract class Customer {
    // Static field for generating unique customer IDs (e.g., CUS001)
    public static int customerCounter=0;

    // Private fields (Encapsulation)
    private String customerId;
    private int age;
    private String name;
    private String contact;
    private String address;

    //constructor
    public Customer(String name, int age, String contact, String address) {
        customerCounter++;
        this.customerId =String.format("CUS%03d",customerCounter) ;
        this.contact = contact;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    // Abstract methods for polymorphism
    public abstract void displayCustomerDetails();
    public abstract  String getCustomerType();

    // Getters
    public String getAddress() {return address;}
    public String getContact() {return contact;}
    public String getName() {return name;}
    public int getAge() {return age;}
    public String getCustomerId() {return customerId;}

}

