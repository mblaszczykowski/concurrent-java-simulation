public class Main {
    public static void main(String[] args) throws InterruptedException {
        // setting parameters
        int numberOfClients = 5;
        int iterationsPerClient = 2;

        // creating threads
        OrderWorker orderWorker = new OrderWorker("[Order worker]", numberOfClients * iterationsPerClient);
        orderWorker.start();

        RepairWorker[] repairWorkers = new RepairWorker[3];
        repairWorkers[0] = new RepairWorker("[Repair worker - 1]", numberOfClients * iterationsPerClient / 3 + numberOfClients * iterationsPerClient % 3);
        repairWorkers[1] = new RepairWorker("[Repair worker - 2]", numberOfClients * iterationsPerClient / 3);
        repairWorkers[2] = new RepairWorker("[Repair worker - 3]", numberOfClients * iterationsPerClient / 3);
        for (int i = 0; i < 3; i++) {
            repairWorkers[i].start();
        }

        DeliveryMan deliveryMan = new DeliveryMan("[Delivery Man]", numberOfClients * iterationsPerClient);
        deliveryMan.start();

        Client[] clients = new Client[numberOfClients];
        for (int i = 0; i < numberOfClients; i++) {
            clients[i] = new Client("[Client - " + i + "]", i, iterationsPerClient);
            clients[i].start();
        }

        // waiting for threads to finish
        orderWorker.join();

        for (int i = 0; i < 3; i++) {
            try {
                repairWorkers[i].join();
            } catch (InterruptedException e) {
            }
        }

        deliveryMan.join();

        for (int i = 0; i < numberOfClients; i++) {
            try {
                clients[i].join();
            } catch (InterruptedException e) {
            }
        }

    }
}