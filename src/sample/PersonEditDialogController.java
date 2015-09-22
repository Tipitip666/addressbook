package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Dialog to edit details of a person.
 */
public class PersonEditDialogController
{
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField streetField;

    @FXML
    private TextField postalCodeField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField birthdayField;

    private Stage dialogStage;
    private Person person;
    private boolean okClicked = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize()
    {}

    /**
     * Sets the stage of this dialog.
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage)
    {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the person to be edited in the dialog.
     * @param person
     */
    public void setPerson(Person person)
    {
        this.person = person;
        firstNameField.setText(person.getFirstName());
        lastNameField.setText(person.getLastName());
        streetField.setText(person.getStreet());
        postalCodeField.setText(Integer.toString(person.getPostalCode()));
        cityField.setText(person.getCity());
        birthdayField.setText(DateUtil.format(person.getBirthday()));
        birthdayField.setPromptText("dd, MM, yyyy");
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     * @return
     */
    public boolean isOkClicked()
    {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk()
    {
        if (isInputValid())
        {
            person.setFirstName(firstNameField.getText());
            person.setLastName(lastNameField.getText());
            person.setStreet(streetField.getText());
            person.setPostalCode(Integer.parseInt(postalCodeField.getText()));
            person.setCity(cityField.getText());
            person.setBirthday(DateUtil.parse(birthdayField.getText()));

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel()
    {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     * @return true if the input is valid
     */
    private boolean isInputValid()
    {
        String errorMessage = "";

        if (firstNameField.getText() == null || firstNameField.getText().length() == 0)
        {
            errorMessage += "Не указано имя\n";
        }

        if (lastNameField.getText() == null || lastNameField.getText().length() == 0)
        {
            errorMessage += "Не указана фамилия\n";
        }

        if (streetField.getText() == null || streetField.getText().length() == 0)
        {
            errorMessage += "Не указана улица\n";
        }

        if (postalCodeField.getText() == null || (postalCodeField.getText().length() == 0))
        {
            errorMessage += "Не указан почтовый индекс\n";
        }
        else
        {
            try
            {
                // try to parse the postal code into an int.
                Integer.parseInt(postalCodeField.getText());
            }
            catch (NumberFormatException e)
            {
                errorMessage += "Неверный почтовый индекс (должно быть число)!\n)";
            }
        }

        if (cityField.getText() == null || cityField.getText().length() == 0)
        {
            errorMessage += "Не указан город\n";
        }

        if (birthdayField.getText() == null || cityField.getText().length() == 0)
        {
            errorMessage += "Не указан день рождения\n";
        }
        else
        {
            if (!DateUtil.validDate(birthdayField.getText()))
            {
                errorMessage += "Неверный лень рождения. Используйте формат дд.мм.гггг!\n";
            }
        }

        if (errorMessage.length() == 0)
        {
            return true;
        }
        else
        {
            // Show the error message.
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Ошибка");
            alert2.setHeaderText("Пожалуйста, заполните поля верно");
            alert2.setContentText(errorMessage);
            alert2.showAndWait();
            return false;
        }
    }
}