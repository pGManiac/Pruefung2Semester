package frontend;

import backend.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.*;

/**
 * @brief The SelectQueueGUI class represents a GUI for selecting and managing songs in the queue (Wiedergabeliste).
 *        It provides a dialog with a TableView displaying the current playlist, allowing the user to choose a song to play or remove from the playlist.
 */
public class SelectQueueGUI {
    // Properties
    private TableView<Song> tableView;
    private TableColumn<Song, String> titleColumn, albumColumn, genreColumn, artistColumn;
    private MediaPlaylist mediaPlaylist;
    private Dialog<Song> adder;
    private ObservableList<Song> tableData;
    private HBox buttonsContainer;


    /**
     * @brief Constructor for the SelectQueueGUI class.
     *
     * @param mediaPlaylist The MediaPlaylist instance for managing the playlist.
     */
    public SelectQueueGUI(MediaPlaylist mediaPlaylist) {
        this.mediaPlaylist = mediaPlaylist;
    }

    /**
     * @brief Opens a dialog to select and manage songs in the media playlist.
     *        It displays a TableView with the current playlist and allows the user to choose a song to play or remove from the playlist.
     *        If the playlist is empty, a warning alert is shown to inform the user.
     */
    public void selectQueue() {
        // *** GUI ***
        tableView = new TableView<>();
        if (mediaPlaylist.getSongs() != null) {
            tableData = FXCollections.observableList(mediaPlaylist.getSongs());

            adder = new Dialog<>();
            adder.setTitle("Aktuelle Wiedergabeliste");
            Label header = new Label("Wähle einen Song, den du abspielen oder entfernen möchtest!");
            header.setId("selectCueHeader");
            adder.setResizable(true);

            titleColumn = new TableColumn<>("Titel");
            albumColumn = new TableColumn<>("Album");
            genreColumn = new TableColumn<>("Genre");
            artistColumn = new TableColumn<>("Interpret");
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
            genreColumn.setCellValueFactory(new PropertyValueFactory<>("genreName"));
            artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
            tableView.getColumns().add(titleColumn);
            tableView.getColumns().add(albumColumn);
            tableView.getColumns().add(genreColumn);
            tableView.getColumns().add(artistColumn);
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tableView.setItems(tableData);

            DialogPane diaPane = adder.getDialogPane();
            diaPane.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());
            diaPane.getStyleClass().add("dialog");

            Button chooseSongButton = new Button("Song auswählen");
            Button removeSongButton = new Button("Song entfernen");
            ButtonType okButtonType = new ButtonType("Fertig", ButtonBar.ButtonData.OK_DONE);
            BorderPane diaBorder = new BorderPane();

            StackPane stack = new StackPane(tableView);
            diaBorder.setCenter(stack);
            diaBorder.setTop(header);
            // Create an HBox to hold the two buttons
            buttonsContainer = new HBox(10); // Set the spacing between buttons to 10 (you can adjust this as needed)

            // Add the buttons to the HBox
            buttonsContainer.getChildren().addAll(chooseSongButton, removeSongButton);

            diaBorder.setBottom(buttonsContainer);

            // plays chosen song according to the index in the queue
            chooseSongButton.setOnAction(e -> {
                Song selectedSong = tableView.getSelectionModel().getSelectedItem();
                if (selectedSong != null) {
                    mediaPlaylist.stop();
                    mediaPlaylist.setStoredPlaybackPosition(null);
                    int index = mediaPlaylist.getIndex(selectedSong);

                    mediaPlaylist.playSongAtIndex(index);
                }
            });

            // removes song from the queue
            removeSongButton.setOnAction(e -> {
               Song selectedSong = tableView.getSelectionModel().getSelectedItem();
               if (mediaPlaylist.getIndex(selectedSong) == mediaPlaylist.getCurrentIndex()) {
                   Alert alert = new Alert(Alert.AlertType.WARNING);
                   alert.setTitle("Warnung");
                   alert.setHeaderText("Du kannst nicht den Song entfernen, der grade abgespielt wird!");
                   alert.showAndWait();
               } else {
                   mediaPlaylist.removeSongFromList(selectedSong);
                   tableView.refresh();
               }
            });

            adder.getDialogPane().setContent(diaBorder);
            adder.getDialogPane().getButtonTypes().add(okButtonType);
            adder.getDialogPane().setPrefSize(1100, 800);
            adder.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warnung");
            alert.setHeaderText("Die Wiedergabeliste ist leer!\nFüge Songs über 'Playlists' hinzu.");
            alert.showAndWait();
        }
    }
}
