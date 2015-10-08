package graph;

public class Vertex implements Comparable<Vertex> {
	private String code;
	private String name;
	private double positionY;
	private double positionX;
	private String[] adjacentNodes;
	
	public Vertex(String code, String name, Double positionX, Double positionY){
		this.code = code;
		this.name = name;
		this.positionY = positionX;
		this.positionX = positionY;
		adjacentNodes = null;
	}
	
	public Vertex(String code, String name, Double positionX, Double positionY, String[] adjacentNode){
		this.code = code;
		this.name = name;
		this.positionX = positionX;
		this.positionY = positionY;
		this.adjacentNodes = adjacentNode;
	}
	
	public String toString(){
		return code + " name: " + name + "(" + positionX + "," + positionY + ")";
	}

	public String getName() {
		return name;
	}
	
	public String getIataCode() {
		return code;
	}
	
	public double getLatitude(){
		return positionY;
	}
	
	public double getLongitude(){
		return positionX;
	}
	
	public String[] getDestinationsIataCodes(){
		return adjacentNodes;
	}
	
	public int compareTo(Vertex o) {
		return code.compareTo(o.code);
	}
	
	public boolean equals(Object o){
		if(o instanceof String)
			return this.code.equals((String) o);
		else if(o instanceof Vertex)
			return this.equals(((Vertex) o).getIataCode());
		return false;
	  }
}
