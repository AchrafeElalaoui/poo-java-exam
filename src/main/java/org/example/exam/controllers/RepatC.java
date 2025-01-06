package org.example.exam.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.exam.HelloApplication;
import org.example.exam.dao.PlatPrincipalDAO;
import org.example.exam.models.PlatPrincipal;
import org.example.exam.models.Repas;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RepatC  {
    @FXML
    private TableView<PlatPrincipal> tabRopat;
    @FXML
    private TableColumn<PlatPrincipal, String> DitC;

    @FXML
    private TableColumn<PlatPrincipal, String> nomCol;
    private PlatPrincipalDAO pdao = new PlatPrincipalDAO();


    public void initialize() {

        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        DitC.setCellFactory(column -> new TableCell<PlatPrincipal, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    Button detils = new Button("detils");
                    detils.setOnAction(event -> {
                        try {
                            gitD(getTableRow().getItem());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    setGraphic(detils);
                }
            }


        });
        loadR();
    }
    private void loadR() {
        tabRopat.setItems(FXCollections.observableArrayList(pdao.findAll()));
        tabRopat.refresh();
    }
    IngredientCo d=new IngredientCo();
    public void gitD(PlatPrincipal item) throws IOException {
            d.setId(item.getId());
        Stage stage=new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(RepatC.class.getResource("gra.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }



}
