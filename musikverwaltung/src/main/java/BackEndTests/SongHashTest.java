package BackEndTests;

import backend.Song;
import backend.SongHash;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SongHashTest {

    private SongHash songHash;


    @BeforeEach
    public void setUp() {
        songHash = new SongHash();
    }

    @Test
    public void testAddSong() {
        Song song1 = new Song("Song 1", "Album 1", "Pop", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", "Hip-Hop", "Artist 2", "0");
        Song song3 = new Song("Song 1", "Album 2", "Electronic", "Artist 3", "0");

        songHash.addSong(song1);
        songHash.addSong(song2);
        songHash.addSong(song3);

        assertEquals(2, songHash.size());
        assertTrue(songHash.containsSong("Song 1", "Album 1", 1, "Artist 1"));
        assertTrue(songHash.containsSong("Song 2", "Album 2", 2, "Artist 2"));
        assertTrue(songHash.containsSong("Song 1", "Album 2", 3, "Artist 3"));
        assertFalse(songHash.containsSong("Song 1", "Album 2", 1,"Artist 2"));
    }

    @Test
    public void testRemoveSong() {
        Song song1 = new Song("Song 1", "Album 1", "Pop", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", "Hip-Hop", "Artist 2", "0");
        Song song3 = new Song("Song 1", "Album 2", "Electronic", "Artist 3", "0");

        songHash.addSong(song1);
        songHash.addSong(song2);
        songHash.addSong(song3);

        songHash.removeSong(song1);

        assertEquals(2, songHash.size());
        assertFalse(songHash.containsSong("Song 1","Album 1", 1,"Artist 1"));
        assertTrue(songHash.containsSong("Song 1","Album 2", 3,"Artist 3"));
        assertTrue(songHash.containsSong("Song 2","Album 2", 2,"Artist 2"));
    }

    @Test
    public void testGetSong() {
        Song song1 = new Song("Song 1", "Album 1", "Pop", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", "Hip-Hop", "Artist 2", "0");
        Song song3 = new Song("Song 1", "Album 3", "Electronic", "Artist 3", "0");

        songHash.addSong(song1);
        songHash.addSong(song2);
        songHash.addSong(song3);

        Song retrievedSong1 = songHash.getSong("Song 1", "Album 1", 1, "Artist 1");
        Song retrievedSong2 = songHash.getSong("Song 2", "Album 2",  2, "Artist 2");
        Song retrievedSong3 = songHash.getSong("Song 1", "Album 3", 3, "Artist 3");

        assertNotNull(retrievedSong1);
        assertNotNull(retrievedSong2);
        assertNotNull(retrievedSong3);

        assertEquals("Song 1", retrievedSong1.getName());
        assertEquals("Album 1", retrievedSong1.getAlbum());
        assertEquals(1, retrievedSong1.getGenreNumber());
        assertEquals("Artist 1", retrievedSong1.getArtist());

        assertEquals("Song 2", retrievedSong2.getName());
        assertEquals("Album 2", retrievedSong2.getAlbum());
        assertEquals(2, retrievedSong2.getGenreNumber());
        assertEquals("Artist 2", retrievedSong2.getArtist());

        assertEquals("Song 1", retrievedSong3.getName());
        assertEquals("Album 3", retrievedSong3.getAlbum());
        assertEquals(3, retrievedSong3.getGenreNumber());
        assertEquals("Artist 3", retrievedSong3.getArtist());
    }

    @Test
    public void testContainsSong() {
        Song song1 = new Song("Song 1", "Album 1", "Pop", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", "Hip-Hop", "Artist 2", "0");
        Song song3 = new Song("Song 3", "Album 2", "Electronic", "Artist 3", "0");
        Song song4 = new Song("Song 3", "Album 4", "Indie", "Artist 1", "0");

        songHash.addSong(song1);
        songHash.addSong(song3);
        songHash.addSong(song4);

        assertTrue(songHash.containsSong("Song 1", "Album 1", 1, "Artist 1"));
        assertFalse(songHash.containsSong("Song 2", "Album 2", 2, "Artist 2"));
        assertTrue(songHash.containsSong("Song 3", "Album 2", 3, "Artist 3"));
        assertTrue(songHash.containsSong("Song 3", "Album 4", 4, "Artist 1"));

        songHash.addSong(song2);

        assertTrue(songHash.containsSong("Song 1", "Album 1", 1, "Artist 1"));
        assertTrue(songHash.containsSong("Song 2", "Album 2", 2, "Artist 2"));
        assertTrue(songHash.containsSong("Song 3", "Album 2", 3, "Artist 3"));
        assertTrue(songHash.containsSong("Song 3", "Album 4", 4, "Artist 1"));
    }

    @Test
    public void testSize() {
        assertEquals(0, songHash.size());

        Song song1 = new Song("Song 1", "Album 1", "Pop", "Artist 1", "0");
        Song song2 = new Song("Song 2", "Album 2", "Hip-Hop", "Artist 2", "0");
        Song song3 = new Song("Song 1", "Album 2", "Electronic", "Artist 3", "0");

        songHash.addSong(song1);
        assertEquals(1, songHash.size());

        songHash.addSong(song2);
        assertEquals(2, songHash.size());

        songHash.addSong(song3);
        assertEquals(2, songHash.size());

        songHash.removeSong(song3);
        assertEquals(2, songHash.size());

        songHash.removeSong(song2);
        assertEquals(1, songHash.size());

        songHash.removeSong(song1);
        assertEquals(0, songHash.size());
    }

    @Test
    public void sortAToZ() {
        // Adding songs to the songHash
        songHash.addSong(new Song("Song F", "5", "Pop", "1", "0"));
        songHash.addSong(new Song("Song I", "3", "Pop", "4", "0"));
        songHash.addSong(new Song("Song G", "7", "Pop", "3", "ß"));
        songHash.addSong(new Song("Song J", "2", "Pop", "5", "0"));
        songHash.addSong(new Song("Song J", "1", "Hip-Hop", "1", "0"));
        songHash.addSong(new Song("Song C", "7", "Electronic", "1", "ß"));
        songHash.addSong(new Song("Song C", "9", "Pop", "2", "0"));
        songHash.addSong(new Song("Song B", "2", "Pop", "1", "0"));
        songHash.addSong(new Song("Song A", "5", "Pop", "1", "0"));
        songHash.addSong(new Song("Song E", "3", "Pop", "2", "0"));
        songHash.addSong(new Song("Song H", "4", "Pop", "1", "0"));
        songHash.addSong(new Song("Song D", "6", "Pop", "1", "0"));

        // Sorting songs in alphabetical order
        List<Song> sortedSongs = songHash.sortAToZ();

        // Asserting the sorted songs
        assertEquals(12, sortedSongs.size());
        assertEquals("Song A", sortedSongs.get(0).getName());
        assertEquals("Song B", sortedSongs.get(1).getName());
        assertEquals("Song C", sortedSongs.get(2).getName());
        assertEquals("1", sortedSongs.get(2).getArtist());
        assertEquals("Song C", sortedSongs.get(3).getName());
        assertEquals("2", sortedSongs.get(3).getArtist());
        assertEquals("Song D", sortedSongs.get(4).getName());
        assertEquals("Song E", sortedSongs.get(5).getName());
        assertEquals("Song F", sortedSongs.get(6).getName());
        assertEquals("Song G", sortedSongs.get(7).getName());
        assertEquals("Song H", sortedSongs.get(8).getName());
        assertEquals("Song I", sortedSongs.get(9).getName());
        assertEquals("Song J", sortedSongs.get(10).getName());
        assertEquals("1", sortedSongs.get(10).getArtist());
        assertEquals("Song J", sortedSongs.get(11).getName());
        assertEquals("5", sortedSongs.get(11).getArtist());
    }

    @Test
    public void testSortZToA() {
        // Adding songs to the songHash
        songHash.addSong(new Song("Song F", "5", "Pop", "1", "0"));
        songHash.addSong(new Song("Song I", "3", "Pop", "4", "0"));
        songHash.addSong(new Song("Song G", "7", "Pop", "3", "ß"));
        songHash.addSong(new Song("Song J", "2", "Pop", "5", "0"));
        songHash.addSong(new Song("Song J", "1", "Hip-Hop", "1", "0"));
        songHash.addSong(new Song("Song C", "7", "Electronic", "1", "ß"));
        songHash.addSong(new Song("Song C", "9", "Pop", "2", "0"));
        songHash.addSong(new Song("Song B", "2", "Pop", "1", "0"));
        songHash.addSong(new Song("Song A", "5", "Pop", "1", "0"));
        songHash.addSong(new Song("Song E", "3", "Pop", "2", "0"));
        songHash.addSong(new Song("Song H", "4", "Pop", "1", "0"));
        songHash.addSong(new Song("Song D", "6", "Pop", "1", "0"));

        // Sorting songs in reverse alphabetical order
        List<Song> sortedSongs = songHash.sortZToA();

        // Asserting the sorted songs
        assertEquals(12, sortedSongs.size());
        assertEquals("Song J", sortedSongs.get(0).getName());
        assertEquals("5", sortedSongs.get(0).getArtist());
        assertEquals("Song J", sortedSongs.get(1).getName());
        assertEquals("1", sortedSongs.get(1).getArtist());
        assertEquals("Song I", sortedSongs.get(2).getName());
        assertEquals("Song H", sortedSongs.get(3).getName());
        assertEquals("Song G", sortedSongs.get(4).getName());
        assertEquals("Song F", sortedSongs.get(5).getName());
        assertEquals("Song E", sortedSongs.get(6).getName());
        assertEquals("Song D", sortedSongs.get(7).getName());
        assertEquals("Song C", sortedSongs.get(8).getName());
        assertEquals("2", sortedSongs.get(8).getArtist());
        assertEquals("Song C", sortedSongs.get(9).getName());
        assertEquals("1", sortedSongs.get(9).getArtist());
        assertEquals("Song B", sortedSongs.get(10).getName());
        assertEquals("Song A", sortedSongs.get(11).getName());
    }

    @Test
    public void testSortAToZByGenre() {
        // Adding songs to the songHash
        songHash.addSong(new Song("Song F", "4", "Electronic", "3", "0"));
        songHash.addSong(new Song("Song I", "5", "Pop", "1","0"));
        songHash.addSong(new Song("Song I", "1", "Pop", "3", "0"));
        songHash.addSong(new Song("Song G", "6", "Pop", "1", "0"));
        songHash.addSong(new Song("Song J", "3", "Hip-Hop", "1", "0"));
        songHash.addSong(new Song("Song C", "1", "Pop", "4", "0"));
        songHash.addSong(new Song("Song C", "2", "Pop", "1", "0"));
        songHash.addSong(new Song("Song B", "1", "Indie", "1", "0"));
        songHash.addSong(new Song("Song A", "3", "Pop", "7", "0"));
        songHash.addSong(new Song("Song E", "1", "Pop", "1", "0"));
        songHash.addSong(new Song("Song H", "2", "Metal", "2", "0"));
        songHash.addSong(new Song("Song D", "1", "Pop", "1", "0"));

        // Sorting songs by genre in alphabetical order
        List<Song> sortedSongs = songHash.sortAToZByGenre(1);

        // Asserting the sorted songs
        assertEquals(8, sortedSongs.size());
        assertEquals("Song A", sortedSongs.get(0).getName());
        assertEquals("Song C", sortedSongs.get(1).getName());
        assertEquals("1", sortedSongs.get(1).getArtist());
        assertEquals("Song C", sortedSongs.get(2).getName());
        assertEquals("4", sortedSongs.get(2).getArtist());
        assertEquals("Song D", sortedSongs.get(3).getName());
        assertEquals("Song E", sortedSongs.get(4).getName());
        assertEquals("Song G", sortedSongs.get(5).getName());
        assertEquals("Song I", sortedSongs.get(6).getName());
        assertEquals("1", sortedSongs.get(6).getArtist());
        assertEquals("Song I", sortedSongs.get(7).getName());
        assertEquals("3", sortedSongs.get(7).getArtist());
    }
}