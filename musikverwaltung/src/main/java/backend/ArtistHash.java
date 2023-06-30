package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtistHash {
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

    public List<Song> getSongsFromArtist(String artist) {
        return artistMap.getOrDefault(artist, new ArrayList<>());
    }

    public boolean containsArtist(String artist) {
        return artistMap.containsKey(artist);
    }

    public int size() {
        return artistMap.size();
    }
}