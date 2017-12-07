package ru.lets.funlagitog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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

    private final String settingsFilePath = "C:\\projects\\labs\\telephonedata.txt";

    private final ConnectionService connectionService;
    private String currentTelephone;

    public FXMLController() throws IOException {
        connectionService = ConnectionService.getConnectionService();
    }

    private Operator currentOperator;
    private Region currentRegion;
    private Integer id;

    @FXML
    private ComboBox<Operator> operatorComboBox;

    @FXML
    private ComboBox<Region> regionsComboBox;

    @FXML
    private void onChooseOperator(ActionEvent event) {
        try {
            currentOperator = operatorComboBox.getSelectionModel().getSelectedItem();
            currentRegion = regionsComboBox.getSelectionModel().getSelectedItem();
            Response response = connectionService.register(currentTelephone, currentOperator, currentRegion);
            this.id = response.getId();
            new Alert(Alert.AlertType.INFORMATION, response.toString(), ButtonType.OK).showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            controller.setRegion(currentRegion);
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
            int balance = connectionService.findBalance(id, currentRegion);
            String rubles = Integer.toString(balance / 100);
            String penny = convertIntToMoney(balance % 100);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Ваш баланс равен " + rubles + " р. " + penny + " к.", ButtonType.OK);
            alert.setTitle("Баланс");
            alert.setHeaderText(null);
            alert.showAndWait();
        } catch (IOException e) {
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
        operators.add(new Operator(1, "MTC"));
        operators.add(new Operator(0, "Beeline"));
        operators.add(new Operator(2, "Megaphone"));
        operatorComboBox.getItems().addAll(operators);
        operatorComboBox.getSelectionModel().select(0);
        currentOperator = operators.get(0);

        List<Region> regions = new ArrayList<>();
        regions.add(new Region("Altai", "10.42.0.1:4000"));
        regions.add(new Region("Moscow", "10.42.0.1:4000"));
        regions.add(new Region("Crimea", "10.42.0.1:4000"));
        regionsComboBox.getItems().addAll(regions);
        regionsComboBox.getSelectionModel().select(0);
        currentRegion = regions.get(0);

        try (BufferedReader reader = new BufferedReader(new FileReader(settingsFilePath))) {
            currentTelephone = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
