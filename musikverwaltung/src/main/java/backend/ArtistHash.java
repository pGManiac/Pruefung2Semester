package backend;

import java.io.Serializable;
import java.util.*;

public class ArtistHash implements Serializable {
    private Map<String, List<Song>> artistMap;

    public ArtistHash() {
        artistMap = new HashMap<>();
    }

    public void addSong(Song song) {
        String key = song.getArtist();

        // Check if the artist already exists in the map
        if (artistMap.containsKey(key)) {
            List<Song> songsList = artistMap.get(key);
            songsList.add(song);
        } else {
            List<Song> songsList = new ArrayList<>();
            songsList.add(song);
            artistMap.put(key, songsList);
        }
    }

    public void removeSong(Song song) {
        String key = song.getArtist();
        List<Song> songs = artistMap.get(key);

        if (songs != null) {
            Iterator<Song> iterator = songs.iterator();
            while (iterator.hasNext()) {
                Song comparedSong= iterator.next();
                if (song.equals(comparedSong)) {
                    iterator.remove();
                    // If the list is empty after removing the song, remove the key from the map
                    if (songs.isEmpty()) {
                        artistMap.remove(key);
                    }
                    return;
                }
            }
        }
    }

    public Song getSong(String songName, String albumName, int genre, String artistName) {
        List<Song> songs = artistMap.get(artistName);
        if (songs != null) {
            for (Song song : songs) {
                if (Objects.equals(song.getName(), songName) && Objects.equals(song.getAlbum(), albumName) &&
                        (genre == song.getGenreNumber()) && Objects.equals(song.getArtist(), artistName)) {
                    return song;
                }
            }
        }
        return null;
    }

    public boolean containsSong(String songName, String albumName, int genre, String artistName) {
        List<Song> songs = artistMap.get(artistName);
        if (songs != null) {
            for (Song song : songs) {
                if (Objects.equals(song.getName(), songName) && Objects.equals(song.getAlbum(), albumName) &&
                        (genre == song.getGenreNumber()) && Objects.equals(song.getArtist(), artistName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Song> getSongsFromArtist(String artist) {
        return artistMap.getOrDefault(artist, null);
    }

    public boolean containsArtist(String artist) {
        return artistMap.containsKey(artist);
    }

    public int size() {
        return artistMap.size();
    }
}