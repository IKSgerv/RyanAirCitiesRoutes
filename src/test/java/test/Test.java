package test;

import java.io.IOException;
import java.util.Scanner;

import mx.ia.algorithms.AscensoDeLaColina;
import mx.ia.algorithms.BreadthFirstSearch;
import mx.ia.algorithms.DepthFirstSearch;
import mx.ia.algorithms.Dijkstra;
import mx.ia.algorithms.LimitedDepthFirstSearch;
import mx.ia.algorithms.Prim;
import mx.ia.algorithms.PrimeroElMejor;
import mx.ia.controllers.MatrixFileController;
import mx.ia.controllers.PoiController;
import mx.ia.controllers.RyanairAirportsController;
import mx.ia.graph.Graph;

public class Test {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		RyanairAirportsController controller;
		PoiController poiController = new PoiController();
		MatrixFileController mfController = new MatrixFileController();
		Dijkstra dijkstra;
		Prim prim;
		BreadthFirstSearch BFS;
		DepthFirstSearch DFS;
		LimitedDepthFirstSearch LDFS;
		PrimeroElMejor PEM;
		AscensoDeLaColina ADLC;
		Graph graphG;
		boolean opc = false;
		int iOpc = 0;
		String workbook = "src/main/resources/ryanairGraph.xls";
		String matrixFile = "src/main/resources/Enemigos.txt";
		Scanner keyboard;
		String strFrom, strTo;
		System.out.println("Started");
		keyboard = new Scanner(System.in);
		
		System.out.println("Desea volver a cargar el archivo? s/n");
		opc = keyboard.next() == "s" ? false : false;
		
		if(opc){
			controller = new RyanairAirportsController();
			controller.fullTable();		
			
			poiController.writeTo(workbook, "sheet");
			poiController.putHeaders(controller.getHeaders());
			poiController.putContent(controller.getTable());
			try {
				poiController.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
//		graphG = poiController.readGraph(workbook);
		graphG = mfController.readGraph(matrixFile);
		
		do{
			System.out.println("1.- Dijkstra\n"
					+ "2.- Prim\n"
					+ "3.- BFS\n"
					+ "4.- DFS\n"
					+ "5.- LDFS\n"
					+ "6.- PEM\n"
					+ "7.- ADLC\n"
					+ "0.- Salir");
			iOpc = keyboard.nextInt();
			switch (iOpc) {
			case 0:
				System.out.println("Terminó el programa");
				break;
			case 1:
				System.out.println("Ingrese el iataCode del aeropuerto de origen");
				strFrom = keyboard.next();
				System.out.println("Ingrese el iataCode del aeropuerto de destino");
				strTo = keyboard.next();
				dijkstra = new Dijkstra(graphG);
				dijkstra.resolve(strFrom, strTo);//"AAR", "AHO"
				break;
			case 2:
				prim = new Prim(graphG);
				prim.resolve();
				break;
			case 3:
				System.out.println("Ingrese el iataCode del aeropuerto de origen");
				strFrom = keyboard.next();
				System.out.println("Ingrese el iataCode del aeropuerto de destino");
				strTo = keyboard.next();
				BFS = new BreadthFirstSearch(graphG);
				BFS.setRules(mfController.getRules());
				BFS.resolve(strFrom, strTo);//"AAR", "AHO"
				break;
			case 4:
				System.out.println("Ingrese el iataCode del aeropuerto de origen");
				strFrom = keyboard.next();
				System.out.println("Ingrese el iataCode del aeropuerto de destino");
				strTo = keyboard.next();
				DFS = new DepthFirstSearch(graphG);
				DFS.setRules(mfController.getRules());
				DFS.resolve(strFrom, strTo);//"AAR", "AHO"
				break;
			case 5:
				System.out.println("Ingrese el iataCode del aeropuerto de origen");
				strFrom = keyboard.next();
				System.out.println("Ingrese el iataCode del aeropuerto de destino");
				strTo = keyboard.next();
				LDFS = new LimitedDepthFirstSearch(graphG);
				LDFS.setRules(mfController.getRules());
				LDFS.setLimitLevel(35);
				LDFS.resolve(strFrom, strTo);//"AAR", "AHO"
				break;
			case 6:
				System.out.println("Ingrese el iataCode del aeropuerto de origen");
				strFrom = keyboard.next();
				System.out.println("Ingrese el iataCode del aeropuerto de destino");
				strTo = keyboard.next();
				PEM = new PrimeroElMejor(graphG);
				PEM.resolve(strFrom, strTo);//"AAR", "AHO"
				break;
			case 7:
				System.out.println("Ingrese el iataCode del aeropuerto de origen");
				strFrom = keyboard.next();
				System.out.println("Ingrese el iataCode del aeropuerto de destino");
				strTo = keyboard.next();
				ADLC = new AscensoDeLaColina(graphG);
				ADLC.resolve(strFrom, strTo);//"AAR", "AHO"
				break;
			default:
				break;
			}
		}while(iOpc != 0);
	}
}
