package backend;

import java.io.Serializable;
import java.util.*;

public class SongHash implements Serializable {
    private Map<String, List<Song>> songMap;

    public SongHash() {
        songMap = new HashMap<>();
    }

    public void addSong(Song song) {
        String key = song.getName();

        // Check if the song title already exists in the map
        if (songMap.containsKey(key)) {
            List<Song> songsList = songMap.get(key);
            songsList.add(song);
        } else {
            List<Song> songsList = new ArrayList<>();
            songsList.add(song);
            songMap.put(key, songsList);
        }
    }

    public Song getSong(String songName, String albumName, int genre, String artistName) {
        List<Song> songs = songMap.get(songName);
        if (songs != null) {
            for (Song song : songs) {
                if (Objects.equals(song.getName(), songName) && Objects.equals(song.getAlbum(), albumName) && (genre == song.getGenre()) && Objects.equals(song.getArtist(), artistName)) {
                    return song;
                }
            }
        }
        return null;
    }

    public void removeSong(String songName, String albumName, int genre, String artistName) {
        List<Song> songs = songMap.get(songName);

        if (songs != null) {
            Iterator<Song> iterator = songs.iterator();
            while (iterator.hasNext()) {
                Song song = iterator.next();
                if (Objects.equals(song.getName(), songName) && Objects.equals(song.getAlbum(), albumName) && (genre == song.getGenre()) && Objects.equals(song.getArtist(), artistName)); {
                    iterator.remove();
                    // If the list is empty after removing the song, remove the key from the map
                    if (songs.isEmpty()) {
                        songMap.remove(songName);
                    }
                    return;
                }
            }
        }
    }

    public List<Song> getSongs(String songName) {
        return songMap.getOrDefault(songName, null); //Returns List if key is part of map, else null
    }

    public boolean containsSong(String songName, String albumName, int genre, String artistName) {
        List<Song> songs = songMap.get(songName);
        if (songs != null) {
            for (Song song : songs) {
                if (Objects.equals(song.getName(), songName) && Objects.equals(song.getAlbum(), albumName) && (genre == song.getGenre()) && Objects.equals(song.getArtist(), artistName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int size() {
        return songMap.size();
    }

    public List<Song> sortAToZ() {
        List<Song> sortedSongs = new ArrayList<>();
        for (List<Song> songsList : songMap.values()) {
            sortedSongs.addAll(songsList);
        }

        sortedSongs.sort((song1, song2) -> {
            // First, compare the song names
            int nameComparison = song1.getName().compareToIgnoreCase(song2.getName());

            // If the song names are the same, compare the artist names
            if (nameComparison == 0) {
                return song1.getArtist().compareToIgnoreCase(song2.getArtist());
            }

            return nameComparison;
        });

        return sortedSongs;
    }

    public List<Song> sortZToA() {
        List<Song> sortedSongs = new ArrayList<>();
        for (List<Song> songsList : songMap.values()) {
            sortedSongs.addAll(songsList);
        }

        sortedSongs.sort((song1, song2) -> {
            // First, compare the song names in reverse order
            int nameComparison = song2.getName().compareToIgnoreCase(song1.getName());

            // If the song names are the same, compare the artist names in reverse order
            if (nameComparison == 0) {
                return song2.getArtist().compareToIgnoreCase(song1.getArtist());
            }

            return nameComparison;
        });

        return sortedSongs;
    }

    public List<Song> sortAToZByGenre(int genre) {
        List<Song> songsByGenre = new ArrayList<>();
        for (List<Song> songsList : songMap.values()) {
            for (Song song : songsList) {
                if (song.getGenre() == genre) {
                    songsByGenre.add(song);
                }
            }
        }
        songsByGenre.sort(Comparator.comparing(Song::getName).thenComparing(Song::getArtist)); //Mix of Mergesort and Insertionsort

        return songsByGenre;
    }
}