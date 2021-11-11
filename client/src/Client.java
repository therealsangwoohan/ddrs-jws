import ws.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class Client {

    public static String validateID(String id) {
        if (id.length() != 8 ||
                (!id.startsWith("DVL") &&
                        !id.startsWith("KKL") &&
                        !id.startsWith("WST")) ||
                (id.charAt(3) != 'S' && id.charAt(3) != 'A') ||
                !isNumeric(id.substring(4, 8))
        ) {
            return "invalid";
        } else if (id.charAt(3) == 'S') {
            return "student";
        } else {
            return "administrator";
        }
    }

    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static HashMap<String, String> stringToHashMap(String responseString) {
        Map<String, String> map = Arrays.stream(responseString.split(", "))
                .map(entry -> entry.split("="))
                .collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
        return new HashMap<>(map);
    }

    public static void writeToLog(String id,
                                  HashMap<String, String> request,
                                  HashMap<String, String> response) {
        try {
            FileWriter myWriter = new FileWriter("./logs/log_" + id + ".txt", true);
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

    public static URL getURL(String campusName, String endpoint) {
        try {
            return new URL("http://" + getHostName(campusName) + "/" + endpoint);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static String getHostName(String campusName) {
        String hostName = "";
        try {
            InetAddress clientHostIP = InetAddress.getByName("localhost");
            int clientHostPort = 3000;
            DatagramSocket aDatagramSocket = new DatagramSocket(clientHostPort, clientHostIP);

            InetAddress serverHostIP = InetAddress.getByName("localhost");
            int serverHostPort = 4000;
            DatagramPacket request = new DatagramPacket(campusName.getBytes(), 3, serverHostIP, serverHostPort);
            aDatagramSocket.send(request);

            DatagramPacket response = new DatagramPacket(new byte[14], 14);
            aDatagramSocket.receive(response);
            hostName = new String(response.getData());

            aDatagramSocket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return hostName;
    }

    public static void studentOperations(String studentID,
                                         Scanner userInput) {
        System.out.println("What would you like to do?:");
        System.out.println("    1) Book a room.");
        System.out.println("    2) Cancel booking.");
        System.out.println("    3) Get available time slots.");
        System.out.println("    4) Change reservation.");
        String choice = userInput.nextLine();
        if (choice.equals("1")) {
            studentBookRoom(studentID, userInput);
        } else if (choice.equals("2")) {
            studentCancelBooking(studentID, userInput);
        } else if (choice.equals("3")) {
            studentGetAvailableTimeSlot(studentID, userInput);
        } else if (choice.equals("4")) {
            studentChangeReservation(studentID, userInput);
        }
    }

    public static void studentBookRoom(String studentID,
                                       Scanner userInput) {
        HashMap<String, String> request = new HashMap<>();
        request.put("requestType", "Book Room");
        System.out.println("Enter campus name:");
        request.put("campusName", userInput.nextLine());
        System.out.println("Enter room number:");
        request.put("roomNumber", userInput.nextLine());
        System.out.println("Enter date:");
        request.put("date", userInput.nextLine());
        System.out.println("Enter timeslot. For example, '19h-20h':");
        request.put("timeslot", userInput.nextLine());

        URL url = getURL(request.get("campusName"), "student");
        StudentServerService studentImplService = new StudentServerService(url);
        StudentClient studentProxy = studentImplService.getStudentServerPort();
        String responseString = studentProxy.bookRoom(request.get("campusName"),
                                                      request.get("roomNumber"),
                                                      request.get("date"),
                                                      request.get("timeslot"),
                                                      studentID);
        HashMap<String, String> response = stringToHashMap(responseString);
        writeToLog(studentID, request, response);
    }

    public static void studentCancelBooking(String studentID,
                                            Scanner userInput) {
        HashMap<String, String> request = new HashMap<>();
        request.put("requestType", "Cancel Booking");
        System.out.println("Enter booking ID:");
        request.put("bookingID", userInput.nextLine());

        URL url = getURL(studentID.substring(0, 3), "student");
        StudentServerService studentImplService = new StudentServerService(url);
        StudentClient studentProxy = studentImplService.getStudentServerPort();
        String responseString = studentProxy.cancelBooking(request.get("bookingID"), studentID);
        HashMap<String, String> response = stringToHashMap(responseString);
        writeToLog(studentID, request, response);
    }

    public static void studentGetAvailableTimeSlot(String studentID,
                                                   Scanner userInput) {
        HashMap<String, String> request = new HashMap<>();
        request.put("requestType", "Get Available Time Slot");
        System.out.println("Enter date:");
        request.put("date", userInput.nextLine());

        URL url = getURL(request.get("campusName"), "student");
        StudentServerService studentImplService = new StudentServerService(url);
        StudentClient studentProxy = studentImplService.getStudentServerPort();
        String responseString = studentProxy.getAvailableTimeSlot(request.get("date"));
        HashMap<String, String> response = stringToHashMap(responseString);
        writeToLog(studentID, request, response);
    }

    public static void studentChangeReservation(String studentID, Scanner userInput) {
        HashMap<String, String> request = new HashMap<>();
        request.put("requestType", "Change Reservation");
        System.out.println("Enter booking_id:");
        request.put("bookingID", userInput.nextLine());
        System.out.println("Enter new campus name:");
        request.put("newCampusName", userInput.nextLine());
        System.out.println("Enter new timeslot. For example, '19h-20h':");
        request.put("newTimeslot", userInput.nextLine());

        String[] args = request.get("bookingID").split(",");
        String campusName = args[0];
        String roomNumber = args[1];
        String date = args[2];

        URL url = getURL(campusName, "student");
        StudentClient studentProxyOfOldCampus = (new StudentServerService(url)).getStudentServerPort();
        studentProxyOfOldCampus.cancelBooking(request.get("bookingID"), studentID);

        url = getURL(request.get("newCampusName"), "student");
        StudentClient studentProxyOfNewCampus = (new StudentServerService(url)).getStudentServerPort();
        String responseString = studentProxyOfNewCampus.bookRoom(request.get("newCampusName"),
                                                                 roomNumber,
                                                                 date,
                                                                 request.get("newTimeslot"),
                                                                 studentID);
        HashMap<String, String> response = stringToHashMap(responseString);
        writeToLog(studentID, request, response);
    }

    public static void administratorOperations(String adminID,
                                               Scanner userInput) {
        System.out.println("What would you like to do?:");
        System.out.println("    1) Create a room.");
        System.out.println("    2) Delete a room.");
        String choice = userInput.nextLine();
        if (choice.equals("1")) {
            administratorCreateRoom(adminID, userInput);
        } else if (choice.equals("2")) {
            administratorDeleteRoom(adminID, userInput);
        }
    }

    public static void administratorCreateRoom(String adminID,
                                               Scanner userInput) {
        HashMap<String, String> request = new HashMap<>();
        request.put("requestType", "Create Room");
        System.out.println("Enter room number:");
        request.put("roomNumber", userInput.nextLine());
        System.out.println("Enter date. For example, '2021:10:05'");
        request.put("date", userInput.nextLine());
        System.out.println("Enter list of time slots. For example, '09h-10h,12h-13h':");
        request.put("timeSlotsString", userInput.nextLine());

        URL url = getURL(adminID.substring(0, 3), "admin");
        AdminServerService adminServerService = new AdminServerService(url);
        AdminClient adminProxy = adminServerService.getAdminServerPort();
        String responseString = adminProxy.createRoom(request.get("roomNumber"),
                                                      request.get("date"),
                                                      request.get("timeSlotsString"));
        HashMap<String, String> response = stringToHashMap(responseString);
        writeToLog(adminID, request, response);
    }

    public static void administratorDeleteRoom(String adminID,
                                               Scanner userInput) {
        HashMap<String, String> request = new HashMap<>();
        request.put("requestType", "Delete Room");
        System.out.println("Enter room number:");
        request.put("roomNumber", userInput.nextLine());
        System.out.println("Enter date. For example, '2021:10:05'");
        request.put("date", userInput.nextLine());
        System.out.println("Enter list of time slots. For example, '09h-10h,12h-13h':");
        request.put("timeSlotsString", userInput.nextLine());

        URL url = getURL(adminID.substring(0, 3), "admin");
        AdminServerService adminServerService = new AdminServerService(url);
        AdminClient adminProxy = adminServerService.getAdminServerPort();
        String responseString = adminProxy.deleteRoom(request.get("roomNumber"),
                                                      request.get("date"),
                                                      request.get("timeSlotsString"));
        HashMap<String, String> response = stringToHashMap(responseString);
        writeToLog(adminID, request, response);
    }

    public static void startClient() {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter your id. For example, 'DVLS1234':");
        String id = userInput.nextLine();

        String validationResponse = validateID(id);
        if (validationResponse.equals("invalid")) {
            System.out.println("Your id is invalid.");
            return;
        }

        try {
            File file = new File("./logs/log_" + id + ".txt");
            file.createNewFile();
            if (validationResponse.equals("student")) {
                do {
                    studentOperations(id, userInput);
                    System.out.println("Would you like to quit? (y/n): ");
                } while (!userInput.nextLine().equals("y"));
            } else {
                do {
                    administratorOperations(id, userInput);
                    System.out.println("Would you like to quit? (y/n): ");
                } while (!userInput.nextLine().equals("y"));
            }
            file.delete();
        } catch (IOException e) {
            System.out.println(e);
        }
        userInput.close();
    }

    public static void main(String[] args) {
        startClient();
    }
}
