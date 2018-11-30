package model;

public enum ChartType {
   NORMAL(1), INTEGRAL(2), DERIVATIVE(3);
   private int value = 1;
   ChartType(int value){this.value = value;}
   public int getValue(){return value;}

   @Override
   public String toString(){
      switch(value){
         case 1: return "Normal";
         case 2: return "Integral";
         case 3: return "Derivative";
         default: return null;
      }
   }
}
