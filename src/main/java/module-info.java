module smith.firstscreen {
    requires javafx.controls;
    requires javafx.fxml;


    opens smith.files to javafx.fxml;
    exports smith.files;
}