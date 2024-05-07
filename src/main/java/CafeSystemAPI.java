import java.util.List;

class NumberOfTables {
    int numberOfTables;
}

// Define the table for orders
class TableRequest {
    int tableNumber;
}

// Define list of menus
class ListOfItems {
    List<ProductOrdered> itemList;
}

// Define set discount from item
class DiscountProduct {
    int itemId;
    double discountPercentage;
    String managerId;
}

// Define set quantities request
class QuantityRequest {
    int itemId;
    double itemQty;
}

// Define request and response classes
class OpenTableRequest {
    int tableNumber;
    int itemId;
    int itemQty;
}

class PayBillRequest {
    int tableNumber;
}

class AdjustTableRequest {
    int tableNumber;
    int itemId;
    int quantity;
}

class AdjustTableResponse {
    boolean success;

    public AdjustTableResponse(boolean success) {
        this.success = success;
    }
}

class boolResponse {
    boolean success;

    public boolResponse(boolean success) {
        this.success = success;
    }
}
