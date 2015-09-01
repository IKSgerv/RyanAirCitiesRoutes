package ryanairController;

import org.json.JSONArray;

import jsonController.JsonController;

public class RyanairCityController {
	private String name;
	private String iataCode;
	private double latitude;
	private double longitude;
		
	private static final String api_key = "859475af-d985-4081-aea5-b527f943c27a";
	
	private static String iatacodesAirportsUrl = "http://iatacodes.org/api/v1/airports";//?api_key={api_key}&code={airportIataCode}";
	private static String iatacodesCitiesUrl = "http://iatacodes.org/api/v1/cities";
	/* response 
	 * 
	 * 
	 */
	
	RyanairCityController(String airportIataCode){
		JsonController jsonController = new JsonController();
		JSONArray response = null;
		
		response = jsonController.doIatacodesOrgGetRequest(iatacodesAirportsUrl, "api_key", api_key, "code", airportIataCode);
		iataCode = response.getJSONObject(0).getString("city_code");
		
		response = jsonController.doIatacodesOrgGetRequest(iatacodesCitiesUrl, "api_key", api_key, "code", iataCode);
		name = response.getJSONObject(0).getString("name");
		latitude = response.getJSONObject(0).getDouble("lat");
		longitude = response.getJSONObject(0).getDouble("lng");
		
		System.out.println("City: " + name);
	}

	public String getName() {
		return name;
	}

	public String getIataCode() {
		return iataCode;
	}

	public double getLogitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}
}
