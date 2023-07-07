package backendTesting;

import backend.ArtistHash;
import backend.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArtistHashTest {

    private ArtistHash artistHash;

    @BeforeEach
    public void setUp() {
        artistHash = new ArtistHash();
    }

    @Test
    public void testAddSong() {
        Song song1 = new Song("Song 1", "Album 1", "Pop", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", "Hip-Hop", "Artist 1", "0");
        Song song3 = new Song("Song 3", "Album 2", "Electronic", "Artist 2", "0");

        artistHash.addSong(song1);
        artistHash.addSong(song2);
        artistHash.addSong(song3);

        assertEquals(2, artistHash.size());
        assertTrue(artistHash.containsArtist("Artist 1"));
        assertTrue(artistHash.containsArtist("Artist 2"));
    }

    @Test
    public void testAddMultipleSongsToSameArtist() {
        Song song1 = new Song("Song 1", "Album 1", "Pop", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", "Hip-Hop", "Artist 1", "0");
        Song song3 = new Song("Song 3", "Album 2", "Electronic", "Artist 1", "0");

        artistHash.addSong(song1);
        artistHash.addSong(song2);
        artistHash.addSong(song3);

        List<Song> songs = artistHash.getSongsFromArtist("Artist 1");

        assertEquals(3, songs.size());
        assertTrue(songs.contains(song1));
        assertTrue(songs.contains(song2));
        assertTrue(songs.contains(song3));
    }

    @Test
    public void testRemoveSong() {
        Song song1 = new Song("Song 1", "Album 1", "Pop", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", "Hip-Hop", "Artist 1", "0");
        Song song3 = new Song("Song 1", "Album 2", "Electronic", "Artist 2", "0");

        artistHash.addSong(song1);
        artistHash.addSong(song2);
        artistHash.addSong(song3);

        assertEquals(2, artistHash.size());

        artistHash.removeSong(song3);

        assertEquals(1, artistHash.size());

        assertEquals(1, artistHash.size());
        assertTrue(artistHash.containsArtist("Artist 1"));
        assertFalse(artistHash.containsArtist("Artist 2"));
    }

    @Test
    public void testRemoveAllSongsFromArtist() {
        Song song1 = new Song("Song 1", "Album 1", "Pop", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", "Hip-Hop", "Artist 1", "0");

        artistHash.addSong(song1);
        artistHash.addSong(song2);

        // Remove all songs from artist "Artist 1"
        artistHash.removeSong(song1);
        artistHash.removeSong(song2);

        // Verify that the artist hash no longer contains the artist
        assertFalse(artistHash.containsArtist("Artist 1"));
        assertEquals(0, artistHash.size());
    }

    @Test
    public void testGetSongsFromArtist() {
        Song song1 = new Song("Song 1", "Album 1", "Pop", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", "Hip-Hop", "Artist 1", "0");
        Song song3 = new Song("Song 3", "Album 2", "Hip-Hop", "Artist 2", "0");
        Song song4 = new Song("Song 4", "Album 3", "Indie", "Artist 3", "0");

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
    public void testGetNonExistingSongFromArtist() {
        Song song1 = new Song("Song 1", "Album 1", "Pop", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", "Hip-Hop", "Artist 2", "0");

        artistHash.addSong(song1);

        // Attempt to get a non-existing song from artist "Artist 2"
        Song retrievedSong = artistHash.getSong("Song 2", "Album 2", 2, "Artist 2");

        // Verify that the retrieved song is null
        assertNull(retrievedSong);
    }

    @Test
    public void testGetSongsFromNonExistingArtist() {
        Song song1 = new Song("Song 1", "Album 1", "Pop", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", "Hip-Hop", "Artist 2", "0");

        artistHash.addSong(song1);

        // Get songs from a non-existing artist
        List<Song> songs = artistHash.getSongsFromArtist("Artist 2");

        // Verify that the list of songs is null
        assertNull(songs);
    }

    @Test
    public void testContainsArtist() {
        Song song1 = new Song("Song 1", "Album 1", "Pop", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 4", "Hip-Hop", "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2", "Electronic", "Artist 3", "0");

        artistHash.addSong(song1);
        artistHash.addSong(song3);

        assertTrue(artistHash.containsArtist("Artist 1"));
        assertFalse(artistHash.containsArtist("Artist 4"));
    }


    @Test
    public void testContainsSongInNonExistingArtist() {
        Song song1 = new Song("Song 1", "Album 1", "Pop", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", "Hip-Hop", "Artist 2", "0");

        artistHash.addSong(song1);

        // Check if a song is contained in a non-existing artist
        boolean containsSong = artistHash.containsSong("Song 1", "Album 1", 1, "Artist 2");

        // Verify that the song is not contained in the non-existing artist
        assertFalse(containsSong);
    }

    @Test
    public void testSize() {
        assertEquals(0, artistHash.size());

        Song song1 = new Song("Song 1", "Album 1", "Pop", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", "Hip-Hop", "Artist 1", "0");

        artistHash.addSong(song1);
        assertEquals(1, artistHash.size());

        artistHash.addSong(song2);
        assertEquals(1, artistHash.size()); // Only one artist ("Artist 1") is present with two songs
    }
}