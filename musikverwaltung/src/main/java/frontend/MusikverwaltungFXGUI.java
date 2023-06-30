package frontend;

import java.io.File;

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
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

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
    Dialog<Song> add;
    ObservableList<String> options = FXCollections.observableArrayList("Metal", "Pop", "Rock", "Klassik", "Country");
    FileChooser fileChooser = new FileChooser();

    public static void initiate() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Music Player");

        /* StackPane layout = new StackPane();
        layout.getChildren().add(button); */

        BorderPane border = new BorderPane();

        //Menues
        genre = new Menu("_Genre");
        datei = new Menu("_Datei");
        darstellung = new Menu("_Ansicht");

        hinzu = new MenuItem("Hinzufuegen");
        hinzu.setOnAction(e-> {einfuegen(primaryStage);});
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
        lieder.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        /* scroller.setContent(lieder);
        scroller.setFitToHeight(true);
        scroller.setFitToWidth(true); */

        VBox vbox = new VBox(menuBar);

        border.setTop(vbox);
        border.setCenter(lieder);

        Scene scene = new Scene(border, 960, 600);
        
        
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public void einfuegen(Stage stage) {
        //Dialogfenster
        add = new Dialog<>();
        add.setTitle("Songeingabe");
        add.setHeaderText("Geben Sie Details zu Ihrem Song ein:");
        add.setResizable(true);

        Label titel = new Label("Titel:");
        TextField titTField = new TextField();
        Label album = new Label("Album:");
        TextField albTField = new TextField();
        Label genre = new Label("Genre:");
        final ComboBox<String> genComboBox = new ComboBox<>(options);
        Label interpret = new Label("Interpret:");
        TextField interTField = new TextField();
        fileChooser.setTitle("MP3-Datei waehlen");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("MP3", "*.mp3"));
        Button browse = new Button("Browse...");
        browse.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(stage);
        });

        GridPane grid = new GridPane();
        grid.add(titel, 1, 1);
        grid.add(album, 1, 2);
        grid.add(genre, 1, 3);
        grid.add(interpret, 1, 4);
        grid.add(titTField, 2, 1);
        grid.add(albTField, 2, 2);
        grid.add(genComboBox, 2, 3);
        grid.add(interTField, 2, 4);
        grid.add(browse, 2, 6);
        //alles im Zentrum des Fensters anordnen und etwas groesser machen
        ButtonType buttonTypeOk = new ButtonType("Fertig", ButtonData.OK_DONE);
        add.getDialogPane().setContent(grid);
        add.getDialogPane().getButtonTypes().add(buttonTypeOk);
        add.showAndWait();
    }
}
