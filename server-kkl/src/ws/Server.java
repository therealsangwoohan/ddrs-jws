package ws;

import javax.xml.ws.Endpoint;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Server {
    public static RoomRecords roomRecords = new RoomRecords();

    public static void main(String[] args) throws FileNotFoundException {
        new PrintWriter("log.txt").close();
        Endpoint.publish("http://localhost:5002/admin", new AdminServer());
        System.out.println("Server started.");
    }
}
