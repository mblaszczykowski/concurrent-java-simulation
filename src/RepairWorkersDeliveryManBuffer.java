import java.util.concurrent.Semaphore;

public class RepairWorkersDeliveryManBuffer {
    public static int capacity = 10;

    public static Device[] buffor = new Device[capacity];
    public static Semaphore available = new Semaphore(capacity);
    public static Semaphore taken = new Semaphore(0);

    public static int j = 0;
    public static Semaphore protect_j = new Semaphore(1);
}