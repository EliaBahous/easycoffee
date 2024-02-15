public class CafeEasy {
    public static void main(String[] args) {
        CafeSystem cafeSystem = new CafeSystem();
      
        cafeSystem.addTable(251);
        //cafeSystem.addTable(422);

         cafeSystem.addMenuItem("Cheese", 15.99, 100);
       // cafeSystem.addMenuItem("Cake", 3.99, 50); 
        //cafeSystem.applyDiscount("Coffee", 13); 
       
       Waiter waiter = new Waiter();
        waiter.takeOrder(cafeSystem, 251, "Coffee", 12);
        //waiter.cancelOrder(cafeSystem, 251);
        waiter.takeOrder(cafeSystem, 2, "Cake", 1);
        waiter.changeOrder(cafeSystem, 251, "Coffee", 1);
      
        Customer customer = new Customer("John Doe");
        customer.payBill(cafeSystem,251);

       // cafeSystem.generateMonthlySalesReport();
       // cafeSystem.generateMonthlySalesGraph();  
    }
}