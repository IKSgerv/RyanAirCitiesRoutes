package mx.ia.controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LogPrinter {
	private PrintWriter logWriter;
	private File file;
	private String log = "";
	public LogPrinter(){
		log = "";
		file = new File("log.txt");
		newLogWrite(false, "", false);
	}
	
	public LogPrinter(String fileName){
		log = "";
		file = new File(fileName);
		newLogWrite(false, "", false);
	}
	
	private void newLogWrite(boolean append, String str, boolean println){
		try {
			logWriter = new PrintWriter(new FileWriter(file, append));
			if(println){
				logWriter.println(str);
				
			}else{
				logWriter.print(str);
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			logWriter.close();
		}
	}
	
	public void print(String str){
		log += str;
		System.out.print(str);
//		newLogWrite(true, str, false);
	}
	
	public void println(String str){
		log += str + "\n";
		System.out.println(str);
//		newLogWrite(true, str, true);
	}
	
	public void save(){
		newLogWrite(true, log, true);
	}
}
