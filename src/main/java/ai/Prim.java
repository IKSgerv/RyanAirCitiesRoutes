package ai;

import java.util.Collections;
import java.util.Random;

import graph.Edge;
import graph.Graph;
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
				+ " Vertices: " + g.getVectorV().size() + "\n"
				+ " Edges: " + g.getVectorE().size());
	}
	
	public void resolve(){
		Edge newEdge;
		Random rand = new Random();
		int random = rand.nextInt(graphG.getVectorV().size() - 1);
		String v = graphG.getVectorV().get(random);
		log.println("Resolve: " + v);
		graphT.getVectorV().add(v);
		while(!graphT.getVectorV().containsAll(graphG.getVectorV())){
			if(graphT.getVectorV().size() >= graphG.getVectorV().size())
				throw new OutOfMemoryError("It is infinite iterating: " + showMissing() + "\n" + graphT.getVectorV().containsAll(graphG.getVectorV()));
			newEdge = getMin();
			weight += newEdge.getDistance();
			graphT.getVectorE().add(newEdge);
			graphT.getVectorV().add(!graphT.getVectorV().contains(newEdge.getFrom())? newEdge.getFrom(): newEdge.getTo());
			Collections.sort(graphT.getVectorV());
			Collections.sort(graphG.getVectorV());
			log.println("(" + graphT.getVectorV().size() + "/" + graphG.getVectorV().size() + ")\n" + graphT.getVectorV().toString() + "\n" + graphG.getVectorV().toString());
			
		}
		log.println("T: {\n" + graphT.toString() + "\n}\n Weight: " + weight);
	}
	
	String showMissing(){
		for(String s : graphG.getVectorV()){
			if(!graphT.getVectorV().contains(s))
				return s;
		}
		return "";
	}
	
	Edge getMin(){
		//Edge res;
		double min = Double.POSITIVE_INFINITY;
		int index = 0;
		String from, to;
		for(int i = 0; i < graphG.getVectorE().size(); i++){
			from = graphG.getVectorE().get(i).getFrom();
			to = graphG.getVectorE().get(i).getTo();
			if((graphT.getVectorV().contains(from) && !graphT.getVectorV().contains(to)) || (graphT.getVectorV().contains(to) && !graphT.getVectorV().contains(from))){
				if(graphG.getVectorE().get(i).getDistance() < min){
					min = graphG.getVectorE().get(i).getDistance();
					index = i;
				}
			}
		}
		return graphG.getVectorE().get(index);
	}
}
