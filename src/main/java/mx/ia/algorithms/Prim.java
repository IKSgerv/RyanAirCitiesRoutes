package mx.ia.algorithms;

import java.util.Collections;
import java.util.Random;

import mx.ia.graph.Edge;
import mx.ia.graph.Graph;
import mx.ia.graph.Vertex;

public class Prim {
	private Graph graphG;
	private Graph graphT = new Graph();
	private double weight;
//	private static logPrinter log = new logPrinter("src/main/resources/System.out_Prim.txt");
	public Prim(Graph g){
		System.out.println("Prim Controller - Started");
		graphG = g;
		weight = 0;
		System.out.println("G: {\n" + g.toString() + "\n}\n"
				+ " Vertices: " + g.getV().size() + "\n"
				+ " Edges: " + g.getE().size());
	}
	
	private Graph quitLonely(Graph graphG){
		boolean found;
		for(int i = 0; i < graphG.getV().size(); i++){
			found = false;
			for(int j = 0; j < graphG.getE().size(); j++){
				if(graphG.getE().get(j).getFrom().equals(graphG.getV().get(i).getCode()) || graphG.getE().get(j).getTo().equals(graphG.getV().get(i).getCode())){
					found = true;
				}
			}
			if(!found){
				System.out.println("Deleted: " + graphG.getV().remove(i));
				i--;
			}
		}
		return graphG;
	}
	
	public Graph resolve(){
		boolean fail = false;
		NewString newFrom = new NewString();
		NewString newTo = new NewString();
		graphG = quitLonely(graphG);
		Edge newEdge;
		Random rand = new Random();
		int random = rand.nextInt(graphG.getV().size() - 1);
		Vertex v = graphG.getV().get(random);
		
		System.out.println("Resolve: " + v);
		graphT.getV().add(v);
		while( !graphT.getV().containsAll( graphG.getV() )){
			
			if(graphT.getV().size() >= graphG.getV().size()){
//				throw new OutOfMemoryError("It is infinite iterating: " + showMissing() + "\n" + graphT.getV().containsAll(graphG.getV()));
				fail = true;				
				break;
			}
			
			newEdge = getMin();
			
			newFrom.str = newEdge.getFrom();
			newTo.str = newEdge.getTo();
			weight += newEdge.getDistance();
			graphT.getE().add(newEdge);
			
			graphG.getV().get( graphG.getV().indexOf( newFrom ) );
			
			if(!graphT.getV().contains(graphG.getV().get(graphG.getV().indexOf(newFrom)))){
				graphT.getV().add(graphG.getV().get(graphG.getV().indexOf(newFrom)));
			}else{
				graphT.getV().add(graphG.getV().get(graphG.getV().indexOf(newTo)));
			}
			
			Collections.sort(graphT.getV());
			Collections.sort(graphG.getV());
			System.out.println("(" + graphT.getV().size() + "/" + graphG.getV().size() + ")\n" + graphT.getV().toString() + "\n" + graphG.getV().toString());
			
		}
		if (fail) {
			System.out.println("Failed to resolve");
			return null;
		}
		System.out.println("T: {\n" + graphT.toString() + "\n}\n Weight: " + weight);
//		System.out.save();
		return graphT;
	}
	
	Vertex showMissing(){
		for(Vertex s : graphG.getV()){
			if(!graphT.getV().contains(s))
				return s;
		}
		return null;
	}
	
	Edge getMin(){
		//Edge res;
		double min = Double.POSITIVE_INFINITY;
		int index = 0;
		NewString from = new NewString();
		NewString to = new NewString();
		
		for(int i = 0; i < graphG.getE().size(); i++){
			from.str = graphG.getE().get(i).getFrom();
			to.str = graphG.getE().get(i).getTo();
			if((graphT.getV().contains(from) && !graphT.getV().contains(to)) || (graphT.getV().contains(to) && !graphT.getV().contains(from))){
				if(graphG.getE().get(i).getDistance() < min){
					min = graphG.getE().get(i).getDistance();
					index = i;
				}
			}
		}
		return graphG.getE().get(index);
	}
}