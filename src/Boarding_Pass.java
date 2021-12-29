import java.util.Calendar;

public class Boarding_Pass {
    private String boardingPassNumber;
    private Calendar date;
    private Calendar departureTime;
    private String origin;
    private String destination;
    private Calendar eta;

    Boarding_Pass(){}

    Boarding_Pass(String boardingPassNumber, Calendar date, Calendar departureTime, String origin, String destination){
        this.boardingPassNumber = boardingPassNumber;
        this.date = date;
        this.departureTime = departureTime;
        this.origin = origin;
        this.destination = destination;
        calcEta(origin,destination,departureTime);
    }

    //Setters
    public void setBoardingPassNumber(String boardingPassNumber){
        this.boardingPassNumber = boardingPassNumber;
    }

    public void setDate(Calendar date){
        this.date = date;
    }

    public void setDepartureTime(Calendar departureTime) {
        this.departureTime = departureTime;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setEta(Calendar eta) {
        this.eta = eta;
    }

    public void setOrigin(String origin) {
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

    public String getDestination() {
        return this.destination;
    }

    public String getOrigin() {
        return this.origin;
    }

    //Methods
    public Calendar calcEta(){
        //TODO calculate the ETA from distances. The origin,destination, and  departureTime can not be null.
        return this.getEta();
    }

    public Calendar calcEta(String origin, String destination, Calendar departureTime){
        //TODO create a list of origins and destinations to have set distances between airports.
        return this.getEta();
    }
}
