package jsonController;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

public class JsonController {
	private int timeout = 2000;
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public JSONArray getJsonArray(String url) {
		String json = "";
		JSONArray jsonArray = null;
	    try {
			json = Jsoup.connect(url).ignoreContentType(true).timeout(timeout).execute().body();
			jsonArray = new JSONArray(json);
		} catch (IOException e) {
			System.out.println("Error - url: " + url);
			e.printStackTrace();
		}
	    return jsonArray;
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public JSONObject getJsonObject(String url) {
		String response = "";
		JSONObject jsonObject = null;
	    try {
	    	response = Jsoup.connect(url).ignoreContentType(true).timeout(timeout).execute().body();
			jsonObject = new JSONObject(response);
		} catch (IOException e) {
			System.out.println("Error - url: " + url);
			e.printStackTrace();
		}
	    return jsonObject;
	}
	
	
	public JSONArray doIatacodesOrgGetRequest(String url, String... data){
		String response = "";
		JSONObject jsonObject = null;
		try {
			response = Jsoup.connect(url).ignoreContentType(true).timeout(timeout).data(data).referrer(url).method(Method.GET).execute().body();
			jsonObject = new JSONObject(response);
		} catch (IOException e) {
			System.out.println("Error - url: " + url);
			e.printStackTrace();
		}
		//System.out.println(jsonObject.getJSONArray("response"));
		return jsonObject.getJSONArray("response");
	}
	
}
