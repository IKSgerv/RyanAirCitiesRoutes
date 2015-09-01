package ryanairController;

import org.json.JSONArray;
import org.json.JSONObject;

import jsonController.JsonController;

/*
 * ryanairAirportsUrl: JSONArray
 * -----------------------------------
 * iataCode
 * name
 * base
 * latitude
 * longitude
 * country: JSONObject
 *   code
 *   name
 *   seoName
 *   englishSeoName
 *   currency
 *   url
 * -----------------------------------
 */

public class RyanairAirportsController {
	private static String ryanairAirportsUrl = "https://www.ryanair.com/es/api/2/airports/";
	
	public static JSONArray jsonAirports;
	public static RyanairAirportController[] airtports;
	
	
	public static void main(String[] args){
		System.out.println("Started");
		RyanairAirportsController controller = new RyanairAirportsController();
		
	}
	
	RyanairAirportsController(){
		JsonController jsonController = new JsonController();
		jsonAirports = jsonController.getJsonArray(ryanairAirportsUrl);	
		
		airtports = new RyanairAirportController[jsonAirports.length()];
		for(int i = 0; i < jsonAirports.length(); i++){
			airtports[i] = new RyanairAirportController(jsonAirports.getJSONObject(i));
			System.out.println("Airport: " + airtports[i].getName());
		}
	}
	
	/**
	 * 
	 * @param jsonCity
	 * @return
	 */
	public static int getCityID(JSONObject jsonCity){
		for(int i = 0; i < jsonAirports.length(); i++)
			if (jsonCity.equals(jsonAirports.getJSONObject(i)))
				return i;
		return -1;
	}
	
	/**
	 * 
	 * @param iataCode
	 * @return
	 */
	public static RyanairAirportController getAirport(String iataCode){
		for(int i = 0; i < airtports.length; i++)
			if (airtports[i].getIataCode().equals(iataCode))
				return airtports[i];
		return null;
	}
	
	public static double getDistance(RyanairAirportController airport1, RyanairAirportController airport2){
		/*
		 * Formula:
		 * R = 6371000
		 * a = sin(delta_latitude / 2)^2 + cos(latitude_1) * cos(latitude_2) * sin(delta_longitude / 2)^2
		 * c = 2 * atan2(sqrt(a), sqrt(1-a))
		 * d = r * c
		 * 
		 */
		double R = 6371000; // world radious m
		double lat1 = Math.toRadians(airport1.getLatitude());
		double lat2 = Math.toRadians(airport2.getLatitude());
		double dLat = Math.toRadians(airport2.getLatitude() - airport1.getLatitude());
		double dLon = Math.toRadians(airport2.getLongitude() - airport1.getLongitude());
		double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c;
		
		
		
		double dlat, dlong, h, distance;
		R = 6371; // world radious km
		dlat = Math.toRadians(airport2.getLatitude() - airport1.getLatitude());
		dlong = Math.toRadians(airport2.getLongitude() - airport1.getLongitude());
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);
		h = Math.pow((Math.sin(dlat/2)),2) + Math.cos(lat1)*Math.cos(lat2)*Math.pow((Math.sin(dlong/2)),2);
		distance = 2 * R * Math.asin(Math.sqrt(h));
		if (d > 0)
			return d / 1000;
		else
			return distance;
	}
}
