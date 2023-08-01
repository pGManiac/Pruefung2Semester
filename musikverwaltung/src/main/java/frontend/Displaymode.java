package frontend;

import backend.Database;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.*;

public class Displaymode extends Application {
    private Database data;
    private MenuBar menuBar;
    private StackPane root;
    private BorderPane border;
    private Menu queue, playlists;
    private MenuItem addAllSongsMenuItem, genreMenuItem, albumsMenuItem, artistMenuItem, selectQueueMenuItem, deleteQueueMenuItem;
    private HBox hbox, buttonsBox;
    private Button swap, exit, playButton, nextButton, previousButton;
    private MediaPlaylist mediaPlaylist;
    private AddAllSongsGUI addAllSongsGUI;
    private ChooseAlbumGUI albumGUI;
    private ChooseGenreGUI genreGUI;
    private ChooseArtistGUI artistGUI;
    private SelectQueueGUI selectQueueGUI;
    private DeleteQueueGUI deleteQueueGUI;
    protected static Slider progressSlider;
    protected static Label currentTimeLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Darstellungsmodus");

        // Make scene and stage visible
        Scene displayScene = createDisplayScene();
        data = Database.getInstance();

        //create instances of Scene GUIS
        addAllSongsGUI = new AddAllSongsGUI(data, mediaPlaylist);
        albumGUI = new ChooseAlbumGUI(data, mediaPlaylist);
        genreGUI = new ChooseGenreGUI(data, mediaPlaylist);
        artistGUI = new ChooseArtistGUI(data, mediaPlaylist);
        selectQueueGUI = new SelectQueueGUI(mediaPlaylist);
        deleteQueueGUI = new DeleteQueueGUI(mediaPlaylist);

        primaryStage.setScene(displayScene);
        // primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public Scene createDisplayScene() {

        mediaPlaylist = new MediaPlaylist();
        border = new BorderPane(); // General layout

        // *** TOP ***
        queue = new Menu("_Wiedergabeliste");
        playlists = new Menu("_Playlists");
        queue.setId("queue");
        playlists.setId("playlists");

        // Create menus
        menuBar = new MenuBar();
        menuBar.getStyleClass().add("menuBar");
        menuBar.getMenus().addAll(playlists, queue);

        // Playlist
        addAllSongsMenuItem = new MenuItem("Alle Songs");
        genreMenuItem = new MenuItem("Genres");
        albumsMenuItem = new MenuItem("Alben");
        artistMenuItem = new MenuItem("Interpreten");
        addAllSongsMenuItem.setOnAction(e -> addAllSongsGUI.addAllSongs());
        genreMenuItem.setOnAction(e -> genreGUI.selectGenre());
        albumsMenuItem.setOnAction(e -> albumGUI.chooseAlbum());
        artistMenuItem.setOnAction(e -> artistGUI.selectArtist());
        playlists.getItems().addAll(addAllSongsMenuItem, genreMenuItem, albumsMenuItem, artistMenuItem);

        // Queue
        selectQueueMenuItem = new MenuItem("Song wählen/entfernen");
        deleteQueueMenuItem = new MenuItem("Alle Songs entfernen");
        selectQueueMenuItem.setOnAction(e -> selectQueueGUI.selectQueue());
        deleteQueueMenuItem.setOnAction(e -> {
            deleteQueueGUI.deleteQueue();
        });
        queue.getItems().addAll(selectQueueMenuItem, deleteQueueMenuItem);

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
        exit.setOnAction(e -> saveAndExit());

        // Adjust exit button once JavaFX is fully initialized
        Platform.runLater(() -> exit.setPrefHeight(swap.getHeight()));

        hbox = new HBox(menuBar, swap, exit);
        HBox.setHgrow(menuBar, Priority.ALWAYS);
        HBox.setHgrow(swap, Priority.NEVER);
        hbox.setStyle("-fx-background-color: transparent;");
        HBox.setHgrow(menuBar, Priority.ALWAYS);
        border.setTop(hbox);

        // *** CENTER ***
        Label songTitle = new Label("");
        songTitle.getStyleClass().add("songtitle");

        Image image1 = new Image("file:src/main/java/frontend/icons/Platzhalter_CoverArt.jpg");
        ImageView imageView = new ImageView(image1);
        imageView.setFitHeight(400);
        imageView.setFitWidth(400);
        //border.setCenter(imageView);

        // Slider
        progressSlider = new Slider();
        progressSlider.setMaxWidth(750);
        currentTimeLabel = new Label("0:00");

        // Add listener to the Slider to update the current time label and adjust song playback
        progressSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mediaPlaylist.seekTo(newValue.doubleValue()); // Update the song playback to the new value
            MediaPlaylist.counter = newValue.doubleValue();
            currentTimeLabel.setText(formatTime(newValue.doubleValue())); // Update the label with the current time
        });

        VBox imageBox = new VBox(30);
        imageBox.getChildren().addAll(imageView, progressSlider, currentTimeLabel, songTitle);
        imageBox.setAlignment(Pos.CENTER);
        border.setCenter(imageBox);

        // Add a listener to the mediaPlaylist to update the label with the currently playing song title
        mediaPlaylist.getCurrentSongProperty().addListener((observable, oldValue, newValue) -> {
            // Update the label with the currently playing song title
            if (newValue != null) {
                songTitle.setText(newValue.getName() + " - " + newValue.getArtist()); // Assuming the Song class has a getName() method to get the song title
            } else {
                // If there is no currently playing song, clear the label
                songTitle.setText("");
            }
        });

        // *** BOTTOM ***
        // Create buttons with icons
        playButton = createButton("Play", "file:src/main/java/frontend/icons/playglow.PNG");
        nextButton = createButton("Next", "file:src/main/java/frontend/icons/nextglow.PNG");
        previousButton = createButton("Previous", "file:src/main/java/frontend/icons/previousglow.PNG");

        // Functionality for buttons
        playButton.setOnAction(e -> {
            if (mediaPlaylist.getMediaPlayer() != null && mediaPlaylist.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
                // If the media is playing, pause it and save the time
                mediaPlaylist.pause();
                mediaPlaylist.setStoredPlaybackPosition(mediaPlaylist.getCurrentTime());
            } else {
                // otherwise resume playing
                    mediaPlaylist.resumePlaying();
            }
        });

        nextButton.setOnAction(e -> mediaPlaylist.playNextSong());
        previousButton.setOnAction(e -> mediaPlaylist.playPreviousSong());

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

        Scene scene = new Scene(border, 1100, 900);
        scene.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());
        scene.getStylesheets().add((new File("src/main/java/frontend/DarstellungGUI.css")).toURI().toString());

        return scene;
    }

    public void writeObjectToFile() {
        try (FileOutputStream outputFile = new FileOutputStream("songObjects.ser", true);
             ObjectOutputStream outputObject = new ObjectOutputStream(new BufferedOutputStream(outputFile))) {
            outputObject.writeObject(this.data);
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }
    }

    // TODO IntelliJ says result of 'File.delete()' is ignored?
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

    // Utility method to format time in seconds to "mm:ss" format
    private String formatTime(double seconds) {
        int minutes = (int) seconds / 60;
        int remainingSeconds = (int) seconds % 60;
        return String.format("%2d:%02d", minutes, remainingSeconds);
    }

    public void switchToArchiveMode() {
        try {
            mediaPlaylist.stop();
            Stage currentStage = (Stage) swap.getScene().getWindow();

            Archivemode musicManagement = new Archivemode(currentStage);
            musicManagement.setDatabase(Database.getInstance());
            Scene newScene = musicManagement.createScene();

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

    public void setDatabase(Database database) {
        this.data = database;
    }
}
