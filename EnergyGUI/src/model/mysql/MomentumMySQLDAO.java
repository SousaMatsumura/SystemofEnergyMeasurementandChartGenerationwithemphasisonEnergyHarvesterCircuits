package model.mysql;

import controller.Main;
import model.ConstraintsInterval;
import model.Momentum;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MomentumMySQLDAO{

   public List<Momentum> all(ConstraintsInterval cons, MySQLBase base){
      ArrayList<Momentum> result = new ArrayList<>();
      base.open();
      try{
         PreparedStatement stm;
         if(cons == null) {
            stm = base.conn.prepareStatement(
               "select energy.id_energy, energy.voltage, energy.`current`, energy.power, momentum.`time`" +
               "  from energy, momentum where energy.id_energy = momentum.id_energy order by `time` ASC;");
         }else{
            stm = base.conn.prepareStatement(
                   "select energy.id_energy, energy.voltage, energy.`current`, energy.power, momentum.`time`" +
                   "  from energy, momentum where energy.id_energy = momentum.id_energy"+cons.getQuery()+" order by `time` ASC;");
            for(int i = 0;i<cons.getConstraints().size(); i++) {
               stm.setString(i+1, cons.getConstraints().get(i));
            }
         }
         ResultSet rs = stm.executeQuery();
         while(rs.next()){
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Brazil/East"));
            Momentum e = new Momentum(
               rs.getInt("id_energy"),
               rs.getDouble("voltage"),
               rs.getDouble("current"),
               rs.getDouble("power"),
               rs.getTimestamp("time",cal));
            result.add(e);
         }
      }catch(SQLException e){
         e.printStackTrace();
      }finally{
         base.close();
         return result;
      }
   }

   public Integer length(ConstraintsInterval cons, MySQLBase base){
      Integer result = null;
      base.open();
      try{
         PreparedStatement stm;
         if(cons == null) {
            stm = base.conn.prepareStatement(
               "select COUNT(energy.id_energy) as 'length' from energy, momentum"+
               " where energy.id_energy = momentum.id_energy;");
         }else{
            stm = base.conn.prepareStatement(
               "select COUNT(energy.id_energy) as 'length' from energy, momentum"+
               " where energy.id_energy = momentum.id_energy"+cons.getQuery()+";");
            for(int i = 0;i<cons.getConstraints().size(); i++) {
               stm.setString(i+1, cons.getConstraints().get(i));
            }
         }
         ResultSet rs = stm.executeQuery();
         if(rs.next()){
            result = rs.getInt("length");
         }
      }catch(SQLException e){
         e.printStackTrace();
      }finally{
         base.close();
         return result;
      }
   }

   public Momentum find(int pk, ConstraintsInterval cons, MySQLBase base){
      Momentum result = null;
      base.conn = base.open();
      try {
         PreparedStatement stm;
         if (cons == null){
            stm = base.conn.prepareStatement(
               "select energy.id_energy, energy.voltage, energy.`current`, energy.power, momentum.`time`" +
               "from energy, momentum where energy.id_energy = momentum.id_energy and energy.id_energy = ?;");
            stm.setInt(1, pk);
         }else{
            stm = base.conn.prepareStatement(
               "select energy.id_energy, energy.voltage, energy.`current`, energy.power, momentum.`time`" +
               "from energy, momentum where energy.id_energy = momentum.id_energy and energy.id_energy = ?"+
               cons.getQuery()+";");
            stm.setInt(1, pk);
            for(int i = 0;i<cons.getConstraints().size(); i++) {
               stm.setString(i+2, cons.getConstraints().get(i));
            }
         }
         ResultSet rs = stm.executeQuery();
         if(rs.next()){
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Brazil/East"));
            Momentum m = new Momentum(
               rs.getInt("id_energy"),
               rs.getDouble("voltage"),
               rs.getDouble("current"),
               rs.getDouble("power"),
               rs.getTimestamp("time", cal));
            result = m;
         }
      }catch(SQLException e){
         e.printStackTrace();
      }finally{
         base.close();
         return result;
      }
   }

   public Timestamp newest(ConstraintsInterval cons, MySQLBase base){
      Timestamp result = null;
      base.conn = base.open();
      try{
         PreparedStatement stm;
         if(cons == null) {
            stm = base.conn.prepareStatement("select max(`time`) as 'time' from momentum;");
         }else{
            stm = base.conn.prepareStatement("select max(`time`) as 'time' from momentum, energy " +
               "where energy.id_energy = momentum.id_energy "+cons.getQuery()+";");
            for(int i = 0;i<cons.getConstraints().size(); i++) {
               stm.setString(i+1, cons.getConstraints().get(i));
            }
         }
         ResultSet rs = stm.executeQuery();
         Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Brazil/East"));
         if(rs != null && rs.next()){
            result = rs.getTimestamp("time", cal);
         }
      }catch(SQLException e){
         e.printStackTrace();
      }finally{
         base.close();
         return result;
      }
   }

   public Timestamp older(ConstraintsInterval cons, MySQLBase base){
      Timestamp result = null;
      base.conn = base.open();
      try{
         PreparedStatement stm;
         if(cons == null) {
            stm = base.conn.prepareStatement("select min(`time`) as 'time' from momentum;");
         }else{
            stm = base.conn.prepareStatement("select min(`time`) as 'time' from momentum, energy " +
               "where energy.id_energy = momentum.id_energy "+cons.getQuery()+";");
            for(int i = 0;i<cons.getConstraints().size(); i++) {
               stm.setString(i+1, cons.getConstraints().get(i));
            }
         }
         ResultSet rs = stm.executeQuery();
         Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Brazil/East"));
         if(rs != null && rs.next()){
            result = rs.getTimestamp("time", cal);
         }
      }catch(SQLException e){
         e.printStackTrace();
      }finally{
         base.close();
         return result;
      }
   }

   public Double upperValue(int v, int c, int p, ConstraintsInterval cons, MySQLBase base){
      base.conn = base.open();
      double voltage = 0, current= 0, power = 0;
      try{
         PreparedStatement stm;
         if(cons == null){
            stm = base.conn.prepareStatement(
               "select max(voltage) as 'voltage', max(`current`) as 'current', max(power) as 'power' from energy;");
         }else{
            stm = base.conn.prepareStatement(
               "select max(voltage) as 'voltage', max(`current`) as 'current', max(power) as 'power' from energy, momentum " +
               "where energy.id_energy = momentum.id_energy"+cons.getQuery()+";");
            for(int i = 0;i<cons.getConstraints().size(); i++) {
               stm.setString(i+1, cons.getConstraints().get(i));
            }
         }
         ResultSet rs = stm.executeQuery();
         if(rs != null && rs.next()){
            voltage = rs.getDouble("voltage");
            current = rs.getDouble("current");
            power = rs.getDouble("power");
         }
      }catch(SQLException e){
         e.printStackTrace();
      }finally{
         base.close();
      }
      switch(v){
         case 3 : voltage /= 1000000; break;
         case 2 : voltage /= 1000; break;
         case 4 : voltage =0;
      }
      switch(c){
         case 3 : current /= 1000000; break;
         case 2 : current /= 1000; break;
         case 4 : current = 0;
      }
      switch(p){
         case 3 : power /= 1000000; break;
         case 2 : power /= 1000; break;
         case 4 : power = 0;
      }
      if(voltage > current){
         if(voltage > power){
            System.out.println("Voltage: "+String.format("%.8f",voltage)+"\nCurrent: "+String.format("%.8f",current)+"\nPower: "+String.format("%.8f",power));
            System.out.println("VOLTAGE");
            return voltage;
         }else{
            System.out.println("Voltage: "+String.format("%.8f",voltage)+"\nCurrent: "+String.format("%.8f",current)+"\nPower: "+String.format("%.8f",power));
            System.out.println("POWER");
            return power;
         }
      }else{
         if(current > power){
            System.out.println("Voltage: "+String.format("%.8f",voltage)+"\nCurrent: "+String.format("%.8f",current)+"\nPower: "+String.format("%.8f",power));
            System.out.println("CURRENT");
            return current;
         }else{
            System.out.println("Voltage: "+String.format("%.8f",voltage)+"\nCurrent: "+String.format("%.8f",current)+"\nPower: "+String.format("%.8f",power));
            System.out.println("POWER");
            return power;
         }
      }
   }
}