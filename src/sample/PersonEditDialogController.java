package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    @FXML
    private void initialize()
    {}

    public void setDialogStage(Stage dialogStage)
    {
        this.dialogStage = dialogStage;
    }

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

    public boolean isOkClicked()
    {
        return okClicked;
    }

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

    @FXML
    private void handleCancel()
    {
        dialogStage.close();
    }

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
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Ошибка");
            alert2.setHeaderText("Пожалуйста, заполните поля верно");
            alert2.setContentText(errorMessage);
            alert2.showAndWait();
            return false;
        }
    }
}