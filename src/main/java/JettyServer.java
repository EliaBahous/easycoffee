import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.sparkproject.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class JettyServer {

    public static void main(String[] args) throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        CafeSystem cafeSystem = new CafeSystem();
        Server server = new Server();

        // Configure HTTP connector
        ServerConnector httpConnector = new ServerConnector(server);
        httpConnector.setPort(8080); // Set the port number for HTTP
        server.addConnector(httpConnector);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        FilterHolder cors = context.addFilter(CrossOriginFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        cors.setInitParameter("allowedOrigins", "*"); // Set allowed origins, "*" allows all origins
        cors.setInitParameter("allowedMethods", "GET,POST,PUT,DELETE,OPTIONS"); // Set allowed HTTP methods
        cors.setInitParameter("allowedHeaders", "Content-Type,Authorization"); // Set allowed headers

        context.addServlet(new ServletHolder(new GetTableCounterServlet(cafeSystem)), "/get-number-of-tables");
        context.addServlet(new ServletHolder(new GetItems(cafeSystem)), "/get-items");
        context.addServlet(new ServletHolder(new GetOrderedItems(cafeSystem)), "/get-ordered-items");
        context.addServlet(new ServletHolder(new AdjustTable(cafeSystem)), "/adjust-table");
        context.addServlet(new ServletHolder(new CancelOrder(cafeSystem)), "/cancel-orders");
        context.addServlet(new ServletHolder(new PayOrder(cafeSystem)), "/pay-bill");
        context.addServlet(new ServletHolder(new SetDiscount(cafeSystem)), "/set-discount");
        context.addServlet(new ServletHolder(new SetQuantity(cafeSystem)), "/set-quantity");
        context.addServlet(new ServletHolder(new ItemOrderHistory(cafeSystem)), "/get-orders");
        context.addServlet(new ServletHolder(new addItem(cafeSystem)), "/add-new-item");
        context.addServlet(new ServletHolder(new AddTableRequest(cafeSystem)), "/add-table");
        String webDir = System.getProperty("user.dir");
        System.out.println(webDir);
        String resourceBase = webDir + "/src/main/resources";
        System.out.println(resourceBase);
        context.setResourceBase(resourceBase);
        context.addServlet(DefaultServlet.class, "/*");

        server.start();
        server.join();
    }
}
