package frontend;
import backend.Database;
import backend.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.*;
import java.util.List;

public class GenreGUI {
    private Database data;
    private TableView<Song> albums;
    private TableColumn<Song, String> albumColumn;
    private TableColumn<Song, String> artistColumn;

    private MediaPlaylist mediaPlaylist;
    private Dialog<Song> adder;
    private ObservableList<Song> tableData;

    public GenreGUI(Database data, MediaPlaylist mediaPlaylist) {
        this.data = data;
        this.mediaPlaylist = mediaPlaylist;
    }

    public void selectGenre() {
        // *** GUI ***
        albums = new TableView<>();
        tableData = FXCollections.observableList(data.getAlbumHash().getOneSongPerAlbum());

        adder = new Dialog<>();
        adder.setTitle("Albumwahl");
        Label header = new Label("Wähle ein Album!");
        header.setId("albumSelectionHeader");
        adder.setResizable(true);

        albumColumn = new TableColumn<>("Album");
        artistColumn = new TableColumn<>("Artist");
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        albums.getColumns().add(albumColumn);
        albums.getColumns().add(artistColumn);
        albums.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        albums.setItems(tableData);

        DialogPane diaPane = adder.getDialogPane();
        diaPane.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());
        diaPane.getStyleClass().add("dialog");

        Button chooseAlbumButton = new Button("Zur Playlist hinzufügen");
        ButtonType okButtonType = new ButtonType("Fertig", ButtonBar.ButtonData.OK_DONE);
        BorderPane diaBorder = new BorderPane();

        StackPane stack = new StackPane(albums);
        diaBorder.setCenter(stack);
        diaBorder.setTop(header);
        diaBorder.setBottom(chooseAlbumButton);

        chooseAlbumButton.setOnAction(e -> {
            Song selectedAlbum = albums.getSelectionModel().getSelectedItem();
            if (selectedAlbum != null) {
                List<Song> selectedSongs = data.getAlbumHash().getAllSongsFromAlbum(selectedAlbum);
                mediaPlaylist.setSongs(selectedSongs);
                // Create and show an information alert to display the notification
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Album hinzugefügt");
                alert.setHeaderText(null);
                alert.setContentText("Das Album '" + selectedAlbum.getAlbum() + "' wurde der Playlist hinzugefügt!");
                alert.showAndWait();
            }
        });

        adder.getDialogPane().setContent(diaBorder);
        adder.getDialogPane().getButtonTypes().add(okButtonType);
        adder.getDialogPane().setPrefSize(900, 800);
        adder.showAndWait();
    }
}