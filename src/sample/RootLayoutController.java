package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;

public class RootLayoutController
{
    private Main main;
    public void setMain(Main main)
    {
        this.main = main;
    }
    @FXML
    private void handleNew()
    {
        main.getPersonData().clear();
        main.setPersonFilePath(null);
    }

    @FXML
    private void handleOpen()
    {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                 "XML файлы (*.xml)", "(*.xml)");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(main.getPrimaryStage());

        if (file != null)
        {
            main.loadPersonDataFromFile(file);
        }
    }

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

    @FXML
    private void handleSaveAs()
    {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML файлы (*.xml)", "(*.xml)");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(main.getPrimaryStage());

        if (file != null)
        {
            if (!file.getPath().endsWith(".xml"))
            {
                file = new File(file.getPath() + ".xml");
                main.savePersonDataToFile(file);
            }
        }
    }

    @FXML
    private void handleAbout()
    {
        Alert alert6 = new Alert(Alert.AlertType.ERROR);
        alert6.setTitle("Адресная книга");
        alert6.setHeaderText("О программе");
        alert6.setContentText("Автор: Вадим Мясников");
        alert6.showAndWait();
    }

    @FXML
    private void handleExit()
    {
        System.exit(0);
    }

    @FXML
    private void handleShowBirthdayStatistics()
    {
        main.showBirthdayStatistics();
    }
}