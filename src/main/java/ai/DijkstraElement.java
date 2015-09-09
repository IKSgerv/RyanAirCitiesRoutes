package ai;

import java.util.HashMap;
import java.util.Map;

class DijkstraElement {
	private String name;
	private double weight = Double.POSITIVE_INFINITY;
	private DijkstraElement previos = null;
	private boolean permanent = false;
	Map<String, Double> neighbours = new HashMap<String, Double>();
	
	DijkstraElement(String name){
		this.name = name;
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
		String previosName = previos != null ? previos.name : "-";
		return res + name + "(" + weight + ", " + previosName + ")";
	}
	
	String printTrace(){
		String res = this != this.previos? this.previos.printTrace() + " > " : "";
		return res + name + "(" + weight + ", " + previos.name + ")";
	}
}
