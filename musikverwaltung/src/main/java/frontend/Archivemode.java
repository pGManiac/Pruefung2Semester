package frontend;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;

import backend.Database;
import backend.Song;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

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
    Dialog<Song> adder;
    ObservableList<String> options = FXCollections.observableArrayList("Rock", "Pop", "Hip-Hop", "Electronic", "Indie", "Classical", "Metal");
    FileChooser fileChooser = new FileChooser();
    Button swap;
    private Database data;
    Button saveExit;
    Scene scene;
    private Stage primaryStage;
    Dialog<Song> alert;

    public Archivemode(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /* public static void initiate() {
        launch();
    } */

    //@Override
    public Scene createScene() throws Exception {
        data = readObjectFromFile();
        //primaryStage.setTitle("Music Player");

        /* StackPane layout = new StackPane();
        layout.getChildren().add(button); */

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
        saveExit = new Button("Save and Exit");
        saveExit.setOnAction(e-> {saveAndExit(data);});
        Platform.runLater(() -> {saveExit.setPrefHeight(swap.getHeight());});

        //Menues
        genre = new Menu("_Genre"); //ermoeglicht shortkey durch Unterstrich (Anfangsbuchstabe)
        datei = new Menu("_Datei");
        datei.setId("datei");
        darstellung = new Menu("_Ansicht");
        hinzu = new MenuItem("Hinzufuegen");
        hinzu.setOnAction(e-> {einfuegen(primaryStage, lieder, data);});
        entfernen = new MenuItem("Entfernen");
        entfernen.setOnAction(e-> {loeschen(data, lieder);});
        az = new MenuItem("A-Z");
        az.setOnAction(e-> {alphabeticalSortA(data, lieder);});
        za = new MenuItem("Z-A");
        za.setOnAction(e-> {alphabeticalSortZ(data, lieder);});
        rock = new MenuItem("Rock");
        rock.setOnAction(e-> {genreSort(data, lieder, 0);});
        pop = new MenuItem("Pop");
        pop.setOnAction(e-> {genreSort(data, lieder, 1);});
        hipHop = new MenuItem("Hip-Hop");
        hipHop.setOnAction(e-> {genreSort(data, lieder, 2);});
        electronic = new MenuItem("Electronic");
        electronic.setOnAction(e-> {genreSort(data, lieder, 3);});
        indie = new MenuItem("Indie");
        indie.setOnAction(e-> {genreSort(data, lieder, 4);});
        classical = new MenuItem("Classical");
        classical.setOnAction(e-> {genreSort(data, lieder, 5);});
        metal = new MenuItem("Metal");
        metal.setOnAction(e-> {genreSort(data, lieder, 6);});
        datei.getItems().addAll(hinzu, entfernen);
        genre.getItems().addAll(rock, pop, hipHop, electronic, indie, classical, metal);
        darstellung.getItems().addAll(az, za, genre);
        menuBar = new MenuBar();
        menuBar.getMenus().add(datei);
        menuBar.getMenus().add(darstellung);

        //Tabelle
        lieder = new TableView<>();
        tableData = FXCollections.observableList(data.getSongHash().getAllSongs());
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
        scene = new Scene(border, 960, 600);
        scene.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());
        
        
        //primaryStage.setScene(scene);
        //primaryStage.show();

        return scene;

    }

    public void einfuegen(Stage stage, TableView<Song> v, Database database) {
        //Dialogfenster Hinzufuegen
        adder = new Dialog<>();
        adder.setTitle("Songeingabe");
        Label headerE = new Label("Geben Sie Details zu Ihrem Song ein:");
        headerE.setId("headerEinfuegen");
        adder.setResizable(true);
        DialogPane diaPane = adder.getDialogPane();
        diaPane.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());
        diaPane.getStyleClass().add("dialog");
        Label titel = new Label("Titel:");
        TextField titTField = new TextField();
        Label album = new Label("Album:");
        TextField albTField = new TextField();
        Label genre = new Label("Genre:");
        final ComboBox<String> genComboBox = new ComboBox<>(options);
        genComboBox.setMinWidth(300); //Achtung, bei Änderung der allgemeinen font-size auch anpassen!
        Label interpret = new Label("Interpret:");
        TextField interTField = new TextField();
        fileChooser.setTitle("MP3-Datei waehlen");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("MP3", "*.mp3"));
        Button create = new Button("Create!");
        create.setOnAction(e -> {createSong(stage, titTField, albTField, genComboBox, interTField, v, database);}); //durch create ist es moeglich mehrere Songs hintereinander zu adden
        
        //Layout
        GridPane grid = new GridPane();
        grid.getStyleClass().add("diaGrid");
        grid.add(titel, 1, 1);
        grid.add(album, 1, 2);
        grid.add(genre, 1, 3);
        grid.add(interpret, 1, 4);
        grid.add(titTField, 2, 1);
        grid.add(albTField, 2, 2);
        grid.add(genComboBox, 2, 3);
        grid.add(interTField, 2, 4);
        grid.add(create, 3, 7);
        ButtonType okButtonType = new ButtonType("Fertig", ButtonData.OK_DONE); //dieser Button ist notwendig zum Schliessen des Dialogs
        BorderPane diaBorder = new BorderPane();
        diaBorder.setTop(headerE);
        diaBorder.setCenter(grid);
        adder.getDialogPane().setContent(diaBorder);
        adder.getDialogPane().getButtonTypes().add(okButtonType);
        adder.showAndWait();
    }

    public void createSong(Stage stage, TextField t, TextField a, ComboBox<String> g, TextField i, TableView<Song> v, Database database) {
        String titleNew = t.getText();
        String albumNew = a.getText();
        String genreNew = g.getValue();
        String artistNew = i.getText();

        File selectedFile = fileChooser.showOpenDialog(stage); //root window stage cannot be accessed, while the dialog is open
        String destination = "src/main/java/frontend/lieder/";
        String id = new SimpleDateFormat("mm:ss:SSS").format(new java.util.Date());

        Path source = selectedFile.toPath();
        String targetString = destination + id + "_" + selectedFile.getName();
        Path target = Path.of(targetString);
        try {
            Files.copy(source, target, StandardCopyOption.COPY_ATTRIBUTES); //attributes of og file copied as well
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }

        Song songNew = new Song(titleNew, albumNew, genreNew, artistNew, targetString);
        //Song Objekte im Anschluss in .ser file schreiben und immer beim Öffnen der Applikation die .ser files lesen
        this.data.addSong(songNew);
        //database.addSong(songNew);
        ObservableList<Song> tableDataNew = FXCollections.observableList(this.data.getSongHash().getAllSongs());
        this.lieder.setItems(tableDataNew);
        //v.setItems(tableDataNew);
    }

    public void loeschen(Database database, TableView<Song> v) {
        //Dialogfenster entfernen
        alert = new Dialog<>();
        DialogPane alertPane = alert.getDialogPane();
        alertPane.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());
        alertPane.getStyleClass().add("alert");
        alert.setTitle("Loeschen bestaetigen");
        Label headerL = new Label("Ausgewaehlten Song loeschen?");
        headerL.setId("headerLoeschen");
        Button yes = new Button("Ja");
        Button no = new Button("Nein");
        Song picked = v.getSelectionModel().getSelectedItem();
        yes.setOnAction(e-> {deleteSong(database, picked, alert, v);});
        no.setOnAction(e-> {dialogClosing(alert);});

        //Layout
        HBox alertHBox = new HBox(50, yes, no);
        alertHBox.getStyleClass().add("alertHBox");
        BorderPane alertBorder = new BorderPane();
        alertBorder.setTop(headerL);
        alertBorder.setCenter(alertHBox);
        alert.getDialogPane().setContent(alertBorder);
        alert.showAndWait();
    }

    public void deleteSong(Database database, Song song, Dialog<Song> a, TableView<Song> v) {
        //database.removeSong(song);
        this.data.removeSong(song);
        ObservableList<Song> tableDataNew = FXCollections.observableList(this.data.getSongHash().getAllSongs());
        //v.setItems(tableDataNew);
        this.lieder.setItems(tableDataNew);
        dialogClosing(this.alert);
    }

    public void alphabeticalSortA(Database database, TableView<Song> v) {
        ObservableList<Song> sortedTableA = FXCollections.observableList(this.data.getSongHash().sortAToZ());
        //v.setItems(sortedTableA);
        this.lieder.setItems(sortedTableA);
    }

    public void alphabeticalSortZ(Database database, TableView<Song> v) {
        ObservableList<Song> sortedTableZ = FXCollections.observableList(this.data.getSongHash().sortZToA());
        //v.setItems(sortedTableZ);
        this.lieder.setItems(sortedTableZ);
    }

    public void genreSort(Database database, TableView<Song> v, int genreNumber) {
        ObservableList<Song> genreSorted = FXCollections.observableList(this.data.getSongHash().sortAToZByGenre(genreNumber));
        //v.setItems(genreSorted);
        this.lieder.setItems(genreSorted);
    }

    public void writeObjectToFile(Database database) {
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

    public Database readObjectFromFile() throws ClassNotFoundException {
        try (FileInputStream inputFile = new FileInputStream("songObjects.ser");
             ObjectInputStream inputObject = new ObjectInputStream(inputFile)) {
                Database dataRead = (Database) inputObject.readObject();
                return dataRead;
            }
        catch (IOException ioException) {
            System.err.println(ioException.getMessage());
            return new Database();
        }
    }

    public void saveAndExit(Database database) {
        File oldSer = new File("songObjects.ser");
        oldSer.delete();
        //writeObjectToFile(database);
        writeObjectToFile(this.data);
        System.exit(0);
    }

    public void dialogClosing(Dialog<Song> dialog) { //workaround, da dialog.close() einfach so nicht funktioniert
        dialog.setResult(null); //Dialog gibt nichts zurück, da wir das ja nur für entfernen nutzen, was nichts zurückgeben muss
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.close(); //anstatt des dialogs selbst, hole ich mir die stage, auf der der Dialog ist und schließe diese, womit auch der dialog weg ist
    }

    public Scene getScene() {
        return scene;
    }

    public void switchToDisplaymode() {
        try {
            Displaymode displaymode = new Displaymode();
            Scene displayScene = displaymode.createDisplayScene();

            Stage currentStage = (Stage) swap.getScene().getWindow();
            currentStage.setScene(displayScene);
            currentStage.setTitle("Darstellungsmodus");
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
