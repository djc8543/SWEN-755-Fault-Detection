import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

public class SensorNode {
    private LinkedList<ObstacleSensor> obstacleSensors;
    private ObstacleSensor activeSensor;
    private final Socket serverSocket;
    private boolean running = true;

    public SensorNode(String id, Integer seed, int numSensors) throws Exception {
        this.obstacleSensors = new LinkedList<>();
        this.activeSensor = new ObstacleSensor(id, seed);
        this.obstacleSensors.add(this.activeSensor);
        this.serverSocket = new Socket("localhost", 9999);
        for (int i = 0; i < numSensors; i++) {
            obstacleSensors.add(new ObstacleSensor(id + "_backup_" + (i + 1), seed));
        }
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

    public void createSensors(int numSensors) {

    }

    public void startBackupSensor() {
        int sensorIndex = obstacleSensors.indexOf(activeSensor);
        if (sensorIndex + 1 < obstacleSensors.size()) {
            activeSensor = obstacleSensors.get(sensorIndex + 1);
            System.out.println("Sensor: " + obstacleSensors.get(sensorIndex).getId() + " has died. Switching to next available backup.");
        } else {
            System.out.println("Final sensor has failed, ending process...");
            running = false;
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("NO ID OR SENSOR COUNT PROVIDED");
            return;
        }
        String id = args[0];
        int numSensors = Integer.parseInt(args[1]);
        SensorNode node = new SensorNode(id, null, numSensors);
        node.run();
    }
}
