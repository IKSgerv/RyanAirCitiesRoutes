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
	private static LogPrinter log = new LogPrinter("src/main/resources/log_RyanairAirportsController.txt");
	
	public RyanairAirportController[] airports;
	public double[][] table;
	
	public RyanairAirportsController(){
		double ind;		
		JsonController jsonController = new JsonController();
		jsonAirports = jsonController.getJsonArray(ryanairAirportsUrl);	
		
		log.println("RyanairAirportsController constructor - Started");
		
		airports = new RyanairAirportController[jsonAirports.length()];
		ind = 100.0 / airports.length;
		
		for(int i = 0; i < jsonAirports.length(); i++){
			airports[i] = new RyanairAirportController(jsonAirports.getJSONObject(i));
			airports[i].setId(i);
			log.println("(" + Math.round(ind * (i + 1)) + "%) " + airports[i].toString());
		}
		log.println("Airports: " + airports.length);
		log.println("RyanairAirportsController constructor - Finished");
	}
		
	/**
	 * 
	 * @param iataCode
	 * @return
	 */
	public RyanairAirportController getAirport(String iataCode){
		for(int i = 0; i < airports.length; i++)
			if (airports[i].getIataCode().equals(iataCode))
				return airports[i];
		return null;
	}
	
	public void fullTable(){
		int count = 0;
		RyanairAirportController toAirport;
		table = new double[airports.length][airports.length];
		for (int i = 0; i < airports.length; i++) {
			log.println("--------------------------------------------------\n"
					+ "Calculating for: " + airports[i].toString());			
			for (int j = 0; j < airports[i].getDestinationsIataCodes().length; j++) {
				toAirport = getAirport(airports[i].getDestinationsIataCodes()[j]);
				table[i][getIndex(toAirport.getIataCode())] = (int) getDistance(airports[i], toAirport);
				log.println("(" + ++count + ") " + airports[i].getName() + " to " + toAirport.getName() + "(" + toAirport.getIataCode() + ") Distance: " + table[i][getIndex(toAirport.getIataCode())]);
			}
		}
		log.println("Routes: " + count);
	}
	
	private int getIndex(String iataCode){
		for(int i = 0; i < airports.length; i++){
			if(iataCode.equals(airports[i].getIataCode()))
				return i;
		}
		return -1;
	}
	
	/**
	 * 
	 * @param airport1
	 * @param airport2
	 * @return
	 */
	public double getDistance(RyanairAirportController airport1, RyanairAirportController airport2){				
		double dlat, dlon, h, distance, lat1, lat2, R;
		R = 6371;
		dlat = Math.toRadians(airport2.getLatitude() - airport1.getLatitude());
		dlon = Math.toRadians(airport2.getLongitude() - airport1.getLongitude());
		lat1 = Math.toRadians(airport1.getLatitude());
		lat2 = Math.toRadians(airport2.getLatitude());
		h = Math.pow((Math.sin(dlat/2)),2) + Math.cos(lat1)*Math.cos(lat2)*Math.pow((Math.sin(dlon/2)),2);
		distance = 2 * R * Math.asin(Math.sqrt(h));
		
		return distance;
	}
	
	public double[][] getTable(){
		double[][] res = table;
		for(int i = 0; i < res.length; i++){
			for(int j = 0; j < res.length; j++){
				if (res[i][j] == 0.0)
					res[i][j] = Double.POSITIVE_INFINITY; 
			}
		}
		return res;
	}
	
	public String[] getIataCodes(){
		String[] iataCodes = new String[airports.length];
		for (int i = 0; i < iataCodes.length; i++){
			iataCodes[i] = airports[i].getIataCode();
		}
		return iataCodes;
	}
}
