package smith.files;

//imported material
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;

/** The Inventory class keeps track of the products and parts. It places them in lists
 * and contains methods to add, remove, search and set individual parts*/
public class Inventory {
    /**
     * list for all parts created
     */
    public static ObservableList<Part> partInventory = FXCollections.observableArrayList();
    /**
     * list for all products created
     */
    public static ObservableList<Product> productInventory = FXCollections.observableArrayList();

    /**
     * Method to add parts to inventory.
     * @param part the part to add
     */
    public void AddPart (Part part){
        partInventory.add(part);
    }

    /**
     * Method to add products to inventory.
     * @param product the product to add
     */
    public void AddProduct (Product product){

        productInventory.add(product);
    }

    /**
     * Method to Delete Part from Inventory.
     * @param part part to delete
     * @return true confirming delete
     */
    public boolean DeletePart(Part part){
        partInventory.remove(part);
        return true;
    }

    /**
     * Method to delete Product from Inventory.
     * @param product product to delete
     * @return true confirming delete
     */
    public boolean DeleteProduct(Product product){
        productInventory.remove(product);
                return true;
    }

    /**
     * Method to search the Part Inventory by ID.
     * @param partID ID used to search the part Inventory
     * @return part with matching id, or return nothing if no match
     */
    public static Part searchByPartID(int partID){
        for (Part p : partInventory) {
            if (p.getId() == partID) {
                return p;
            }
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Product not found");
        alert.showAndWait();
        return null;
    }

    /**
     *Method to search the Part Inventory by Name.
     * @param partialName used to search Part List
     * @return list of parts with matching string.
     */
    public static ObservableList<Part> searchByPartName(String partialName){
        ObservableList<Part> namedParts = FXCollections.observableArrayList();
        for(Part p: partInventory){
            if (p.getName().contains(partialName)){
                namedParts.add(p);
            }
        }
        return namedParts;
    }

    /**
     * Method to search the Product Inventory by ID.
     * @param productID used to search product list
     * @return product with matching id, or return nothing
     */
    public static Product searchByProductID(int productID){
        for (Product p : productInventory) {
            if (p.getId() == productID) {
                return p;
            }
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Product not found");
        alert.showAndWait();
        return null;
    }

    /**
     * Method to search the Product Inventory by Name.
     * @param partialName used to search product list
     * @return list of products with matching strings
     */
    public static ObservableList<Product> searchByProductName(String partialName){
        ObservableList<Product> namedProducts =  FXCollections.observableArrayList();
        for(Product p: productInventory){
            if (p.getName().contains(partialName)){
                namedProducts.add(p);
            }
        }
        return namedProducts;
    }

    /**
     * Method to return all parts in the inventory.
     * @return list of all parts from inventory
     */
    public ObservableList<Part> getAllParts(){
        return partInventory;
    }

    /**
     *Method to return all products from the inventory.
     * @return list of all products from inventory
     */
    public ObservableList<Product> getAllProducts(){
        return productInventory;
    }

    /**
     * Method to Update a part in the inventory.
     * @param partID selects the part that will be updated
     * @param part replaces the current part with the new part
     */
    public static void UpdatePart(int partID, Part part){
        partInventory.set(partID,part);
    }

    /**
     * Method to Update a product in the Inventory.
     * @param productID selects the product that will be updated
     * @param product replaces the current part with the new part
     */
    public static void UpdateProduct(int productID, Product product){
        productInventory.set(productID, product);
    }

}
