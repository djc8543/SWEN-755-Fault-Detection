import java.util.Arrays;
import java.util.Random;

public class ObstacleSensor {
    private final Random random;
    private final Sensor sensor;
    private ObstacleSensor nextSensor;
    private final String id;

    public ObstacleSensor(String id, Integer seed) {
        this.id = id;
        this.sensor = new Sensor(0, 0, 0, 10, 10, 10);
        this.random = (seed == null) ? new Random() : new Random(seed);
        this.nextSensor = null;
    }

    public ObstacleSensor(String id, Integer seed, ObstacleSensor nextSensor) {
        this(id, seed);
        this.nextSensor = nextSensor;
    }

    public boolean hasNext() {
        return (this.nextSensor != null);
    }

    public String getId() {
        return this.id;
    }

    public String getSensorData() throws OutOfBoundsException {
        return "ObstacleSensor " + id + "  received " + Arrays.toString(sensor.getDetection());
    }
}