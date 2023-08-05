package frontend;

import java.io.File;

import backend.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class LoeschenGUI {
    private Archivemode master;
    private Dialog<Song> alert;

    public LoeschenGUI(Archivemode masterArchivemode) {
        this.master = masterArchivemode;
    }

    /**
     * @brief Creates the dialog needed to remove a new song from the database.
     * @return none
     * @implNote none
     * @note This method uses standard JavaFX controls and implements a css stylesheet.
     *       For more details on how the controls work check official JavaFX documentation.
     *       If no song is picked, a warning will show instead.
     * @see javafx.scene
     */

    public void loeschen() {
        alert = new Dialog<>();
        DialogPane alertPane = alert.getDialogPane();
        alertPane.getStylesheets().add((new File("src/main/java/frontend/VerwaltungGUI.css")).toURI().toString());
        alertPane.getStyleClass().add("alert");
        Label headerL;
        Song picked = master.getTable().getSelectionModel().getSelectedItem();
        BorderPane alertBorder = new BorderPane();

        if (picked == null) {
            //Dialogfenster Warnung

            alert.setTitle("Warnung!");
            headerL = new Label("Sie haben keinen Song ausgewaehlt!");
            ButtonType okButtonType = new ButtonType("OK", ButtonData.OK_DONE);
            alertPane.getButtonTypes().add(okButtonType);

        } else {
            //Dialogfenster entfernen
        
            alert.setTitle("Loeschen bestaetigen");
            headerL = new Label("Ausgewaehlten Song loeschen?");
            headerL.setId("headerLoeschen");
            Button yes = new Button("Ja");
            Button no = new Button("Nein");
            yes.setOnAction(e-> {deleteSong(picked);});
            no.setOnAction(e-> {master.dialogClosing(alert);});
            HBox alertHBox = new HBox(50, yes, no);
            alertHBox.getStyleClass().add("alertHBox");
            alertBorder.setCenter(alertHBox);
        }
        
        //Layout
        alertBorder.setTop(headerL);
        alertPane.setContent(alertBorder);
        alert.showAndWait();
    }

    /**
     * @brief Removes song object from database.
     * @param song the song to be removed 
     * @return none
     * @implNote none
     * @note This method removes the song from the database and creates an updated List for the
     *       TableView to display. A method to custom close the dialog is called at the end.
     * @see javafx.collections
     */

    public void deleteSong(Song song) {
        //database.removeSong(song);
        master.getDatabase().removeSong(song);
        ObservableList<Song> tableDataNew = FXCollections.observableList(master.getDatabase().getSongHash().getAllSongs());
        //v.setItems(tableDataNew);
        master.getTable().setItems(tableDataNew);
        master.dialogClosing(this.alert);
    }

}
