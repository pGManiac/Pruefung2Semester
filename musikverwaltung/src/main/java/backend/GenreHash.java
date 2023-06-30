package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenreHash {
    private Map<Integer, List<Song>> genreMap;

    public GenreHash() {
        genreMap = new HashMap<>();
    }

    public void addSong(Song song) {
        int key = song.getGenre();

        // Check if the genre already exists in the map
        if (genreMap.containsKey(key)) {
            List<Song> songsList = genreMap.get(key);
            songsList.add(song);
        } else {
            List<Song> songsList = new ArrayList<>();
            songsList.add(song);
            genreMap.put(key, songsList);
        }
    }

    public List<Song> getSongsFromGenre(int genre) {
        return genreMap.getOrDefault(genre, new ArrayList<>());
    }

    public boolean containsGenre(int genre) {
        return genreMap.containsKey(genre);
    }

    public int size() {
        return genreMap.size();
    }
}
