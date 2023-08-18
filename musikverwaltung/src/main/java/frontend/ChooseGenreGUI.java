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
//import java.util.ArrayList;
import java.util.List;

/**
 * @brief The ChooseGenreGUI class represents a graphical user interface (GUI) for selecting genres and adding songs from each selected genre to the media playlist.
 *        It displays a dialog with a TableView containing a list of genres, allowing the user to select genres and add their songs to the playlist.
 */
public class ChooseGenreGUI {
    private Database data;
    private TableView<Song> tableview;
    private TableColumn<Song, String> genreColumn;
    private MediaPlaylist mediaPlaylist;
    private Dialog<Song> adder;
    private ObservableList<Song> tableData;

    /**
     * @brief Constructor for the ChooseGenreGUI class.
     *
     * @param data          The Database instance that provides genre and song data.
     * @param mediaPlaylist The MediaPlaylist instance for managing the playlist.
     */
    public ChooseGenreGUI(Database data, MediaPlaylist mediaPlaylist) {
        this.data = data;
        this.mediaPlaylist = mediaPlaylist;
    }

    /**
     * @brief Displays the GUI for selecting genres and adds songs from each selected genre to the media playlist.
     */
    public void selectGenre() {
        // *** GUI ***
        tableview = new TableView<>();
        tableData = FXCollections.observableList(data.getGenreHash().getOneSongFromEachRepresentedGenre());

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

        Button chooseAlbumButton = new Button("Zur Wiedergabeliste hinzufügen");
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
}