import java.util.concurrent.Semaphore;

// Order Worker - Repair Workers buffor
public class ShelfBuffer {
    public static int capacity = 10;
    public static Device[] buffor = new Device[capacity];
    public static Semaphore available = new Semaphore(capacity);
    public static Semaphore taken = new Semaphore(0);

    // k is used to synchronize the removal (by the multiple repair workers) of elements from the buffer
    public static int k = 0;
    public static Semaphore protect_k = new Semaphore(1);
}