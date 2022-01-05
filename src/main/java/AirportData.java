import env.AccessKeys;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


public class AirportData implements AccessKeys {

    public HashMap<String, JSONObject> airport = new HashMap<>();
    public List<JSONObject> airportResults = new ArrayList<>();
    public JSONObject response;
    private Scanner input = new Scanner(System.in);
    public Boolean status;
    final int[] fileAppend = {0};
    String location;

    public AirportData(String area) throws IOException {

        this.location = cleanseLocation(area).toUpperCase();// removes any Non-letters and replaces spaces with %20

        URL url = new URL(
                "https://www.air-port-codes.com/api/v1/multi?term=" + this.location + "&limit=5&size=3&countries=US");
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

    }

    public String cleanseLocation(String location) {
        location = location.replace(" ", "-");
        ArrayList<String> strList = new ArrayList<>(Arrays.asList(location.split("[^a-zA-Z-]+")));
        String result = String.join("", strList);
        return result.replace("-", "%20");
    }

    void parseJSON(String responseBody) throws ParseException {
        DataStore data = new DataStore();
        JSONParser parse = new JSONParser();
        response = (JSONObject) parse.parse(responseBody);
        status = (Boolean) response.get("status");

        if (status) {
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
            airportResults.forEach(x -> {
                boolean append = true;
                fileAppend[0]++;
                try {
                    if (!(fileAppend[0] > 1) == append) {
                        data.writeToAFile("output/" + this.location.replace("%20", "") + "_Airports.json", "[{\"airports\"", x.toString()+"},", ((fileAppend[0] > 1) == append));
                    } else {
                        data.writeToAFile("output/" + this.location.replace("%20", "") + "_Airports.json", "{\"airports\"", x.toString()+"},", ((fileAppend[0] > 1) == append));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            System.err.println(response.get("statusCode")+" - "+response.get("message"));
        }
    }

    public JSONObject choice() {
        String name;

        do {
            System.out.println("\nPlease select an airport (Airport Code): ");
            airport.forEach((k, v) -> System.out.println(k + " - " + v.get("name").toString()));
            name = input.nextLine().toUpperCase();
        } while (!airport.containsKey(name));

        return airport.get(name);
    }


}
