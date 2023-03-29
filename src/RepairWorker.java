import java.util.concurrent.TimeUnit;

public class RepairWorker extends Thread{
    int iterations;
    int kk;
    int jj;

    public RepairWorker(String name, int iterations){
        super(name);
        this.iterations = iterations;
    }
    public void run(){
        for(int i = 0; i< iterations; i++){
            Device device;
            try{
                // we try to take an element from the buffer, if there is none we wait
                ShelfBuffer.taken.tryAcquire(200, TimeUnit.MILLISECONDS);
            }catch(InterruptedException e){}
            try{
                // consumer blocks the semaphore protect_k, so that only one repair worker at a time can take one specific device from shelf
                ShelfBuffer.protect_k.acquire();
                // consumer takes the value of the index k, which points to the buffer element to be taken
                kk  =  ShelfBuffer.k;
                // k index is then incremented by 1 (modulo the buffer capacity) to point to the next buffer element to be taken.
                ShelfBuffer.k = (ShelfBuffer.k+1) %  ShelfBuffer.capacity;
            }catch(InterruptedException e){}
            // consumer releases the semaphore protect_k so that another repair worker can take the next element from the shelf
            ShelfBuffer.protect_k.release();

            // and takes the value of the buffer element with index k
            device =  ShelfBuffer.buffor[kk];

            // after taking the element the consumer releases the semaphore, which increases the number of free places in the buffer by 1
            ShelfBuffer.available.release();

            System.out.println(getName() + " got " + device + " from the shelf to repair, repaired it and gives it to the Delivery Man for sending back to the client");


            // repair worker after repairing tries to put device which is ready for delivery on delivery's man buffer
            try{
                RepairWorkersDeliveryManBuffer.available.tryAcquire(200, TimeUnit.MILLISECONDS);
            }catch(InterruptedException e){}

            try{
                // making sure that other repair workers don't try to change the index j and grab the same element in the same time
                RepairWorkersDeliveryManBuffer.protect_j.acquire();
                // getting the index j, which points to the place in the buffer where the producer should put the repaired device
                jj  =  RepairWorkersDeliveryManBuffer.j;
                // modyfing the value of the index j, so that it points to the next place in the buffer
                RepairWorkersDeliveryManBuffer.j = ( RepairWorkersDeliveryManBuffer.j+1) % RepairWorkersDeliveryManBuffer.capacity;
                RepairWorkersDeliveryManBuffer.protect_j.release();

                // putting device in a buffer
                RepairWorkersDeliveryManBuffer.buffor[jj] = device;
            }catch(InterruptedException e){}

            // informing delivery man that device is ready to be delivered
            RepairWorkersDeliveryManBuffer.taken.release();
        }
    }
}