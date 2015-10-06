package graph;

import java.util.Vector;

import ryanairController.RyanairAirportController;

public class Graph {
	private Vector<RyanairAirportController> vectorV;
	private Vector<Edge> vectorE;	
	public Graph(Vector<RyanairAirportController> v, Vector<Edge> e){
		vectorV = v;
		vectorE = e;
	}
	public Graph(){
		vectorV = new Vector<RyanairAirportController>();
		vectorE = new Vector<Edge>();
	}
	public Vector<Edge> getE() {
		return vectorE;
	}
	public Vector<RyanairAirportController> getV() {
		return vectorV;
	}
	public String toString(){
		return " V: " + vectorV.toString() + "\n E: " + vectorE.toString();
	}
}
