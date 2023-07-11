package frontend;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;

public class    Displaymode extends Application {

    MenuBar menuBar;
    Menu datei;
    Menu playlists;
    MenuItem genre;
    MenuItem alben;
    Button swap;
    Button exit;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Darstellungsmodus");

        // Macht Scene und Stage sichtbar
        Scene DisplayScene = createDisplayScene();

        primaryStage.setScene(DisplayScene);
        //primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public Scene createDisplayScene() {

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
        Image image = new Image("file:src/main/java/frontend/icons/mode2.PNG"); //es muss vornedran file: stehen
        ImageView img = new ImageView(image);
        swap = new Button("Musik");
        img.setFitWidth(swap.getWidth());
        img.setFitHeight(swap.getHeight()); //Button passt sich groesse des Bildes an
        swap.setGraphic(img);
        swap.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        swap.getStyleClass().add("swap");
        swap.setOnAction(e -> {
            // Methode zum Wechseln der Szene aufrufen
            switchToArchivemode();
        });

        // Beenden button
        exit = new Button("Save and Exit");
        exit.setOnAction(e-> System.exit(0));

        // exit button wird erst angepasst, wenn JavaFX vollst. initialisiert
        Platform.runLater(() -> exit.setPrefHeight(swap.getHeight()));

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
        Button playButton = createButton("Play", "file:src/main/java/frontend/icons/playglow.PNG");
        Button nextButton = createButton("Next", "file:src/main/java/frontend/icons/nextglow.PNG");
        Button previousButton = createButton("Previous", "file:src/main/java/frontend/icons/previousglow.PNG");

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

        Scene scene = new Scene(border, 960, 600);
        scene.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());
        scene.getStylesheets().add((new File("src/main/java/frontend/DarstellungGUI.css")).toURI().toString());

        return scene;
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

    public void switchToArchivemode() {
        try {
            Stage currentStage = (Stage) swap.getScene().getWindow();

            Archivemode musikverwaltung = new Archivemode(currentStage);
            Scene newScene = musikverwaltung.createScene();

            currentStage.setScene(newScene);
            currentStage.setTitle("Verwaltungsmodus");
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initiate() {
        launch();
    }
}
