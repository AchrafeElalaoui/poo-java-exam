package org.example.exam.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.exam.dao.IngredientDAO;
import org.example.exam.models.Ingredient;

public class IngredientCo {
    @FXML
    private TableView<Ingredient> tabRopat;
    @FXML
    private TableColumn<Ingredient, String> DitC;

    @FXML
    private TableColumn<Ingredient, String> nomCol;
    IngredientDAO pdao = new IngredientDAO();
    private int id;


    public void initialize() {

        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        DitC.setCellFactory(column -> new TableCell<Ingredient, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    Button add = new Button("add to plat");
                    add.setOnAction(event -> gitD(getTableRow().getItem()));
                    setGraphic(add);
                }
            }


        });
        loadR();
    }
    private void loadR() {
        tabRopat.setItems(FXCollections.observableArrayList(pdao.getIngredientsByPlatPrincipalId(id)));
        tabRopat.refresh();
    }
    public void gitD(Ingredient item) {

    }

    public void setId(int id) {
        this.id=id;
    }
}
