package frontend;

import backend.Song;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.List;

public class MediaPlaylist {
    public List <Song> songs;
    public int currentIndex;
    private Duration storedPlaybackPosition;
    private MediaPlayer mediaPlayer;

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public void play() {
        currentIndex = 0;
        playSongAtIndex(currentIndex);
    }

    private void playSongAtIndex(int index) {
        if (index >= 0 && index < songs.size()) {
            Song song = songs.get(index);
            String filePath = song.getMp3Path();
            System.out.println(song.getMp3Path());

            // Erstellen Sie einen neuen MediaPlayer mit der Datei des aktuellen Songs
            mediaPlayer = new MediaPlayer(new Media(new File(filePath).toURI().toString()));

            // Fügen Sie einen EventListener hinzu, um den nächsten Song abzuspielen, wenn der aktuelle Song beendet ist
            mediaPlayer.setOnEndOfMedia(this::playNextSong);

            // Starten Sie die Wiedergabe
            mediaPlayer.play();
        }
    }

    public void playNextSong() {
        mediaPlayer.stop();
        currentIndex++;
        playSongAtIndex(currentIndex);
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
        }
        else {
            this.play();
        }

    }

    public void pause() {
        mediaPlayer.pause();
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
}