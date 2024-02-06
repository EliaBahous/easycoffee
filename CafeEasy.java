public class CafeEasy {
    public static void main(String[] args) {
        CafeSystem cafeSystem = new CafeSystem();

        cafeSystem.addTable(1);
        cafeSystem.addTable(2);

        cafeSystem.addMenuItem("Coffee", 5.99, 100);
        cafeSystem.addMenuItem("Cake", 3.99, 50);
        cafeSystem.applyDiscount("Coffee", 10); 
        Waiter waiter = new Waiter();
        //waiter.takeOrder(cafeSystem, 1, "Coffee", 2);
        //waiter.takeOrder(cafeSystem, 2, "Cake", 1);

        Customer customer = new Customer("John Doe");
        customer.placeOrder(cafeSystem, 1, "Cake", 1);
        customer.payBill(cafeSystem);

        cafeSystem.generateMonthlySalesReport();
        cafeSystem.generateMonthlySalesGraph();
    }
}