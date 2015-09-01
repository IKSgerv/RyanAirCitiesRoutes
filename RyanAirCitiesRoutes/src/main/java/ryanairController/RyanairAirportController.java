package ryanairController;

import org.json.JSONArray;
import org.json.JSONObject;

import jsonController.JsonController;

/* 
 * ryanairRoutesUrl: JSONArray
 * -----------------------------------
 * airportFrom - iataCode
 * airportTo - iataCode
 * -----------------------------------
 */

public class RyanairAirportController {
	private String name;
	private String iataCode;
	private double latitude;
	private double longitude;
	private String[] destinationsIataCodes;	
	private RyanairCityController city;
	
	private static String ryanairRoutesUrl = "https://www.ryanair.com/es/api/2/routes/{iataCode}/";
	
	RyanairAirportController(JSONObject jsonAirport){
		JSONArray jsonRoutes = null;
		JsonController jsonController = new JsonController();
		name = jsonAirport.getString("name");
		iataCode = jsonAirport.getString("iataCode");
		latitude = jsonAirport.getDouble("latitude");
		longitude = jsonAirport.getDouble("longitude");		
		city = new RyanairCityController(iataCode);
		
		jsonRoutes = jsonController.getJsonArray(ryanairRoutesUrl.replace("{iataCode}", iataCode));
		destinationsIataCodes = new String[jsonRoutes.length()];
		for(int i = 0; i < jsonRoutes.length(); i++){
			destinationsIataCodes[i] = jsonRoutes.getJSONObject(i).getString("airportTo");
		}
	}

	public String getName() {
		return name;
	}
	
	public String getIataCode() {
		return iataCode;
	}
	
	public double getLatitude(){
		return latitude;
	}
	
	public double getLongitude(){
		return longitude;
	}
}
