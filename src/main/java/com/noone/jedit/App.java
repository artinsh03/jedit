package com.noone.jedit;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;
    private static Stage stg;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("main"), 700, 480);
//        ===Set window resizable
        stage.setResizable(true);
        stage.setTitle("Jedit");
        stage.setScene(scene);
        stage.show();
        this.stg = stage;
     
    }
   



    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static void setTitle(String text) {
    	stg.setTitle(text);
    }
    
    public static void exit() {
    	Platform.exit();
    	System.exit(0);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}