package model;

public enum CurrentMetric {
   AMP(1), MILLIAMP(2), MICROAMP(3), NULL(4);
   private int value = 1;
   CurrentMetric(int value){this.value = value;}
   public int getValue(){return value;}

   @Override
   public String toString(){
      switch(value){
         case 1: return "Amp (A)";
         case 2: return "Milliamp (mA)";
         case 3: return "Microamp (uA)";
         case 4: return "Null";
         default: return null;
      }
   }
}
