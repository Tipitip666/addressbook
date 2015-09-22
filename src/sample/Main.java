package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.prefs.Preferences;

public class Main extends Application
{
    /**
     *   The data as an observable list of Persons.
     */
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     *   Returns the data as an observable list of Persons.
     *   @return
     */
    public ObservableList<Person> getPersonData()
    {
        return personData;
    }

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Адресная книга");

        //add some simple data.
        personData.add(new Person("Иван", "Иванов"));
        personData.add(new Person("Пётр", "Петров"));
        personData.add(new Person("Андрей", "Андреев"));
        personData.add(new Person("Николай", "Николаев"));
        personData.add(new Person("Сергей", "Сергеев"));
        personData.add(new Person("Максим", "Максимов"));
        personData.add(new Person("Алексей", "Алексеев"));
        personData.add(new Person("Валерий", "Валерьев"));
        personData.add(new Person("Владимир", "Владимиров"));

        // Set the application icon.
        this.primaryStage.getIcons().add(new Image("file: resource/images/333.png"));

        initRootLayout();
        showPersonOverview();
    }

    /**
     * Initializes the root layout and tries to load the last opened
     * person file.
     */
        public void initRootLayout()
    {
        try
        {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("rootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main application.
            RootLayoutController controller = loader.getController();
            controller.setMain(this);
            primaryStage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        // Try to load last opened person file.
        File file = getPersonFilePath();
        if (file != null)
        {
            loadPersonDataFromFile(file);
        }
    }

    /**
     *   Show the person overview inside the root layout.
     */
    public void showPersonOverview()
    {
        try
        {
        // Load person overview.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("sample.fxml"));
        AnchorPane personOverview = (AnchorPane) loader.load();

        // Set the person overview into the center of root layout.
        rootLayout.setCenter(personOverview);

        // Give the controller access to the main application.
        PersonOverviewController controller = loader.getController();
        controller.setMain(this);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     * @param person the person object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showPersonEditDialog(Person person)
    {
        try
        {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("PersonEditDialog.fxml"));
            AnchorPane page = (AnchorPane)loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редактировать");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            // Show the dialog and wait until the user closes it.
            dialogStage.showAndWait();

            return controller.isOkClicked();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *   Returns the main stage.
     *   @return
     */
    public Stage getPrimaryStage()
    {
        return primaryStage;
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * Returns the person file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     * @return
     */
    public File getPersonFilePath()
    {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null)
        {
            return new File(filePath);
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     * @param file the file or null to remove the path
     */
    public void setPersonFilePath(File file)
    {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        if (file != null)
        {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            primaryStage.setTitle("Адресная книга - " + file.getName());
        }
        else
        {
            prefs.remove("filePath");

            // Update the stage title.
            primaryStage.setTitle("Адресная книга");
        }
    }

    /**
     * Helper class to wrap a list of persons. This is used for saving the
     * list of persons to XML.
     */
    @XmlRootElement(name = "persons")
    public class PersonListWrapper
    {
        private List<Person> persons;

        @XmlElement(name = "person")
        public List<Person> getPersons()
        {
            return persons;
        }

        public void setPersons(List<Person> persons)
        {
            this.persons = persons;
        }
    }

    /**
     * Loads person data from the specified file. The current person data will
     * be replaced.
     * @param file
     */
    public void loadPersonDataFromFile(File file)
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            PersonListWrapper wrapper = (PersonListWrapper)um.unmarshal(file);

            personData.clear();
            personData.addAll(wrapper.getPersons());

            // Save the file path to the registry.
            setPersonFilePath(file);
        }
        catch (Exception e)
        {
            // Catches any exception
            Alert alert4 = new Alert(Alert.AlertType.ERROR);
            alert4.setTitle("Ошибка");
            alert4.setHeaderText("Не удалось загрузить файл:\n" + file.getPath());
            alert4.showAndWait();
        }
    }

    /**
     * Saves the current person data to the specified file.
     * @param file
     */
    public void  savePersonDataToFile(File file)
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our person data.
            PersonListWrapper wrapper = new PersonListWrapper();
            wrapper.setPersons(personData);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setPersonFilePath(file);
        }
        catch (Exception e)
        {
            // Catches any exception
            Alert alert5 = new Alert(Alert.AlertType.ERROR);
            alert5.setTitle("Ошибка");
            alert5.setHeaderText("Не удалось сохранить файл:\n" + file.getPath());
            alert5.showAndWait();
        }
    }

    /**
     * Opens a dialog to show birthday statistics.
     */
    public void showBirthdayStatistics()
    {
        try
        {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("BirthdayStatistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Статистика дней рождения");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            BirthdayStatisticsController controller = loader.getController();
            controller.setPersonData(personData);
            dialogStage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}