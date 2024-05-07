import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

class GetItems extends HttpServlet {
    private final CafeSystem cafeSystem;
    private final Gson gson;

    public GetItems(CafeSystem cafeSystem) {
        this.cafeSystem = cafeSystem;
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Set content type of the response
        resp.setContentType("application/json");
        try {
            List<ProductOrdered> resultOfCurrentOrders = this.cafeSystem.getAllMenuItem();


            if (!resultOfCurrentOrders.isEmpty()) {
                ListOfItems resultForItems = new ListOfItems();
                resultForItems.itemList = resultOfCurrentOrders;

                resp.getWriter().write(gson.toJson(resultForItems, ListOfItems.class));
            }
        } catch (Exception error) {
            resp.getWriter().write(gson.toJson(new AdjustTableResponse(false)));
        }
    }

}