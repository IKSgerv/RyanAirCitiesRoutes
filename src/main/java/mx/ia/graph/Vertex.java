package mx.ia.graph;

public class Vertex implements Comparable<Vertex> {
	private String code;
	private String name;
	private double positionY;
	private double positionX;
	private double danger = 0;//value from 0 to 1
	private boolean blocked = false;
	private String[] adjacentNodes;//used just for the pio controller - have to change that
	
	public Vertex(String code, String name, Double positionX, Double positionY){
		this.code = code;
		this.name = name;
		this.positionY = positionX;
		this.positionX = positionY;
		adjacentNodes = new String[0];
	}
	
	public Vertex(String code, String name, Double positionX, Double positionY, String[] adjacentNode){
		this.code = code;
		this.name = name;
		this.positionX = positionX;
		this.positionY = positionY;
		this.adjacentNodes = adjacentNode;
	}
	
	public void setAdjacentNodes(String[] adjacentNodes){
		this.adjacentNodes = adjacentNodes;
	}
	
	public String toString(){
		return code + "(" + positionX + "," + positionY + ":" + danger + ")";
	}

	public String getName() {
		return name;
	}
	
	public String getCode() {
		return code;
	}
	
	public double getPositionY(){
		return positionY;
	}
	
	public double getPositionX(){
		return positionX;
	}
	
	public String[] getAdjacentNodes(){
		return adjacentNodes;
	}
	
	public int compareTo(Vertex o) {
		return code.compareTo(o.code);
	}
	
	public boolean equals(Object o){
		if(o instanceof String)
			return this.code.equals((String) o);
		else if(o instanceof Vertex)
			return this.equals(((Vertex) o).getCode());
		return false;
	  }

	public double getDanger() {
		return danger;
	}

	public void setDanger(double danger) {
		this.danger = danger;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
}
