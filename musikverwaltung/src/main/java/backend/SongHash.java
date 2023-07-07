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
                if (Objects.equals(song.getName(), songName) && Objects.equals(song.getAlbum(), albumName) && (genre == song.getGenreNumber()) && Objects.equals(song.getArtist(), artistName)) {
                    return song;
                }
            }
        }
        return null;
    }

    public void removeSong(Song song) {
        String key = song.getName();
        List<Song> songs = songMap.get(key);

        if (songs != null) {
            Iterator<Song> iterator = songs.iterator();
            while (iterator.hasNext()) {
                Song comparedSong = iterator.next();
                if (song.equals(comparedSong)) {
                    iterator.remove();
                    // If the list is empty after removing the song, remove the key from the map
                    if (songs.isEmpty()) {
                        songMap.remove(key);
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
                if (Objects.equals(song.getName(), songName) && Objects.equals(song.getAlbum(), albumName) && (genre == song.getGenreNumber()) && Objects.equals(song.getArtist(), artistName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Song> getAllSongs() {
        List<Song> allSongs = new ArrayList<>();
        for (List<Song> songs : songMap.values()) {
            allSongs.addAll(songs);
        }
        return allSongs;
    }

    public boolean isEmpty() {
        return songMap.isEmpty();
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
                if (song.getGenreNumber() == genre) {
                    songsByGenre.add(song);
                }
            }
        }
        songsByGenre.sort(Comparator.comparing(Song::getName).thenComparing(Song::getArtist)); //Mix of Mergesort and Insertionsort

        return songsByGenre;
    }
}