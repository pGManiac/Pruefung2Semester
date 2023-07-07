package BackEndTests;

import backend.AlbumHash;
import backend.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AlbumHashTest {

    private AlbumHash albumHash;

    @BeforeEach
    public void setUp() {
        albumHash = new AlbumHash();
    }

    @Test
    public void testAddSong() {
        Song song1 = new Song("Song 1", "Album 1","Rock", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2","Pop", "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2","Hip-Hop", "Artist 3", "0");

        albumHash.addSong(song1);
        albumHash.addSong(song2);
        albumHash.addSong(song3);

        assertEquals(2, albumHash.size());
        assertTrue(albumHash.containsAlbum("Album 1"));
        assertTrue(albumHash.containsAlbum("Album 2"));
    }

    @Test
    public void testRemoveSong() {
        Song song1 = new Song("Song 1", "Album 1","Rock", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2","Pop", "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2","Hip-Hop", "Artist 3", "0");

        albumHash.addSong(song1);
        albumHash.addSong(song2);
        albumHash.addSong(song3);

        assertEquals(2, albumHash.size());

        albumHash.removeSong(song1);

        assertEquals(1, albumHash.size());

        assertEquals(1, albumHash.size());
        assertFalse(albumHash.containsAlbum("Album 1"));
        assertTrue(albumHash.containsAlbum("Album 2"));
    }

    @Test
    public void testAddMultipleSongsWithSameAlbum() {
        Song song1 = new Song("Song 1", "Album 1","Rock", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 1","Pop", "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 1","Hip-Hop", "Artist 3", "0");

        albumHash.addSong(song1);
        albumHash.addSong(song2);
        albumHash.addSong(song3);

        List<Song> songsFromAlbum = albumHash.getSongsFromAlbum("Album 1");

        assertNotNull(songsFromAlbum);
        assertEquals(3, songsFromAlbum.size());
        assertTrue(songsFromAlbum.contains(song1));
        assertTrue(songsFromAlbum.contains(song2));
        assertTrue(songsFromAlbum.contains(song3));
    }


    @Test
    public void testRemoveNonExistingSongFromAlbum() {
        // Create songs and add them to the album hash
        Song song1 = new Song("Song 1", "Album 1","Rock", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2","Pop", "Artist 2", "0");
        albumHash.addSong(song1);

        // Attempt to remove a non-existing song from the album
        albumHash.removeSong(song2);

        // Assert that the album size remains unchanged and contains the original song
        assertEquals(1, albumHash.size());
        assertTrue(albumHash.containsSong("Song 1", "Album 1", 0, "Artist 1"));
        assertFalse(albumHash.containsSong("Song 2", "Album 2", 1, "Artist 2"));
    }

    @Test
    public void testGetSongsFromAlbum() {
        Song song1 = new Song("Song 1", "Album 1", "Rock", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", "Pop", "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2", "Pop", "Artist 3", "0");
        Song song4 = new Song("Song 4", "Album 3", "Electronic", "Artist 4", "0");

        albumHash.addSong(song1);
        albumHash.addSong(song2);
        albumHash.addSong(song3);
        albumHash.addSong(song4);

        assertEquals(1, albumHash.getSongsFromAlbum("Album 1").size());
        assertEquals(2, albumHash.getSongsFromAlbum("Album 2").size());
        assertEquals(1, albumHash.getSongsFromAlbum("Album 3").size());
        assertNull(albumHash.getSongsFromAlbum("Album 4"));
    }
    @Test
    public void testGetSongsFromSameAlbum() {
        Song song1 = new Song("Song 1", "Album 1", "Rock", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", "Pop", "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2", "Pop", "Artist 3", "0");
        Song song4 = new Song("Song 4", "Album 3", "Electronic", "Artist 4", "0");

        albumHash.addSong(song1);
        albumHash.addSong(song2);
        albumHash.addSong(song3);
        albumHash.addSong(song4);

        // Test retrieving songs from Album 1
        List<Song> songsFromAlbum1 = albumHash.getSongsFromAlbum("Album 1");
        assertEquals(1, songsFromAlbum1.size());
        assertTrue(songsFromAlbum1.contains(song1));

        // Test retrieving songs from Album 2
        List<Song> songsFromAlbum2 = albumHash.getSongsFromAlbum("Album 2");
        assertEquals(2, songsFromAlbum2.size());
        assertTrue(songsFromAlbum2.contains(song2));
        assertTrue(songsFromAlbum2.contains(song3));

        // Test retrieving songs from Album 3
        List<Song> songsFromAlbum3 = albumHash.getSongsFromAlbum("Album 3");
        assertEquals(1, songsFromAlbum3.size());
        assertTrue(songsFromAlbum3.contains(song4));

        // Test retrieving songs from non-existing Album 4
        List<Song> songsFromNonExistingAlbum = albumHash.getSongsFromAlbum("Album 4");
        assertNull(songsFromNonExistingAlbum);
    }

    @Test
    public void testContainsAlbum() {
        Song song1 = new Song("Song 1", "Album 1","Rock", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2","Pop", "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 3","Hip-Hop", "Artist 3", "0");

        albumHash.addSong(song1);
        albumHash.addSong(song3);

        // Test for existing albums
        assertTrue(albumHash.containsAlbum("Album 1"));
        assertTrue(albumHash.containsAlbum("Album 3"));

        // Test for non-existing album
        assertFalse(albumHash.containsAlbum("Album 2"));
        assertFalse(albumHash.containsAlbum("Album 4"));
    }


    @Test
    public void testSize() {
        assertEquals(0, albumHash.size());

        Song song1 = new Song("Song 1", "Album 1","Rock", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2","Pop", "Artist 2", "0");


        albumHash.addSong(song1);
        assertEquals(1, albumHash.size());

        albumHash.addSong(song2);
        assertEquals(2, albumHash.size());
    }
}