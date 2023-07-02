package frontend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;

import backend.Song;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    public static void initiate() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Music Player");

        /* StackPane layout = new StackPane();
        layout.getChildren().add(button); */

        BorderPane border = new BorderPane(); //algemeines Layout

        //Moduswechsel
        Image image = new Image("file:/home/misha/Documents/unicode/Java/Pruefung2Semester/musikverwaltung/src/main/java/frontend/icons/mode11.png"); //es muss vornedran file: stehen
        ImageView img = new ImageView(image);
        swap = new Button("Musik");
        img.setFitWidth(swap.getWidth());
        img.setFitHeight(swap.getHeight()); //Button passt sich groesse des Bildes an
        swap.setGraphic(img);
        swap.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        //Menues
        genre = new Menu("_Genre"); //ermoeglicht shortkey durch Unterstrich (Anfangsbuchstabe)
        datei = new Menu("_Datei");
        datei.setId("datei");
        darstellung = new Menu("_Ansicht");

        hinzu = new MenuItem("Hinzufuegen");
        hinzu.setOnAction(e-> {einfuegen(primaryStage, lieder);});
        entfernen = new MenuItem("Entfernen");
        az = new MenuItem("A-Z");
        za = new MenuItem("Z-A");
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
        TableColumn<Song, String> spalte1 = new TableColumn<>("Titel");
        TableColumn<Song, String> spalte2 = new TableColumn<>("Album");
        TableColumn<Song, Integer> spalte3 = new TableColumn<>("Genre");
        TableColumn<Song, String> spalte4 = new TableColumn<>("Interpret");
        spalte1.setCellValueFactory(new PropertyValueFactory<>("name"));
        spalte2.setCellValueFactory(new PropertyValueFactory<>("album"));
        spalte3.setCellValueFactory(new PropertyValueFactory<>("genre"));
        spalte4.setCellValueFactory(new PropertyValueFactory<>("artist"));
        lieder.getColumns().addAll(spalte1, spalte2, spalte3, spalte4);
        lieder.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //Spalten passen sich window an
        /* scroller.setContent(lieder);
        scroller.setFitToHeight(true);
        scroller.setFitToWidth(true); */

        HBox hbox = new HBox(menuBar, swap); //button und menubar in hbox, wobei menubar sich resizen darf
        HBox.setHgrow(menuBar, Priority.ALWAYS);
        HBox.setHgrow(swap, Priority.NEVER);

        border.setTop(hbox);
        border.setCenter(lieder);

        Scene scene = new Scene(border, 960, 600);
        scene.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());
        
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public void einfuegen(Stage stage, TableView<Song> v) {
        //Dialogfenster
        adder = new Dialog<>();
        adder.setTitle("Songeingabe");
        adder.setHeaderText("Geben Sie Details zu Ihrem Song ein:");
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
        Button finish = new Button("Create!");
        finish.setOnAction(e -> {createSong(stage, titTField, albTField, genComboBox, interTField, v);}); //durch create ist es moeglich mehrere Songs hintereinander zu adden
        
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
        grid.add(finish, 3, 7);
        ButtonType okButtonType = new ButtonType("Fertig", ButtonData.OK_DONE); //dieser Button ist notwendig zum Schliessen des Dialogs
        adder.getDialogPane().setContent(grid);
        adder.getDialogPane().getButtonTypes().add(okButtonType);
        
        adder.showAndWait();
    }

    public void createSong(Stage stage, TextField t, TextField a, ComboBox<String> g, TextField i, TableView<Song> v) {
        String titleNew = t.getText();
        String albumNew = a.getText();
        String genreNew = g.getValue();
        String artistNew = i.getText();
        int genreValue;

        switch (genreNew) {
            case "Metal":
                genreValue = 0;
                break;
            case "Pop":
                genreValue = 1;
                break;
            case "Rock":
                genreValue = 2;
                break;
            case "Klassik":
                genreValue = 3;
                break;
            case "Country":
                genreValue = 4;
                break;
            default:
                genreValue = -1; //Genre N/A
        }

        File selectedFile = fileChooser.showOpenDialog(stage); //root window stage cannot be accessed, while the is dialog open
        String destination = "/home/misha/Documents/unicode/Java/Pruefung2Semester/musikverwaltung/src/main/java/frontend/lieder/";
            String id = new SimpleDateFormat("mm:ss:SSS").format(new java.util.Date());

            Path source = selectedFile.toPath();
            Path target = Path.of(destination + id + "_" + selectedFile.getName());
            try {
                Files.copy(source, target, StandardCopyOption.COPY_ATTRIBUTES); //attributes of og file copied as well
            } catch (IOException ioException) {
                System.err.println(ioException.getMessage());
            }

        Song songNew = new Song(titleNew, albumNew, genreValue, artistNew);
        //Song Objekte im Anschluss in .ser file schreiben und immer beim Öffnen der Applikation die .ser files lesen
        v.getItems().add(songNew);
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
}
