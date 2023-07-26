package backend;

import java.io.Serializable;
import java.util.*;

/**
 * @brief The GenreHash class represents a hash table that stores songs based on their genres.
 *        It uses an array to store SongHash maps, with each map dedicated to a specific genre.
 *        The class provides methods for adding, removing, and retrieving songs based on their genre.
 * @see Song
 * @see SongHash
 */
public class GenreHash implements Serializable {
    private SongHash[] songHashArray;

    /**
     * @brief Constructs a new GenreHash object.
     *        Initializes the songHashArray with a fixed number of genres and creates SongHash objects for each genre.
     */
    public GenreHash() {
        final int numberOfGenres = 7;
        songHashArray = new SongHash[numberOfGenres];
        for (int i = 0; i < numberOfGenres; i++) {
            songHashArray[i] = new SongHash();
        }
    }

    /**
     * @brief Adds a song to the SongHash object stored for its genre.
     * @see Song
     * @see SongHash
     * @param song to be added.
     */
    public void addSong(Song song) {
        songHashArray[song.getGenreNumber()].addSong(song);
    }

    /**
     * @brief Removes a song from the SongHash object stored for its genre.
     * @param song The song to be removed.
     */
    public void removeSong(Song song) {
        songHashArray[song.getGenreNumber()].removeSong(song);
    }

    /**
     * @brief Retrieves a song from the hash table based on its name, album, genre, and artist.
     *
     * @param songName    The name of the song.
     * @param albumName   The name of the album.
     * @param genreNumber The genre number of the song.
     * @param artistName  The name of the artist.
     *
     * @return The song object if found, otherwise null.
     */
    public Song getSong(String songName, String albumName, int genreNumber, String artistName) {
        return songHashArray[genreNumber].getSong(songName, albumName, genreNumber, artistName);
    }

    /**
     * @brief Checks if the hash table contains a song based on its name, album, genre, and artist.
     *
     * @param songName    The name of the song.
     * @param albumName   The name of the album.
     * @param genreNumber The genre number of the song.
     * @param artistName  The name of the artist.
     *
     * @return true if the song is present, otherwise false.
     */
    public boolean containsSong(String songName, String albumName, int genreNumber, String artistName) {
        if (genreNumber >= 0 && genreNumber < songHashArray.length) {
            return songHashArray[genreNumber].containsSong(songName, albumName, genreNumber, artistName);
        }
        return false;
    }

    /**
     * @brief Retrieves a list of songs from the hash table based on the genreNumber.
     *
     * @param genreNumber The genreNumber number of the songs.
     *
     * @return A list of songs from the specified genreNumber.
     */
    public List<Song> getSongsFromGenre(int genreNumber) {
        return songHashArray[genreNumber].getAllSongs();
    }

    /**
     * @brief Checks if the hash table contains any songs of a specific genre.
     *
     * @param genreNumber The genre to check.
     *
     * @return true if the genre is present, otherwise false.
     */
    public boolean containsGenre(int genreNumber) {
        return !(songHashArray[genreNumber].isEmpty());
    }

    /**
     * @brief Returns the number of non-empty genres in the hash table.
     * @see SongHash
     * @return The number of non-empty SongHash objects stored in the array.
     */
    public int nonEmptyGenres() {
        int sum = 0;
        for (int i = 0; i < songHashArray.length; i++) {
            if (!(songHashArray[i].isEmpty())) {
                sum++;
            }
        }
        return sum;
    }

    /**
     * @brief Returns an array filled with boolean values indicating whether each genre is empty or not.
     *
     * @return An array of boolean values, where each value represents whether the corresponding genre is empty (false) or not (true).
     */
    public boolean[] genresRepresented() {
        boolean[] genresRepresented = new boolean[numberOfGenres()];
        for (int i = 0; i < numberOfGenres(); i++) {
            genresRepresented[i] = containsGenre(i);
        }
        return genresRepresented;
    }

    /**
     * @brief Returns the total number of genres in the hash table.
     * @see SongHash
     * @return The number of SongHash objects stored in the array.
     */
    public int numberOfGenres() {
        return songHashArray.length;
    }
}
