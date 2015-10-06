package ai;

import java.util.Vector;

import graph.Graph;
import ryanairController.RyanairAirportController;

public class Kruskal {
	private Vector<Graph> forestB;
	public Kruskal(Graph g){
		Graph newGraph;
		Vector<RyanairAirportController> newRyanVect;
		forestB = new Vector<Graph>();
		for (RyanairAirportController airport : g.getV()) {
			newRyanVect = new Vector<RyanairAirportController>();
			newGraph = new Graph();
			forestB.add(newGraph);
		}
	}
}
