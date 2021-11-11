package ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface AdminClient {

    @WebMethod
    public String createRoom(String roomNumber, String date, String timeSlotsString);

    @WebMethod
    public String deleteRoom(String roomNumber, String date, String timeSlotsString);
}
