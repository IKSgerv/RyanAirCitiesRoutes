package test;

import java.io.IOException;

import ai.Dijkstra;
import graph.Graph;
import poiController.PoiController;
import ryanairController.RyanairAirportsController;

public class Test {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RyanairAirportsController controller;
		PoiController poiController = new PoiController();
		Dijkstra dijkstra;
		Graph graphG;
		String workbook = "src/main/resources/ryanairGraph.xls";
		System.out.println("Started");
				
//		controller = new RyanairAirportsController();
//		controller.fullTable();		
//		
//		poiController.writeTo(workbook, "sheet");
//		poiController.putHeaders(controller.getIataCodes());
//		poiController.putContent(controller.getTable());
//		try {
//			poiController.save();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		graphG = poiController.readGraph(workbook);
		System.out.println(graphG.toString());
		
		dijkstra = new Dijkstra(graphG);
		dijkstra.resolve("AAR", "BRQ");
	}
}
