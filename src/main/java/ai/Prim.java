package ai;

import java.util.Collections;
import java.util.Random;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import printerController.LogPrinter;

public class Prim {
	private Graph graphG;
	private Graph graphT = new Graph();
	private double weight;
	private static LogPrinter log = new LogPrinter("src/main/resources/log_Prim.txt");
	public Prim(Graph g){
		log.println("Prim Controller - Started");
		graphG = g;
		weight = 0;
		log.println("G: {\n" + g.toString() + "\n}\n"
				+ " Vertices: " + g.getV().size() + "\n"
				+ " Edges: " + g.getE().size());
	}
	
	public Graph resolve(){
		NewString newFrom = new NewString();
		NewString newTo = new NewString();
		
		Edge newEdge;
		Random rand = new Random();
		int random = rand.nextInt(graphG.getV().size() - 1);
		Vertex v = graphG.getV().get(random);
		
		log.println("Resolve: " + v);
		graphT.getV().add(v);
		while( !graphT.getV().containsAll( graphG.getV() )){
			
			if(graphT.getV().size() >= graphG.getV().size())
				throw new OutOfMemoryError("It is infinite iterating: " + showMissing() + "\n" + graphT.getV().containsAll(graphG.getV()));
			
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
			log.println("(" + graphT.getV().size() + "/" + graphG.getV().size() + ")\n" + graphT.getV().toString() + "\n" + graphG.getV().toString());
			
		}
		log.println("T: {\n" + graphT.toString() + "\n}\n Weight: " + weight);
		log.save();
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
	
	void trans(){
		
	}
}