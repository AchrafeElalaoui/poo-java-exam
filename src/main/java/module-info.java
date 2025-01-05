module org.example.exam {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.exam to javafx.fxml;
    exports org.example.exam;
}