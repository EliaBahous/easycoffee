public class Customer {
    String name;
    int id;
    

    public Customer(String name) {
        this.name = name;
    }

    public void payBill(CafeSystem cafeSystem, int tableNumber) {
        cafeSystem.recordPayment(tableNumber);
    }
}