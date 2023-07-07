package frontend;

import backend.Song;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;

public class Displaymode extends Application {

    Button button;
    MenuBar menuBar;
    Menu datei;
    Menu playlists;
    Menu darstellung;
    MenuItem hinzu;
    MenuItem entfernen;
    MenuItem az;
    MenuItem za;
    MenuItem genre;
    MenuItem alben;
    MenuItem metal;
    MenuItem pop;
    MenuItem rock;
    MenuItem klassik;
    MenuItem country;
    TableView<Song> lieder;
    ScrollPane scroller;
    Dialog<Song> adder;
    ObservableList<String> options = FXCollections.observableArrayList("Metal", "Pop", "Rock", "Klassik", "Country");
    FileChooser fileChooser = new FileChooser();
    Button swap;
    Button exit;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Darstellungsmodus");
        BorderPane border = new BorderPane(); // allgemeines Layout

        // *** TOP ***
        datei = new Menu("_Datei");
        datei.setId("datei");
        playlists = new Menu("_Playlists");
        playlists.setId("playlists");

        // Erschafft Menues
        menuBar = new MenuBar();
        menuBar.getStyleClass().add("menuBar");

        menuBar.getMenus().add(playlists);

        genre = new MenuItem("Genres");
        alben = new MenuItem("Alben");

        playlists.getItems().addAll(genre, alben);

        //Moduswechsel
        Image image = new Image("file:src/main/java/frontend/icons/mode11.png"); //es muss vornedran file: stehen
        ImageView img = new ImageView(image);
        swap = new Button("Musik");
        img.setFitWidth(swap.getWidth());
        img.setFitHeight(swap.getHeight()); //Button passt sich groesse des Bildes an
        swap.setGraphic(img);
        swap.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        swap.getStyleClass().add("swap");
        swap.setOnAction(e -> {
            // Methode zum Wechseln der Szene aufrufen
            switchScene();
        });

        // Beenden button
        exit = new Button("Beenden");
        exit.setOnAction(e-> {System.exit(0);});

        // exit button wird erst angepasst, wenn JavaFX vollst. initialisiert
        Platform.runLater(() -> {
            exit.setPrefHeight(swap.getHeight());
        });

        HBox hbox = new HBox(menuBar, swap, exit); //menubar in hbox + swap
        HBox.setHgrow(menuBar, Priority.ALWAYS);
        HBox.setHgrow(swap, Priority.NEVER);
        hbox.setStyle("-fx-background-color: transparent;");
        HBox.setHgrow(menuBar, Priority.ALWAYS); //menubar darf sich resizen

        border.setTop(hbox);

        // *** CENTER ***
        Image image2 = new Image("file:src/main/java/frontend/icons/Platzhalter_CoverArt.jpg"); //es muss vornedran file: stehen
        ImageView imageView = new ImageView(image2);
        imageView.setFitHeight(400);
        imageView.setFitWidth(400);

        border.setCenter(imageView);

        // *** BOTTOM ***
        // Erstelle die Buttons mit Icon
        Button playButton = createButton("Play", "file:src/main/java/frontend/icons/play.png");
        Button nextButton = createButton("Next", "file:src/main/java/frontend/icons/next.png");
        Button previousButton = createButton("Previous", "file:src/main/java/frontend/icons/previous.png");

        //Groesse der Buttons
        playButton.setPrefSize(80, 80); // Breite: 80, Höhe: 40
        nextButton.setPrefSize(80, 80);
        previousButton.setPrefSize(80, 80);

        // Erstelle das HBox-Layout für die Buttons
        HBox buttonsBox = new HBox(70); // 10 ist der Abstand zwischen den Buttons
        buttonsBox.getChildren().addAll(previousButton, playButton, nextButton);


        // Erstelle das Hauptlayout
        // Hier kann man versch. layouts verwenden, z. B. BorderPane, VBox, etc.
        StackPane root = new StackPane();
        HBox centerBox = new HBox(buttonsBox);
        centerBox.setAlignment(Pos.CENTER);
        root.getChildren().addAll(centerBox);

        border.setBottom(root);

        // Macht Scene und Stage sichtbar
        Scene scene = new Scene(border, 960, 600);
        scene.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());
        scene.getStylesheets().add((new File("src/main/java/frontend/DarstellungGUI.css")).toURI().toString());

        primaryStage.setScene(scene);
        //primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private Button createButton(String name, String iconPath) {
        Button button = new Button(name);

        // Fuege ein Icon zum Button hinzu, falls ein gueltiger Pfad angegeben ist
        if (iconPath != null && !iconPath.isEmpty()) {
            Image image = new Image(iconPath);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(80); // Breite des Icons festlegen
            imageView.setFitHeight(80); // height des Icons festlegen
            button.setGraphic(imageView);
            button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

        return button;
    }

    private void switchScene(Stage primaryStage) {
        // Musik-Szene anzeigen
        MusikverwaltungFXGUI managementGUI = new MusikverwaltungFXGUI();
        MusikverwaltungFXGUI.initiate();
        primaryStage.setScene(managementGUI.getScene());
    }

    public static void initiate() {
        launch();
    }
}
