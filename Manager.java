public class Manager {
    public void defineDiscount(CafeSystem cafeSystem, String itemName, double discountPercentage) {
        cafeSystem.applyDiscount(itemName, discountPercentage);
    }
}
