package BackEndTests;

import backend.ArtistHash;
import backend.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArtistHashTest {

    private ArtistHash artistHash;

    @BeforeEach
    public void setUp() {
        artistHash = new ArtistHash();
    }

    @Test
    public void testAddSong() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 1", "0");
        Song song3 = new Song("Song 3", "Album 2", 3, "Artist 2", "0");

        artistHash.addSong(song1);
        artistHash.addSong(song2);
        artistHash.addSong(song3);

        assertEquals(2, artistHash.size());
        assertTrue(artistHash.containsArtist("Artist 1"));
        assertTrue(artistHash.containsArtist("Artist 2"));
    }

    @Test
    public void testRemoveSong() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 1", "0");
        Song song3 = new Song("Song 1", "Album 2", 3, "Artist 2", "0");

        artistHash.addSong(song1);
        artistHash.addSong(song2);
        artistHash.addSong(song3);

        assertEquals(2, artistHash.size());

        artistHash.removeSong("Song 1", "Album 1", 1, "Artist 2");

        assertEquals(1, artistHash.size());

        assertEquals(1, artistHash.size());
        assertTrue(artistHash.containsArtist("Artist 1"));
        assertFalse(artistHash.containsArtist("Artist 2"));
    }

    @Test
    public void testGetSongsFromArtist() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 1", "0");
        Song song3 = new Song("Song 3", "Album 2", 2, "Artist 2", "0");
        Song song4 = new Song("Song 4", "Album 3", 4, "Artist 3", "0");

        artistHash.addSong(song1);
        artistHash.addSong(song2);
        artistHash.addSong(song3);
        artistHash.addSong(song4);

        assertEquals(2, artistHash.getSongsFromArtist("Artist 1").size());
        assertEquals(1, artistHash.getSongsFromArtist("Artist 2").size());
        assertEquals(1, artistHash.getSongsFromArtist("Artist 3").size());
        assertNull(artistHash.getSongsFromArtist("Artist 4"));
    }

    @Test
    public void testContainsArtist() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 4", 2, "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2", 3, "Artist 3", "0");

        artistHash.addSong(song1);
        artistHash.addSong(song3);

        assertTrue(artistHash.containsArtist("Artist 1"));
        assertFalse(artistHash.containsArtist("Artist 4"));
    }

    @Test
    public void testSize() {
        assertEquals(0, artistHash.size());

        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 1", "0");

        artistHash.addSong(song1);
        assertEquals(1, artistHash.size());

        artistHash.addSong(song2);
        assertEquals(1, artistHash.size()); // Only one artist ("Artist 1") is present with two songs
    }
}