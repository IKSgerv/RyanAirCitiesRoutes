package mx.ia.graph;

import java.util.Vector;

public class Graph {
	private Vector<Vertex> vectorV;
	private Vector<Edge> vectorE;	
	public Graph(Vector<Vertex> v, Vector<Edge> e){
		vectorV = v;
		vectorE = e;
	}
	public Graph(){
		vectorV = new Vector<Vertex>();
		vectorE = new Vector<Edge>();
	}
	public Vector<Edge> getE() {
		return vectorE;
	}
	public Vector<Vertex> getV() {
		return vectorV;
	}
	public String toString(){
		return " V: " + vectorV.toString() + "\n E: " + vectorE.toString();
	}
}
