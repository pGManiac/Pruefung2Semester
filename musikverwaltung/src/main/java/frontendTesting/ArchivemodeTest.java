package frontendTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import backend.Database;
import frontend.Archivemode;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ArchivemodeTest {
    private Archivemode archivemode;
    private Stage primaryStage;

    @BeforeEach
    public void setup() {
        primaryStage = new Stage();
        archivemode = new Archivemode(primaryStage);
    }

    @Test
    public void testCreateScene() {
        try {
            Scene scene = archivemode.createScene();
            assertNotNull(scene);
            assertEquals(960, scene.getWidth());
            assertEquals(600, scene.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void createScene_shouldReturnNonNullScene() throws Exception {

        // Act
        Scene scene = archivemode.createScene();

        // Assert
        assertNotNull(scene);

    }

    @Test
    public void writeObjectToFile_shouldWriteDatabaseToFile() {

        // Act
        archivemode.writeObjectToFile();

        // Assert
        // Check if the file was written successfully
        File file = new File("songObjects.ser"); // Replace with the actual path to the file
        assertTrue(file.exists(), "The file should exist");
        // Add assertions to check if the file was written successfully
    }

    @Test
    public void readObjectFromFile_shouldReturnDatabaseFromFile() throws ClassNotFoundException {

        // Act
        Database database = archivemode.readObjectFromFile();

        // Assert
        assertNotNull(database);
        // Add additional assertions to validate the returned database or its content
    }

}
