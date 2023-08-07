package it.faint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import it.faint.controller.MainController;
import it.faint.model.DrawEditor;
import it.faint.model.Drawing;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main.fxml"));
        Parent root = fxmlLoader.load();
        scene = new Scene(root, 1080, 720);
        stage.setScene(scene);

        MainController mainController = fxmlLoader.getController();
        Drawing drawing = new Drawing();
        DrawEditor drawEditor = new DrawEditor(drawing);
        mainController.initModel(drawEditor);

        //stage.getIcons().add(new Image(getClass().getResourceAsStream("/it/unisa/diem/gruppo8/img/icon.png").toString(), true));

        stage.setTitle("Faint v0.3");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}