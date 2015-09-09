package ai;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import graph.Edge;
import graph.Graph;
import printerController.LogPrinter;

public class Dijkstra {
	private Vector<Edge> edges;
	private Vector<String> vertices;
	private Map<String, DijkstraElement> dijkstraElements = new HashMap<String, DijkstraElement>();
	private static LogPrinter log = new LogPrinter("src/main/resources/log_Dijkstra.txt");
	
	public Dijkstra(Graph g){
		log.println("Dijkstra Controller - Started");
		edges = g.getVectorE();
		vertices = g.getVectorV();
		log.println("G: {\n" + g.toString() + "\n}\n"
				+ " Vertices: " + edges.size() + "\n"
				+ " Edges: " + vertices.size());
	}
	
	public void resolve(String from, String to){
		log.println("Resolve: from " + from + " to" + to);
		DijkstraElement element = null;
		if (!vertices.contains(from))throw new IllegalArgumentException("Can not find the vertex: " + from);
		if (!vertices.contains(to))throw new IllegalArgumentException("Can not find the vertex: " + to);
		
		for (String s : vertices)
			dijkstraElements.put(s, new DijkstraElement(s));
		
		for (Edge e : edges)
			dijkstraElements.get(e.getFrom()).neighbours.put(e.getTo(), e.getDistance());
		dijkstraElements.get(from).setWeight(0.0, dijkstraElements.get(from));
		
		while(!dijkstraElements.get(to).isPermanent()){
			element = getMin();
			element.setPermanent();			
			for(Map.Entry<String, Double> neighbour : element.neighbours.entrySet())
				dijkstraElements.get(neighbour.getKey()).setWeight(neighbour.getValue() + element.getWeight(), element);
			log.println(dijkstraElements.toString());
		}
		log.println(element.printTrace());
	}
	
	private DijkstraElement getMin(){
		DijkstraElement minDijkstraElement = new DijkstraElement("");
		for (Map.Entry<String, DijkstraElement> dijkstraElement : dijkstraElements.entrySet())
			if(dijkstraElement.getValue().getWeight() < minDijkstraElement.getWeight() && !dijkstraElement.getValue().isPermanent())
				minDijkstraElement = dijkstraElement.getValue();
		return minDijkstraElement;
	}
}
