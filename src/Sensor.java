import java.util.Arrays;
import java.util.Random;

public class Sensor {
  private final int lowerXBound;
  private final int upperXBound;
  private final int lowerYBound;
  private final int upperYBound;
  private final int lowerZBound;
  private final int upperZBound;
  private final Random random;
  private final double SUCCESS_RATE = 0.95;

  public Sensor(int lowerXBound, int lowerYBound, int lowerZBound, int upperXBound, int upperYBound, int upperZBound, Random random) {
    this.lowerXBound = lowerXBound;
    this.lowerYBound = lowerYBound;
    this.lowerZBound = lowerZBound;
    this.upperXBound = upperXBound;
    this.upperYBound = upperYBound;
    this.upperZBound = upperZBound;
    this.random = random;
  }

  public int[] getDetection() throws OutOfBoundsException {
    int[] detection = { calcX(), calcY(), calcZ() };
    return detection;
  }

  private int calcX() throws OutOfBoundsException {
    return lowerXBound + (int) (random.nextDouble() * (double) (upperXBound - lowerXBound));
  }
  
  private int calcY() throws OutOfBoundsException {
    return lowerYBound + (int) (random.nextDouble() * (double) (upperYBound - lowerYBound));
  }
  
  private int calcZ() throws OutOfBoundsException {
    int zLoc = random.nextDouble() < SUCCESS_RATE
      ? lowerZBound + (int) (random.nextDouble() * (double) (upperZBound - lowerZBound))
      : lowerZBound - 1;
    if (zLoc < lowerZBound || zLoc > upperZBound) throw new OutOfBoundsException("Invalid Z position: " + zLoc);
    return zLoc;
  }

  public static void main(String[] args) throws OutOfBoundsException {
    Sensor test = new Sensor(0,  0, 0, 10, 10, 10, new Random());
    while (true) {
      System.out.println(Arrays.toString(test.getDetection()));
    }
  }
}