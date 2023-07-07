package backendTesting;

import backend.Song;
import backend.GenreHash;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GenreHashTest {
    private GenreHash genreHash;

    @BeforeEach
    public void setUp() {
        genreHash = new GenreHash();
    }

    @Test
    public void testAddSong() {
        Song song1 = new Song("Song 1", "Album 1","Rock", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2","Pop", "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2","Pop", "Artist 3", "0");

        genreHash.addSong(song1);
        genreHash.addSong(song2);
        genreHash.addSong(song3);

        assertTrue(genreHash.containsGenre(0));
        assertTrue(genreHash.containsGenre(1));
        assertFalse(genreHash.containsGenre(2));
    }

    @Test
    public void testAddMultipleSongsToSameGenre() {
        Song song1 = new Song("Song 1", "Album 1","Rock", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2","Rock", "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2","Rock", "Artist 3", "0");

        genreHash.addSong(song1);
        genreHash.addSong(song2);
        genreHash.addSong(song3);

        assertEquals(3, genreHash.getSongsFromGenre(0).size());
    }

    @Test
    public void testRemoveSong() {
        Song song1 = new Song("Song 1", "Album 1","Rock", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2","Pop", "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2","Hip-Hop", "Artist 3", "0");

        genreHash.addSong(song1);
        genreHash.addSong(song2);
        genreHash.addSong(song3);

        genreHash.removeSong(song1);

        assertFalse(genreHash.containsGenre(0));
        assertTrue(genreHash.containsGenre(1));
        assertTrue(genreHash.containsGenre(2));
    }

    @Test
    public void testRemoveAllSongs() {
        Song song1 = new Song("Song 1", "Album 1","Rock", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2","Pop", "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2","Pop", "Artist 3", "0");

        genreHash.addSong(song1);
        genreHash.addSong(song2);
        genreHash.addSong(song3);

        // Remove all songs
        genreHash.removeSong(song1);
        genreHash.removeSong(song2);
        genreHash.removeSong(song3);

        // Verify that all genres are empty
        assertFalse(genreHash.containsGenre(0));
        assertFalse(genreHash.containsGenre(1));
        assertFalse(genreHash.containsGenre(2));
        assertFalse(genreHash.containsGenre(3));
        assertFalse(genreHash.containsGenre(4));
        assertFalse(genreHash.containsGenre(5));

        int numberOfGenres = genreHash.nonEmptyGenres();
        assertEquals(0, numberOfGenres);
    }

    @Test
    public void testRemoveNonExistingSong() {
        Song song1 = new Song("Song 1", "Album 1","Rock", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2","Pop", "Artist 2", "0");

        genreHash.addSong(song1);

        // Attempt to remove a non-existing song
        genreHash.removeSong(song2);

        // Verify that the genre still contains the existing song
        assertTrue(genreHash.containsGenre(0));
        assertEquals(1, genreHash.getSongsFromGenre(0).size());
    }

    @Test
    public void testGetNonExistingSong() {
        Song song1 = new Song("Song 1", "Album 1","Rock", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2","Pop", "Artist 2", "0");

        genreHash.addSong(song1);

        // Attempt to get a non-existing song
        Song retrievedSong = genreHash.getSong("Song 2", "Album 2", 2, "Artist 2");

        // Verify that the retrieved song is null
        assertNull(retrievedSong);
    }

    @Test
    public void testGetSongsFromGenre() {
        Song song1 = new Song("Song 1", "Album 1","Rock", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2","Pop", "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2","Pop", "Artist 3", "0");
        Song song4 = new Song("Song 4", "Album 4", "Electronic", "Artist 4", "0");

        genreHash.addSong(song1);
        genreHash.addSong(song2);
        genreHash.addSong(song3);
        genreHash.addSong(song4);

        assertEquals(1, genreHash.getSongsFromGenre(0).size());
        assertEquals(2, genreHash.getSongsFromGenre(1).size());
        assertEquals(0, genreHash.getSongsFromGenre(2).size());
        assertEquals(1, genreHash.getSongsFromGenre(3).size());
    }

    @Test
    public void testGetSongsFromEmptyGenre() {
        Song song1 = new Song("Song 1", "Album 1","Rock", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2","Pop", "Artist 2", "0");

        genreHash.addSong(song1);
        genreHash.addSong(song2);

        // Get songs from non-existing genre
        List<Song> songsFromGenre = genreHash.getSongsFromGenre(2);

        // Verify that the returned list is empty
        assertNotNull(songsFromGenre);
        assertTrue(songsFromGenre.isEmpty());
    }

    @Test
    public void testNonEmptyGenres() {
        Song song1 = new Song("Song 1", "Album 1","Rock", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2","Pop", "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2","Pop", "Artist 3", "0");
        Song song4 = new Song("Song 4", "Album 4", "Electronic", "Artist 4", "0");

        genreHash.addSong(song1);
        genreHash.addSong(song2);
        genreHash.addSong(song3);
        genreHash.addSong(song4);

        // Check the number of non-empty genres
        int nonEmptyGenres = genreHash.nonEmptyGenres();

        // Verify that the correct number of non-empty genres is returned
        assertEquals(3, nonEmptyGenres);
    }

    @Test
    public void testRemoveAllSongsFromGenre() {
        Song song1 = new Song("Song 1", "Album 1","Rock", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2","Pop", "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2","Pop", "Artist 3", "0");
        Song song4 = new Song("Song 4", "Album 4", "Electronic", "Artist 4", "0");

        genreHash.addSong(song1);
        genreHash.addSong(song2);
        genreHash.addSong(song3);
        genreHash.addSong(song4);

        // Remove all songs from genre 1
        genreHash.removeSong(song2);
        genreHash.removeSong(song3);

        // Verify that genre 1 is empty
        assertFalse(genreHash.containsGenre(1));
        assertEquals(0, genreHash.getSongsFromGenre(1).size());
    }

    @Test
    public void testContainsGenre() {
        Song song1 = new Song("Song 1", "Album 1","Rock", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2","Pop", "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2","Hip-Hop", "Artist 3", "0");

        genreHash.addSong(song1);
        genreHash.addSong(song3);

        assertTrue(genreHash.containsGenre(0));
        assertFalse(genreHash.containsGenre(1));
        assertTrue(genreHash.containsGenre(2));

        genreHash.addSong(song2);

        assertTrue(genreHash.containsGenre(0));
        assertTrue(genreHash.containsGenre(1));
        assertTrue(genreHash.containsGenre(2));
    }
}