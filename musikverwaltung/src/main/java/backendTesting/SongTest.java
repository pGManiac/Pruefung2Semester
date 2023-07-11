package backendTesting;

import backend.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SongTest {
    private Song song;

    @BeforeEach
    void setUp() {
        song = new Song("Song Name", "Album Name", "Rock", "Artist Name", "song_path.mp3");
    }

    @Test
    void testConvertGenreStringToInt() {
        assertEquals(0, song.convertGenreStringToInt("Rock"));
        assertEquals(1, song.convertGenreStringToInt("Pop"));
        assertEquals(2, song.convertGenreStringToInt("Hip-Hop"));
        assertEquals(3, song.convertGenreStringToInt("Electronic"));
        assertEquals(4, song.convertGenreStringToInt("Indie"));
        assertEquals(5, song.convertGenreStringToInt("Classical"));
        assertEquals(6, song.convertGenreStringToInt("Metal"));
        assertEquals(-1, song.convertGenreStringToInt("UnknownGenre"));
    }

    @Test
    void testEquals() {
        Song sameSong = new Song("Song Name", "Album Name", "Rock", "Artist Name", "song_path.mp3");
        Song differentSong = new Song("Different Song", "Album Name", "Rock", "Artist Name", "song_path.mp3");

        assertTrue(song.equals(sameSong));
        assertFalse(song.equals(differentSong));
    }

    @Test
    void testHashCode() {
        Song sameSong = new Song("Song Name", "Album Name", "Rock", "Artist Name", "song_path.mp3");

        assertEquals(song.hashCode(), sameSong.hashCode());
    }
}
