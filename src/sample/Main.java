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
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    private Stage primaryStage;
    private BorderPane rootLayout;

    public ObservableList<Person> getPersonData()
    {
        return personData;
    }

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Адресная книга");
        personData.add(new Person("Иван", "Иванов"));
        personData.add(new Person("Пётр", "Петров"));
        personData.add(new Person("Андрей", "Андреев"));
        personData.add(new Person("Николай", "Николаев"));
        personData.add(new Person("Сергей", "Сергеев"));
        personData.add(new Person("Максим", "Максимов"));
        personData.add(new Person("Алексей", "Алексеев"));
        personData.add(new Person("Валерий", "Валерьев"));
        personData.add(new Person("Владимир", "Владимиров"));
        this.primaryStage.getIcons().add(new Image("file: resource/images/333.png"));

        initRootLayout();
        showPersonOverview();
    }
        public void initRootLayout()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("rootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            RootLayoutController controller = loader.getController();
            controller.setMain(this);
            primaryStage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        File file = getPersonFilePath();
        if (file != null)
        {
            loadPersonDataFromFile(file);
        }
    }

    public void showPersonOverview()
    {
        try
        {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("sample.fxml"));
        AnchorPane personOverview = (AnchorPane) loader.load();

        rootLayout.setCenter(personOverview);

        PersonOverviewController controller = loader.getController();
        controller.setMain(this);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public boolean showPersonEditDialog(Person person)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("PersonEditDialog.fxml"));
            AnchorPane page = (AnchorPane)loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редактировать");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public Stage getPrimaryStage()
    {
        return primaryStage;
    }

    public static void main(String[] args)
    {
        launch(args);
    }

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

    public void setPersonFilePath(File file)
    {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        if (file != null)
        {
            prefs.put("filePath", file.getPath());
            primaryStage.setTitle("Адресная книга - " + file.getName());
        }
        else
        {
            prefs.remove("filePath");
            primaryStage.setTitle("Адресная книга");
        }
    }

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

    public void loadPersonDataFromFile(File file)
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            PersonListWrapper wrapper = (PersonListWrapper)um.unmarshal(file);

            personData.clear();
            personData.addAll(wrapper.getPersons());

            setPersonFilePath(file);
        }
        catch (Exception e)
        {
            Alert alert4 = new Alert(Alert.AlertType.ERROR);
            alert4.setTitle("Ошибка");
            alert4.setHeaderText("Не удалось загрузить файл:\n" + file.getPath());
            alert4.showAndWait();
        }
    }

    public void  savePersonDataToFile(File file)
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            PersonListWrapper wrapper = new PersonListWrapper();
            wrapper.setPersons(personData);

            m.marshal(wrapper, file);

            setPersonFilePath(file);
        }
        catch (Exception e)
        {
            Alert alert5 = new Alert(Alert.AlertType.ERROR);
            alert5.setTitle("Ошибка");
            alert5.setHeaderText("Не удалось сохранить файл:\n" + file.getPath());
            alert5.showAndWait();
        }
    }

    public void showBirthdayStatistics()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("BirthdayStatistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Статистика дней рождения");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

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