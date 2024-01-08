package infotrix;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrencyConverter 
{
    private static final String API_KEY = "YOUR_OPEN_EXCHANGE_RATES_API_KEY";
    private static final String BASE_URL = "https://open.er-api.com/v6/latest/";

    public static double convertCurrency(String fromCurrency, String toCurrency, double amount) throws IOException 
    {
        String apiUrl = BASE_URL + API_KEY;
        URL url = new URL(apiUrl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) 
        {
            response.append(line);
        }

        reader.close();
        connection.disconnect();

        // Parse the JSON response to get exchange rates
        // Note: You might want to use a JSON library for more robust parsing
        double fromRate = parseExchangeRate(response.toString(), fromCurrency);
        double toRate = parseExchangeRate(response.toString(), toCurrency);

        // Convert the amount
        return amount * (toRate / fromRate);
    }

    private static double parseExchangeRate(String jsonResponse, String currencyCode) {
        // Implement parsing logic based on your JSON response format
        // For simplicity, assume a direct mapping between currency codes and rates
        // You may need to adjust this based on the actual response format
        // Example: {"rates":{"USD":1.0,"EUR":0.85,"GBP":0.72}, ...}
        // This assumes a JSON structure where rates are stored in a "rates" object
        int startIndex = jsonResponse.indexOf("\"" + currencyCode + "\":");
        int endIndex = jsonResponse.indexOf(",", startIndex);
        String rateSubstring = jsonResponse.substring(startIndex, endIndex);
        return Double.parseDouble(rateSubstring.split(":")[1]);
    }

    public static void main(String[] args) 
    {
    	try {
            String fromCurrency = "USD";
            String toCurrency = "EUR";
            double amount = 100.0;

            double convertedAmount = convertCurrency(fromCurrency, toCurrency, amount);
            System.out.println(amount + " " + fromCurrency + " is equal to " + convertedAmount + " " + toCurrency);
        } 
    	catch (IOException e) 
    	{
            e.printStackTrace();
        }

    }
}