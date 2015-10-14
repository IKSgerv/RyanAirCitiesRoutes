package ai;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

public class DepthFirstSearch {
	private Graph graphG;
	private Graph graphT = new Graph();
	private Map<String, DepthFirstSearchElement> depthFirstSearchElements = new HashMap<String, DepthFirstSearchElement>();
	private String[] rules = null;// {"270","0","90","180"};
	private Vector<DepthFirstSearchElement> opened = new Vector<DepthFirstSearchElement>();
	private Vector<DepthFirstSearchElement> closed = new Vector<DepthFirstSearchElement>();
	
	public DepthFirstSearch(Graph g){
		System.out.println("DepthFirstSearch Controller - Started");
		graphG = g;
		System.out.println("G: {\n" + g.toString() + "\n}\n"
				+ " Vertices: " + graphG.getV().size() + "\n"
				+ " Edges: " + graphG.getE().size());
	}
	
	public void setRules(String[] rul){
		this.rules = rul;
	}
	
	public Graph resolve(String from, String to){
		boolean fail = true;
		NewString newFrom = new NewString();
		NewString newTo = new NewString();
		newFrom.str = from;
		newTo.str = to;
		System.out.println("Resolve: from " + newFrom + " to " + newTo);
		DepthFirstSearchElement element = null;
		
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
			depthFirstSearchElements.put(s.getCode(), new DepthFirstSearchElement(s));
		
		for (Edge e : graphG.getE())
			depthFirstSearchElements.get(e.getFrom()).neighbors.put(e.getTo(), e.getDistance());//here is geter the order

		System.out.println(depthFirstSearchElements.toString());
		depthFirstSearchElements.get(from).setWeight(0.0);
		depthFirstSearchElements.get(from).previous = depthFirstSearchElements.get(from);
		opened.add(depthFirstSearchElements.get(from));
		while(!closed.contains(depthFirstSearchElements.get(to))){
			if(!opened.isEmpty())
				element = opened.lastElement();
			else
				break;
			
			if(element.equals(depthFirstSearchElements.get(to))){
				closed.add(element);
				fail = false;
				break;
			}
			
			if(rules != null){
				opened.addAll(ruledNeighbors(rules, element));
			}else{
				for (String neighborCode : element.neighbors.keySet()) {
					if(!closed.contains(depthFirstSearchElements.get(neighborCode))){
						depthFirstSearchElements.get(neighborCode).setWeight(element.neighbors.get(neighborCode) + element.getWeight());
						depthFirstSearchElements.get(neighborCode).previous = element;
						opened.add(depthFirstSearchElements.get(neighborCode));
					}
				}
			}
			
			opened.remove(element);
			if(!closed.contains(element)){
				closed.add(element);
			}
			System.out.println("\n-");
			System.out.println("O:=" + opened.toString());
			System.out.println("C:=" + closed.toString());
		}
		if (fail) {
			System.out.println("Failed to resolve");
			return null;
		}
		System.out.println(element.printTrace());
		do{
			graphT.getV().add(element.vertex);
			graphT.getE().add(new Edge(element.previous.vertex.getCode(), element.vertex.getCode(), element.getWeight() - element.previous.getWeight()));
			element = element.previous;
		}while(element.previous != element);
		graphT.getV().add(element.vertex);
		
		System.out.println(graphT.toString());
//		System.out.save();
		return graphT;
	}
	
	private Vector<DepthFirstSearchElement> ruledNeighbors (String[] rules, DepthFirstSearchElement element){
		Vector<DepthFirstSearchElement> res = new Vector<DepthFirstSearchElement>();
		System.out.print("Ruled neighbors: [");
		String strRule = "";
		for (int i = rules.length - 1; i >=0 ; i--) {
			strRule = rules[i];
			for(String neighborCode : element.neighbors.keySet()){
				for(Edge edge : graphG.getE()){
					if(edge.getFrom().equals(element.vertex.getCode()) && edge.getTo().equals(neighborCode) && edge.getOrderCode().equals(strRule)){
						//Here we have to let open again the closed vertex
						if(!closed.contains(depthFirstSearchElements.get(neighborCode))){
							depthFirstSearchElements.get(neighborCode).setWeight(element.neighbors.get(neighborCode) + element.getWeight());
							depthFirstSearchElements.get(neighborCode).previous = element;
							res.add(depthFirstSearchElements.get(neighborCode));
							System.out.print(strRule + ": " + neighborCode + "  ");
						}
					}
				}
			}
		}
		System.out.println("]\n");
		return res;
	}
}

class DepthFirstSearchElement{
	Vertex vertex;
	Map<String, Double> neighbors = new HashMap<String, Double>();
	DepthFirstSearchElement previous = null;
	double weight;
	
	DepthFirstSearchElement(Vertex vertex){
		this.vertex = vertex;
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
		return res + vertex + "(" + previosName + ")";
	}
	
	String printTrace(){
		String res = this != this.previous ? previous.printTrace() + " > " : "";
		return res + vertex.getCode() + "(" + weight + ", " + previous.vertex.getCode() + ")";
	}
}