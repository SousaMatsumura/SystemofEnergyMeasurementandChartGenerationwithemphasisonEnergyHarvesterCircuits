package model;

public enum MonthName {
   JAN(1), FEB(2), MAR(3), APR(4), MAY(5), JUN(6), JUL(7), AUG(8), SET(9), OCT(10), NOV(11), DEZ(12);
   /*Jan Feb Mar Apr May Jun Jul Aug Set Oct Nov Dec*/
   private int value = 1;
   MonthName(int value){this.value = value;}
   public int getValue(){return value;}

   public static String getString(int v){
      switch(v){
         case 1: return "Jan";
         case 2: return "Feb";
         case 3: return "Mar";
         case 4: return "Apr";
         case 5: return "May";
         case 6: return "Jun";
         case 7: return "Jul";
         case 8: return "Aug";
         case 9: return "Set";
         case 10: return "Oct";
         case 11: return "Nov";
         case 12: return "Dez";
         default: return null;
      }
   }

   @Override
   public String toString(){
      switch(value){
         case 1: return "Jan";
         case 2: return "Feb";
         case 3: return "Mar";
         case 4: return "Apr";
         case 5: return "May";
         case 6: return "Jun";
         case 7: return "Jul";
         case 8: return "Aug";
         case 9: return "Set";
         case 10: return "Oct";
         case 11: return "Nov";
         case 12: return "Dez";
         default: return null;
      }
   }
}