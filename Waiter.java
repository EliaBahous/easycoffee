public class Waiter {
    public void takeOrder(CafeSystem cafeSystem, int tableNumber, String itemName, int quantity) {
        cafeSystem.placeOrder(tableNumber, itemName, quantity);
    }

    public void cancelOrder(CafeSystem cafeSystem, int tableNumber) {
        cafeSystem.cancelOrder(tableNumber);
    }

    public void changeOrder(CafeSystem cafeSystem, int tableNumber, String itemName, int quantity) {
        cafeSystem.changeOrder(tableNumber, itemName, quantity);
    }
}