package mx.ia.controllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import mx.ia.graph.Edge;
import mx.ia.graph.Graph;
import mx.ia.graph.Vertex;

public class MatrixFileController {
	Vector<String> rules = new Vector<String>();
	public Graph readGraph(String file){
		rules.clear();
		Graph resGraph = null;
		int width = 0;
		String str = "";
		String[] strMatr = null;
		Vector<String[]> vStr = new Vector<String[]>();
		int[][] intMat;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		try {
			while((str = bufferedReader.readLine()) != null){
				strMatr = str.split(",");
				vStr.add(strMatr);
				if(strMatr.length > width && !strMatr[0].equals("*")){
					System.out.println("Width: " + width);
					width = strMatr.length;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(strMatr[0].equals("*")){
			intMat = new int[vStr.size() - 1][width];
		}else{
			System.out.println("Not Ruled: " + (vStr.size()));
			intMat = new int[vStr.size()][width];
		}
		
		for (int i = 0; i < vStr.size(); i++){
			for (int j = 0; j < vStr.elementAt(i).length; j++){
				if (!vStr.elementAt(i)[0].equals("*")){
					intMat[i][j] = Integer.parseInt(vStr.elementAt(i)[j]);
					
				}else if(j != 0){
					rules.add(vStr.elementAt(i)[j]);
				}
			}
		}
		System.out.println("Ruled: " + rules.size() + " " + rules);
		
		resGraph = new Graph();
		for(int f = 0; f < intMat.length; f++){
			for(int c = 0; c < intMat[f].length; c++){
				Vertex v = new Vertex("(" + c + "," + f + ")", "(" + c + "," + f + ")", (double)(intMat.length - f), (double)c);
				v.setDanger(intMat[f][c] == 2 ? 1 : 0);
				v.setBlocked(intMat[f][c] == 0);
				resGraph.getV().add(v);
			}
		}
		for(int f = 0; f < intMat.length; f++){
			for(int c = 0; c < intMat[f].length; c++){
				if((intMat[f][c] >= 1) && c < intMat[f].length - 1){
					if(intMat[f][c + 1] >= 1){
						if(rules.contains("0"))
							resGraph.getE().add(new Edge("(" + c + "," + f + ")", "(" + (c + 1) + "," + f + ")", (double)1, "0"));
						if(rules.contains("180"))
							resGraph.getE().add(new Edge("(" + (c + 1) + "," + f + ")", "(" + c + "," + f + ")", (double)1, "180"));
					}
				}
				if((intMat[f][c] >= 1) && f < intMat.length - 1 ){
					if(intMat[f + 1][c] >= 1){
						if(rules.contains("270"))
							resGraph.getE().add(new Edge("(" + c + "," + f + ")", "(" + c + "," + (f + 1) + ")", (double)1, "270"));
						if(rules.contains("90"))
							resGraph.getE().add(new Edge("(" + c + "," + (f + 1) + ")", "(" + c + "," + f + ")", (double)1, "90"));
					}
				}
				//-----------------------------------------------------------------------------------------------------------------------
				if((intMat[f][c] >= 1) && c < intMat[f].length - 1 && f < intMat.length - 1){
					if(intMat[f + 1][c + 1] >= 1){
						if(rules.contains("315"))
							resGraph.getE().add(new Edge("(" + c + "," + f + ")", "(" + (c + 1) + "," + (f + 1) + ")", (double)Math.sqrt(2), "315"));
						if(rules.contains("135"))
							resGraph.getE().add(new Edge("(" + (c + 1) + "," + (f + 1) + ")", "(" + c + "," + f + ")", (double)Math.sqrt(2), "135"));
					}
				}
				if((intMat[f][c] >= 1) && c > 0 && f < intMat.length - 1){
					if(intMat[f + 1][c - 1] >= 1){
						if(rules.contains("225"))
							resGraph.getE().add(new Edge("(" + c + "," + f + ")", "(" + (c - 1) + "," + (f + 1) + ")", (double)Math.sqrt(2), "225"));
						if(rules.contains("45"))
							resGraph.getE().add(new Edge("(" + (c - 1) + "," + (f + 1) + ")", "(" + c + "," + f + ")", (double)Math.sqrt(2), "45"));
					}
				}
			}
		}
		
		for(Vertex v: resGraph.getV()){
			if(v.getDanger() == 1){
				for(Edge e: resGraph.getE()){
					if(e.getFrom().equals(v.getCode())){
						Vertex temp = null;
						for(Vertex v2: resGraph.getV()){
							if(v2.getCode().equals(e.getTo()))
								temp = v2;
						}
						if(temp != null && temp.getDanger() == 0)
							temp.setDanger(0.66);
					}
				}
			}
		}
		for(Vertex v: resGraph.getV()){
			if(v.getDanger() == 0.66){
				for(Edge e: resGraph.getE()){
					if(e.getFrom().equals(v.getCode())){
						Vertex temp = null;
						for(Vertex v2: resGraph.getV()){
							if(v2.getCode().equals(e.getTo()))
								temp = v2;
						}
						if(temp != null && temp.getDanger() == 0)
							temp.setDanger(0.33);
					}
				}
			}
		}
		
		System.out.println(resGraph);
		return resGraph;
	}
	
	public Vector<String> getRules(){
		return rules;
	}
}
