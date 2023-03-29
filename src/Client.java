import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Client extends Thread {
    int id;
    int iterations;

    static List<Semaphore> clientsSemaphores = new ArrayList<>();

    Client(String name, int id, int iterations) {
        super(name);
        this.id = id;
        this.iterations = iterations;
    }

    // Client is a producer of devices to repair
    public void run(){
        // for each client we are creating a semaphore to check whether the device will be returned
        clientsSemaphores.add(new Semaphore(0));

        for(int i = 0; i < iterations; i++){
            // production
            Device device = new Device(id, i);
            System.out.println(getName() + " delivers for repairing: " + device);

            try{
                //producent podnosi semafor wolne, aby sprawdzić, czy w buforze jest jeszcze miejsce na kolejny element, jesli nie ma to czeka az sie pojawi
                // acquiring semaphore
                ClientsOrderWorkerBuffer.available.tryAcquire(200, TimeUnit.MILLISECONDS);
            }catch(InterruptedException e){}

            // critical section
            ClientsOrderWorkerBuffer.orderBuffor = device;

            // notyfing consumer (order takich worker) that there is new device waiting
            ClientsOrderWorkerBuffer.taken.release();
        }

        // waiting for the return of all devices
        for(int i = 0; i < iterations; i++){
            try {
                Client.clientsSemaphores.get(id).acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(getName() + " got his repaired device nr: " + i + " back");
        }
    }
}