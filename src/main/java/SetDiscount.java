import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SetDiscount extends HttpServlet {
    private final CafeSystem cafeSystem;
    private final Gson gson;

    public SetDiscount(CafeSystem cafeSystem) {
        this.cafeSystem = cafeSystem;
        this.gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DiscountProduct discountProduct = gson.fromJson(req.getReader(), DiscountProduct.class);
        if (discountProduct.discountPercentage < 0 || discountProduct.discountPercentage > 50) {
            System.out.println("Discount Precentage is not correct: " + discountProduct.discountPercentage);
            resp.getWriter().write(gson.toJson(new boolResponse(false)));
            return;
        }
        boolean result = this.cafeSystem.applyDiscount(discountProduct.managerId, discountProduct.itemId,
                discountProduct.discountPercentage);
        if (result) {
            System.out.println("Updated Discount: " + discountProduct.itemId + " value: "
                    + discountProduct.discountPercentage);
        }
        resp.getWriter().write(gson.toJson(new AdjustTableResponse(result)));
    }
}