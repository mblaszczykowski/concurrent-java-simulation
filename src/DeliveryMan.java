import java.util.concurrent.TimeUnit;

public class DeliveryMan extends Thread{
    int iterations;
    int k = 0;

    DeliveryMan(String name, int iterations) {
        super(name);
        this.iterations = iterations;
    }

    public void run() {
        for (int i = 0; i < iterations; i++) {
            Device device;

            try{
                // trying to acquire semaphore, if there is no element in the buffer we are waiting for something to appear
                RepairWorkersDeliveryManBuffer.taken.tryAcquire(200, TimeUnit.MILLISECONDS); // poniewaz nie chcemy czekac nieskonczenie dlugo na element, potrafila wystapic blokada
            }catch(InterruptedException e){}

            // collecting an element from the buffer
            device =  RepairWorkersDeliveryManBuffer.buffor[k];
            // calculating the next position to collect from the buffer
            k = k+1 %  RepairWorkersDeliveryManBuffer.capacity;

            //Po pobraniu elementu konsument zwalnia semafor wolne, co powoduje zwiekszenie ilosci wolnych miejsc w buforze o 1.
            RepairWorkersDeliveryManBuffer.available.release();

            // konsumpcja

            System.out.println(getName() + " took " + device + " from repair worker after repair and is going to deliver it to the owner");

            // releasing client's semaphore to notify him that his device is back
            Client.clientsSemaphores.get(device.ownerID).release();

        }
    }
}
