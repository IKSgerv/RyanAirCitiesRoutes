package mx.ia.graph;

public class Edge implements Comparable<Edge>{
	private String fromVertexCode;
	private String toVertexCode;
	private double distance;
	private String orderCode; // needed for BFS and DFS
	
	public Edge(String from, String to, double distance){
		this.fromVertexCode = from;
		this.toVertexCode = to;
		this.distance = distance < 0 ? Double.POSITIVE_INFINITY : distance;
	}
	
	public Edge(String from, String to, double distance, String orderCode){
		this.fromVertexCode = from;
		this.toVertexCode = to;
		this.distance = distance < 0 ? Double.POSITIVE_INFINITY : distance;
		this.orderCode = orderCode;
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

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
}
