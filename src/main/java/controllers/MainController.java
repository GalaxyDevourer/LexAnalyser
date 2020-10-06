package main.java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.entities.LexisEntity;
import main.java.models.LexAnalyser;

public class MainController {
    @FXML public TextArea sql_query;
    @FXML public TextArea shorted_query;

    @FXML public TableView<LexisEntity> lexis_table;
    @FXML public TableColumn<LexisEntity, String> token_column;
    @FXML public TableColumn<LexisEntity, String> lexis_column;
    @FXML public TableColumn<LexisEntity, Integer> start_column;
    @FXML public TableColumn<LexisEntity, Integer> length_column;
    @FXML public TableColumn<LexisEntity, Integer> position_column;

    @FXML public Button start_processing;

    @FXML private static String DEFAULT_QUERY = "DROP TABLE IF EXISTS Orenda ;\n" +
            "CREATE TABLE Orenda (\n" +
            "  kod int ( 11 ) NOT NULL ,\n" +
            "  data date DEFAULT NULL ,\n" +
            "  kodzasobu int ( 11 ) DEFAULT NULL ,\n" +
            "  kodorendarya int ( 11 ) NOT NULL ,\n" +
            "  strok int ( 11 ) NOT NULL\n" +
            ")\n" +
            "ALTER TABLE Orenda\n" +
            "  ADD PRIMARY KEY ( kod ),\n" +
            "  ADD UNIQUE KEY kodorendarya_index ( kodorendarya ),\n" +
            "  ADD KEY kodzas ( kodzasobu );";

    @FXML
    private void initialize() {
        token_column.setCellValueFactory(new PropertyValueFactory<>("token"));
        lexis_column.setCellValueFactory(new PropertyValueFactory<>("lexis"));
        start_column.setCellValueFactory(new PropertyValueFactory<>("start"));
        length_column.setCellValueFactory(new PropertyValueFactory<>("length"));
        position_column.setCellValueFactory(new PropertyValueFactory<>("postition"));

        sql_query.setText(DEFAULT_QUERY);
    }

    @FXML
    public void startProcessing () {
        String query = sql_query.getText();

        LexAnalyser analyser = new LexAnalyser(query);
        analyser.startLexisSplit();

        shorted_query.setText(analyser.getQuery());

        ObservableList<LexisEntity> observableList = FXCollections.observableArrayList(analyser.getLexis_list());
        lexis_table.setItems(observableList);
    }

    @FXML
    public void findSelectedItem () {
        LexisEntity entity = lexis_table.getSelectionModel().getSelectedItem();
        shorted_query.selectRange(entity.getReal_start(), entity.getReal_start() + entity.getLength());
    }
}
