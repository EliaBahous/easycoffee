import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CancelOrder  extends HttpServlet {
    private final CafeSystem cafeSystem;
    private final Gson gson;

    public CancelOrder(CafeSystem cafeSystem) {
        this.cafeSystem = cafeSystem;
        this.gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            TableRequest tableNumber = gson.fromJson(req.getReader(), TableRequest.class);

            this.cafeSystem.cancelOrder(tableNumber.tableNumber);
            resp.getWriter().write(gson.toJson(new AdjustTableResponse(true)));
        } catch (Exception error) {
            resp.getWriter().write(gson.toJson(new AdjustTableResponse(false)));
        }
    }
}