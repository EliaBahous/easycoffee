import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class addItem extends HttpServlet {
    private final CafeSystem cafeSystem;
    private final Gson gson;

    public addItem(CafeSystem cafeSystem) {
        this.cafeSystem = cafeSystem;
        this.gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Set content type of the response

        resp.setContentType("application/json");
        System.out.println("addItem-");
        ItemToAdd itemObj = gson.fromJson(req.getReader(), ItemToAdd.class);
        int response = this.cafeSystem.addMenuItem(itemObj.itemId, itemObj.itemName, itemObj.itemPrice, itemObj.itemQuantity, itemObj.itemDiscount);

        resp.getWriter().write(gson.toJson(new AdjustTableResponse(response==1)));
    }

}


class ItemToAdd{
    String itemName;
    int itemId;
    double itemPrice;
    double itemDiscount;
    double itemQuantity;
    public ItemToAdd(String itemName, int itemId, double itemDiscount, double itemPrice, double itemQuantity){
        this.itemName = itemName;
        this.itemDiscount = itemDiscount;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.itemId = itemId;
    }

}