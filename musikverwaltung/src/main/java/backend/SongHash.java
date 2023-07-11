package backend;

import java.io.Serializable;
import java.util.*;

/**
 * @brief The SongHash class represents a hash table that stores songs based on their names.
 *        It provides methods to add and remove songs, retrieve songs, check the presence of songs,
 *        get all songs, and perform various sorting operations.
 * @see Song
 */
public class SongHash implements Serializable {
    private Map<String, List<Song>> songMap;

    /**
     * @brief Constructs a new SongHash object.
     *        Initializes the songMap as an empty HashMap.
     */
    public SongHash() {
        songMap = new HashMap<>();
    }

    /**
     * @brief Adds a song to the hash table based on its name.
     *        If the song with the same name already exists in the map, it is added to the existing list of songs.
     *        Otherwise, a new list is created for the song and added to the map.
     *
     * @param song The song to be added.
     */
    public void addSong(Song song) {
        String key = song.getName();

        if (songMap.containsKey(key)) {
            List<Song> songsList = songMap.get(key);
            songsList.add(song);
        } else {
            List<Song> songsList = new ArrayList<>();
            songsList.add(song);
            songMap.put(key, songsList);
        }
    }

    /**
     * @brief Retrieves a song from the hash table based on its name, album, genre, and artist.
     * @param songName    The name of the song.
     * @param albumName   The name of the album.
     * @param genre       The genre of the song.
     * @param artistName  The name of the artist.
     *
     * @return The song object if found, otherwise null.
     */
    public Song getSong(String songName, String albumName, int genre, String artistName) {
        List<Song> songs = songMap.get(songName);
        if (songs != null) {
            for (Song song : songs) {
                if (Objects.equals(song.getName(), songName) &&
                        Objects.equals(song.getAlbum(), albumName) &&
                        (genre == song.getGenreNumber()) &&
                        Objects.equals(song.getArtist(), artistName)) {
                    return song;
                }
            }
        }
        return null;
    }

    /**
     * @brief Removes a song from the hash table.
     * @param song The song to be removed.
     */
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

    /**
     * @brief Retrieves a list of songs with the specified name.
     * @param songName The name of the song.
     * @return A list of songs with the specified name if found, otherwise null.
     */
    public List<Song> getSongs(String songName) {
        return songMap.getOrDefault(songName, null); //Returns List if key is part of map, else null
    }

    /**
     * @brief Checks if the hash table contains a song based on its name, album, genre, and artist.
     * @param songName    The name of the song.
     * @param albumName   The name of the album.
     * @param genre       The genre of the song.
     * @param artistName  The name of the artist.
     * @return true if the song is present, otherwise false.
     */
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

    /**
     * @brief Retrieves a list of all songs in the hash table.
     * @return A list of all songs in the hash table.
     */
    public List<Song> getAllSongs() {
        List<Song> allSongs = new ArrayList<>();
        for (List<Song> songs : songMap.values()) {
            allSongs.addAll(songs);
        }
        return allSongs;
    }

    /**
     * @brief Checks if the hash table is empty.
     * @return true if the hash table is empty, otherwise false.
     */
    public boolean isEmpty() {
        return songMap.isEmpty();
    }

    /**
     * @brief Returns the number of songs in the hash table.
     * @return The number of songs in the hash table.
     */
    public int size() {
        return songMap.size();
    }

    /**
     * @brief Sorts the songs in ascending order by name, then by artist.
     * @return A list of songs sorted in ascending order by name, then by artist.
     */
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

    /**
     * @brief Sorts a list of songs in ascending order by name, then by artist.
     * @param songs The list of songs to be sorted.
     * @return A new list of songs sorted in ascending order by name, then by artist.
     */
    public static List<Song> sortListAToZ(List<Song> songs) {
        List<Song> sortedSongs = new ArrayList<>(songs);

        sortedSongs.sort(new Comparator<Song>() {
            @Override
            public int compare(Song song1, Song song2) {
                // First, compare the song names
                int nameComparison = song1.getName().compareToIgnoreCase(song2.getName());

                // If the song names are the same, compare the artist names
                if (nameComparison == 0) {
                    return song1.getArtist().compareToIgnoreCase(song2.getArtist());
                }

                return nameComparison;
            }
        });

        return sortedSongs;
    }

    /**
     * @brief Sorts the songs in descending order by name, then by artist.
     * @return A list of songs sorted in descending order by name, then by artist.
     */
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

    /**
     * @brief Sorts the songs in ascending order by name and artist for a specific genre.
     * @param genre The genre of the songs to be sorted.
     * @return A list of songs sorted in ascending order by name and artist for the specified genre.
     */
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

    /**
     * @brief Searches for songs that contain the specified query in their name.
     * @param query The query to search for.
     * @return A list of songs that match the query in alphabetical order (A-Z)
     */
    public List<Song> searchSongs(String query) {
        List<Song> searchResults = new ArrayList<>();

        for (List<Song> songsList : songMap.values()) {
            for (Song song : songsList) {
                if (song.getName().toLowerCase().contains(query.toLowerCase())) {
                    searchResults.add(song);
                }
            }
        }

        searchResults = sortListAToZ(searchResults);
        return searchResults;
    }
}