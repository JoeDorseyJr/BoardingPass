import org.json.simple.JSONObject;

import java.io.IOException;
import java.time.*;
import java.util.Date;
import java.util.Scanner;

public class Boarding_Pass {
    private String boardingPassNumber;
    private LocalDateTime departureTime;
    private JSONObject origin;
    private JSONObject destination;
    private LocalTime eta;
    private double distance;

    Boarding_Pass(){
        init();
    }

    Boarding_Pass(LocalDateTime departureTime, JSONObject origin, JSONObject destination){
        this.setBoardingPassNumber(createBoardingPassNumber());
        this.departureTime = departureTime;
        this.origin = origin;
        this.destination = destination;
        calculateDistance();
        calcEta();
        storeData();
    }

    //Setters
    public void setBoardingPassNumber(String boardingPassNumber){
        this.boardingPassNumber = boardingPassNumber;
    }

    public void setDepartureTime(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
        this.departureTime = LocalDateTime.of(LocalDate.of(year,month,dayOfMonth),LocalTime.of(hourOfDay,minute));
    }
    public void setDepartureTime() {
        /*TODO Methods for Natural Language input for Date and Time
        * 1. Create a method for date MM/DD/YEAR
        * 2. Create a method for Time N:NN -> AM || PM
        * */

        System.out.println("Year: ");
        int year = Integer.parseInt(userInput());
        System.out.println("Month: ");
        int month = Integer.parseInt(userInput());
        int dayOfMonth = 0;
        do {
            System.out.println("Day: ");
            int day = Integer.parseInt(userInput());
            dayOfMonth = getDay(month, day);
            System.out.println("YUP");
        } while (dayOfMonth == 0);
        System.out.println("Hour: ");
        int hourOfDay = Integer.parseInt(userInput());
        System.out.println("Minute: ");
        int min = Integer.parseInt(userInput());
        int minute = getMinute(min);

        this.setDepartureTime(year,month,dayOfMonth,hourOfDay,minute);
    }

    private int getMinute(int min) {// flights in 15 min increments
        if (min>45) {
            return 60;
        } else if (min > 30){
            return 45;
        } else if (min > 15){
            return 30;
        }
        return 15;
    }

    public void setDestination(JSONObject destination) {
        this.destination = destination;
    }

    public void setDestination() {
        System.out.println("Destination(State): ");
        String state = userInput();

        AirportData apd = null;
        try {
            apd = new AirportData(state);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert apd != null;
        this.setDestination(apd.choice());
    }


    public void setEta(LocalTime eta) {
        this.eta = eta;
    }

    public void setOrigin(JSONObject origin) {
        this.origin = origin;
    }

    public void setOrigin() {
        System.out.println("Origin (State): ");
        String state = userInput();

        AirportData apd = null;
        try {
            apd = new AirportData(state);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert apd != null;
        this.setOrigin(apd.choice());
    }

    //Getters
    public LocalDateTime getDepartureTime() {
        return this.departureTime;
    }

    public LocalTime getEta() {
        return this.eta;
    }

    public String getBoardingPassNumber() {
        return this.boardingPassNumber;
    }

    public JSONObject getDestination() {
        return this.destination;
    }

    public JSONObject getOrigin() {
        return this.origin;
    }

    public double getDistance() {
        return this.distance;
    }

    /* ASSUMPTIONS: There are few assumptions made to complete the next few calculations.
   1. Every plane is a Boeing 747 because it's one of the most sold planes in the world.
   2. Every flight is full so the fuel efficiency will be calculated accordingly. (To determine price possibly)
   3. A 747 transporting 500 people one-mile uses five gallons of fuel. That means the plane is burning 0.01 gallons per person per mile.
   4. The plane travels at 550 mph (900 km/h).
    */

    //Methods
    private void init(){
        this.setBoardingPassNumber(createBoardingPassNumber());
        this.setOrigin();
        this.setDestination();
        this.setDepartureTime();

    }

    public LocalTime calcEta() {
        //TODO calculate the ETA from distances. The origin,destination, and departureTime.
        return this.getEta();
    }

    public void storeData() {
        //TODO output the data from the instance to a csv file.
    }

    private void calculateDistance() {
        double lat1 = Double.parseDouble((String) this.origin.get("latitude"));
        double lat2 = Double.parseDouble((String) this.destination.get("latitude"));
        double lon1 = Double.parseDouble((String) this.origin.get("longitude"));
        double lon2 = Double.parseDouble((String) this.destination.get("longitude"));
        double earthRadius = 3963.0;// Miles

        this.distance = earthRadius * Math.acos( //convert coordinates to distances
                Math.cos(Math.toRadians(lat1)) *
                Math.cos(Math.toRadians(lon1)) *
                Math.cos(Math.toRadians(lat2)) *
                Math.cos(Math.toRadians(lon2))
                +
                Math.cos(Math.toRadians(lat1)) *
                Math.sin(Math.toRadians(lon1)) *
                Math.cos(Math.toRadians(lat2)) *
                Math.sin(Math.toRadians(lon2))
                +
                Math.sin(Math.toRadians(lat1)) *
                Math.sin(Math.toRadians(lat2)) );
    }

    public String createBoardingPassNumber(){
        //Take the current time from Date Class (nanoseconds) and convert it to Hexadecimal
        StringBuilder passNumber = new StringBuilder();
        long remainder;
        Date pass = new Date();
        long numToConvert = pass.getTime();
        char[] hex = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

        int dash = 0;
        while (numToConvert > 0){
            remainder = numToConvert % 16;
            passNumber.append(hex[(int) remainder]);
            numToConvert /= 16;
            dash++;
            if (dash%4 == 0){
                passNumber.append("-");
            }
        }

        System.out.println(passNumber);
        return passNumber.toString();
    }

    private int getDay(int month, int day){
        if(day <= Month.of(month).length(false)){
            return day;
        }
        return 0;
    }

    private String userInput(){
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }
}
