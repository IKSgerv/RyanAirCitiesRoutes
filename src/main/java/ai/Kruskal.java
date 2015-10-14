package ai;

import java.util.Vector;

import graph.Graph;
import graph.Vertex;

public class Kruskal {
	private Vector<Graph> forestB;
	@SuppressWarnings("unused")
	public Kruskal(Graph g){
		Graph newGraph;
		Vector<Vertex> vertex;
		forestB = new Vector<Graph>();
		for (Vertex airport : g.getV()) {
			vertex = new Vector<Vertex>();
			newGraph = new Graph();
			forestB.add(newGraph);
		}
	}
}
