package graph;

import java.util.Vector;

public class Graph {
	private Vector<String> vectorV;
	private Vector<Edge> vectorE;	
	public Graph(Vector<String> v, Vector<Edge> e){
		vectorV = v;
		vectorE = e;
	}
	public Vector<Edge> getVectorE() {
		return vectorE;
	}
	public Vector<String> getVectorV() {
		return vectorV;
	}
	public String toString(){
		return "V: " + vectorV.toString() + "\nE: " + vectorE.toString();
	}
}
