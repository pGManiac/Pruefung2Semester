package BackEndTests;

import backend.Database;
import backend.Song;
import backend.SongHash;
import backend.AlbumHash;
import backend.GenreHash;
import backend.ArtistHash;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    private static final String TEST_FILENAME = "test_database.ser";
    private Database originalDatabase;
    private Database deserializedDatabase;

    @BeforeEach
    public void setUp() {
        originalDatabase = new Database(15  );
    }

    @AfterEach
    public void tearDown() {
        // Delete the test file after each test
        File testFile = new File(TEST_FILENAME);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testAddSongs() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2", 2, "Artist 3", "0");
        originalDatabase.addSong(song1);
        originalDatabase.addSong(song2);
        originalDatabase.addSong(song3);


        assertTrue(originalDatabase.containsSong("Song 1", "Album 1", 1, "Artist 1"));
        assertTrue(originalDatabase.containsSong("Song 2", "Album 2", 2, "Artist 2"));
        assertTrue(originalDatabase.containsSong("Song 3", "Album 2", 2, "Artist 3"));
        assertFalse(originalDatabase.containsSong("Song 3", "Album 2", 3, "Artist 3"));
        assertFalse(originalDatabase.containsSong("Song 3", "Album 2", 2, "Artist 2"));
        assertFalse(originalDatabase.containsSong("Song 3", "Album 1", 2, "Artist 3"));
        assertFalse(originalDatabase.containsSong("Song 4", "Album 2", 2, "Artist 3"));
    }

    @Test
    public void testRemoveSongs() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2", 2, "Artist 3", "0");

        originalDatabase.addSong(song1);
        originalDatabase.addSong(song2);
        originalDatabase.addSong(song3);

        originalDatabase.removeSong(song1);
        originalDatabase.removeSong(song2);


        assertFalse(originalDatabase.containsSong("Song 1", "Album 1", 1, "Artist 1"));
        assertFalse(originalDatabase.containsSong("Song 2", "Album 2", 2, "Artist 2"));
        assertTrue(originalDatabase.containsSong("Song 3", "Album 2", 2, "Artist 3"));
        assertFalse(originalDatabase.containsSong("Song 3", "Album 2", 3, "Artist 3"));
        assertFalse(originalDatabase.containsSong("Song 3", "Album 2", 2, "Artist 2"));
        assertFalse(originalDatabase.containsSong("Song 3", "Album 1", 2, "Artist 3"));
        assertFalse(originalDatabase.containsSong("Song 4", "Album 2", 2, "Artist 3"));
    }

    @Test
    public void testRemoveNonExistingSong() {

        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2", 2, "Artist 3", "0");

        originalDatabase.addSong(song1);
        originalDatabase.addSong(song2);
        originalDatabase.addSong(song3);

        Song nonExistingSong = new Song("Non-Existing Song", "Album 2", 1, "Artist 3", "0");
        originalDatabase.removeSong(nonExistingSong);

        assertTrue(originalDatabase.containsSong("Song 1", "Album 1", 1, "Artist 1"));
        assertTrue(originalDatabase.containsSong("Song 2", "Album 2", 2, "Artist 2"));
        assertTrue(originalDatabase.containsSong("Song 3", "Album 2", 2, "Artist 3"));
        assertFalse(originalDatabase.containsSong("Non-Existing Song", "Album 2", 1, "Artist 3"));

        int numberOfSongs = originalDatabase.getSongHash().getAllSongs().size();
        assertEquals(3, numberOfSongs);
    }

    @Test
    public void testSerializationDeserialization() {
        originalDatabase.addSong(new Song("Song 1", "Album 1", 1, "Artist 1", "path1"));
        originalDatabase.addSong(new Song("Song 2", "Album 2", 2, "Artist 2", "path2"));

        // Serialize the database
        Database.serializeDatabase(originalDatabase, TEST_FILENAME);

        // Deserialize the database
        deserializedDatabase = Database.deserializeDatabase(TEST_FILENAME);

        assertNotNull(deserializedDatabase, "Deserialized database should not be null");

        // Compare original and deserialized databases sizes
        assertEquals(originalDatabase.getSongHash().size(), deserializedDatabase.getSongHash().size());
        assertEquals(originalDatabase.getAlbumHash().size(), deserializedDatabase.getAlbumHash().size());
        assertEquals(originalDatabase.getGenreHash().nonEmptyGenres(), deserializedDatabase.getGenreHash().nonEmptyGenres());
        assertEquals(originalDatabase.getArtistHash().size(), deserializedDatabase.getArtistHash().size());

        // Compare specific data in the original and deserialized databases
        assertTrue(Objects.equals(originalDatabase.getSongHash().getSong("Song 1", "Album 1", 1, "Artist 1"), deserializedDatabase.getSongHash().getSong("Song 1", "Album 1", 1, "Artist 1")));
        assertTrue(Objects.equals(originalDatabase.getSongHash().getSong("Song 2", "Album 2", 2, "Artist 2"), deserializedDatabase.getSongHash().getSong("Song 2", "Album 2", 2, "Artist 2")));
        assertNull(originalDatabase.getSongHash().getSong("Song 3", "Album 2", 2, "Artist 2"));
        assertNull(deserializedDatabase.getSongHash().getSong("Song 3", "Album 2", 2, "Artist 2"));

        assertTrue(Objects.equals(originalDatabase.getAlbumHash().getSong("Song 1", "Album 1", 1, "Artist 1"), deserializedDatabase.getAlbumHash().getSong("Song 1", "Album 1", 1, "Artist 1")));
        assertTrue(Objects.equals(originalDatabase.getAlbumHash().getSong("Song 2", "Album 2", 2, "Artist 2"), deserializedDatabase.getAlbumHash().getSong("Song 2", "Album 2", 2, "Artist 2")));
        assertNull(originalDatabase.getAlbumHash().getSong("Song 3", "Album 2", 2, "Artist 2"));
        assertNull(deserializedDatabase.getAlbumHash().getSong("Song 3", "Album 2", 2, "Artist 2"));

        assertTrue(Objects.equals(originalDatabase.getGenreHash().getSong("Song 1", "Album 1", 1, "Artist 1"), deserializedDatabase.getGenreHash().getSong("Song 1", "Album 1", 1, "Artist 1")));
        assertTrue(Objects.equals(originalDatabase.getGenreHash().getSong("Song 2", "Album 2", 2, "Artist 2"), deserializedDatabase.getGenreHash().getSong("Song 2", "Album 2", 2, "Artist 2")));
        assertNull(originalDatabase.getGenreHash().getSong("Song 3", "Album 2", 2, "Artist 2"));
        assertNull(deserializedDatabase.getGenreHash().getSong("Song 3", "Album 2", 2, "Artist 2"));

        assertTrue(Objects.equals(originalDatabase.getArtistHash().getSong("Song 1", "Album 1", 1, "Artist 1"), deserializedDatabase.getArtistHash().getSong("Song 1", "Album 1", 1, "Artist 1")));
        assertTrue(Objects.equals(originalDatabase.getArtistHash().getSong("Song 2", "Album 2", 2, "Artist 2"), deserializedDatabase.getArtistHash().getSong("Song 2", "Album 2", 2, "Artist 2")));
        assertNull(originalDatabase.getArtistHash().getSong("Song 3", "Album 2", 2, "Artist 2"));
        assertNull(deserializedDatabase.getArtistHash().getSong("Song 3", "Album 2", 2, "Artist 2"));
    }

    @Test
    public void testSerializationDeserializationWithNonExistingFile() {
        originalDatabase.addSong(new Song("Song 1", "Album 1", 1, "Artist 1", "path1"));
        originalDatabase.addSong(new Song("Song 2", "Album 2", 2, "Artist 2", "path2"));

        // Serialize the database
        Database.serializeDatabase(originalDatabase, TEST_FILENAME);

        // Deserialize the database
        deserializedDatabase = Database.deserializeDatabase(TEST_FILENAME);

        // Attempt to deserialize a non-existing file
        Database nonExistingDatabase = Database.deserializeDatabase("non_existing_file.ser");

        assertNull(nonExistingDatabase);
    }
}