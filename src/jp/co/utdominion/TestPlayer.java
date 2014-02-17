package jp.co.utdominion;

import java.util.ArrayList;

public class TestPlayer extends Player{

	private static final boolean DEBUG = false;
	
	//決定パラメータ
	double _params[];

	TestPlayer(DomCore core, double params[]) {
		super(core);
		_params = params;
	}

	@Override
	void aiTurn() {
		ArrayList<Card> hand = getHand();
		//隊商があったら使う
		for (Card card : hand) {
			if (card.getId() == Params.CARD_CARAVAN) {
				if (getAction() > 0){
					useCard(card);
					//TODO これやっていいか怪しい
					hand = getHand();
				}
			}
		}
		
		//鍛冶屋があったら使う
				for (Card card : hand) {
					if (card.getId() == Params.CARD_SMITHY) {
						if (getAction() > 0){
							useCard(card);
							break;
						}
					}
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

		if (getCoin() >= 6 && existCard(Params.CARD_GOLD) && getCardLeftUseNum() >= _params[0]) {
			buyCard(Params.CARD_GOLD);
			return;
		}
		if (getCoin() >= 8 && existCard(Params.CARD_PROVINCE)) {
			buyCard(Params.CARD_PROVINCE);
			return;
		}
		if (getCoin() >= 5 && existCard(Params.CARD_DUCHY) && getCardLeftUseNum() <= _params[1]) {
			buyCard(Params.CARD_DUCHY);
			return;
		}
		if (getCoin() >= 6 && existCard(Params.CARD_GOLD)) {
			buyCard(Params.CARD_GOLD);
			return;
		}
		if (getCoin() >= 5 && existCard(Params.CARD_DUCHY) && getCardLeftUseNum() <= _params[2]) {
			buyCard(Params.CARD_DUCHY);
			return;
		}
		if (getCoin() >= 2 && existCard(Params.CARD_ESTATE) && getCardLeftUseNum() <= _params[3]) {
			buyCard(Params.CARD_ESTATE);
			return;
		}
		//２週目鍛冶屋
		/*if (getCoin() >= 4 && existCard(Params.CARD_SMITHY) && countCard(Params.CARD_SMITHY) < 2) {
			buyCard(Params.CARD_SMITHY);
			return;
		}*/
		//３週目鍛冶屋
		if (getCoin() >= 4 && existCard(Params.CARD_SMITHY) && countCard(Params.CARD_SMITHY) < 2 && (countCardAll() >= 14 || (countCardAll() >= 13 && countDeckCard() >= 10))) {
			buyCard(Params.CARD_SMITHY);
			return;
		}
		if (getCoin() >= 4 && existCard(Params.CARD_SMITHY) && countCard(Params.CARD_SMITHY) < 1) {
			buyCard(Params.CARD_SMITHY);
			return;
		}
		if (getCoin() >= 4 && existCard(Params.CARD_CARAVAN)) {
			buyCard(Params.CARD_CARAVAN);
			return;
		}
		if (getCoin() >= 3 && existCard(Params.CARD_SILVER)) {
			buyCard(Params.CARD_SILVER);
			return;
		}
		if (getCoin() >= 2 && existCard(Params.CARD_ESTATE) && getCardLeftUseNum() <= _params[4]) {
			buyCard(Params.CARD_ESTATE);
			return;
		}
	}
	
	//今買ったカードが終了までに何回回ってくるかを計算する関数
	//改善の余地あり（ドローを考慮に入れていない）
	double getCardLeftUseNum(){
		int count = countDeckCard();
		int left = (getEndTurn() - getTurn()) * 5 - count;
		int all = countCardAll();
		double result = 0;
		if(left < 0) return 0;
		while(true){
			if(left < all) return result + (double)left/all;
			left -= all;
			result ++;
		}
	}

	class CardPoint {
		public double point;
		public int card;

		CardPoint(double mpoint, int mcard) {
			point = mpoint;
			card = mcard;
		}
	}
}
