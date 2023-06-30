package BackEndTests;


import backend.Song;
import backend.SongHash;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;


class SongHashTest {

    private SongHash songHash;

    @BeforeEach
    public void setUp() {
        songHash = new SongHash();
    }

    @Test
    public void testAddSong() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 2");
        Song song3 = new Song("Song 3", "Album 2", 3, "Artist 3");

        songHash.addSong(song1);
        songHash.addSong(song2);
        songHash.addSong(song3);

        assertEquals(3, songHash.size());
        assertTrue(songHash.containsSong("Song 1"));
        assertTrue(songHash.containsSong("Song 2"));
        assertTrue(songHash.containsSong("Song 3"));
    }

    @Test
    public void testRemoveSong() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 2");
        Song song3 = new Song("Song 3", "Album 2", 3, "Artist 3");


        songHash.addSong(song1);
        songHash.addSong(song2);
        songHash.addSong(song3);

        songHash.removeSong("Song 2");

        assertEquals(2, songHash.size());
        assertFalse(songHash.containsSong("Song 2"));
        assertTrue(songHash.containsSong("Song 1"));
        assertTrue(songHash.containsSong("Song 3"));
    }

    @Test
    public void testGetSong() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 2");
        Song song3 = new Song("Song 3", "Album 3", 3, "Artist 3");

        songHash.addSong(song1);
        songHash.addSong(song2);
        songHash.addSong(song3);

        Song retrievedSong1 = songHash.getSong("Song 1");
        Song retrievedSong2 = songHash.getSong("Song 2");
        Song retrievedSong3 = songHash.getSong("Song 3");

        assertNotNull(retrievedSong1);
        assertNotNull(retrievedSong2);
        assertNotNull(retrievedSong3);

        assertEquals("Song 1", retrievedSong1.getName());
        assertEquals("Album 1", retrievedSong1.getAlbum());
        assertEquals(1, retrievedSong1.getGenre());
        assertEquals("Artist 1", retrievedSong1.getArtist());

        assertEquals("Song 2", retrievedSong2.getName());
        assertEquals("Album 2", retrievedSong2.getAlbum());
        assertEquals(2, retrievedSong2.getGenre());
        assertEquals("Artist 2", retrievedSong2.getArtist());

        assertEquals("Song 3", retrievedSong3.getName());
        assertEquals("Album 3", retrievedSong3.getAlbum());
        assertEquals(3, retrievedSong3.getGenre());
        assertEquals("Artist 3", retrievedSong3.getArtist());

    }

    @Test
    public void testContainsSong() {
        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 2");
        Song song3 = new Song("Song 3", "Album 2", 3, "Artist 3");

        songHash.addSong(song1);
        songHash.addSong(song3);

        assertTrue(songHash.containsSong("Song 1"));
        assertFalse(songHash.containsSong("Song 2"));
        assertTrue(songHash.containsSong("Song 3"));

        songHash.addSong(song2);

        assertTrue(songHash.containsSong("Song 1"));
        assertTrue(songHash.containsSong("Song 2"));
        assertTrue(songHash.containsSong("Song 3"));
    }

    @Test
    public void testSize() {
        assertEquals(0, songHash.size());

        Song song1 = new Song("Song 1", "Album 1", 1, "Artist 1");
        Song song2 = new Song("Song 2", "Album 2", 2, "Artist 2");
        Song song3 = new Song("Song 3", "Album 2", 3, "Artist 3");

        songHash.addSong(song1);
        assertEquals(1, songHash.size());

        songHash.addSong(song2);
        assertEquals(2, songHash.size());

        songHash.addSong(song3);
        assertEquals(3, songHash.size());

        songHash.removeSong("Song 1");
        assertEquals(2, songHash.size());

        songHash.removeSong("Song 2");
        assertEquals(1, songHash.size());

        songHash.removeSong("Song 3");
        assertEquals(0, songHash.size());
    }
    @Test
    public void sortAToZ() {
        // Adding songs to the songHash
        songHash.addSong(new Song("Song F", "1", 1, "1"));
        songHash.addSong(new Song("Song I", "1", 1, "1"));
        songHash.addSong(new Song("Song G", "1", 1, "1"));
        songHash.addSong(new Song("Song J", "1", 1, "1"));
        songHash.addSong(new Song("Song C", "1", 1, "1"));
        songHash.addSong(new Song("Song B", "1", 1, "1"));
        songHash.addSong(new Song("Song A", "1", 1, "1"));
        songHash.addSong(new Song("Song E", "1", 1, "1"));
        songHash.addSong(new Song("Song H", "1", 1, "1"));
        songHash.addSong(new Song("Song D", "1", 1, "1"));

        // Sorting songs in reverse alphabetical order
        List<Song> sortedSongs = songHash.sortAToZ();

        // Asserting the sorted songs
        assertEquals(10, sortedSongs.size());
        assertEquals("Song J", sortedSongs.get(9).getName());
        assertEquals("Song I", sortedSongs.get(8).getName());
        assertEquals("Song H", sortedSongs.get(7).getName());
        assertEquals("Song G", sortedSongs.get(6).getName());
        assertEquals("Song F", sortedSongs.get(5).getName());
        assertEquals("Song E", sortedSongs.get(4).getName());
        assertEquals("Song D", sortedSongs.get(3).getName());
        assertEquals("Song C", sortedSongs.get(2).getName());
        assertEquals("Song B", sortedSongs.get(1).getName());
        assertEquals("Song A", sortedSongs.get(0).getName());
    }

    @Test
    public void testSortZToA() {
        // Adding songs to the songHash
        songHash.addSong(new Song("Song F", "1", 1, "1"));
        songHash.addSong(new Song("Song I", "1", 1, "1"));
        songHash.addSong(new Song("Song G", "1", 1, "1"));
        songHash.addSong(new Song("Song J", "1", 1, "1"));
        songHash.addSong(new Song("Song C", "1", 1, "1"));
        songHash.addSong(new Song("Song B", "1", 1, "1"));
        songHash.addSong(new Song("Song A", "1", 1, "1"));
        songHash.addSong(new Song("Song E", "1", 1, "1"));
        songHash.addSong(new Song("Song H", "1", 1, "1"));
        songHash.addSong(new Song("Song D", "1", 1, "1"));

        // Sorting songs in reverse alphabetical order
        List<Song> sortedSongs = songHash.sortZToA();

        // Asserting the sorted songs
        assertEquals(10, sortedSongs.size());
        assertEquals("Song J", sortedSongs.get(0).getName());
        assertEquals("Song I", sortedSongs.get(1).getName());
        assertEquals("Song H", sortedSongs.get(2).getName());
        assertEquals("Song G", sortedSongs.get(3).getName());
        assertEquals("Song F", sortedSongs.get(4).getName());
        assertEquals("Song E", sortedSongs.get(5).getName());
        assertEquals("Song D", sortedSongs.get(6).getName());
        assertEquals("Song C", sortedSongs.get(7).getName());
        assertEquals("Song B", sortedSongs.get(8).getName());
        assertEquals("Song A", sortedSongs.get(9).getName());
    }

    @Test
    public void testSortAToZByGenre() {
        // Adding songs to the songHash
        songHash.addSong(new Song("Song F", "1", 3, "1"));
        songHash.addSong(new Song("Song I", "1", 1, "1"));
        songHash.addSong(new Song("Song G", "1", 1, "1"));
        songHash.addSong(new Song("Song J", "1", 2, "1"));
        songHash.addSong(new Song("Song C", "1", 1, "1"));
        songHash.addSong(new Song("Song B", "1", 4, "1"));
        songHash.addSong(new Song("Song A", "1", 1, "1"));
        songHash.addSong(new Song("Song E", "1", 1, "1"));
        songHash.addSong(new Song("Song H", "1", 7, "1"));
        songHash.addSong(new Song("Song D", "1", 1, "1"));

        // Sorting songs in reverse alphabetical order
        List<Song> sortedSongs = songHash.sortAToZByGenre(1);

        // Asserting the sorted songs
        assertEquals(6, sortedSongs.size());
        assertEquals("Song I", sortedSongs.get(5).getName());
        assertEquals("Song G", sortedSongs.get(4).getName());
        assertEquals("Song E", sortedSongs.get(3).getName());
        assertEquals("Song D", sortedSongs.get(2).getName());
        assertEquals("Song C", sortedSongs.get(1).getName());
        assertEquals("Song A", sortedSongs.get(0).getName());
    }
}