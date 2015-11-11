package mx.ia.algorithms;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import mx.ia.graph.Edge;
import mx.ia.graph.Graph;
import mx.ia.graph.Vertex;

public class PrimeroElMejor {

	private Graph graphG;
	private Graph graphT = new Graph();
	private Map<String, Vertex> vElements = new HashMap<String, Vertex>();
	private Vector<PrimeroElMejorElement> bfsOpened = new Vector<PrimeroElMejorElement>();
	private Vector<PrimeroElMejorElement> bfsClosed = new Vector<PrimeroElMejorElement>();
	
	public PrimeroElMejor(Graph g){
		System.out.println("PrimeroElMejor Controller - Started");
		graphG = g;
		System.out.println("G: {\n" + g.toString() + "\n}\n"
				+ " Vertices: " + graphG.getV().size() + "\n"
				+ " Edges: " + graphG.getE().size());
	}
	public Graph resolve(String from, String to){
		boolean fail = true;
		double angle, deltaX, deltaY;
		NewString newFrom = new NewString(), newTo = new NewString();
		PrimeroElMejorElement element = null;
		PrimeroElMejorElement elementToAdd;
		newFrom.str = from;
		newTo.str = to;
		System.out.println("Resolve: from " + newFrom + " to " + newTo);
		
		if (!graphG.getV().contains(newFrom)){
			System.out.println("Can not find the vertex: " + newFrom);
			return null;
//			throw new IllegalArgumentException("Can not find the vertex: " + newFrom);
		}
		if (!graphG.getV().contains(newTo)){
			System.out.println("Can not find the vertex: " + newTo);
			return null;
//			throw new IllegalArgumentException("Can not find the vertex: " + newTo);
		}
		
		for (Vertex s : graphG.getV())
			vElements.put(s.getCode(), s);

		System.out.println(vElements.toString());
		
		deltaX = vElements.get(to).getPositionX() - vElements.get(from).getPositionX();
		deltaY = vElements.get(to).getPositionY() - vElements.get(from).getPositionY();
		angle = Math.atan2(deltaY, deltaX) * 180 / Math.PI;
		angle = angle > 180 ? 360 - angle : angle;

		elementToAdd = new PrimeroElMejorElement(vElements.get(from), 1, 0.0, vElements.get(to), angle);
		elementToAdd.setPrevious(elementToAdd);
		putNeighbors(elementToAdd);
		
		bfsOpened.add(elementToAdd);
		
		while(!bfsOpened.isEmpty()){
			
			element = bfsOpened.firstElement();
			System.out.println("Level: " + element.getLevel());
			if(element.equalsStr(to)){
				fail = false;
				break;
			}
			
			bfsOpened.addAll(heuristicNeighbors(element, vElements.get(to), angle));
			
			bfsOpened.remove(element);
			bfsClosed.add(element);
			
			System.out.println("\n-");
			System.out.println("O:=(" + bfsOpened.size() + ")" + bfsOpened.toString());
			System.out.println("C:=(" + bfsClosed.size() + ")" + bfsClosed.toString());
		}
		if (fail) {
			System.out.println("Failed to resolve");
			return null;
		}
		System.out.println(element.printTrace());
		do{
			graphT.getV().add(element.getVertex());
			graphT.getE().add(new Edge(element.getPrevious().getVertex().getCode(), element.getVertex().getCode(), element.getWeight() - element.getPrevious().getWeight()));
			element = element.getPrevious();
		}while(element.getPrevious() != element);
		graphT.getV().add(element.getVertex());
		
		System.out.println(graphT.toString());
//		System.out.save();
		return graphT;
	}
	
	void putNeighbors(PrimeroElMejorElement elementToAdd){
		Map<String, Double> neighbors = new HashMap<String, Double>();
		for (Edge e : graphG.getE())
			if(elementToAdd.equalsStr(e.getFrom()))
				neighbors.put(e.getTo(), e.getDistance());
		elementToAdd.getNeighbors().putAll(neighbors);
	}
	
	private Vector<PrimeroElMejorElement> heuristicNeighbors (PrimeroElMejorElement bfsElement, Vertex vTo, double angle){
		
		Vector<PrimeroElMejorElement> res = new Vector<PrimeroElMejorElement>();
		System.out.println("All neighbors: " + bfsElement.getNeighbors().size());
		System.out.print("Neighbors: [");
		PrimeroElMejorElement elementToAdd;
		for(String neighborCode : bfsElement.getNeighbors().keySet()){
			for(Edge edge : graphG.getE()){
				if(bfsElement.equalsStr(edge.getFrom()) && neighborCode.equals(edge.getTo())){
					if(!contains(bfsClosed, neighborCode) && !containsRoute(bfsOpened, neighborCode, bfsElement.getVertex().getCode())){
						elementToAdd = new PrimeroElMejorElement(vElements.get(neighborCode), bfsElement.getLevel() + 1, bfsElement.getNeighbors().get(neighborCode) + bfsElement.getWeight(), vTo, angle);
						elementToAdd.setPrevious(bfsElement);
						putNeighbors(elementToAdd);
						res.add(elementToAdd);
						System.out.print( neighborCode + "  ");
					}
				}
			}
		}
		System.out.println("]\n");
		Collections.sort(res);
		for (PrimeroElMejorElement primeroElMejorElement : res) {
			System.out.print(primeroElMejorElement.getValue() + "|");
		}
		System.out.println();
		return res;
	}	
	
	private boolean contains(Vector<PrimeroElMejorElement> v, String str){
		for (PrimeroElMejorElement primeroElMejorElement : v)
			if(primeroElMejorElement.equalsStr(str))
				return true;
		return false;
	}
	
	private boolean containsRoute(Vector<PrimeroElMejorElement> v, String strActual, String strPrev){
		for (PrimeroElMejorElement primeroElMejorElement : v)
			if(primeroElMejorElement.equalsStr(strActual) && primeroElMejorElement.getPrevious().equalsStr(strPrev))
				return true;
		return false;
	}
	
}

class PrimeroElMejorElement implements Comparable<PrimeroElMejorElement>{
	private Vertex vertex;
	private Map<String, Double> neighbors = new HashMap<String, Double>();
	private PrimeroElMejorElement previous = null;
	private int level;
	private double weight;
	private double value;
	public Vertex getVertex(){
		return vertex;
	}
	
	public Map<String, Double> getNeighbors(){
		return neighbors;
	}
	
	public int getLevel(){
		return level;
	}
	
	public void setPrevious(PrimeroElMejorElement previous){
		this.previous = previous;
	}
	
	public PrimeroElMejorElement getPrevious(){
		return previous;
	}
	
	PrimeroElMejorElement(Vertex vertex, int level, double weight, Vertex vTo, double oAngle){
		this.vertex = vertex;
		this.level = level;
		this.weight = weight;
		this.value = heuristicValuer(vTo, oAngle);
	}
	
	void setWeight(double w){
		this.weight = w;
	}
	
	double getWeight(){
		return weight;
	}
	
	void setNeighbors(Map<String, Double> neighbors){
		this.neighbors = neighbors;
	}
	
	public String toString(){
		String res = "";
		String previosName = previous != null ? previous.vertex.getCode() : "-";
		return res + vertex.getCode() + "[" + previosName + "," + level + "]";
	}
	
	String printTrace(){
		String res = this != this.previous ? previous.printTrace() + " > " : "";
		return res + this.toString();
	}
	
	public boolean equalsStr(String str){
		return this.vertex.getCode().equals(str);
	}
	public double getValue(){
		return value;
	}
	public double heuristicValuer(Vertex vTo, double oAngle){
		double distance, angle;
		double deltaX, deltaY;
		deltaX = vTo.getPositionX() - vertex.getPositionX();
		deltaY = vTo.getPositionY() - vertex.getPositionY();
		distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
		angle = Math.atan2(deltaY, deltaX) * 180 / Math.PI;
		angle = angle > 180 ? 360 - angle : angle;
		angle = Math.abs(oAngle - angle);
		double res = ((angle + 1) * 0.5) + ((distance + 1) * 10) + ((vertex.getDanger() + 1) * 1000);
		return res;
	}

	public int compareTo(PrimeroElMejorElement o) {
		return Double.compare(value, o.getValue());
	}
}