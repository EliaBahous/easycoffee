public class MenuItem {
    String itemName;
    double price;
    double quantityInStock;
    int totalSold;
    double discount;

    public MenuItem(String itemName, double price, double quantityInStock) {
        this.itemName = itemName;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.totalSold = 0;
        this.discount = 0; //intially no discount
    }

       // Getter and setter methods for discount
       public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    // Method to apply discount to price
    public void applyDiscount() {
        double discountedPrice = price * (1 - discount / 100);
        System.out.println("Discounted price for " + itemName + ": $" + discountedPrice);
    }
}
