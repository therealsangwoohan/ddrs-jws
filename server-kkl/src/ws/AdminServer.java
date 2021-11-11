package ws;

import javax.jws.WebService;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@WebService(endpointInterface = "ws.AdminClient")
public class AdminServer implements AdminClient {

    public static String hashMapToString(HashMap<String, String> response) {
        Map<String, String> map = new HashMap<String, String>(response);
        return map.keySet().stream()
                .map(key -> key + "=" + map.get(key))
                .collect(Collectors.joining(", "));
    }

    private void writeToLog(HashMap<String, String> request,
                            HashMap<String, String> response) {
        try {
            FileWriter myWriter = new FileWriter("log.txt", true);
            myWriter.write("REQUEST: \r\n");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            myWriter.write("Time: " + dtf.format(now) + "\r\n");
            for (String requestArg: request.keySet()) {
                myWriter.write(requestArg + ": " + request.get(requestArg) + "\r\n");
            }
            myWriter.write("\r\n");
            myWriter.write("RESPONSE:\r\n");
            for (String responseArg: response.keySet()) {
                myWriter.write(responseArg + ": " + response.get(responseArg) + "\r\n");
            }
            myWriter.write("\r\n");
            myWriter.write("***\r\n");
            myWriter.write("\r\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public String createRoom(String roomNumber, String date, String timeSlotsString) {
        HashMap<String, String> request = new HashMap<>();
        request.put("requestType", "Create Room");
        request.put("roomNumber", roomNumber);
        request.put("date", date);
        request.put("timeSlotsString", timeSlotsString);
        HashMap<String, String> response = Server.roomRecords.createRoom(roomNumber, date, timeSlotsString);
        writeToLog(request, response);
        return hashMapToString(response);
    }

    @Override
    public String deleteRoom(String roomNumber, String date, String timeSlotsString) {
        HashMap<String, String> request = new HashMap<>();
        request.put("requestType", "Delete Room");
        request.put("roomNumber", roomNumber);
        request.put("date", date);
        request.put("timeSlotsString", timeSlotsString);
        HashMap<String, String> response = Server.roomRecords.deleteRoom(roomNumber, date, timeSlotsString);
        writeToLog(request, response);
        return hashMapToString(response);
    }
}
