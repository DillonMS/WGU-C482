package smith.files;

// imported items
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import static smith.files.Inventory.*;

/**
 * class that contains methods for the main screen
 */
public class MainController implements Initializable {

    // table columns and searches
    @FXML private TableView<Part> mainViewPartsTable;
    @FXML private TableColumn<Part,Integer> partIDColumn;
    @FXML private TableColumn<Part,String> partNameColumn;
    @FXML private TableColumn<Part,Integer> partInventoryColumn;
    @FXML private TableColumn<Part,Double> partPriceColumn;
    @FXML private TableView<Product> mainViewProductsTable;
    @FXML private TableColumn<Product,Integer> productIDColumn;
    @FXML private TableColumn<Product,String> productNameColumn;
    @FXML private TableColumn<Product,Integer> productInventoryColumn;
    @FXML private TableColumn<Product,Double> productPriceColumn;
    @FXML private TextField mainPartsSearch;
    @FXML private TextField mainProductsSearch;

    /**
     * Searches the Product Inventory. The search is either based on the name or the ID.
     * If the ID is used, only one product is returned. If the name is used, all names
     * that match the text from the search bar are displayed.  If nothing matches, then an
     * error message is displayed. If the text box is empty, all products are displayed.
     */
    @FXML
    void enterProductSearch(){
        //gets text from search box
        String searchString = mainProductsSearch.getText();
        // searches by name first.
        ObservableList<Product> searchedProducts = searchByProductName(searchString);

        // if nothing comes back, searches by ID
        if (searchedProducts.isEmpty()){
            try {
                int productID = Integer.parseInt(searchString);
                Product p = searchByProductID(productID);
                if (p != null) {
                    searchedProducts.add(p);
                }
            }
            // displays error if no matches are found.
            catch(NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Product not found");
                alert.showAndWait();
            }
        }
        //displays matching products.
        mainViewProductsTable.setItems(searchedProducts);
    }

    /**
     * Searches the part Inventory. The search is either based on the name or the ID.
     * If the ID is used, only one part is returned. If the name is used, all names
     * that match the text from the search bar are displayed.  If nothing matches, then an
     * error message is displayed. If the text box is empty, all parts are displayed.
     */
    @FXML
    void enterPartSearch(){
        // gets text from string
        String searchString = mainPartsSearch.getText();
        // searches based on name
        ObservableList<Part> searchedParts = Inventory.searchByPartName(searchString);

        // searches based on ID
        if (searchedParts.isEmpty()){
            try {
                int partID = Integer.parseInt(searchString);
                Part p = searchByPartID(partID);
                if (p != null) {
                    searchedParts.add(p);
                }
            }
            // displays error message if search returns nothing
            catch(NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Part not found");
                alert.showAndWait();
            }
        }
        // displays search results in table
        mainViewPartsTable.setItems(searchedParts);

    }

    /**
     * Exits the application. A confirmation appears for the user to decide
     * if they really want to exit the application
     */
    @FXML
    void onMainExitButtonClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }

    }

    /**
     * Opens the Add Parts window.
     * @param event clicking the button
     * @throws IOException
     */
    @FXML
    void onMainPartsAddButtonClick(ActionEvent event) throws IOException {
        Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("add-part.fxml")));
        Scene mainScreenScene = new Scene(mainScreenParent);
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mainStage.setScene(mainScreenScene);
        mainStage.show();
    }

    /**
     * Opens the Modify Parts window. The window is loaded before it is opened so that
     * the part that is being modified can be handed off to the new controller. Displays an
     * error message if no part is selected.
     * @param event button being pressed
     */
    @FXML
    void onMainPartsModifyButtonClick(ActionEvent event){
        try {
            //loads modify parts but does not open it.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modify-part.fxml"));
            Parent root = loader.load();

            // moves the selected part from this controller to the new controller
            ModifyPartController mpController = loader.getController();
            Part selectedPart = mainViewPartsTable.getSelectionModel().getSelectedItem();
            mpController.PassPart(selectedPart);

            Scene mainScreenScene = new Scene(root);
            Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();
        // if nothing is selected, displays error message.
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Selection Error");
            alert.setContentText("Please select a part to modify");
            alert.showAndWait();
        }
    }

    /**
     * Opens the Modify Products window. The window is loaded before it is opened so that
     * the product that is being modified can be handed off to the new controller. Displays an
     * error message if no product is selected.
     * @param event button being pressed
     */
    @FXML
    void onMainProductsModifyButtonClick(ActionEvent event) {
        try {
            // loads modify product window, but does not open it.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("modify-product.fxml"));
            Parent root = loader.load();

            // transfers selected product from one controller to the next.
            ModifyProductController mpController = loader.getController();
            Product selectedProduct = mainViewProductsTable.getSelectionModel().getSelectedItem();
            mpController.PassProduct(selectedProduct);

            Scene mainScreenScene = new Scene(root);
            Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();
        // displays error if no product is selected.
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Selection Error");
            alert.setContentText("Please select a product to modify");
            alert.showAndWait();
        }
    }

    /**
     * Method opens the add product page.
     * @param event button click
     * @throws IOException
     */
    @FXML
    void onMainProductsAddButtonClick(ActionEvent event) throws IOException{
        Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("add-product.fxml")));
        Scene mainScreenScene = new Scene(mainScreenParent);
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mainStage.setScene(mainScreenScene);
        mainStage.show();
    }

    /**
     * RUNTIME ERROR - When trying to select a single part from the table, I had accidentally
     * said .getSelectedItems() instead of .getSelectedItem().  This caused an error because
     * Part can only contain one part instead of multiple like .getSelectedItems was trying
     * to do.
     *
     *
     * Method to delete Part that is selected.  If no part is selected an error message
     * is displayed.  Once a part is deleted, the page is refreshed.
     * @param event clicking button
     * @throws IOException IO Exception
     */
   @FXML
   public void onMainPartDeleteButtonClick(ActionEvent event) throws IOException {
       // select part to delete
        Part selectedPart = mainViewPartsTable.getSelectionModel().getSelectedItem();
        // if nothing selected, display error.
        if (selectedPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Selection Error");
            alert.setContentText("Must Select a part to delete.");
            alert.showAndWait();
            return;
        }
       // confirmation that the user wants to delete the part
       Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
       alert.setTitle("Confirmation");
       alert.setHeaderText("Are you sure?");
       alert.setContentText("Do you want to delete this part?");
       Optional<ButtonType> result = alert.showAndWait();
       if (result.isPresent() && result.get() == ButtonType.OK) {
           partInventory.remove(selectedPart);

           // reloads the page
           Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-view.fxml")));
           Scene mainScreenScene = new Scene(mainScreenParent);
           Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
           mainStage.setScene(mainScreenScene);
           mainStage.show();
       }
    }

    /**
     * Method to delete product that is selected.  If no product is selected an error message
     * is displayed.  Once a product is deleted, the page is refreshed.  Product cannot be
     * deleted if parts are associated with it.
     * @param event clicking button
     * @throws IOException IOException
     */
    @FXML
    public void onMainProductDeleteButtonClick(ActionEvent event) throws IOException {
        // choose a product to delete
        Product selectedProduct = mainViewProductsTable.getSelectionModel().getSelectedItem();
        // if nothing is selected, display error message.
        if (selectedProduct == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Selection Error");
            alert.setContentText("Must Select a product to delete.");
            alert.showAndWait();
            return;
        }
        // cannot delete product if parts are associated with it.
        else if (!selectedProduct.getAssociatedParts().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Selection Error");
            alert.setContentText("Product cannot be deleted if it has associated parts");
            alert.showAndWait();
            return;
        }
        // delete confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Do you want to delete this part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            productInventory.remove(selectedProduct);

            // refresh page
            Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-view.fxml")));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();
        }
    }

    /**
     * Initializes tables.  Populates Parts table with parts from parts inventory.
     * populates products table with products from products inventory
     * @param url url
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // loads table with parts
        mainViewPartsTable.setItems(partInventory);

        // sets table columns
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // loads table with products
        mainViewProductsTable.setItems(productInventory);

        // sets table columns
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        productInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
