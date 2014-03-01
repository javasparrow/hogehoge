package jp.co.utdominion;

import jp.co.utdominion.gui.DomGUI;





public class Main {
	//Player[] _players = new Player[Params.GA_TOTAL_NUM];

	public static void main(String[] args) {
		//Gaの場合
		//GaSys ga = new GaSys();
		//ga.executeGa();
		
		
		new DomGUI().execute();
		
		
		//new LogLearning().execute();
		
		//鍛冶屋パラメータチューナの場合
		//SmithyParamTuner spt = new SmithyParamTuner();
		//spt.execute();
		
		//鍛冶屋規定属州枚数終了
	
		//LoadPlayer
		/*LoadPlayer load = new LoadPlayer();
		load.execute();*/
		
	}
}
