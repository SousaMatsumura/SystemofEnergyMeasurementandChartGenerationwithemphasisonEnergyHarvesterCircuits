package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.*;
import model.ConstraintsInterval;
import model.DateTime;
import model.Momentum;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class TableController {

   @FXML
   private CheckBox checkBoxVoltage;

   @FXML
   private CheckBox checkBoxCurrent;

   @FXML
   private CheckBox checkBoxPower;

   @FXML
   private TextField textFieldDateBegin;

   @FXML
   private TextField textFieldDateEnd;

   @FXML
   private TextField textFieldTimeBegin;

   @FXML
   private TextField textFieldTimeEnd;

   @FXML
   private Button buttonSearch;

   @FXML
   private Button buttonBack;

   @FXML
   private TableView<Momentum> tableView;

   @FXML
   void buttonBackAction(ActionEvent event) {
      Main.changeScreen("menu");
   }

   @FXML
   void buttonSearchAction(ActionEvent event) {
      updateList();
   }

   @FXML
   protected void initialize() {
      tableView.getSelectionModel().setCellSelectionEnabled(true);
      tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      checkBoxVoltage.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            if (checkBoxVoltage.isSelected()) {
               checkBoxVoltage.setSelected(false);
            } else {
               checkBoxVoltage.setSelected(true);
            }
         }
      });
      checkBoxCurrent.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            if (checkBoxCurrent.isSelected()) {
               checkBoxCurrent.setSelected(false);
            } else {
               checkBoxCurrent.setSelected(true);
            }
         }
      });
      checkBoxPower.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            if (checkBoxPower.isSelected()) {
               checkBoxPower.setSelected(false);
            } else {
               checkBoxPower.setSelected(true);
            }
         }
      });
      textFieldDateBegin.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            updateList();
         }
      });
      textFieldDateEnd.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            updateList();
         }
      });
      textFieldTimeBegin.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            updateList();
         }
      });
      textFieldTimeEnd.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            updateList();
         }
      });
      buttonSearch.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            updateList();
         }
      });
      buttonBack.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            Main.changeScreen("menu");
         }
      });
      final KeyCodeCombination keyCodeCopy = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);
      tableView.setOnKeyPressed(event -> {
         if (keyCodeCopy.match(event)) {
            copySelectionToClipboard(tableView);
         }
      });
      Main.addOnChangeScreenListener(new Main.OnChangeScreen() {
         @Override
         public void onScreenChanged(String newScreen, Object userData) {
            if (newScreen.equals("table")) {

               updateList();
               System.out.println("updateList()");
            }
         }
      });
      copySelectionToClipboard(tableView);
      //updateList();
   }

   /*@FXML
   public String constraintsInterval() {
      String s = null;
      if(!textFieldDateBegin.getText().equalsIgnoreCase("any")){
         s = " and `time`>= '" + textFieldDateBegin.getText() + "'";
      }
      if(!textFieldDateEnd.getText().equalsIgnoreCase("any")){
         if(s==null){
            s = " and `time`<= '" + textFieldDateEnd.getText() + "'";
         }else {
            s += " and `time`<= '" + textFieldDateEnd.getText() + "'";
         }
      }
      if(!textFieldTimeBegin.getText().equalsIgnoreCase("any")){
         if(s==null){
            s = " and time(`time`)>= time('" + textFieldTimeBegin.getText() + "')";
         }else {
            s += " and time(`time`)>= time('" + textFieldTimeBegin.getText() + "')";
         }
      }
      if(!textFieldTimeEnd.getText().equalsIgnoreCase("any")){
         if(s==null){
            s = " and time(`time`)<= time('" + textFieldTimeEnd.getText() + "')";
         }else{
            s += " and time(`time`)<= time('" + textFieldTimeEnd.getText() + "')";
         }
      }
      return s;
   }*/

   @FXML
   private ConstraintsInterval constraintsInterval() {
      ConstraintsInterval ci = null;
      if(!textFieldDateBegin.getText().equalsIgnoreCase("any")){
         ci = new ConstraintsInterval(" and `time`>= ?", textFieldDateBegin.getText());
      }
      if(!textFieldDateEnd.getText().equalsIgnoreCase("any")){
         if(ci==null){
            ci = new ConstraintsInterval(" and `time`<= ?", textFieldDateEnd.getText());
         }else {
            ci.setQuery(ci.getQuery()+" and `time`<= ?");
            ci.getConstraints().add(textFieldDateEnd.getText());
         }
      }
      if(!textFieldTimeBegin.getText().equalsIgnoreCase("any")){
         if(ci==null){
            ci = new ConstraintsInterval(" and `time`>= ?", textFieldTimeBegin.getText());
         }else {
            ci.setQuery(ci.getQuery()+" and time(`time`)>= time(?)");
            ci.getConstraints().add(textFieldTimeBegin.getText());
         }
      }
      if(!textFieldTimeEnd.getText().equalsIgnoreCase("any")){
         if(ci==null){
            ci = new ConstraintsInterval(" and `time`<= ?", textFieldTimeEnd.getText());
         }else{
            ci.setQuery(ci.getQuery()+" and time(`time`)<= time(?)");
            ci.getConstraints().add(textFieldTimeEnd.getText());
         }
      }
      System.out.println("Cons: "+ci);
      return ci;
   }

   @FXML
   private void updateList() {
      tableView.getItems().clear();
      tableView.getColumns().clear();

      TableColumn<Momentum, String> columnTime = new TableColumn("DateTime");
      columnTime.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTime().toString()));
      tableView.getColumns().add(columnTime);

      if (checkBoxVoltage.isSelected()) {
         TableColumn<Momentum, Double> columnVoltage = new TableColumn("Voltage");
         columnVoltage.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getEnergy().getVoltage()).asObject());
         tableView.getColumns().add(columnVoltage);
      }

      if (checkBoxCurrent.isSelected()) {
         TableColumn<Momentum, Double> columnCurrent = new TableColumn("Current");
         columnCurrent.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getEnergy().getCurrent()).asObject());
         tableView.getColumns().add(columnCurrent);
      }

      if (checkBoxPower.isSelected()) {
         TableColumn<Momentum, Double> columnPower = new TableColumn("Power");
         columnPower.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getEnergy().getPower()).asObject());
         tableView.getColumns().add(columnPower);
      }
      try {
         ConstraintsInterval cons = constraintsInterval();
         System.out.println("Cons: " + cons);
         if (cons != null){
            System.out.println("ConsQuery: " + cons.getQuery());
            System.out.println("Constraints: " + cons.getConstraints());
         }
         for (Momentum m : Momentum.all(cons, Main.getUserBase())) {
            System.out.println(m.getTime().toString()+" => ");
            System.out.println(" => "+DateTime.secondsToDateTime(new DateTime(m.getTime().toString()).toSeconds(
               cons, Main.getUserBase()),cons, Main.getUserBase()).toString());
         }
         for (Momentum m : Momentum.all(cons, Main.getUserBase())) {
            tableView.getItems().add(m);
         }
         /*switch (textFieldDateBegin.getText().toLowerCase()) {
            case "any":
               switch (textFieldDateEnd.getText().toLowerCase()) {
                  case "any":
                     switch (textFieldTimeBegin.getText().toLowerCase()) {
                        case "any":
                           switch (textFieldTimeEnd.getText().toLowerCase()) {
                              case "any":
                                 for (Momentum m : Momentum.all(cons)) {
                                    tableView.getItems().add(m);
                                 }
                                 break;
                              default:
                                 for (Momentum m : Momentum.all(cons)) {
                                    if (DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime("0-1-0 " + textFieldTimeEnd.getText())), 3, 5, "<=")) {
                                       tableView.getItems().add(m);
                                    }
                                 }
                           }
                           break;
                        default:
                           switch (textFieldTimeEnd.getText().toLowerCase()) {
                              case "any":
                                 for (Momentum m : Momentum.all(cons)) {
                                    if (DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime("0-1-0 " + textFieldTimeBegin.getText())), 3, 5, ">=")) {
                                       tableView.getItems().add(m);
                                    }
                                 }
                                 break;
                              default:
                                 for (Momentum m : Momentum.all(cons)) {
                                    if (DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime("0-1-0 " + textFieldTimeEnd.getText())), 3, 5, "<=")&&
                                          DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime("0-1-0 " + textFieldTimeBegin.getText())), 3, 5, ">=")) {
                                       tableView.getItems().add(m);
                                    }
                                 }
                           }
                     }
                  break;
                  default:
                     switch (textFieldTimeBegin.getText().toLowerCase()) {
                        case "any":
                           switch (textFieldTimeEnd.getText().toLowerCase()) {
                              case "any":
                                 for (Momentum m : Momentum.all(cons)) {
                                    if (DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                       DateTime.toArr(new DateTime(textFieldDateEnd.getText()+" 0:0:0")), 0, 2, "<=")) {
                                       tableView.getItems().add(m);
                                    }
                                 }
                              break;
                              default:
                                 for (Momentum m : Momentum.all(cons)) {
                                    if (DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                       DateTime.toArr(new DateTime("0-1-0 "+textFieldTimeEnd.getText())), 3, 5, "<=")&&
                                       DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime(textFieldDateEnd.getText()+" 0:0:0")), 0, 2, "<=")) {
                                       tableView.getItems().add(m);
                                    }
                                 }
                              }
                           break;
                           default:
                              switch (textFieldTimeEnd.getText().toLowerCase()) {
                                 case "any":
                                    for (Momentum m : Momentum.all(cons)) {
                                       if (DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime("0-1-0 " + textFieldTimeBegin.getText())), 3, 5, ">=")&&
                                          DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime(textFieldDateEnd.getText()+" 0:0:0")), 0, 2, "<=")) {
                                          tableView.getItems().add(m);
                                       }
                                    }
                                    break;
                                 default:
                                    for (Momentum m : Momentum.all(cons)) {
                                       if (DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime("0-1-0 "+textFieldTimeEnd.getText())), 3, 5, "<=")&&
                                          DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                             DateTime.toArr(new DateTime(textFieldDateEnd.getText()+" 0:0:0")), 0, 2, "<=")&&
                                          DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                             DateTime.toArr(new DateTime("0-1-0 " + textFieldTimeBegin.getText())), 3, 5, ">=")) {
                                          tableView.getItems().add(m);
                                       }
                                    }
                              }
                        }
               }
            break;
            default:
               switch (textFieldDateEnd.getText().toLowerCase()) {
                  case "any":
                     switch (textFieldTimeBegin.getText().toLowerCase()) {
                        case "any":
                           switch (textFieldTimeEnd.getText().toLowerCase()) {
                              case "any":
                                 for (Momentum m : Momentum.all(cons)) {
                                    if(DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                       DateTime.toArr(new DateTime(textFieldDateBegin.getText()+" 0:0:0")), 0, 2, ">=")) {
                                       tableView.getItems().add(m);
                                    }
                                 }
                                 break;
                              default:
                                 for (Momentum m : Momentum.all(cons)) {
                                    if (DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime("0-1-0 " + textFieldTimeEnd.getText())), 3, 5, "<=")&&
                                       DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime(textFieldDateBegin.getText()+" 0:0:0")), 0, 2, ">=")) {
                                       tableView.getItems().add(m);
                                    }
                                 }
                           }
                           break;
                        default:
                           switch (textFieldTimeEnd.getText().toLowerCase()) {
                              case "any":
                                 for (Momentum m : Momentum.all(cons)) {
                                    if (DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                       DateTime.toArr(new DateTime("0-1-0 "+textFieldTimeBegin.getText())), 3, 5, ">=")&&
                                       DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime(textFieldDateBegin.getText()+" 0:0:0")), 0, 2, ">=")) {
                                       tableView.getItems().add(m);
                                    }
                                 }
                                 break;
                              default:
                                 for (Momentum m : Momentum.all(cons)) {
                                    if (DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                       DateTime.toArr(new DateTime("0-1-0 " + textFieldTimeEnd.getText())), 3, 5, "<=")&&
                                       DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime("0-1-0 "+textFieldTimeBegin.getText())), 3, 5, ">=")&&
                                       DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime(textFieldDateBegin.getText()+" 0:0:0")), 0, 2, ">=")) {
                                       tableView.getItems().add(m);
                                    }
                                 }
                           }
                     }
                     break;
                  default:
                     switch (textFieldTimeBegin.getText().toLowerCase()) {
                        case "any":
                           switch (textFieldTimeEnd.getText().toLowerCase()) {
                              case "any":
                                 for (Momentum m : Momentum.all(cons)) {
                                    if (DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                       DateTime.toArr(new DateTime(textFieldDateEnd.getText()+" 0:0:0")), 0, 2, "<=")&&
                                       DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime(textFieldDateBegin.getText()+" 0:0:0")), 0, 2, ">=")) {
                                       tableView.getItems().add(m);
                                    }
                                 }
                                 break;
                              default:
                                 for (Momentum m : Momentum.all(cons)) {
                                    if (DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                       DateTime.toArr(new DateTime("0-1-0 "+textFieldTimeEnd.getText())), 3, 5, "<=")&&
                                       DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime(textFieldDateEnd.getText()+" 0:0:0")), 0, 2, "<=")&&
                                       DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime(textFieldDateBegin.getText()+" 0:0:0")), 0, 2, ">=")) {
                                       tableView.getItems().add(m);
                                    }
                                 }
                           }
                           break;
                        default:
                           switch (textFieldTimeEnd.getText().toLowerCase()) {
                              case "any":
                                 for (Momentum m : Momentum.all(cons)) {
                                    if (DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                       DateTime.toArr(new DateTime("0-1-0 "+textFieldTimeBegin.getText())), 3, 5, ">=")&&
                                       DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime(textFieldDateBegin.getText()+" 0:0:0")), 0, 2, ">=")&&
                                       DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime(textFieldDateEnd.getText()+" 0:0:0")), 0, 2, "<=")) {
                                       tableView.getItems().add(m);
                                    }
                                 }
                                 break;
                              default:
                                 for (Momentum m : Momentum.all(cons)) {
                                    System.out.println(textFieldDateBegin.getText()+" "+textFieldTimeBegin.getText()+" ? "
                                       +m.getTime()+" ? "+textFieldDateEnd.getText()+" "+textFieldTimeEnd.getText());
                                    if (DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                       DateTime.toArr(new DateTime("0-1-0 "+textFieldTimeEnd.getText())), 3, 5, "<=")&&
                                       DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime("0-1-0 "+textFieldTimeBegin.getText())), 3, 5, ">=")&&
                                       DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime(textFieldDateEnd.getText()+" 0:0:0")), 0, 2, "<=")&&
                                       DateTime.compare(DateTime.toArr(new DateTime(m.getTime().toString())),
                                          DateTime.toArr(new DateTime(textFieldDateBegin.getText()+" 0:0:0")), 0, 2, ">=")) {
                                       tableView.getItems().add(m);
                                    }
                                 }
                           }
                     }
               }
         }*/
      }catch(Exception e){
         e.printStackTrace();
         Alert alert = new Alert(Alert.AlertType.WARNING);
         alert.setTitle("ERROR - Format error");
         alert.setHeaderText(e.toString());
         alert.setContentText(e.getMessage());
         alert.showAndWait();
      }
   }

   @SuppressWarnings("rawtypes")
   public void copySelectionToClipboard(final TableView<Momentum> table) {
      final Set<Integer> rows = new TreeSet<>();
      for (final TablePosition tablePosition : table.getSelectionModel().getSelectedCells()) {
         rows.add(tablePosition.getRow());
      }
      final StringBuilder strb = new StringBuilder();
      boolean firstRow = true;
      for (final Integer row : rows) {
         if (!firstRow) {
            strb.append('\n');
         }
         firstRow = false;
         boolean firstCol = true;
         for (final TableColumn<Momentum, ?> column : table.getColumns()) {
            if (!firstCol) {
               strb.append('\t');
            }
            firstCol = false;
            final Object cellData = column.getCellData(row);
            strb.append(cellData == null ? "\t" : cellData.toString());
         }
      }
      final ClipboardContent clipboardContent = new ClipboardContent();
      clipboardContent.putString(strb.toString());
      Clipboard.getSystemClipboard().setContent(clipboardContent);
   }
}