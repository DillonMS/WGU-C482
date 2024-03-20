package smith.files;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import static smith.files.Inventory.partInventory;
import static smith.files.Inventory.productInventory;

/**
 * ////////////JAVADOCS LOCATED IN THE FOLDER FIRSTSCREEN/JAVADOCS//////////
 * Class that initiates the main screen and opens it for the user.
 */
public class Main extends Application {
    @Override
    /**
     * loads the main screen
     * @param stage loads the main screen
     */
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Inventory Management");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This is the main method of the program. Test data is already put in.
     * @param args meant for compiling and running.
     */
    public static void main(String[] args)
    {
        // test data created and placed in their respective inventories.
        OutsourcedPart screw = new OutsourcedPart(1, "screw", 0.99, 15, 0, 100, "Home Depot" );
        InHousePart bolt = new InHousePart(2, "bolt", 0.99, 15, 0, 100, 1 );
        partInventory.add(screw);
        partInventory.add(bolt);
        Product bike = new Product(1, "bike", 50.00, 5, 1, 20);
        Product scooter = new Product(2, "scooter", 50.00, 5, 1, 20);
        productInventory.add(bike);
        productInventory.add(scooter);
        launch();
    }
}