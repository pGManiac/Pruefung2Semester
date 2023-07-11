package backend;

import java.io.Serializable;
import java.util.*;

/**
 * @brief The ArtistHash class represents a hash table that stores songs based on their artists.
 *        It provides methods for adding, removing, and retrieving songs based on their artist.
 * @see Song
 */
public class ArtistHash implements Serializable {
    private Map<String, List<Song>> artistMap;

    /**
     * @brief Constructs a new ArtistHash object.
     *        Initializes the artistMap as a HashMap.
     */
    public ArtistHash() {
        artistMap = new HashMap<>();
    }

    /**
     * @brief Adds a song to the hash table. The used key is the artist name.
     * @see Song
     * @param song The song to be added.
     */
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

    /**
     * @brief Removes a specific song from the hash table.
     * @see Song
     * @param song The song to be removed.
     */
    public void removeSong(Song song) {
        String key = song.getArtist();
        List<Song> songs = artistMap.get(key);

        if (songs != null) {
            Iterator<Song> iterator = songs.iterator();
            while (iterator.hasNext()) {
                Song comparedSong= iterator.next();
                if (song.equals(comparedSong)) {
                    iterator.remove();
                    // If the list is empty after removing the song, remove the key from the map
                    if (songs.isEmpty()) {
                        artistMap.remove(key);
                    }
                    return;
                }
            }
        }
    }

    /**
     * @brief Retrieves a song from the hash table based on its name, album, genre, and artist (key).
     * @param songName    The name of the song.
     * @param albumName   The name of the album.
     * @param genre       The genre of the song.
     * @param artistName  The name of the artist.
     * @return The song object if found, otherwise null.
     */
    public Song getSong(String songName, String albumName, int genre, String artistName) {
        List<Song> songs = artistMap.get(artistName);
        if (songs != null) {
            for (Song song : songs) {
                if (Objects.equals(song.getName(), songName) && Objects.equals(song.getAlbum(), albumName) &&
                        (genre == song.getGenreNumber()) && Objects.equals(song.getArtist(), artistName)) {
                    return song;
                }
            }
        }
        return null;
    }

    /**
     * @brief Checks if the hash table contains a song based on its name, album, genre, and artist (key).
     * @param songName    The name of the song.
     * @param albumName   The name of the album.
     * @param genre       The genre of the song.
     * @param artistName  The name of the artist.
     * @return true if the song is present, otherwise false.
     */
    public boolean containsSong(String songName, String albumName, int genre, String artistName) {
        List<Song> songs = artistMap.get(artistName);
        if (songs != null) {
            for (Song song : songs) {
                if (Objects.equals(song.getName(), songName) && Objects.equals(song.getAlbum(), albumName) &&
                        (genre == song.getGenreNumber()) && Objects.equals(song.getArtist(), artistName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @brief Retrieves a list of songs from the hash table based on the artist name.
     * @param artist The artist of the songs.
     * @return A list of songs from the specified artist name if present, else null.
     */
    public List<Song> getSongsFromArtist(String artist) {
        return artistMap.getOrDefault(artist, null);
    }

    /**
     * @brief Checks if the hash table contains any songs with a specific artist name.
     * @param artist The artist name to check.
     * @return true if the artist name is present, otherwise false.
     */
    public boolean containsArtist(String artist) {
        return artistMap.containsKey(artist);
    }

    /**
     * @brief Returns the number of Lists of songs saved in the hash table
     * @return Number of List<Song> saved in hash table
     */
    public int size() {
        return artistMap.size();
    }
}