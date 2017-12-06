package ru.lets.funlagitog;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Setter;

/**
 *
 * @author Евгений
 */
public class CallController implements Initializable {
    
    private final ConnectionService connectionService = ConnectionService.getConnectionService();

    @Setter
    private Stage currentStage;

    private Operator operator;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label operatorLabel;

    @FXML
    private Label durationLabel;

    private String telephone;

    private LocalDateTime startTime;

    private Timer secondsTimer;

    public void setTelephone(String telephone) {
        this.telephone = telephone;
        phoneLabel.setText(telephone);
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
        operatorLabel.setText(operator.getName());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private static final Integer ONE_SECOND = 1000;

    public void show() {
        connectionService.startCall(telephone);
        startTime = LocalDateTime.now();
        secondsTimer = new Timer();
        secondsTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Duration duration = Duration.between(startTime, LocalDateTime.now());
                    long minutes = duration.getSeconds() / 60;
                    long seconds = duration.getSeconds() % 60;
                    String minutesText = convertLongToTimePart(minutes);
                    String secondsText = convertLongToTimePart(seconds);
                    String durationText = minutesText + ":" + secondsText;
                    durationLabel.setText(durationText);
                });
            }
        }, 0, ONE_SECOND);
        currentStage.showAndWait();
    }
    
    private String convertLongToTimePart(long part) {
        if (part >= 10) return Long.toString(part);
        return "0" + Long.toString(part);
    }

    @FXML
    private void stopCall(ActionEvent event) {
        connectionService.stopCall(telephone);
        secondsTimer.cancel();
        currentStage.close();
    }

}
