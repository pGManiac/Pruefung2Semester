package backend;
import java.io.Serializable;
import java.util.Objects;

public class Song implements Serializable {
    private String name;
    private String album;
    private int genreNumber;

    private String genreName;
    private String artist;
    private String mp3Path;


    public Song(String name, String album, String genreName, String artist, String mp3Path) {
        this.name = name;
        this.album = album;
        this.genreNumber = convertGenreStringToInt(genreName);
        this.genreName = genreName;
        this.artist = artist;
        this.mp3Path = mp3Path;
    }
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