public class Waiter {
    public void takeOrder(CafeSystem cafeSystem, int tableNumber, String itemName, int quantity) {
        cafeSystem.placeOrder(tableNumber, itemName, quantity);
    }

    public void closeOrder(CafeSystem cafeSystem, int tableNumber) {
        cafeSystem.closeOrder(tableNumber);
    }

    public void changeOrder(CafeSystem cafeSystem, int tableNumber, String itemName, int quantity) {
        cafeSystem.changeOrder(tableNumber, itemName, quantity);
    }
}