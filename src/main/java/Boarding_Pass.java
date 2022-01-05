import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.json.simple.JSONObject;

/*
 * ASSUMPTIONS: There are few assumptions made to complete the next few
 * calculations.
 * 1. Every plane is a Boeing 747 because it's one of the most sold planes in
 * the world.
 * 2. Every flight is full so the fuel efficiency will be calculated
 * accordingly. (To determine price possibly)
 * 3. A 747 transporting 500 people one-mile uses five gallons of fuel. That
 * means the plane is burning 0.01 gallons per person per mile. (anecdotal information)
 * 4. The plane travels at an average of 550 mph (900 km/h).
 */

public class Boarding_Pass {

    // Variables
    private final double MPH = 550.0;
    private Scanner input = new Scanner(System.in);
    private String boardingPassNumber;
    private LocalTime departureTime;
    private LocalDate departureDate;
    private LocalTime eta;
    private JSONObject origin;
    private JSONObject destination;
    private double distance;
    private double travelTime;
    private int departMonth;
    private int departYear;
    private int departDay;
    private int departHour;
    private int departMinute;
    private DataStore data = new DataStore();
    private Boolean append = true;

    // Constructors
    Boarding_Pass() throws IOException {
        init();
    }

    Boarding_Pass(JSONObject origin, JSONObject destination, LocalDate departureDate,LocalTime departureTime) throws IOException {
        this.setBoardingPassNumber(createBoardingPassNumber());
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.calculateDistance();
        this.calcEta();
        this.storeData();
    }

    // Setters
    public void setDepartMonth(int departMonth) {
        this.departMonth = departMonth;
    }

    public void setDepartYear(int departYear) {
        this.departYear = departYear;
    }

    public void setDepartDay(int departDay) {
        this.departDay = departDay;
    }

    public void setDepartHour(int departHour) {
        this.departHour = departHour;
    }

    public void setDepartMinute(int departMinute) {
        this.departMinute = departMinute;
    }

    public void setBoardingPassNumber(String boardingPassNumber) {
        this.boardingPassNumber = boardingPassNumber;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setDepartureTime(int hourOfDay, int minute) {
        this.departureTime = LocalTime.of(hourOfDay, minute);
    }

    public void setDepartureTime() {
        this.getDepartureDate();
        this.getTime();
        this.setDepartureTime(this.departHour, this.departMinute);
        this.setDepartureDate(this.departYear, this.departMonth, this.departDay);
    }

    private void setDepartureDate(int departYear, int departMonth, int departDay) {
        this.departureDate = LocalDate.of(departYear,departMonth,departDay);
    }

    public void setDestination(JSONObject destination) {
        this.destination = destination;
    }

    public void setDestination() {
        AirportData apd = null;
        String state;
        do {
            System.out.println("\nDestination (US): ");
            state = input.nextLine();
            try {
                apd = new AirportData(state);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!Objects.requireNonNull(apd).status);
        this.setDestination(apd.choice());
        try {
            data.writeToAJSON("output/"+apd.cleanseLocation(state).replace("%20","").toUpperCase()+"_Airports.json","{\"purpose\":\"destination\"}","]",true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setEta(LocalTime eta) {
        this.eta = eta;
    }

    public void setOrigin(JSONObject origin) {
        this.origin = origin;
    }

    public void setOrigin() {
        AirportData apd = null;
        String state;
        do {
            System.out.println("\nFlying From (US): ");
            state = input.nextLine();
            try {
                apd = new AirportData(state);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!Objects.requireNonNull(apd).status);

        this.setOrigin(apd.choice());
        try {
            data.writeToAJSON("output/"+apd.cleanseLocation(state).replace("%20","").toUpperCase()+"_Airports.json","{\"purpose\" : \"origin\"}","]",true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters
    public LocalTime getDepartureTime() {
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

    public int getDepartMonth() {
        return this.departMonth;
    }

    public int getDepartYear() {
        return this.departYear;
    }

    public int getDepartDay() {
        return this.departDay;
    }

    public int getDepartHour() {
        return this.departHour;
    }

    public int getDepartMinute() {
        return this.departMinute;
    }

    private void getTime() {
        boolean inputIsGood = false,goodTime = false;
        int originalHour = 0;

        do {
            try {
                System.out.println("\nWhat Time (HH:MM)? ");
                String in = input.nextLine();
                String[] value = in.split("[\\D:]+");
                this.setDepartHour(Integer.parseInt(value[0]));
                originalHour = Integer.parseInt(value[0]);
                this.setDepartMinute(getMinute(Integer.parseInt(value[1])));
                if(this.getDepartMonth()<=12){
                    goodTime = true;
                } else {
                    System.err.println(in);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (!goodTime);

        do {
            try {
                System.out.println("AM or PM: ");
                String value = input.nextLine().toUpperCase();
                switch (value) {
                    case "P":
                    case "PM":
                        if (this.getDepartHour() < 12) {
                            this.departHour += 12;
                        }
                        inputIsGood = true;
                        break;
                    case "A":
                    case "AM":
                        if (this.departHour == 12 && originalHour != 11) {
                            this.setDepartHour(0);
                        }
                        inputIsGood = true;
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (!inputIsGood);
    }

    private int getMinute(int min) {// flights in 15 min increments
        if (min > 45) {
            this.departHour +=1;
            return 0;
        } else if (min > 30) {
            return 45;
        } else if (min > 15) {
            return 30;
        }
        return 15;
    }

    private int getDay(int month, int day) {
        if (day <= Month.of(month).length(false)) {
            return day;
        }
        return 0;
    }

    private void getDepartureDate(){
        boolean goodInputs = false;
        do {
            System.out.println("\nDeparture Date MM/DD/YEAR: ");
            try {
                String in = input.nextLine();
                String[] values = in.split("[\\D/]+");
                int[] results = Arrays.stream(values).mapToInt(Integer::parseInt).toArray();
                this.setDepartMonth(results[0]);
                this.setDepartDay(getDay(this.getDepartMonth(), results[1]));
                this.setDepartYear(results[2]);
                if (this.getDepartMonth() <= 12 && this.getDepartDay() > 0 && this.getDepartYear() > 2021) {
                    goodInputs = true;
                    System.out.println(Month.of(this.getDepartMonth()) +
                            " " + this.getDepartDay() +
                            ", " + this.getDepartYear());
                } else {
                    System.err.println(in);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        } while (!goodInputs);

    }

    // Methods
    private void init() throws IOException {
        this.setBoardingPassNumber(createBoardingPassNumber());
        this.setOrigin();
        this.setDestination();
        this.setDepartureTime();
        this.calculateDistance();
        this.calcEta();
        System.out.println();
        this.dateNF(this.departureDate, "Flight");
        this.timeNF(this.departureTime,"Departure");
        this.timeNF(this.eta,"Arrival");
        Formatter travel = new Formatter();
        travel.format("%1.1f",this.getDistance());
        System.out.println("Total Miles: " + travel);
        this.storeData();
    }

    public void calcEta() {
        this.travelTime = this.getDistance()/MPH*60;
        double mins = this.travelTime;
        int hours = 0;
        while (mins > 60){
            hours++;
            mins-= 60;
        }
        System.out.println("\nTime in flight: "+ hours +"h "+ (int)Math.round(mins) + "m");
        this.setEta(this.getDepartureTime().plusMinutes((long) this.travelTime));

    }

    public void storeData() throws IOException {
        String fileName = "output/BoardingPass.txt";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a");
        data.writeToAFile(fileName,"Boarding_Pass_Number",this.boardingPassNumber,!append);
        data.writeToAFile(fileName,"Departure_Date", this.departureDate.format(dateFormat),append);
        data.writeToAFile(fileName,"Departure_Time", this.getDepartureTime().format(timeFormat),append);
        data.writeToAFile(fileName,"Arrival_Time", this.getEta().format(timeFormat),append);
        data.writeToAFile(fileName,"Origin", this.origin.get("iata").toString()+ " - " + this.origin.get("name").toString(),append);
        data.writeToAFile(fileName,"Destination", this.destination.get("iata").toString()+ " - " + this.destination.get("name").toString(),append);
        data.writeToAFile(fileName,"Total_Miles", String.valueOf(this.distance),append);
    }

    private void calculateDistance() {
        double lat1 = Double.parseDouble((String) this.origin.get("latitude"));
        double lat2 = Double.parseDouble((String) this.destination.get("latitude"));
        double lon1 = Double.parseDouble((String) this.origin.get("longitude"));
        double lon2 = Double.parseDouble((String) this.destination.get("longitude"));
        double earthRadius = 3963.0;// Miles

        this.setDistance (earthRadius * Math.acos( // convert coordinates to distances
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
                Math.sin(Math.toRadians(lat2)) ));
    }

    public String createBoardingPassNumber() {
        // Take the current time from Date Class (nanoseconds) and convert it to
        // Hexadecimal
        StringBuilder passNumber = new StringBuilder();
        long remainder;
        Date pass = new Date();
        long numToConvert = pass.getTime();
        char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

        int dash = 0;
        while (numToConvert > 0) {
            remainder = numToConvert % 16;
            passNumber.append(hex[(int) remainder]);
            numToConvert /= 16;
            dash++;
            if (dash % 4 == 0) {
                passNumber.append("-");
            }
        }

        return passNumber.toString();
    }

    public void dateNF(LocalDate date, String what){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        System.out.println(what+" Date: " + formatter.format(date));
    }

    public void timeNF(LocalTime time, String when) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        System.out.println(when+" Time: " + formatter.format(time));
    }
}
