package frontend;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import backend.Database;
import backend.Song;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Archivemode {
    
    BorderPane border;
    Image image;
    ImageView imgV;
    Button button, swap, saveExit;
    MenuBar menuBar;
    Menu file, view, genre;
    MenuItem add, delete, az, za;
    MenuItem rock, pop, hipHop, electronic, indie, classical, metal;
    ObservableList<Song> tableData;
    TableView<Song> songs;
    TableColumn<Song, String> col1, col2, col3, col4;
    HBox hbox;
    StackPane stack;
    private Database data;
    Scene scene;
    private Stage primaryStage;
    private AddSongGUI addSongGUI;
    private DeleteSongGUI deleteSongGUI;

    public Archivemode(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    /**
     * @brief Creates the scene in archive mode and adds all necessary controls to it.
     * 
     * @return The created scene.
     * @note This method uses standard JavaFX controls and implements a css style sheet.
     *       More detailed information on how the controls are implemented is noted directly
     *       in the method. For more details on how the controls work check official JavaFX documentation.
     * @see javafx.scene
     * @see javafx.application.Platform
     * @see javafx.collections
     */
    //@Override
    public Scene createScene() throws Exception {

        BorderPane border = new BorderPane();

        //switch modes
        image = new Image("file:src/main/java/frontend/icons/mode11.png");
        imgV = new ImageView(image);
        swap = new Button("Musik");
        imgV.setFitWidth(swap.getWidth());
        imgV.setFitHeight(swap.getHeight());
        swap.setGraphic(imgV);
        swap.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        swap.getStyleClass().add("swap");
        swap.setOnAction(e -> {switchToDisplaymode();});

        //save database and exit program
        saveExit = new Button("Speichern und Beenden");
        saveExit.setOnAction(e-> {saveAndExit();});
        Platform.runLater(() -> {saveExit.setPrefHeight(swap.getHeight());});

        //menus
        genre = new Menu("_Genre");
        file = new Menu("_Datei");
        file.setId("datei");
        view = new Menu("_Ansicht");
        add = new MenuItem("Hinzufügen");
        add.setOnAction(e-> {addSongGUI = new AddSongGUI(this); addSongGUI.addSong();}); //kann man Konstruktor woanders aufrufen?
        delete = new MenuItem("Entfernen");
        delete.setOnAction(e-> {deleteSongGUI = new DeleteSongGUI(this); deleteSongGUI.deleteSong();});
        az = new MenuItem("A-Z");
        az.setOnAction(e-> {alphabeticalSortA();});
        za = new MenuItem("Z-A");
        za.setOnAction(e-> {alphabeticalSortZ();});
        rock = new MenuItem("Rock");
        rock.setOnAction(e-> {genreSort(0);});
        pop = new MenuItem("Pop");
        pop.setOnAction(e-> {genreSort(1);});
        hipHop = new MenuItem("Hip-Hop");
        hipHop.setOnAction(e-> {genreSort(2);});
        electronic = new MenuItem("Electronic");
        electronic.setOnAction(e-> {genreSort(3);});
        indie = new MenuItem("Indie");
        indie.setOnAction(e-> {genreSort(4);});
        classical = new MenuItem("Classical");
        classical.setOnAction(e-> {genreSort(5);});
        metal = new MenuItem("Metal");
        metal.setOnAction(e-> {genreSort(6);});
        file.getItems().addAll(add, delete);
        genre.getItems().addAll(rock, pop, hipHop, electronic, indie, classical, metal);
        view.getItems().addAll(az, za, genre);
        menuBar = new MenuBar();
        menuBar.getMenus().add(file);
        menuBar.getMenus().add(view);

        //table
        songs = new TableView<>();
        tableData = FXCollections.observableList(this.data.getSongHash().getAllSongs());
        col1 = new TableColumn<Song, String>("Titel");
        col2 = new TableColumn<Song, String>("Album");
        col3 = new TableColumn<Song, String>("Genre");
        col4 = new TableColumn<Song, String>("Interpret");
        col1.setCellValueFactory(new PropertyValueFactory<>("name"));
        col2.setCellValueFactory(new PropertyValueFactory<>("album"));
        col3.setCellValueFactory(new PropertyValueFactory<>("genreName"));
        col4.setCellValueFactory(new PropertyValueFactory<>("artist"));
        songs.getColumns().add(col1);
        songs.getColumns().add(col2);
        songs.getColumns().add(col3);
        songs.getColumns().add(col4);
        songs.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        songs.setItems(tableData);

        //layout
        hbox = new HBox(menuBar, swap, saveExit);
        HBox.setHgrow(menuBar, Priority.ALWAYS);
        HBox.setHgrow(swap, Priority.NEVER);
        HBox.setHgrow(saveExit, Priority.NEVER);
        border.setTop(hbox);
        stack = new StackPane(songs);
        border.setCenter(stack);
        scene = new Scene(border, 1100, 900);
        scene.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());

        return scene;

    }

    /**
     * @brief Sorts songs from a to z.
     * 
     * @note This method uses a database songHash method to get a List of all songs sorted from a to z.
     *       The List is then given to the TableView for display.
     * @see javafx.collections
     */

    public void alphabeticalSortA() {
        ObservableList<Song> sortedTableA = FXCollections.observableList(this.data.getSongHash().sortAToZ());
        this.songs.setItems(sortedTableA);
    }

    /**
     * @brief Sorts songs from z to a.
     * 
     * @note This method uses a database songHash method to get a List of all songs sorted from z to a.
     *       The List is then given to the TableView for display.
     * @see javafx.collections
     */

    public void alphabeticalSortZ() {
        ObservableList<Song> sortedTableZ = FXCollections.observableList(this.data.getSongHash().sortZToA());
        this.songs.setItems(sortedTableZ);
    }

    /**
     * @brief Sorts songs after genre.
     * 
     * @param genreNumber the genre number representing a genre as specified in the song class
     * @note This method uses a database songHash method to get a List of all songs sorted after genre.
     *       The method needs the genre number to know which genre to sort after.
     *       The List is then given to the TableView for display.
     * @see javafx.collections
     */

    public void genreSort(int genreNumber) {
        ObservableList<Song> genreSorted = FXCollections.observableList(this.data.getSongHash().sortAToZByGenre(genreNumber));
        this.songs.setItems(genreSorted);
    }

    /**
     * @brief Serializes database object and writes it to .ser file.
     * 
     * @implNote the method uses try-with block which initializes output stream objects. Using this guarantees for
     *           the streams to be closed, when the program exits the try-with block.
     * @note Using output streams writes the provided database object to a .ser file and saves it in the current
     *       working directory. An exception is thrown, if issues with output are met.
     * @see java.io
     */

    public void writeObjectToFile() {
        //try-with initializes streams in round brackets, so they'll get closed right after leaving the block
        try (FileOutputStream outputFile = new FileOutputStream("songObjects.ser", true);
             ObjectOutputStream outputObject = new ObjectOutputStream(new BufferedOutputStream(outputFile))) {
                outputObject.writeObject(this.data);
             }
        catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }
    }


    /**
     * @brief Closes the application and calls writeObjectToFile() method to save database.
     * 
     * @note The old .ser file is deleted before a new .ser file is created and saved.
     * @see java.io
     */

    public void saveAndExit() {
        File oldSer = new File("songObjects.ser");
        oldSer.delete();
        writeObjectToFile();
        System.exit(0);
    }

    /**
     * @brief A method to close a dialog.
     * 
     * @param dialog the dialog to be closed
     * @implNote Dialog result set to null, since delete dialog doesn't need to return anything. Instead
     * 			 of closing the dialog itself the root stage is closed instead, therefore closing the dialog as well.
     * @note Instead of closing the dialog directly, the stage on which the dialog takes place is closed. This method is
     *       needed to create custom buttons that can close a dialog, for example.
     * @see javafx.stage
     */
    
    public void dialogClosing(Dialog<Song> dialog) {
        dialog.setResult(null);
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.close();
    }

    public Scene getScene() {
        return scene;
    }

    public void setDatabase(Database database) {
        this.data = database;
    }

    public Database getDatabase() {
        return this.data;
    }

    public TableView<Song> getTable() {
        return this.songs;
    }

    public Stage getStage() {
        return this.primaryStage;
    }

    /**
     * @brief A method to switch to display mode.
     * 
     * @note An object for display mode is created and parsed to a new scene, which is then given to
     *       the new stage. The looks and methods of a display mode object is specified in the
     *       respective class.
     * @see javafx.stage
     */

    public void switchToDisplaymode() {
        try {
            Displaymode displaymode = new Displaymode();
            displaymode.start(primaryStage);
            displaymode.setDatabase(Database.getInstance()); //set database before creating a scene
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
