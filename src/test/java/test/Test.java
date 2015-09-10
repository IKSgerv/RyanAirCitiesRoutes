package test;

import java.io.IOException;

import ai.Dijkstra;
import ai.Prim;
import graph.Graph;
import poiController.PoiController;
import ryanairController.RyanairAirportsController;

public class Test {
	public static void main(String[] args) {
		RyanairAirportsController controller;
		PoiController poiController = new PoiController();
		Dijkstra dijkstra;
		Prim prim;
		Graph graphG;
		boolean opc = false;
		String workbook = "src/main/resources/ryanairGraph.xls";
		System.out.println("Started");
		
		if(opc){
			controller = new RyanairAirportsController();
			controller.fullTable();		
			
			poiController.writeTo(workbook, "sheet");
			poiController.putHeaders(controller.getIataCodes());
			poiController.putContent(controller.getTable());
			try {
				poiController.save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		graphG = poiController.readGraph(workbook);
		
		dijkstra = new Dijkstra(graphG);
		dijkstra.resolve("AAR", "AHO");
		
		prim = new Prim(graphG);
		prim.resolve();
	}
}
