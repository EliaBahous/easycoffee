package Table;

import java.io.IOException;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

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

public class CloseTable {

    @SuppressWarnings("deprecation")
    @Test
    public void CloseTableAndPay() throws IOException {
        URL obj = new URL("http://localhost:8080/pay-bill");
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
        assertEquals(responseCode, 200);
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

            assertEquals(response.toString().contains(" 1 is Available For New Orders"), true);

        } else {
            System.out.println("GET request not worked");
        }

    }

    @SuppressWarnings("deprecation")
    @Test
    public void closeTableAlreadyClosed() throws IOException {
        URL obj = new URL("http://localhost:8080/pay-bill");
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

            assertEquals(response.toString().contains(" 1 is Available For New Orders"),
                    true);

        } else {
            System.out.println("GET request not worked");
        }

    }

}
