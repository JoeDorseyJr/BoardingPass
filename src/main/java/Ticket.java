import java.io.IOException;

public class Ticket {
    //This class will call the other classes and glue all the other information together.
    //TODO 1. Grab Stored the passenger info
    //TODO 2. Establish a multiplier for flight price.
    //TODO 3. Output a ticket price.

    Ticket() throws IOException {
        new Boarding_Pass();
    }
}
