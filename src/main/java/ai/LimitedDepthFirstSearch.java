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
	private Map<String, Vertex> vElements = new HashMap<String, Vertex>();
	private String[] rules = null;// {"270","0","90","180"};
	private Vector<LimitedDepthFirstSearchElement> ldfsOpened = new Vector<LimitedDepthFirstSearchElement>();
	private Vector<LimitedDepthFirstSearchElement> ldfsClosed = new Vector<LimitedDepthFirstSearchElement>();
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
		NewString newFrom = new NewString(), newTo = new NewString();
		LimitedDepthFirstSearchElement element = null, elementToAdd;
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

		elementToAdd = new LimitedDepthFirstSearchElement(vElements.get(from), 1, 0.0);
		elementToAdd.setPrevious(elementToAdd);
		putNeighbors(elementToAdd);
		
		ldfsOpened.add(elementToAdd);

		while(!ldfsOpened.isEmpty()){
			
			element = ldfsOpened.lastElement();
			System.out.println("Level: " + element.getLevel());
			if(element.equalsStr(to)){
				fail = false;
				break;
			}
			
			if(element.getLevel() <= getBiggerLevel(ldfsClosed))
				for (int i = 0; i < ldfsClosed.size(); i++)
					if (ldfsClosed.elementAt(i).getLevel() >= element.getLevel())
						ldfsClosed.remove(ldfsClosed.elementAt(i--));

			
			if(element.getLevel() < limitLevel){
				if(rules != null){
					ldfsOpened.addAll(ruledNeighbors(rules, element));
				}else{
					for (String neighborCode : element.getNeighbors().keySet()) {
						if(!contains(ldfsClosed, neighborCode)){
							elementToAdd = new LimitedDepthFirstSearchElement(vElements.get(neighborCode), element.getLevel() + 1, element.getNeighbors().get(neighborCode) + element.getWeight());
							elementToAdd.setPrevious(element);
							putNeighbors(elementToAdd);
							ldfsOpened.add(elementToAdd);
						}
					}
				}
			}
			
			ldfsOpened.remove(element);
			ldfsClosed.add(element);
			System.out.println("----------------------------------");
			System.out.println("O:=" + ldfsOpened.toString());
			System.out.println("C:=" + ldfsClosed.toString());
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
	
	void putNeighbors(LimitedDepthFirstSearchElement ldfsElement){
		Map<String, Double> neighbors = new HashMap<String, Double>();
		for (Edge e : graphG.getE())
			if(ldfsElement.equalsStr(e.getFrom()))
				neighbors.put(e.getTo(), e.getDistance());
		ldfsElement.getNeighbors().putAll(neighbors);
	}
	
	private int getBiggerLevel(Vector<LimitedDepthFirstSearchElement> v){
		int res = 1;
		for (LimitedDepthFirstSearchElement limitedDepthFirstSearchElement : v)
			if(limitedDepthFirstSearchElement.getLevel() > res) 
				res = limitedDepthFirstSearchElement.getLevel();
		return res;
	}
	
	private Vector<LimitedDepthFirstSearchElement> ruledNeighbors (String[] rules, LimitedDepthFirstSearchElement ldfsElement){
		Vector<LimitedDepthFirstSearchElement> res = new Vector<LimitedDepthFirstSearchElement>();
		System.out.println("All neighbors: " + ldfsElement.getNeighbors().size());
		System.out.print("Ruled neighbors: [");
		String strRule = "";
		LimitedDepthFirstSearchElement elementToAdd;
		for (int i = rules.length - 1; i >=0 ; i--) {
			strRule = rules[i];
			for(String neighborCode : ldfsElement.getNeighbors().keySet()){
				for(Edge edge : graphG.getE()){
					if(ldfsElement.equalsStr(edge.getFrom()) && neighborCode.equals(edge.getTo()) && strRule.equals(edge.getOrderCode())){
						if(!contains(ldfsClosed, neighborCode)){
							elementToAdd = new LimitedDepthFirstSearchElement(vElements.get(neighborCode), ldfsElement.getLevel() + 1, ldfsElement.getNeighbors().get(neighborCode) + ldfsElement.getWeight());
							elementToAdd.setPrevious(ldfsElement);
							putNeighbors(elementToAdd);
							res.add(elementToAdd);
							System.out.print(strRule + ": " + neighborCode + "  ");
						}
					}
				}
			}
		}
		System.out.println("]\n");
		return res;
	}
	
	private boolean contains(Vector<LimitedDepthFirstSearchElement> v, String str){
		for (LimitedDepthFirstSearchElement limitedDepthFirstSearchElement : v)
			if(limitedDepthFirstSearchElement.equalsStr(str))
				return true;
		return false;
	}
	
}

class LimitedDepthFirstSearchElement{
	private Vertex vertex;
	private Map<String, Double> neighbors = new HashMap<String, Double>();
	private LimitedDepthFirstSearchElement previous = null;
	private int level;
	private double weight;
	
	public Vertex getVertex(){
		return vertex;
	}
	
	public Map<String, Double> getNeighbors(){
		return neighbors;
	}
	
	public int getLevel(){
		return level;
	}
	
	public void setPrevious(LimitedDepthFirstSearchElement previous){
		this.previous = previous;
	}
	
	public LimitedDepthFirstSearchElement getPrevious(){
		return previous;
	}
	
	LimitedDepthFirstSearchElement(Vertex vertex, int level, double weight){
		this.vertex = vertex;
		this.level = level;
		this.weight = weight;
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
}