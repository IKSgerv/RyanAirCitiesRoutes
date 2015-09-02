package ryanairController;

import org.json.JSONArray;

import jsonController.JsonController;
import printerController.LogPrinter;

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
	private static String ryanairAirportsUrl = "https://www.ryanair.com/en/api/2/airports/";
	
	private JSONArray jsonAirports;
	private static LogPrinter log = new LogPrinter();
	
	public RyanairAirportController[] airtports;
	public int[][] table;
	
	public RyanairAirportsController(){
		double ind;		
		JsonController jsonController = new JsonController();
		jsonAirports = jsonController.getJsonArray(ryanairAirportsUrl);	
		
		log.println("RyanairAirportsController constructor - Started");
		
		airtports = new RyanairAirportController[jsonAirports.length()];
		ind = 100.0 / airtports.length;
		
		for(int i = 0; i < jsonAirports.length(); i++){
			airtports[i] = new RyanairAirportController(jsonAirports.getJSONObject(i));
			airtports[i].setId(i);
			log.println("(" + Math.round(ind * (i + 1)) + "%) " + airtports[i].toString());
		}
		
		log.println("RyanairAirportsController constructor - Finished");
	}
		
	/**
	 * 
	 * @param iataCode
	 * @return
	 */
	public RyanairAirportController getAirport(String iataCode){
		for(int i = 0; i < airtports.length; i++)
			if (airtports[i].getIataCode().equals(iataCode))
				return airtports[i];
		return null;
	}
	
	public void fullTable(){
		int count = 0;
		RyanairAirportController toAirport;
		table = new int[airtports.length][airtports.length];
		for (int i = 0; i < airtports.length; i++) {
			log.println("--------------------------------------------------\n"
					+ "Calculating for: " + airtports[i].toString());			
			for (int j = 0; j < airtports[i].getDestinationsIataCodes().length; j++) {
				toAirport = getAirport(airtports[i].getDestinationsIataCodes()[j]);
				table[i][j] = (int) getDistance(airtports[i], toAirport);
				log.println("(" + ++count + ") " + airtports[i].getName() + " to " + toAirport.getName() + " Distance: " + table[i][j]);
			}
		}
	}
	
	/**
	 * 
	 * @param airport1
	 * @param airport2
	 * @return
	 */
	public double getDistance(RyanairAirportController airport1, RyanairAirportController airport2){
		/*
		 * R = 6371000
		 * a = sin(delta_latitude / 2)^2 + cos(latitude_1) * cos(latitude_2) * sin(delta_longitude / 2)^2
		 * c = 2 * atan2(sqrt(a), sqrt(1-a))
		 * d = r * c
		 */
				
		double dlat, dlon, h, distance, lat1, lat2, R;
		R = 6371; // world radious km
		dlat = Math.toRadians(airport2.getLatitude() - airport1.getLatitude());
		dlon = Math.toRadians(airport2.getLongitude() - airport1.getLongitude());
		lat1 = Math.toRadians(airport1.getLatitude());
		lat2 = Math.toRadians(airport2.getLatitude());
		h = Math.pow((Math.sin(dlat/2)),2) + Math.cos(lat1)*Math.cos(lat2)*Math.pow((Math.sin(dlon/2)),2);
		distance = 2 * R * Math.asin(Math.sqrt(h));
		
		return distance;
	}
}
