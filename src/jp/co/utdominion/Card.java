package jp.co.utdominion;

public class Card {
	Integer _card;
	Integer _cost;
	Integer _type;
	Integer _victry;
	boolean _isDuration;
	String _name;
	String _imagePath;
	
	Card(int card, int cost, int type, int victry, boolean isDuration, String name, String imagePath){
		_card = card;
		_cost = cost;
		_type = type;
		_victry = victry;
		_isDuration = isDuration;
		_name = name;
		_imagePath = imagePath;
	}
	
	public String getImagePath(){
		return _imagePath;
	}
	
	public Integer getId(){
		return _card;
	}
	
	public Integer getCost(){
		return _cost;
	}
	
	public Integer getVictry(Player player){
		//特殊勝利店の処理
		if(_card == Params.CARD_DUKE){
			return player.getMyCardCnt(Params.CARD_DUCHY);
		}
		if(_card == Params.CARD_GARDEN){
			return (int)(player.countCardAll() / 10);
		}
		return _victry;
	}
	
	public boolean isAction(){
		return (_type & Params.TYPE_ACTION) != 0;
	}
	
	public boolean isTreasure(){
		return (_type & Params.TYPE_TREASURE) != 0;
	}
	
	public boolean isVictry(){
		return (_type & Params.TYPE_VICTRY) != 0;
	}
	
	public boolean isDuration(){
		return _isDuration;
	}
	
	public String getName(){
		return _name;
	}
}
