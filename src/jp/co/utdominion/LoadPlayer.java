package jp.co.utdominion;


public class LoadPlayer {
	//プレイヤー
	Player[] _players = new Player[4];
	//世代数
	int _generation = 0;
	//試合数
	long _totalMatch = 0;

	private static final boolean DEBUG = false;

	LoadPlayer() {

	}

	private boolean dontHave2(int[] target, int num) {
		int counter = 0;
		int i;
		for (i = 0; i < target.length; i++) {
			if (target[i] == num)
				counter++;
		}
		if (counter >= 2)
			return false;
		else
			return true;
	}

	public void execute() {
		int i, n, j, k;

		DomCore core = new DomCore();
		//TODO 一時凍結
		/*for (n = 0; n < 4; n++) {
			_players[n] = new GaSysPlayer(core, 
					GaUtils.createMatFromFile("C:\\Users\\denjo\\Documents\\domiAI\\othersDeckMat.txt"),
					GaUtils.createMatFromFile("C:\\Users\\denjo\\Documents\\domiAI\\myDeckMat.txt"),
					GaUtils.createMatFromFile("C:\\Users\\denjo\\Documents\\domiAI\\supplyMat.txt"));
		}*/
		Player[] pl = { new HumanPlayer(core), _players[1], _players[2], _players[3] };
		Result re = core.executeGame(pl, 100, Params.HAND_RANDOM, null);
		re.printResult();
		
	}
}
