package ai;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

public class LimitedDepthFirstSearch {
	private Graph graphG;
	private Graph graphT = new Graph();
	private Map<String, LimitedDepthFirstSearchElement> limitedDepthFirstSearchElements = new HashMap<String, LimitedDepthFirstSearchElement>();
	private String[] rules = null;// {"270","0","90","180"};
	private Vector<LimitedDepthFirstSearchElement> opened = new Vector<LimitedDepthFirstSearchElement>();
	private Vector<LimitedDepthFirstSearchElement> closed = new Vector<LimitedDepthFirstSearchElement>();
	private int limitLevel;
	
	public LimitedDepthFirstSearch(Graph g){
		System.out.println("LimitedDepthFirstSearch Controller - Started");
		graphG = g;
		System.out.println("G: {\n" + g.toString() + "\n}\n"
				+ " Vertices: " + graphG.getV().size() + "\n"
				+ " Edges: " + graphG.getE().size());
	}
	
	public void setRules(String[] rul){
		this.rules = rul;
	}
	
	public void setLimitLevel(int limit){
		limitLevel = limit;
	}
	
	public Graph resolve(String from, String to){
		boolean fail = true;
		NewString newFrom = new NewString();
		NewString newTo = new NewString();
		newFrom.str = from;
		newTo.str = to;
		System.out.println("Resolve: from " + newFrom + " to " + newTo);
		LimitedDepthFirstSearchElement element = null;
		
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
			limitedDepthFirstSearchElements.put(s.getCode(), new LimitedDepthFirstSearchElement(s));
		
		for (Edge e : graphG.getE())
			limitedDepthFirstSearchElements.get(e.getFrom()).neighbors.put(e.getTo(), e.getDistance());//here is geter the order

		System.out.println(limitedDepthFirstSearchElements.toString());
		limitedDepthFirstSearchElements.get(from).setWeight(0.0);
		limitedDepthFirstSearchElements.get(from).previous = limitedDepthFirstSearchElements.get(from);
		limitedDepthFirstSearchElements.get(from).level = 1;
		opened.add(limitedDepthFirstSearchElements.get(from));
		while(!closed.contains(limitedDepthFirstSearchElements.get(to))){
			if(!opened.isEmpty())
				element = opened.lastElement();
			else
				break;
			
			if(element.equals(limitedDepthFirstSearchElements.get(to))){
				closed.add(element);
				fail = false;
				break;
			}
			
			if(element.level < limitLevel){
				if(rules != null){
					opened.addAll(ruledNeighbors(rules, element));
				}else{
					for (String neighborCode : element.neighbors.keySet()) {
						if(!closed.contains(limitedDepthFirstSearchElements.get(neighborCode))){
							limitedDepthFirstSearchElements.get(neighborCode).setWeight(element.neighbors.get(neighborCode) + element.getWeight());
							limitedDepthFirstSearchElements.get(neighborCode).previous = element;
							limitedDepthFirstSearchElements.get(neighborCode).level = element.level + 1;
							opened.add(limitedDepthFirstSearchElements.get(neighborCode));
						}
					}
				}
			}else{
				//Remove all closed elements from the level to up
				
				
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
	
	private Vector<LimitedDepthFirstSearchElement> ruledNeighbors (String[] rules, LimitedDepthFirstSearchElement element){
		Vector<LimitedDepthFirstSearchElement> res = new Vector<LimitedDepthFirstSearchElement>();
		System.out.print("Ruled neighbors: [");
		String strRule = "";
		for (int i = rules.length - 1; i >=0 ; i--) {
			strRule = rules[i];
			for(String neighborCode : element.neighbors.keySet()){
				for(Edge edge : graphG.getE()){
					if(edge.getFrom().equals(element.vertex.getCode()) && edge.getTo().equals(neighborCode) && edge.getOrderCode().equals(strRule)){
						//Here we have to let open again the closed vertex
						if(!closed.contains(limitedDepthFirstSearchElements.get(neighborCode))){
							limitedDepthFirstSearchElements.get(neighborCode).setWeight(element.neighbors.get(neighborCode) + element.getWeight());
							limitedDepthFirstSearchElements.get(neighborCode).previous = element;
							limitedDepthFirstSearchElements.get(neighborCode).level = element.level + 1;
							res.add(limitedDepthFirstSearchElements.get(neighborCode));
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

class LimitedDepthFirstSearchElement{
	Vertex vertex;
	Map<String, Double> neighbors = new HashMap<String, Double>();
	LimitedDepthFirstSearchElement previous = null;
	int level;
	double weight;
	
	LimitedDepthFirstSearchElement(Vertex vertex){
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
		return res + vertex + "(" + previosName + "," + level + ")";
	}
	
	String printTrace(){
		String res = this != this.previous ? previous.printTrace() + " > " : "";
		return res + vertex.getCode() + "(" + weight + ", " + previous.vertex.getCode() + ")";
	}
}