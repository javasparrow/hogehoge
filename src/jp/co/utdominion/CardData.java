package jp.co.utdominion;

import java.util.HashMap;

public class CardData {
	
	HashMap<Integer, Card> _data = new HashMap<Integer, Card>();
	
	private CardData(){
		_data.put(Params.CARD_COPPER,			new Card(Params.CARD_COPPER,		 0, Params.TYPE_TREASURE, 0, false, "銅", "/cardImage/Copper.png"));
		_data.put(Params.CARD_SILVER,			new Card(Params.CARD_SILVER,		 3, Params.TYPE_TREASURE, 0, false, "銀", "/cardImage/Silver.png"));
		_data.put(Params.CARD_GOLD, 			new Card(Params.CARD_GOLD,			 6, Params.TYPE_TREASURE, 0, false, "金", "/cardImage/Gold.png"));
		_data.put(Params.CARD_ESTATE,			new Card(Params.CARD_ESTATE,		 2, Params.TYPE_VICTRY,	  1, false, "屋", "/cardImage/Estate.png"));
		_data.put(Params.CARD_DUCHY,			new Card(Params.CARD_DUCHY,			 5, Params.TYPE_VICTRY,	  3, false, "領", "/cardImage/Duchy.png"));
		_data.put(Params.CARD_PROVINCE,			new Card(Params.CARD_PROVINCE,		 8, Params.TYPE_VICTRY,	  6, false, "属", "/cardImage/Province.png"));
		_data.put(Params.CARD_SMITHY,			new Card(Params.CARD_SMITHY,		 4, Params.TYPE_ACTION,	  0, false, "鍛冶", "/cardImage/Smithy.png"));
		_data.put(Params.CARD_CARAVAN,			new Card(Params.CARD_CARAVAN,		 4, Params.TYPE_ACTION,	  0, true,  "隊", "/cardImage/Caravan.png"));
		_data.put(Params.CARD_FISHINGVILLAGE, 	new Card(Params.CARD_FISHINGVILLAGE, 3, Params.TYPE_ACTION,	  0, true,  "漁", "/cardImage/FishingVillage.png"));
		_data.put(Params.CARD_LABORATRY,		new Card(Params.CARD_LABORATRY,		 5, Params.TYPE_ACTION,	  0, false, "研", "/cardImage/Laboratory.png"));
		_data.put(Params.CARD_MONEYLENDER,		new Card(Params.CARD_MONEYLENDER,	 4, Params.TYPE_ACTION,	  0, false, "貸", "/cardImage/MoneyLender.png"));
		_data.put(Params.CARD_WITCH,			new Card(Params.CARD_WITCH,			 5, Params.TYPE_ACTION,	  0, false, "魔", "/cardImage/Witch.png"));
		_data.put(Params.CARD_LIGHTHOUSE,		new Card(Params.CARD_LIGHTHOUSE,	 2, Params.TYPE_ACTION,	  0, true,  "灯", "/cardImage/Lighthouse.png"));
		_data.put(Params.CARD_SEAHUG,			new Card(Params.CARD_SEAHUG,		 4, Params.TYPE_ACTION,	  0, false, "婆", "/cardImage/Seahag.png"));
		_data.put(Params.CARD_DUKE,				new Card(Params.CARD_DUKE,			 5, Params.TYPE_VICTRY,	  0, false, "爵", "/cardImage/Duke.png"));
		_data.put(Params.CARD_JUNKDEALER,		new Card(Params.CARD_JUNKDEALER,	 5, Params.TYPE_ACTION,	  0, false, "屑", "/cardImage/Junkdealer.PNG"));
		_data.put(Params.CARD_MARKET,			new Card(Params.CARD_MARKET,	 	 5, Params.TYPE_ACTION,	  0, false, "市", "/cardImage/Market.png"));
		_data.put(Params.CARD_WOODCUTTER,		new Card(Params.CARD_WOODCUTTER,	 3, Params.TYPE_ACTION,	  0, false, "木", "/cardImage/Woodcutter.png"));
		_data.put(Params.CARD_FESTIVAL,			new Card(Params.CARD_FESTIVAL,		 5, Params.TYPE_ACTION,	  0, false, "祭", "/cardImage/Festival.png"));
		_data.put(Params.CARD_COUNCILROOM,		new Card(Params.CARD_COUNCILROOM,	 5, Params.TYPE_ACTION,	  0, false, "議", "/cardImage/CouncilRoom.png"));
		_data.put(Params.CARD_GARDEN,			new Card(Params.CARD_GARDEN,		 4, Params.TYPE_VICTRY,	  0, false, "庭", "/cardImage/Gardens.png"));
		_data.put(Params.CARD_MOAT,				new Card(Params.CARD_MOAT,			 2, Params.TYPE_ACTION,	  0, false, "掘", "/cardImage/Moat.png"));
		_data.put(Params.CARD_VILLAGE,			new Card(Params.CARD_VILLAGE,		 3, Params.TYPE_ACTION,	  0, false, "村", "/cardImage/Village.png"));
		_data.put(Params.CARD_CHAPEL,			new Card(Params.CARD_CHAPEL,		 2, Params.TYPE_ACTION,	  0, false, "礼", "/cardImage/Chapel.png"));
		_data.put(Params.CARD_GREATHALL,		new Card(Params.CARD_GREATHALL,		 3, Params.TYPE_ACTION  | Params.TYPE_VICTRY,	  1, false, "大", "/cardImage/GreatHall.png"));
		_data.put(Params.CARD_HAREM,			new Card(Params.CARD_HAREM,			 6, Params.TYPE_VICTRY | Params.TYPE_TREASURE,	  2, false, "ハ", "/cardImage/Harem.png"));
		_data.put(Params.CARD_CONSPIRATOR,		new Card(Params.CARD_CONSPIRATOR,	 4, Params.TYPE_ACTION,	  0, false, "謀", "/cardImage/Conspirator.png"));
		_data.put(Params.CARD_WHARF,			new Card(Params.CARD_WHARF,	 		 5, Params.TYPE_ACTION,	  0, true,  "船", "/cardImage/Wharf.png"));
		_data.put(Params.CARD_MARCHANTSHIP,		new Card(Params.CARD_MARCHANTSHIP,	 5, Params.TYPE_ACTION,	  0, true,  "商", "/cardImage/MerchantShip.png"));
		_data.put(Params.CARD_SHANTYTOWN,		new Card(Params.CARD_SHANTYTOWN,	 3, Params.TYPE_ACTION,	  0, false, "貧", "/cardImage/Shantytown.png"));
		_data.put(Params.CARD_CUTPURSE,			new Card(Params.CARD_CUTPURSE,		 4, Params.TYPE_ACTION,	  0, false, "巾", "/cardImage/Cutpurse.png"));
		_data.put(Params.CARD_EXPLORER,			new Card(Params.CARD_EXPLORER,		 5, Params.TYPE_ACTION,	  0, false, "探", "/cardImage/Explorer.png"));
		_data.put(Params.CARD_BAZZER,			new Card(Params.CARD_BAZZER,		 5, Params.TYPE_ACTION,	  0, false, "バ", "/cardImage/Bazaar.png"));
		_data.put(Params.CARD_WORKERSVILLAGE,	new Card(Params.CARD_WORKERSVILLAGE, 4, Params.TYPE_ACTION,	  0, false, "労", "/cardImage/WorkersVillage.png"));
		_data.put(Params.CARD_MONUMENT		,	new Card(Params.CARD_MONUMENT, 		 4, Params.TYPE_ACTION,	  0, false, "記", "/cardImage/Monument.png"));
		_data.put(Params.CARD_RABBLE		,	new Card(Params.CARD_RABBLE, 		 5, Params.TYPE_ACTION,	  0, false, "衆", "/cardImage/Rabble.png"));
		
		_data.put(Params.CARD_CURSE,			new Card(Params.CARD_CURSE,			 0, 0,				  	 -1, false, "呪", "/cardImage/Curse.png"));
		
		
		_data.put(Params.CARD_DUMMY,			new Card(Params.CARD_DUMMY,			 100, 0, 0, false, "？", "/cardImage/Copper.png"));
	}
	
	private static final CardData _instance = new CardData();

	public static CardData getInstence(){
		return _instance;
	}
	
	public Card getCardData(int id){
		return _data.get(id);
	}
}
