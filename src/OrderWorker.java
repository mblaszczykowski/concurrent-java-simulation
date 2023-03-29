import java.util.concurrent.TimeUnit;

public class OrderWorker extends Thread{
    int iterations;
    int j = 0;

    public OrderWorker(String name, int iterations){
        super(name);
        this.iterations = iterations;
    }

    public void run(){
        for(int i = 0; i< iterations; i++){
            Device device;

            // Production - here we accept orders from the client, we are the consumer
            try{
                // trying to acquire semaphore, if there is no element in the buffer we are waiting
                ClientsOrderWorkerBuffer.taken.tryAcquire(200, TimeUnit.MILLISECONDS);
            }catch(InterruptedException e){}

            device =  ClientsOrderWorkerBuffer.orderBuffor;

            // after taking the element the consumer releases the semaphore, which increases the number of free places in the buffer by 1
            ClientsOrderWorkerBuffer.available.release();

            System.out.println(getName() + " received " + device + " from the client and puts it on a shelf for repair");

            // here we are the producer, consuming = passing the device to the shelf (repair workers)
            try{
                // producer raises the semaphore, to check if there is still space in the buffer for the next element, if there is no space, it waits until it appears
                ShelfBuffer.available.tryAcquire(200, TimeUnit.MILLISECONDS);
            }catch(InterruptedException e){}

            // placing an element in the buffer
            ShelfBuffer.buffor[j] = device;

            // calculating the next position in the buffer
            j = (j +1) %  ShelfBuffer.capacity;

            // informing the consumer that there is a new element in the buffer
            ShelfBuffer.taken.release();
        }
    }
}
