package frontend;

import backend.Database;
import backend.Song;
import javafx.collections.ObservableList;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AddAllSongsGUI {
    private Database data;
    private TableView<Song> albums;
    private TableColumn<Song, String> albumColumn;
    private TableColumn<Song, String> artistColumn;

    private MediaPlaylist mediaPlaylist;
    private Dialog<Song> adder;
    private ObservableList<Song> tableData;

    public AddAllSongsGUI(Database data, MediaPlaylist mediaPlaylist) {
        this.data = data;
        this.mediaPlaylist = mediaPlaylist;
    }

    public void addAllSongs() {

    }
}
