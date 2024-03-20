package smith.files;

//imports needed to run
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

/** This class contains functions for the modify-product screen.  This screen
 * can be found after clicking the "modify product" button on the main window.  Here,
 * a user can modify a product from the product inventory. The user can add or remove
 * associated parts before saving.
 */
public class ModifyProductController {

    // initializing text fields and tables
    @FXML private TableColumn<Part,Integer> allPartsID;
    @FXML private TableColumn<Part,String> allPartsName;
    @FXML private TableView<Part> allPartsTable;
    @FXML private TableColumn<Part,Integer> allPartsInv;
    @FXML private TableColumn<Part,Double> allPartsCost;
    @FXML private TableView<Part> associatedPartsTable;
    @FXML private TableColumn<Part,Integer> aPartID;
    @FXML private TableColumn<Part,String> aPartName;
    @FXML private TableColumn<Part,Integer> aPartInv;
    @FXML private TableColumn<Part,Double> aPartPrice;
    @FXML private TextField modifyProductIDText;
    @FXML private TextField modifyProductInvText;
    @FXML private TextField modifyProductMaxText;
    @FXML private TextField modifyProductMinText;
    @FXML private TextField modifyProductNameText;
    @FXML private TextField modifyProductPriceText;
    @FXML private TextField modifyProductSearchText;
    Product product;

    // temporary list of parts that will be stored to the product when saved.
    ObservableList<Part> tempPartList = FXCollections.observableArrayList();

    /** This method cancels the "modify product" function. When pressed, an alert will pop up
     * asking if the user is sure they want to cancel. If the user says yes, they will
     * return to the main screen.  If they select no, the user will return to the
     * "modify product" screen with all of their data still in place.
     * @param event This is the button being clicked by the user. */
    @FXML
    void onModifyProductCancelButtonClick(ActionEvent event) throws IOException {
        // Alert confirming cancel
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();

        // if user says yes, load the main page and clear the temporary parts list.
        if (result.isPresent() && result.get() == ButtonType.OK) {
            tempPartList.clear();

            Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-view.fxml")));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();
        }
    }

    /**
     * Method used to move selected product from one controller to the next.
     * Populates the text fields with all the information from the product that
     * was selected.
     * @param product used to populate text boxes with correct data.
     */
    @FXML
    public void PassProduct(Product product) {
        //clear temp parts list and populates variables with data.
        tempPartList.clear();
        this.product = product;
        String productName = product.getName();
        int productID = product.getId();
        double productPrice = product.getPrice();
        int productStock = product.getStock();
        int productMin = product.getMin();
        int productMax = product.getMax();
        ObservableList<Part> productParts = this.product.getAssociatedParts();

        //adds parts into list
        tempPartList.addAll(productParts);

        // populates text boxes with data from product
        modifyProductIDText.setText(Integer.toString(productID));
        modifyProductNameText.setText(productName);
        modifyProductPriceText.setText(Double.toString(productPrice));
        modifyProductInvText.setText(Integer.toString(productStock));
        modifyProductMaxText.setText(Integer.toString(productMax));
        modifyProductMinText.setText(Integer.toString(productMin));


    }

    /** This method searches the parts table. The search first looks for a common string
     * anywhere in the part name.  If found, it will display all parts matching the
     * string in the parts table.  If no string can be matched, it then searches by ID
     * number.  This will only display 1 item if the ID matches exactly.  If nothing
     * matches, an error message will appear saying that no match can be found. If
     * the nothing is in the text box, all parts will be displayed.*/
    public void onSearchEnter() {
        // get string from search box
        String searchString = modifyProductSearchText.getText();
        // search by part name
        ObservableList<Part> searchedParts = searchByPartName(searchString);
        // if search by part name is empty, search by id
        if (searchedParts.isEmpty()) {
            try {
                int partID = Integer.parseInt(searchString);
                Part p = searchByPartID(partID);
                if (p != null) {
                    searchedParts.add(p);
                }
                // if search contains no match, display error method
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Part not found");
                alert.showAndWait();
            }
        }
        // display all parts found from teh search
        allPartsTable.setItems(searchedParts);
    }

    /** This method adds a part to the associated parts table. The user select a
     * part from the top table. They then hit add to add it to the associated parts table.
     * This is necessary so that when the saved button is clicked later on, the program knows
     * what parts are associated with the product. */
    public void onAddButtonClick() {
        // if no part selected, display error message
        if (allPartsTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Selection Error");
            alert.setContentText("Must select part to add.");
            alert.showAndWait();
        } else {
            // add part to temporary parts table to display in table
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
                tempPartList.remove(selectedPart);
            }
        }
    }

    /** This saves the Product that was modified. The product is saved into the
     * productInventory found in the inventory class.  If the Inventory is not in between
     * the min and max, an error message is generated. If a user inputs something other than what
     * the program calls for, an error message is generated and the Product is not saved.
     * Finally, the main screen loads and is put on the screen
     * @param event this is the button being clicked by the user. */
    @FXML
    private void onSaveClick(ActionEvent event) throws IOException{
        try {
            //getting data from text box and storing into variables
            Product saveProduct = new Product();
            int productID = Integer.parseInt(modifyProductIDText.getText());
            String productName = modifyProductNameText.getText();
            int productStock = Integer.parseInt(modifyProductInvText.getText());
            double productPrice = Double.parseDouble(modifyProductPriceText.getText());
            int productMin = Integer.parseInt(modifyProductMinText.getText());
            int productMax = Integer.parseInt(modifyProductMaxText.getText());

            //storing variables into a product to be saved
            saveProduct.setId(productID);
            saveProduct.setName(productName);
            saveProduct.setStock(productStock);
            saveProduct.setPrice(productPrice);
            saveProduct.setMax(productMax);
            saveProduct.setMin(productMin);

            //max can't be less than min
            if (productMax < productMin){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Max cannot be less than Min.");
                alert.showAndWait();
                return;
            }
            // Inventory must be in between min and max
            if (productStock < productMin || productStock > productMax){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inv must be in between Min and Max.");
                alert.showAndWait();
                return;
            }
            //loads parts into the product to be saved.
            for (Part p : tempPartList) {
                saveProduct.AddAssociatedParts(p);
            }
            // confirmation for save
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save");
            alert.setContentText("Are you sure you want to Save?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // saving the product to the product inventory
                productInventory.remove(this.product);
                productInventory.add(saveProduct);
                tempPartList.clear();

                // loading the main screen
                Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-view.fxml")));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainStage.setScene(mainScreenScene);
                mainStage.show();
            }
        }
        // display error if values aren't the appropriate data type
        catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setContentText("Inv, Max, Min, and/or Machine ID must be an integer. Price must be an integer or double.");
            alert.showAndWait();
        }
    }

    /**
     * Initialize the tables and puts the parts in the tables
     */
    @FXML
    private void initialize() {
        // puts parts in the all parts table
        allPartsTable.setItems(partInventory);

        //sets the columns for the all parts table
        allPartsID.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        allPartsInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsCost.setCellValueFactory(new PropertyValueFactory<>("price"));

        //puts the temp part list in the associated parts table
        associatedPartsTable.setItems(tempPartList);

        // sets the columns for the associated parts table
        aPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        aPartName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        aPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        aPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}

