package matrixFileController;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

public class MatrixFileController {
	public Graph readGraph(String file){
		Graph resGraph = null;
		String str = "";
		String[] strMatr = null;
		Vector<String[]> vStr = new Vector<String[]>();
		int[][] intMat;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		try {
			while((str = bufferedReader.readLine()) != null){
				strMatr = str.split(",");
				vStr.add(strMatr);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		intMat = new int[vStr.size()][strMatr.length];
		for (int i = 0; i < vStr.size(); i++){
			for (int j = 0; j < vStr.elementAt(i).length; j++){
				intMat[i][j] = Integer.parseInt(vStr.elementAt(i)[j]);
			}			
		}
		resGraph = new Graph();
		for(int f = 0; f < intMat.length; f++){
			for(int c = 0; c < intMat[f].length; c++){
				resGraph.getV().add(new Vertex("(" + c + "," + f + ")", "(" + c + "," + f + ")", (double)(intMat.length - f), (double)c));
			}
		}
		for(int f = 0; f < intMat.length; f++){
			for(int c = 0; c < intMat[f].length; c++){
				if(intMat[f][c] == 1 && c < intMat[f].length - 1){
					if(intMat[f][c + 1] == 1){
						resGraph.getE().add(new Edge("(" + c + "," + f + ")", "(" + (c + 1) + "," + f + ")", (double)1, "0"));
						resGraph.getE().add(new Edge("(" + (c + 1) + "," + f + ")", "(" + c + "," + f + ")", (double)1, "180"));
					}
				}
				if(intMat[f][c] == 1 && f < intMat.length - 1 ){
					if(intMat[f + 1][c] == 1){
						resGraph.getE().add(new Edge("(" + c + "," + f + ")", "(" + c + "," + (f + 1) + ")", (double)1, "270"));
						resGraph.getE().add(new Edge("(" + c + "," + (f + 1) + ")", "(" + c + "," + f + ")", (double)1, "90"));
					}
				}
			}
		}
		
		System.out.println(resGraph);
		return resGraph;
	}
}
