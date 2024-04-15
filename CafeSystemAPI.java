import com.google.gson.Gson;

import static spark.Spark.*;

import java.util.List;

public class CafeSystemAPI {

    private CafeSystem cafeSystem;
    private Gson gson;

    public CafeSystemAPI() {
        this.cafeSystem = new CafeSystem(); // Assuming you have a CafeSystem class
        this.gson = new Gson();
        setupRoutes();
    }

    private void setupRoutes() {
        // Endpoint to open a table
        post("/take-order", "application/json", (req, res) -> {
            OpenTableRequest openTableRequest = gson.fromJson(req.body(), OpenTableRequest.class);
            this.cafeSystem.placeOrder(openTableRequest.tableNumber, openTableRequest.itemId, openTableRequest.itemQty);
            return gson.toJson(new String("Good Choice!!!"));
        });

        // Endpoint to pay the bill
        post("/pay-bill", "application/json", (req, res) -> {
            try {
                PayBillRequest payBillRequest = gson.fromJson(req.body(), PayBillRequest.class);
                this.cafeSystem.recordPayment(payBillRequest.tableNumber);
                String toSend = this.cafeSystem.closeTable(payBillRequest.tableNumber);
                return gson.toJson(toSend);
            } catch (Exception error) {
                return gson.toJson(new boolResponse(false));
            }
        });

        // Endpoint to adjust a table
        post("/adjust-table", "application/json", (req, res) -> {
            try {
                AdjustTableRequest adjustTableRequest = gson.fromJson(req.body(), AdjustTableRequest.class);
                this.cafeSystem.changeOrder(adjustTableRequest.tableNumber, adjustTableRequest.itemId, adjustTableRequest.quantity);
                return gson.toJson(new AdjustTableResponse(true));
            } catch (Exception error) {
                return gson.toJson(new AdjustTableResponse(false));
            }
        });

        // Endpoint to set quantity
        post("/set-quantity", "application/json", (req, res) -> {
            try {
                QuantityRequest adjustItemQuantity = gson.fromJson(req.body(), QuantityRequest.class);
                if (adjustItemQuantity.itemQty < 0) {
                    return gson.toJson(new boolResponse(false));
                }
                boolean result = this.cafeSystem.updatedQuantity(adjustItemQuantity.itemId, adjustItemQuantity.itemQty);
                if (result) {
                    System.out.println(
                            "Updated quantity: " + adjustItemQuantity.itemId + " qty: " + adjustItemQuantity.itemQty);
                }
                return gson.toJson(new AdjustTableResponse(result));
            } catch (Exception error) {
                return gson.toJson(new AdjustTableResponse(false));
            }
        });

        // Endpoint to discount on product
        post("/set-discount", "application/json", (req, res) -> {
            try {
                DiscountProduct discountProduct = gson.fromJson(req.body(), DiscountProduct.class);
                if (discountProduct.discountPercentage < 0 || discountProduct.discountPercentage > 100) {
                    System.out.println("Discount Precentage is not correct: " + discountProduct.discountPercentage);
                    return gson.toJson(new boolResponse(false));
                }
                boolean result = this.cafeSystem.applyDiscount(discountProduct.managerId, discountProduct.itemId,
                        discountProduct.discountPercentage);
                if (result) {
                    System.out.println("Updated Discount: " + discountProduct.itemId + " value: "
                            + discountProduct.discountPercentage);
                }
                return gson.toJson(new AdjustTableResponse(result));
            } catch (Exception error) {
                return gson.toJson(new AdjustTableResponse(false));
            }
        });

        // Return list of product for specified table
        post("/get-ordered-items", "application/json", (req, res) -> {
            try {
                TableRequest tableNumber = gson.fromJson(req.body(), TableRequest.class);

                List<ProductOrdered> resultOfCurrentOrders = this.cafeSystem
                        .getCurrentMenuItemsForTable(tableNumber.tableNumber);
                if (resultOfCurrentOrders.size() > 0) {
                    ListOfItems resultForItems = new ListOfItems();
                    resultForItems.itemList = resultOfCurrentOrders;
                    return gson.toJson(resultForItems, ListOfItems.class);
                }
                return gson.toJson(new AdjustTableResponse(false));
            } catch (Exception error) {
                return gson.toJson(new AdjustTableResponse(false));
            }
        });
    }
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
