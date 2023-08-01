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

public class ChooseArtistGUI {
    private Database data;
    private TableView<Song> tableView;
    private TableColumn<Song, String> artistColumn;

    private MediaPlaylist mediaPlaylist;
    private Dialog<Song> adder;
    private ObservableList<Song> tableData;

    public ChooseArtistGUI(Database data, MediaPlaylist mediaPlaylist) {
        this.data = data;
        this.mediaPlaylist = mediaPlaylist;
    }

    // TODO: Works for now but will need getArtistHash():getEachArtist() or similar
    public void selectArtist() {
        // *** GUI ***
        tableView = new TableView<>();
        tableData = FXCollections.observableList(data.getAlbumHash().getOneSongPerAlbum());

        adder = new Dialog<>();
        adder.setTitle("Interpretenwahl");
        Label header = new Label("Wähle einen Interpreten!");
        header.setId("albumSelectionHeader");
        adder.setResizable(true);

        artistColumn = new TableColumn<>("Artist");
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        tableView.getColumns().add(artistColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setItems(tableData);

        DialogPane diaPane = adder.getDialogPane();
        diaPane.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());
        diaPane.getStyleClass().add("dialog");

        Button chooseArtistButton = new Button("Zur Wiedergabeliste hinzufügen");
        ButtonType okButtonType = new ButtonType("Fertig", ButtonBar.ButtonData.OK_DONE);
        BorderPane diaBorder = new BorderPane();

        StackPane stack = new StackPane(tableView);
        diaBorder.setCenter(stack);
        diaBorder.setTop(header);
        diaBorder.setBottom(chooseArtistButton);

        chooseArtistButton.setOnAction(e -> {
            Song selectedArtist = tableView.getSelectionModel().getSelectedItem();
            String artist = selectedArtist.getArtist();
            if (selectedArtist != null) {
                List<Song> selectedSongs = data.getArtistHash().getSongsFromArtist(artist);
                mediaPlaylist.stop();
                mediaPlaylist.setSongs(selectedSongs);
                mediaPlaylist.setStoredPlaybackPosition(null);
                // Create and show an information alert to display the notification
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Interpret hinzugefügt");
                alert.setHeaderText(null);
                alert.setContentText("Alle Songs des Interpreten '" + artist + "' wurden der Playlist hinzugefügt!");
                alert.showAndWait();
            }
        });

        adder.getDialogPane().setContent(diaBorder);
        adder.getDialogPane().getButtonTypes().add(okButtonType);
        adder.getDialogPane().setPrefSize(900, 800);
        adder.showAndWait();
    }
}

