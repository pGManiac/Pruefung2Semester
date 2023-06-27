package musikverwaltung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtistHash {
    private Map<String, Song> artistMap;

    public ArtistHash() {
        artistMap = new HashMap<>();
    }

    public void addSong(Song song) {
        String key = song.getArtist();
        artistMap.put(key, song);
    }

    public boolean containsArtist(String name) {
        return artistMap.containsKey(name);
    }

    public List<Song> getSongsFromArtist(String artist) {
        List<Song> songsFromArtist = new ArrayList<>();

        for(Song song : artistMap.values()) {
            if(song.getAlbum().equals(artist)) {
                songsFromArtist.add(song);
            }
        }

        return songsFromArtist;
    }

    public int size() {
        return artistMap.size();
    }
}
