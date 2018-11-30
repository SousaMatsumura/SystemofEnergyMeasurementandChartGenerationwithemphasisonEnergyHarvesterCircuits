package model;

public enum TimeMetric {
   YEAR(1), MONTH(2), DAY(3), HOUR(4), MINUTE(5), SECOND(6);
   private int value = 1;
   TimeMetric(int value){this.value = value;}
   public int getValue(){return value;}

   @Override
   public String toString(){
      switch(value){
         case 1: return "Year";
         case 2: return "Month";
         case 3: return "Day";
         case 4: return "Hour";
         case 5: return "Minute";
         case 6: return "Second";
         default: return null;
      }
   }
}
