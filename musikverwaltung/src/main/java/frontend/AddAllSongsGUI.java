package frontend;

import backend.Database;
import backend.Song;
import javafx.scene.control.*;

import java.util.List;
import java.util.Optional;

/**
 * @brief The AddAllSongsGUI class represents a graphical user interface for adding all available songs to the media playlist.
 *        It provides a confirmation dialog allowing the user to add all songs from the database to the current playlist.
 */
public class AddAllSongsGUI {
    private Database data;
    private MediaPlaylist mediaPlaylist;

    /**
     * @brief Constructor for the AddAllSongsGUI class.
     *
     * @param data          The Database instance that provides song data.
     * @param mediaPlaylist The MediaPlaylist instance for managing the playlist.
     */
    public AddAllSongsGUI(Database data, MediaPlaylist mediaPlaylist) {
        this.data = data;
        this.mediaPlaylist = mediaPlaylist;
    }

    /**
     * @brief Opens a confirmation dialog to add all available songs to the media playlist.
     *        If the user confirms the action, all songs from the database are added to the playlist, and playback starts from the beginning.
     */
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
