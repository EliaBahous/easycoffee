public class Waiter {

    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public Waiter(String managerId, String firstName, String lastName, String phoneNumber) {
        this.id = managerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }
    
    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }
}
