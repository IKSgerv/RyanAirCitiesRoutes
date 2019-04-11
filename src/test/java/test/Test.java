package test;

import java.io.IOException;
import java.util.Scanner;

import mx.ia.algorithms.AStar;
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
		AStar aStar;

		Graph graphG;

		boolean opc = false;
		int iOpc = 0;
		String workbook = "src/main/resources/ryanairGraph-latest.xlsx";
		String matrixFile = "src/main/resources/Enemigos.txt";
		Scanner keyboard;
		String strFrom, strTo;
		System.out.println("Started");
		keyboard = new Scanner(System.in);

		System.out.println("Select the resource to use\n"//
				+ "0.- " + workbook + "\n"//
				+ "1.- " + matrixFile + "\n");
		iOpc = keyboard.nextInt();

		if (iOpc == 0) {
			System.out.println("Desea volver a cargar el archivo? s/n");
			opc = keyboard.next().equals("s") ? true : false;

			if (opc) {
				controller = new RyanairAirportsController();
				controller.fillTable();

				poiController.writeTo(workbook, "sheet");
				poiController.putHeaders(controller.getHeaders());
				poiController.putContent(controller.getTable());
				try {
					poiController.save();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			graphG = poiController.readGraph(workbook);
		} else {
			graphG = mfController.readGraph(matrixFile);
		}

		do {
			System.out.println("1.- Dijkstra\n"//
					+ "2.- Prim\n"//
					+ "3.- BFS (Breadth First Search)\n"//
					+ "4.- DFS (Depth First Search)\n"//
					+ "5.- LDFS (Limited Depth First Search)\n"//
					+ "6.- PEM (Primero El Mejor)\n"//
					+ "7.- ADLC (Ascenso De La Colina)\n"//
					+ "8.- A estrella\n"//
					+ "0.- Salir");
			iOpc = keyboard.nextInt();
			switch (iOpc) {
			case 0:
				System.out.println("Terminó el programa");
				break;
			case 1:
				System.out.println("Ingrese el codigo de origen");
				strFrom = keyboard.next();
				System.out.println("Ingrese el codigo de destino");
				strTo = keyboard.next();
				dijkstra = new Dijkstra(graphG);
				dijkstra.resolve(strFrom, strTo);// "AAR", "AHO"
				break;
			case 2:
				prim = new Prim(graphG);
				prim.resolve();
				break;
			case 3:
				System.out.println("Ingrese el codigo de origen");
				strFrom = keyboard.next();
				System.out.println("Ingrese el codigo de destino");
				strTo = keyboard.next();
				BFS = new BreadthFirstSearch(graphG);
				BFS.setRules(mfController.getRules());
				BFS.resolve(strFrom, strTo);// "AAR", "AHO"
				break;
			case 4:
				System.out.println("Ingrese el codigo de origen");
				strFrom = keyboard.next();
				System.out.println("Ingrese el codigo de destino");
				strTo = keyboard.next();
				DFS = new DepthFirstSearch(graphG);
				DFS.setRules(mfController.getRules());
				DFS.resolve(strFrom, strTo);// "AAR", "AHO"
				break;
			case 5:
				System.out.println("Ingrese el codigo de origen");
				strFrom = keyboard.next();
				System.out.println("Ingrese el codigo de destino");
				strTo = keyboard.next();
				LDFS = new LimitedDepthFirstSearch(graphG);
				LDFS.setRules(mfController.getRules());
				LDFS.setLimitLevel(35);
				LDFS.resolve(strFrom, strTo);// "AAR", "AHO"
				break;
			case 6:
				System.out.println("Ingrese el codigo de origen");
				strFrom = keyboard.next();
				System.out.println("Ingrese el codigo de destino");
				strTo = keyboard.next();
				PEM = new PrimeroElMejor(graphG);
				PEM.resolve(strFrom, strTo);// "AAR", "AHO"
				break;
			case 7:
				System.out.println("Ingrese el codigo de origen");
				strFrom = keyboard.next();
				System.out.println("Ingrese el codigo de destino");
				strTo = keyboard.next();
				ADLC = new AscensoDeLaColina(graphG);
				ADLC.resolve(strFrom, strTo);// "AAR", "AHO"
				break;
			case 8:
				System.out.println("Ingrese el codigo de origen");
				strFrom = keyboard.next();
				System.out.println("Ingrese el codigo de destino");
				strTo = keyboard.next();
				aStar = new AStar(graphG);
				aStar.resolve(strFrom, strTo);// "AAR", "AHO"
				break;
			default:
				System.out.println("Porfavor ingrese una opcion valida");
				break;
			}
		} while (iOpc != 0);
	}
}
