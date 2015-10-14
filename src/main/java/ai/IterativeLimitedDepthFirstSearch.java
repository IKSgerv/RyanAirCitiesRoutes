package ai;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

public class IterativeLimitedDepthFirstSearch {
	private Graph graphG;
	private Graph graphT = new Graph();
	private Map<String, IterativeLimitedDepthFirstSearchElement> iterativeLimitedDepthFirstSearchElements = new HashMap<String, IterativeLimitedDepthFirstSearchElement>();
	private String[] rules = null;// {"270","0","90","180"};
	private Vector<IterativeLimitedDepthFirstSearchElement> opened = new Vector<IterativeLimitedDepthFirstSearchElement>();
	private Vector<IterativeLimitedDepthFirstSearchElement> closed = new Vector<IterativeLimitedDepthFirstSearchElement>();
	private int limitLevel;
	private int iterativeLimitLevel;
	
	public IterativeLimitedDepthFirstSearch(Graph g){
		System.out.println("IterativeLimitedDepthFirstSearch Controller - Started");
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
	
	public void setiterativeLimitLevel(int iterativeLimitLevel){
		this.iterativeLimitLevel = iterativeLimitLevel;
	}
	
	public Graph resolve(String from, String to){
		boolean fail = true;
		NewString newFrom = new NewString();
		NewString newTo = new NewString();
		newFrom.str = from;
		newTo.str = to;
		System.out.println("Resolve: from " + newFrom + " to " + newTo);
		IterativeLimitedDepthFirstSearchElement element = null;
		
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
			iterativeLimitedDepthFirstSearchElements.put(s.getCode(), new IterativeLimitedDepthFirstSearchElement(s));
		
		for (Edge e : graphG.getE())
			iterativeLimitedDepthFirstSearchElements.get(e.getFrom()).neighbors.put(e.getTo(), e.getDistance());//here is geter the order

		System.out.println(iterativeLimitedDepthFirstSearchElements.toString());
		iterativeLimitedDepthFirstSearchElements.get(from).setWeight(0.0);
		iterativeLimitedDepthFirstSearchElements.get(from).previous = iterativeLimitedDepthFirstSearchElements.get(from);
		iterativeLimitedDepthFirstSearchElements.get(from).level = 1;
		opened.add(iterativeLimitedDepthFirstSearchElements.get(from));
		do{
			fail =  true;
			while(!closed.contains(iterativeLimitedDepthFirstSearchElements.get(to))){
				if(!opened.isEmpty())
					element = opened.lastElement();
				else
					break;
				
				if(element.equals(iterativeLimitedDepthFirstSearchElements.get(to))){
					closed.add(element);
					fail = false;
					break;
				}
				
				if(element.level < limitLevel){
					if(rules != null){
						opened.addAll(ruledNeighbors(rules, element));
					}else{
						for (String neighborCode : element.neighbors.keySet()) {
							if(!closed.contains(iterativeLimitedDepthFirstSearchElements.get(neighborCode))){
								iterativeLimitedDepthFirstSearchElements.get(neighborCode).setWeight(element.neighbors.get(neighborCode) + element.getWeight());
								iterativeLimitedDepthFirstSearchElements.get(neighborCode).previous = element;
								iterativeLimitedDepthFirstSearchElements.get(neighborCode).level = element.level + 1;
								opened.add(iterativeLimitedDepthFirstSearchElements.get(neighborCode));
							}
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
				//Reset the iterativeLimitedDepthFirstSearchElements
				limitLevel = limitLevel + iterativeLimitLevel;
				for (Entry<String, IterativeLimitedDepthFirstSearchElement> entryElement : iterativeLimitedDepthFirstSearchElements.entrySet()) {
					entryElement.getValue().previous = null;
					entryElement.getValue().setWeight(0.0);
				}
				
				opened.clear();
				closed.clear();
				
				iterativeLimitedDepthFirstSearchElements.get(from).setWeight(0.0);
				iterativeLimitedDepthFirstSearchElements.get(from).previous = iterativeLimitedDepthFirstSearchElements.get(from);
				iterativeLimitedDepthFirstSearchElements.get(from).level = 1;
				opened.add(iterativeLimitedDepthFirstSearchElements.get(from));
				System.out.println("New iteration: " + limitLevel);
				System.out.println("O:=" + opened.toString());
				System.out.println("C:=" + closed.toString());
			}
			System.out.println("fail: " + fail + ", limitLevel: " + limitLevel);
		}while(fail && limitLevel < graphG.getV().size());
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
	
	private Vector<IterativeLimitedDepthFirstSearchElement> ruledNeighbors (String[] rules, IterativeLimitedDepthFirstSearchElement element){
		Vector<IterativeLimitedDepthFirstSearchElement> res = new Vector<IterativeLimitedDepthFirstSearchElement>();
		System.out.print("Ruled neighbors: [");
		String strRule = "";
		for (int i = rules.length - 1; i >=0 ; i--) {
			strRule = rules[i];
			for(String neighborCode : element.neighbors.keySet()){
				for(Edge edge : graphG.getE()){
					if(edge.getFrom().equals(element.vertex.getCode()) && edge.getTo().equals(neighborCode) && edge.getOrderCode().equals(strRule)){
						//Here we have to let open again the closed vertex
						if(!closed.contains(iterativeLimitedDepthFirstSearchElements.get(neighborCode))){
							iterativeLimitedDepthFirstSearchElements.get(neighborCode).setWeight(element.neighbors.get(neighborCode) + element.getWeight());
							iterativeLimitedDepthFirstSearchElements.get(neighborCode).previous = element;
							iterativeLimitedDepthFirstSearchElements.get(neighborCode).level = element.level + 1;
							res.add(iterativeLimitedDepthFirstSearchElements.get(neighborCode));
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

class IterativeLimitedDepthFirstSearchElement{
	Vertex vertex;
	Map<String, Double> neighbors = new HashMap<String, Double>();
	IterativeLimitedDepthFirstSearchElement previous = null;
	int level;
	double weight;
	
	IterativeLimitedDepthFirstSearchElement(Vertex vertex){
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