package backend;

import java.io.*;

public class Database implements Serializable {
    private SongHash songHash;
    private AlbumHash albumHash;
    private GenreHash genreHash;
    private ArtistHash artistHash;

    public Database() {
        // Initialize the data structures when creating the Database object
        songHash = new SongHash();
        albumHash = new AlbumHash();
        genreHash = new GenreHash();
        artistHash = new ArtistHash();
    }

    public void addSong(Song song) {
        songHash.addSong(song);
        albumHash.addSong(song);
        genreHash.addSong(song);
        artistHash.addSong(song);

    }

    // Serialize the Database object to a file
    public static void serializeDatabase(Database database, String filename) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            outputStream.writeObject(database);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Deserialize the Database object from a file
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