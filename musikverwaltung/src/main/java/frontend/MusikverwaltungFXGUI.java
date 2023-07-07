package frontend;

import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;

import backend.Database;
import backend.Song;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.css.*;

public class MusikverwaltungFXGUI extends Application{
    
    Button button;
    MenuBar menuBar;
    Menu datei;
    Menu darstellung;
    MenuItem hinzu;
    MenuItem entfernen;
    MenuItem az;
    MenuItem za;
    Menu genre;
    MenuItem metal;
    MenuItem pop;
    MenuItem rock;
    MenuItem klassik;
    MenuItem country;
    TableView<Song> lieder;
    ScrollPane scroller;
    Dialog<Song> adder;
    ObservableList<String> options = FXCollections.observableArrayList("Metal", "Pop", "Rock", "Klassik", "Country");
    FileChooser fileChooser = new FileChooser();
    Button swap;
    private Database data;
    Button exit;
    Scene scene;

    public static void initiate() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        data = readObjectFromFile();
        primaryStage.setTitle("Music Player");

        /* StackPane layout = new StackPane();
        layout.getChildren().add(button); */

        BorderPane border = new BorderPane(); //algemeines Layout

        //Moduswechsel
        Image image = new Image("file:src/main/java/frontend/icons/mode11.png"); //es muss vornedran file: stehen
        ImageView img = new ImageView(image);
        swap = new Button("Musik");
        img.setFitWidth(swap.getWidth());
        img.setFitHeight(swap.getHeight()); //Button passt sich groesse des Bildes an
        swap.setGraphic(img);
        swap.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        swap.getStyleClass().add("swap");

        //Database speichern und beenden
        exit = new Button("Save and Exit");
        exit.setOnAction(e-> {saveAndExit(data);});
        Platform.runLater(() -> {exit.setPrefHeight(swap.getHeight());});

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
        metal = new MenuItem("Metal");
        pop = new MenuItem("Pop");
        rock = new MenuItem("Rock");
        klassik = new MenuItem("Klassik");
        country = new MenuItem("Country");

        datei.getItems().addAll(hinzu, entfernen);
        genre.getItems().addAll(metal, pop, rock, klassik, country);
        darstellung.getItems().addAll(az, za, genre);

        menuBar = new MenuBar();
        
        menuBar.getMenus().add(datei);
        menuBar.getMenus().add(darstellung);

        //Tabelle
        lieder = new TableView<>();
        ObservableList<Song> tableData = FXCollections.observableList(data.getSongHash().getAllSongs());
        TableColumn<Song, String> spalte1 = new TableColumn<Song, String>("Titel");
        TableColumn<Song, String> spalte2 = new TableColumn<Song, String>("Album");
        TableColumn<Song, Integer> spalte3 = new TableColumn<Song, Integer>("Genre");
        TableColumn<Song, String> spalte4 = new TableColumn<Song, String>("Interpret");
        spalte1.setCellValueFactory(new PropertyValueFactory<>("name"));
        spalte2.setCellValueFactory(new PropertyValueFactory<>("album"));
        spalte3.setCellValueFactory(new PropertyValueFactory<>("genre"));
        spalte4.setCellValueFactory(new PropertyValueFactory<>("artist"));
        lieder.getColumns().add(spalte1);
        lieder.getColumns().add(spalte2);
        lieder.getColumns().add(spalte3);
        lieder.getColumns().add(spalte4);
        lieder.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //Spalten passen sich window an
        lieder.setItems(tableData);
        //lieder.getSelectionModel().getSelectedItems();
        //lieder.getSelectionModel().getSelectedItem();
        /* scroller.setContent(lieder);
        scroller.setFitToHeight(true);
        scroller.setFitToWidth(true); */

        HBox hbox = new HBox(menuBar, swap, exit); //button und menubar in hbox, wobei menubar sich resizen darf
        HBox.setHgrow(menuBar, Priority.ALWAYS);
        HBox.setHgrow(swap, Priority.NEVER);

        border.setTop(hbox);
        StackPane stack = new StackPane(lieder); //stack pane should automatically create scroller
        border.setCenter(stack);

        scene = new Scene(border, 960, 600);
        scene.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());
        
        
        primaryStage.setScene(scene);
        primaryStage.show();


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
        database.addSong(songNew);
        ObservableList<Song> tableDataNew = FXCollections.observableList(database.getSongHash().getAllSongs());
        v.setItems(tableDataNew);
    }

    public void loeschen(Database database, TableView<Song> v) {
        //Dialogfenster entfernen
        Dialog<Song> alert = new Dialog<>();
        DialogPane alertPane = alert.getDialogPane();
        alertPane.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());
        alertPane.getStyleClass().add("alert");
        alert.setTitle("Loeschen bestaetigen");
        Label headerL = new Label("Ausgewaehlten Song loeschen?");
        headerL.setId("headerLoeschen");
        Button yes = new Button("Ja");
        Button no = new Button("Nein");
        yes.setOnAction(e-> {deleteSong(database, v.getSelectionModel().getSelectedItem(), alert, v);});
        no.setOnAction(e-> {dialogClosing(alert);});
        
        /* GridPane alertGrid = new GridPane();
        alertGrid.getStyleClass().add("alertGrid");
        alertGrid.add(yes, 1, 1);
        alertGrid.add(no, 3, 1); */

        HBox alertHBox = new HBox(50, yes, no);
        alertHBox.getStyleClass().add("alertHBox");
        
        BorderPane alertBorder = new BorderPane();
        alertBorder.setTop(headerL);
        alertBorder.setCenter(alertHBox);
        alert.getDialogPane().setContent(alertBorder);
        alert.showAndWait();
    }

    public void deleteSong(Database database, Song song, Dialog<Song> a, TableView<Song> v) {
        database.removeSong(song);
        ObservableList<Song> tableDataNew = FXCollections.observableList(database.getSongHash().getAllSongs());
        v.setItems(tableDataNew);
        dialogClosing(a);
    }

    public void alphabeticalSortA(Database database, TableView<Song> v) {
        ObservableList<Song> sortedTableA = FXCollections.observableList(database.getSongHash().sortAToZ());
        v.setItems(sortedTableA);
    }

    public void alphabeticalSortZ(Database database, TableView<Song> v) {
        ObservableList<Song> sortedTableZ = FXCollections.observableList(database.getSongHash().sortZToA());
        v.setItems(sortedTableZ);
    }

    /* File target = new File("/home/misha/Documents/unicode/Java/Pruefung2Semester/musikverwaltung/src/main/java/frontend/lieder/" + selectedFile.getName());
            selectedFile.renameTo(target); */ //kann File bewegen
            

            /* String source = "/home/misha/Downloads/test.mp3";
            String target = "/home/misha/Documents/unicode/Java/Pruefung2Semester/musikverwaltung/src/main/java/frontend/lieder/frog.mp3";
            try (InputStream inputStream = new FileInputStream(source);
             OutputStream outputStream = new FileOutputStream(target)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("File copied successfully!");

            } catch (IOException ioException) {
            System.err.println("An error occurred while copying the file: " + ioException.getMessage());
            } */

    public void writeObjectToFile(Database database) {
        /* String serFileS = "songObjects.ser";
        File serFile = new File(serFileS);
         try {
            if (!serFile.exists()) {
            serFile.createNewFile();
        } */
         /* } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
         } */

        //try-with initialisiert die Streams in den runden Klammern, damit sie automatisch geschlossen werden, sobal man den try Block verlässt
        try (FileOutputStream outputFile = new FileOutputStream("songObjects.ser", true);
             ObjectOutputStream outputObject = new ObjectOutputStream(new BufferedOutputStream(outputFile))) {
                
                outputObject.writeObject(database);
                
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
        writeObjectToFile(database);
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
}
