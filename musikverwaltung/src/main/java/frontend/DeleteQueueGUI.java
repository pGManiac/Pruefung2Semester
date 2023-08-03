package frontend;
import backend.Database;
import javafx.scene.control.*;

import javax.swing.plaf.IconUIResource;
import java.util.Optional;

/**
 * @brief The DeleteQueueGUI class represents a graphical user interface for deleting all songs from the media playlist (Wiedergabeliste).
 *        It provides a confirmation dialog allowing the user to remove all songs from the current playlist.
 */
public class DeleteQueueGUI {
    private MediaPlaylist mediaPlaylist;

    /**
     * @brief Constructor for the DeleteQueueGUI class.
     *
     * @param mediaPlaylist The MediaPlaylist instance for managing the playlist.
     */
    public DeleteQueueGUI(MediaPlaylist mediaPlaylist) {
        this.mediaPlaylist = mediaPlaylist;
    }

    /**
     * @brief Opens a confirmation dialog to delete all songs from the media playlist.
     *        If the user confirms the action, all songs from the current playlist are removed, and the media player is stopped.
     */
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
            mediaPlaylist.getCurrentSongProperty().set(null);
        }
    }
}
