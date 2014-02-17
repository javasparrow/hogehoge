package jp.co.utdominion;

import java.util.ArrayList;

import jp.co.utdominion.gui.DomController;

public class DomCore {

	private static final boolean DEBUG = false;

	//プレイヤー
	Player[] _players = new Player[Params.PLAYER_NUM];

	//現在のプレイヤー
	int _currentPlayer;

	//ターン数
	int _turn;

	//サプライ
	//TODO 基本セットのみの対応
	Supply[] _supply = new Supply[17];

	//終了ターン
	int _endTurn;
	
	//初期手札
	int[][] _initialHand = new int[Params.PLAYER_NUM][2];

	DomController _gui = null;
	
	public DomCore() {

	}

	//ゲーム開始
	public Result executeGame(Player[] players, int maxTurn, int hand, DomController gui) {
		_endTurn = maxTurn;
		_players = players;
		_gui = gui;
		int i;
		for (i = 0; i < Params.PLAYER_NUM; i++) {
			_players[i].init(hand, i);
		}
		initGame();
		
		refreshGUI();
		
		for (_turn = 1; _turn < maxTurn + 1; _turn++) {
			
			if (DEBUG)
				System.out.println("turn" + _turn + "start");
			for (i = 0; i < Params.PLAYER_NUM; i++) {
				_currentPlayer = i;
				_players[i].executeTurn();
				if (judgeEndGame())
					return getResult();
			}
		}
		return getResult();
	}

	//ゲームログの作成
	Result getResult() {
		int score[] = new int[Params.PLAYER_NUM];
		int score_keep[] = new int[Params.PLAYER_NUM];
		int[] position = new int[Params.PLAYER_NUM];
		ArrayList<Integer> maxPlayer = new ArrayList<Integer>();
		int i;
		int maxScore;
		int max = 1;
		maxScore = -100;
		for (i = 0; i < Params.PLAYER_NUM; i++) {
			score[i] = _players[i].getTotalVictry();
			if (DEBUG)
				System.out.println("player" + i + " score:" + score[i]);
		}
		score_keep = score.clone();

		while (max <= Params.PLAYER_NUM) {
			maxScore = -100;
			maxPlayer.clear();
			for (i = 0; i < Params.PLAYER_NUM; i++) {
				if (score[i] >= maxScore)
					maxScore = score[i];
			}

			for (i = 0; i < Params.PLAYER_NUM; i++) {
				if (score[i] == maxScore)
					maxPlayer.add(i);
			}
			//複数人いる場合
			if (maxPlayer.size() > 1) {
				if (maxPlayer.get(0) <= _currentPlayer && maxPlayer.get(maxPlayer.size() - 1) > _currentPlayer) {
					int counter = 0;
					for (Integer pl : maxPlayer) {
						if (pl > _currentPlayer) {
							position[pl] = max;
							score[pl] = -100;
							counter++;
						}
					}
					max += counter;
				}
				else {
					for (Integer pl : maxPlayer) {
						position[pl] = max;
						score[pl] = -100;
					}
					max += maxPlayer.size();
				}
			}
			else {
				position[maxPlayer.get(0)] = max;
				score[maxPlayer.get(0)] = -100;
				max++;
			}
			//プレイヤーの点数(1:6 2:3 3:1 4:0)の記録
			int n;
			for (n = 0; n < Params.PLAYER_NUM; n++) {
				switch (position[n]) {
				case 1:
					if (count(position, 1) == 1)
						_players[n].addScore(6);
					if (count(position, 1) == 2)
						_players[n].addScore(4.5);
					if (count(position, 1) == 3)
						_players[n].addScore(3.3);
					if (count(position, 1) == 4)
						_players[n].addScore(2.5);
					break;
				case 2:
					if (count(position, 2) == 1)
						_players[n].addScore(3);
					if (count(position, 2) == 2)
						_players[n].addScore(2);
					if (count(position, 2) == 3)
						_players[n].addScore(1.3);
					break;
				case 3:
					if (count(position, 3) == 1)
						_players[n].addScore(1);
					if (count(position, 3) == 2)
						_players[n].addScore(0.5);
					break;
				case 4:
					_players[n].addScore(0);
					break;
				}
			}
		}

		int[][] finalCard = new int[Params.PLAYER_NUM][Params.CARD_MAX_NUM];
		int n;
		for (i = 0; i < Params.PLAYER_NUM; i++) {
			for (n = 0; n < Params.CARD_MAX_NUM; n++) {
				finalCard[i][n] = getPlayersCardCnt(i, n + 1);
			}
		}
		return new Result(position.clone(), finalCard, score_keep, _turn, _initialHand);
	}

	private int count(int[] target, int num) {
		int counter = 0;
		int i;
		for (i = 0; i < target.length; i++) {
			if (target[i] == num)
				counter++;
		}
		return counter;
	}

	//ゲーム終了判断
	public boolean judgeEndGame() {
		int emptyCnt = 0;
		for (Supply supply : _supply) {
			if (supply.getNum() == 0) {
				if (supply.getCard() == Params.CARD_PROVINCE)
					return true;
				emptyCnt++;
			}
		}
		if (emptyCnt >= 3)
			return true;
		return false;
	}

	boolean dontHave(Supply[] data, int num){
		for(int i = 0; i < data.length; i ++){
			if(data[i] != null && data[i].getCard() == num) return false;
		}
		return true;
	}
	
	//ゲーム初期化
	void initGame() {
		//サプライの初期化
		//テスト用 noroi = 7
		int i;
		for(i = 0; i < 10; i ++){
			int card = (int)(Math.random()* (Params.CARD_MAX_NUM - 7)) + 8;
			while(!dontHave(_supply, card)){
				card = (int)(Math.random()* (Params.CARD_MAX_NUM - 7)) + 8;
			}
			
			if(CardData.getInstence().getCardData(card).isVictry())
				_supply[i] = new Supply(card, 12);
			else
				_supply[i] = new Supply(card, 10);
		}
		
		/*_supply[0] = new Supply(Params.CARD_SMITHY, 10);
		_supply[1] = new Supply(Params.CARD_CARAVAN, 10);
		_supply[2] = new Supply(Params.CARD_FISHINGVILLAGE, 10);
		_supply[3] = new Supply(Params.CARD_LABORATRY, 10);
		_supply[4] = new Supply(Params.CARD_MONEYLENDER, 10);
		_supply[5] = new Supply(Params.CARD_WITCH, 10);
		_supply[6] = new Supply(Params.CARD_LIGHTHOUSE, 10);
		_supply[7] = new Supply(Params.CARD_SEAHUG, 10);
		_supply[8] = new Supply(Params.CARD_DUKE, 12);
		_supply[9] = new Supply(Params.CARD_JUNKDEALER, 10);*/
		_supply[Params.SUPPLY_COPPER] = new Supply(Params.CARD_COPPER, 60 - 7 * Params.PLAYER_NUM);
		_supply[Params.SUPPLY_SILVER] = new Supply(Params.CARD_SILVER, 40);
		_supply[Params.SUPPLY_GOLD] = new Supply(Params.CARD_GOLD, 30);
		_supply[Params.SUPPLY_ESTATE] = new Supply(Params.CARD_ESTATE, 12);
		_supply[Params.SUPPLY_DUCHY] = new Supply(Params.CARD_DUCHY, 12);
		_supply[Params.SUPPLY_PROVINCE] = new Supply(Params.CARD_PROVINCE, 12);
		_supply[Params.SUPPLY_CURSE] = new Supply(Params.CARD_CURSE, 30);
	}

	//サプライの情報を提供
	public Supply[] getSupply() {
		return _supply.clone();
	}
	
	//残り枚数を取得
	public int getSupplyLeft(int card){
		for(int i = 0; i < 18; i ++){
			if(_supply[i].getCard() == card) return _supply[i].getNum();
		}
		return -1;
	}

	//購入(サプライの番号で指定)
	//買えたかどうかを返す
	public boolean buyCard(int num) {
		if(_turn < 3){
			_initialHand[_currentPlayer][_turn - 1] = _supply[num].getCard();
		}
		return _supply[num].buy();
	}

	//指定したプレイヤーの指定したカードの所持枚数を返す
	public int getPlayersCardCnt(int player, int card) {
		return _players[player].countCard(card);
	}

	//終了ターンを返す
	public int getEndTurn() {
		return _endTurn;
	}

	//終了ターンを返す
	public int getTurn() {
		return _turn;
	}
	
	public int getPlayersVictry(int player){
		return _players[player].getTotalVictry();
	}

	//アタック処理
	public void executeAttack(int id, Player player) {
		for (int i = 0; i < Params.PLAYER_NUM; i++) {
			if (_players[i] != player)
				_players[i].executeAttack(id);
		}
	}
	//GUI用
	public ArrayList<Card> getPlayArea(){
		return _players[_currentPlayer]._play;
	}
	//GUI用
	public ArrayList<Card> getNewDuration(){
		return _players[_currentPlayer]._newDuration;
	}
	public ArrayList<Card> getOldDuration(){
		return _players[_currentPlayer]._oldDuration;
	}
	
	public void refreshGUI(){
		if(_gui != null) _gui.refresh(this); 
	}
}
