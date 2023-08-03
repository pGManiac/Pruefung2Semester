package frontend;
import backend.Database;
import backend.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.*;
import java.util.List;

/**
 * @brief The ChooseArtistGUI class represents a graphical user interface for selecting an artist and adding their songs to the media playlist.
 *        It provides a dialog with a TableView displaying a list of artists, allowing the user to select an artist and add their songs to the playlist.
 */
public class ChooseArtistGUI {
    private Database data;
    private TableView<Song> tableView;
    private TableColumn<Song, String> artistColumn;

    private MediaPlaylist mediaPlaylist;
    private Dialog<Song> adder;
    private ObservableList<Song> tableData;

    /**
     * @brief Constructor for the ChooseArtistGUI class.
     *
     * @param data          The Database instance that provides artist and song data.
     * @param mediaPlaylist The MediaPlaylist instance for managing the playlist.
     */
    public ChooseArtistGUI(Database data, MediaPlaylist mediaPlaylist) {
        this.data = data;
        this.mediaPlaylist = mediaPlaylist;
    }

    /**
     * @brief Opens a dialog to select an artist and add their songs to the media playlist.
     *        It displays a TableView with a list of artists and allows the user to add all songs of the selected artist to the playlist.
     *        An information alert is shown once the songs are added to the playlist.
     */
    public void selectArtist() {
        // *** GUI ***
        tableView = new TableView<>();
        tableData = FXCollections.observableList(data.getAlbumHash().getOneSongPerAlbum());

        adder = new Dialog<>();
        adder.setTitle("Interpretenwahl");
        Label header = new Label("Wähle einen Interpreten!");
        header.setId("albumSelectionHeader");
        adder.setResizable(true);

        artistColumn = new TableColumn<>("Artist");
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        tableView.getColumns().add(artistColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setItems(tableData);

        DialogPane diaPane = adder.getDialogPane();
        diaPane.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());
        diaPane.getStyleClass().add("dialog");

        Button chooseArtistButton = new Button("Zur Wiedergabeliste hinzufügen");
        ButtonType okButtonType = new ButtonType("Fertig", ButtonBar.ButtonData.OK_DONE);
        BorderPane diaBorder = new BorderPane();

        StackPane stack = new StackPane(tableView);
        diaBorder.setCenter(stack);
        diaBorder.setTop(header);
        diaBorder.setBottom(chooseArtistButton);

        chooseArtistButton.setOnAction(e -> {
            Song selectedArtist = tableView.getSelectionModel().getSelectedItem();
            String artist = selectedArtist.getArtist();
            if (selectedArtist != null) {
                List<Song> selectedSongs = data.getArtistHash().getSongsFromArtist(artist);
                mediaPlaylist.stop();
                mediaPlaylist.setSongs(selectedSongs);
                mediaPlaylist.setStoredPlaybackPosition(null);
                // Create and show an information alert to display the notification
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Interpret hinzugefügt");
                alert.setHeaderText(null);
                alert.setContentText("Alle Songs des Interpreten '" + artist + "' wurden der Playlist hinzugefügt!");
                alert.showAndWait();
            }
        });

        adder.getDialogPane().setContent(diaBorder);
        adder.getDialogPane().getButtonTypes().add(okButtonType);
        adder.getDialogPane().setPrefSize(900, 800);
        adder.showAndWait();
    }
}

