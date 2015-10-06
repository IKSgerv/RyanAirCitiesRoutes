package graph;

public class Edge implements Comparable<Edge>{
	private String fromIatacode;
	private String toIatacode;
	private double distance;
	
	public Edge(String from, String to, double distance){
		this.fromIatacode = from;
		this.toIatacode = to;
		this.distance = distance < 0 ? Double.POSITIVE_INFINITY : distance;
	}
	
	public double getDistance(){
		return distance;
	}
	
	public String getFrom(){
		return fromIatacode;
	}
	
	public String getTo(){
		return toIatacode;
	}
	
	public String toString(){
		return "(" + fromIatacode + "," + toIatacode + ":" + distance + ")";
	}
	
	public int compareTo(Edge o) {
		return Double.compare(distance, o.distance);
	}
}
