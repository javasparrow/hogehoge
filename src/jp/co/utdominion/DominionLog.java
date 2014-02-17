package jp.co.utdominion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DominionLog {
	
	//評価地の計算
	double[][][] othersMat = new double[Params.PLAYER_NUM - 1][Params.CARD_MAX_NUM][1];
	double[][] playerMat = new double[Params.CARD_MAX_NUM][1];
	double[][] supplyMat = new double[Params.CARD_MAX_NUM][1];
	int leftTurn, vDiff, deckCnt, pos , coin , buy;
	ArrayList<Integer> buyCard = new ArrayList<Integer>();
	ArrayList<Supply> supply = new ArrayList<Supply>();
	
	double[][][] getOthersMat(){
		return othersMat;
	}
	
	double[][] getPlayerMat(){
		return playerMat;
	}
	
	double[][] getSupplyMat(){
		return supplyMat;
	}
	
	int getLeftTurn(){
		return leftTurn;
	}
	
	int getVdiff(){
		return vDiff;
	}
	
	int getDeckCnt(){
		return deckCnt;
	}
	
	int getPos(){
		return pos;
	}
	
	int getCoin(){
		return coin;
	}
	
	int getBuy(){
		return buy;
	}
	
	ArrayList<Integer> getBuyList(){
		return buyCard;
	}
	
	ArrayList<Supply> getSupply(){
		return supply;
	}
	
			
	DominionLog(String pass){
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
		
		double[][][][] mat1 = new double[5][1][1][Params.CARD_MAX_NUM];
		String[] col = new String[Params.CARD_MAX_NUM];
		String[] val = new String[Params.CARD_MAX_NUM];
		col = result.split("\n");
		//0 = supply
		val = col[1].split(",");
		for (j = 0; j < Params.CARD_MAX_NUM; j++) {
			supplyMat[j][0] = Double.valueOf(val[j]);
		}
		//2 = myDeck
		val = col[3].split(",");
		for (j = 0; j < Params.CARD_MAX_NUM; j++) {
			playerMat[j][0] = Double.valueOf(val[j]);
		}
		//4,6,8othersDeckMat
		for(n = 0; n < 3; n ++){
			val = col[n * 2 + 5].split(",");
			for (j = 0; j < Params.CARD_MAX_NUM; j++) {
				othersMat[n][j][0] = Double.valueOf(val[j]);
			}
		}
		//10 deckCnt
		deckCnt = Integer.valueOf(col[11]);
		//12endTurn
		leftTurn = Integer.valueOf(col[13]);
		//14 vDiff
		vDiff = Integer.valueOf(col[15]);
		//16 pos
		pos = Integer.valueOf(col[17]);
		//18 coin
		coin = Integer.valueOf(col[19]);
		//20 buy
		buy = Integer.valueOf(col[21]);
		//22 boughtCard
		val = col[23].split(",");
		for(String card: val){
			if(card.contains("\n") || card.isEmpty()) break;
			buyCard.add(Double.valueOf(card).intValue());
		}
		//24 supplyLeft
		for(n = 0; n < 17; n ++){
			val = col[n + 25].split(",");
			supply.add(new Supply(Integer.valueOf(val[0]), Integer.valueOf(val[1])));
		}
	}
}
