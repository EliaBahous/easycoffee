public class Customer {
    String name;
    int id;
    

    public Customer(String name) {
        this.name = name;
    }

    public void placeOrder(CafeSystem cafeSystem, int tableNumber, String itemName, int quantity) {
        cafeSystem.placeOrder(tableNumber, itemName, quantity);
    }

    public void payBill(CafeSystem cafeSystem, int tableNumber) {
        cafeSystem.recordPayment(tableNumber);
    }
}