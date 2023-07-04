package backend;

import java.util.*;

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

    public void removeSong(String songName, String albumName, int genre, String artistName) {
        List<Song> songs = genreMap.get(genre);

        if (songs != null) {
            Iterator<Song> iterator = songs.iterator();
            while (iterator.hasNext()) {
                Song song = iterator.next();
                if (Objects.equals(song.getName(), songName) && Objects.equals(song.getAlbum(), albumName) && (genre == song.getGenre()) && Objects.equals(song.getArtist(), artistName)); {
                    iterator.remove();
                    // If the list is empty after removing the song, remove the key from the map
                    if (songs.isEmpty()) {
                        genreMap.remove(genre);
                    }
                    return;
                }
            }
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
