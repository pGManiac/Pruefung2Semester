package BackEndTests;

import backend.Song;
import backend.GenreHash;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GenreHashTest {
    private GenreHash genreHash;

    @BeforeEach
    public void setUp() {
        genreHash = new GenreHash(5);
    }

    @Test
    public void testAddSong() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2", 2, "Artist 3", "0");

        genreHash.addSong(song1);
        genreHash.addSong(song2);
        genreHash.addSong(song3);

        assertTrue(genreHash.containsGenre(1));
        assertTrue(genreHash.containsGenre(2));
        assertFalse(genreHash.containsGenre(3));
    }

    @Test
    public void testAddMultipleSongsToSameGenre() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", 1, "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 3", 1, "Artist 3", "0");

        genreHash.addSong(song1);
        genreHash.addSong(song2);
        genreHash.addSong(song3);

        assertEquals(3, genreHash.getSongsFromGenre(1).size());
    }

    @Test
    public void testRemoveSong() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 2", "0");
        Song song3 = new Song("Song 1", "Album 2", 3, "Artist 3", "0");

        genreHash.addSong(song1);
        genreHash.addSong(song2);
        genreHash.addSong(song3);

        genreHash.removeSong("Song 1", "Album 1", 1, "Artist 1");

        assertFalse(genreHash.containsGenre(1));
        assertTrue(genreHash.containsGenre(2));
        assertTrue(genreHash.containsGenre(3));
    }

    @Test
    public void testRemoveNonExistingSong() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 2", "0");

        genreHash.addSong(song1);

        // Attempt to remove a non-existing song
        genreHash.removeSong("Song 2", "Album 2", 2, "Artist 2");

        // Verify that the genre still contains the existing song
        assertTrue(genreHash.containsGenre(1));
        assertEquals(1, genreHash.getSongsFromGenre(1).size());
    }

    @Test
    public void testGetNonExistingSong() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 2", "0");

        genreHash.addSong(song1);

        // Attempt to get a non-existing song
        Song retrievedSong = genreHash.getSong("Song 2", "Album 2", 2, "Artist 2");

        // Verify that the retrieved song is null
        assertNull(retrievedSong);
    }

    @Test
    public void testGetSongsFromGenre() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2", 2, "Artist 3", "0");
        Song song4 = new Song("Song 4", "Album 3", 4, "Artist 4", "0");

        genreHash.addSong(song1);
        genreHash.addSong(song2);
        genreHash.addSong(song3);
        genreHash.addSong(song4);

        assertEquals(1, genreHash.getSongsFromGenre(1).size());
        assertEquals(2, genreHash.getSongsFromGenre(2).size());
        assertEquals(0, genreHash.getSongsFromGenre(3).size());
        assertEquals(1, genreHash.getSongsFromGenre(4).size());
    }

    @Test
    public void testRemoveAllSongsFromGenre() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 2", "0");

        genreHash.addSong(song1);
        genreHash.addSong(song2);

        // Remove all songs from genre 1
        genreHash.removeSong("Song 1", "Album 1", 1, "Artist 1");

        // Verify that genre 1 is empty
        assertFalse(genreHash.containsGenre(1));
        assertEquals(0, genreHash.getSongsFromGenre(1).size());
    }

    @Test
    public void testContainsGenre() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2", 3, "Artist 3", "0");

        genreHash.addSong(song1);
        genreHash.addSong(song3);

        assertTrue(genreHash.containsGenre(1));
        assertFalse(genreHash.containsGenre(2));

        genreHash.addSong(song2);

        assertTrue(genreHash.containsGenre(1));
        assertTrue(genreHash.containsGenre(2));
    }
}