package jp.co.utdominion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GaUtils {
	public static double[][][] createMatFromFile(String pass){
		String result = "";
		try{
			  File file = new File(pass);
			  FileReader filereader = new FileReader(file);

			  int ch = filereader.read();
			  while(ch != -1){
			    result = result + (char)ch;

			    ch = filereader.read();
			  }
			  
			  filereader.close();
			}catch(FileNotFoundException e){
			  System.out.println(e);
			}catch(IOException e){
			  System.out.println(e);
			}finally{
				
			}
		
		int n,j;
		
		double[][][] mat1 = new double[Params.CARD_MAX_NUM][1][Params.CARD_MAX_NUM];
		String[] col = new String[Params.CARD_MAX_NUM];
		String[] val = new String[Params.CARD_MAX_NUM];
		col = result.split("\n");
		
		for (n = 0; n < Params.CARD_MAX_NUM; n++) {
			val = col[n].split(" ");
			for (j = 0; j < Params.CARD_MAX_NUM; j++) {
				mat1[n][0][j] = Double.valueOf(val[j]);
			}
		}
		return mat1;
	}
	
	public static double[][][] createMatFromFileSmall(String pass){
		String result = "";
		try{
			  File file = new File(pass);
			  FileReader filereader = new FileReader(file);

			  int ch = filereader.read();
			  while(ch != -1){
			    result = result + (char)ch;

			    ch = filereader.read();
			  }
			  
			  filereader.close();
			}catch(FileNotFoundException e){
			  System.out.println(e);
			}catch(IOException e){
			  System.out.println(e);
			}finally{
				
			}
		
		int j;
		
		double[][][] mat1 = new double[Params.CARD_MAX_NUM][1][1];
		String[] val = new String[Params.CARD_MAX_NUM];
		val = result.split(" ");
		for (j = 0; j < Params.CARD_MAX_NUM; j++) {
			mat1[j][0][0] = Double.valueOf(val[j]);
		}
		return mat1;
	}
	
	public static GaSysPlayer createPlayerFromFile(String pass, boolean useLog){
		String result = "";
		try{
			  File file = new File(pass);
			  FileReader filereader = new FileReader(file);

			  int ch = filereader.read();
			  while(ch != -1){
			    result = result + (char)ch;

			    ch = filereader.read();
			  }
			  
			  filereader.close();
			}catch(FileNotFoundException e){
			  System.out.println(e);
			}catch(IOException e){
			  System.out.println(e);
			}finally{
				
			}
		double[][][] mat1 = new double[Params.CARD_MAX_NUM][1][Params.CARD_MAX_NUM];
		double[][][] mat2 = new double[Params.CARD_MAX_NUM][1][Params.CARD_MAX_NUM];
		double[][][] mat3 = new double[Params.CARD_MAX_NUM][1][Params.CARD_MAX_NUM];
		double[][][] mat4 = new double[Params.CARD_MAX_NUM][1][1];
		double[][][] mat5 = new double[Params.CARD_MAX_NUM][1][1];
		double[][][] mat6 = new double[Params.CARD_MAX_NUM][1][1];
		double[][][] mat7 = new double[Params.CARD_MAX_NUM][1][1];
		
		String[] col = new String[Params.CARD_MAX_NUM];
		String[] val = new String[Params.CARD_MAX_NUM];
		col = result.split("\n");
		
		for (int n = 0; n < Params.CARD_MAX_NUM; n++) {
			val = col[1 + n].split(" ");
			for (int j = 0; j < Params.CARD_MAX_NUM; j++) {
				mat1[n][0][j] = Double.valueOf(val[j]);
			}
		}
		
		for (int n = 0; n < Params.CARD_MAX_NUM; n++) {
			val = col[2 + n + Params.CARD_MAX_NUM].split(" ");
			for (int j = 0; j < Params.CARD_MAX_NUM; j++) {
				mat2[n][0][j] = Double.valueOf(val[j]);
			}
		}
		
		for (int n = 0; n < Params.CARD_MAX_NUM; n++) {
			val = col[3 + n + Params.CARD_MAX_NUM * 2].split(" ");
			for (int j = 0; j < Params.CARD_MAX_NUM; j++) {
				mat3[n][0][j] = Double.valueOf(val[j]);
			}
		}
		
		val = col[4 + Params.CARD_MAX_NUM * 3].split(" ");
		for (int n = 0; n < Params.CARD_MAX_NUM; n++) {
			mat4[n][0][0] = Double.valueOf(val[n]);
		}
		
		val = col[6 + Params.CARD_MAX_NUM * 3].split(" ");
		for (int n = 0; n < Params.CARD_MAX_NUM; n++) {
			mat5[n][0][0] = Double.valueOf(val[n]);
		}
		
		val = col[8 + Params.CARD_MAX_NUM * 3].split(" ");
		for (int n = 0; n < Params.CARD_MAX_NUM; n++) {
			mat6[n][0][0] = Double.valueOf(val[n]);
		}
		
		val = col[10 + Params.CARD_MAX_NUM * 3].split(" ");
		for (int n = 0; n < Params.CARD_MAX_NUM; n++) {
			mat7[n][0][0] = Double.valueOf(val[n]);
		}
		
		return new GaSysPlayer(null, mat1, mat2, mat3, mat4, mat5, mat6, mat7, useLog);
	}
}
