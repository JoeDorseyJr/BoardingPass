import java.io.IOException;

public class Ticket {
    //This class will call the other classes and glue all the other information together.
    //TODO Get the origin and destination
    //TODO Store the passenger info
    //TODO Calculate distance and flight times
    //TODO Establish a multiplier for flight price.
    //TODO output a ticket price.

    Ticket() throws IOException {
        new AirportData("New York");
    }
}
