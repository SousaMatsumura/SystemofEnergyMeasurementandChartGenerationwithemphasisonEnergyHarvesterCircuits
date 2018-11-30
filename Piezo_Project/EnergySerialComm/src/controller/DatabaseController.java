package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import model.mysql.MySQLBase;

import static controller.Main.changeScreen;

public class DatabaseController {

   @FXML
   private TextField textFieldIP;

   @FXML
   private TextField textFieldPort;

   @FXML
   private TextField textFieldDatabase;

   @FXML
   private TextField textFieldUser;

   @FXML
   private PasswordField passwordField;

   @FXML
   private Button buttonConnect;

   @FXML
   protected void initialize() {
      textFieldIP.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            buttonConnectAction(new ActionEvent());
         }
      });
      textFieldPort.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            buttonConnectAction(new ActionEvent());
         }
      });
      textFieldDatabase.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            buttonConnectAction(new ActionEvent());
         }
      });
      textFieldUser.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            buttonConnectAction(new ActionEvent());
         }
      });
      passwordField.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            buttonConnectAction(new ActionEvent());
         }
      });
      buttonConnect.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            buttonConnectAction(new ActionEvent());
         }
      });
   }

   @FXML
   void buttonConnectAction(ActionEvent event) {
      try {
         MySQLBase base = new MySQLBase(String.valueOf(textFieldIP.getText()),
            Integer.parseInt(String.valueOf(textFieldPort.getText())),
            String.valueOf(textFieldDatabase.getText()),
            String.valueOf(textFieldUser.getText()),
            String.valueOf(passwordField.getText()));
         base.test();
         Main.setMySQLBase(base);
         changeScreen("serial", null);
      }catch (Exception ex) {
         ex.printStackTrace();
         String cnUrl = "jdbc:mysql://"+String.valueOf(textFieldIP.getText())+":"+String.valueOf(textFieldPort.getText())+"/"+ String.valueOf(textFieldDatabase.getText()) +"?useSSL=false&useTimezone=true&serverTimezone=UTC&autoReconnect=true&useUnicode=true&characterEncoding=utf8";
         System.out.println("URL: "+cnUrl+"\nUser: "+String.valueOf(textFieldUser.getText())+"\nPassword: "+String.valueOf(passwordField.getText()));
         Alert alert = new Alert(Alert.AlertType.WARNING);
         alert.setTitle("ERROR - Connection failed");
         alert.setHeaderText(ex.getMessage());
         alert.setContentText("URL: "+cnUrl+"\nUser: "+String.valueOf(textFieldUser.getText()));
         alert.showAndWait();
      }
   }
}