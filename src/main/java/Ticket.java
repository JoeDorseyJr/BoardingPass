import java.io.*;
import java.util.Formatter;
import java.util.HashMap;

public class Ticket {
    /*
     * ASSUMPTIONS: There are few assumptions made to complete the next few
     * calculations.
     * 1. Every plane is a Boeing 747 because it's one of the most sold planes in
     * the world.
     * 2. Every flight is full so the fuel efficiency will be calculated
     * accordingly. (To determine price possibly)
     * 3. A 747 transporting 500 people one-mile uses five gallons of fuel. That
     * means the plane is burning 0.01 gallons per person per mile. *Equation: 0.01 gal/mile * total miles * fuel price*
     * 4. Average Jet-A fuel price is $5.24 per gal according to https://www.globalair.com/airport/region.aspx
     * 5. TAXES and FEE - https://www.airlines.org/dataset/government-imposed-taxes-on-air-transportation/
     *
     */
    private final double BASE_AIRFARE$ = 15.00;
    private final double FEDERAL_TAX = .075;
    private final double LUGGAGE$ = 10.00;
    private final double FLIGHT_SEGMENT_TAX$ = 4.50;
    private final double SEPT911$ = 5.60;

    private double gpm = 0.01;
    private double avgFuelPrice = 5.24;
    private double price = 0.0;
    private HashMap<String,String> fetch = new HashMap<>();
    private DataStore data = new DataStore();

    Ticket() throws IOException {
        new Passenger();
        new Boarding_Pass();

        fetch.putAll(readFile("output/BoardingPass.txt"));
        fetch.putAll(readFile("output/Passenger.txt"));
        this.setPrice(this.calcPrice(Double.parseDouble(fetch.get("Total_Miles")),Double.parseDouble(fetch.get("DISCOUNT"))));
        this.storeData();
        this.output();
    }

// Setters
    public void setPrice(double price){
        this.price = price;
    }

    public void setGpm (double gpm){
        this.gpm = gpm;
    }

    public void setAvgFuelPrice(double avgFuelPrice){
        this.avgFuelPrice = avgFuelPrice;
    }

// Getters
    public double getGpm() {
        return this.gpm;
    }

    public double getAvgFuelPrice() {
        return this.avgFuelPrice;
    }

    public double getPrice() {
        return this.price;
    }

// Methods
    private HashMap<String,String> readFile(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String[] keyValue;
        HashMap<String,String> results = new HashMap<>();

        while ((line = br.readLine()) != null){
            keyValue = line.split(":",2);
            results.put(keyValue[0].replace(" ",""),keyValue[1]);
        }
        return results;
    }

    private double calcPrice (double miles, double discount){
        this.setPrice(((gpm*miles*avgFuelPrice)+BASE_AIRFARE$+ LUGGAGE$)*(1+FEDERAL_TAX)+FLIGHT_SEGMENT_TAX$+ SEPT911$);
        this.setPrice(this.getPrice()-this.getPrice()*discount);

        Formatter cost = new Formatter();
        cost.format("%1.2f",this.getPrice());
        return Double.parseDouble((cost.toString()));

    }

    private void storeData(){
        boolean append = true;
        String fileName = "output/Ticket.txt";
        try {
            data.writeToAFile(fileName,("Ticket_Price").toUpperCase(),"$ "+ this.getPrice(),!append);
            fetch.forEach((k,v) -> {
                try {
                    data.writeToAFile(fileName,k.toUpperCase(),v.toUpperCase(),append);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void output(){
        System.out.println();
        for(int i=0; i<70;i++){System.out.print("*");}
        System.out.print("\n\nBoarding Pass No.:"+fetch.get("Boarding_Pass_Number"));
        System.out.println("\tFlight Date:"+fetch.get("Departure_Date"));
        System.out.print("\nName:"+fetch.get("NAME"));
        System.out.print("\tE-mail:"+fetch.get("EMAIL"));
        System.out.print("\tAge:"+fetch.get("AGE"));
        System.out.println("\tGender:"+fetch.get("GENDER"));
        System.out.println("\nOrigin Airport:"+fetch.get("Origin"));
        System.out.println("Destination Airport:"+fetch.get("Destination"));
        System.out.println("\nDeparture Time:"+fetch.get("Departure_Time"));
        System.out.print("Arrival Time:"+fetch.get("Arrival_Time"));
        System.out.println("\t\t\t\tTicket Price: $"+this.getPrice()+"\n");
        for(int i=0; i<70;i++){System.out.print("*");}
    }
}