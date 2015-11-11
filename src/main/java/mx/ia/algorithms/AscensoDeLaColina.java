package mx.ia.algorithms;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import mx.ia.graph.Edge;
import mx.ia.graph.Graph;
import mx.ia.graph.Vertex;

public class AscensoDeLaColina {
	private Graph graphG;
	private Graph graphT = new Graph();
	private Map<String, Vertex> vElements = new HashMap<String, Vertex>();
	private Vector<AscensoDeLaColinaElement> dfsOpened = new Vector<AscensoDeLaColinaElement>();
	private Vector<AscensoDeLaColinaElement> dfsClosed = new Vector<AscensoDeLaColinaElement>();
	
	public AscensoDeLaColina(Graph g){
		System.out.println("AscensoDeLaColina Controller - Started");
		graphG = g;
		System.out.println("G: {\n" + g.toString() + "\n}\n"
				+ " Vertices: " + graphG.getV().size() + "\n"
				+ " Edges: " + graphG.getE().size());
	}
	public Graph resolve(String from, String to){
		boolean fail = true;
		double angle, deltaX, deltaY;
		NewString newFrom = new NewString(), newTo = new NewString();
		AscensoDeLaColinaElement element = null, elementToAdd;
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

		elementToAdd = new AscensoDeLaColinaElement(vElements.get(from), 1, 0.0, vElements.get(to), null);
		putNeighbors(elementToAdd);
		
		dfsOpened.add(elementToAdd);
		
		while(!dfsOpened.isEmpty()){
			
			element = dfsOpened.lastElement();
			System.out.println("Level: " + element.getLevel());
			System.out.println("Selected: " + element);
			if(element.equalsStr(to)){
				fail = false;
				break;
			}
			
			dfsOpened.addAll(heuristicNeighbors(element, vElements.get(to)));
			
			dfsOpened.remove(element);
			dfsClosed.add(element);
			
			System.out.println("----------------------------------");
			System.out.println("O:=(" + dfsOpened.size() + ")" + dfsOpened.toString());
			System.out.println("C:=(" + dfsClosed.size() + ")" + dfsClosed.toString());
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
		}while(!element.getPrevious().equalsStr(element.getVertex().getCode()));
		graphT.getV().add(element.getVertex());
		
		System.out.println(graphT.toString());
//		System.out.save();
		return graphT;
	}
	
	void putNeighbors(AscensoDeLaColinaElement elementToAdd){
		Map<String, Double> neighbors = new HashMap<String, Double>();
		for (Edge e : graphG.getE())
			if(elementToAdd.equalsStr(e.getFrom()))
				neighbors.put(e.getTo(), e.getDistance());
		elementToAdd.getNeighbors().putAll(neighbors);
	}
	
	private Vector<AscensoDeLaColinaElement> heuristicNeighbors (AscensoDeLaColinaElement bfsElement, Vertex vTo){
		
		Vector<AscensoDeLaColinaElement> res = new Vector<AscensoDeLaColinaElement>();
		System.out.println("All neighbors: " + bfsElement.getNeighbors().size());
		System.out.print("Neighbors: [");
		AscensoDeLaColinaElement elementToAdd;
		for(String neighborCode : bfsElement.getNeighbors().keySet()){
			for(Edge edge : graphG.getE()){
				if(bfsElement.equalsStr(edge.getFrom()) && neighborCode.equals(edge.getTo())){
					if(!contains(dfsClosed, neighborCode)){
						elementToAdd = new AscensoDeLaColinaElement(vElements.get(neighborCode), bfsElement.getLevel() + 1, bfsElement.getNeighbors().get(neighborCode) + bfsElement.getWeight(), vTo, bfsElement);
						putNeighbors(elementToAdd);
						res.add(elementToAdd);
						System.out.print( neighborCode + "  ");
					}
				}
			}
		}
		System.out.println("]\n");
		Collections.sort(res);
		for (AscensoDeLaColinaElement ascensoDeLaColinaElement : res) {
			System.out.print(ascensoDeLaColinaElement.getVertex().getCode() + ":" + ascensoDeLaColinaElement.getValue() + "|");
		}
		System.out.println();
		return res;
	}
	
	private boolean contains(Vector<AscensoDeLaColinaElement> dfsClosed2, String str){
		for (AscensoDeLaColinaElement ascensoDeLaColinaElement : dfsClosed2)
			if(ascensoDeLaColinaElement.equalsStr(str))
				return true;
		return false;
	}
	
}

class AscensoDeLaColinaElement implements Comparable<AscensoDeLaColinaElement>{
	private Vertex vertex;
	private Map<String, Double> neighbors = new HashMap<String, Double>();
	private AscensoDeLaColinaElement previous = null;
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
	public AscensoDeLaColinaElement getPrevious(){
		return previous;
	}
	AscensoDeLaColinaElement(Vertex vertex, int level, double weight, Vertex vTo, AscensoDeLaColinaElement previous){
		this.vertex = vertex;
		this.level = level;
		this.weight = weight;
		if(previous != null)
			this.previous = previous;
		else
			this.previous = this;
		this.value = heuristicValuer(vTo);
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
	public double heuristicValuer(Vertex vTo){
		double distance, angle, angle2;
		double deltaX, deltaY, deltaX2, deltaY2;
		deltaX = vTo.getPositionX() - vertex.getPositionX();
		deltaY = vTo.getPositionY() - vertex.getPositionY();
		deltaX2 = vTo.getPositionX() - previous.getVertex().getPositionX();
		deltaY2 = vTo.getPositionY() - previous.getVertex().getPositionY();
		
		distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
		
		angle = Math.atan2(deltaY, deltaX) * 180 / Math.PI;
		angle = angle > 180 ? 360 - angle : angle;
		
		angle2 = Math.atan2(deltaY2, deltaX2) * 180 / Math.PI;
		angle2 = angle2 > 180 ? 360 - angle2 : angle2;
		angle = Math.abs(angle2 - angle);
		double res = ((angle + 1) * 0.1) + ((distance + 1) * 12) + ((vertex.getDanger() + 1) * 62);
		System.out.println(":::" + vertex.getCode() + ":" + res + "|" + angle + "|" + distance + "|" + ((vertex.getDanger() + 1) * 100));
		return res;
	}
	public int compareTo(AscensoDeLaColinaElement o) {
		return Double.compare(o.getValue(), value);
	}
}