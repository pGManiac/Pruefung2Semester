package frontend;

import backend.Song;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class JavaFX extends Application {

    @Override
    public void start(Stage stage) {
        Song song = new Song("hi","hi", "Pop", "hans", "0");
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " +  song.getName()+ javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void initiate() {
        launch();
    }

    public static class SelectQueueGUI {
    }
}