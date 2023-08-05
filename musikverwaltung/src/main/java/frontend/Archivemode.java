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
    ImageView img;
    Button button;
    MenuBar menuBar;
    Menu datei;
    Menu darstellung;
    MenuItem hinzu;
    MenuItem entfernen;
    MenuItem az;
    MenuItem za;
    Menu genre;
    MenuItem rock;
    MenuItem pop;
    MenuItem hipHop;
    MenuItem electronic;
    MenuItem indie;
    MenuItem classical;
    MenuItem metal;
    ObservableList<Song> tableData;
    TableView<Song> lieder;
    TableColumn<Song, String> spalte1;
    TableColumn<Song, String> spalte2;
    TableColumn<Song, String> spalte3;
    TableColumn<Song, String> spalte4;
    HBox hbox;
    StackPane stack;
    Button swap;
    private Database data;
    Button saveExit;
    Scene scene;
    private Stage primaryStage;
    private EinfuegenGUI einfuegenGUI;
    private LoeschenGUI loeschenGUI;

    public Archivemode(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    /**
     * @brief Creates the scene in archivemode and adds all necessary controls to it.
     * @return The created scene.
     * @implNote none
     * @note This method uses standard JavaFX controls and implements a css stylesheet.
     *       More detailed information on how the controls are implemented is noted directly
     *       in the method. For more details on how the controls work check official JavaFX documentation.
     * @see javafx.scene
     * @see javafx.application.Platform
     * @see javafx.collections
     */
    //@Override
    public Scene createScene() throws Exception {
        //data = database;

        BorderPane border = new BorderPane(); //algemeines Layout

        //Moduswechsel
        image = new Image("file:src/main/java/frontend/icons/mode11.png"); //es muss vornedran file: stehen
        img = new ImageView(image);
        swap = new Button("Musik");
        img.setFitWidth(swap.getWidth());
        img.setFitHeight(swap.getHeight()); //Button passt sich groesse des Bildes an
        swap.setGraphic(img);
        swap.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        swap.getStyleClass().add("swap");
        swap.setOnAction(e -> {
            // Methode zum Wechseln der Szene aufrufen
            switchToDisplaymode();
        });

        //Database speichern und beenden
        saveExit = new Button("Speichern und Beenden");
        saveExit.setOnAction(e-> {saveAndExit();});
        Platform.runLater(() -> {saveExit.setPrefHeight(swap.getHeight());});

        //Menues
        genre = new Menu("_Genre"); //ermoeglicht shortkey durch Unterstrich (Anfangsbuchstabe)
        datei = new Menu("_Datei");
        datei.setId("datei");
        darstellung = new Menu("_Ansicht");
        hinzu = new MenuItem("Hinzufügen");
        hinzu.setOnAction(e-> {einfuegenGUI = new EinfuegenGUI(this); einfuegenGUI.einfuegen();}); //kann man Konstruktor woanders aufrufen?
        entfernen = new MenuItem("Entfernen");
        entfernen.setOnAction(e-> {loeschenGUI = new LoeschenGUI(this); loeschenGUI.loeschen();});
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
        datei.getItems().addAll(hinzu, entfernen);
        genre.getItems().addAll(rock, pop, hipHop, electronic, indie, classical, metal);
        darstellung.getItems().addAll(az, za, genre);
        menuBar = new MenuBar();
        menuBar.getMenus().add(datei);
        menuBar.getMenus().add(darstellung);

        //Tabelle
        lieder = new TableView<>();
        tableData = FXCollections.observableList(this.data.getSongHash().getAllSongs());
        spalte1 = new TableColumn<Song, String>("Titel");
        spalte2 = new TableColumn<Song, String>("Album");
        spalte3 = new TableColumn<Song, String>("Genre");
        spalte4 = new TableColumn<Song, String>("Interpret");
        spalte1.setCellValueFactory(new PropertyValueFactory<>("name"));
        spalte2.setCellValueFactory(new PropertyValueFactory<>("album"));
        spalte3.setCellValueFactory(new PropertyValueFactory<>("genreName"));
        spalte4.setCellValueFactory(new PropertyValueFactory<>("artist"));
        lieder.getColumns().add(spalte1);
        lieder.getColumns().add(spalte2);
        lieder.getColumns().add(spalte3);
        lieder.getColumns().add(spalte4);
        lieder.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //Spalten passen sich window an
        lieder.setItems(tableData);

        //Layout
        hbox = new HBox(menuBar, swap, saveExit); //button und menubar in hbox, wobei menubar sich resizen darf
        HBox.setHgrow(menuBar, Priority.ALWAYS);
        HBox.setHgrow(swap, Priority.NEVER);
        HBox.setHgrow(saveExit, Priority.NEVER);
        border.setTop(hbox);
        stack = new StackPane(lieder); //stack pane should automatically create scroller
        border.setCenter(stack);
        scene = new Scene(border, 1100, 900);
        scene.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());

        return scene;

    }

    /**
     * @brief Sorts songs from a to z.
     * @return none
     * @implNote none
     * @note This method uses a database songHash method to get a List of all songs sorted from a to z.
     *       The List is then given to the TableView for display.
     * @see javafx.collections
     */

    public void alphabeticalSortA() {
        ObservableList<Song> sortedTableA = FXCollections.observableList(this.data.getSongHash().sortAToZ());
        //v.setItems(sortedTableA);
        this.lieder.setItems(sortedTableA);
    }

    /**
     * @brief Sorts songs from z to a.
     * @return none
     * @implNote none
     * @note This method uses a database songHash method to get a List of all songs sorted from z to a.
     *       The List is then given to the TableView for display.
     * @see javafx.collections
     */

    public void alphabeticalSortZ() {
        ObservableList<Song> sortedTableZ = FXCollections.observableList(this.data.getSongHash().sortZToA());
        //v.setItems(sortedTableZ);
        this.lieder.setItems(sortedTableZ);
    }

    /**
     * @brief Sorts songs after genre.
     * @param genreNumber the genre number representing a genre as specified in the song class
     * @return none
     * @implNote none
     * @note This method uses a database songHash method to get a List of all songs sorted after genre.
     *       The method needs the genre number to know which genre to sort after.
     *       The List is then given to the TableView for display.
     * @see javafx.collections
     */

    public void genreSort(int genreNumber) {
        ObservableList<Song> genreSorted = FXCollections.observableList(this.data.getSongHash().sortAToZByGenre(genreNumber));
        //v.setItems(genreSorted);
        this.lieder.setItems(genreSorted);
    }

    /**
     * @brief Serializes database object and writes it to .ser file.
     * @return none
     * @implNote the method uses try-with block which initializes outputstream objects. Using this guarantees for
     *           the streams to be closed, when the program exits the try-with block.
     * @note Using outputstreams writes the provided database object to a .ser file and saves it in the current
     *       working directory. An exception is thrown, if issues with output are met.
     * @see java.io
     */

    public void writeObjectToFile() {
        //try-with initialisiert die Streams in den runden Klammern, damit sie automatisch geschlossen werden, sobal man den try Block verlässt
        try (FileOutputStream outputFile = new FileOutputStream("songObjects.ser", true);
             ObjectOutputStream outputObject = new ObjectOutputStream(new BufferedOutputStream(outputFile))) {
                //outputObject.writeObject(database);
                outputObject.writeObject(this.data);
             }
        catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }
    }


    /**
     * @brief Closes the application and calls writeObjectToFile() method to save database.
     * @return none
     * @implNote none
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
     * @param dialog the dialog to be closed
     * @return none
     * @implNote none
     * @note Instead of closing the dialog directly, the stage on which the dialog takes place is closed. This method is
     *       needed to create custom buttons that can close a dialog for example, since a regular dialog.close() won't work.
     * @see javafx.stage
     */
    public void dialogClosing(Dialog<Song> dialog) { //workaround, da dialog.close() einfach so nicht funktioniert
        dialog.setResult(null); //Dialog gibt nichts zurück, da wir das ja nur für entfernen nutzen, was nichts zurückgeben muss
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.close(); //anstatt des dialogs selbst, hole ich mir die stage, auf der der Dialog ist und schließe diese, womit auch der dialog weg ist
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
        return this.lieder;
    }

    public Stage getStage() {
        return this.primaryStage;
    }

    /**
     * @brief A method to switch to display mode.
     * @return none
     * @implNote none
     * @note An object for displaymode is created and parsed to a new scene, which is then given to
     *       the new stage. The looks and methods of a displaymode object is specified in the
     *       respective class.
     * @see javafx.stage
     */

    public void switchToDisplaymode() {
        try {
            Displaymode displaymode = new Displaymode();
            displaymode.start(primaryStage);
            displaymode.setDatabase(Database.getInstance()); //set database before creating a scene!
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
