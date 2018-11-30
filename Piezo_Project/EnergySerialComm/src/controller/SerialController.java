package controller;

import com.fazecast.jSerialComm.SerialPort;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import model.Energy;


import java.util.Scanner;

import static controller.Main.changeScreen;

public class SerialController {
   private SerialPort port;

   @FXML
   private CheckBox checkBoxOn;

   @FXML
   private ChoiceBox<SerialPort> choiceBoxPort;

   @FXML
   private Button buttonClear;

   @FXML
   private Button buttonBack;

   @FXML
   private TextArea textAreaSerial;

   @FXML
   protected void initialize() {
      choiceBoxPort.getItems().setAll(SerialPort.getCommPorts());
      System.out.println(choiceBoxPort.getItems());
      checkBoxOn.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            if(checkBoxOn.isSelected()) {
               checkBoxOn.setSelected(false);
            }else{
               checkBoxOn.setSelected(true);
            }
         }
      });
      buttonClear.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            buttonClearAction(new ActionEvent());
         }
      });
      buttonBack.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            buttonBackAction(new ActionEvent());
         }
      });
   }

   @FXML
   void buttonClearAction(ActionEvent event) {
      textAreaSerial.clear();
   }

   @FXML
   void buttonBackAction(ActionEvent event) {
      changeScreen("database", null);
   }

   @FXML
   void choiceBoxPortAction(ActionEvent event) {
      /*if(thread != null && thread.isAlive()){
         thread.interrupt();
      }
      thread = new Thread(new Update());
      thread.start();*/
      new Thread(new Update()).start();
      //serialWorker();
   }

   /*private void serialWorker(){
      Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(.001), ev -> {
         port = choiceBoxPort.getValue();
         port.openPort();
         port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
         Scanner data = new Scanner(port.getInputStream());
         if(data.hasNextLine()) {
            textAreaSerial.appendText(data.nextLine()+"\n");
         }
      }));
      timeline.setCycleCount(Timeline.INDEFINITE);
      timeline.play();
   }*/

   class Update implements Runnable{

      @Override
      public void run() {
         port = choiceBoxPort.getValue();
         port.openPort();
         port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
         Scanner data = new Scanner(port.getInputStream());
         while(data.hasNextLine()) {
            if(checkBoxOn.isSelected()) {
               try {
                  Energy energy = new Energy(Double.parseDouble(data.nextLine()), 1.);
                  energy.create();
                  textAreaSerial.appendText(energy.toString() + "\n");
               } catch (Exception e) {
                  Alert alert = new Alert(Alert.AlertType.WARNING);
                  alert.setTitle("ERROR - " + e.getClass());
                  alert.setHeaderText(e.getLocalizedMessage());
                  alert.setContentText(e.getMessage());
                  alert.showAndWait();
               }
            }
         }
      }
   }
}
