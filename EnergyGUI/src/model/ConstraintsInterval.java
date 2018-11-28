package model;

import java.util.ArrayList;
import java.util.List;

public class ConstraintsInterval{
   private String query;
   private List<String> constraints = new ArrayList<String>();

   public ConstraintsInterval(String q, String c ){
      this.query = q;
      this.constraints.add(c);
   }

   public String getQuery() {
      return query;
   }

   public void setQuery(String query) {
      this.query = query;
   }

   public List<String> getConstraints() {
      return constraints;
   }

   @Override
   public String toString() {
      return "ConstraintsInterval{" +
                   "query='" + query + '\'' +
                   ", constraints=" + constraints +
                   '}';
   }
}
