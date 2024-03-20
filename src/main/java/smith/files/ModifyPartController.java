package smith.files;

// items imported
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import static smith.files.Inventory.partInventory;

/** This class contains functions for the modify-part screen.  This screen
 * can be found after clicking the "modify part" button on the main window.  Here,
 * a user can modify a part from the part inventory.  The radio buttons at the top determine
 * whether it is an in-house part or an outsourced part.  Both are objects that inherit
 * the "Part" class.*/
public class ModifyPartController {

    // initializes labels, radio buttons and text fields
    @FXML private Label modifyPartMachineIDLabel;
    @FXML private TextField modifyPartIDText;
    @FXML private RadioButton modifyPartInHouseButton;
    @FXML private TextField modifyPartInvText;
    @FXML private TextField modifyPartMachineIDText;
    @FXML private TextField modifyPartMaxText;
    @FXML private TextField modifyPartMinText;
    @FXML private TextField modifyPartNameText;
    @FXML private RadioButton modifyPartOutsourcedButton;
    @FXML private TextField modifyPartPriceText;
    /**
     * part to be used in this controller
     */
    public Part part;

    /**
     * This method cancels the "modify part" function. When pressed, an alert will pop up
     * asking if the user is sure they want to cancel. If the user says yes, they will
     * return to the main screen.  If they select no, the user will return to the
     * "modify part" screen with all of their data still in place.
     * @param event This is the button being clicked by the user.
     * @throws IOException
     */
    @FXML
    void onModifyPartCancelButtonClick(ActionEvent event) throws IOException{
        // Alert confirming cancel
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();

        // if user says yes, load the main page.
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-view.fxml")));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();
        }
    }
    /**
     * This saves the part that was modified. The part is updated in the partInventory
     * found in the inventory class.  If the Inventory is not in between the min and
     * max, an error message is generated. If a user inputs something other than what
     * the program calls for, an error message is generated and the part is not saved.
     * Finally, the main screen loads and is put on the screen
     * @param event this is the button being clicked by the user.
     * */
    @FXML
    void onSaveButtonClick(ActionEvent event) throws IOException{
        try {
            // saves text box data into variables
            int partID = Integer.parseInt(modifyPartIDText.getText());
            String partName = modifyPartNameText.getText();
            int inv = Integer.parseInt(modifyPartInvText.getText());
            int max = Integer.parseInt(modifyPartMaxText.getText());
            int min = Integer.parseInt(modifyPartMinText.getText());
            double price = Double.parseDouble(modifyPartPriceText.getText());

            // max cannot be less than min, or else an error shows.
            if (max < min){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Max cannot be less than Min.");
                alert.showAndWait();
                return;
            }
            // inv has to be in between  min and max.
            if (inv < min || inv > max){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inv must be in between Min and Max.");
                alert.showAndWait();
                return;
            }

            // Confirmation that the user wants to save their part into the partInventory
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setContentText("Are you sure you want to save?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (modifyPartInHouseButton.isSelected()) {
                    // If the InHouseButton is selected, save it as an InHousePart
                    int machineID = Integer.parseInt(modifyPartMachineIDText.getText());
                    InHousePart savePart = new InHousePart(partID, partName, price, inv, min, max, machineID);
                    partInventory.remove(this.part);
                    partInventory.add(savePart);
                } else if (modifyPartOutsourcedButton.isSelected()) {
                    // If the OutsourcedButton is selected, save it as an OutsourcedPart.
                    String company = modifyPartMachineIDText.getText();
                    OutsourcedPart savePart = new OutsourcedPart(partID, partName, price, inv, min, max, company);
                    partInventory.remove(this.part);
                    partInventory.add(savePart);

                }

                // loads main page
                Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-view.fxml")));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainStage.setScene(mainScreenScene);
                mainStage.show();
            }

        // If one of the fields has the wrong data type in it, display an error.
        }catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setContentText("Inv, Max, Min, and/or Machine ID must be an integer. Price must be an integer or double.");
            alert.showAndWait();
        }
    }

    /**
     * This method changes the text to "Machine ID". If "In-House" is already
     * clicked, then nothing happens. If the radio dial "Outsourced" is selected, it
     * will be unselected when this button is pressed.
     */
     @FXML
     void onModifyPartInHouseButtonClick() {
         modifyPartMachineIDLabel.setText("Machine ID");
    }

    /**
     * This method changes the text to "Company Name". If "Outsourced" is already
     * clicked, then nothing happens. If the radio dial "In-house" is selected, it
     * will be unselected when this button is pressed*/
     @FXML
     void onModifyPartOutsourcedClick() {
         modifyPartMachineIDLabel.setText("Company Name");
     }

    /**
     * Method used to move selected part from one controller to the next.
     * Populates the text fields with all the information from the part that
     * was selected.
     * @param part used to populate text boxes with correct data.
     */
     public void PassPart(Part part){
         // puts data into variables
         this.part = part;
         String partName = part.getName();
         int partID = part.getId();
         double partPrice = part.getPrice();
         int partStock = part.getStock();
         int partMin = part.getMin();
         int partMax = part.getMax();

         // puts variable information into the text boxes
         modifyPartIDText.setText(Integer.toString(partID));
         modifyPartNameText.setText(partName);
         modifyPartPriceText.setText(Double.toString(partPrice));
         modifyPartInvText.setText(Integer.toString(partStock));
         modifyPartMinText.setText(Integer.toString(partMin));
         modifyPartMaxText.setText(Integer.toString(partMax));

         // if part is In-House, move the radio dial to appropriate button and
         // set text to partMachineID
         if (part instanceof InHousePart inHousePart){
             int partMachineID = inHousePart.GetMachineID();
             modifyPartMachineIDText.setText(Integer.toString(partMachineID));
        }
        else{
            // if part is Outsourced, move the radio dial to appropriate button,
             // set text to partCompanyName, and change the label to "Company Name:
            OutsourcedPart outsourcedPart = (OutsourcedPart) part;
            String partCompanyName = outsourcedPart.GetCompanyName();
            modifyPartMachineIDText.setText(partCompanyName);
            modifyPartMachineIDLabel.setText("Company Name");
            modifyPartOutsourcedButton.fire();


         }
     }

    /**
     * This initializes the Toggle Group.  When the page is loaded, the radio dials are
     * placed into a toggle group so that when one is pressed, the other is removed.
     * */
    @FXML
    private void initialize() {
        // Creates a toggle group for the radio buttons
        ToggleGroup group = new ToggleGroup();
        modifyPartInHouseButton.setToggleGroup(group);
        modifyPartOutsourcedButton.setToggleGroup(group);
    }

}

