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
import ryanairController.RyanairAirportController;

public class PoiController{
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
	
	public void putHeaders(RyanairAirportController[] headers){
		Row rowIata = sheet.createRow(0);//for iataCodes
		Row rowName = sheet.createRow(1);//for names
		Row rowLati = sheet.createRow(2);//for latitude
		Row rowLong = sheet.createRow(3);//for longitude
		Cell cellIata;
		Cell cellName;
		Cell cellLati;
		Cell cellLong;
		
		for(int i = 0; i < headers.length; i++){
			cellIata = rowIata.createCell(i);
			cellName = rowName.createCell(i);
			cellLati = rowLati.createCell(i);
			cellLong = rowLong.createCell(i);
			
			cellIata.setCellValue(headers[i].getIataCode());
			cellName.setCellValue(headers[i].getName());
			cellLati.setCellValue(headers[i].getLatitude());
			cellLong.setCellValue(headers[i].getLongitude());
		}
	}
	
	public void putContent(double[][] content){
		double prom;
		for(int i = 0; i < content.length; i++){
			for(int j = 0; j < content[i].length; j++){
				prom = 0;
				if(content[i][j] > 0 && content[j][i] > 0){
					prom = (content[i][j] + content[j][i]) / 2;
				}else if(content[i][j] > 0 && content[j][i] <= 0){
					prom = content[i][j] / 2;
				}else if(content[j][i] > 0 && content[i][j] <= 0){
					prom = content[j][i] / 2;
				}				
				content[i][j] = content[j][i] = prom;
			}
		}
		
		Row row;
		Cell cell;
		
		for(int i = 0; i < content.length; i++){
			row = sheet.createRow(i + 4);
			for(int j = 0; j < content[i].length; j++){
				cell = row.createCell(j);
			    cell.setCellValue((Double) (Double.isInfinite(content[i][j]) ? -1 : content[i][j]));
			}
		}
	}
	
	public Graph readGraph(String file){
		Row row;
		Row rowName;//for names
		Row rowLati;//for latitude
		Row rowLong;//for longitude
	    
		Cell cell;
	    Cell cellName;
		Cell cellLati;
		Cell cellLong;
		
	    Sheet sheet = null;
	    Vector<RyanairAirportController> vectorV = new Vector<RyanairAirportController>();
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
	    row = sheet.getRow(0);//for iataCodes
		rowName = sheet.getRow(1);//for names
		rowLati = sheet.getRow(2);//for latitude
		rowLong = sheet.getRow(3);//for longitude
		
	    for (int i = 0; row.getCell(i) != null; i++ ){
	    	cell = row.getCell(i);
	    	cellName = rowName.getCell(i);
			cellLati = rowLati.getCell(i);
			cellLong = rowLong.getCell(i);
	    	vectorV.add(new RyanairAirportController(cell.getStringCellValue(), cellName.getStringCellValue(), cellLati.getNumericCellValue(), cellLong.getNumericCellValue()));
	    }
	    
	    for(int i = 4; sheet.getRow(i) != null; i++){
		    row = sheet.getRow(i);
		    for (int j = 0; row.getCell(j) != null; j++ ){
		    	cell = row.getCell(j);
		    	if (row.getCell(j).getNumericCellValue() >= 0)
		    		vectorE.add(new Edge(vectorV.get(i - 4).getIataCode(), vectorV.get(j).getIataCode(), cell.getNumericCellValue()));
		    }
	    }
	    System.out.println("Reading graph - finished");
	    
		return solveMissing(new Graph(vectorV, vectorE));
	}
	
	Graph solveMissing(Graph graphG){
		boolean found;
		for(int i = 0; i < graphG.getV().size(); i++){
			found = false;
			for(int j = 0; j < graphG.getE().size(); j++){
				if(graphG.getE().get(j).getFrom().equals(graphG.getV().get(i).getIataCode()) || graphG.getE().get(j).getTo().equals(graphG.getV().get(i).getIataCode())){
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
}
