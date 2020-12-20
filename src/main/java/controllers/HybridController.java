package controllers;

import entities.HybridEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.hybrid.HybridAnalyser;

public class HybridController {
    @FXML public TextArea inputArea;
    @FXML public Button analyseButton;

    @FXML public TableView<HybridEntity> outputTable;
    @FXML public TableColumn<HybridEntity, Integer> positionColumn;
    @FXML public TableColumn<HybridEntity, String> tokenColumn;
    @FXML public TableColumn<HybridEntity, String> lexemColumn;
    @FXML public TableColumn<HybridEntity, Integer> startColumn;
    @FXML public TableColumn<HybridEntity, Integer> lengthColumn;
    @FXML public TableColumn<HybridEntity, String> stackColumn;
    @FXML public TableColumn<HybridEntity, String> inputColumn;
    @FXML public TableColumn<HybridEntity, String> productionColumn;

    @FXML
    public void initialize () {
        inputArea.setText("UPDATE Customers " +
                "SET ContactName = 'Alfred Schmidt' , City = 'Frankfurt' " +
                "WHERE CustomerID = 1");

        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        tokenColumn.setCellValueFactory(new PropertyValueFactory<>("token"));
        lexemColumn.setCellValueFactory(new PropertyValueFactory<>("lexem"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));
        stackColumn.setCellValueFactory(new PropertyValueFactory<>("stack"));
        inputColumn.setCellValueFactory(new PropertyValueFactory<>("input"));
        productionColumn.setCellValueFactory(new PropertyValueFactory<>("production"));
    }

    @FXML
    public void startAnalyse () {
        String input = inputArea.getText();
        HybridAnalyser analyser = new HybridAnalyser(input);

        if (!analyser.isTableExist()) dialogRegMessage("Analysis Warning!",
                "Not valid table :(",
                "It looks like your table is not exist!", Alert.AlertType.WARNING);

        if (!analyser.isColumnExist()) dialogRegMessage("Analysis Warning!",
                "Not valid column :(",
                "It looks like your column in table is not exist!", Alert.AlertType.WARNING);

                Integer code = analyser.startAnalysis();

        ObservableList<HybridEntity> observableList = FXCollections.observableList(analyser.getHybridEntities());
        outputTable.setItems(observableList);

        switch (code) {
            case 0:
                dialogRegMessage("Analysis Info!",
                        "Analysis completed!",
                        "Analysis successfully over and data put in the table!", Alert.AlertType.INFORMATION);
                break;

            case -1:
                dialogRegMessage("Analysis Error!",
                        "Not valid query :(",
                        "It looks like your query is not valid!", Alert.AlertType.ERROR);
                break;

            case -2:
                dialogRegMessage("Analysis Error!",
                        "Jump Table not found :(",
                        "Sorry, this analyser do not support your query!", Alert.AlertType.ERROR);
                break;

            default:
                break;
        }

    }

    private void dialogRegMessage(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
