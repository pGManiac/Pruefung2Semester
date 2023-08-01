package frontend;
import backend.Database;
import backend.GenreHash;
import backend.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChooseGenreGUI {
    private Database data;
    private TableView<Song> tableview;
    private TableColumn<Song, String> genreColumn;
    private MediaPlaylist mediaPlaylist;
    private Dialog<Song> adder;
    private ObservableList<Song> tableData;

    public ChooseGenreGUI(Database data, MediaPlaylist mediaPlaylist) {
        this.data = data;
        this.mediaPlaylist = mediaPlaylist;
    }

    public void selectGenre() {
        // *** GUI ***
        tableview = new TableView<>();
        tableData = FXCollections.observableList(this.getOneSongFromEachRepresentedGenre());

        adder = new Dialog<>();
        adder.setTitle("Genrewahl");
        Label header = new Label("Wähle ein Genre aus!");
        header.setId("genreSelectionHeader");
        adder.setResizable(true);

        genreColumn = new TableColumn<>("Genres");
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genreName"));
        tableview.getColumns().add(genreColumn);
        tableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableview.setItems(tableData);

        DialogPane diaPane = adder.getDialogPane();
        diaPane.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());
        diaPane.getStyleClass().add("dialog");

        Button chooseAlbumButton = new Button("Zur Playlist hinzufügen");
        ButtonType okButtonType = new ButtonType("Fertig", ButtonBar.ButtonData.OK_DONE);
        BorderPane diaBorder = new BorderPane();

        StackPane stack = new StackPane(tableview);
        diaBorder.setCenter(stack);
        diaBorder.setTop(header);
        diaBorder.setBottom(chooseAlbumButton);

        chooseAlbumButton.setOnAction(e -> {
            Song selectedGenre = tableview.getSelectionModel().getSelectedItem();
            if (selectedGenre != null) {
                List<Song> selectedSongs = data.getGenreHash().getSongsFromGenre(selectedGenre.getGenreNumber());
                mediaPlaylist.setSongs(selectedSongs);
                // Create and show an information alert to display the notification
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Genre hinzugefügt");
                alert.setHeaderText(null);
                alert.setContentText("Alle Songs des Genres '" + selectedGenre.getAlbum() + "' wurden der Playlist hinzugefügt!");
                alert.showAndWait();
            }
        });

        adder.getDialogPane().setContent(diaBorder);
        adder.getDialogPane().getButtonTypes().add(okButtonType);
        adder.getDialogPane().setPrefSize(900, 800);
        adder.showAndWait();
    }

    private List<Song> getOneSongFromEachRepresentedGenre() {
        boolean[] usedGenreList = data.getGenreHash().genresRepresented();
        List<Song> oneSongFromEachGenre = new ArrayList<>();

        for (int i = 0; i <= 6; i++) {
            if (usedGenreList[i]) {
                // adds the first Song in the List of Songs from Genre to the list
                oneSongFromEachGenre.add(data.getGenreHash().getSongsFromGenre(i).get(0));
            }
        }
        return oneSongFromEachGenre;
    }
}