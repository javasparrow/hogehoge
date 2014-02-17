package jp.co.utdominion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import Jama.Matrix;

public class GaSysPlayer extends Player {

	private static final boolean DEBUG = false;

	//ログ用
		ArrayList<String> _logs = new ArrayList<String>();
		ArrayList<Integer> _lastBuyCard = new ArrayList<Integer>();
		int _lastCoin;
		int _lastBuy;
	
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

	public GaSysPlayer(DomCore core, double[][][] othersDeckMat, double[][][] myDeckMat, double[][][] supplyMat
			, double[][][] deckCntMat, double[][][] positionMat, double[][][] endTurnMat, double[][][] victryDiffMat) {
		super(core);
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

	boolean useCard() {
		ArrayList<Card> hand = getHand();

		//5コスドローがあったら使う(残り2アクション)
		for (Card card : hand) {
			if (card.getId() == Params.CARD_WITCH || card.getId() == Params.CARD_COUNCILROOM
					|| card.getId() == Params.CARD_WHARF) {
				if (getAction() > 1) {
					useCard(card);
					return true;
				}
			}
		}

		//鍛冶屋があったら使う(残り2アクション)
		for (Card card : hand) {
			if (card.getId() == Params.CARD_SMITHY) {
				if (getAction() > 1) {
					useCard(card);
					return true;
				}
			}
		}

		//掘があったら使う(残り2アクション)
		for (Card card : hand) {
			if (card.getId() == Params.CARD_MOAT) {
				if (getAction() > 1) {
					useCard(card);
					return true;
				}
			}
		}

		//共謀できる共謀者
		for (Card card : hand) {
			if (card.getId() == Params.CARD_CONSPIRATOR && (_play.size() + _newDuration.size()) >= 2) {
				if (getAction() > 0) {
					useCard(card);
					return true;
				}
			}
		}

		//使う
		for (Card card : hand) {
			if (card.getId() == Params.CARD_CARAVAN || card.getId() == Params.CARD_FISHINGVILLAGE
					|| card.getId() == Params.CARD_LABORATRY || card.getId() == Params.CARD_LIGHTHOUSE
					|| card.getId() == Params.CARD_MARKET || card.getId() == Params.CARD_FESTIVAL
					|| card.getId() == Params.CARD_VILLAGE || card.getId() == Params.CARD_GREATHALL
					|| card.getId() == Params.CARD_BAZZER || card.getId() == Params.CARD_WORKERSVILLAGE) {
				if (getAction() > 0) {
					useCard(card);
					return true;
				}
			}
		}

		//屑屋があったら使う
		for (Card card : hand) {
			if (card.getId() == Params.CARD_JUNKDEALER) {
				if (getAction() > 0 && (isCardInHand(Params.CARD_CURSE) ||
						isCardInHand(Params.CARD_ESTATE) || isCardInHand(Params.CARD_COPPER))) {
					useCard(card);
					return true;
				}
			}
		}

		//アクションがあと１しかない場合貧民街を使う
		for (Card card : hand) {
			if (card.getId() == Params.CARD_SHANTYTOWN) {
				if (getAction() == 1) {
					useCard(card);
					return true;
				}
			}
		}

		//屋敷または呪いが手にある場合優先的に礼拝堂を使う
		for (Card card : hand) {
			if (card.getId() == Params.CARD_CHAPEL) {
				if (getAction() > 0 && (isCardInHand(Params.CARD_ESTATE) || isCardInHand(Params.CARD_CURSE))) {
					useCard(card);
					return true;
				}
			}
		}

		//ヨウバがあったら使う
		for (Card card : hand) {
			if (card.getId() == Params.CARD_SEAHUG) {
				if (getAction() > 0 && existCard(Params.CARD_CURSE)) {
					useCard(card);
					return true;
				}
			}
		}

		//魔女があったら使う
		for (Card card : hand) {
			if (card.getId() == Params.CARD_WITCH) {
				if (getAction() > 0 && existCard(Params.CARD_CURSE)) {
					useCard(card);
					return true;
				}
			}
		}

		//5コスターミナルあったら使う
		for (Card card : hand) {
			if (card.getId() == Params.CARD_COUNCILROOM || card.getId() == Params.CARD_WHARF
					|| card.getId() == Params.CARD_MARCHANTSHIP || card.getId() == Params.CARD_EXPLORER) {
				if (getAction() > 0) {
					useCard(card);
					return true;
				}
			}
		}

		//その他4コスターミナル
		for (Card card : hand) {
			if (card.getId() == Params.CARD_MONEYLENDER || card.getId() == Params.CARD_WOODCUTTER
					|| card.getId() == Params.CARD_SMITHY || card.getId() == Params.CARD_CUTPURSE) {
				if (getAction() > 0) {
					useCard(card);
					return true;
				}
			}
		}

		//その他2コスターミナル+共謀できない共謀者+呪いがない魔女
		for (Card card : hand) {
			if (card.getId() == Params.CARD_MOAT || card.getId() == Params.CARD_CONSPIRATOR
					|| card.getId() == Params.CARD_WITCH) {
				if (getAction() > 0) {
					useCard(card);
					return true;
				}
			}
		}

		//屋敷呪いないときの礼拝堂
		for (Card card : hand) {
			if (card.getId() == Params.CARD_CHAPEL) {
				if (getAction() > 0) {
					useCard(card);
					return true;
				}
			}
		}

		//貧民街は最後
		for (Card card : hand) {
			if (card.getId() == Params.CARD_SHANTYTOWN) {
				if (getAction() > 0) {
					useCard(card);
					return true;
				}
			}
		}

		return false;
	}

	@Override
	void aiTurn() {
		ArrayList<Card> hand;

		//ログの生成
		String logString = "supply\n";
		//評価地の計算
		double[][][] playerMat = new double[Params.PLAYER_NUM][Params.CARD_MAX_NUM][1];
		double[][] supplyMat = new double[Params.CARD_MAX_NUM][1];

		for (int n = 0; n < Params.PLAYER_NUM; n++) {
			for (int i = 1; i <= Params.CARD_MAX_NUM; i++) {
				playerMat[n][i - 1][0] = getPlayersCardCnt(n, i);
			}
		}
		for (int i = 1; i <= Params.CARD_MAX_NUM; i++) {
			if (existCardIgnoreLeft(i) == true)
				logString += "1,";
			else
				logString += "0,";
		}
		logString += "\nmyDeck\n";
		for (int i = 1; i <= Params.CARD_MAX_NUM; i++) {
			logString += playerMat[getPosition()][i - 1][0] + ",";
		}
		for (int n = 0; n < Params.PLAYER_NUM; n++) {
			if (n == getPosition())
				continue;
			logString += "\nothersDeck\n";
			for (int i = 1; i <= Params.CARD_MAX_NUM; i++) {
				logString += playerMat[n][i - 1][0] + ",";
			}
		}
		String postString;

		postString = "\nendTurn\n" + estimateLeftTurn();
		postString += "\nvDiff\n" + calculateVictryDiff();
		postString += "\npos\n" + getPosition();

		Supply[] supply = _core.getSupply();
		String supString = new String();
		for (Supply sup : supply) {
			supString += sup.getCard() + "," + sup.getNum() + "\n";
		}

		while (getAction() != 0) {
			if (useCard() == false)
				break;
		}
		endActionPhase();

		hand = getHand();
		//財宝があったら使う
		for (Card card : hand) {
			if (card.isTreasure()) {
				useCard(card);
				hand = getHand();
			}
		}
		endTreasurePhase();

		
		_lastCoin = getCoin();
		_lastBuy = getBuy();

		int i, n;

		for (n = 0; n < Params.PLAYER_NUM; n++) {
			for (i = 1; i <= Params.CARD_MAX_NUM; i++) {
				playerMat[n][i - 1][0] = getPlayersCardCnt(n, i);
			}
		}
		for (i = 1; i <= Params.CARD_MAX_NUM; i++) {
			if (existCardIgnoreLeft(i) == true)
				supplyMat[i - 1][0] = 1;
			else
				supplyMat[i - 1][0] = 0;
		}
		leftTurn = estimateLeftTurn();
		vDiff = calculateVictryDiff();
		ArrayList<Integer> buyList = new ArrayList<Integer>();

		getBestScore(getCoin(), getBuy(), buyList, playerMat[getPosition()]);

		for (Integer card : buyList) {
			if (DEBUG)
				System.out.println("player" + getPosition() + "buy card" + card);
			buyCard(card);
		}
		
		logString += "\ndeckCnt\n" + _deck.size();
		logString += postString;
		logString += "\ncoin\n" + _lastCoin;
		logString += "\nbuyNum\n" + _lastBuy;
		logString += "\nBoughtCard\n";
		for (int card : buyList) {
			logString += card + ",";
		}
		
		logString += "\nsupplyLeft\n";
		logString += supString;
		
		_logs.add(logString);
	}

	//評価地の計算
	double[][][] playerMat = new double[Params.PLAYER_NUM][Params.CARD_MAX_NUM][1];
	double[][] supplyMat = new double[Params.CARD_MAX_NUM][1];
	//TODO 予測ターンなどの更新をしていない
	int leftTurn, vDiff;

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
					|| _core.getSupplyLeft(i) - countArray(list, i) <= 0)
				continue;
			//コインが3以上残ってるとき銅貨の探索を飛ばす（こうしないと探索時間がかかりすぎる）
			if (i == Params.CARD_COPPER && coin >= 3)
				continue;
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
			if (cardScore < 0)
				continue;

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

			if (n == getPosition()) {
				mat1 = new Matrix(myDeckMat);
				mat2 = new Matrix(_myDeckMat[card - 1]);
			}
			else {
				mat1 = new Matrix(playerMat[n]);
				mat2 = new Matrix(_othersDeckMat[card - 1]);
			}
			score += mat1.times(mat2).get(0, 0);
		}
		mat1 = new Matrix(supplyMat);
		mat2 = new Matrix(_supplyMat[card - 1]);
		score += mat1.times(mat2).get(0, 0);

		//デッキの残り枚数の考慮
		score += _deck.size() * _deckCntMat[card - 1][0][0];
		//手番
		score += getPosition() * _positionMat[card - 1][0][0];
		//ゲーム終了推測ターン数の考慮
		score += leftTurn * _endTurnMat[card - 1][0][0];
		//トップとの勝利点差の考慮
		score += vDiff * _victryDiffMat[card - 1][0][0];

		return score;
	}

	private int calculateVictryDiff() {
		int minDiff = 999;
		for (int n = 0; n < Params.PLAYER_NUM; n++) {
			if (n == getPosition())
				continue;
			int diff = getTotalVictry() - _core.getPlayersVictry(n);
			if (diff < minDiff)
				minDiff = diff;
		}
		return minDiff;
	}

	private int estimateLeftTurn() {

		//サプライ少ない順に並び替えるコンパレータ
		class MyComparator implements Comparator<Supply> {
			@Override
			public int compare(Supply o1, Supply o2) {
				if (o1.getNum() == o2.getNum())
					return 0;
				return o1.getNum() < o2.getNum() ? -1 : 1;
			}
		}

		Supply[] supply = _core.getSupply();

		int leftProvince = supply[Params.SUPPLY_PROVINCE].getNum();

		// sort
		Arrays.sort(supply, new MyComparator());

		int leftForThree = supply[0].getNum() + supply[1].getNum() + supply[2].getNum();
		return Math.min(leftForThree, leftProvince);
	}

	//ログ学習用の関数
	/*public double calculateDiffFromLog(DominionLog log){
		for (int n = 0; n < Params.PLAYER_NUM; n++) {
			for (int i = 1; i <= Params.CARD_MAX_NUM; i++) {
				playerMat[n][i - 1][0] = getPlayersCardCnt(n, i);
			}
		}
		for (int i = 1; i <= Params.CARD_MAX_NUM; i++) {
			if (existCardIgnoreLeft(i) == true)
				supplyMat[i - 1][0] = 1;
			else
				supplyMat[i - 1][0] = 0;
		}
		leftTurn = estimateLeftTurn();
		vDiff = calculateVictryDiff();
		ArrayList<Integer> buyList = new ArrayList<Integer>();

		getBestScore(getCoin(), getBuy(), buyList,log.getPlayerMat());
	}*/

	class CardPoint {
		public double point;
		public int card;

		CardPoint(double mpoint, int mcard) {
			point = mpoint;
			card = mcard;
		}

	}

	private int countArray(ArrayList<Integer> array, int num) {
		int count = 0;
		for (int card : array) {
			if (card == num)
				count++;
		}
		return count;
	}
	
	public ArrayList<String> getLogString() {
		return _logs;
	}
}
