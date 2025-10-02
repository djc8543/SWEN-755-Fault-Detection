public class OutOfBoundsException extends Exception {
  public OutOfBoundsException(String msg) {
    super("OutOfBoundsException: " + msg);
  }
}