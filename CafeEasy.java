public class CafeEasy {
    public static void main(String[] args) {
        CafeSystem cafeSystem = new CafeSystem();
      
        cafeSystem.addTable(251);
        //cafeSystem.addTable(422);

         cafeSystem.addMenuItem("Cheese", 15.99, 100);
       // cafeSystem.addMenuItem("Cake", 3.99, 50); 
        //cafeSystem.applyDiscount("Coffee", 13); 
       
       Waiter waiter = new Waiter();
        waiter.takeOrder(cafeSystem, 251, "Coffee", 2);
        waiter.cancelOrder(cafeSystem, 251);
        //waiter.takeOrder(cafeSystem, 2, "Cake", 1);

       //  Customer customer = new Customer("John Doe");
      //  customer.placeOrder(cafeSystem, 1, "Cake", 1);
       // customer.payBill(cafeSystem,13);

       // cafeSystem.generateMonthlySalesReport();
       // cafeSystem.generateMonthlySalesGraph();  
    }
}