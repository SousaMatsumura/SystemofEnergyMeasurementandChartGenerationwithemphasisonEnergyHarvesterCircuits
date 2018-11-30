package model;

import model.mysql.MomentumMySQLDAO;
import model.mysql.MySQLBase;

import java.sql.Timestamp;
import java.util.List;

public class Momentum{
   private Timestamp time;
   private Energy energy;

   public Momentum(Integer _id_energy, double voltage, double current, double power, Timestamp time) {
      this.energy = new Energy(_id_energy, voltage, current, power);
      this.time = time;
   }

   @Override
   public String toString(){
      return "ID("+ energy.get_id_energy()+") time("+time+") voltage("+energy.getVoltage()+
         ") current("+energy.getCurrent()+") power("+energy.getPower()+")";
   }

    public Timestamp getTime() {
        return time;
    }

    public Energy getEnergy() {
        return energy;
    }
   
   //-----------------DAO

   private static MomentumMySQLDAO dao = new MomentumMySQLDAO();

   public static List<Momentum> all(ConstraintsInterval cons, MySQLBase base){
      return dao.all(cons, base);
   }

   public static Integer length(ConstraintsInterval cons, MySQLBase base){
      return dao.length(cons, base);
   }

   public static Timestamp newest(ConstraintsInterval cons, MySQLBase base){
      return dao.newest(cons, base);
   }

   public static Timestamp older(ConstraintsInterval cons, MySQLBase base){
      return dao.older(cons, base);
   }

   public static Double upperValue(int v, int c, int p, ConstraintsInterval cons, MySQLBase base){
      return dao.upperValue(v,c,p,cons, base);
   }

   public static Momentum find(int pk, ConstraintsInterval cons, MySQLBase base){
      return dao.find(pk,cons, base);
   }
}
