package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumHash {
    private Map<String, Song> albumMap;

    public AlbumHash() {
        albumMap = new HashMap<>();
    }

    public void addSong(Song song) {
        String key = song.getAlbum();
        albumMap.put(key, song);
    }

    public List<Song> getSongsFromAlbum(String album) {
        List<Song> songsFromAlbum = new ArrayList<>();

        for(Song song : albumMap.values()) {
            if(song.getAlbum().equals(album)) {
                songsFromAlbum.add(song);
            }
        }
        return songsFromAlbum;
    }

    public boolean containsAlbum(String album) {
        return albumMap.containsKey(album);
    }

    public int size() {
        return albumMap.size();
    }
}
