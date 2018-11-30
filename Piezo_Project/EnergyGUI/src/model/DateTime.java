package model;

import model.mysql.MySQLBase;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.SchemaOutputResolver;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

public class DateTime {
   private int year;
   private int month;
   private int day;
   private int hour;
   private int minute;
   private double second;

   public DateTime(String s) throws Exception{
      String[] tokens = StringUtils.split(s, " ");
      String[] date = StringUtils.split(tokens[0], "-");
      String[] time = StringUtils.split(tokens[1], ":");
      if(time[0] == "19") {
         System.out.println("DATETIME: " + s);
      }
      /*for(String r : tokens){
         System.out.println(r);
      }
      for(String t : date){
         System.out.println(t);
      }
      for(String u : time){
         System.out.println(u);
      }*/

      if(tokens.length != 2 || date.length != 3 || time.length != 3){
         throw new Exception("Format yyyy-MM-dd hh:mm:ss.SSSS not match.");
      }
      if(Double.parseDouble(time[2]) < 0 || Double.parseDouble(time[2]) > 60 ){
         System.out.println("SECONDS EXCEPTION: "+time[2]);
         throw new Exception("Invalid seconds.");
      }
      if(Integer.parseInt(time[1]) < 0 || Integer.parseInt(time[1]) > 60 ){
         throw new Exception("Invalid minutes.");
      }
      if(Integer.parseInt(time[0]) < 0 || Integer.parseInt(time[0]) >= 24 ){
         throw new Exception("Invalid hours.");
      }
      switch (Integer.parseInt(date[1])){
         case 1 : case 3 : case 5 : case 7 : case 8 : case 10 : case 12 :
            if(Integer.parseInt(date[2]) > 31){
               throw new Exception("Invalid day.");
            }
         break;
         case 4: case 6: case 9: case 11:
            if(Integer.parseInt(date[2]) > 30){
               throw new Exception("Invalid day.");
            }
         break;
         case 2:
            if(Integer.parseInt(date[0])%400 == 0 || (Integer.parseInt(date[0])%4 == 0 && Integer.parseInt(date[0])%100 != 0)){
               if(Integer.parseInt(date[2]) > 29){
                  throw new Exception("Invalid day.");
               }
            }else{
               if(Integer.parseInt(date[2]) > 28){
                  throw new Exception("Invalid day.");
               }
            }

         break;
         default: throw new Exception("Invalid month.");
      }
      this.year = Integer.parseInt(date[0]);
      this.month = Integer.parseInt(date[1]);
      this.day = Integer.parseInt(date[2]);
      this.hour = Integer.parseInt(time[0]);
      this.minute = Integer.parseInt(time[1]);
      this.second = Double.parseDouble(time[2]);
   }

   public static double[] toArr(DateTime dt){
      return new double[]{Double.parseDouble(String.valueOf(dt.year)),
         Double.parseDouble(String.valueOf(dt.month)),
         Double.parseDouble(String.valueOf(dt.day)),
         Double.parseDouble(String.valueOf(dt.hour)),
         Double.parseDouble(String.valueOf(dt.minute)),
         dt.second};
   }

   public int getYear() {
      return year;
   }

   public int getMonth() {
      return month;
   }

   public int getDay() {
      return day;
   }

   public int getHour() {
      return hour;
   }

   public int getMinute() {
      return minute;
   }

   public double getSecond() {
      return second;
   }

   public static boolean compare(double[] first, double[] last, int i, int l, String op){
      switch(op) {
         case ">=":
            if (i == l) {
               System.out.println(i+": "+first[i]+">="+last[i]+"? "+(first[i] >= last[i]));
               return first[i] >= last[i];
            } else {
               if (first[i] >= last[i]) {
                  if (first[i] > last[i]) {
                     System.out.println(i+": "+first[i]+">="+last[i]+"? "+(first[i] >= last[i]));
                     return true;
                  }else {
                     System.out.println(i+": "+first[i]+">="+last[i]+"? "+(first[i] >= last[i]));
                     return compare(first, last, i + 1, l, op);
                  }
               } else {
                  System.out.println(i+": "+first[i]+">="+last[i]+"? "+(first[i] >= last[i]));
                  return false;
               }
            }
         case "<=":
            if (i == l) {
               System.out.println(i+": "+first[i]+"<="+last[i]+"? "+(first[i] <= last[i]));
               return first[i] <= last[i];
            } else {
               if (first[i] <= last[i]) {
                  if (first[i] < last[i]) {
                     System.out.println(i+": "+first[i]+"<="+last[i]+"? "+(first[i] <= last[i]));
                     return true;
                  }else {
                     System.out.println(i+": "+first[i]+"<="+last[i]+"? "+(first[i] <= last[i]));
                     return compare(first, last, i + 1, l, op);
                  }
               } else {
                  System.out.println(i+": "+first[i]+"<="+last[i]+"? "+(first[i] <= last[i]));
                  return false;
               }
            }
         default: return false;
      }
   }

   @Override
   public String toString() {
      return year+"-"+(month<10? "0"+month:month)+"-"+(day<10? "0"+day:day)+" "+
         (hour<10? "0"+hour:hour)+":"+(minute<10? "0"+minute:minute)+":"+(second<10? "0"+second:second);
   }

   public Integer getDayOfMonth(int y, int m){
      switch (m){
         case 1:
            return 31;
         case 2:
            if (isLeap(y)) {
               return 29;
            } else {
               return 28;
            }
         case 3:
            return 31;
         case 4:
            return 30;
         case 5:
            return 31;
         case 6:
            return 30;
         case 7:
            return 31;
         case 8:
            return 31;
         case 9:
            return 30;
         case 10:
            return 31;
         case 11:
            return 30;
         case 12:
            return 31;
      }
      return null;
   }

   public Double toSeconds(ConstraintsInterval cons, MySQLBase base) throws Exception{
      DateTime older = new DateTime(Momentum.older(cons, base).toString());
      int y = older.getYear();
      int mo = older.getMonth();
      int amountOfDays = 0;
      if(y==year && mo==(month)) {
         amountOfDays = day-older.getDay();
      }else{
         amountOfDays = getDayOfMonth(y,mo)-older.getDay()+day;
         if (mo == 12) {
            y++;
            mo = 1;
         } else {
            mo++;
         }
         while(y!=year && mo!=(month)){
            if (mo == 12) {
               y++;
               mo = 1;
            } else {
               mo++;
            }
            if(month == 1) {
               if (y != (year-1) && mo != 12) {
                  amountOfDays += getDayOfMonth(y, mo);
               }
            }else{
               if (y != year && mo != (month-1)) {
                  amountOfDays += getDayOfMonth(y, mo);
               }
            }
         }
      }
      double s;
      int mi=minute;
      int h=hour;
      if(second<older.getSecond()) {
         s = roundUp((second+60)-older.getSecond(), 4);
         mi--;
      }else{
         s = roundUp(second-older.getSecond(), 4);
      }
      if(mi<older.getMinute()) {
         mi += 60-older.getMinute();
         h--;
      }else{
         mi -= older.getMinute();
      }

      if(hour<older.getHour()){
         System.out.println("HERE: "+h);
         h += 24-older.getHour();
         amountOfDays--;
      }else{
         h -= older.getHour();
      }
      System.out.println("~~~~~~~toSeconds~~~~~~~");
      System.out.println("OLD: "+older.toString());
      System.out.println("NEW: "+this.toString());
      System.out.println("Secoond: " + s);
      System.out.println("Minute: " + mi);
      System.out.println("Hour: " + h);
      System.out.println("AmountOfDays: "+amountOfDays);
      System.out.println("RETURN: " + (amountOfDays * 86400 + (h * 3600) + (mi * 60) + s));
      System.out.println("~~~~~~~toSeconds~~~~~~~");

      return amountOfDays*86400+(h*3600)+(mi*60)+s;
   }

   public static double roundUp(double value, int places) {
      if (places < 0) throw new IllegalArgumentException();
      BigDecimal bd = new BigDecimal(value);
      bd = bd.setScale(places, RoundingMode.HALF_UP);
      return bd.doubleValue();
   }

   public static int roundDown(double value) {
      System.out.println("roundDown_VALUE: "+value);
      if(value > 1) {
         String[] tokens = StringUtils.split(String.valueOf(value), ".");
         //System.out.println("roundDown_INT: " + Integer.parseInt(tokens[0]));
         return Integer.parseInt(tokens[0]);
      }else{
         //System.out.println("roundDown_INT: " + 0);
         return 0;
      }
   }

   public static boolean isLeap(int y){
      return (y % 400 == 0) || (y % 4 == 0 && y % 100 != 0);
   }

   public static DateTime secondsToDateTime(double in, ConstraintsInterval cons, MySQLBase base) {
      System.out.println("IN: "+in);
      DateTime older = null;
      try {
         older = new DateTime(Momentum.older(cons, base).toString());
      }catch (Exception e){
         e.printStackTrace();
      }
      int amountOfDays = roundDown(in/86400);
      double temp = in-(amountOfDays*86400);
      System.out.println("AmountOfDays: "+amountOfDays);
      System.out.println("TempHour: "+temp);
      int h =  roundDown(temp/3600);
      System.out.println("Hour: "+h);
      temp  = temp-(h*3600);
      System.out.println("TempMin: "+temp);
      int mi =  roundDown(temp/60);
      System.out.println("PREV_minutes: " + mi);
      temp  = temp-(mi*60);
      System.out.println("TempSec: "+temp);
      double s = temp;
      System.out.println("PREV_sec: " + s);
      if(s+older.getSecond()>=60){
         s+=older.getSecond()-60;
         mi++;
      }else{
         s+=older.getSecond();
      }
      if(mi+older.getMinute()>=60){
         mi+=older.getMinute()-60;
         h++;
      }else{
         mi+=older.getMinute();
      }
      if(h+older.getHour()>=24){
         h+=older.getHour()-24;
         amountOfDays++;
      }else{
         h+=older.getHour();
      }
      int y = older.getYear();
      int mo = older.getMonth();
      int d = older.getDay();
      do{
         if(mo == older.getMonth() && y == older.getYear()){
            if(d+amountOfDays>older.getDayOfMonth(y,mo)){
               amountOfDays-=older.getDayOfMonth(y,mo)-d+1;
               d=1;
               if(mo == 12){
                  mo = 1;
                  y++;
               }else{
                  mo++;
               }
            }else{
               d+=amountOfDays;
               amountOfDays = 0;
            }
         }else{
            if(d+amountOfDays>older.getDayOfMonth(y,mo)){
               amountOfDays-=older.getDayOfMonth(y,mo);
               if(mo == 12){
                  mo = 1;
                  y++;
               }else{
                  mo++;
               }
            }else{
               d+=amountOfDays;
               amountOfDays = 0;
            }
         }
      }while(amountOfDays != 0);

      String result = y+"-"+mo+"-"+d+" "+h+":"+mi+":"+roundUp(s,4);

      System.out.println("~~~~~~~secondsToDateTime~~~~~~~");
      System.out.println("Older: "+older.toString());
      System.out.println("In: "+in);
      System.out.println("ano: " + y);
      System.out.println("mes: " + mo);
      System.out.println("dia: " + d);
      System.out.println("AmountOfDays: " + amountOfDays);
      System.out.println("horas: " + h);
      System.out.println("minutes: " + mi);
      System.out.println("segundos: " + s);
      System.out.println("SECONDS: "+roundUp(s,4));
      System.out.println("~~~~~~~secondsToDateTime~~~~~~~");

      try {
         return new DateTime(result);
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   /*public static DateTime secondsToDateTime(double in) throws Exception{
      System.out.println(in);
      DateTime older = new DateTime(Momentum.older().toString());
      int y = older.getYear();
      int mo = older.getMonth();
      int tempDay = older.getDayOfMonth(y, mo)-older.getDay();
      if(in > (tempDay) * 86400) {
         while (in > (tempDay) * 86400) {
            if (mo == 12) {
               y++;
               mo = 1;
            } else {
               mo++;
            }
            tempDay += older.getDayOfMonth(y, mo);
         }
         tempDay -= older.getDayOfMonth(y, mo);

         if (mo == 1) {
            y--;
            mo = 12;
         } else {
            mo--;
         }
         in-=(tempDay)*86400;
      }
      int d = older.getDay();
      if(in>d*86400) {
         while (in > d * 86400) {
            d++;
         }
         d--;
         in -= (d * 86400);
      }else{
         while (in < d * 86400) {
            d++;
         }
         d--;
         d=older.getDay();
      }
      int h=1;
      if(in>h*3600) {
         while (in > h * 3600) {
            h++;
         }
         h--;
         in -= (h * 3600);
      }else{
         h=0;
      }
      int mi = 1;
      if(in>mi*60) {
         while (in > mi * 60) {
            mi++;
         }
         mi--;
      }else{
         mi=0;
      }
      double s = in - (mi * 60);
      String result = y+"-"+mo+"-"+d+" "+h+":"+mi+":"+s*//*String.format("%.4f", s)*//*;
      System.out.println(result);
      return new DateTime(result);
   }*/
}
