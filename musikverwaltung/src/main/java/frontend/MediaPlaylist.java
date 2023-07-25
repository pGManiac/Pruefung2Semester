package frontend;

import backend.Song;
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

            // creates new mediaPlayer with the file from the current Song
            mediaPlayer = new MediaPlayer(new Media(new File(filePath).toURI().toString()));

            // adds an event listener to play the next song once the current one is finished
            mediaPlayer.setOnEndOfMedia(this::playNextSong);

            // starts the playback
            mediaPlayer.play();
        }
    }

    public void playNextSong() {
        if (currentIndex == (songs.size() - 1)) {
            this.showEndOfQueue();
        } else {
            mediaPlayer.stop();
            currentIndex++;
            playSongAtIndex(currentIndex);
        }
    }

    public void playPreviousSong() {
        if (currentIndex > 0) {
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

    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.stop();
        }
    }

    public void removeSongFromList(Song song) {
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