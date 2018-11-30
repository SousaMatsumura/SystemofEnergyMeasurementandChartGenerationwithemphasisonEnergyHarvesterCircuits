package model;

public class Chart {
   private String chartType;
   private String dateBegin;
   private String dateEnd;
   private String timeBegin;
   private String timeEnd;
   private String timeMetric;
   private String voltageMetric;
   private String currentMetric;
   private String powerMetric;

   public Chart(String chartType, String dateBegin, String dateEnd, String timeBegin, String timeEnd, String timeMetric, String voltageMetric, String currentMetric, String powerMetric) throws Exception{
      if(!dateBegin.equalsIgnoreCase("any")) {
         new DateTime(dateBegin + " 0:0:0");
      }
      if(!dateEnd.equalsIgnoreCase("any")) {
         new DateTime(dateEnd+" 0:0:0");
      }
      if(!timeBegin.equalsIgnoreCase("any")) {
         new DateTime("0-1-0 "+timeBegin);
      }
      if(!timeEnd.equalsIgnoreCase("any")) {
         new DateTime("0-1-0 "+timeEnd);
      }
      this.chartType = chartType;
      this.dateBegin = dateBegin;
      this.dateEnd = dateEnd;
      this.timeBegin = timeBegin;
      this.timeEnd = timeEnd;
      this.timeMetric = timeMetric;
      this.voltageMetric = voltageMetric;
      this.currentMetric = currentMetric;
      this.powerMetric = powerMetric;
   }

   public String getChartType() {
      return chartType;
   }

   public String getDateBegin() {
      return dateBegin;
   }

   public String getDateEnd() {
      return dateEnd;
   }

   public String getTimeBegin() {
      return timeBegin;
   }

   public String getTimeEnd() {
      return timeEnd;
   }

   public String getTimeMetric() {
      return timeMetric;
   }

   public String getVoltageMetric() {
      return voltageMetric;
   }

   public String getCurrentMetric() {
      return currentMetric;
   }

   public String getPowerMetric() {
      return powerMetric;
   }

   public boolean equals(Chart chart){
      if(this.chartType.equals(chart.getChartType()) && this.dateBegin.equalsIgnoreCase(chart.getDateBegin()) &&
         this.dateEnd.equalsIgnoreCase(chart.getDateEnd()) && this.timeBegin.equalsIgnoreCase(chart.getTimeBegin()) &&
         this.timeEnd.equalsIgnoreCase(chart.getTimeEnd()) && this.timeMetric.equals(chart.getTimeMetric()) &&
         this.voltageMetric.equals(chart.getVoltageMetric()) && this.currentMetric.equals(chart.getCurrentMetric()) &&
         this.powerMetric.equals(chart.getPowerMetric())){
         return true;
      }else{
         return false;
      }
   }
}
