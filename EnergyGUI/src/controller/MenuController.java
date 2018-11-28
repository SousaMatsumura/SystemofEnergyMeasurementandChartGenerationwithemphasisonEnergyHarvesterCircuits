package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

public class MenuController {

   @FXML
   private Button buttonDatabase;

   @FXML
   private Button buttonTable;

   @FXML
   private Button buttonChart;

   @FXML
   protected void initialize(){
      Main.addOnChangeScreenListener(new Main.OnChangeScreen() {
         @Override
         public void onScreenChanged(String newScreen, Object userData){

         }
      });
      buttonDatabase.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            buttonDatabaseAction(new ActionEvent());
         }
      });
      buttonTable.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            buttonTableAction(new ActionEvent());
         }
      });
      buttonChart.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            buttonChartAction(new ActionEvent());
         }
      });

   }

   @FXML
   void buttonChartAction(ActionEvent event) {
      Main.changeScreen("chart");
   }

   @FXML
   void buttonDatabaseAction(ActionEvent event) {
      Main.changeScreen("database");
   }

   @FXML
   void buttonTableAction(ActionEvent event) {
      Main.changeScreen("table");
   }
}
