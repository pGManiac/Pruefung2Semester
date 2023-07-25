package frontend;
import backend.Database;
import backend.Song;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.util.Optional;

public class DeleteQueueGUI {

    private Database data;
    private TableView<Song> albums;
    private TableColumn<Song, String> albumColumn;
    private TableColumn<Song, String> artistColumn;

    private MediaPlaylist mediaPlaylist;
    private Dialog<Song> adder;
    private ObservableList<Song> tableData;

    public DeleteQueueGUI(Database data, MediaPlaylist mediaPlaylist) {
        this.data = data;
        this.mediaPlaylist = mediaPlaylist;
    }

    public void deleteQueue() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Songs aus der Wiedergabeliste entfernen");
        alert.setHeaderText("Alle Songs aus der aktuellen Wiedergabeliste entfernen?");
        alert.setContentText(null);

        // Set the "OK" button and "Cancel" button
        ButtonType yesButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton);

        // Show the alert and wait for user input
        Optional<ButtonType> result = alert.showAndWait();

        // Check if the user clicked the "Yes" button and perform the custom action
        if (result.isPresent() && result.get() == yesButton) {
            // removing alls songs from the queue
            mediaPlaylist.stop();
            mediaPlaylist.setStoredPlaybackPosition(null);
            mediaPlaylist.setSongs(null);
        }
    }
}
