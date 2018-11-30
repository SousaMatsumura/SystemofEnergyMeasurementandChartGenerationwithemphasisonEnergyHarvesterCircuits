package controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.mysql.MySQLBase;

import java.util.ArrayList;

public class Main extends Application {

   private static Stage stage;
   private static Scene databaseScene;
   private static Scene serialScene;
   private static MySQLBase mySQLBase = new MySQLBase();
   //private static MySQLBase rootBase = new MySQLBase("localhost", 3306, "piezo", "root", "1!RO5@ot");

   @Override
   public void start(Stage primaryStage) throws Exception{

      stage = primaryStage;

      primaryStage.setTitle("Energy Serial Communication to MySQL");

      stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
         @Override
         public void handle(WindowEvent event) {
            Platform.exit();
            System.exit(0);
         }});

      Parent fxmlDatabase = FXMLLoader.load(getClass().getResource("/view/database_screen.fxml"));
      databaseScene = new Scene(fxmlDatabase, 400, 350);

      Parent fxmlMain = FXMLLoader.load(getClass().getResource("/view/serial_screen.fxml"));
      serialScene = new Scene(fxmlMain, 600, 400);

      primaryStage.setScene(databaseScene);
      primaryStage.show();
   }

   public static void changeScreen(String s, Object userData){
      switch(s){
         case "database":
            stage.setScene(databaseScene);
            notifyAllListeners("database", userData);
            break;
         case "serial":
            stage.setScene(serialScene);
            notifyAllListeners("serial", userData);
      }
   }

   public static void changeScreen(String s){
      changeScreen(s, null);
   }

   public static void main(String[] args) {
        launch(args);
    }

   private static ArrayList<OnChangeScreen> listeners = new ArrayList<>();

   public static interface OnChangeScreen{
      void onScreenChanged(String newScreen, Object userData);
   }

   public static void addOnChangeScreenListener(OnChangeScreen newListener){
      listeners.add(newListener);
   }

   private static void notifyAllListeners(String newScreen, Object userData){
      for(OnChangeScreen l : listeners){
         l.onScreenChanged(newScreen, userData);
      }
   }

   public static MySQLBase getMySQLBase() {
      return mySQLBase;
   }

   public static void setMySQLBase(MySQLBase mySQLBase) {
      Main.mySQLBase = mySQLBase;
   }
}
