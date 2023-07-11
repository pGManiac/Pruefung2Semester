package backend;

import java.io.Serializable;
import java.util.*;

/**
 * @brief The AlbumHash class represents a hash table that stores songs based on their albums.
 *        It provides methods for adding, removing, and retrieving songs based on their albums.
 * @see Song
 */
public class AlbumHash implements Serializable {
    private Map<String, List<Song>> albumMap;

    /**
     * @brief Constructs a new AlbumHash object.
     *        Initializes the albumMap as a new HashMap to store songs based on their album names.
     */
    public AlbumHash() {
        albumMap = new HashMap<>();
    }

    /**
     * @brief Adds a song to the hash table based on its album name.
     * @param song The song to be added.
     */
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

    /**
     * @brief Removes a song from the hash table based on its album name.
     * @param song The song to be removed.
     */
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

    /**
     * @brief Retrieves the list of songs from a specific album.
     * @param album The album name.
     * @return The list of songs from the specified album, or null if the album doesn't exist in the map.
     */
    public List<Song> getSongsFromAlbum(String album) {
        return albumMap.getOrDefault(album, null);
    }

    /**
     * @brief Retrieves one song per album, avoiding duplicates.
     * @return A list of songs with one song per album.
     */
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

    /**
     * @brief Retrieves all songs from an album by a specific artist.
     * @param song A sample song from the album containing the album name and artist.
     * @return The list of songs from the specified album and artist, or an empty list if no match is found.
     */
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


    /**
     * @brief Retrieves a specific song based on its name, album, genre, and artist.
     * @param songName The name of the song.
     * @param albumName The name of the album.
     * @param genre The genre number of the song.
     * @param artistName The name of the artist.
     * @return The song object if found, or null if no match is found.
     */
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

    /**
     * @brief Checks if a specific song exists in the hash table based on its name, album, genre, and artist.
     * @param songName The name of the song.
     * @param albumName The name of the album.
     * @param genre The genre number of the song.
     * @param artistName The name of the artist.
     * @return true if the song exists, false otherwise.
     */
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

    /**
     * @brief Checks if a specific album exists in the hash table.
     * @param album The album name.
     * @return true if the album exists, false otherwise.
     */
    public boolean containsAlbum(String album) {
        return albumMap.containsKey(album);
    }

    /**
     * @brief Retrieves the number of albums in the hash table.
     * @return The number of albums.
     */
    public int size() {
        return albumMap.size();
    }
}