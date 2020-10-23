package main.java.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.java.Main;

public class MenuController {
    @FXML public Button lexis_follow_button;
    @FXML public Button synt_follow_button;

    @FXML
    private void loadLexisAnalyserWindow () throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("gui/lex_gui.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 500, 600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Lexis Analyser");
        stage.show();
    }

    @FXML
    private void loadSyntAnalyserWindow () throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("gui/synt_gui.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 600, 500);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Syntactic Analyser");
        stage.show();
    }
}
