module org.example.exam {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;


    opens org.example.exam.controllers to javafx.fxml;
    opens org.example.exam.models  to javafx.base;

    exports org.example.exam;
}