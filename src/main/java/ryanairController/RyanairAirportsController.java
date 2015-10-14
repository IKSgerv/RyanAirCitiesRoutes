package ryanairController;

import org.json.JSONArray;

import graph.Vertex;
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

/* 
 * ryanairRoutesUrl: JSONArray
 * -----------------------------------
 * airportFrom - iataCode
 * airportTo - iataCode
 * -----------------------------------
 */
public class RyanairAirportsController {
	private static String ryanairAirportsUrl = "https://www.ryanair.com/en/api/2/airports/";
	private static String ryanairRoutesUrl = "https://www.ryanair.com/en/api/2/routes/{iataCode}/";
	
	private JSONArray jsonAirports;
	private static LogPrinter log = new LogPrinter("src/main/resources/log_RyanairAirportsController.txt");
	
	public Vertex[] airports;
	public double[][] table;
	
	public RyanairAirportsController(){
		double ind;		
		JsonController jsonController = new JsonController();
		jsonAirports = jsonController.getJsonArray(ryanairAirportsUrl);	
		JSONArray jsonRoutes = null;
		String name, code;
		Double positionY, positionX;
		String[] adjacentNodes;
		
		log.println("RyanairAirportsController constructor - Started");
		
		airports = new Vertex[jsonAirports.length()];
		ind = 100.0 / airports.length;
		
		for(int i = 0; i < jsonAirports.length(); i++){
			
			name = jsonAirports.getJSONObject(i).getString("name");
			code = jsonAirports.getJSONObject(i).getString("iataCode");
			positionX = jsonAirports.getJSONObject(i).getDouble("longitude");
			positionY = jsonAirports.getJSONObject(i).getDouble("latitude");
			
			jsonRoutes = jsonController.getJsonArray(ryanairRoutesUrl.replace("{iataCode}", code));
			adjacentNodes = new String[jsonRoutes.length()];
			
			for(int j = 0; j < jsonRoutes.length(); j++){
				adjacentNodes[j] = jsonRoutes.getJSONObject(j).getString("airportTo");
			}
			
			airports[i] = new Vertex(code, name, positionX, positionY, adjacentNodes);
			log.println("(" + Math.round(ind * (i + 1)) + "%) " + airports[i].toString());
		}
		log.println("Airports: " + airports.length);
		log.println("RyanairAirportsController constructor - Finished");
		log.save();
	}
		
	/**
	 * 
	 * @param iataCode
	 * @return
	 */
	public Vertex getAirport(String iataCode){
		for(int i = 0; i < airports.length; i++)
			if (airports[i].getCode().equals(iataCode))
				return airports[i];
		return null;
	}
	
	public void fullTable(){
		int count = 0;
		Vertex toAirport;
		table = new double[airports.length][airports.length];
		for (int i = 0; i < airports.length; i++) {
			log.println("--------------------------------------------------\n"
					+ "Calculating for: " + airports[i].toString());			
			for (int j = 0; j < airports[i].getDestinationsIataCodes().length; j++) {
				toAirport = getAirport(airports[i].getDestinationsIataCodes()[j]);
				table[i][getIndex(toAirport.getCode())] = (int) getDistance(airports[i], toAirport);
				log.println("(" + ++count + ") " + airports[i].getName() + " to " + toAirport.getName() + "(" + toAirport.getCode() + ") Distance: " + table[i][getIndex(toAirport.getCode())]);
			}
		}
		log.println("Routes: " + count);
	}
	
	private int getIndex(String iataCode){
		for(int i = 0; i < airports.length; i++){
			if(iataCode.equals(airports[i].getCode()))
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
	public double getDistance(Vertex airport1, Vertex airport2){				
		double dlat, dlon, h, distance, lat1, lat2, R;
		R = 6371;
		dlat = Math.toRadians(airport2.getPositionY() - airport1.getPositionY());
		dlon = Math.toRadians(airport2.getPositionX() - airport1.getPositionX());
		lat1 = Math.toRadians(airport1.getPositionY());
		lat2 = Math.toRadians(airport2.getPositionY());
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
	
	public Vertex[] getHeaders(){
		Vertex[] iataCodes = new Vertex[airports.length];
		for (int i = 0; i < iataCodes.length; i++){
			iataCodes[i] = airports[i];
		}
		return iataCodes;
	}
}
