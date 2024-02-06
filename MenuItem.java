public class MenuItem {
    String itemName;
    double price;
    double quantityInStock;
    int totalSold;

    public MenuItem(String itemName, double price, double quantityInStock) {
        this.itemName = itemName;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.totalSold = 0;
    }
}
