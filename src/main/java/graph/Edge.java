package graph;

public class Edge implements Comparable<Edge>{
	private String from;
	private String to;
	private double distance;
	
	public Edge(String from, String to, double distance){
		this.from = from;
		this.to = to;
		this.distance = distance < 0? Double.POSITIVE_INFINITY : distance;
	}
	
	public double getDistance(){
		return distance;
	}
	
	public String getFrom(){
		return from;
	}
	
	public String getTo(){
		return to;
	}
	
	public String toString(){
		return "(" + from + "," + to + ":" + distance + ")";
	}
	
	public int compareTo(Edge o) {
		return Double.compare(distance, o.distance);
	}
}
