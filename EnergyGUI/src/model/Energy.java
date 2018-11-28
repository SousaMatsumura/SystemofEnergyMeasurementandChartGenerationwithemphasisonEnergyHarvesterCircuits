package model;

public class Energy {
   private Integer _id_energy;
   private double voltage;
   private double current;
   private double power;

   public Integer get_id_energy() {
      return _id_energy;
   }

   public Energy(Integer _id_energy, double voltage, double current, double power) {
      this._id_energy = _id_energy;
      this.voltage = voltage;
      this.current = current;
      this.power = power;
   }

   @Override
   public String toString(){
      return "ID("+ _id_energy +") voltage("+voltage+
         ") current("+current+") power("+power+")";
   }

   public double getVoltage() {
      return voltage;
   }

   public double getCurrent() {
      return current;
   }

   public double getPower() {
      return power;
   }
}
