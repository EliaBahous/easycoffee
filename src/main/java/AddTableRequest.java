import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddTableRequest extends HttpServlet {
    private final CafeSystem cafeSystem;
    private final Gson gson;

    public AddTableRequest(CafeSystem cafeSystem) {
        this.cafeSystem = cafeSystem;
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Set content type of the response
        try {
            resp.setContentType("application/json");
            System.out.println("AddTableRequest-");
            this.cafeSystem.addTable();
            resp.getWriter().write(gson.toJson(new AdjustTableResponse(true)));
        } catch (Exception error) {
            resp.getWriter().write(gson.toJson(new AdjustTableResponse(false)));
        }
    }
}