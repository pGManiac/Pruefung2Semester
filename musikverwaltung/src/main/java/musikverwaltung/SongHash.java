package musikverwaltung;

import java.util.*;



public class SongHash {
    private Map<String, Song> songMap;

    public SongHash() {
        songMap = new HashMap<>();
    }

    public void addSong(Song song) {
        String key = song.getName();
        songMap.put(key, song);
    }

    public void removeSong(String name) {
        songMap.remove(name);
    }

    public Song getSong(String name) {
        return songMap.get(name);
    }

    public boolean containsSong(String name) {
        return songMap.containsKey(name);
    }

    public int size() {
        return songMap.size();
    }

    public List<Song> sortAToZ() {
        List<Song> sortedSongs = new ArrayList<>(songMap.values());
        Collections.sort(sortedSongs, new Comparator<Song>() {
            public int compare(Song song1, Song song2) {
                return song1.getName().compareTo(song2.getName());
            }
        });
        return sortedSongs;
    }

    public List<Song> sortZToA() {
        List<Song> sortedSongs = new ArrayList<>(songMap.values());
        Collections.sort(sortedSongs, new Comparator<Song>() {
            public int compare(Song song1, Song song2) {
                return song2.getName().compareTo(song1.getName());
            }
        });

        return sortedSongs;
    }

    public List<Song> sortAToZByGenre(int genre) {
        List<Song> songsByGenre = new ArrayList<>();
        for (Song song : songMap.values()) {
            if (song.getGenre() == genre) {
                songsByGenre.add(song);
            }
        }
        Collections.sort(songsByGenre, new Comparator<Song>() {
            public int compare(Song song1, Song song2) {
                return song1.getName().compareTo(song2.getName());
            }
        });
        return songsByGenre;
    }
}