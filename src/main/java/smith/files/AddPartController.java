package smith.files;

//imports needed to run the add-part screen
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

/**
 * This class contains functions for the add-part screen.  This screen
 * can be found after clicking the "add part" button on the main window.  Here,
 * a user can add a part to the part inventory.  The radio buttons at the top determine
 * whether it is an in-house part or an outsourced part.  Both are objects that inherit
 * the "Part" class.*/
public class AddPartController {

    // labels, buttons and text fields that will be used
    @FXML private Label addPartMachineIDLabel;
    @FXML private RadioButton addPartInHouseButton;
    @FXML private TextField addPartInvText;
    @FXML private TextField addPartMachineIDText;
    @FXML private TextField addPartMaxText;
    @FXML private TextField addPartMinText;
    @FXML private TextField addPartNameText;
    @FXML private RadioButton addPartOutsourcedButton;
    @FXML private TextField addPartPriceText;

    /**
     * This method changes the text to "Machine ID". If "In-House" is already
     * clicked, then nothing happens. If the radio dial "Outsourced" is selected, it
     * will be unselected when this button is pressed. */
    @FXML
    void onAddPartInHouseButtonClick()  {
        addPartMachineIDLabel.setText("Machine ID");
    }

    /**
     * This method changes the text to "Company Name". If "Outsourced" is already
     * clicked, then nothing happens. If the radio dial "In-house" is selected, it
     * will be unselected when this button is pressed*/
    @FXML
    void onAddPartOutsourcedClick(){
        addPartMachineIDLabel.setText("Company Name");
    }

    /**
     * This method cancels the "add part" function. When pressed, an alert will pop up
     * asking if the user is sure they want to cancel. If the user says yes, they will
     * return to the main screen.  If they select no, the user will return to the
     * "add part" screen with all of their data still in place.
     * @param event This is the button being clicked by the user.
     * @throws IOException IOException
     * */
    @FXML
    void onAddPartCancelButtonClick(ActionEvent event) throws IOException{
        // Alert to confirm the cancel.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();

        // Loads the main screen if the user confirms the cancel
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Parent mainScreenParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-view.fxml")));
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();
        }
    }

    /**
     * This saves the part that was created. The part is saved into the partInventory
     * found in the inventory class.  If the Inventory is not in between the min and
     * max, an error message is generated. If a user inputs something other than what
     * the program calls for, an error message is generated and the part is not saved.
     * Finally, the main screen loads and is put on the screen
     * @param event this is the button being clicked by the user.
     * @throws IOException when there is an I/O error
     * */
    @FXML
    public void onSaveButtonClick(ActionEvent event) throws IOException{
        try {
            // Storing values from the text box to a variable.  The partID is auto
            // generated by the AutoGenID method.
            int partID = AutoGenID();
            String partName = addPartNameText.getText();
            int inv = Integer.parseInt(addPartInvText.getText());
            int max = Integer.parseInt(addPartMaxText.getText());
            int min = Integer.parseInt(addPartMinText.getText());
            double price = Double.parseDouble(addPartPriceText.getText());

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
            alert.setTitle("Save");
            alert.setContentText("Are you sure you want to Save?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // If the InHouseButton is selected, save it as an InHousePart
                if (addPartInHouseButton.isSelected()) {
                    int machineID = Integer.parseInt(addPartMachineIDText.getText());
                    InHousePart savePart = new InHousePart(partID, partName, price, inv, min, max, machineID);
                    partInventory.add(savePart);
                // If the OutsourcedButton is selected, save it as an OutsourcedPart.
                } else if (addPartOutsourcedButton.isSelected()) {
                    String company = addPartMachineIDText.getText();
                    OutsourcedPart savePart = new OutsourcedPart(partID, partName, price, inv, min, max, company);
                    partInventory.add(savePart);
                }
                // Load the main screen.
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
     * This method generates an ID for a new part. The method takes all the currently
     * created parts and finds the highest ID.  The return is that highest number + 1.
     * If there are no parts in the partInventory, then the method returns 1.
     * @return 1 if partInventory is empty, or the highestID + 1
     * */
    @FXML
    public int AutoGenID(){
        // checks if the partInventory is empty. If so, return 1
        if(partInventory.isEmpty()) {return 1;}
        else {
            // checks for the highest ID from the partInventory
            int highestID = 1;
            for (Part p : partInventory) {
                if(p.getId() > highestID)
                {highestID = p.getId();}
            }
            // returns the highest ID plus 1 so that it is a new unique ID.
            return highestID + 1;
        }
    }

    /**
     * This initializes the Toggle Group.  When the page is loaded, the radio dials are
     * placed into a toggle group so that when one is pressed, the other is removed.
     * */
    @FXML
    private void initialize() {
        // Toggle Group for In-House and Outsourced.
        ToggleGroup group = new ToggleGroup();
        addPartInHouseButton.setToggleGroup(group);
        addPartOutsourcedButton.setToggleGroup(group);
    }

}

