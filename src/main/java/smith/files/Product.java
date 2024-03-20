package smith.files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The class provides the product constructor, as the methods associated with them.
 */
public class Product {

    /**
     * default constructor
     */
    public Product() {}

    /**
     * list of associated parts for the product
     */
    public ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Product constructor
     * @param id id
     * @param name name
     * @param price price
     * @param stock stock
     * @param min min
     * @param max max
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * ID Getter
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * ID setter
     * @param id value to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Name Getter
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * name setter
     * @param name value to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Price Getter
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * price setter
     * @param price value to set
     */
    public void setPrice(double price) {
        this.price = price;
    }


    /**
     * stock getter
     * @return stock
     */
    public int getStock() {return stock;}

    /**
     * stock setter
     * @param stock value to set
     */
    public void setStock(int stock) {this.stock = stock;}

    /**
     * min getter
     * @return min
     */
    public int getMin() {return min;}

    /**
     * min setter
     * @param min value to set
     */
    public void setMin(int min) {this.min = min;}

    /**
     * max getter
     * @return max
     */
    public int getMax() {return max;}

    /**
     * max setter
     * @param max value to set
     */
    public void setMax(int max) {this.max = max;}

    /**
     * associated parts getter
     * @return associated parts
     */
    public ObservableList<Part> getAssociatedParts() {return associatedParts;}

    /**
     * add associated parts
     * @param part part to add
     */
    public void AddAssociatedParts(Part part) {associatedParts.add(part);}

    /**
     * delete part from list
     * @param part value to delete
     * @return true confirming delete
     */
    public boolean DeleteAssociatedParts(Part part){
        associatedParts.remove(part);
        return true;
    }

}