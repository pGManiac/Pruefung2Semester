package frontend;

import backend.Song;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.List;

public class MediaPlaylist {
    private List <Song> songs;
    private int currentIndex;
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
            mediaPlayer.setOnEndOfMedia(() -> playNextSong());

            // Starten Sie die Wiedergabe
            mediaPlayer.play();
        }
    }

    private void playNextSong() {
        currentIndex++;
        playSongAtIndex(currentIndex);
    }
}