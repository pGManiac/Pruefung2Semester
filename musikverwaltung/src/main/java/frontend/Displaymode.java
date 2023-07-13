package frontend;

import backend.Database;
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
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Optional;

public class    Displaymode extends Application {

    private Database data;
    TableView<Song> albums;
    TableColumn<Song, String> albumColumn;
    TableColumn<Song, String> artistColumn;
    MenuBar menuBar;
    StackPane root;
    BorderPane border;
    Menu datei;
    Menu playlists;
    MenuItem genre;
    MenuItem alben;
    HBox hbox;
    HBox buttonsBox;
    Button swap;
    Button exit;
    Button playButton;
    Button nextButton;
    Button previousButton;
    private MediaPlayer mediaPlayer;
    private MediaPlaylist mediaPlaylist;
    Dialog<Song> adder;
    ObservableList<Song> tableData;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Darstellungsmodus");

        // Macht Scene und Stage sichtbar
        Scene DisplayScene = createDisplayScene();

        primaryStage.setScene(DisplayScene);
        //primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public Scene createDisplayScene() throws Exception {

        data = readObjectFromFile();
        mediaPlaylist = new MediaPlaylist();
        border = new BorderPane(); // allgemeines Layout

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
        alben.setOnAction(e -> chooseAlbum());


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

        hbox = new HBox(menuBar, swap, exit); //menubar in hbox + swap
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
        playButton = createButton("Play", "file:src/main/java/frontend/icons/playglow.PNG");
        nextButton = createButton("Next", "file:src/main/java/frontend/icons/nextglow.PNG");
        previousButton = createButton("Previous", "file:src/main/java/frontend/icons/previousglow.PNG");

        // functionality for buttons
        //playButton.setOnAction(e -> playSongsFromPlaylist());

        //Groesse der Buttons
        playButton.setPrefSize(80, 80); // Breite: 80, Höhe: 40
        nextButton.setPrefSize(80, 80);
        previousButton.setPrefSize(80, 80);

        // Erstelle das HBox-Layout für die Buttons
        buttonsBox = new HBox(70); // 10 ist der Abstand zwischen den Buttons
        buttonsBox.getChildren().addAll(previousButton, playButton, nextButton);


        // Erstelle das Hauptlayout
        // Hier kann man versch. layouts verwenden, z. B. BorderPane, VBox, etc.
        root = new StackPane();
        HBox centerBox = new HBox(buttonsBox);
        centerBox.setAlignment(Pos.CENTER);
        root.getChildren().addAll(centerBox);

        border.setBottom(root);

        Scene scene = new Scene(border, 960, 600);
        scene.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());
        scene.getStylesheets().add((new File("src/main/java/frontend/DarstellungGUI.css")).toURI().toString());

        return scene;
    }

    public Database readObjectFromFile() throws ClassNotFoundException {
        try (FileInputStream inputFile = new FileInputStream("songObjects.ser");
             ObjectInputStream inputObject = new ObjectInputStream(inputFile)) {
            return (Database) inputObject.readObject();
        }
        catch (IOException ioException) {
            System.err.println(ioException.getMessage());
            return new Database();
        }
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

    private void playSongsFromList(List<Song> songs) {
        MediaPlaylist mediaPlaylist = new MediaPlaylist();
        mediaPlaylist.setSongs(songs);
        mediaPlaylist.play();
    }

    public void chooseAlbum() {
        // GUI
        albums = new TableView<>();
        tableData = FXCollections.observableList(data.getAlbumHash().getOneSongPerAlbum());

        adder = new Dialog<>();
        adder.setTitle("Albumwahl");
        Label headerE = new Label("Wähle ein Album!");
        headerE.setId("headerAlbumwahl");
        adder.setResizable(true);

        albumColumn = new TableColumn<Song, String>("Album");
        artistColumn = new TableColumn<Song, String>("Interpret");
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        albums.getColumns().add(albumColumn);
        albums.getColumns().add(artistColumn);
        albums.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //Spalten passen sich window an
        albums.setItems(tableData);

        // add to have stylesheets
        DialogPane diaPane = adder.getDialogPane();
        diaPane.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());
        diaPane.getStyleClass().add("dialog");

        ButtonType okButtonType = new ButtonType("Fertig", ButtonBar.ButtonData.OK_DONE); //dieser Button ist notwendig zum Schliessen des Dialogs
        BorderPane diaBorder = new BorderPane();

        StackPane stack = new StackPane(albums);
        diaBorder.setCenter(stack);
        diaBorder.setTop(headerE);
        adder.getDialogPane().setContent(diaBorder);
        adder.getDialogPane().getButtonTypes().add(okButtonType);
        adder.getDialogPane().setPrefSize(900, 800);
        adder.showAndWait();

        adder.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                Song selectedSong = albums.getSelectionModel().getSelectedItem();
                if (selectedSong != null) {
                    List<Song> selectedSongs = data.getAlbumHash().getAllSongsFromAlbum(selectedSong);
                    mediaPlaylist.setSongs(selectedSongs);
                    mediaPlaylist.play();
                }
            }
            return null;
        });

    }

    public static void initiate() {
        launch();
    }
}

