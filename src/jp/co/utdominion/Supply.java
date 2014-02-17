package jp.co.utdominion;

public class Supply {
	int _card;
	int _num;
	Supply(int card, int num){
		_card = card;
		_num = num;
	}
	public int getCard(){
		return _card;
	}
	public int getNum(){
		return _num;
	}
	public boolean buy(){
		if(_num == 0) return false;
		_num --;
		return true;
	}
}
