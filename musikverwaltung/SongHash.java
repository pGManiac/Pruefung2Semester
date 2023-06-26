package musikverwaltung;

import java.util.HashMap;
import java.util.Map;

public class SongHash {
    private Map<String, Song> songMap;

    public SongHash() {
        songMap = new HashMap<>();
    }

    public void addSong(Song song) {
        String key = song.getName();
        songMap.put(key, song);
    }

    public void removeSong(String name) {
        songMap.remove(name);
    }

    public Song getSong(String name) {
        return songMap.get(name);
    }

    public boolean containsSong(String name) {
        return songMap.containsKey(name);
    }

    public int size() {
        return songMap.size();
    }
}