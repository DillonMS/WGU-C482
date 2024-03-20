package smith.files;
/** This is the Outsourced class. It inherits the "Part" class and adds one more attribute,
 * the string company name*/
public class OutsourcedPart extends Part {

    // attribute company name
    private String companyName;

    /**
     * Outsourced Part Constructor
     * @param id id
     * @param name name
     * @param price price
     * @param stock stock
     * @param min min
     * @param max max
     * @param companyName company name
     */
    public OutsourcedPart(int id, String name, double price, int stock, int min, int max, String companyName) {
        // takes the information from the inherited class, Part, and adds the companyName
        // value to it.
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }
    /**
     *
     * @param companyName string to set companyName
     */
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    /**
     *
     * @return companyName
     */
    public String GetCompanyName(){
        return companyName;
    }
}
