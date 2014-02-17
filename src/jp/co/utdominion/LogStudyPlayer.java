package jp.co.utdominion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import Jama.Matrix;

public class LogStudyPlayer {
	private static final boolean DEBUG = true;

	//他人のデッキに対する行列(カード種類*1*カード種類)
	double[][][] _othersDeckMat = new double[Params.CARD_MAX_NUM][1][Params.CARD_MAX_NUM];
	//自分の n*1*n
	double[][][] _myDeckMat = new double[Params.CARD_MAX_NUM][1][Params.CARD_MAX_NUM];
	//サプライに対する n*1*n
	double[][][] _supplyMat = new double[Params.CARD_MAX_NUM][1][Params.CARD_MAX_NUM];
	//デッキの残り枚数に対する n*1*1
	double[][][] _deckCntMat = new double[Params.CARD_MAX_NUM][1][1];
	//デッキ総枚数に対する n*1*1
	//double[][][] _allCntMat;
	//ゲーム終了予測ターンに対する n*1*1
	double[][][] _endTurnMat = new double[Params.CARD_MAX_NUM][1][1];
	//トップとの勝利点差に対する n*1*1
	double[][][] _victryDiffMat = new double[Params.CARD_MAX_NUM][1][1];
	//手番mat
	double[][][] _positionMat = new double[Params.CARD_MAX_NUM][1][1];
	//距離合計
	double _dest;

	public LogStudyPlayer(double[][][] othersDeckMat, double[][][] myDeckMat, double[][][] supplyMat
			, double[][][] deckCntMat, double[][][] positionMat, double[][][] endTurnMat, double[][][] victryDiffMat) {
		_dest = 0;
		for (int n = 0; n < Params.CARD_MAX_NUM; n++) {
			for (int j = 0; j < Params.CARD_MAX_NUM; j++) {
				_othersDeckMat[n][0][j] = othersDeckMat[n][0][j];
				_myDeckMat[n][0][j] = myDeckMat[n][0][j];
				_supplyMat[n][0][j] = supplyMat[n][0][j];
			}
			_deckCntMat[n][0][0] = deckCntMat[n][0][0];
			_positionMat[n][0][0] = positionMat[n][0][0];
			_endTurnMat[n][0][0] = endTurnMat[n][0][0];
			_victryDiffMat[n][0][0] = victryDiffMat[n][0][0];
		}
	}

	void addDest(double dest) {
		_dest += dest;
	}

	double getDest() {
		return _dest;
	}

	public double[][][] getDeckCntMat() {
		return _deckCntMat;
	}

	public double[][][] getPositionMat() {
		return _positionMat;
	}

	/*public double[][][] getAllCntMat() {
		return _allCntMat;
	}*/

	public double[][][] getEndTurnMat() {
		return _endTurnMat;
	}

	public double[][][] getVictryDiffMat() {
		return _victryDiffMat;
	}

	public double[][][] getOthersDeckMat() {
		return _othersDeckMat;
	}

	public double[][][] getMyDeckMat() {
		return _myDeckMat;
	}

	public double[][][] getsupplyMat() {
		return _supplyMat;
	}

	//ログ学習用の関数
	public double calculateDiffFromLog(DominionLog log) {
		_log = log;
		ArrayList<Integer> buyList = new ArrayList<Integer>();
		double maxScore = getBestScore(log.getCoin(), log.getBuy(), buyList, log.getPlayerMat());

		double maxLogScore = getBestLogScore(_log.getBuyList(), log.getPlayerMat());

		//正解
		if (buyList.containsAll(log.buyCard) && buyList.size() == log.buyCard.size())
			return 0;

		if (maxScore < 0.000000001 && _log.buyCard.size() != 0)
			return Params.LOG_DISTANCE_MAX;

		//不正解の場合ペナルティを与える
		double result = (1 - maxLogScore / maxScore) * (maxScore - maxLogScore / maxScore)
				+ Params.LOG_UNCORRECT_PENALTY;

		if(result > Params.LOG_DISTANCE_MAX) result = Params.LOG_DISTANCE_MAX;
		
		return result;
	}

	private boolean existCard(int card) {
		for (Supply sup : _log.getSupply()) {
			if (sup.getCard() == card && sup.getNum() != 0)
				return true;
		}
		return false;
	}

	private int getSupplyLeft(int card) {
		for (Supply sup : _log.getSupply()) {
			if (sup.getCard() == card)
				return sup.getNum();
		}
		return -1;
	}

	DominionLog _log;

	//ログの最高評価値となる買い方を探索
	double getBestLogScore(ArrayList<Integer> buyList, double[][] myDeckMat) {
		if (buyList.size() == 0)
			return 0;
		double maxScore = -999;
		for (Integer card : buyList) {
			ArrayList<Integer> newList = new ArrayList<Integer>();
			newList.addAll(buyList);
			newList.remove(card);
			double[][] newMyDeckMat = new double[Params.CARD_MAX_NUM][1];
			for (int n = 0; n < Params.CARD_MAX_NUM; n++) {
				newMyDeckMat[n][0] = myDeckMat[n][0];
			}
			newMyDeckMat[card - 1][0] += 1;
			double score = getBestLogScore(newList, newMyDeckMat) + calcScoreOfCard(card, myDeckMat);
			if (score > maxScore) {
				maxScore = score;
			}
		}
		return maxScore;
	}

	//再帰による評価値計算
		double getBestScore(int coin, int buy, ArrayList<Integer> list, double[][] myDeckMat) {
			if (buy == 0)
				return 0;
			double maxScore = 0;
			ArrayList<Integer> maxList = null;
			for (int i = 1; i <= Params.CARD_MAX_NUM; i++) {
				//呪いまたは買えないものは飛ばす
				if (i == Params.CARD_CURSE
						|| !existCard(i)
						|| coin < CardData.getInstence().getCardData(i).getCost()
						|| getSupplyLeft(i) - countArray(list, i) <= 0)
					continue;
				//コインが3以上残ってるとき銅貨の探索を飛ばす（こうしないと探索時間がかかりすぎる）
				if(i == Params.CARD_COPPER && coin >= 3) continue;
				ArrayList<Integer> newList = new ArrayList<Integer>();
				newList.addAll(list);
				double[][] newMyDeckMat = new double[Params.CARD_MAX_NUM][1];
				for (int n = 0; n < Params.CARD_MAX_NUM; n++) {
					newMyDeckMat[n][0] = myDeckMat[n][0];
				}
				newList.add(i);
				newMyDeckMat[i - 1][0]++;
				double cardScore = calcScoreOfCard(i, myDeckMat);
				//評価値が負のものは近似的に飛ばす TODO いいのか？
				if(cardScore < 0) continue;
				
				double score = getBestScore(coin - CardData.getInstence().getCardData(i).getCost(), buy - 1, newList,
						newMyDeckMat)
						+ cardScore;
				if (score > maxScore) {
					maxScore = score;
					maxList = newList;
				}
			}
			if (maxList != null) {
				list.clear();
				list.addAll(maxList);
			}
			return maxScore;
		}

	double calcScoreOfCard(int card, double[][] myDeckMat) {
		Matrix mat1, mat2;
		double score = 0;
		for (int n = 0; n < Params.PLAYER_NUM; n++) {
			if (n == 0) {
				mat1 = new Matrix(myDeckMat);
				mat2 = new Matrix(_myDeckMat[card - 1]);
			}
			else {
				mat1 = new Matrix(_log.getOthersMat()[n - 1]);
				mat2 = new Matrix(_othersDeckMat[card - 1]);
			}
			score += mat1.times(mat2).get(0, 0);
		}
		mat1 = new Matrix(_log.getSupplyMat());
		mat2 = new Matrix(_supplyMat[card - 1]);
		score += mat1.times(mat2).get(0, 0);

		//デッキの残り枚数の考慮
		score += _log.getDeckCnt() * _deckCntMat[card - 1][0][0];
		//手番
		score += _log.getPos() * _positionMat[card - 1][0][0];
		//ゲーム終了推測ターン数の考慮
		score += _log.getLeftTurn() * _endTurnMat[card - 1][0][0];
		//トップとの勝利点差の考慮
		score += _log.getVdiff() * _victryDiffMat[card - 1][0][0];

		return score;
	}

	public static Serializable deepClone(Serializable src)
			throws IOException, ClassNotFoundException {

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream oout = new ObjectOutputStream(bout);
		oout.writeObject(src);
		oout.flush();

		byte[] serialized = bout.toByteArray();

		ByteArrayInputStream bin = new ByteArrayInputStream(serialized);
		ObjectInputStream oin = new ObjectInputStream(bin);
		return (Serializable) oin.readObject();
	}

	private int countArray(ArrayList<Integer> array, int num) {
		int count = 0;
		for (int card : array) {
			if (card == num)
				count++;
		}
		return count;
	}
}
