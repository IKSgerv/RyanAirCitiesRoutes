package ai;

import java.util.Vector;

import graph.Graph;
import graph.Vertex;

public class Kruskal {
	private Vector<Graph> forestB;
	public Kruskal(Graph g){
		Graph newGraph;
		Vector<Vertex> newRyanVect;
		forestB = new Vector<Graph>();
		for (Vertex airport : g.getV()) {
			newRyanVect = new Vector<Vertex>();
			newGraph = new Graph();
			forestB.add(newGraph);
		}
	}
}
