import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ItemOrderHistory extends HttpServlet {
    private final CafeSystem cafeSystem;
    private final Gson gson;

    public ItemOrderHistory(CafeSystem cafeSystem) {
        this.cafeSystem = cafeSystem;
        this.gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {

            List<OrderHistory> listOfHistory = this.cafeSystem.getSumOfOrders();
            resp.getWriter().write(gson.toJson(listOfHistory));
        } catch (Exception error) {
            resp.getWriter().write(gson.toJson(new boolResponse(false)));
        }
    }
}