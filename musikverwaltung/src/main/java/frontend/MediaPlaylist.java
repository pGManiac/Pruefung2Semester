package frontend;

import backend.Song;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.List;

/**
 * @brief The MediaPlaylist class represents a playlist of songs that can be played and managed by the music player application.
 *        It contains methods to play, pause, and switch between songs, as well as manage the playlist queue.
 */
public class MediaPlaylist {
    public List <Song> songs;
    private int currentIndex;
    private Duration storedPlaybackPosition;
    private MediaPlayer mediaPlayer;
    private ObjectProperty<Song> currentSongProperty = new SimpleObjectProperty<>();
    protected static double counter;
    private Timeline timeline;

    /**
     * @brief Plays the first song in the playlist from the beginning.
     */
    public void playFromStart() {
        currentIndex = 0;
        playSongAtIndex(currentIndex);
    }

    /**
     * @brief Plays the song at the specified index in the playlist.
     *        Updates progressSlider and currentTimeLabel.
     *
     * @param index The index of the song to be played.
     */
    public void playSongAtIndex(int index) {
        if (index >= 0 && index < songs.size()) {
            this.setCurrentIndex(index);
            Song song = songs.get(index);
            String filePath = song.getMp3Path();
            System.out.println(song.getMp3Path());

            //Resets slider
            Displaymode.progressSlider.setValue(0);
            Displaymode.currentTimeLabel.setText("0:00");

            // Creates new mediaPlayer with the file from the current Song
            mediaPlayer = new MediaPlayer(new Media(new File(filePath).toURI().toString()));
            // Adds an event listener to play the next song once the current one is finished
            mediaPlayer.setOnEndOfMedia(this::playNextSong);

            mediaPlayer.setOnReady(() -> {
                // Get the total duration of the song in seconds
                double totalDurationInSeconds = mediaPlayer.getTotalDuration().toSeconds();
                // Update the maximum value of the slider
                Displaymode.progressSlider.setMax(totalDurationInSeconds);
            });

           this.resetCounter();

            // starts the playback
            mediaPlayer.play();
            currentSongProperty.set(song);
            Displaymode.setPauseButton();

            if (currentIndex == songs.size() - 1) {
                // Register the onEndOfMedia event handler for the current song
                mediaPlayer.setOnEndOfMedia(() -> {
                    timeline.stop();
                    Displaymode.setPlayButton();
                    showEndOfQueue();
                });
            }
        }
    }

    public void playNextSong() {
        if (songs == null) {
            // catches null-exception in case the next button is pressed although there is no song playing
        }
        else if (currentIndex == (songs.size() - 1)) {
            this.showEndOfQueue();
        } else {
            mediaPlayer.stop();
            timeline.stop();
            currentIndex++;
            playSongAtIndex(currentIndex);
        }
    }

    public void playPreviousSong() {
        if (songs == null) {
            // catches null - exception in case previous button is pressed
        }
        else if (currentIndex > 0) {
            mediaPlayer.stop();
            timeline.stop();
            currentIndex--;
            playSongAtIndex(currentIndex);
        }
        else if (currentIndex == 0 && mediaPlayer != null) {
            mediaPlayer.stop();
            timeline.stop();
            playSongAtIndex(currentIndex);
        }
    }

    public void resumePlaying() {
        if (storedPlaybackPosition != null) {
            mediaPlayer.seek(storedPlaybackPosition);
            mediaPlayer.play();
            timeline.play();
            Displaymode.setPauseButton();
        } else if (songs == null) {
           this.showNoSongs();
        } else {
            this.playSongAtIndex(currentIndex);
        }
    }

    public void pause() {
        mediaPlayer.pause();
        timeline.pause();
        Displaymode.setPlayButton();
    }

    /**
     * @brief Seeks to the specified time in seconds for the media playback.
     *
     * @param seconds The time in seconds to seek to.
     */
    public void seekTo(double seconds) {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.seek(Duration.seconds(seconds));
        }
    }

    /**
     * @brief Stops the media playback and resets the counter and current time label.
     */
    public void stop() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.stop();
            timeline.stop();
            counter = 0;
            Displaymode.currentTimeLabel.setText("0:00");
            Displaymode.setPlayButton();
            mediaPlayer.setOnEndOfMedia(null);
        }
    }

    /**
     * @brief Removes a song from the playlist and adjusts the currentIndex if necessary.
     *
     * @param song The song to be removed from the playlist.
     */
    public void removeSongFromList(Song song) {
        if (getIndex(song) < currentIndex) {
            currentIndex--;
        }
        songs.remove(song);
    }

    /**
     * @brief Retrieves the ObjectProperty that holds the current playing song in the playlist.
     *
     * @return The ObjectProperty holding the current playing song.
     */
    public ObjectProperty<Song> getCurrentSongProperty() {
        return currentSongProperty;
    }

    public void showNoSongs() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warnung");
        alert.setHeaderText("Die Wiedergabeliste ist leer!\nFüge Songs über 'Playlists' hinzu.");
        alert.showAndWait();
    }

    public void showEndOfQueue() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warnung");
        alert.setHeaderText("Du bist beim letzten letzten Song der Wiedergabeliste");
        alert.showAndWait();
    }

    /**
     * @brief Resets the counter and starts a new timeline to update the current time label.
     */
    private void resetCounter() {
        counter = 0;
        // Create a Timeline with a KeyFrame that triggers every second
        Duration duration = Duration.seconds(1);
        KeyFrame keyFrame = new KeyFrame(duration, event -> {
            // Increment the counter and update the label text
            counter++;
            Displaymode.currentTimeLabel.setText((formatTime(counter) + " /" + getTotalDuration()));
        });

        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE); // Repeat indefinitely
        timeline.play();
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

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setStoredPlaybackPosition(Duration duration) {
        this.storedPlaybackPosition = duration;
    }

    public Duration getStoredPlaybackPosition() {
        return storedPlaybackPosition;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public Duration getCurrentTime() {
        return mediaPlayer.getCurrentTime();
    }

    public int getIndex(Song song) {
        int i = 0;
        while (!songs.get(i).equals(song)) {
            i++;
        }
        return i;
    }

    public Song getCurrentSong() {
        return songs.get(currentIndex);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int index) {
        this.currentIndex = index;
    }

    public String getTotalDuration() {
        return formatTime(mediaPlayer.getTotalDuration().toSeconds());
    }
}