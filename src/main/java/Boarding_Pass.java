import org.json.simple.JSONObject;

import java.util.Calendar;

public class Boarding_Pass {
    private String boardingPassNumber;
    private Calendar date;
    private Calendar departureTime;
    private JSONObject origin;
    private JSONObject destination;
    private Calendar eta;
    private double distance;

    Boarding_Pass(){}

    Boarding_Pass(String boardingPassNumber, Calendar date, Calendar departureTime, JSONObject origin, JSONObject destination){
        this.boardingPassNumber = boardingPassNumber;
        this.date = date;
        this.departureTime = departureTime;
        this.origin = origin;
        this.destination = destination;
        calculateDistance();
        calcEta(origin,destination,departureTime);
        storeData();
    }

    //Setters
    public void setBoardingPassNumber(String boardingPassNumber){
        this.boardingPassNumber = boardingPassNumber;
    }

    public void setDate(int year, int month, int day) {
        Calendar date = null;
        date.set(year,month,day);
        this.date = date;
    }

    public void setDepartureTime(int year, int month, int date, int hourOfDay, int minute) {
        Calendar departureTime = null;
        assert false;
        departureTime.set(year,month,date,hourOfDay,minute);
        this.departureTime = departureTime;
    }

    public void setDestination(JSONObject destination) {
        this.destination = destination;
    }

    public void setEta(Calendar eta) {
        this.eta = eta;
    }

    public void setOrigin(JSONObject origin) {
        this.origin = origin;
    }

    //Getters
    public Calendar getDate() {
        return this.date;
    }

    public Calendar getDepartureTime() {
        return this.departureTime;
    }

    public Calendar getEta() {
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

    /* ASSUMPTIONS: There are few assumptions made to complete the next few calculations.
   1. Every plane is a Boeing 747 because it's one of the most sold planes in the world.
   2. Every flight is full so the fuel efficiency will be calculated accordingly. (To determine price possibly)
   3. A 747 transporting 500 people one-mile uses five gallons of fuel. That means the plane is burning 0.01 gallons per person per mile.
   4. The plane travels at 550 mph (900 km/h).
    */

    //Methods
    public Calendar calcEta(){
        //TODO calculate the ETA from distances. The origin,destination, and departureTime can not be null.
        return this.getEta();
    }

    public Calendar calcEta(JSONObject origin, JSONObject destination, Calendar departureTime){
        //TODO create a list of origins and destinations to have set distances between airports.
        return this.getEta();
    }

    public void storeData(){
        //TODO output the data from the instance to a csv file.
    }

    private void calculateDistance(){
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

}
