package org.example.exam.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.example.exam.dao.PlatPrincipalDAO;
import org.example.exam.models.PlatPrincipal;
import org.example.exam.models.Repas;

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
                    detils.setOnAction(event -> gitD(getTableRow().getItem()));
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
    public void gitD(PlatPrincipal item) {

    }



}
