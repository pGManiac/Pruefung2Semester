package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.Objects;

public class AlbumHash {
    private Map<String, List<Song>> albumMap;

    public AlbumHash() {
        albumMap = new HashMap<>();
    }

    public void addSong(Song song) {
        String key = song.getAlbum();

        // Check if the album already exists in the map
        if (albumMap.containsKey(key)) {
            List<Song> songsList = albumMap.get(key);
            songsList.add(song);
        } else {
            List<Song> songsList = new ArrayList<>();
            songsList.add(song);
            albumMap.put(key, songsList);
        }
    }

    public void removeSong(String songName, String album, int genre, String artist) {
        List<Song> songs = albumMap.get(album);

        if (songs != null) {
            Iterator<Song> iterator = songs.iterator();
            while (iterator.hasNext()) {
                Song song = iterator.next();
                if (Objects.equals(song.getName(), songName) && Objects.equals(song.getAlbum(), album) && (genre == song.getGenre()) && Objects.equals(song.getArtist(), artist)) {
                    iterator.remove();
                    // If the list is empty after removing the song, remove the key from the map
                    if (songs.isEmpty()) {
                        albumMap.remove(album);
                    }
                    return;
                }
            }
        }
    }

    public List<Song> getSongsFromAlbum(String album) {
        return albumMap.getOrDefault(album, null);
    }

    public boolean containsAlbum(String album) {
        return albumMap.containsKey(album);
    }

    public int size() {
        return albumMap.size();
    }
}