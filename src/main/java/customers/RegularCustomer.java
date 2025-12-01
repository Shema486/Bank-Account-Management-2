package customers;

public class RegularCustomer extends Customer {

    // Constructor (simply calls the abstract class constructor)
    public RegularCustomer(String name, int age, String contact, String address) {
        super(name, age, contact, address);
    }

    // Override the abstract method to display customer details
    @Override
    public void displayCustomerDetails() {
        System.out.println("CustomerId: "+getCustomerId());
        System.out.println("Name: "+getName());
        System.out.println("Age: "+getAge());
        System.out.println("Contact:" +getContact());
        System.out.println("Address: "+getAddress());
        System.out.println("Type: "+getCustomerType());
    }


    // Override the abstract method to specify the type
    @Override
    public String getCustomerType() {
        return "Regular";
    }
}
