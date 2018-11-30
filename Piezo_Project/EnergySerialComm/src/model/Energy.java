package model;

import model.mysql.EnergyMySQLDAO;

public class Energy {
   private double voltage;
   private double current;

   public Energy(double voltage, double current) {
      this.voltage = voltage;
      this.current = current;
   }

   @Override
   public String toString(){
      return "Energy -> voltage("+voltage+") current("+current+") power("+voltage*current+")";
   }

   public double getVoltage() {
      return voltage;
   }

   public double getCurrent() {
      return current;
   }

//-------------DAO

   private static EnergyMySQLDAO dao = new EnergyMySQLDAO();

   public void create(){
      dao.create(this);
   }
}
