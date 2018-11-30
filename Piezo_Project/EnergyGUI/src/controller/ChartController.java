package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import javafx.util.StringConverter;
import model.*;
import model.mysql.MySQLBase;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ChartController {

   private Chart chart;

   private static String last;

   private static ArrayList<Momentum> momentumList = new ArrayList<>();

   private static int timeMetric;

   @FXML
   private TextField textFieldDateBegin;

   @FXML
   private TextField textFieldDateEnd;

   @FXML
   private TextField textFieldTimeBegin;

   @FXML
   private TextField textFieldTimeEnd;

   @FXML
   private ChoiceBox<String> choiceBoxChart;
   ObservableList<String> chartList = FXCollections.observableArrayList(
      String.valueOf(ChartType.NORMAL.getValue())+" - "+ChartType.NORMAL.toString()+" Chart",
      String.valueOf(ChartType.INTEGRAL.getValue())+" - "+ChartType.INTEGRAL.toString()+" Chart",
      String.valueOf(ChartType.DERIVATIVE.getValue())+" - "+ChartType.DERIVATIVE.toString()+" Chart");

   @FXML
   private ChoiceBox<String> choiceBoxTime;
   ObservableList<String> timeList = FXCollections.observableArrayList(
      String.valueOf(TimeMetric.YEAR.getValue())+" - "+TimeMetric.YEAR.toString(),
      String.valueOf(TimeMetric.MONTH.getValue())+" - "+TimeMetric.MONTH.toString(),
      String.valueOf(TimeMetric.DAY.getValue())+" - "+TimeMetric.DAY.toString(),
      String.valueOf(TimeMetric.HOUR.getValue())+" - "+TimeMetric.HOUR.toString(),
      String.valueOf(TimeMetric.MINUTE.getValue())+" - "+TimeMetric.MINUTE.toString(),
      String.valueOf(TimeMetric.SECOND.getValue())+" - "+TimeMetric.SECOND.toString());

   @FXML
   private ChoiceBox<String> choiceBoxVoltage;
   ObservableList<String> voltageList = FXCollections.observableArrayList(
      String.valueOf(VoltageMetric.VOLT.getValue())+" - "+VoltageMetric.VOLT.toString(),
      String.valueOf(VoltageMetric.MILLIVOLT.getValue())+" - "+VoltageMetric.MILLIVOLT.toString(),
      String.valueOf(VoltageMetric.MICROVOLT.getValue())+" - "+VoltageMetric.MICROVOLT.toString(),
      String.valueOf(VoltageMetric.NULL.getValue())+" - "+VoltageMetric.NULL.toString());

   @FXML
   private ChoiceBox<String> choiceBoxCurrent;
   ObservableList<String> currentList = FXCollections.observableArrayList(
      String.valueOf(CurrentMetric.AMP.getValue())+" - "+CurrentMetric.AMP.toString(),
      String.valueOf(CurrentMetric.MILLIAMP.getValue())+" - "+CurrentMetric.MILLIAMP.toString(),
      String.valueOf(CurrentMetric.MICROAMP.getValue())+" - "+CurrentMetric.MICROAMP.toString(),
      String.valueOf(CurrentMetric.NULL.getValue())+" - "+CurrentMetric.NULL.toString());

   @FXML
   private ChoiceBox<String> choiceBoxPower;
   ObservableList<String> powerList = FXCollections.observableArrayList(
      String.valueOf(PowerMetric.WATT.getValue())+" - "+PowerMetric.WATT.toString(),
      String.valueOf(PowerMetric.MILLIWATT.getValue())+" - "+PowerMetric.MILLIWATT.toString(),
      String.valueOf(PowerMetric.MICROWATT.getValue())+" - "+PowerMetric.MICROWATT.toString(),
      String.valueOf(PowerMetric.NULL.getValue())+" - "+PowerMetric.NULL.toString());

   @FXML
   private Button buttonBack;

   @FXML
   private LineChart<Double, Double> lineChart;

   @FXML
   private NumberAxis x;

   @FXML
   private NumberAxis y;

   @FXML
   private Label labelSelected;

   @FXML
   protected void initialize(){
      timeMetric = Integer.parseInt(String.valueOf(choiceBoxTime.getValue().charAt(0)));
      buttonBack.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            Main.changeScreen("menu");
         }
      });
      textFieldDateBegin.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            update(Main.getUserBase());
         }
      });
      textFieldDateEnd.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            update(Main.getUserBase());
         }
      });
      textFieldTimeBegin.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            update(Main.getUserBase());
         }
      });
      textFieldTimeEnd.setOnKeyPressed(e -> {
         if (e.getCode() == KeyCode.ENTER) {
            update(Main.getUserBase());
         }
      });
      last = "?";
      XTickLabelFormatter(Main.getUserBase());
      y.setTickLabelFormatter(new StringConverter<Number>() {
         @Override
         public String toString(Number object) {
            double value = object.doubleValue();
            return String.valueOf(DateTime.roundUp(value, 1));
         }

         @Override
         public Number fromString(String string) {
            return null;
         }
      });

      /*Timer timer = new Timer();
      timer.scheduleAtFixedRate(ChartWorker, 1000, 1000);*/

      chartWorker();

      Main.addOnChangeScreenListener(new Main.OnChangeScreen() {
         @Override
         public void onScreenChanged(String newScreen, Object userData) {
            if (newScreen.equals("chart")) {
               choiceBoxChart.setItems(chartList);
               choiceBoxChart.getSelectionModel().select(0);
               choiceBoxTime.setItems(timeList);
               choiceBoxVoltage.setItems(voltageList);
               choiceBoxCurrent.setItems(currentList);
               choiceBoxPower.setItems(powerList);
               try {
                  chart = new Chart(choiceBoxChart.getValue(), textFieldDateBegin.getText(), textFieldDateEnd.getText(),
                      textFieldTimeBegin.getText(), textFieldTimeEnd.getText(), choiceBoxTime.getValue(),
                      choiceBoxVoltage.getValue(), choiceBoxCurrent.getValue(), choiceBoxPower.getValue());
               }catch(Exception e){
                  e.printStackTrace();
                  Alert alert = new Alert(Alert.AlertType.WARNING);
                  alert.setTitle("ERROR - Format error");
                  alert.setHeaderText(e.toString());
                  alert.setContentText(e.getMessage());
                  alert.showAndWait();
               }
               update(Main.getUserBase());
            }
         }
      });
   }

   /*TimerTask ChartWorker = new TimerTask() {
      @Override
      public void run() {
         if(momentumList.size() < Momentum.length(constraintsInterval(), Main.getRootBase())){
            update(Main.getRootBase());
         }
      }
   };*/

   private void chartWorker(){
      Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
         if(momentumList.size() < Momentum.length(constraintsInterval(), Main.getRootBase())){
            update(Main.getRootBase());
         }
      }));
      timeline.setCycleCount(Timeline.INDEFINITE);
      timeline.play();
   }

   @FXML
   private void XTickLabelFormatter(MySQLBase base){
      x.setTickLabelFormatter(new StringConverter<Number>() {
         @Override
         public String toString(Number object) {
            double value = object.doubleValue();
            switch(timeMetric){
               case 6:
                  if (value >= 0) {
                     return String.valueOf(DateTime.roundUp(value,4));
                  } else {
                     //return "yyyy-MM-dd hh:mm:ss.SSSS";
                     return "seconds";
                  }
               case 5:
                  if(value < 0 && !last.equals("minutes")){
                     //return "yyyy-MM-dd hh:mm:ss.SSSS";
                     last = "minutes";
                     return last;
                  }else {
                     if(!last.equals(String.valueOf(DateTime.roundDown(value/60)))) {
                        last = String.valueOf(DateTime.roundDown(value/60));
                        return last;
                     }else{
                        return " ";
                     }
                  }
               case 4:
                  if(value < 0 && !last.equals("hours")){
                     last = "hours";
                     return last;
                  }else {
                     if(!last.equals(String.valueOf(DateTime.roundDown(value/3600)))) {
                        last = String.valueOf(DateTime.roundDown(value/3600));
                        return last;
                     }else{
                        return " ";
                     }
                  }
               case 3:
                  if(value < 0 && !last.equals("days")){
                     last = "days";
                     return last;
                  }else {
                     if(!last.equals(String.valueOf(DateTime.roundDown(value/86400)))) {
                        last = String.valueOf(DateTime.roundDown(value/86400));
                        return last;
                     }else{
                        return " ";
                     }
                  }
               case 2:
                  if(value < 0 && !last.equals("months")){
                     //return "yyyy-MM-dd hh:mm:ss.SSSS";
                     last = "months";
                     return last;
                  }else {
                     if(!last.equals(MonthName.getString(DateTime.secondsToDateTime(value, constraintsInterval(), base).getMonth()))) {
                        last = MonthName.getString(DateTime.secondsToDateTime(value, constraintsInterval(), base).getMonth());
                        return last;
                     }else{
                        return " ";
                     }
                  }
               case 1:
                  if(value < 0 && !last.equals("years")){
                     //return "yyyy-MM-dd hh:mm:ss.SSSS";
                     last = "years";
                     return last;
                  }else {
                     if(!last.equals(String.valueOf(DateTime.secondsToDateTime(value, constraintsInterval(), base).getYear()))) {
                        last = String.valueOf(DateTime.secondsToDateTime(value, constraintsInterval(), base).getYear());
                        return last;
                     }else{
                        return " ";
                     }
                  }
               default: return " ";
            }
         }

         @Override
         public Number fromString(String string) {
            return null;
         }
      });
   }

   @FXML
   void buttonBackAction(ActionEvent event) {
      Main.changeScreen("menu");
   }

   @FXML
   private void updateScreen(ActionEvent event) {
      timeMetric = Integer.parseInt(String.valueOf(choiceBoxTime.getValue().charAt(0)));
      update(Main.getUserBase());
   }

   @FXML
   private String voltageFormatter(double value){
      switch(Integer.valueOf(String.valueOf(choiceBoxVoltage.getValue().charAt(0)))){
         case 1: return String.format("%.8f",DateTime.roundUp(value,8))+" V";
         case 2: return String.format("%.8f",DateTime.roundUp(value,8))+" mV";
         case 3: return String.format("%.8f",DateTime.roundUp(value,8))+" µV";
         default: return " ";
      }
   }

   @FXML
   private String currentFormatter(double value){
      switch(Integer.valueOf(String.valueOf(choiceBoxCurrent.getValue().charAt(0)))){
         case 1: return String.format("%.8f",DateTime.roundUp(value,8))+" A";
         case 2: return String.format("%.8f",DateTime.roundUp(value,8))+" mA";
         case 3: return String.format("%.8f",DateTime.roundUp(value,8))+" µA";
         default: return " ";
      }
   }

   @FXML
   private String powerFormatter(double value){
      switch(Integer.valueOf(String.valueOf(choiceBoxPower.getValue().charAt(0)))){
         case 1: return String.format("%.8f",DateTime.roundUp(value,8))+" W";
         case 2: return String.format("%.8f",DateTime.roundUp(value,8))+" mW";
         case 3: return String.format("%.8f",DateTime.roundUp(value,8))+" µW";
         default: return " ";
      }
   }

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
   public void update(MySQLBase base) {
      lineChart.getData().clear();
      momentumList.clear();
      try{
         ConstraintsInterval cons = constraintsInterval();
         momentumList = new ArrayList<>(Momentum.all(cons, base));
         double upperX, lowerX;
         if(momentumList.size() > 0) {
            if (choiceBoxVoltage.getValue() != null && choiceBoxCurrent.getValue() != null &&
                      choiceBoxPower.getValue() != null && choiceBoxTime.getValue() != null) {
               upperX = new DateTime(Momentum.newest(cons, base).toString()).toSeconds(cons, base);
               lowerX = new DateTime(Momentum.older(cons, base).toString()).toSeconds(cons, base);
               double upperY = Momentum.upperValue(
                     Integer.parseInt(choiceBoxVoltage.getValue().substring(0, 1)),
                     Integer.parseInt(choiceBoxCurrent.getValue().substring(0, 1)),
                     Integer.parseInt(choiceBoxPower.getValue().substring(0, 1)), cons, base);
               double lowerY = 0.;
               upperX += (upperX - lowerX) / 50;
               lowerX -= (upperX - lowerX) / 50;
               upperY += upperY / 40;
               lowerY -= upperY / 40;
               System.out.println("UpperY: " + String.format("%.8f",upperY));

               x.setAutoRanging(false);
               x.setLowerBound(lowerX);
               x.setUpperBound(upperX);
               x.setTickUnit((upperX - lowerX)/10);

               y.setAutoRanging(false);
               y.setLowerBound(lowerY);
               y.setUpperBound(upperY);
               y.setTickUnit(upperY / 8);
            }
            XTickLabelFormatter(base);
            if (choiceBoxVoltage.getValue() != null && !choiceBoxVoltage.getValue().substring(0, 1).equals("4")) {
               XYChart.Series<Double, Double> voltageSeries = new XYChart.Series<>();
               for (int i = 0; i < momentumList.size(); i++) {
                  switch(Integer.valueOf(String.valueOf(choiceBoxVoltage.getValue().charAt(0)))) {
                     case 3: voltageSeries.getData().add(new XYChart.Data<>(
                        new DateTime(momentumList.get(i).getTime().toString()).toSeconds(cons, base),
                           momentumList.get(i).getEnergy().getVoltage()/1000000));
                        break;
                     case 2: voltageSeries.getData().add(new XYChart.Data<>(
                        new DateTime(momentumList.get(i).getTime().toString()).toSeconds(cons, base),
                           momentumList.get(i).getEnergy().getVoltage()/1000));
                        break;
                     case 1: voltageSeries.getData().add(new XYChart.Data<>(
                        new DateTime(momentumList.get(i).getTime().toString()).toSeconds(cons, base),
                           momentumList.get(i).getEnergy().getVoltage()));
                  }
                  double temp = new DateTime(momentumList.get(i).getTime().toString()).toSeconds(cons, base);
                  voltageSeries.getData().add(new XYChart.Data<>(temp+(x.getTickUnit()*0.045),.0));
                  voltageSeries.getData().add(new XYChart.Data<>(temp-(x.getTickUnit()*0.045),.0));
                  if(i==0){
                     voltageSeries.getData().add(new XYChart.Data<>(temp-(2*x.getTickUnit()),.0));
                  }
                  if(i == (momentumList.size()-1)){
                     voltageSeries.getData().add(new XYChart.Data<>(temp+(2*x.getTickUnit()),.0));
                  }
               }
               voltageSeries.setName("Voltage (Volts)");
               lineChart.getData().addAll(voltageSeries);
               for (final XYChart.Data<Double, Double> data : voltageSeries.getData()) {
                  data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                     @Override
                     public void handle(MouseEvent event) {
                        if(data.getYValue()>0){
                           labelSelected.setText("X: " + DateTime.secondsToDateTime(data.getXValue(), cons, base) +
                              "\t\t\tY: " + voltageFormatter(data.getYValue()));
                        }
                     }
                  });
               }
            }
            if (choiceBoxCurrent.getValue() != null && !choiceBoxCurrent.getValue().substring(0, 1).equals("4")) {
               XYChart.Series<Double, Double> currentSeries = new XYChart.Series<>();
               /*
                  Fazer código para média!
               */
               //int k = -1;
               for (int i = 0; i<momentumList.size(); i++) {
                  /*if(i+1<momentumList.size() && new DateTime(momentumList.get(i+1).getTime().toString()).toSeconds(cons)
                     -new DateTime(momentumList.get(i).getTime().toString()).toSeconds(cons) < x.getTickUnit()*.09){

                  }else {*/

                  switch (Integer.valueOf(String.valueOf(choiceBoxCurrent.getValue().charAt(0)))) {
                     case 3:
                        currentSeries.getData().add(new XYChart.Data<>(
                           new DateTime(momentumList.get(i).getTime().toString()).toSeconds(cons, base),
                           momentumList.get(i).getEnergy().getCurrent() / 1000000));
                        break;
                     case 2:
                        currentSeries.getData().add(new XYChart.Data<>(
                           new DateTime(momentumList.get(i).getTime().toString()).toSeconds(cons, base),
                           momentumList.get(i).getEnergy().getCurrent() / 1000));
                        break;
                     case 1:
                        currentSeries.getData().add(new XYChart.Data<>(
                           new DateTime(momentumList.get(i).getTime().toString()).toSeconds(cons, base),
                           momentumList.get(i).getEnergy().getCurrent()));
                  }
                  double temp = new DateTime(momentumList.get(i).getTime().toString()).toSeconds(cons, base);
                  currentSeries.getData().add(new XYChart.Data<>(temp + (x.getTickUnit() * 0.045), .0));
                  currentSeries.getData().add(new XYChart.Data<>(temp - (x.getTickUnit() * 0.045), .0));
                  if (i == 0) {
                     currentSeries.getData().add(new XYChart.Data<>(temp - (x.getTickUnit() * 2), .0));
                  }
                  if (i == (momentumList.size() - 1)) {
                     currentSeries.getData().add(new XYChart.Data<>(temp + (x.getTickUnit() * 2), .0));
                  }
                  //}
               }
               currentSeries.setName("Current (Amps)");
               lineChart.getData().addAll(currentSeries);
               for (final XYChart.Data<Double, Double> data : currentSeries.getData()) {
                  data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                     @Override
                     public void handle(MouseEvent event) {
                        if(data.getYValue()>0){
                           labelSelected.setText("X: " + DateTime.secondsToDateTime(data.getXValue(), cons, base) +
                              "\t\t\tY: " + currentFormatter(data.getYValue()));
                        }
                     }
                  });
               }
            }
            if (choiceBoxPower.getValue() != null && !choiceBoxPower.getValue().substring(0, 1).equals("4")) {
               XYChart.Series<Double, Double> powerSeries = new XYChart.Series<>();
               for (int i = 0; i < momentumList.size(); i++) {
                  switch(Integer.valueOf(String.valueOf(choiceBoxPower.getValue().charAt(0)))) {
                     case 3: powerSeries.getData().add(new XYChart.Data<>(
                        new DateTime(momentumList.get(i).getTime().toString()).toSeconds(cons, base),
                           momentumList.get(i).getEnergy().getPower()/1000000));
                        break;
                     case 2: powerSeries.getData().add(new XYChart.Data<>(
                        new DateTime(momentumList.get(i).getTime().toString()).toSeconds(cons,base),
                           momentumList.get(i).getEnergy().getPower()/1000));
                        break;
                     case 1: powerSeries.getData().add(new XYChart.Data<>(
                        new DateTime(momentumList.get(i).getTime().toString()).toSeconds(cons, base),
                           momentumList.get(i).getEnergy().getPower()));
                  }
                  double temp = new DateTime(momentumList.get(i).getTime().toString()).toSeconds(cons, base);
                  powerSeries.getData().add(new XYChart.Data<>(temp+(x.getTickUnit()*0.045),.0));
                  powerSeries.getData().add(new XYChart.Data<>(temp-(x.getTickUnit()*0.045),.0));
                  if(i==0){
                     powerSeries.getData().add(new XYChart.Data<>(temp-(2*x.getTickUnit()),.0));
                  }
                  if(i == (momentumList.size()-1)){
                     powerSeries.getData().add(new XYChart.Data<>(temp+(2*x.getTickUnit()),.0));
                  }
               }
               powerSeries.setName("Power (Watts)");
               lineChart.getData().addAll(powerSeries);
               for (final XYChart.Data<Double, Double> data : powerSeries.getData()) {
                  data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                     @Override
                     public void handle(MouseEvent event) {
                        if(data.getYValue()>0){
                           labelSelected.setText("X: " + DateTime.secondsToDateTime(data.getXValue(), cons, base) +
                              "\t\t\tY: " + powerFormatter(data.getYValue()));
                        }
                     }
                  });
               }
            }
         }
      }catch (Exception e){
         e.printStackTrace();
      }
   }
}