package BackEndTests;

import backend.Database;
import backend.Song;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.File;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    private static final String TEST_FILENAME = "test_database.ser";
    private Database originalDatabase;
    private Database deserializedDatabase;

    @BeforeEach
    public void setUp() {
        // Create a sample database with some data for testing
        originalDatabase = new Database();
        originalDatabase.addSong(new Song("Song 1", "Album 1", 1, "Artist 1", "path1"));
        originalDatabase.addSong(new Song("Song 2", "Album 2", 2, "Artist 2", "path2"));

        // Serialize the database
        Database.serializeDatabase(originalDatabase, TEST_FILENAME);

        // Deserialize the database
        deserializedDatabase = Database.deserializeDatabase(TEST_FILENAME);
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
    public void testSerializationDeserialization() {
        assertNotNull(deserializedDatabase, "Deserialized database should not be null");

        // Compare original and deserialized databases
        assertEquals(originalDatabase.getSongHash().size(), deserializedDatabase.getSongHash().size());
        assertEquals(originalDatabase.getAlbumHash().size(), deserializedDatabase.getAlbumHash().size());
        assertEquals(originalDatabase.getGenreHash().size(), deserializedDatabase.getGenreHash().size());
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
        // Attempt to deserialize a non-existing file
        Database nonExistingDatabase = Database.deserializeDatabase("non_existing_file.ser");

        assertNull(nonExistingDatabase);
    }
}