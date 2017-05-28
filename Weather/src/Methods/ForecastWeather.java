package Methods;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

@Path("/forecast")
public class ForecastWeather {
	@GET	
	@Produces(MediaType.APPLICATION_JSON)
	public JSONArray temperature(@QueryParam("date1") String dateval)
	{
		//System.out.println("a is "+dateval);
		JSONArray datesList = new JSONArray(); 
		
		String date = dateval.substring(0,4)+dateval.substring(5,7)+dateval.substring(8,10); 
		ArrayList<output> list = new ArrayList<output>();
		String yearmonth;
		String month=date.substring(4,6);
		String monthdate;
		int monthvalue=Integer.parseInt(date.substring(4,6));
		String year=date.substring(0,4);
		int count=0;
		int datevalue=Integer.parseInt(date.substring(6,8));
		int nextdate=datevalue;
		String nextdatevalue;
		float tmax=0.0f;
		float tmin=0.0f;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/weatherreport",
			        "root","Rashmitha@05"); PreparedStatement pstmt=null; ResultSet rs=null;			 
			        yearmonth=year+month;
		      for(int i=0;i<7;i++){		    	  
		    	  if(nextdate<=9){		    		 
		    		   nextdatevalue=yearmonth+0+nextdate;
		    	  }
		    	  else{
		    		 nextdatevalue=yearmonth+nextdate;
		    	  }
		    	  monthdate=nextdatevalue.substring(4,6)+nextdatevalue.substring(6,8);		    	  
		    	   pstmt = conn.prepareStatement(
			    		  "select avg(tmax) as tmax,avg(tmin) as tmin from report where date LIKE ? ");
			      pstmt.setString(1, "%" + monthdate + "%");
			      rs = pstmt.executeQuery(); 
			      while(rs.next()){		          
			    	 
			           tmax=Float.parseFloat(rs.getString("tmax"));
			           tmin=Float.parseFloat(rs.getString("tmin"));
			         
			         
			       }
			      
		    	  output r = new output(nextdatevalue,tmax,tmin);
		    	  JSONObject obj = new JSONObject();
		    	  obj.put("DATE", nextdatevalue);
				    obj.put("TMAX", tmax);
				    obj.put("TMIN", tmin);
				    datesList.put(obj);
		    	  if((nextdate==31) && (monthvalue==1 || monthvalue==3 || monthvalue==7 || monthvalue==8 || monthvalue==10 || monthvalue==12 || monthvalue==5)){
		    		  nextdate=1;count=count+1;
		    		  
		    	  }
		    	  else if((nextdate==30) && (monthvalue==4 || monthvalue==6 || monthvalue==9 || monthvalue==11)){
		    		  nextdate=1;count=count+1;
		    	  }
		    	  else if((nextdate==28) && (monthvalue==2)){
		    		  nextdate=1;count=count+1;
		    	  }		    	  
		    	  else{
		    		  nextdate=nextdate+1;
		    	  }	
		    	  if(count>0){
		    		  if(monthvalue<=9){	
		    			  monthvalue=monthvalue+1;
		    			  yearmonth=year+0+monthvalue;count=0;
			    	  }
			    	  else{
			    		  monthvalue=monthvalue+1;
			    		  yearmonth=year+monthvalue;count=0;
			    	  }
		    	  }
		    	  
		    	  //System.out.println(obj);
		    	  
				    
		    	  list.add(r);
		      }
		      //System.out.println(datesList);
		      rs.close();
		      pstmt.close();
		      conn.close();
		      
		} catch (Exception ex) {			
			 System.out.println("Exception: " + ex.getMessage());			    
		}
		
		return datesList;
			}

}
