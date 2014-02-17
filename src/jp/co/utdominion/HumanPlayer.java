package jp.co.utdominion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JOptionPane;

public class HumanPlayer extends Player {

	//ログ用
	ArrayList<String> _logs = new ArrayList<String>();
	ArrayList<Integer> _lastBuyCard = new ArrayList<Integer>();
	int _lastCoin;
	int _lastBuy;

	public HumanPlayer(DomCore core) {
		super(core);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	int currentPhase = -1;

	public ArrayList<String> getLogString() {
		return _logs;
	}

	public void useCard(int num) {
		if (!getHand().get(num).isAction())
			return;
		super.useCard(getHand().get(num));
		_core.refreshGUI();
	}

	public void buyCard(int num) {
		if (currentPhase != Params.PHASE_BUY)
			return;
		System.out.println("buy:" + num);
		super.buyCard(_core.getSupply()[num].getCard());
		_core.refreshGUI();
		_lastBuyCard.add(_core.getSupply()[num].getCard());
		if (getBuy() == 0)
			currentPhase = -1;
	}

	public void goNextPhase() {
		switch (currentPhase) {
		case Params.PHASE_ACTION:
			endActionPhase();
			ArrayList<Card> hand = getHand();
			//財宝があったら使う
			for (Card card : hand) {
				if (card.isTreasure()) {
					useCard(card);
					hand = getHand();
				}
			}
			_lastBuy = getBuy();
			_lastCoin = getCoin();
			endTreasurePhase();
			currentPhase = Params.PHASE_BUY;
			break;
		case Params.PHASE_BUY:
			if (_lastBuyCard.size() == 0) {
				int option = JOptionPane.showConfirmDialog(null, "何も買っていませんが購入フェイズを終了しますか？",
						"最終確認", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);

				if (option == JOptionPane.YES_OPTION) {
					;
				} else if (option == JOptionPane.NO_OPTION) {
					return;
				}
			}
			currentPhase = -1;
			break;
		}
		_core.refreshGUI();
	}

	@Override
	void aiTurn() {
		currentPhase = Params.PHASE_ACTION;

		_core.refreshGUI();
		_lastBuyCard.clear();

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
		

		while (currentPhase != -1) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		
		logString += "\ndeckCnt\n" + _deck.size();
		logString += postString;
		logString += "\ncoin\n" + _lastCoin;
		logString += "\nbuyNum\n" + _lastBuy;
		logString += "\nBoughtCard\n";
		for (int card : _lastBuyCard) {
			logString += card + ",";
		}
		
		logString += "\nsupplyLeft\n";
		logString += supString;
		
		_logs.add(logString);

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

}
