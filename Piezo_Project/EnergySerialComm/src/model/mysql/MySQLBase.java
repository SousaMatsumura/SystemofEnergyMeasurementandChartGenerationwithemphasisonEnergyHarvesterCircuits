package model.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLBase{
   protected Connection conn;
   private String ip;
   private int port;
   private String database;
   private String user;
   private String password;

   public MySQLBase(){}

   public MySQLBase(String ip, int port, String database, String user, String password) {
      this.ip = ip;
      this.port = port;
      this.database = database;
      this.user = user;
      this.password = password;
   }

   public Connection open(){
      try{
         String cnUrl = "jdbc:mysql://"+ip+":"+port+"/"+ database +"?useSSL=false&useTimezone=true&serverTimezone=UTC&autoReconnect=true&useUnicode=true&characterEncoding=utf8";
         conn = DriverManager.getConnection(cnUrl ,user, password);
         return conn;
      }catch (Exception e){
         e.printStackTrace();
      }
      return null;
   }

   public Connection test() throws SQLException {
      String cnUrl = "jdbc:mysql://"+ip+":"+port+"/"+ database +"?useSSL=false&useTimezone=true&serverTimezone=UTC&autoReconnect=true&useUnicode=true&characterEncoding=utf8";
      System.out.println("URL: "+cnUrl+"\nUser: "+user+"\nPassword: "+password);
      conn = DriverManager.getConnection(cnUrl ,user, password);
      return conn;
   }

   public void close(){
      try {
         if(conn != null)
            conn.close();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }
}
