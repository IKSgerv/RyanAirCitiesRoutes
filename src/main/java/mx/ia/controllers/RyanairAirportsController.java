package mx.ia.controllers;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import mx.ia.graph.Vertex;

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

//	private static String ryanairAirportsUrl = "https://www.ryanair.com/en/api/2/airports/";
	private static String ryanairStationsUrl = "https://desktopapps.ryanair.com/v4/en-us/res/stations";
//	private static String ryanairRoutesUrl = "https://www.ryanair.com/en/api/2/routes/{iataCode}/";
	private static String ryanairFaresUrl = "https://services-api.ryanair.com/farfnd/3/oneWayFares?&"//
			+ "departureAirportIataCode={iataCode}&"//
			+ "language=en&market=en-us&"//
			+ "offset=0&"//
			+ "outboundDepartureDateFrom={dateFrom}&"// 2018-10-04
			+ "outboundDepartureDateTo={dateTo}";// 2019-10-04

	private JSONObject jsonStations;
	private static LogPrinter log = new LogPrinter("src/main/resources/log_RyanairAirportsController.txt");

	public Vertex[] airports;
	public double[][] table;

	public RyanairAirportsController() {
		double ind;
		JsonController jsonController = new JsonController();
		jsonStations = jsonController.getJsonObject(ryanairStationsUrl);
		JSONObject jsonRoutes = null;
		String name, code, latitude, longitude;
		Double positionY, positionX;
		String[] adjacentNodes;
		double[] prices;

		log.println("RyanairAirportsController constructor - Started");

		Iterator<String> keys = jsonStations.keys();

		airports = new Vertex[jsonStations.length()];
		ind = 100.0 / airports.length;
		int i = 0;
		while (keys.hasNext()) {
			String key = keys.next();
			code = key;
			if (jsonStations.get(key) instanceof JSONObject) {

				JSONObject station = (JSONObject) jsonStations.get(key);
				name = station.getString("name");
				latitude = station.getString("longitude");
				longitude = station.getString("latitude");

				positionX = Double.valueOf(latitude.substring(0, latitude.length() - 1))
						* (latitude.charAt(latitude.length() - 1) == 'N' ? 1 : -1);
				positionY = Double.valueOf(longitude.substring(0, longitude.length() - 1))
						* (longitude.charAt(longitude.length() - 1) == 'E' ? 1 : -1);

				jsonRoutes = jsonController.getJsonObject(ryanairFaresUrl//
						.replace("{iataCode}", code)//
						.replace("{dateFrom}", "2019-04-01")//
						.replace("{dateTo}", "2020-04-01"));

				JSONArray fares = jsonRoutes.getJSONArray("fares");
				adjacentNodes = new String[fares.length()];
				prices = new double[fares.length()];
				for (int j = 0; j < fares.length(); j++) {
					adjacentNodes[j] = fares.getJSONObject(j).getJSONObject("outbound").getJSONObject("arrivalAirport")
							.getString("iataCode");
					prices[j] = fares.getJSONObject(j).getJSONObject("outbound").getJSONObject("price")
							.getDouble("value");
				}

				airports[i] = new Vertex(code, name, positionX, positionY, adjacentNodes, prices);
				log.println("(" + Math.round(ind * (i + 1)) + "%) " + airports[i].toString());
			}
			i++;
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
	public Vertex getAirport(String iataCode) {
		for (int i = 0; i < airports.length; i++)
			if (airports[i].getCode().equals(iataCode))
				return airports[i];
		return null;
	}

	public void fillTable() {
		int count = 0;
		Vertex toAirport;
		table = new double[airports.length][airports.length];
		for (int i = 0; i < airports.length; i++) {
			log.println("--------------------------------------------------\n" + "Calculating for: "
					+ airports[i].toString());

			for (int j = 0; j < airports[i].getAdjacentNodes().length; j++) {
				toAirport = getAirport(airports[i].getAdjacentNodes()[j]);
//				table[i][getIndex(toAirport.getCode())] = (int) getDistance(airports[i], toAirport);
				table[i][getIndex(
						toAirport.getCode())] = (double) (Math.round(airports[i].getPrices()[j] * 1000d) / 1000d);

				log.println("(" + ++count + ") " + airports[i].getName() + " to " + toAirport.getName() + "("
						+ toAirport.getCode() + ") Price: " + table[i][getIndex(toAirport.getCode())]);
			}
		}
		log.println("Routes: " + count);
	}

	private int getIndex(String iataCode) {
		for (int i = 0; i < airports.length; i++) {
			if (iataCode.equals(airports[i].getCode()))
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
	public double getDistance(Vertex airport1, Vertex airport2) {
		double dlat, dlon, h, distance, lat1, lat2, R;
		R = 6371;
		dlat = Math.toRadians(airport2.getPositionY() - airport1.getPositionY());
		dlon = Math.toRadians(airport2.getPositionX() - airport1.getPositionX());
		lat1 = Math.toRadians(airport1.getPositionY());
		lat2 = Math.toRadians(airport2.getPositionY());
		h = Math.pow((Math.sin(dlat / 2)), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow((Math.sin(dlon / 2)), 2);
		distance = 2 * R * Math.asin(Math.sqrt(h));

		return distance;
	}

	public double[][] getTable() {
		double[][] res = table;
		for (int i = 0; i < res.length; i++) {
			for (int j = 0; j < res.length; j++) {
				if (res[i][j] == 0.0)
					res[i][j] = Double.POSITIVE_INFINITY;
			}
		}
		return res;
	}

	public Vertex[] getHeaders() {
		Vertex[] iataCodes = new Vertex[airports.length];
		for (int i = 0; i < iataCodes.length; i++) {
			iataCodes[i] = airports[i];
		}
		return iataCodes;
	}
}
