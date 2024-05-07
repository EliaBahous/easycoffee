import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class GetOrderedItems extends HttpServlet {
    private final CafeSystem cafeSystem;
    private final Gson gson;

    public GetOrderedItems(CafeSystem cafeSystem) {
        this.cafeSystem = cafeSystem;
        this.gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Set content type of the response

        resp.setContentType("application/json");
        System.out.println("GetOrderedItems-");
        TableRequest tableNumber = gson.fromJson(req.getReader(), TableRequest.class);
        List<ProductOrdered> resultOfCurrentOrders = this.cafeSystem.getCurrentMenuItemsForTable(tableNumber.tableNumber);
        ListOfItems resultForItems = new ListOfItems();
        resultForItems.itemList = resultOfCurrentOrders;
        resp.getWriter().write(gson.toJson(resultForItems, ListOfItems.class));
    }

}
