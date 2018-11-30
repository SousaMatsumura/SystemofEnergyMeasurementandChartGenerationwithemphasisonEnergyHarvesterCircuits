package model;

public enum VoltageMetric {
   VOLT(1), MILLIVOLT(2), MICROVOLT(3), NULL(4);
   private int value = 1;
   VoltageMetric(int value){this.value = value;}
   public int getValue(){return value;}

   @Override
   public String toString(){
      switch(value){
         case 1: return "Volt (V)";
         case 2: return "Millivolt (mV)";
         case 3: return "Microvolt (uV)";
         case 4: return "Null";
         default: return null;
      }
   }
}
