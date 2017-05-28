import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.codehaus.jettison.json.JSONArray;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;

public class WeatherServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException{
		String date = request.getParameter("date2");
		System.out.println(date);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date dat = null;
		String s = null;
		StringBuffer responseString = null;
		long unixTime = 0;
		JSONArray jsonArray = new JSONArray();
		PrintWriter out=response.getWriter();
		String dateString = date + ":00";
		int i = 0;
	    /*String[] msg= new String[7];
	    msg[0] = "'date',"+"'MinTemp',"+"'MaxTemp'";*/
	           
		try {
			dat = dateFormat.parse(dateString);
		} catch (java.text.ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-4"));
		unixTime = (long) dat.getTime()/1000;
		while(i < 5){
		String requestedURL = "https://api.darksky.net/forecast/";
		String key          = "7d3d8bde7de6f07a1023c9b02d6d7653";
		long   latitude     = (long) 39.1405450;
		long   longitude    = (long) -84.5138030;
		requestedURL = requestedURL + key+"/"+latitude+","+longitude+","+unixTime+"?exclude=currently,minutely,hourly,alerts,flags";
		URL url = new URL(requestedURL);   
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();  
		connection.setRequestMethod("GET");
		if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
			 BufferedReader reader = new BufferedReader(new InputStreamReader( connection.getInputStream(),"UTF-8" ));
			 responseString = new StringBuffer(); 
			 while( (s = reader.readLine()) != null ){
                  responseString.append(s);
			  }    
			  if(responseString != null){
				  JSONParser jsonParser = new JSONParser();
				  JSONObject jsonObject = null;
				try {
					jsonObject = (JSONObject) jsonParser.parse(responseString.toString());
				
				JSONObject jsonObject2 = (JSONObject) jsonObject.get("daily");
				JSONArray jsonObject3  = (JSONArray)  jsonObject2.get("data");
				JSONObject jsonObject4 = (JSONObject) (jsonObject3.get(0));
				response.setContentType("application/json");
				response.setCharacterEncoding("utf-8");
				
				JSONObject obj = new JSONObject();
				obj.put("temperatureMin",jsonObject4.get("temperatureMin"));
				obj.put("temperatureMax",jsonObject4.get("temperatureMax"));
				java.util.Date d = new java.util.Date((unixTime * 1000));
				obj.put("date", d.toString());
	            jsonArray.add(obj);
				System.out.println("Min Temp:"+jsonObject4.get("temperatureMin")+" °C");
				System.out.println("Max Temp:"+jsonObject4.get("temperatureMax") +" °C");
				System.out.println("I = "+i+"\t"+jsonObject4);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  }
		   } 
		  i++;
		  unixTime = unixTime + (24*60*60);
		}
        //System.out.println(jsonArray);
        out.println(jsonArray);
	} 

}
