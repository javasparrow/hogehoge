package jp.co.utdominion;

public class Params {
	public final static int CARD_DUMMY = 0;
	public final static int CARD_COPPER = 1;
	public final static int CARD_SILVER = 2;
	public final static int CARD_GOLD = 3;
	public final static int CARD_ESTATE = 4;
	public final static int CARD_DUCHY = 5;
	public final static int CARD_PROVINCE = 6;
	public final static int CARD_CURSE = 7;

	public final static int CARD_MARKET = 18;
	//改築
	public final static int CARD_SMITHY = 8;
	public final static int CARD_MONEYLENDER = 12;
	public final static int CARD_WOODCUTTER = 19;
	public final static int CARD_COUNCILROOM = 20;
	//玉座の間
	public final static int CARD_LABORATRY = 11;
	//鉱山
	//工房
	//宰相
	//祝宴
	public final static int CARD_FESTIVAL = 21;
	//書庫
	//地下貯蔵庫
	public final static int CARD_GARDEN = 22;
	//泥棒
	//冒険者
	public final static int CARD_MOAT = 23;
	public final static int CARD_WITCH = 13;
	//密偵
	//民兵
	public final static int CARD_VILLAGE = 24;
	//役人
	public final static int CARD_CHAPEL = 25;

	public final static int CARD_GREATHALL = 26;
	//改良
	//仮面舞踏会
	//貴族
	public final static int CARD_CONSPIRATOR = 28;
	//交易場
	//鉱山の村
	public final static int CARD_DUKE = 16;
	//拷問人
	//詐欺師
	//執事
	//男爵
	//寵臣
	//偵察員
	//手先
	//鉄工所
	//銅細工師
	//中庭
	//願いの井戸
	public final static int CARD_HAREM = 27;
	//破壊工作員
	//橋
	//秘密の部屋
	public final static int CARD_SHANTYTOWN = 31;
	//貢物
	public final static int CARD_SEAHUG = 15;
	//海賊船
	public final static int CARD_FISHINGVILLAGE = 10;
	public final static int CARD_CUTPURSE = 32;
	//原住民
	//航海市
	//策士
	//島
	public final static int CARD_MARCHANTSHIP = 30;
	//真珠取り
	//前哨地
	//倉庫
	//大使
	public final static int CARD_CARAVAN = 9;
	//宝の地図
	public final static int CARD_EXPLORER = 33;
	//停泊所
	public final static int CARD_LIGHTHOUSE = 14;
	public final static int CARD_BAZZER = 34;
	//引き上げ水夫
	public final static int CARD_WHARF = 29;
	//宝物庫
	//密輸人
	//見張り
	//幽霊船
	//抑留

	//闇市場
	//公使
	//へそくり
	//薬師
	//賢者の石
	//ゴーレム
	//支配
	//大学
	//使い魔
	//弟子
	//年始の泉
	//ブドウ園
	//変性
	//ポーション
	//薬草商
	//錬金術師

	//石切り場
	//大市場
	//会計所
	//隠し財産
	//拡張
	public final static int CARD_MONUMENT = 36;
	//宮廷
	//行商人
	//玉璽
	//銀行
	//禁制品
	//交易路
	//護符
	//司教
	//借金
	//植民地
	//造幣所
	public final static int CARD_RABBLE = 37;
	//鍛造
	//投機
	//都市
	//ならずもの
	//八金貨
	//望楼
	//保管庫
	//香具師
	public final static int CARD_WORKERSVILLAGE = 35;
	//移動動物園
	//馬商人
	//占い師
	//王冠
	//王女
	//金貨袋
	//再建
	//収穫
	//狩猟団
	//村落
	//道化師
	//農村
	//馬上槍時代
	//品評会
	//角笛
	//魔女娘
	//名馬
	//郎党
	//オアシス
	//街道
	//開発
	//画策
	//官吏
	//義賊
	//厩舎
	//岐路
	//愚者の黄金
	//交易人
	//侯爵夫人
	//香辛料商人
	//坑道
	//国境の村
	//シルクロード
	//神託
	//大使館
	//地図職人
	//値切りや
	//農地
	//不正利得
	//辺境伯
	//埋蔵金
	//宿屋
	//遊牧民の野営地
	//よろずや
	//威嚇損
	//総督

	//青空
	//市場跡地
	//隠遁者
	//金物商
	//狩場
	//騎士
	//偽造通貨
	//救貧院
	//狂人
	//狂信者
	//共同墓地
	//吟遊詩人
	//草茂屋敷
	public final static int CARD_JUNKDEALER = 17;






	//第一段階のため35に固定hontouha37
	public final static int CARD_MAX_NUM = 35;

	public final static int PLAYER_NUM = 4;

	public final static int PHASE_ACTION = 0;
	public final static int PHASE_TREASURE = 1;
	public final static int PHASE_BUY = 2;

	//サプライ
	public final static int SUPPLY_COPPER = 10;
	public final static int SUPPLY_SILVER = 11;
	public final static int SUPPLY_GOLD = 12;
	public final static int SUPPLY_ESTATE = 13;
	public final static int SUPPLY_DUCHY = 14;
	public final static int SUPPLY_PROVINCE = 15;
	public final static int SUPPLY_CURSE = 16;

	//タイプ
	public final static int TYPE_ACTION = 0x01;
	public final static int TYPE_TREASURE = 0x02;
	public final static int TYPE_VICTRY = 0x04;

	//GA
	//エリート
	public final static int GA_ELETE_NUM = 100;
	//エリートのうち、一定確率で死ぬ物の数
	public final static int GA_ALIVE_NUM = 98;

	//次世代に残る数
	public final static int GA_GO_NEXT_NUM = 2;

	public final static int GA_TOTAL_NUM = 400;

	public final static int GA_TOTAL_NUM_1 = 100;
	public final static int GA_TOTAL_NUM_2 = 200;
	public final static int GA_TOTAL_NUM_3 = 300;
	public final static int GA_TOTAL_NUM_4 = 400;

	//GA選択確率
	public final static double GA_FIRST_PROBABILITY = 0.99;
	public final static double GA_REDUCE_PROBABILITY = 0.999;

	//初手
	public final static int HAND_25 = 1;
	public final static int HAND_34 = 2;
	public final static int HAND_RANDOM = 3;

	//ログ学習のペナルティ
	public final static double LOG_UNCORRECT_PENALTY = 0.6;
	//ログ学習の距離の最大値（ペナルティ含む）
	public static final double LOG_DISTANCE_MAX = 2.1;

	//0201 UltimateMode
	//GA
	/*	//エリート
		public final static int GA_ELETE_NUM = 1000;
		//エリートのうち、一定確率で死ぬ物の数
		public final static int GA_ALIVE_NUM = 950;

		//次世代に残る数
		public final static int GA_GO_NEXT_NUM = 10;

		public final static int GA_TOTAL_NUM = 5000;

		//GA選択確率
		public final static double GA_FIRST_PROBABILITY = 0.99;
		public final static double GA_REDUCE_PROBABILITY = 0.999;*/

}
