package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 */
public class RootLayoutController
{
    // Reference to the main application
    private Main main;

    /**
     * Is called by the main application to give a reference back to itself.
     * @param main
     */
    public void setMain(Main main)
    {
        this.main = main;
    }

    /**
     * Creates an empty address book.
     */
    @FXML
    private void handleNew()
    {
        main.getPersonData().clear();
        main.setPersonFilePath(null);
    }

    /**
     * Opens a FileChooser to let the user select an address book to load.
     */
    @FXML
    private void handleOpen()
    {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                 "XML файлы (*.xml)", "(*.xml)");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(main.getPrimaryStage());

        if (file != null)
        {
            main.loadPersonDataFromFile(file);
        }
    }

    /**
     * Saves the file to the person file that is currently open. If there is no
     * open file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave()
    {
        File personFile = main.getPersonFilePath();
        if (personFile != null)
        {
            main.savePersonDataToFile(personFile);
        }
        else
        {
            handleSaveAs();
        }
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs()
    {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML файлы (*.xml)", "(*.xml)");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(main.getPrimaryStage());

        if (file != null)
        {
            // Make sure it has the correct extension.
            if (!file.getPath().endsWith(".xml"))
            {
                file = new File(file.getPath() + ".xml");
                main.savePersonDataToFile(file);
            }
        }
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout()
    {
        Alert alert6 = new Alert(Alert.AlertType.INFORMATION);
        alert6.setTitle("О программе");
        alert6.setHeaderText("Адресная книга v0.02");
        alert6.setContentText("Автор: Deathmachine");
        alert6.showAndWait();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit()
    {
        System.exit(0);
    }

    /**
     * Opens the birthday statistics.
     */
    @FXML
    private void handleShowBirthdayStatistics()
    {
        main.showBirthdayStatistics();
    }
}