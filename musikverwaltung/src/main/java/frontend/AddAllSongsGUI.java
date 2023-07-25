package frontend;

import backend.Database;
import backend.Song;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.util.List;
import java.util.Optional;

public class AddAllSongsGUI {
    private Database data;
    private TableView<Song> albums;
    private TableColumn<Song, String> albumColumn;
    private TableColumn<Song, String> artistColumn;

    private MediaPlaylist mediaPlaylist;
    private Dialog<Song> adder;
    private ObservableList<Song> tableData;

    public AddAllSongsGUI(Database data, MediaPlaylist mediaPlaylist) {
        this.data = data;
        this.mediaPlaylist = mediaPlaylist;
    }

    public void addAllSongs() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alle Songs zur Wiedergabeliste hinzufügen?");
        alert.setHeaderText("Möchten Sie alle vorhandenen Songs zur Wiedergabeliste hinzufügen?");
        alert.setContentText(null);

        // Set the "Yes" button and "No" button
        ButtonType yesButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton);

        // Show the alert and wait for user input
        Optional<ButtonType> result = alert.showAndWait();

        // Check if the user clicked the "Yes" button and perform the custom action
        if (result.isPresent() && result.get() == yesButton) {
            // adding all songs to playlist
            mediaPlaylist.stop();
            List<Song> allSongs = data.getSongHash().getAllSongs();
            mediaPlaylist.setSongs(allSongs);
            mediaPlaylist.playFromStart();
        }
    }
}
