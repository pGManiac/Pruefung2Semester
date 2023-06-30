package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenreHash {
    private Map<Integer, Song> genreMap;

    public GenreHash() {
        genreMap = new HashMap<>();
    }

    public void addSong(Song song) {
        int key = song.getGenre();
        genreMap.put(key, song);
    }

    public boolean containsGenre(int genre) {
        return genreMap.containsKey(genre);
    }

    public List<Song> getSongsFromGenre(String genre) {
        List<Song> songsFromGenre = new ArrayList<>();

        for(Song song : genreMap.values()) {
            if(song.getAlbum().equals(genre)) {
                songsFromGenre.add(song);
            }
        }

        return songsFromGenre;
    }

    public int size() {
        return genreMap.size();
    }
}
