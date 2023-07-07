package backend;

import java.io.Serializable;
import java.util.*;

public class GenreHash implements Serializable {
    private SongHash[] songHashArray;


    public GenreHash() {
        final int numberOfGenres = 7;
        songHashArray = new SongHash[numberOfGenres];
        for(int i = 0; i < numberOfGenres; i++) {
            songHashArray[i] = new SongHash();
        }
    }

    public void addSong(Song song) {
        songHashArray[song.getGenreNumber()].addSong(song);
    }

    public void removeSong(Song song) {
        songHashArray[song.getGenreNumber()].removeSong(song);
    }

    public Song getSong(String songName, String albumName, int genre, String artistName) {
        return songHashArray[genre].getSong(songName, albumName, genre, artistName);
    }

    public boolean containsSong(String songName, String albumName, int genre, String artistName) {
        if (genre >= 0 && genre < songHashArray.length) {
            return songHashArray[genre].containsSong(songName, albumName, genre, artistName);
        }
        return false;
    }

    public List<Song> getSongsFromGenre(int genre) {
        return songHashArray[genre].getAllSongs();
    }

    public boolean containsGenre(int genre) {
        return !(songHashArray[genre].isEmpty());
    }

    public int nonEmptyGenres() {
        int sum = 0;
        for (int i = 0; i < songHashArray.length; i++) {
            if(!(songHashArray[i].isEmpty())) {
                sum++;
            }
        }
        return sum;
    }

    public int numberOfGenres() {
        return songHashArray.length;
    }
}
