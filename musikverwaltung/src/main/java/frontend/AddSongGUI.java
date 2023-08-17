package frontend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;

import backend.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class AddSongGUI {
    private Dialog<Song> adder;
    private ObservableList<String> options = FXCollections.observableArrayList("Rock", "Pop", "Hip-Hop", "Electronic", "Indie", "Classical", "Metal");
    private FileChooser fileChooser = new FileChooser();
    private TextField titTField, albTField, interTField;
    private ComboBox<String> genComboBox;
    private Archivemode master;

    public AddSongGUI(Archivemode masterArchivemode) {
        this.master = masterArchivemode;
    }

    /**
     * @brief Creates the dialog needed to add a new song to the database.
     * 
     * @note This method uses standard JavaFX controls and implements a css stylesheet.
     *       For more details on how the controls work check official JavaFX documentation.
     * @see javafx.scene
     */

    public void addSong() {
        //dialog window
        adder = new Dialog<>();
        adder.setTitle("Songeingabe");
        Label headerE = new Label("Geben Sie Details zu Ihrem Song ein:");
        headerE.setId("headerEinfuegen");
        adder.setResizable(true);
        DialogPane diaPane = adder.getDialogPane();
        diaPane.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());
        diaPane.getStyleClass().add("dialog");
        Label title = new Label("Titel:");
        titTField = new TextField();
        Label album = new Label("Album:");
        albTField = new TextField();
        Label genre = new Label("Genre:");
        genComboBox = new ComboBox<>(options);
        genComboBox.setMinWidth(300); //Achtung, bei Änderung der allgemeinen font-size auch anpassen!
        Label interpret = new Label("Interpret:");
        interTField = new TextField();
        fileChooser.setTitle("MP3-Datei waehlen");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("MP3", "*.mp3"));
        Button create = new Button("Datei auswählen");
        create.setOnAction(e -> {createSong();});
        Button cancel = new Button("Abbrechen");
        cancel.setOnAction(e -> {master.dialogClosing(this.adder);});
        
        //layout
        GridPane grid = new GridPane();
        grid.getStyleClass().add("diaGrid");
        grid.add(title, 1, 1);
        grid.add(album, 1, 2);
        grid.add(genre, 1, 3);
        grid.add(interpret, 1, 4);
        grid.add(titTField, 2, 1);
        grid.add(albTField, 2, 2);
        grid.add(genComboBox, 2, 3);
        grid.add(interTField, 2, 4);
        grid.add(create, 1, 7);
        grid.add(cancel, 3, 7);
        BorderPane diaBorder = new BorderPane();
        diaBorder.setTop(headerE);
        diaBorder.setCenter(grid);
        adder.getDialogPane().setContent(diaBorder);
        
        // set window size
        adder.setWidth(800);
        adder.setHeight(600);
        adder.showAndWait();
    }

    /**
     * @brief Opens file chooser and copies selected file to program directory.
     * 
     * @return the path to the created file copy as a String
     * @implNote the Files class copy() function is used to copy files.
     * @note The method opens a file chooser. While this is open the primary stage cannot be accessed. To avoid
     *       file copies with the same name, a unique identifier (time stamp) is added to file name.
     * @see java.nio.file
     */

    public String copyFile() {
    	//root window stage cannot be accessed, while the file chooser is open
        File selectedFile = fileChooser.showOpenDialog(master.getStage());
        String destination = "src/main/java/frontend/lieder/";
        String id = new SimpleDateFormat("MM_ss_SSS").format(new java.util.Date());

        Path source = selectedFile.toPath();
        String targetString = destination + id + "_" + selectedFile.getName();
        Path target = Path.of(targetString);
        try {
            Files.copy(source, target, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }

        return targetString;
    }

    /**
     * @brief Reads user input, creates new song object and adds it to database.
     * 
     * @note The method reads the dialog control values and calls the copyFile() function. All values are passed to
     *       song object constructor, which is added to the database. Since the TableView always displays a List,
     *       a new List containing the new song object(s) replaces the old TableView List.
     * @see javafx.collections
     */

     public void createSong() {
        String titleNew = this.titTField.getText();
        String albumNew = this.albTField.getText();
        String genreNew = this.genComboBox.getValue();
        String artistNew = this.interTField.getText();

        String targetString = copyFile();
        
        Song songNew = new Song(titleNew, albumNew, genreNew, artistNew, targetString);
        master.getDatabase().addSong(songNew);
        ObservableList<Song> tableDataNew = FXCollections.observableList(master.getDatabase().getSongHash().getAllSongs());
        master.getTable().setItems(tableDataNew);

        master.dialogClosing(this.adder);
    }
}
