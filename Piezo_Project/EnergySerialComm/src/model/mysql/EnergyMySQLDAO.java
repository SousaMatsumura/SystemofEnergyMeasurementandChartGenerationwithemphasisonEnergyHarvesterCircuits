package model.mysql;

import controller.Main;
import model.Energy;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class EnergyMySQLDAO{
   public EnergyMySQLDAO(){
      Main.getMySQLBase().open();
   }

   public void create(Energy e){
      Main.getMySQLBase().open();
      try{
         PreparedStatement stm =
            Main.getMySQLBase().conn.prepareStatement("call create_energy(?,?)");
         stm.setString(1, String.valueOf(e.getVoltage()));
         stm.setString(2, String.valueOf(e.getCurrent()));
         stm.executeUpdate();
      }catch(SQLException ex){
         ex.printStackTrace();
      }finally{
         Main.getMySQLBase().close();
      }
   }
}
