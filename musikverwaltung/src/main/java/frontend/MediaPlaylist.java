package frontend;

import backend.Song;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.List;

public class MediaPlaylist {
    public List <Song> songs;
    private int currentIndex;
    private Duration storedPlaybackPosition;
    private MediaPlayer mediaPlayer;

    private ObjectProperty<Song> currentSongProperty = new SimpleObjectProperty<>();


    public void playFromStart() {
        currentIndex = 0;
        playSongAtIndex(currentIndex);
    }

    public void playSongAtIndex(int index) {
        if (index >= 0 && index < songs.size()) {
            this.setCurrentIndex(index);
            Song song = songs.get(index);
            String filePath = song.getMp3Path();
            System.out.println(song.getMp3Path());

            //Reset slider
            Displaymode.progressSlider.setValue(0);
            Displaymode.currentTimeLabel.setText("00:00");

            // creates new mediaPlayer with the file from the current Song
            mediaPlayer = new MediaPlayer(new Media(new File(filePath).toURI().toString()));
            // adds an event listener to play the next song once the current one is finished
            mediaPlayer.setOnEndOfMedia(this::playNextSong);

            mediaPlayer.setOnReady(() -> {
                // Get the total duration of the song in seconds
                double totalDurationInSeconds = mediaPlayer.getTotalDuration().toSeconds();
                // Update the maximum value of the slider
                Displaymode.progressSlider.setMax(totalDurationInSeconds);
            });


            // starts the playback
            mediaPlayer.play();
            currentSongProperty.set(song);
        }
    }

    public void playNextSong() {
        if (songs == null) {

        }
        else if (currentIndex == (songs.size() - 1)) {
            this.showEndOfQueue();
        } else {
            mediaPlayer.stop();
            currentIndex++;
            playSongAtIndex(currentIndex);
        }
    }

    public void playPreviousSong() {
        if (songs == null) {

        }
        else if (currentIndex > 0) {
            mediaPlayer.stop();
            currentIndex--;
            playSongAtIndex(currentIndex);
        }
        else if (currentIndex == 0 && mediaPlayer != null) {
            mediaPlayer.stop();
            playSongAtIndex(currentIndex);
        }
    }

    public void resumePlaying() {
        if (storedPlaybackPosition != null) {
            mediaPlayer.seek(storedPlaybackPosition);
            mediaPlayer.play();
        } else if (songs == null) {
           this.showNoSongs();
        } else {
            this.playSongAtIndex(currentIndex);
        }
    }

    // seeks time for slider
    public void seekTo(double seconds) {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.seek(Duration.seconds(seconds));
        }
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.stop();
        }
    }

    public void removeSongFromList(Song song) {
        if (getIndex(song) < currentIndex) {
            currentIndex--;
        }
        songs.remove(song);
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

    // Getter for the currentSongProperty
    public ObjectProperty<Song> getCurrentSongProperty() {
        return currentSongProperty;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
    public void setCurrentIndex(int index) {
        this.currentIndex = index;
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
}