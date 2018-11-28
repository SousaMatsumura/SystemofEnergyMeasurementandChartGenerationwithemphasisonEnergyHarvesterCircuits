package model;

public enum PowerMetric {
   WATT(1), MILLIWATT(2), MICROWATT(3), NULL(4);
   private int value = 1;
   PowerMetric(int value){this.value = value;}
   public int getValue(){return value;}

   @Override
   public String toString(){
      switch(value){
         case 1: return "Watt (W)";
         case 2: return "Milliwatt (mW)";
         case 3: return "Microwatt (uW)";
         case 4: return "Null";
         default: return null;
      }
   }
}
