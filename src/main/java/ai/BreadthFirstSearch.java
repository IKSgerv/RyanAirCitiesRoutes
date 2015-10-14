package ai;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

public class BreadthFirstSearch {
	private Graph graphG;
	private Graph graphT = new Graph();
	private Map<String, BreadthFirstSearchElement> breadthFirstSearchElements = new HashMap<String, BreadthFirstSearchElement>();
	private String[] rules = null;// {"270","0","90","180"};
	private Vector<BreadthFirstSearchElement> opened = new Vector<BreadthFirstSearchElement>();
	private Vector<BreadthFirstSearchElement> closed = new Vector<BreadthFirstSearchElement>();
	
	public BreadthFirstSearch(Graph g){
		System.out.println("BreadthFirstSearch Controller - Started");
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
		BreadthFirstSearchElement element = null;
		
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
			breadthFirstSearchElements.put(s.getCode(), new BreadthFirstSearchElement(s));
		
		for (Edge e : graphG.getE())
			breadthFirstSearchElements.get(e.getFrom()).neighbors.put(e.getTo(), e.getDistance());//here is geter the order

		System.out.println(breadthFirstSearchElements.toString());
		breadthFirstSearchElements.get(from).setWeight(0.0);
		breadthFirstSearchElements.get(from).previous = breadthFirstSearchElements.get(from);
		opened.add(breadthFirstSearchElements.get(from));
		while(!closed.contains(breadthFirstSearchElements.get(to))){
			if(!opened.isEmpty())
				element = opened.firstElement();
			else
				break;
			
			if(element.equals(breadthFirstSearchElements.get(to))){
				closed.add(element);
				fail = false;
				break;
			}
			
			if(rules != null){
				opened.addAll(ruledNeighbors(rules, element));
			}else{
				for (String neighborCode : element.neighbors.keySet()) {
					if(!closed.contains(breadthFirstSearchElements.get(neighborCode))){
						breadthFirstSearchElements.get(neighborCode).setWeight(element.neighbors.get(neighborCode) + element.getWeight());
						breadthFirstSearchElements.get(neighborCode).previous = element;
						opened.add(breadthFirstSearchElements.get(neighborCode));
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
	
	private Vector<BreadthFirstSearchElement> ruledNeighbors (String[] rules, BreadthFirstSearchElement element){
		Vector<BreadthFirstSearchElement> res = new Vector<BreadthFirstSearchElement>();
		System.out.print("Ruled neighbors: [");
		for (String strRule : rules) {
			for(String neighborCode : element.neighbors.keySet()){
				for(Edge edge : graphG.getE()){
					if(edge.getFrom().equals(element.vertex.getCode()) && edge.getTo().equals(neighborCode) && edge.getOrderCode().equals(strRule)){
						//Here we have to let open again the closed vertex
						if(!closed.contains(breadthFirstSearchElements.get(neighborCode))){
							breadthFirstSearchElements.get(neighborCode).setWeight(element.neighbors.get(neighborCode) + element.getWeight());
							breadthFirstSearchElements.get(neighborCode).previous = element;
							res.add(breadthFirstSearchElements.get(neighborCode));
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

class BreadthFirstSearchElement{
	Vertex vertex;
	Map<String, Double> neighbors = new HashMap<String, Double>();
	BreadthFirstSearchElement previous = null;
	double weight;
	
	BreadthFirstSearchElement(Vertex vertex){
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