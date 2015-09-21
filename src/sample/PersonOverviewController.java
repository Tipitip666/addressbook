package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PersonOverviewController
{
    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label postalCOdeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label birthdayLabel;

    private Main main;

    public PersonOverviewController()
    {
    }

    @FXML
    public void initialize()
    {
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        showPersonDetails(null);

        personTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    private void showPersonDetails (Person person)
    {
        if (person != null)
        {
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCOdeLabel.setText(Integer.toString(person.getPostalCode()));
            cityLabel.setText(person.getCity());
            birthdayLabel.setText(DateUtil.format(person.getBirthday()));
        }
        else
        {
            firstNameLabel.setText((""));
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCOdeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }
    }

    public void setMain (Main main)
    {
        this.main = main;
        personTable.setItems(main.getPersonData());
    }

    @FXML
    private void handleDeletePerson()
    {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0)
        {
        personTable.getItems().remove(selectedIndex);
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ничто не выделено");
            alert.setContentText("Пожалуйста, сначала выделите запись");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleNewPerson()
    {
        Person tempPerson = new Person();
        boolean okClicked = main.showPersonEditDialog(tempPerson);

        if (okClicked)
        {
            main.getPersonData().add(tempPerson);
        }
    }

    @FXML
    private void handleEditPerdon()
    {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null)
        {
            boolean okClicked = main.showPersonEditDialog(selectedPerson);
            if (okClicked)
            {
                showPersonDetails(selectedPerson);
            }
        }
        else
        {
            Alert alert3 = new Alert(Alert.AlertType.ERROR);
            alert3.setTitle("Ошибка");
            alert3.setHeaderText("Ничто не выделено");
            alert3.setContentText("Пожалуйста, сначала выделите запись");
            alert3.showAndWait();
        }
    }
}