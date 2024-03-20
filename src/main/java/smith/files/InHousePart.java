package smith.files;
/** This is the In-House class. It inherits the "Part" class and adds one more attribute,
 * the integer machine ID*/
public class InHousePart extends Part {

    /**
     * variable machineID specific to the In-House class
     */
    public int machineID;

    /**
     * Constructor for InHousePart
     * @param id id
     * @param name name
     * @param price price
     * @param stock stock
     * @param min min
     * @param max max
     * @param machineID machineID
     */
    public InHousePart(int id, String name, double price, int stock, int min, int max, int machineID) {
        // takes the information from the inherited class, Part, and adds the machineID
        // value to it.
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /**
     * @return the machineID
     * */
    public int GetMachineID(){
        return machineID;
    }

    /**
     *
     * @param machineID the machine id to set
     */
    public void setMachineID(int machineID){
        this.machineID = machineID;
    }
}
