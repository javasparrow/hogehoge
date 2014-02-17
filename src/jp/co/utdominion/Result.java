package jp.co.utdominion;

import java.io.PrintWriter;

public class Result {
	int[] _position;
	int _finalTurn;
	int[][] _finalDeck;
	int[] _finalVictry;
	int[][] _initialHand;
	
	Result(int[] position, int[][] finalDeck, int[] finalVictry, int finalTurn, int[][] initialHand){
		_position = position;
		_finalDeck = finalDeck;
		_finalTurn = finalTurn;
		_finalVictry = finalVictry;
		_initialHand = initialHand;
	}
	
	//1番手の順位、二番手の順位、、	と格納
	public int[]getPosition(){
		return _position;
	}
	
	public int[] getFinalVictry(){
		return _finalVictry;
	}
	
	public void printResult(){
		System.out.println("turn:" + _finalTurn);
		if(Params.PLAYER_NUM == 4) System.out.println(_position[0] + " " + _position[1] + " " + _position[2] + " " + _position[3]);
		int i, n;
		String cards = new String();
		for(i = 0;i < Params.PLAYER_NUM;i++){
			cards = "";
			for(n = 0; n < Params.CARD_MAX_NUM; n ++){
				if(_finalDeck[i][n] != 0)
					cards += (CardData.getInstence().getCardData(n + 1).getName() + ":" + _finalDeck[i][n]  + "/");
			}
			System.out.println("player" + i + "initial:" + CardData.getInstence().getCardData(_initialHand[i][0]).getName() 
					+ "/" + CardData.getInstence().getCardData(_initialHand[i][1]).getName());
			System.out.println("player" + i + "victry:" + _finalVictry[i]);
			System.out.println("player" + i + "deck:" + cards);
		}
	}
	
	public void printResult(PrintWriter pw){
		System.out.println("turn:" + _finalTurn);
		if(Params.PLAYER_NUM == 4) System.out.println(_position[0] + " " + _position[1] + " " + _position[2] + " " + _position[3]);
		pw.println("turn:" + _finalTurn);
		if(Params.PLAYER_NUM == 4) pw.println(_position[0] + "," + _position[1] + "," + _position[2] + "," + _position[3]);
		int i, n;
		String cards = new String();
		for(i = 0;i < Params.PLAYER_NUM;i++){
			cards = "";
			for(n = 0; n < Params.CARD_MAX_NUM; n ++){
				if(_finalDeck[i][n] != 0)
					cards += (CardData.getInstence().getCardData(n + 1).getName() + ":" + _finalDeck[i][n]  + "/");
			}
			System.out.println("player" + i + "initial:" + CardData.getInstence().getCardData(_initialHand[i][0]).getName() 
					+ "/" + CardData.getInstence().getCardData(_initialHand[i][1]).getName());
			System.out.println("player" + i + "victry:" + _finalVictry[i]);
			System.out.println("player" + i + "deck:" + cards);
			pw.println("player" + i + "initial:" + CardData.getInstence().getCardData(_initialHand[i][0]).getName() 
					+ "/" + CardData.getInstence().getCardData(_initialHand[i][1]).getName());
			pw.println("player" + i + "victry:" + _finalVictry[i]);
			pw.println("player" + i + "deck:" + cards);
		}
	}
	
	public String getResult(){
		String result;
		result = "turn:" + _finalTurn + "\n";
		if(Params.PLAYER_NUM == 4) result += _position[0] + " " + _position[1] + " " + _position[2] + " " + _position[3] + "\n";
		int i, n;
		String cards = new String();
		for(i = 0;i < Params.PLAYER_NUM;i++){
			cards = "";
			for(n = 0; n < Params.CARD_MAX_NUM; n ++){
				if(_finalDeck[i][n] != 0)
					cards += (CardData.getInstence().getCardData(n + 1).getName() + ":" + _finalDeck[i][n]  + "/");
			}
			result += "player" + i + "initial:" + CardData.getInstence().getCardData(_initialHand[i][0]).getName() 
					+ "/" + CardData.getInstence().getCardData(_initialHand[i][1]).getName() + "\n";
			result += "player" + i + "victry:" + _finalVictry[i] + "\n";
			result += "player" + i + "deck:" + cards + "\n";
		}
		return result;
	}
}
