package mx.ia.algorithms;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import mx.ia.graph.Edge;
import mx.ia.graph.Graph;
import mx.ia.graph.Vertex;

public class DepthFirstSearch {
	private Graph graphG;
	private Graph graphT = new Graph();
	private Map<String, Vertex> vElements = new HashMap<String, Vertex>();
	private Vector<String> rules = null;// {"270","0","90","180"};
	private Vector<DepthFirstSearchElement> dfsOpened = new Vector<DepthFirstSearchElement>();
	private Vector<DepthFirstSearchElement> dfsClosed = new Vector<DepthFirstSearchElement>();

	public DepthFirstSearch(Graph g) {
		System.out.println("DepthFirstSearch Controller - Started");
		graphG = g;
		System.out.println("G: {\n" + g.toString() + "\n}\n" + " Vertices: " + graphG.getV().size() + "\n" + " Edges: "
				+ graphG.getE().size());
	}

	public void setRules(Vector<String> rul) {
		this.rules = rul;
	}

	@SuppressWarnings("unlikely-arg-type")
	public Graph resolve(String from, String to) {
		boolean fail = true;
		NewString newFrom = new NewString(), newTo = new NewString();
		DepthFirstSearchElement element = null, elementToAdd;
		newFrom.str = from;
		newTo.str = to;
		System.out.println("Resolve: from " + newFrom + " to " + newTo);

		if (!graphG.getV().contains(newFrom)) {
			System.err.println("Can not find the vertex: " + newFrom);
			return null;
		}
		if (!graphG.getV().contains(newTo)) {
			System.err.println("Can not find the vertex: " + newTo);
			return null;
		}

		for (Vertex s : graphG.getV())
			vElements.put(s.getCode(), s);

		System.out.println(vElements.toString());

		// Pseudocodigo
		// 1- Agregar el vertice de inicio a abiertos y lo seleccionamos como vertice
		// actual
		// 2- Setear el vertice previo del vertice actual a el mismo
		// 3- Mientras abiertos no esté vacio
		// 3.1- Si el vertice actual es el vertice de busqueda terminamos sino
		// continuamos
		// 3.2- Por cada vertice vecino del vertice actual que no se encuentre en
		// cerrados
		// 3.2.1- Agregar el vertice vecino a abiertos
		// 3.2.2- Setear el vertice previo del vertice vecino a el vertice actual
		// 3.3- Quitar el vertice actual de abiertos y agregarlo a cerrados
		// 3.4- Tomar el ultimo vertice de abiertos como vertice actual

		elementToAdd = new DepthFirstSearchElement(vElements.get(from), 1, 0.0);
		elementToAdd.setPrevious(elementToAdd);
		putNeighbors(elementToAdd);

		dfsOpened.add(elementToAdd);

		while (!dfsOpened.isEmpty()) {

			element = dfsOpened.lastElement();
			System.out.println("Level: " + element.getLevel());

			if (element.equalsStr(to)) {
				// Found and break the while
				fail = false;
				break;
			}

			if (rules != null) {
				/*
				 * If rules where set, only create and add to the opened nodes the neighbors
				 * that accomplish this rules
				 */

				dfsOpened.addAll(ruledNeighbors(rules, element));
			} else {
				/*
				 * Otherwise create and add all the not closed neighbor nodes to the opened
				 * vector
				 */

				for (String neighborCode : element.getNeighbors().keySet()) {
					if (!contains(dfsClosed, neighborCode)) {
						/*
						 * If the neighbor isn't closed (marked) we can continue
						 */
						elementToAdd = new DepthFirstSearchElement(vElements.get(neighborCode), element.getLevel() + 1,
								element.getNeighbors().get(neighborCode) + element.getWeight());
						elementToAdd.setPrevious(element);
						putNeighbors(elementToAdd);

						/*
						 * Add the element to the opened nodes vector
						 */
						dfsOpened.add(elementToAdd);
					}
				}
			}

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
		do {
			graphT.getV().add(element.getVertex());
			graphT.getE().add(new Edge(element.getPrevious().getVertex().getCode(), element.getVertex().getCode(),
					element.getWeight() - element.getPrevious().getWeight()));
			element = element.getPrevious();
		} while (!element.getPrevious().equalsStr(element.getVertex().getCode()));
		graphT.getV().add(element.getVertex());

		System.out.println(graphT.toString());
//		System.out.save();
		return graphT;
	}

	/**
	 * Use the graphG to fill its neighbors
	 * 
	 * @param elementToAdd the element for which we have to find its neighbors and
	 *                     pass them to it
	 */
	void putNeighbors(DepthFirstSearchElement elementToAdd) {
		Map<String, Double> neighbors = new HashMap<String, Double>();
		for (Edge e : graphG.getE())
			if (elementToAdd.equalsStr(e.getFrom()))
				neighbors.put(e.getTo(), e.getDistance());
		elementToAdd.getNeighbors().putAll(neighbors);
	}

	/**
	 * TODO
	 * 
	 * @param rules
	 * @param dfsElement
	 * @return
	 */
	private Vector<DepthFirstSearchElement> ruledNeighbors(Vector<String> rules, DepthFirstSearchElement dfsElement) {

		Vector<DepthFirstSearchElement> res = new Vector<DepthFirstSearchElement>();
		System.out.println("All neighbors: " + dfsElement.getNeighbors().size());
		System.out.print("Ruled neighbors: [");
		String strRule = "";
		DepthFirstSearchElement elementToAdd;
		for (int i = rules.size() - 1; i >= 0; i--) {
			strRule = rules.elementAt(i);
			for (String neighborCode : dfsElement.getNeighbors().keySet()) {
				for (Edge edge : graphG.getE()) {
					if (dfsElement.equalsStr(edge.getFrom()) && neighborCode.equals(edge.getTo())
							&& strRule.equals(edge.getOrderCode())) {
						if (!contains(dfsClosed, neighborCode)) {
							elementToAdd = new DepthFirstSearchElement(vElements.get(neighborCode),
									dfsElement.getLevel() + 1,
									dfsElement.getNeighbors().get(neighborCode) + dfsElement.getWeight());
							elementToAdd.setPrevious(dfsElement);
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

	/**
	 * Search in a vector v, if it contains a node with the code str
	 * 
	 * @param v   vector to use for search
	 * @param str code to search for in v
	 * @return boolean whether the node had been found or not
	 */
	private boolean contains(Vector<DepthFirstSearchElement> v, String str) {
		for (DepthFirstSearchElement depthFirstSearchElement : v)
			if (depthFirstSearchElement.equalsStr(str))
				return true;
		return false;
	}

}

class DepthFirstSearchElement {
	private Vertex vertex;
	private Map<String, Double> neighbors = new HashMap<String, Double>();
	private DepthFirstSearchElement previous = null;
	private int level;
	private double weight;

	public Vertex getVertex() {
		return vertex;
	}

	public Map<String, Double> getNeighbors() {
		return neighbors;
	}

	public int getLevel() {
		return level;
	}

	public void setPrevious(DepthFirstSearchElement previous) {
		this.previous = previous;
	}

	public DepthFirstSearchElement getPrevious() {
		return previous;
	}

	/**
	 * DepthFirstSearchElement constructor
	 * 
	 * @param vertex Vertex for which this object is created
	 * @param level  The level from the initial vertex to here
	 * @param weight The weight of the edge from the parent element to this element
	 */
	DepthFirstSearchElement(Vertex vertex, int level, double weight) {
		this.vertex = vertex;
		this.level = level;
		this.weight = weight;
	}

	void setWeight(double w) {
		this.weight = w;
	}

	double getWeight() {
		return weight;
	}

	void setNeighbors(Map<String, Double> neighbors) {
		this.neighbors = neighbors;
	}

	public String toString() {
		String res = "";
		String previosName = previous != null ? previous.vertex.getCode() : "-";
		return res + vertex.getCode() + "[" + previosName + "," + level + "]";
	}

	String printTrace() {
		String res = this != this.previous ? previous.printTrace() + " > " : "";
		return res + this.toString();
	}

	public boolean equalsStr(String str) {
		return this.vertex.getCode().equals(str);
	}
}