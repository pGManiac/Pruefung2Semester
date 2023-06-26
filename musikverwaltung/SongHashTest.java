package musikverwaltung;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class SongHashTest {

    @Test
    public void sortAToZ() {
        SongHash songHash = new SongHash();
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
        Assertions.assertEquals(10, sortedSongs.size());
        Assertions.assertEquals("Song J", sortedSongs.get(9).getName());
        Assertions.assertEquals("Song I", sortedSongs.get(8).getName());
        Assertions.assertEquals("Song H", sortedSongs.get(7).getName());
        Assertions.assertEquals("Song G", sortedSongs.get(6).getName());
        Assertions.assertEquals("Song F", sortedSongs.get(5).getName());
        Assertions.assertEquals("Song E", sortedSongs.get(4).getName());
        Assertions.assertEquals("Song D", sortedSongs.get(3).getName());
        Assertions.assertEquals("Song C", sortedSongs.get(2).getName());
        Assertions.assertEquals("Song B", sortedSongs.get(1).getName());
        Assertions.assertEquals("Song A", sortedSongs.get(0).getName());
    }

    @Test
    public void testSortZToA() {

        SongHash songHash = new SongHash();
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
        Assertions.assertEquals(10, sortedSongs.size());
        Assertions.assertEquals("Song J", sortedSongs.get(0).getName());
        Assertions.assertEquals("Song I", sortedSongs.get(1).getName());
        Assertions.assertEquals("Song H", sortedSongs.get(2).getName());
        Assertions.assertEquals("Song G", sortedSongs.get(3).getName());
        Assertions.assertEquals("Song F", sortedSongs.get(4).getName());
        Assertions.assertEquals("Song E", sortedSongs.get(5).getName());
        Assertions.assertEquals("Song D", sortedSongs.get(6).getName());
        Assertions.assertEquals("Song C", sortedSongs.get(7).getName());
        Assertions.assertEquals("Song B", sortedSongs.get(8).getName());
        Assertions.assertEquals("Song A", sortedSongs.get(9).getName());
    }

    @Test
    public void testSortAToZByGenre() {
        SongHash songHash = new SongHash();
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
        Assertions.assertEquals(6, sortedSongs.size());
        Assertions.assertEquals("Song I", sortedSongs.get(5).getName());
        Assertions.assertEquals("Song G", sortedSongs.get(4).getName());
        Assertions.assertEquals("Song E", sortedSongs.get(3).getName());
        Assertions.assertEquals("Song D", sortedSongs.get(2).getName());
        Assertions.assertEquals("Song C", sortedSongs.get(1).getName());
        Assertions.assertEquals("Song A", sortedSongs.get(0).getName());
    }
}