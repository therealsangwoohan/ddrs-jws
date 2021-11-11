package ws;

import javax.jws.WebService;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@WebService(endpointInterface = "ws.StudentClient")
public class StudentServer implements StudentClient {

    public static String hashMapToString(HashMap<String, String> response) {
        Map<String, String> map = new HashMap<>(response);
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
    public String bookRoom(String campusName, String roomNumber, String date, String timeslot, String studentID) {
        HashMap<String, String> request = new HashMap<>();
        request.put("requestType", "Book Room");
        request.put("studentID", studentID);
        request.put("campusName", roomNumber);
        request.put("date", date);
        request.put("timeslot", timeslot);
        HashMap<String, String> response = Server.roomRecords.bookRoom(campusName,
                                                                       roomNumber,
                                                                       date,
                                                                       timeslot,
                                                                       studentID);
        writeToLog(request, response);
        return hashMapToString(response);
    }

    @Override
    public String cancelBooking(String bookingID, String studentID) {
        HashMap<String, String> request = new HashMap<>();
        request.put("requestType", "Cancel Booking");
        request.put("studentID", studentID);
        request.put("bookingID", bookingID);
        HashMap<String, String> response = Server.roomRecords.cancelBooking(bookingID, studentID);
        writeToLog(request, response);
        return hashMapToString(response);
    }

    @Override
    public String getAvailableTimeSlot(String date) {
        HashMap<String, String> request = new HashMap<>();
        request.put("requestType", "Get available time slots");
        request.put("date", date);
        return hashMapToString(request);
    }

    @Override
    public String changeReservation(String bookingID, String newCampusName, String newRoomNumber, String newTimeSlot) {
        HashMap<String, String> request = new HashMap<>();
        request.put("requestType", "Book Room");
        request.put("newCampusName", newCampusName);
        request.put("newRoomNumber", newRoomNumber);
        request.put("newTimeSlot", newTimeSlot);
        return hashMapToString(request);
    }
}
