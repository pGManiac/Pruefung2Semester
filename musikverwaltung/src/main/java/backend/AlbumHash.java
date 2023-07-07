package backend;

import java.io.Serializable;
import java.util.*;

public class AlbumHash implements Serializable {
    private Map<String, List<Song>> albumMap;

    public AlbumHash() {
        albumMap = new HashMap<>();
    }

    public void addSong(Song song) {
        String key = song.getAlbum();

        // Check if the album already exists in the map
        if (albumMap.containsKey(key)) {
            List<Song> songsList = albumMap.get(key);
            songsList.add(song);
        } else {
            List<Song> songsList = new ArrayList<>();
            songsList.add(song);
            albumMap.put(key, songsList);
        }
    }

    public void removeSong(Song song) {
        String key = song.getAlbum();
        List<Song> songs = albumMap.get(key);

        if (songs != null) {
            Iterator<Song> iterator = songs.iterator();
            while (iterator.hasNext()) {
                Song comparedSong = iterator.next();
                if (song.equals(comparedSong)) {
                    iterator.remove();
                    // If the list is empty after removing the song, remove the key from the map
                    if (songs.isEmpty()) {
                        albumMap.remove(key);
                    }
                    return;
                }
            }
        }
    }

    public List<Song> getSongsFromAlbum(String album) {
        return albumMap.getOrDefault(album, null);
    }

    public List<Song> getOneSongPerAlbum() {
        List<Song> result = new ArrayList<>();
        Set<String> visitedAlbums = new HashSet<>();

        for (List<Song> albumSongs : albumMap.values()) {
            for (Song song : albumSongs) {
                String albumName = song.getAlbum();
                String artistName = song.getArtist();
                String albumKey = albumName + artistName;

                if (!visitedAlbums.contains(albumKey)) {
                    result.add(song);
                    visitedAlbums.add(albumKey);
                }
            }
        }
        return result;
    }

    public List<Song> getAllSongsFromAlbum(Song song) {
        String albumName = song.getAlbum();
        String artistName = song.getArtist();

        List<Song> songsFromAlbum = albumMap.get(albumName);
        List<Song> result = new ArrayList<>();

        if (songsFromAlbum != null) {
            for (Song albumSong : songsFromAlbum) {
                if (albumSong.getArtist().equals(artistName)) {
                    result.add(albumSong);
                }
            }
        }

        return result;
    }


    public Song getSong(String songName, String albumName, int genre, String artistName) {
        List<Song> songs = albumMap.get(albumName);
        if (songs != null) {
            for (Song song : songs) {
                if (Objects.equals(song.getName(), songName) && Objects.equals(song.getAlbum(), albumName) && (genre == song.getGenreNumber()) && Objects.equals(song.getArtist(), artistName)) {
                    return song;
                }
            }
        }
        return null;
    }
    public boolean containsSong(String songName, String albumName, int genre, String artistName) {
        List<Song> songs = albumMap.get(albumName);
        if (songs != null) {
            for (Song song : songs) {
                if (Objects.equals(song.getName(), songName) && Objects.equals(song.getAlbum(), albumName) && (genre == song.getGenreNumber()) && Objects.equals(song.getArtist(), artistName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsAlbum(String album) {
        return albumMap.containsKey(album);
    }

    public int size() {
        return albumMap.size();
    }
}