package ai;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

public class IterativeLimitedDepthFirstSearch {
	private Graph graphG;
	private Graph graphT = new Graph();
	private Map<String, Vertex> vElements = new HashMap<String, Vertex>();
	private Vector<String> rules = null;// {"270","0","90","180"};
	private Vector<IterativeLimitedDepthFirstSearchElement> ildfsOpened = new Vector<IterativeLimitedDepthFirstSearchElement>();
	private Vector<IterativeLimitedDepthFirstSearchElement> ildfsClosed = new Vector<IterativeLimitedDepthFirstSearchElement>();
	private int limitLevel;
	private int iterativeLimitLevel;
	
	public IterativeLimitedDepthFirstSearch(Graph g){
		System.out.println("IterativeLimitedDepthFirstSearch Controller - Started");
		graphG = g;
		System.out.println("G: {\n" + g.toString() + "\n}\n"
				+ " Vertices: " + graphG.getV().size() + "\n"
				+ " Edges: " + graphG.getE().size());
	}
	
	public void setRules(Vector<String> rul){
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
		NewString newFrom = new NewString(), newTo = new NewString();
		IterativeLimitedDepthFirstSearchElement element = null, elementToAdd;
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

		elementToAdd = new IterativeLimitedDepthFirstSearchElement(vElements.get(from), 1, 0.0);
		elementToAdd.setPrevious(elementToAdd);
		putNeighbors(elementToAdd);
		
		ildfsOpened.add(elementToAdd);
		
		do{
			fail =  true;
			while(!ildfsOpened.isEmpty()){
				
				element = ildfsOpened.lastElement();
				System.out.println("Level: " + element.getLevel());
				if(element.equalsStr(to)){
					fail = false;
					break;
				}
				
				if(element.getLevel() <= getBiggerLevel(ildfsClosed))
					for (int i = 0; i < ildfsClosed.size(); i++)
						if (ildfsClosed.elementAt(i).getLevel() >= element.getLevel())
							ildfsClosed.remove(ildfsClosed.elementAt(i--));
				
				if(element.getLevel() < limitLevel){
					if(rules != null){
						ildfsOpened.addAll(ruledNeighbors(rules, element));
					}else{
						for (String neighborCode : element.getNeighbors().keySet()) {
							if(!contains(ildfsClosed, neighborCode)){
								elementToAdd = new IterativeLimitedDepthFirstSearchElement(vElements.get(neighborCode), element.getLevel() + 1, element.getNeighbors().get(neighborCode) + element.getWeight());
								elementToAdd.setPrevious(element);
								putNeighbors(elementToAdd);
								ildfsOpened.add(elementToAdd);
							}
						}
					}
				}
				
				ildfsOpened.remove(element);
				ildfsClosed.add(element);
				System.out.println("----------------------------------");
				System.out.println("O:=(" + ildfsOpened.size() + ")" + ildfsOpened.toString());
				System.out.println("C:=(" + ildfsClosed.size() + ")" + ildfsClosed.toString());
			}
			if (fail) {
				limitLevel = limitLevel + iterativeLimitLevel;
				
				ildfsOpened.clear();
				ildfsClosed.clear();
				
				elementToAdd = new IterativeLimitedDepthFirstSearchElement(vElements.get(from), 1, 0.0);
				elementToAdd.setPrevious(elementToAdd);
				putNeighbors(elementToAdd);
				
				ildfsOpened.add(elementToAdd);
				
				System.out.println("::::::::::::::::::::::::::::::::::");
				System.out.println("New iteration: " + limitLevel);
				System.out.println("O:=" + ildfsOpened.toString());
				System.out.println("C:=" + ildfsClosed.toString());
			}
			System.out.println("fail: " + fail + ", limitLevel: " + limitLevel);
		}while(fail && limitLevel < graphG.getV().size());
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
	
	void putNeighbors(IterativeLimitedDepthFirstSearchElement ildfsElement){
		Map<String, Double> neighbors = new HashMap<String, Double>();
		for (Edge e : graphG.getE())
			if(ildfsElement.equalsStr(e.getFrom()))
				neighbors.put(e.getTo(), e.getDistance());
		ildfsElement.getNeighbors().putAll(neighbors);
	}
	
	private int getBiggerLevel(Vector<IterativeLimitedDepthFirstSearchElement> v){
		int res = 1;
		for (IterativeLimitedDepthFirstSearchElement iterativeLimitedDepthFirstSearchElement : v)
			if(iterativeLimitedDepthFirstSearchElement.getLevel() > res) 
				res = iterativeLimitedDepthFirstSearchElement.getLevel();
		return res;
	}
	
	private Vector<IterativeLimitedDepthFirstSearchElement> ruledNeighbors (Vector<String> rules, IterativeLimitedDepthFirstSearchElement ildfsElement){
		
		Vector<IterativeLimitedDepthFirstSearchElement> res = new Vector<IterativeLimitedDepthFirstSearchElement>();
		System.out.println("All neighbors: " + ildfsElement.getNeighbors().size());
		System.out.print("Ruled neighbors: [");
		String strRule = "";
		IterativeLimitedDepthFirstSearchElement elementToAdd;
		for (int i = rules.size() - 1; i >=0 ; i--) {
			strRule = rules.elementAt(i);
			for(String neighborCode : ildfsElement.getNeighbors().keySet()){
				for(Edge edge : graphG.getE()){
					if(ildfsElement.equalsStr(edge.getFrom()) && neighborCode.equals(edge.getTo()) && strRule.equals(edge.getOrderCode())){
						if(!contains(ildfsClosed, neighborCode)){
							elementToAdd = new IterativeLimitedDepthFirstSearchElement(vElements.get(neighborCode), ildfsElement.getLevel() + 1, ildfsElement.getNeighbors().get(neighborCode) + ildfsElement.getWeight());
							elementToAdd.setPrevious(ildfsElement);
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
	
	private boolean contains(Vector<IterativeLimitedDepthFirstSearchElement> v, String str){
		for (IterativeLimitedDepthFirstSearchElement iterativeLimitedDepthFirstSearchElement : v)
			if(iterativeLimitedDepthFirstSearchElement.equalsStr(str))
				return true;
		return false;
	}
	
}

class IterativeLimitedDepthFirstSearchElement{
	private Vertex vertex;
	private Map<String, Double> neighbors = new HashMap<String, Double>();
	private IterativeLimitedDepthFirstSearchElement previous = null;
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
	
	public void setPrevious(IterativeLimitedDepthFirstSearchElement previous){
		this.previous = previous;
	}
	
	public IterativeLimitedDepthFirstSearchElement getPrevious(){
		return previous;
	}
	
	IterativeLimitedDepthFirstSearchElement(Vertex vertex, int level, double weight){
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