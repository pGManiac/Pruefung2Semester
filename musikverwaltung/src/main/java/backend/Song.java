package backend;

public class Song {
    private String name;
    private String album;
    private int genre;
    private String artist;

    private String mp3Path;

    public Song(String name, String album, int genre, String artist, String mp3Path) {
        this.name = name;
        this.album = album;
        this.genre = genre;
        this.artist = artist;
        this.artist = mp3Path;
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

    public String getMp3Path() { return mp3Path;}

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

    public void setMp3Path(String mp3Path) { this.mp3Path = mp3Path;}
}