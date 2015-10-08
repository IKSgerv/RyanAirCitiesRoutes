package graph;

public class Edge implements Comparable<Edge>{
	private String fromVertexCode;
	private String toVertexCode;
	private double distance;
	
	public Edge(String from, String to, double distance){
		this.fromVertexCode = from;
		this.toVertexCode = to;
		this.distance = distance < 0 ? Double.POSITIVE_INFINITY : distance;
	}
	
	public double getDistance(){
		return distance;
	}
	
	public String getFrom(){
		return fromVertexCode;
	}
	
	public String getTo(){
		return toVertexCode;
	}
	
	public String toString(){
		return "(" + fromVertexCode + "," + toVertexCode + ":" + distance + ")";
	}
	
	public int compareTo(Edge o) {
		return Double.compare(distance, o.distance);
	}
}
