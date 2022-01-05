import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DataStore {
    public void writeToAFile(String fileName,String field, String line, Boolean append ) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, append));
        writer.append(field).append(" : ").append(line);
        writer.newLine();
        writer.close();
    }
    public void writeToAJSON(String fileName,String field, String line, Boolean append ) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, append));
        writer.append(field).append(line);
        writer.newLine();
        writer.close();
    }
}
