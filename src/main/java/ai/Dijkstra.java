package ai;

import java.util.HashMap;
import java.util.Map;

import graph.Edge;
import graph.Graph;
//import printerController.LogPrinter;
import ryanairController.RyanairAirportController;

public class Dijkstra {
	private Graph graphG;
	private Graph graphT = new Graph();
	private Map<String, DijkstraElement> dijkstraElements = new HashMap<String, DijkstraElement>();
//	private static LogPrinter log = new LogPrinter("src/main/resources/log_Dijkstra.txt");
	
	public Dijkstra(Graph g){
		System.out.println("Dijkstra Controller - Started");
		graphG = g;
		System.out.println("G: {\n" + g.toString() + "\n}\n"
				+ " Vertices: " + graphG.getV().size() + "\n"
				+ " Edges: " + graphG.getE().size());
	}
	
	public Graph resolve(String from, String to){
		NewString newFrom = new NewString();
		NewString newTo = new NewString();
		newFrom.str = from;
		newTo.str = to;
		System.out.println("Resolve: from " + newFrom + " to " + newTo);
		DijkstraElement element = null;
		
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
		
		for (RyanairAirportController s : graphG.getV())
			dijkstraElements.put(s.getIataCode(), new DijkstraElement(s));
		
		for (Edge e : graphG.getE())
			dijkstraElements.get(e.getFrom()).neighbours.put(e.getTo(), e.getDistance());
		dijkstraElements.get(from).setWeight(0.0, dijkstraElements.get(from));
		
		while(!dijkstraElements.get(to).isPermanent()){
			element = getMin();
			element.setPermanent();			
			for(Map.Entry<String, Double> neighbour : element.neighbours.entrySet())
				dijkstraElements.get(neighbour.getKey()).setWeight(neighbour.getValue() + element.getWeight(), element);
			System.out.println(dijkstraElements.toString());
		}
		
		System.out.println(element.printTrace());
		do{
			graphT.getV().add(element.airport);
			graphT.getE().add(new Edge(element.previos.airport.getIataCode(), element.airport.getIataCode(), element.getWeight() - element.previos.getWeight()));
			element = element.previos;
		}while(element.previos != element);
		graphT.getV().add(element.airport);
		
		System.out.println(graphT.toString());
		return graphT;
	}
	
	private DijkstraElement getMin(){
		DijkstraElement minDijkstraElement = new DijkstraElement(null);
		for (Map.Entry<String, DijkstraElement> dijkstraElement : dijkstraElements.entrySet())
			if(dijkstraElement.getValue().getWeight() < minDijkstraElement.getWeight() && !dijkstraElement.getValue().isPermanent())
				minDijkstraElement = dijkstraElement.getValue();
		return minDijkstraElement;
	}
}

//------------------------------------------------------------------------------------------------------------------------------------
class DijkstraElement{
	RyanairAirportController airport;
	private double weight = Double.POSITIVE_INFINITY;
	DijkstraElement previos = null;
	private boolean permanent = false;
	Map<String, Double> neighbours = new HashMap<String, Double>();
	
	DijkstraElement(RyanairAirportController airport){
		this.airport = airport;
	}
	
	void setWeight(double w, DijkstraElement prev){
		if(this.weight > w){
			this.weight = w;
			this.previos = prev;
		}
	}
	void setPermanent(){
		permanent = true;
	}
	
	boolean isPermanent(){
		return permanent;
	}
	
	double getWeight(){
		return weight;
	}
	
	public String toString(){
		String res = permanent ? "*" : "-";
		String previosName = previos != null ? previos.airport.getIataCode() : "-";
		return res + airport + "(" + weight + ", " + previosName + ")";
	}
	
	String printTrace(){
		String res = this != this.previos ? previos.printTrace() + " > " : "";
		return res + airport.getIataCode() + "(" + weight + ", " + previos.airport.getIataCode() + ")";
	}
}


class NewString{
	public String str;
	
	@Override
	public boolean equals(Object o){
		return o.equals(str);
	}
	
	public String toString(){
		return str;
	}
}