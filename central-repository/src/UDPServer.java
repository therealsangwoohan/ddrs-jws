import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;

public class UDPServer {
    public static void main(String[] args) {
        HashMap<String, String> nameToHost = new HashMap<>();
        nameToHost.put("DVL", "localhost:5001");
        nameToHost.put("KKL", "localhost:5002");
        nameToHost.put("WST", "localhost:5003");

        try {
            InetAddress serverHostIP = InetAddress.getByName("localhost");
            int serverHostPort = 4000;
            DatagramSocket aDatagramSocket = new DatagramSocket(serverHostPort, serverHostIP);

            while (true) {
                DatagramPacket request = new DatagramPacket(new byte[3], 3);
                aDatagramSocket.receive(request);
                String serverName = new String(request.getData());
                System.out.println(serverName);
                String hostName = nameToHost.get(serverName);
                DatagramPacket response = new DatagramPacket(hostName.getBytes(),
                        14,
                        request.getAddress(),
                        request.getPort());
                aDatagramSocket.send(response);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}