import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


public class AirportData implements AccessKeys {

    public AirportData(String location) throws IOException {

        location = cleanseLocation(location);//removes any Non-letters and replaces spaces with %20

        URL url = new URL("https://www.air-port-codes.com/api/v1/multi?term="+location+"&limit=5&size=3&countries=US");
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
        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    String cleanseLocation(String location){
        location = location.replace(" ","-");
        ArrayList<String> strList = new ArrayList<>(Arrays.asList(location.split("[^a-zA-Z-]+")));
        String result = String.join("", strList);
        return result.replace("-","%20");
    }

    void parseJSON (String responseBody) throws ParseException {
        JSONParser parse = new JSONParser();
        JSONObject response = (JSONObject) parse.parse(responseBody);
        JSONArray airports = (JSONArray) response.get("airports");


        System.out.println(response.toJSONString());
        airports.stream()
                .map(x -> x.toString())
                .forEach(System.out::println);
    }
}
