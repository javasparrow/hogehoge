package jp.co.utdominion;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Player {
	private static final boolean DEBUG = false;

	//デッキのデータ
	ArrayList<Card> _deck =
			new ArrayList<Card>();
	//捨て札のデータ
	ArrayList<Card> _discard =
			new ArrayList<Card>();
	//プレイされたカードのデータ
	ArrayList<Card> _play =
			new ArrayList<Card>();
	//手札のデータ
	ArrayList<Card> _hand =
			new ArrayList<Card>();
	//持続カードのデータ
	//使ったときにnewに入りターン開始時に効果解決した後oldに入る(停泊所どうするんだ...)
	ArrayList<Card> _newDuration =
			new ArrayList<Card>();
	ArrayList<Card> _oldDuration =
			new ArrayList<Card>();
	DomCore _core;
	
	ArrayList<Card> _lastBuy = new ArrayList<Card>();

	//各種パラメータ
	int _coin;
	int _action;
	int _buy;
	int _vpToken;
	//現在のフェイズ
	int _phase;
	//手番
	int _position;
	//得点
	double _score;
	//
	int _games;

	Player(DomCore core) {
		_core = core;
		_games = 0;
		_score = 0;
		//init(Params.HAND_RANDOM);
	}

	public void init(int hand, int position) {

		_position = position;
		_vpToken = 0;

		CardData cd = CardData.getInstence();

		_deck.clear();
		_discard.clear();
		_play.clear();
		_hand.clear();
		_newDuration.clear();
		_oldDuration.clear();

		//初期デッキの作成
		int i;

		switch (hand) {
		case Params.HAND_RANDOM:
			for (i = 0; i < 7; i++) {
				_deck.add(cd.getCardData(Params.CARD_COPPER));
			}
			for (i = 0; i < 3; i++) {
				_deck.add(cd.getCardData(Params.CARD_ESTATE));
			}
			suffleDeck();
			break;
		case Params.HAND_25:
			for (i = 0; i < 7; i++) {
				_deck.add(cd.getCardData(Params.CARD_COPPER));
			}
			for (i = 0; i < 3; i++) {
				_deck.add(cd.getCardData(Params.CARD_ESTATE));
			}
			break;
		case Params.HAND_34:
			for (i = 0; i < 3; i++) {
				_deck.add(cd.getCardData(Params.CARD_COPPER));
			}
			for (i = 0; i < 3; i++) {
				_deck.add(cd.getCardData(Params.CARD_ESTATE));
			}
			for (i = 0; i < 4; i++) {
				_deck.add(cd.getCardData(Params.CARD_COPPER));
			}
			break;
		}

		drawCard(5);
	}

	//合計点を返す
	public int getTotalVictry() {
		int point = 0;
		for (Card card : _deck) {
			point += card.getVictry(this);
		}
		for (Card card : _discard) {
			point += card.getVictry(this);
		}
		for (Card card : _play) {
			point += card.getVictry(this);
		}
		for (Card card : _hand) {
			point += card.getVictry(this);
		}
		for (Card card : _newDuration) {
			point += card.getVictry(this);
		}
		for (Card card : _oldDuration) {
			point += card.getVictry(this);
		}
		return point + _vpToken;
	}

	protected int countCard(int id) {
		int point = 0;
		for (Card card : _deck) {
			if (card.getId() == id)
				point++;
		}
		for (Card card : _discard) {
			if (card.getId() == id)
				point++;
		}
		for (Card card : _play) {
			if (card.getId() == id)
				point++;
		}
		for (Card card : _hand) {
			if (card.getId() == id)
				point++;
		}
		for (Card card : _newDuration) {
			if (card.getId() == id)
				point++;
		}
		for (Card card : _oldDuration) {
			if (card.getId() == id)
				point++;
		}
		return point;
	}

	protected int countCardAll() {
		int point = 0;
		for (Card card : _deck) {
			point++;
		}
		for (Card card : _discard) {
			point++;
		}
		for (Card card : _play) {
			point++;
		}
		for (Card card : _hand) {
			point++;
		}
		for (Card card : _newDuration) {
			point++;
		}
		for (Card card : _oldDuration) {
			point++;
		}
		return point;
	}

	protected int countDeckCard() {
		int point = 0;
		for (Card card : _deck) {
			point++;
		}
		return point;
	}

	//指定されたidのカードがあるかどうか
	protected boolean existCard(int id) {
		Supply[] supply = _core.getSupply();
		for (Supply card : supply) {
			if (card.getCard() == id && card.getNum() != 0)
				return true;
		}
		return false;
	}

	//指定されたidのカードがあるかどうか(残数は無視)
	protected boolean existCardIgnoreLeft(int id) {
		Supply[] supply = _core.getSupply();
		for (Supply card : supply) {
			if (card.getCard() == id)
				return true;
		}
		return false;
	}

	//指定したidのカードを買う
	protected void buyCard(int id) {
		Supply[] supply = _core.getSupply();
		int i;
		//金量不足
		if (CardData.getInstence().getCardData(id).getCost() > _coin) {
			throw new java.lang.IllegalArgumentException(_coin + "金で" + id + "を買おうとしました");
		}
		//購入県内
		if (_buy == 0) {
			throw new java.lang.IllegalArgumentException("購入権がないのに" + id + "を買おうとしました");
		}
		//購入フェイズじゃない
		if (_phase != Params.PHASE_BUY) {
			throw new java.lang.IllegalArgumentException(_phase + "フェイズに" + id + "を買おうとしました");
		}

		for (i = 0; i < supply.length; i++) {
			if (supply[i].getCard() == id) {
				if (_core.buyCard(i) == false) {
					throw new java.lang.IllegalArgumentException(id + "は買えません");
				}
				_coin -= CardData.getInstence().getCardData(id).getCost();
				_buy--;
				_discard.add(CardData.getInstence().getCardData(id));
				_lastBuy.add(CardData.getInstence().getCardData(id));
				if (DEBUG)
					System.out.println("player" + _position + " buy:" + id);
				return;
			}
		}
		throw new java.lang.IllegalArgumentException("サプライに該当するカード" + id + "がありません");
	}


	//DomGUI用
	public String getPlayerInfoString(){
		String infoString = new String();
		infoString = "action:" + getAction() + "/coin:" + getCoin() + "/buy:" 
				+ getBuy() + "\n" + "play:";
		for (Card card : _play) {
			infoString += card.getName() + "/";
		}
		infoString += "\nduration:";
		
		for (Card card : _oldDuration) {
			infoString += card.getName() + "/";
		}
		for (Card card : _newDuration) {
			infoString += card.getName() + "/";
		}
		
		String top = null;
		if(_discard.size() == 0) top = "";
		else top = _discard.get(_discard.size() - 1).getName();
		
		infoString += "\ndeck:" + _deck.size() + "/discard:" + _discard.size() +  top
				+ "/hand:" + _hand.size() + "\nbuy:";
		
		for (Card card : _lastBuy) {
			infoString += card.getName() + "/";
		}
				
		return infoString;
	}
	
	//デバッグ用手札表示
	protected void showHand() {
		String hand = new String();
		for (Card card : _hand) {
			hand += card.getName() + "/";
		}
		if (DEBUG)
			System.out.println("player" + _position + " hand:" + hand);
	}

	//デバッグ用デッキ表示
	protected void showDeck() {
		String deck = new String();
		for (Card card : _deck) {
			deck += card.getName() + "/";
		}
		if (DEBUG)
			System.out.println("player" + _position + " deck:" + deck);
	}

	//デバッグ用デ表示
	protected void showDiscard() {
		String deck = new String();
		for (Card card : _discard) {
			deck += card.getName() + "/";
		}
		if (DEBUG)
			System.out.println("player" + _position + " discard:" + deck);
	}

	//デバッグ用デッキ表示
	protected void showPlay() {
		String deck = new String();
		for (Card card : _play) {
			deck += card.getName() + "/";
		}
		if (DEBUG)
			System.out.println("player" + _position + " play:" + deck);
	}

	//デバッグ用表示
	protected void showNewDuration() {
		String deck = new String();
		for (Card card : _newDuration) {
			deck += card.getName() + "/";
		}
		if (DEBUG)
			System.out.println("player" + _position + " newduration:" + deck);
	}

	//デバッグ用表示
	protected void showOldDuration() {
		String deck = new String();
		for (Card card : _oldDuration) {
			deck += card.getName() + "/";
		}
		if (DEBUG)
			System.out.println("player" + _position + " oldduration:" + deck);
	}

	//
	public void executeTurn() {
		if (DEBUG) {
			System.out.println("player" + _position + " turn start");
			showHand();
			showDeck();
			showDiscard();
			showOldDuration();
			showNewDuration();
		}
		
		_action = 1;
		_buy = 1;
		_coin = 0;
		_phase = Params.PHASE_ACTION;

		//持続効果の解決
		executeDurations();

		_lastBuy.clear();
		
		//抽象メソッドの呼び出し
		aiTurn();

		if (DEBUG) {
			System.out.println("player" + _position + " ai end");
			showHand();
			showDeck();
			showDiscard();
			showPlay();
			showOldDuration();
			showNewDuration();
		}

		cleanUp();

		if (DEBUG) {
			System.out.println("player" + _position + " turn end");
			showHand();
			showDeck();
			showDiscard();
			showOldDuration();
			showNewDuration();
		}
	}

	//AIターン
	abstract void aiTurn();

	//カード処理の判断 TODO 抽象関数推奨
	int judgeCard(int card) {
		switch (card) {
		//TODO 呪い屋敷銅貨銀貨以外の処理
		case Params.CARD_JUNKDEALER:
			int trashCard = -1;
			if (isCardInHand(Params.CARD_CURSE)) {
				trashCard = Params.CARD_CURSE;
			}
			else if (isCardInHand(Params.CARD_ESTATE)) {
				trashCard = Params.CARD_ESTATE;
			}
			else if (isCardInHand(Params.CARD_COPPER)) {
				trashCard = Params.CARD_COPPER;
			}
			else if (isCardInHand(Params.CARD_SILVER)) {
				trashCard = Params.CARD_SILVER;
			}
			for (int i = 0; i < _hand.size(); i++) {
				if (_hand.get(i).getId() == trashCard) {
					return i;
				}
			}
			break;
			
		case Params.CARD_CHAPEL:
			trashCard = -1;
			if (isCardInHand(Params.CARD_CURSE)) {
				trashCard = Params.CARD_CURSE;
			}
			else if (isCardInHand(Params.CARD_ESTATE)) {
				trashCard = Params.CARD_ESTATE;
			}
			else if (isCardInHand(Params.CARD_COPPER) && 
					(countCard(Params.CARD_GOLD) >= 1 || countCard(Params.CARD_SILVER) >= 2 || 
					countCard(Params.CARD_COPPER) >= 4 || (countCard(Params.CARD_SILVER) >= 1 && countCard(Params.CARD_COPPER) >= 2) ) ) {
				trashCard = Params.CARD_COPPER;
			}
			for (int i = 0; i < _hand.size(); i++) {
				if (_hand.get(i).getId() == trashCard) {
					return i;
				}
			}
			return -1;
		}
		return -1;
	}

	//持続カードの解決
	private void executeDurations() {
		for (Card duration : _newDuration) {
			_oldDuration.add(duration);
			switch (duration.getId()) {
			case Params.CARD_CARAVAN:
				drawCard(1);
				break;
			case Params.CARD_FISHINGVILLAGE:
				_coin++;
				_action++;
				break;
			case Params.CARD_LIGHTHOUSE:
				_coin++;
				break;
			case Params.CARD_WHARF:
				drawCard(2);
				_buy++;
				break;
			case Params.CARD_MARCHANTSHIP:
				_coin += 2;
				break;
			}
		}
		_newDuration.clear();
	}

	//クリーンアップフェイズ
	void cleanUp() {
		//手札のクリーンアップ
		for (Card hand : _hand) {
			_discard.add(hand);
		}
		_hand.clear();
		//場のクリーンアップ
		for (Card play : _play) {
			_discard.add(play);
		}
		_play.clear();
		//持続のクリーンアップ
		for (Card duration : _oldDuration) {
			_discard.add(duration);
		}
		_oldDuration.clear();

		//五枚引く
		drawCard(5);
	}

	//カードを引く
	private void drawCard(int num) {
		int i;
		for (i = 0; i < num; i++) {
			if (_deck.size() == 0) {
				if (_discard.size() == 0)
					return;
				for (Card card : _discard) {
					_deck.add(card);
				}
				_discard.clear();
				suffleDeck();
			}
			_hand.add(_deck.get(0));
			_deck.remove(0);
		}
	}

	private void suffleDeck() {
		Collections.shuffle(_deck);
	}

	//アクションフェイズ終了
	protected void endActionPhase() {
		_core.refreshGUI();
		
		//アクションフェイズでなければ強制終了
		if (_phase != Params.PHASE_ACTION) {
			throw new java.lang.IllegalArgumentException("フェイズ" + _phase + "にアクションフェイズの終了が呼ばれました");
		}
		_phase = Params.PHASE_TREASURE;
	}

	//財宝フェイズ終了
	protected void endTreasurePhase() {
		_core.refreshGUI();
		
		//アクションフェイズでなければ強制終了
		if (_phase != Params.PHASE_TREASURE) {
			throw new java.lang.IllegalArgumentException("フェイズ" + _phase + "に財宝フェイズの終了が呼ばれました");
		}
		_phase = Params.PHASE_BUY;
	}

	public ArrayList<Card> getHand() {
		return new ArrayList<Card>(_hand);
	}

	//カードの使用（カード番号で呼び出し）
	protected void useCard(Card card) {
		//カードを持ってなかったら強制終了
		if (!isCardInHand(card.getId())) {
			throw new java.lang.IllegalArgumentException("指定されたカード" + card.getId() + "を持っていません");
		}
		//アクションがなければ強制終了
		if (_phase == Params.PHASE_ACTION && _action == 0) {
			throw new java.lang.IllegalArgumentException("アクションがないのに" + card.getId() + "を使おうとしました");
		}
		//使用できないカードをプレイ
		if (!((_phase == Params.PHASE_ACTION && card.isAction()) || (_phase == Params.PHASE_TREASURE && card
				.isTreasure()))) {
			throw new java.lang.IllegalArgumentException("フェイズ" + _phase + "に" + card.getId() + "を使おうとしました");
		}
		//カードを使用場所へ移す
		for (Card hand : _hand) {
			if (hand.getId() == card.getId()) {
				//持続の場合
				if (hand.isDuration()) {
					_newDuration.add(hand);
					_hand.remove(hand);
				}
				else {
					_play.add(hand);
					_hand.remove(hand);
				}
				break;
			}
		}
		//アクション使用
		if (_phase == Params.PHASE_ACTION)
			_action--;

		//カードの効果発動
		switch (card.getId()) {
		case Params.CARD_COPPER:
			_coin++;
			break;
		case Params.CARD_SILVER:
			_coin += 2;
			break;
		case Params.CARD_GOLD:
			_coin += 3;
			break;
		case Params.CARD_SMITHY:
			if (DEBUG)
				System.out.println("player" + _position + " use smithy");
			drawCard(3);
			break;
		case Params.CARD_CARAVAN:
			drawCard(1);
			_action += 1;
			break;
		case Params.CARD_FISHINGVILLAGE:
			_action += 2;
			_coin++;
			break;
		case Params.CARD_LABORATRY:
			_action += 1;
			drawCard(2);
			break;
		case Params.CARD_MONEYLENDER:
			for (Card hand : _hand) {
				if (hand.getId() == Params.CARD_COPPER) {
					_hand.remove(hand);
					_coin += 3;
					break;
				}
			}
			break;
		case Params.CARD_WITCH:
			drawCard(2);
			_core.executeAttack(Params.CARD_WITCH, this);
			break;
		case Params.CARD_LIGHTHOUSE:
			_action++;
			_coin++;
			break;
		case Params.CARD_SEAHUG:
			_core.executeAttack(Params.CARD_SEAHUG, this);
			break;
		case Params.CARD_JUNKDEALER:
			drawCard(1);
			_action++;
			_coin++;
			_hand.remove(judgeCard(card.getId()));
			break;
		case Params.CARD_MARKET:
			drawCard(1);
			_action++;
			_coin++;
			_buy++;
			break;
		case Params.CARD_WOODCUTTER:
			_coin += 2;
			_buy ++;
			break;
		case Params.CARD_COUNCILROOM:
			drawCard(4);
			_buy ++;
			_core.executeAttack(Params.CARD_COUNCILROOM, this);
			break;
		case Params.CARD_FESTIVAL:
			_coin += 2;
			_action += 2;
			_buy ++;
			break;
		case Params.CARD_MOAT:
			drawCard(2);
			break;
		case Params.CARD_VILLAGE:
			drawCard(1);
			_action += 2;
			break;
		case Params.CARD_CHAPEL:
			for(int i = 0; i < 4; i ++){
				int trashCard = judgeCard(card.getId());
				if(trashCard == -1) break;
				else _hand.remove(trashCard);
			}
			break;
		case Params.CARD_GREATHALL:
			_action ++;
			drawCard(1);
			break;
		case Params.CARD_HAREM:
			_coin += 2;
			break;
		case Params.CARD_CONSPIRATOR:
			_coin += 2;
			if(_play.size() + _newDuration.size() >= 3){
				_action ++;
				drawCard(1);
			}
			break;
		case Params.CARD_WHARF:
			drawCard(2);
			_buy++;
			break;
		case Params.CARD_MARCHANTSHIP:
			_coin += 2;
			break;
		case Params.CARD_SHANTYTOWN:
			_action += 2;
			for (Card hand : _hand) {
				if(hand.isAction()) return;
			}
			drawCard(2);
			break;
		case Params.CARD_CUTPURSE:
			_coin += 2;
			_core.executeAttack(Params.CARD_CUTPURSE, this);
			break;
		case Params.CARD_EXPLORER:
			if(isCardInHand(Params.CARD_PROVINCE)){
				if (_core.buyCard(Params.SUPPLY_GOLD)) {
					_hand.add(0, CardData.getInstence().getCardData(Params.CARD_GOLD));
				}	
			}
			else{
				if (_core.buyCard(Params.SUPPLY_SILVER)) {
					_hand.add(0, CardData.getInstence().getCardData(Params.CARD_SILVER));
				}	
			}
			break;
		case Params.CARD_BAZZER:
			drawCard(1);
			_action += 2;
			_coin ++;
			break;
		case Params.CARD_WORKERSVILLAGE:
			drawCard(1);
			_action += 2;
			_buy ++;
			break;
		case Params.CARD_MONUMENT:
			_coin += 2;
			_vpToken ++;
			break;
		case Params.CARD_RABBLE:
			drawCard(3);
			_core.executeAttack(Params.CARD_RABBLE, this);
			break;
		}
	}

	//特定のカードを手札に持っているか
	protected boolean isCardInHand(int card) {
		for (Card hand : _hand) {
			if (hand.getId() == card)
				return true;
		}
		return false;
	}

	public int getCoin() {
		return _coin;
	}

	public int getAction() {
		return _action;
	}

	public int getBuy() {
		return _buy;
	}

	protected int getEndTurn() {
		return _core.getEndTurn();
	}

	protected int getTurn() {
		return _core.getTurn();
	}

	public void setCore(DomCore core) {
		_core = core;
	}

	protected int getMyCardCnt(int card) {
		return getPlayersCardCnt(this.getPosition(), card);
	}

	//指定したプレイヤーの指定したカードの所持枚数を返す
	protected int getPlayersCardCnt(int player, int card) {
		return _core.getPlayersCardCnt(player, card);
	}

	//手番
	protected int getPosition() {
		return _position;
	}

	public void addScore(double score) {
		_score += score;
		_games++;
	}

	public double getAveScore() {
		if (_games == 0)
			return -1;
		return (double) _score / _games;
	}

	//TODO　どこからでもよべちゃだめ
	public void executeAttack(int id) {
		//まず防げないものの処理
		switch (id) {
		case Params.CARD_COUNCILROOM:
			drawCard(1);
			break;
		}
		
		//灯台
		for (Card duration : _newDuration) {
			if (duration.getId() == Params.CARD_LIGHTHOUSE)
				return;
		}
		//掘
		for (Card hand : _hand) {
			if (hand.getId() == Params.CARD_MOAT)
				return;
		}
		
		switch (id) {
		case Params.CARD_WITCH:
			if (_core.buyCard(Params.SUPPLY_CURSE))
				_discard.add(CardData.getInstence().getCardData(Params.CARD_CURSE));
			break;
		case Params.CARD_SEAHUG:
			if (_core.buyCard(Params.SUPPLY_CURSE)) {
				if (_deck.size() == 0) {
					if (_discard.size() == 0) {

					}
					else {
						for (Card card : _discard) {
							_deck.add(card);
						}
						_discard.clear();
						suffleDeck();
					}
				}
				//山捨てる
				if (_deck.size() != 0) {
					_discard.add(_deck.get(0));
					_deck.remove(0);
				}
				_deck.add(0, CardData.getInstence().getCardData(Params.CARD_CURSE));
			}
			//_discard.add(CardData.getInstence().getCardData(Params.CARD_CURSE));
			break;
		case Params.CARD_CUTPURSE:
			for(Card hand: _hand){
				if(hand.getId() == Params.CARD_COPPER){
					_discard.add(hand);
					_hand.remove(hand);
					break;
				}
			}
			break;
		//TODO 戻す順番の処理
		case Params.CARD_RABBLE:
			//公開したカード
			ArrayList<Card> cards = new ArrayList<Card>();
			for(int i = 0; i < 3; i ++){
				if (_deck.size() == 0) {
					if (_discard.size() == 0) {
						
					}
					else {
						for (Card card : _discard) {
							_deck.add(card);
						}
						_discard.clear();
						suffleDeck();
					}
				}
				//山の一番上を公開リストに追加
				if (_deck.size() != 0) {
					cards.add(_deck.get(0));
					_deck.remove(0);
				}
			}
			//公開したカードを戻す
			for(Card card: cards){
				if(card.isAction() || card.isTreasure()){
					_discard.add(card);
				}
				else{
					_deck.add(0, card);
				}
			}
			break;
		}
	}
	
	public ArrayList<Card> getLastBuy(){
		return _lastBuy;
	}
	
	public boolean isDisabled(){
		//2シグマ部分で切っている
		if(_games >= 5 && getAveScore() < 2.5 - 4.6 / Math.sqrt(_games)){
			return true;
		}
		return false;
	}
}
