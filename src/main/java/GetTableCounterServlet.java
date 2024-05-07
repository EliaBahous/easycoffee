import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class GetTableCounterServlet extends HttpServlet {
    private final CafeSystem cafeSystem;
    private final Gson gson;

    public GetTableCounterServlet(CafeSystem cafeSystem) {
        this.cafeSystem = cafeSystem;
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Set content type of the response

        resp.setContentType("application/json");
        System.out.println("GetTableCounterServlet-");
        int currentNumOfTables = this.cafeSystem.getNumberOfTables();
        NumberOfTables resultForTables = new NumberOfTables();
        resultForTables.numberOfTables = currentNumOfTables;

        String jsonResponse = this.gson.toJson(resultForTables);

        resp.getWriter().write(jsonResponse);
        return;
    }

}