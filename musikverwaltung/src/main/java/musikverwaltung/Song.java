package musikverwaltung;

public class Song {
    private String name;
    private String album;
    private int genre;
    private String artist;

    public Song(String name, String album, int genre, String artist) {
        this.name = name;
        this.album = album;
        this.genre = genre;
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public String getAlbum() {
        return album;
    }

    public int getGenre() {
        return genre;
    }

    public String getArtist() {
        return artist;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}