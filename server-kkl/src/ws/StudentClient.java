package ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface StudentClient {

    @WebMethod
    public String bookRoom(String campusName,
                           String roomNumber,
                           String date,
                           String timeslot,
                           String studentID);

    @WebMethod
    public String cancelBooking(String bookingID, String studentID);

    @WebMethod
    public String getAvailableTimeSlot(String date);

    @WebMethod
    public String changeReservation(String bookingID,
                                    String newCampusName,
                                    String newRoomNumber,
                                    String newTimeSlot);
}
