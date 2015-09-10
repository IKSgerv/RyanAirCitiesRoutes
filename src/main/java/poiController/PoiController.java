package poiController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import graph.Edge;
import graph.Graph;

public class PoiController {
	private Workbook wb = null;
    private FileOutputStream fileOut = null;
    private Sheet sheet = null;
	
    public void writeTo(String workbook, String sheet){
		try {
			fileOut = new FileOutputStream(workbook);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wb = new HSSFWorkbook();
		this.sheet = wb.createSheet(sheet);
	}
	
	public void save() throws IOException{
	    wb.write(fileOut);
	    fileOut.close();
	    wb = null;
	    fileOut = null;
	    sheet = null;
	}
	
	public void putHeaders(String[] headers){
		Row row = sheet.createRow(0);
		Cell cell;
		for(int i = 0; i < headers.length; i++){
			cell = row.createCell(i);
		    cell.setCellValue(headers[i]);
		}
	}
	
	public void putContent(double[][] content){
		double prom;
		
		for(int i = 0; i < content.length; i++){
			for(int j = 0; j < content[i].length; j++){
				prom = (content[i][j] + content[j][i]) / 2;
				content[i][j] = content[j][i] = prom;
			}
		}
		
		Row row;
		Cell cell;
		
		for(int i = 0; i < content.length; i++){
			row = sheet.createRow(i + 1);
			for(int j = 0; j < content[i].length; j++){
				cell = row.createCell(j);
			    cell.setCellValue((Double) (Double.isInfinite(content[i][j])? -1 : content[i][j]));
			}
		}
	}
	
	public Graph readGraph(String file){
		Row row;
	    Cell cell;
	    Sheet sheet = null;
	    Vector<String> vectorV = new Vector<String>();
		Vector<Edge> vectorE = new Vector<Edge>();
		System.out.println("Reading graph - started");
		
		try {
			wb = new HSSFWorkbook(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
	    row = sheet.getRow(0);
	    
	    for (int i = 0; row.getCell(i) != null; i++ ){
	    	cell = row.getCell(i);
	    	vectorV.add(cell.getStringCellValue());
	    }
	    
	    for(int i = 1; sheet.getRow(i) != null; i++){
		    row = sheet.getRow(i);
		    for (int j = 0; row.getCell(j) != null; j++ ){
		    	cell = row.getCell(j);
		    	if (row.getCell(j).getNumericCellValue() >= 0)
		    		vectorE.add(new Edge(vectorV.get(i - 1),vectorV.get(j),cell.getNumericCellValue()));
		    }
	    }
	    System.out.println("Reading graph - finished");
	    
		return solveMissing(new Graph(vectorV, vectorE));
	}
	
	Graph solveMissing(Graph graphG){
		boolean found;
		for(int i = 0; i < graphG.getVectorV().size(); i++){
			found = false;
			for(int j = 0; j < graphG.getVectorE().size(); j++){
				if(graphG.getVectorE().get(j).getFrom().equals(graphG.getVectorV().get(i)) || graphG.getVectorE().get(j).getTo().equals(graphG.getVectorV().get(i))){
					found = true;
				}
			}
			if(!found){
				System.out.println("Deleted: " + graphG.getVectorV().remove(i));
				i--;
			}
		}
		return graphG;
	}
}
