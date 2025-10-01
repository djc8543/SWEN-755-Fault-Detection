import java.io.PrintWriter;
import java.net.Socket;

public class SensorNode {
    private ObstacleSensor activeSensor;
    private final ObstacleSensor backupSensor;
    private final Socket serverSocket;
    private boolean running = true;

    public SensorNode(String id, Integer seed) throws Exception {
        this.backupSensor = new ObstacleSensor(id + "_backup", seed);
        this.activeSensor = new ObstacleSensor(id, seed, this.backupSensor);
        this.serverSocket = new Socket("localhost", 9999);
    }

    public void run() throws Exception {
        try (PrintWriter serverWriter = new PrintWriter(serverSocket.getOutputStream(),  true)) {
            while (running) {
                Thread.sleep(1000);
                try {
                    String data = activeSensor.getSensorData();
                    serverWriter.println(data);
                } catch (OutOfBoundsException e) {
                    System.out.println("Sensor failed, switching to next sensor...");
                    startBackupSensor();
                }
            }
        }
    }

    public void startBackupSensor() {
        if (activeSensor.hasNext()) {
            activeSensor = backupSensor;
        } else {
            System.out.println("Final sensor has failed, ending process...");
            running = false;
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("NO ID PROVIDED");
            return;
        }
        String id = args[0];
        SensorNode node = new SensorNode(id, null);
        node.run();
    }
}
