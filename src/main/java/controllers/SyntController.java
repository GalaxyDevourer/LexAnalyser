package main.java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.entities.SyntEntity;
import main.java.models.SyntAnalyser;

public class SyntController {
    @FXML public Button start_processing_button;
    @FXML public TextField input_field;

    @FXML public TableView<SyntEntity> synt_table;
    @FXML public TableColumn<SyntEntity, String> number_column;
    @FXML public TableColumn<SyntEntity, String> stack_column;
    @FXML public TableColumn<SyntEntity, String> input_column;
    @FXML public TableColumn<SyntEntity, String> prod_column;

    @FXML
    private void initialize() {
        number_column.setCellValueFactory(new PropertyValueFactory<>("number"));
        stack_column.setCellValueFactory(new PropertyValueFactory<>("stack"));
        input_column.setCellValueFactory(new PropertyValueFactory<>("input"));
        prod_column.setCellValueFactory(new PropertyValueFactory<>("production"));

        input_column.setStyle( "-fx-alignment: CENTER-RIGHT;");
    }

    @FXML
    public void startProcessing () {

        SyntAnalyser analyser = new SyntAnalyser(input_field.getText());
        int code = analyser.startSyntAnalysis();

        ObservableList<SyntEntity> observableList = FXCollections.observableList(analyser.getSyntEntityList());
        synt_table.setItems(observableList);

        if (code == -1) dialogRegMessage("Analysis Error!",
                "It looks like the expression is not composed correctly :(",
                "Please see the output of the table to understand where the error occurred!", Alert.AlertType.ERROR);

        else dialogRegMessage("Successfully analysis!",
                "Expression analysis was successful!",
                "The analysis results are presented in the table below.", Alert.AlertType.INFORMATION);


    }

    private void dialogRegMessage(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
