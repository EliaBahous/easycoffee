package tests.OrderProduct;

import java.io.IOException;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import junit.framework.Assert;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.lang.reflect.Type;

// Define product as name and price
class ProductOrdered {
    String name;
    double price;
    double quantity;

    public ProductOrdered(String name, double price, double quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}

public class orderProductTests {

    @SuppressWarnings("deprecation")
    @Test
    public void orderProduct() throws IOException {
        URL obj = new URL( "http://localhost:4567/adjust-table");
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setDoOutput(true);
        String jsonInputString = "{\"tableNumber\": 1, \"itemId\": 101010, \"quantity\": 1}";
        try (OutputStream os = httpURLConnection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        int responseCode = httpURLConnection.getResponseCode();

        System.out.println("POST Response Code :: " + responseCode);
        Assert.assertEquals(responseCode, 200);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());

            Assert.assertEquals(response.toString().contains("{\"success\":true}"), true);

        } else {
            System.out.println("GET request not worked");
        }

    }

    @SuppressWarnings("deprecation")
    @Test
    public void checkProductAfterOrder() throws IOException {
        URL obj = new URL( "http://localhost:4567/get-ordered-items");
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setDoOutput(true);
        String jsonInputString = "{\"tableNumber\": 1}";
        try (OutputStream os = httpURLConnection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        int responseCode = httpURLConnection.getResponseCode();

        System.out.println("POST Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());

            Assert.assertEquals(response.toString().contains("{\"name\":\"Coffee\",\"price\":14.0,\"quantity\":1.0}"),
                    true);

        } else {
            System.out.println("GET request not worked");
        }

    }



}
