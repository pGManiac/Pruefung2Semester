package BackEndTests;

import backend.AlbumHash;
import backend.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AlbumHashTest {

    private AlbumHash albumHash;

    @BeforeEach
    public void setUp() {
        albumHash = new AlbumHash();
    }

    @Test
    public void testAddSong() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2", 3, "Artist 3", "0");

        albumHash.addSong(song1);
        albumHash.addSong(song2);
        albumHash.addSong(song3);

        assertEquals(2, albumHash.size());
        assertTrue(albumHash.containsAlbum("Album 1"));
        assertTrue(albumHash.containsAlbum("Album 2"));
    }

    @Test
    public void testGetSongsFromAlbum() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2", 2, "Artist 3", "0");
        Song song4 = new Song("Song 4", "Album 3", 4, "Artist 4", "0");

        albumHash.addSong(song1);
        albumHash.addSong(song2);
        albumHash.addSong(song3);
        albumHash.addSong(song4);

        assertEquals(1, albumHash.getSongsFromAlbum("Album 1").size());
        assertEquals(2, albumHash.getSongsFromAlbum("Album 2").size());
        assertEquals(1, albumHash.getSongsFromAlbum("Album 3").size());
        assertEquals(0, albumHash.getSongsFromAlbum("Album 4").size());
    }

    @Test
    public void testContainsAlbum() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2", 3, "Artist 3", "0");

        albumHash.addSong(song1);
        albumHash.addSong(song3);

        assertTrue(albumHash.containsAlbum("Album 1"));
        assertFalse(albumHash.containsAlbum("Album 4"));
    }

    @Test
    public void testSize() {
        assertEquals(0, albumHash.size());

        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 2", "0");

        albumHash.addSong(song1);
        assertEquals(1, albumHash.size());

        albumHash.addSong(song2);
        assertEquals(2, albumHash.size());
    }
}