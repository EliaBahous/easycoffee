import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PayOrder extends HttpServlet {
    private final CafeSystem cafeSystem;
    private final Gson gson;

    public PayOrder(CafeSystem cafeSystem) {
        this.cafeSystem = cafeSystem;
        this.gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            PayBillRequest payBillRequest = gson.fromJson(req.getReader(), PayBillRequest.class);
            this.cafeSystem.recordPayment(payBillRequest.tableNumber);
            String toSend = this.cafeSystem.closeTable(payBillRequest.tableNumber);
            resp.getWriter().write(gson.toJson(toSend));
        } catch (Exception error) {
            resp.getWriter().write(gson.toJson(new boolResponse(false)));
        }
    }
}