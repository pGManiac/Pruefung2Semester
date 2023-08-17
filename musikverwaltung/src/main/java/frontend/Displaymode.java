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

/**
 * @brief The Displaymode class represents the graphical user interface for the Darstellungsmodus (Display mode) of the music player application.
 *        It extends the JavaFX Application class and provides methods for creating the display scene, handling user interactions, and switching to the Archivemode.
 */
public class Displaymode extends Application {
    // Properties
    private Database data;
    private MenuBar menuBar;
    private StackPane root;
    private BorderPane border;
    private Menu queue, playlists;
    private MenuItem addAllSongsMenuItem, genreMenuItem, albumsMenuItem, artistMenuItem, selectQueueMenuItem, deleteQueueMenuItem;
    private HBox hbox, buttonsBox;
    private Button swap, exit, nextButton, previousButton;
    private static Button playButton;
    private MediaPlaylist mediaPlaylist;
    private AddAllSongsGUI addAllSongsGUI;
    private ChooseAlbumGUI albumGUI;
    private ChooseGenreGUI genreGUI;
    private ChooseArtistGUI artistGUI;
    private SelectQueueGUI selectQueueGUI;
    private DeleteQueueGUI deleteQueueGUI;
    protected static Slider progressSlider;
    protected static Label currentTimeLabel;

    /**
     * @brief Overrides the start() method of the JavaFX Application class to initialize and display the primary stage with the Darstellungsmodus (Display mode) scene.
     *
     * @param primaryStage The primary stage of the JavaFX application.
     * @throws Exception if an error occurs while starting the application.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Darstellungsmodus");
        primaryStage.getIcons().add(new Image("file:src/main/java/frontend/icons/stageicon1.png"));

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

    /**
     * @brief Creates the main display scene of the Darstellungsmodus (Display mode) with various UI components such as menus, buttons, and media controls.
     *
     * @return The Scene object representing the Darstellungsmodus (Display mode) scene.
     */
    public Scene createDisplayScene() {
        // Initialize the media playlist and the main layout (BorderPane)
        mediaPlaylist = new MediaPlaylist();
        border = new BorderPane(); // General layout

        // *** TOP ***
        // Create menus for Wiedergabeliste (Queue) and Playlists
        queue = new Menu("_Wiedergabeliste");
        playlists = new Menu("_Playlists");
        queue.setId("queue");
        playlists.setId("playlists");

        // Create a MenuBar and add menus to it
        menuBar = new MenuBar();
        menuBar.getStyleClass().add("menuBar");
        menuBar.getMenus().addAll(playlists, queue);

        // Add menu items for Playlist and Queue options
        addAllSongsMenuItem = new MenuItem("Alle Songs");
        genreMenuItem = new MenuItem("Genres");
        albumsMenuItem = new MenuItem("Alben");
        artistMenuItem = new MenuItem("Interpreten");
        addAllSongsMenuItem.setOnAction(e -> addAllSongsGUI.addAllSongs());
        genreMenuItem.setOnAction(e -> genreGUI.selectGenre());
        albumsMenuItem.setOnAction(e -> albumGUI.chooseAlbum());
        artistMenuItem.setOnAction(e -> artistGUI.selectArtist());
        playlists.getItems().addAll(addAllSongsMenuItem, genreMenuItem, albumsMenuItem, artistMenuItem);

        selectQueueMenuItem = new MenuItem("Song wählen/entfernen");
        deleteQueueMenuItem = new MenuItem("Alle Songs entfernen");
        selectQueueMenuItem.setOnAction(e -> selectQueueGUI.selectQueue());
        deleteQueueMenuItem.setOnAction(e -> deleteQueueGUI.deleteQueue());
        queue.getItems().addAll(selectQueueMenuItem, deleteQueueMenuItem);

        // Create mode switch button with an icon
        Image image = new Image("file:src/main/java/frontend/icons/mode2.PNG");
        ImageView img = new ImageView(image);
        swap = new Button("Music");
        img.setFitWidth(swap.getWidth());
        img.setFitHeight(swap.getHeight());
        swap.setGraphic(img);
        swap.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        swap.getStyleClass().add("swap");
        swap.setOnAction(e -> {
            // Call method to switch to Archivemode
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
        // Create UI components for displaying song title and cover art image
        Label songTitle = new Label("");
        songTitle.getStyleClass().add("songtitle");

        Image image1 = new Image("file:src/main/java/frontend/icons/planetS.gif");
        ImageView imageView = new ImageView(image1);
        imageView.setFitHeight(400);
        imageView.setFitWidth(400);
        //border.setCenter(imageView);

        // Create a Slider for media playback progress
        progressSlider = new Slider();
        progressSlider.setMaxWidth(750);
        currentTimeLabel = new Label("0:00");

        // Add listener to the Slider to update the current time label and adjust song playback
        progressSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mediaPlaylist.seekTo(newValue.doubleValue()); // Update the song playback to the new value
            MediaPlaylist.counter = newValue.doubleValue();
            currentTimeLabel.setText(formatTime(newValue.doubleValue()) + " /" + mediaPlaylist.getTotalDuration()); // Update the label with the current time
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
        // Create buttons with icons for Play, Next, and Previous actions
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

        // Set button sizes
        playButton.setPrefSize(80, 80);
        nextButton.setPrefSize(80, 80);
        previousButton.setPrefSize(80, 80);

        // Create HBox layout for buttons
        buttonsBox = new HBox(70);
        buttonsBox.getChildren().addAll(previousButton, playButton, nextButton);

        // Create main layout and add buttons to the bottom of the border pane
        root = new StackPane();
        HBox centerBox = new HBox(buttonsBox);
        centerBox.setAlignment(Pos.CENTER);
        root.getChildren().addAll(centerBox);

        border.setBottom(root);

        // Create and configure the scene with the border pane layout
        Scene scene = new Scene(border, 1100, 900);
        scene.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());
        scene.getStylesheets().add((new File("src/main/java/frontend/DarstellungGUI.css")).toURI().toString());

        return scene;
    }

    /**
     * @brief Writes the current instance of Database to the file "songObjects.ser".
     */
    public void writeObjectToFile() {
        try (FileOutputStream outputFile = new FileOutputStream("songObjects.ser", true);
             ObjectOutputStream outputObject = new ObjectOutputStream(new BufferedOutputStream(outputFile))) {
            outputObject.writeObject(this.data);
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }
    }

    /**
     * @brief Removes the old "songObjects.ser" file, writes the current instance of Database to a new file, and exits the application.
     */
    public void saveAndExit() {
        File oldSer = new File("songObjects.ser");
        oldSer.delete();
        writeObjectToFile();
        System.exit(0);
    }

    /**
     * @brief Utility method to create a Button with an optional icon.
     *
     * @param name The name or text of the button.
     * @param iconPath The file path to the icon image.
     * @return The created Button object with an optional icon.
     */
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

    /**
     * @brief Utility method to format time in seconds to "mm:ss" format.
     *
     * @param seconds The time duration in seconds.
     * @return The formatted time string in the "mm:ss" format.
     */
    private String formatTime(double seconds) {
        int minutes = (int) seconds / 60;
        int remainingSeconds = (int) seconds % 60;
        return String.format("%2d:%02d", minutes, remainingSeconds);
    }

    /**
     * @brief Switches the display mode to Verwaltungsmodus (Management mode).
     *        Stops the media playback, creates a new Archivemode instance, sets the Database, creates the Archivemode scene,
     *        sets the scene to the primary stage, and shows the primary stage with the new scene.
     */
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

    public static void setPlayButton() {
        Image playIconImage = new Image("file:src/main/java/frontend/icons/playglow.PNG");
        ImageView playIconView = new ImageView(playIconImage);
        playIconView.setFitWidth(80);
        playIconView.setFitHeight(80);

        playButton.setGraphic(playIconView);
    }

    public static void setPauseButton() {
        Image pauseIconImage = new Image("file:src/main/java/frontend/icons/pauseglow.png");
        ImageView pauseIconView = new ImageView(pauseIconImage);
        pauseIconView.setFitWidth(80);
        pauseIconView.setFitHeight(80);

        playButton.setGraphic(pauseIconView);
    }

    /**
     * @brief Initiates the JavaFX application by launching the application.
     */
    public static void initiate() {
        launch();
    }

    /**
     * @brief Sets the Database instance for this Displaymode class.
     *
     * @param database The Database instance to be set.
     */
    public void setDatabase(Database database) {
        this.data = database;
    }
}
