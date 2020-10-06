package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("gui/main_gui.fxml"));
        primaryStage.setTitle("Lexis Analyser");

        Scene scene = new Scene(root, 500, 600);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getClassLoader().getResource("style.css")).toExternalForm()
        );

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
