import java.util.concurrent.Semaphore;

public class ClientsOrderWorkerBuffer {
    // capacity 1 because we have only one order taking worker
    public static volatile Device orderBuffor;
    public static volatile Semaphore available = new Semaphore(1);
    public static volatile Semaphore taken = new Semaphore(0);
}
