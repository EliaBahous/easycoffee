import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SetQuantity extends HttpServlet {
    private final CafeSystem cafeSystem;
    private final Gson gson;

    public SetQuantity(CafeSystem cafeSystem) {
        this.cafeSystem = cafeSystem;
        this.gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            QuantityRequest adjustItemQuantity = gson.fromJson(req.getReader(), QuantityRequest.class);
            if (adjustItemQuantity.itemQty < 0) {
                resp.getWriter().write(gson.toJson(new boolResponse(false)));
            }
            boolean result = this.cafeSystem.updatedQuantity(adjustItemQuantity.itemId, adjustItemQuantity.itemQty);
            if (result) {
                System.out.println(
                        "Updated quantity: " + adjustItemQuantity.itemId + " qty: " + adjustItemQuantity.itemQty);
            }
            resp.getWriter().write(gson.toJson(new AdjustTableResponse(result)));
        } catch (Exception error) {
            resp.getWriter().write(gson.toJson(new AdjustTableResponse(false)));
        }
    }
}