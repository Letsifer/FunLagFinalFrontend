package ru.lets.funlagitog;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLController implements Initializable {

    private final ConnectionService connectionService;

    public FXMLController() throws IOException {
        connectionService = ConnectionService.getConnectionService();
    }

    private Operator currentOperator;

    @FXML
    private ComboBox<Operator> operatorComboBox;

    @FXML
    private void onChooseOperator(ActionEvent event) {
        currentOperator = operatorComboBox.getSelectionModel().getSelectedItem();
        new Alert(Alert.AlertType.INFORMATION, "Choose operator " + currentOperator.getName(), ButtonType.OK).showAndWait();
    }

    @FXML
    private TextField telephoneNumberTextField;

    @FXML
    private void startCallAction(ActionEvent actionEvent) {
        try {
            Stage callStage = new Stage();
            callStage.setTitle("Звонок");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Call.fxml"));
            Parent root = loader.load();

            CallController controller = (CallController) loader.getController();
            controller.setCurrentStage(callStage);
            controller.setOperator(currentOperator);
            controller.setTelephone(telephoneNumberTextField.getText());

            Scene scene = new Scene(root, 400, 400);
            callStage.setScene(scene);
            controller.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void findBalanceAction(ActionEvent actionEvent) {
        try {
            int balance = connectionService.findBalance();
            String rubles = Integer.toString(balance / 100);
            String penny = convertIntToMoney(balance % 100);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Ваш баланс равен " + rubles + " р. " + penny + " к.", ButtonType.OK);
            alert.setTitle("Баланс");
            alert.setHeaderText(null);
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertIntToMoney(long part) {
        if (part >= 10) {
            return Long.toString(part);
        }
        return "0" + Long.toString(part);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Operator> operators = new ArrayList<>();
        operators.add(new Operator("Op1", 100));
        operators.add(new Operator("Op2", 1000));
        operators.add(new Operator("Op3", 200));
        operators.add(new Operator("Op4", 500));
        operators.sort(new Comparator<Operator>() {
            @Override
            public int compare(Operator o1, Operator o2) {
                return o2.getSignalLevel().compareTo(o1.getSignalLevel());
            }
        });
        operatorComboBox.getItems().addAll(operators);
        operatorComboBox.getSelectionModel().select(0);
        currentOperator = operators.get(0);
    }
}
