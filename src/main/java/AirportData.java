import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AirportData implements AccessKeys {

    public HashMap<String, JSONObject> airport = new HashMap<>();
    public List<JSONObject> airportResults = new ArrayList<>();

    public AirportData(String location) throws IOException {

        location = cleanseLocation(location);// removes any Non-letters and replaces spaces with %20

        URL url = new URL(
                "https://www.air-port-codes.com/api/v1/multi?term=" + location + "&limit=5&size=3&countries=US");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("APC-Auth", APC_AUTH);
        connection.setRequestProperty("APC-Auth-Secret", APC_AUTH_SECRET);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        try {
            parseJSON(sb.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*
         * Write Methods to handle each code from API.
         * TODO 1. 200 - Success
         * TODO 2. 204 - No Content
         * TODO 3. 400 - Bad Request
         * TODO 4. 401 - Unauthorized
         * TODO 5. 404 - Not Found...
         */
    }

    String cleanseLocation(String location) {
        location = location.replace(" ", "-");
        ArrayList<String> strList = new ArrayList<>(Arrays.asList(location.split("[^a-zA-Z-]+")));
        String result = String.join("", strList);
        return result.replace("-", "%20");
    }

    void parseJSON(String responseBody) throws ParseException {
        JSONParser parse = new JSONParser();
        JSONObject response = (JSONObject) parse.parse(responseBody);
        JSONArray airports = (JSONArray) response.get("airports");

        airports.stream()
                .map(Object::toString)
                .forEach(n -> {
                    try {
                        airportResults.add((JSONObject) parse.parse((String) n));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                });

        airportResults.forEach(x -> airport.put((String) x.get("iata"), x));

    }

    public JSONObject choice() {
        String name;

        do {
            System.out.println("\nChoose An Airport (Airport Code): ");
            airport.forEach((k, v) -> System.out.println(k + " - " + v.get("name").toString()));
            name = userInput();
        } while (!airport.containsKey(name));

        return airport.get(name);
    }

    private String userInput() {
        Scanner input = new Scanner(System.in);
        return input.nextLine().toUpperCase();
    }

}
