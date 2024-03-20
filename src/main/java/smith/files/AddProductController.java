package smith.files;

//imports needed to run the add-part screen
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import static smith.files.Inventory.*;

/** This class contains functions for the add-product screen.  This screen
 * can be found after clicking the "add product" button on the main window. Here,
 * a user can add a product to the product inventory. The Table on top is a display of
 * all the parts from the main screen.  The bottom parts are the parts that are being
 * associated with the current product being stored*/
public class AddProductController {

    // labels, buttons and text fields that will be used
    @FXML private TableView<Part> allPartsTable;
    @FXML private TableColumn<Part,Integer> allPartsID;
    @FXML private TableColumn<Part,String> allPartsName;
    @FXML private TableColumn<Part,Integer> allPartsInv;
    @FXML private TableColumn<Part,Double> allPartsCost;
    @FXML private TableView<Part> associatedPartsTable;
    @FXML private TableColumn<Part,Integer> associatedID;
    @FXML private TableColumn<Part,String> associatedName;
    @FXML private TableColumn<Part,Integer> associatedInv;
    @FXML private TableColumn<Part,Double> associatedCost;
    @FXML private TextField addProductInvText;
    @FXML private TextField addProductMaxText;
    @FXML private TextField addProductMinText;
    @FXML private TextField addProductNameText;
    @FXML private TextField addProductPriceText;
    @FXML private TextField addProductSearchText;

    // temporary list of parts that will be stored to the product when saved.
    ObservableList<Part> tempPartList = FXCollections.observableArrayList();

    /**
     * This method cancels the "add product" function. When pressed, an alert will pop up
     * asking if the user is sure they want to cancel. If the user says yes, they will
     * return to the main screen.  If they select no, the user will return to the
     * "add product" screen with all of their data still in place.
     * @param event This is the button being clicked by the user. */
    @FXML
    void onAddProductCancelButtonClick(ActionEvent event) throws IOException{
        // Alert to confirm the cancel.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // clears the temporary parts list so that the table is empty the next
            // time a user opens this screen.
            tempPartList.clear();

            // Loads the main screen if the user confirms the cancel
            Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-view.fxml")));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();
        }
    }
    /** This method searches the parts table. The search first looks for a common string
     * anywhere in the part name.  If found, it will display all parts matching the
     * string in the parts table.  If no string can be matched, it then searches by ID
     * number.  This will only display 1 item if the ID matches exactly.  If nothing
     * matches, an error message will appear saying that no match can be found. If
     * the nothing is in the text box, all parts will be displayed.*/
    @FXML
    void onClickSearch(){
        // gets the string from the search box.
        String searchString = addProductSearchText.getText();
        // searches by the part name first.
        ObservableList<Part> searchedParts = searchByPartName(searchString);
        // if the searchedParts list is empty, search by the ID
        if (searchedParts.isEmpty()){
            try {
                int partID = Integer.parseInt(searchString);
                Part p = searchByPartID(partID);
                if (p != null) {
                    searchedParts.add(p);
                }
            }
            // display an alert if nothing matches.
            catch(NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Part not found");
                alert.showAndWait();
            }
        }
        //displays all matching parts in the table.
        allPartsTable.setItems(searchedParts);

    }

    /** FUTURE ENHANCEMENT - in the future, I would make it so that multiple parts could
     * be added at the same time. This would cut down on the time it would take to add
     * associated parts.
     *
     * This method adds a part to the associated parts table. The user select a
     * part from the top table. They then hit add to add it to the associated parts table.
     * This is necessary so that when the saved button is clicked later on, the program knows
     * what parts are associated with the product. */
    @FXML
    private void onAddClick(){
        // If no part is selected, an error message appears.
        if (allPartsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Selection Error");
            alert.setContentText("Must select part to add.");
            alert.showAndWait();
        }
        // Adds a part to the temporary parts table, which will be added to the product
        // when finished.
        else {
            Part selectedPart = allPartsTable.getSelectionModel().getSelectedItem();
            tempPartList.add(selectedPart);
        }
    }

    /** This method removes an associated part from the table.  Once a part is selected,
     * the part is removed from the table so that it is no longer associated with the product
     * that is being created.  This is necessary if a user makes a mistake and adds a part
     * that they did not mean to. If nothing is selected, an error message appears.  */
    @FXML
    private void onRemoveClick(){
        // checks to see if anything was selected.
        if (associatedPartsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Selection Error");
            alert.setContentText("Must select part to Remove.");
            alert.showAndWait();
        }
        else{
            // confirms that the selection should be removed from the table.
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Part");
            alert.setContentText("Are you sure you want to Remove Part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Removes selected part from the temporary parts list, thus removing it from
                // the table.
                Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();
                tempPartList.remove(selectedPart);            }

        }
    }

    /** This saves the Product that was created. The product is saved into the
     * productInventory found in the inventory class.  If the Inventory is not in between
     * the min and max, an error message is generated. If a user inputs something other than what
     * the program calls for, an error message is generated and the Product is not saved.
     * Finally, the main screen loads and is put on the screen
     * @param event this is the button being clicked by the user. */
    @FXML
    private void onSaveClick(ActionEvent event) throws IOException{
        try {
            //creates a new Product that will be saved. ID is auto generated
            Product saveProduct = new Product();
            int productID = AutoGenID();
            //getting the data from the text boxes
            String productName = addProductNameText.getText();
            int productStock = Integer.parseInt(addProductInvText.getText());
            int productPrice = Integer.parseInt(addProductPriceText.getText());
            int productMin = Integer.parseInt(addProductMinText.getText());
            int productMax = Integer.parseInt(addProductMaxText.getText());

            // saving data into the product
            saveProduct.setId(productID);
            saveProduct.setName(productName);
            saveProduct.setStock(productStock);
            saveProduct.setPrice(productPrice);
            saveProduct.setMax(productMax);
            saveProduct.setMin(productMin);

            // max cannot be more than min
            if (productMax < productMin) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Max cannot be less than Min.");
                alert.showAndWait();
                return;
            }
            //Inventory must be in between min and max
            if (productStock < productMin || productStock > productMax) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inv must be in between Min and Max.");
                alert.showAndWait();
                return;
            }
            //Saves each part from the associated parts table to the product
            for (Part p : tempPartList) {
                saveProduct.AddAssociatedParts(p);
            }
            // Confirmation before saving.
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save");
            alert.setContentText("Are you sure you want to Save?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                //saves the product and clears the temporary parts list.
                productInventory.add(saveProduct);
                tempPartList.clear();

                //loads the main screen
                Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-view.fxml")));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainStage.setScene(mainScreenScene);
                mainStage.show();
            }
        }

        // If one of the fields has the wrong data type in it, display an error.
        catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setContentText("Inv, Max, Min, and/or Machine ID must be an integer. Price must be an integer or double.");
            alert.showAndWait();
        }
    }

    /** This method generates an ID for a new Product. The method takes all the currently
     * created products and finds the highest ID.  The return is that highest number + 1.
     * If there are no products in the partInventory, then the method returns 1.
     * @return 1 if partInventory is empty, or the highestID + 1. */
    public int AutoGenID(){
        // checks if the product inventory is empty. If so, return 1
        if(productInventory.isEmpty()) {return 1;}
        else {
            int highestID = 1;
            // checks for the highest id from the products inventory
            for (Product p : productInventory) {
                if(p.getId() > highestID)
                {highestID = p.getId();}
            }
            // returns the highest ID +1.
            return highestID + 1;
        }
    }

    /** This initializes both the all parts table and the associated parts table.
     * This method then places the data inside both tables table.*/
   @FXML
    private void initialize(){
       //places items in all parts table
       allPartsTable.setItems(partInventory);

       // initializes the columns
       allPartsID.setCellValueFactory(new PropertyValueFactory<>("id"));
       allPartsName.setCellValueFactory(new PropertyValueFactory<>("Name"));
       allPartsInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
       allPartsCost.setCellValueFactory(new PropertyValueFactory<>("price"));

       //places items in all parts table
       associatedPartsTable.setItems(tempPartList);

       // initializes the columns
       associatedID.setCellValueFactory(new PropertyValueFactory<>("id"));
       associatedName.setCellValueFactory(new PropertyValueFactory<>("Name"));
       associatedInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
       associatedCost.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

}

