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
import javafx.scene.layout.*;
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
    private Menu queue;
    private Menu playlists;
    private MenuItem addAllSongsMenuItem;
    private MenuItem genreMenuItem;
    private MenuItem albumsMenuItem;
    private MenuItem artistMenuItem;
    private MenuItem selectQueueMenuItem;
    private MenuItem deleteQueueMenuItem;
    private HBox hbox;
    private HBox buttonsBox;
    private Button swap;
    private Button exit;
    private Button playButton;
    private Button nextButton;
    private Button previousButton;
    private MediaPlaylist mediaPlaylist;
    private Dialog<Song> adder;
    private ObservableList<Song> tableData;
    private AddAllSongsGUI addAllSongsGUI;
    private GenreGUI genreGUI;
    private ArtistGUI artistGUI;
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
        genreGUI = new GenreGUI(data, mediaPlaylist);
        artistGUI = new ArtistGUI(data, mediaPlaylist);
        selectQueueGUI = new SelectQueueGUI(data, mediaPlaylist);
        deleteQueueGUI = new DeleteQueueGUI(data, mediaPlaylist);

        primaryStage.setScene(displayScene);
        // primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public Scene createDisplayScene() throws Exception {

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
        albumsMenuItem.setOnAction(e -> chooseAlbum());
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
        currentTimeLabel = new Label("00:00");

        // Add listener to the Slider to update the current time label and adjust song playback
        progressSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Assuming mediaPlaylist is your MediaPlaylist instance
            mediaPlaylist.seekTo(newValue.doubleValue()); // Update the song playback to the new value
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
                // If the media is playing, pause it
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

    public void chooseAlbum() {
        // *** GUI ***
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

        Button chooseAlbumButton = new Button("Zur Wiedergabeliste hinzufügen");
        ButtonType okButtonType = new ButtonType("Fertig", ButtonBar.ButtonData.OK_DONE);
        BorderPane diaBorder = new BorderPane();

        StackPane stack = new StackPane(albums);
        diaBorder.setCenter(stack);
        diaBorder.setTop(header);
        diaBorder.setBottom(chooseAlbumButton);

        chooseAlbumButton.setOnAction(e -> {
            Song selectedAlbum = albums.getSelectionModel().getSelectedItem();
            if (selectedAlbum != null) {
                List<Song> selectedSongs = data.getAlbumHash().getAllSongsFromAlbum(selectedAlbum);
                mediaPlaylist.stop();
                mediaPlaylist.setSongs(selectedSongs);
                mediaPlaylist.setStoredPlaybackPosition(null);
                // Create and show an information alert to display the notification
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Album hinzugefügt");
                alert.setHeaderText(null);
                alert.setContentText("Das Album '" + selectedAlbum.getAlbum() + "' wurde der Playlist hinzugefügt!");
                alert.showAndWait();
            }
        });

        adder.getDialogPane().setContent(diaBorder);
        adder.getDialogPane().getButtonTypes().add(okButtonType);
        adder.getDialogPane().setPrefSize(900, 800);
        adder.showAndWait();
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
    static String formatTime(double seconds) {
        int minutes = (int) seconds / 60;
        int remainingSeconds = (int) seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
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
