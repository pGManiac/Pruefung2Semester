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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;

public class Displaymode extends Application {
    private Database data;
    private TableView<Song> albums;
    private TableColumn<Song, String> albumColumn;
    private TableColumn<Song, String> artistColumn;
    private MenuBar menuBar;
    private StackPane root;
    private BorderPane border;
    private Menu file;
    private Menu playlists;
    private MenuItem genre;
    private MenuItem albumsMenuItem;
    private HBox hbox;
    private HBox buttonsBox;
    private Button swap;
    private Button exit;
    private Button playButton;
    private Button nextButton;
    private Button previousButton;
    private MediaPlayer mediaPlayer;
    private MediaPlaylist mediaPlaylist;
    private Dialog<Song> adder;
    private ObservableList<Song> tableData;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Darstellungsmodus");

        // Make scene and stage visible
        Scene displayScene = createDisplayScene();
        data = readObjectFromFile();

        primaryStage.setScene(displayScene);
        // primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public Scene createDisplayScene() throws Exception {

        mediaPlaylist = new MediaPlaylist();
        border = new BorderPane(); // General layout

        // *** TOP ***
        file = new Menu("_File");
        file.setId("file");
        playlists = new Menu("_Playlists");
        playlists.setId("playlists");

        // Create menus
        menuBar = new MenuBar();
        menuBar.getStyleClass().add("menuBar");

        menuBar.getMenus().add(playlists);

        genre = new MenuItem("Genres");
        albumsMenuItem = new MenuItem("Albums");
        albumsMenuItem.setOnAction(e -> chooseAlbum());

        playlists.getItems().addAll(genre, albumsMenuItem);

        // Mode switch
        Image image = new Image("file:src/main/java/frontend/icons/mode2.PNG");
        ImageView img = new ImageView(image);
        swap = new Button("Music");
        img.setFitWidth(swap.getWidth());
        img.setFitHeight(swap.getHeight());
        swap.setGraphic(img);
        swap.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        swap.getStyleClass().add("swap");
        swap.setOnAction(e -> {
            // Call method to switch the scene
            switchToArchiveMode();
        });

        // Exit button
        exit = new Button("Speichern und Beenden");
        exit.setOnAction(e -> {
            saveAndExit();
        });

        // Adjust exit button once JavaFX is fully initialized
        Platform.runLater(() -> exit.setPrefHeight(swap.getHeight()));

        hbox = new HBox(menuBar, swap, exit);
        HBox.setHgrow(menuBar, Priority.ALWAYS);
        HBox.setHgrow(swap, Priority.NEVER);
        hbox.setStyle("-fx-background-color: transparent;");
        HBox.setHgrow(menuBar, Priority.ALWAYS);

        border.setTop(hbox);

        // *** CENTER ***
        Image image2 = new Image("file:src/main/java/frontend/icons/Platzhalter_CoverArt.jpg");
        ImageView imageView = new ImageView(image2);
        imageView.setFitHeight(400);
        imageView.setFitWidth(400);

        border.setCenter(imageView);

        // *** BOTTOM ***
        // Create buttons with icons
        playButton = createButton("Play", "file:src/main/java/frontend/icons/playglow.PNG");
        nextButton = createButton("Next", "file:src/main/java/frontend/icons/nextglow.PNG");
        previousButton = createButton("Previous", "file:src/main/java/frontend/icons/previousglow.PNG");

        // Functionality for buttons
        //playButton.setOnAction(e -> playSongsFromPlaylist());

        // Button sizes
        playButton.setPrefSize(80, 80);
        nextButton.setPrefSize(80, 80);
        previousButton.setPrefSize(80, 80);

        // Create HBox layout for buttons
        buttonsBox = new HBox(70);
        buttonsBox.getChildren().addAll(previousButton, playButton, nextButton);

        // Create main layout
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
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
            return new Database();
        }
    }

    public void writeObjectToFile() {
        try (FileOutputStream outputFile = new FileOutputStream("songObjects.ser", true);
             ObjectOutputStream outputObject = new ObjectOutputStream(new BufferedOutputStream(outputFile))) {
            outputObject.writeObject(this.data);
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }
    }

    public void saveAndExit() {
        File oldSer = new File("songObjects.ser");
        oldSer.delete();
        writeObjectToFile();
        System.exit(0);
    }

    private Button createButton(String name, String iconPath) {
        Button button = new Button(name);

        // Add an icon to the button if a valid path is provided
        if (iconPath != null && !iconPath.isEmpty()) {
            Image image = new Image(iconPath);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(80);
            imageView.setFitHeight(80);
            button.setGraphic(imageView);
            button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
        return button;
    }

    public void switchToArchiveMode() {
        try {
            Stage currentStage = (Stage) swap.getScene().getWindow();

            Archivemode musicManagement = new Archivemode(currentStage);
            musicManagement.setDatabase(this.data);
            Scene newScene = musicManagement.createScene();

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
        albums = new TableView<>();
        tableData = FXCollections.observableList(data.getAlbumHash().getOneSongPerAlbum());

        adder = new Dialog<>();
        adder.setTitle("Albumwahl");
        Label header = new Label("Wähle ein Album!");
        header.setId("albumSelectionHeader");
        adder.setResizable(true);

        albumColumn = new TableColumn<>("Album");
        artistColumn = new TableColumn<>("Artist");
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        albums.getColumns().add(albumColumn);
        albums.getColumns().add(artistColumn);
        albums.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        albums.setItems(tableData);

        DialogPane diaPane = adder.getDialogPane();
        diaPane.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());
        diaPane.getStyleClass().add("dialog");

        ButtonType okButtonType = new ButtonType("Fertig", ButtonBar.ButtonData.OK_DONE);
        BorderPane diaBorder = new BorderPane();

        StackPane stack = new StackPane(albums);
        diaBorder.setCenter(stack);
        diaBorder.setTop(header);
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

    public void setDatabase(Database database) {
        this.data = database;
    }

}

