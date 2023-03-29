public class Device {
    int ownerID;
    int ownerDeviceID;


    Device(int ownerID, int ownerDeviceID){
        this.ownerID = ownerID;
        this.ownerDeviceID = ownerDeviceID;

    }

    public String toString(){
        return ("Device from client nr: " + ownerID + " with id (iteration): " + ownerDeviceID);
    }
}
