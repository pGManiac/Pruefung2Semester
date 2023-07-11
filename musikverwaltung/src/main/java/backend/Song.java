package backend;
import java.io.Serializable;
import java.util.Objects;

/**
 * @brief The Song class represents a song with its attributes such as name, album, genre, artist, and file path.
 *        It provides methods for converting genre strings to genre numbers, and overrides equals() and hashCode() methods for comparison.
 */
public class Song implements Serializable {
    private String name;
    private String album;
    private int genreNumber;

    private String genreName;
    private String artist;
    private String mp3Path;


    /**
     * @brief Constructs a Song object with the specified attributes.
     *
     * @param name       The name of the song.
     * @param album      The album the song belongs to.
     * @param genreName  The genre of the song.
     * @param artist     The artist who performed the song.
     * @param mp3Path    The file path of the song.
     */
    public Song(String name, String album, String genreName, String artist, String mp3Path) {
        this.name = name;
        this.album = album;
        this.genreNumber = convertGenreStringToInt(genreName);
        this.genreName = genreName;
        this.artist = artist;
        this.mp3Path = mp3Path;
    }

    /**
     * @brief Converts the genre name to the corresponding genre number.
     *
     * @param genreName The name of the genre.
     * @return The genre number corresponding to the given genre name.
     */
    public int convertGenreStringToInt(String genreName) {
        int genreNumber;

        switch (genreName) {
            case "Rock":
                genreNumber = 0;
                break;
            case "Pop":
                genreNumber = 1;
                break;
            case "Hip-Hop":
                genreNumber = 2;
                break;
            case "Electronic":
                genreNumber = 3;
                break;
            case "Indie":
                genreNumber = 4;
                break;
            case "Classical":
                genreNumber = 5;
                break;
            case "Metal":
                genreNumber = 6;
                break;
            default:
                genreNumber = -1; // Default value if the genre name doesn't match any case
                break;
        }
        return genreNumber;
    }

    /**
     * @brief Compares this Song object with the specified object for equality.
     *
     * @param obj The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Song song = (Song) obj;
        return genreNumber == song.genreNumber &&
                Objects.equals(name, song.name) &&
                Objects.equals(album, song.album) &&
                Objects.equals(artist, song.artist) &&
                Objects.equals(mp3Path, song.mp3Path);
    }

    /**
     * @brief Generates a hash code for this Song object.
     *
     * @return The hash code value for this Song object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, album, genreNumber, artist, mp3Path);
    }

    public String getName() {
        return name;
    }

    public String getAlbum() {
        return album;
    }

    public int getGenreNumber() {
        return genreNumber;
    }

    public String getGenreName() { return genreName;}

    public String getArtist() {
        return artist;
    }

    public String getMp3Path() { return mp3Path;}

    public void setName(String name) {
        this.name = name;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setGenreNumber(int genreNumber) {
        this.genreNumber = genreNumber;
    }

    public void setGenreName(String genreName) { this.genreName = genreName;}

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setMp3Path(String mp3Path) { this.mp3Path = mp3Path;}
}