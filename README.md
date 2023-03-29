# Concurrent-Programming-project-in-Java
> Project for Concurrent Programming subject at Military University of Technology in Warsaw

### Problem: Workshop
Assumptions:

A certain workshop has a total of four employees. <br>
One person takes orders to repair X equipment, and the other three carry out these repairs. <br>
The workshop can accommodate a maximum of 10 pieces of X equipment. <br>
The person taking the orders accepts the equipment for repair and puts the items to be repaired on a shelf. <br>
When any of the three workers repairing the equipment is free, he takes an item from the repair shelf and repairs it. <br>
Once repaired, the repairman hands it over to a courier for shipment back to the customer. There is only one courier.

### Solution
My solution to this problem is based on producer-consumer problem using Semaphores.
I created three buffors: Clients - Order Worker buffor (N - 1), Order Worker - Repair Workers (1 - 3) buffor and Repair Workers - Delivery Man buffor (3-1) and synchronized all threads to work together. <br>

The full process comes full circle: customers, after submitting the equipment for repair, wait for it to be returned by delivery man and then end their threads.
