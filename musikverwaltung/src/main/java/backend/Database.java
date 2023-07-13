package backend;

import javax.xml.crypto.Data;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;

/**
 * @brief The Database class represents a singleton database that stores songs, albums, genres, and artists.
 *        It provides methods for adding, removing, and retrieving songs from the database.
 *        The class also supports and implements serialization and deserialization of the database.
 * @see Song
 * @see SongHash
 * @see AlbumHash
 * @see GenreHash
 * @see ArtistHash
 */
public class Database implements Serializable {
    private static Database instance;
    private SongHash songHash;
    private AlbumHash albumHash;
    private GenreHash genreHash;
    private ArtistHash artistHash;

    /**
     * @brief Constructs a new Database with the specified number of genres.
     * This constructor creates a new Database object with the given number of genres. It initializes
     * the internal data structures for storing songs, albums, genres, and artists. The number of genres
     * is used to initialize the GenreHash, which internally manages songs based on their genre.
     * @implNote The constructor initializes the SongHash, AlbumHash, GenreHash, and ArtistHash to
     *           store songs, albums, genres, and artists, respectively. The GenreHash is configured
     *           with the specified number of genres to support organizing songs based on their genre.
     *           Make sure to provide a positive number of genres for proper initialization.
     * @see backend.SongHash
     * @see backend.AlbumHash
     * @see backend.GenreHash
     * @see backend.ArtistHash
     */
    public Database() {
        songHash = new SongHash();
        albumHash = new AlbumHash();
        genreHash = new GenreHash();
        artistHash = new ArtistHash();
    }

    /**
     * @brief Retrieves the singleton instance of the Database.
     * @return The singleton instance of the Database.
     */
    public static Database getInstance() {
        if(instance == null) {
            synchronized (Database.class) {
                if(instance == null) {
                    instance = new Database();
                }
            }
        }
        return instance;
    }

    /**
     * @brief Adds a new song to the data structure.
     * @param song The Song object to be added.
     * @throws IllegalArgumentException If the genre value of the song is outside the valid range.
     *                                  The valid range is from 0 to the number of genres (exclusive) in the GenreHash.
     *                                  Any value outside this range will result in an IllegalArgumentException.
     */
    public void addSong(Song song) {
        int genre = song.getGenreNumber();

        if(genre >= 0 && genre < genreHash.numberOfGenres()) {
            songHash.addSong(song);
            albumHash.addSong(song);
            genreHash.addSong(song);
            artistHash.addSong(song);
        } else {
            Logger logger = Logger.getLogger(GenreHash.class.getName());
            logger.log(Level.WARNING, "Invalid genre value while adding Song: " + genre);
        }
    }

    /**
     * @brief Removes a song from the database based on its attributes.
     * @param song Song Object
     * @throws IllegalArgumentException if the provided genre value is outside the valid range.
     *                                  The valid range is determined by the number of genres in the genre hash.
     *                                  The error is logged using a Logger with a warning level.
     */
    public void removeSong(Song song) {
        int genre = song.getGenreNumber();

        if (genre >= 0 && genre < genreHash.numberOfGenres()) {
            songHash.removeSong(song);
            albumHash.removeSong(song);
            genreHash.removeSong(song);
            artistHash.removeSong(song);
        } else {
            Logger logger = Logger.getLogger(GenreHash.class.getName());
            logger.log(Level.WARNING, "Invalid genre value while removing Song: " + genre);
        }
    }

    /**
     * @brief Checks if the database contains a song with the specified attributes.
     * @param songName   The name of the song to check.
     * @param albumName  The name of the album to check.
     * @param genre      The genre of the song to check.
     * @param artistName The name of the artist to check.
     * @return `true` if a song with the specified attributes is found in the database, `false` otherwise.
     */
    public boolean containsSong(String songName, String albumName, int genre, String artistName) {
        return (songHash.containsSong(songName, albumName, genre, artistName) &&
                albumHash.containsSong(songName, albumName, genre, artistName) &&
                genreHash.containsSong(songName, albumName, genre, artistName) &&
                artistHash.containsSong(songName, albumName, genre, artistName));
    }

    /**
     * @brief Serializes the provided Database object to a file with the given filename.
     * @param database The Database object to be serialized.
     * @param filename The name of the file where the serialized data will be stored.
     * @throws IOException If an I/O error occurs while writing the data to the file.
     *
     * @implNote This method uses Java's built-in serialization mechanism to convert the Database object
     *           into a byte stream and save it to the specified file. It is important to note that the
     *           objects stored in the Database class must be serializable for this process to succeed.
     * @see java.io.Serializable
     */
    public static void serializeDatabase(Database database, String filename) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            outputStream.writeObject(database);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Deserializes a Database object from the specified file.
     *        Reads the serialized data from the given file and constructs a Database object
     *        from it, which is then returned.
     * @param filename The name of the file from which to read the serialized data.
     * @return The deserialized Database object, or null if an error occurs during deserialization.
     * @implNote This method uses Java's built-in deserialization mechanism to read the serialized
     *           byte stream from the file and reconstruct the Database object. It is important to
     *           ensure that the file contains valid serialized data for the Database class, and
     *           that the class structure has not changed since the data was serialized.
     * @note If the specified file is not found (FileNotFoundException), this method returns null.
     *       Other exceptions during deserialization (IOException, ClassNotFoundException) will
     *       cause the method to return null as well. If desired, you can handle these exceptions
     *       differently based on your application's requirements.
     * @see java.io.Serializable
     */
    public static Database deserializeDatabase(String filename) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            Database database = (Database) inputStream.readObject();
            return database;
        } catch (FileNotFoundException e) {
            // Log the exception or perform any other desired action.
            // Whether you log it or not depends on your application's logging strategy.
            // If you choose not to log it, you can simply ignore it here.
            return null; // File not found, return null.
        } catch (IOException | ClassNotFoundException e) {
            // Handle other exceptions, log them, or perform appropriate actions.
            e.printStackTrace(); // This will log the exception with stack trace.
            return null;
        }
    }

    public SongHash getSongHash() {
        return songHash;
    }

    public AlbumHash getAlbumHash() {
        return albumHash;
    }

    public GenreHash getGenreHash() {
        return genreHash;
    }

    public ArtistHash getArtistHash() {
        return artistHash;
    }
}