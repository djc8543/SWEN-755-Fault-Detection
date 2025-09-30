import java.net.Socket;
import java.io.PrintWriter;
import java.util.Random;

public class ObstacleSensor {
    private static Random random = new Random();

    public static void main(String[] args) throws Exception {

        // user when creating the sensor will give it an ID (name)
        if (args.length != 1) {
            System.out.println("NO ID PROVIDED");
            return;
        }

        String sensorID = args[0]; // get the id
        Socket socket = new Socket("localhost", 9999); // create the socket

        System.out.println("Starting Socket at port 9999");

        try {
            // used for sending the data across the socket, handles byte conversion
            // simpler then using OutputStream
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // main event loop
            while (true) {
                Thread.sleep(1000); // wait and do nothing, 1 second

                int randomValue = random.nextInt(100); // generate a number between 0-99

                if (randomValue < 5) { // 5% chance of failure
                    throw new Exception(sensorID + " crashed, going down!");
                } else { // normal operation
                    writer.println(sensorID + " : I'm alive"); // send over the socket
                    System.out.println(sensorID + " successfully sent heartbeat");
                }
            }
        } catch (Exception e) {
            socket.close();
            System.out.println(e.getMessage());
        }
    }
}