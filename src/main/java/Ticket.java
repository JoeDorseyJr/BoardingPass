import java.io.File;
import java.io.IOException;

public class Ticket {
    //This class will call the other classes and glue all the other information together.
    //TODO 1. Grab Stored the passenger info
    //TODO 2. Establish a multiplier for flight price.
    //TODO 3. Output a ticket price.

    Ticket() throws IOException {
        Passenger p = new Passenger();
        new Boarding_Pass();
    }

    private void readFile(String fileName){
        File file = new File(fileName);
    }

    private void calcPrice(){

    }
}