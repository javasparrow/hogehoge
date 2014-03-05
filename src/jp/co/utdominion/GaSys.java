package jp.co.utdominion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

public class GaSys {
	//プレイヤー
	Player[] _players = new Player[Params.GA_TOTAL_NUM];
	//世代数
	int _generation = 0;
	//試合数
	long _totalMatch = 0;

	private static final boolean DEBUG = false;
	private static final boolean LOADFROMFILE = true;
	private static final String LOADFILEDIR = "C:\\Users\\denjo\\Documents\\domiAI\\domAI.txt";

	GaSys() {

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

	PrintWriter pw = null;

	HashSet<Integer> disableSet = new HashSet<Integer>();
	HashSet<Integer> playingSet = new HashSet<Integer>();

	public void executeGa() {
		File file = new File("C:/dominion/log2.txt");

		try {
			file.createNewFile();
			pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		int i, n, j, k;

		for (i = 0; i < 1000; i++) {
			disableSet = new HashSet<Integer>();
			playingSet = new HashSet<Integer>();
			_generation++;
			makePlayers(null);
			System.out.println("generation" + i);
			pw.println("generation" + i);
			_totalMatch = 0;
			class GaThread extends Thread {
				public void run() {

					int localMatch = 0;
					
					while (_totalMatch < 10000) {

						//非同期処理によりtotalMatchが崩れるかもしれないがやりすぎてもいいので問題ない
						_totalMatch++;
						localMatch ++;
						DomCore core = new DomCore();
						int[] plNums = new int[4];
						//同時に２試合をプレイヤーがすると非常に困るのでplayngSetの参照が衝突しないようにする
						synchronized (playingSet) {
							for (int n = 0; n < 4; n++) {
								plNums[n] = (int) (Math.random() * Params.GA_TOTAL_NUM);
								while (!dontHave2(plNums, plNums[n]) || disableSet.contains(new Integer(plNums[n]))
										|| playingSet.contains(new Integer(plNums[n]))) {
									plNums[n] = (int) (Math.random() * Params.GA_TOTAL_NUM);
								}
							}
							for (int n = 0; n < 4; n++) {
								_players[plNums[n]].setCore(core);
								playingSet.add(plNums[n]);
								if (DEBUG)
									System.out.println("select player" + plNums[n]);
							}
						}
						Player[] pl = { _players[plNums[0]], _players[plNums[1]], _players[plNums[2]],
								_players[plNums[3]] };
						Result re = core.executeGame(pl, 50, Params.HAND_RANDOM, null);
						synchronized (playingSet) {
							for (int n = 0; n < 4; n++) {
								playingSet.remove(plNums[n]);
							}
						}
						if (re == null)
							continue;
						for (int n = 0; n < 4; n++) {
							if (disableSet.size() < Params.GA_TOTAL_NUM / 2 && _players[plNums[n]].isDisabled()) {
								disableSet.add(plNums[n]);
								System.out.println("disabled player:" + disableSet.size());
							}
						}
						if (localMatch % 1000 == 0) {
							synchronized (pw) {
								System.out.println("playing" + playingSet.size());
								System.out.println("match" + _totalMatch);
								pw.println("match" + _totalMatch);
								re.printResult(pw);
							}
						}
					}
					System.out.println("Thread End");
				}
			}
			GaThread thread1 = new GaThread();
			GaThread thread2 = new GaThread();
			GaThread thread3 = new GaThread();
			GaThread thread4 = new GaThread();
			GaThread thread5 = new GaThread();
			GaThread thread6 = new GaThread();

			thread1.start();
			thread2.start();
			thread3.start();
			thread4.start();
			thread5.start();
			thread6.start();
			

			try {
				thread1.join();
				thread2.join();
				thread3.join();
				thread4.join();
				thread5.join();
				thread6.join();
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			pw.flush();
		}

		makePlayers(null);
		pw.close();
	}

	private void makePlayers(DomCore core) {

		ArrayList<Player> parentList = new ArrayList<Player>();

		if (_generation == 1 && LOADFROMFILE == false) {
			//各プレイヤーの初期化
			int i, n, j;
			for (i = 0; i < Params.GA_TOTAL_NUM; i++) {
				double[][][] mat1 = new double[Params.CARD_MAX_NUM][1][Params.CARD_MAX_NUM];
				double[][][] mat2 = new double[Params.CARD_MAX_NUM][1][Params.CARD_MAX_NUM];
				double[][][] mat3 = new double[Params.CARD_MAX_NUM][1][Params.CARD_MAX_NUM];
				double[][][] mat4 = new double[Params.CARD_MAX_NUM][1][1];
				double[][][] mat5 = new double[Params.CARD_MAX_NUM][1][1];
				double[][][] mat6 = new double[Params.CARD_MAX_NUM][1][1];
				double[][][] mat7 = new double[Params.CARD_MAX_NUM][1][1];
				for (n = 0; n < Params.CARD_MAX_NUM; n++) {
					for (j = 0; j < Params.CARD_MAX_NUM; j++) {
						mat1[n][0][j] = (Math.random() - 0.5) / 10; //other
						mat2[n][0][j] = (Math.random() - 0.5) / 2; //my
						mat3[n][0][j] = (Math.random() - 0.5) * 2; //sup
					}
					mat4[n][0][0] = (Math.random() - 0.5) / 5;
					mat5[n][0][0] = (Math.random() - 0.5) / 2;
					mat6[n][0][0] = (Math.random() - 0.5) / 6;
					mat7[n][0][0] = (Math.random() - 0.5) / 5;
				}
				_players[i] = new GaSysPlayer(core, mat1, mat2, mat3, mat4, mat5, mat6, mat7, false);
			}
		}
		else {
			int disNum = 0;
			int i, n, j;

			if (LOADFROMFILE && _generation == 1) {
				GaSysPlayer p = GaUtils.createPlayerFromFile(LOADFILEDIR, false);
				for (i = 0; i < 2; i++) {
					parentList.add(new GaSysPlayer(core, p.getOthersDeckMat(), p.getMyDeckMat(), p.getsupplyMat()
							, p.getDeckCntMat(), p.getPositionMat(), p.getEndTurnMat(), p.getVictryDiffMat(), false));
				}

				//親の初期化
				//初期収束を避けるため優秀な奴で埋めるのはやめておく
				for (i = 2; i < Params.GA_PARENT_NUM; i++) {
					double[][][] mat1 = new double[Params.CARD_MAX_NUM][1][Params.CARD_MAX_NUM];
					double[][][] mat2 = new double[Params.CARD_MAX_NUM][1][Params.CARD_MAX_NUM];
					double[][][] mat3 = new double[Params.CARD_MAX_NUM][1][Params.CARD_MAX_NUM];
					double[][][] mat4 = new double[Params.CARD_MAX_NUM][1][1];
					double[][][] mat5 = new double[Params.CARD_MAX_NUM][1][1];
					double[][][] mat6 = new double[Params.CARD_MAX_NUM][1][1];
					double[][][] mat7 = new double[Params.CARD_MAX_NUM][1][1];
					for (n = 0; n < Params.CARD_MAX_NUM; n++) {
						for (j = 0; j < Params.CARD_MAX_NUM; j++) {
							mat1[n][0][j] = (Math.random() - 0.5) / 10; //other
							mat2[n][0][j] = (Math.random() - 0.5) / 2; //my
							mat3[n][0][j] = (Math.random() - 0.5) * 2; //sup
						}
						mat4[n][0][0] = (Math.random() - 0.5) / 5;
						mat5[n][0][0] = (Math.random() - 0.5) / 2;
						mat6[n][0][0] = (Math.random() - 0.5) / 6;
						mat7[n][0][0] = (Math.random() - 0.5) / 5;
					}
					parentList.add(new GaSysPlayer(core, mat1, mat2, mat3, mat4, mat5, mat6, mat7, false));
				}
			}
			else {

				for (i = 0; i < Params.GA_TOTAL_NUM; i++) {
					if (_players[i].isDisabled())
						disNum++;
				}
				System.out.println("disNum is" + disNum);

				class MyComparator implements Comparator<Player> {
					@Override
					public int compare(Player o1, Player o2) {
						if (o1.getAveScore() == o2.getAveScore())
							return 0;
						return o1.getAveScore() > o2.getAveScore() ? -1 : 1;
					}
				}
				Arrays.sort(_players, new MyComparator());

				//必ず生き残る個体
				for (i = 0; i < Params.GA_PARENT_NUM - Params.GA_ALIVE_NUM; i++) {
					System.out.println("elete" + i + " score:" + _players[i].getAveScore());
					pw.println("elete" + i + " score:" + _players[i].getAveScore());

					GaSysPlayer p = ((GaSysPlayer) _players[i]);
					parentList.add(new GaSysPlayer(core, p.getOthersDeckMat(), p.getMyDeckMat(), p.getsupplyMat()
							, p.getDeckCntMat(), p.getPositionMat(), p.getEndTurnMat(), p.getVictryDiffMat(), false));

					String str = new String();
					System.out.println("othersDeckMat");
					pw.println("othersDeckMat");
					for (n = 0; n < Params.CARD_MAX_NUM; n++) {
						str = "";
						for (j = 0; j < Params.CARD_MAX_NUM; j++) {
							str += ((GaSysPlayer) _players[i]).getOthersDeckMat()[n][0][j] + " ";
						}
						System.out.println(str);
						pw.println(str);
					}
					System.out.println("MyDeckMat");
					pw.println("MyDeckMat");
					for (n = 0; n < Params.CARD_MAX_NUM; n++) {
						str = "";
						for (j = 0; j < Params.CARD_MAX_NUM; j++) {
							str += ((GaSysPlayer) _players[i]).getMyDeckMat()[n][0][j] + " ";
						}
						System.out.println(str);
						pw.println(str);
					}
					System.out.println("supplyMat");
					pw.println("supplyMat");
					for (n = 0; n < Params.CARD_MAX_NUM; n++) {
						str = "";
						for (j = 0; j < Params.CARD_MAX_NUM; j++) {
							str += ((GaSysPlayer) _players[i]).getsupplyMat()[n][0][j] + " ";
						}
						System.out.println(str);
						pw.println(str);
					}
					System.out.println("deckMat");
					pw.println("deckCntMat");
					str = "";
					for (j = 0; j < Params.CARD_MAX_NUM; j++) {
						str += ((GaSysPlayer) _players[i]).getDeckCntMat()[j][0][0] + " ";
					}
					System.out.println(str);
					pw.println(str);

					System.out.println("posMat");
					pw.println("posMat");
					str = "";
					for (j = 0; j < Params.CARD_MAX_NUM; j++) {
						str += ((GaSysPlayer) _players[i]).getPositionMat()[j][0][0] + " ";
					}
					System.out.println(str);
					pw.println(str);

					System.out.println("endMat");
					pw.println("endMat");
					str = "";
					for (j = 0; j < Params.CARD_MAX_NUM; j++) {
						str += ((GaSysPlayer) _players[i]).getEndTurnMat()[j][0][0] + " ";
					}
					System.out.println(str);
					pw.println(str);

					System.out.println("vDiffMat");
					pw.println("vDiffMat");
					str = "";
					for (j = 0; j < Params.CARD_MAX_NUM; j++) {
						str += ((GaSysPlayer) _players[i]).getVictryDiffMat()[j][0][0] + " ";
					}
					System.out.println(str);
					pw.println(str);
				}

				//一定確率で死ぬ個体
				int currentPlayerCursor = Params.GA_PARENT_NUM - Params.GA_ALIVE_NUM;
				double currentProbability = Params.GA_FIRST_PROBABILITY;
				for (i = Params.GA_PARENT_NUM - Params.GA_ALIVE_NUM; i < Params.GA_PARENT_NUM; i++) {
					while (true) {
						if (!_players[currentPlayerCursor].isDisabled() && Math.random() < currentProbability) {
							/*_players[i] = new GaSysPlayer(core,
									((GaSysPlayer) _players[currentPlayerCursor]).getOthersDeckMat()
									, ((GaSysPlayer) _players[currentPlayerCursor]).getMyDeckMat(),
									((GaSysPlayer) _players[currentPlayerCursor]).getsupplyMat());*/

							GaSysPlayer p = ((GaSysPlayer) _players[currentPlayerCursor]);

							parentList.add(new GaSysPlayer(core,
									p.getOthersDeckMat(), p.getMyDeckMat(), p.getsupplyMat(),
									p.getDeckCntMat(), p.getPositionMat(), p.getEndTurnMat(), p.getVictryDiffMat(), false));

							currentPlayerCursor++;
							currentProbability *= Params.GA_REDUCE_PROBABILITY;
							break;
						}
						currentPlayerCursor++;
						//WARNING! でっちあげ例外処理
						if (currentPlayerCursor == _players.length) {
							currentPlayerCursor = Params.GA_PARENT_NUM - Params.GA_ALIVE_NUM;
							System.out.println("warning! failed");
						}
					}
				}

			}
			//永遠の命を与えられるエリート
			for (i = 0; i < Params.GA_ELETE_NUM; i++) {
				GaSysPlayer p = ((GaSysPlayer) parentList.get(i));
				_players[i] = new GaSysPlayer(core,
						p.getOthersDeckMat(), p.getMyDeckMat(), p.getsupplyMat(),
						p.getDeckCntMat(), p.getPositionMat(), p.getEndTurnMat(), p.getVictryDiffMat(), false);
			}

			//子供の個体
			for (i = Params.GA_ELETE_NUM; i < Params.GA_TOTAL_NUM; i++) {
				double[][][] mat1 = new double[Params.CARD_MAX_NUM][1][Params.CARD_MAX_NUM];
				double[][][] mat2 = new double[Params.CARD_MAX_NUM][1][Params.CARD_MAX_NUM];
				double[][][] mat3 = new double[Params.CARD_MAX_NUM][1][Params.CARD_MAX_NUM];
				double[][][] mat4 = new double[Params.CARD_MAX_NUM][1][1];
				double[][][] mat5 = new double[Params.CARD_MAX_NUM][1][1];
				double[][][] mat6 = new double[Params.CARD_MAX_NUM][1][1];
				double[][][] mat7 = new double[Params.CARD_MAX_NUM][1][1];
				//GA
				int rnd = (int) (Math.random() * 30);
				int target;
				switch (rnd) {
				//完全ランダム
				case 17:
					for (n = 0; n < Params.CARD_MAX_NUM; n++) {
						for (j = 0; j < Params.CARD_MAX_NUM; j++) {
							mat1[n][0][j] = (Math.random() - 0.5) / 10; //other
							mat2[n][0][j] = (Math.random() - 0.5) / 2; //my
							mat3[n][0][j] = (Math.random() - 0.5) * 2; //sup
						}
						mat4[n][0][0] = (Math.random() - 0.5) / 5;
						mat5[n][0][0] = (Math.random() - 0.5) / 2;
						mat6[n][0][0] = (Math.random() - 0.5) / 6;
						mat7[n][0][0] = (Math.random() - 0.5) / 5;
					}
					break;
				//突然変異付きコピー
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
					int targetPlayerNum = (int) (Math.random() * Params.GA_PARENT_NUM);
					GaSysPlayer targetPlayer = ((GaSysPlayer) parentList.get(targetPlayerNum));
					for (n = 0; n < Params.CARD_MAX_NUM; n++) {
						for (j = 0; j < Params.CARD_MAX_NUM; j++) {
							if ((int) (Math.random() * 100) > 2)
								mat1[n][0][j] = targetPlayer.getOthersDeckMat()[n][0][j];
							else
								mat1[n][0][j] = (Math.random() - 0.5) / 10;
							if ((int) (Math.random() * 100) > 2)
								mat2[n][0][j] = targetPlayer.getMyDeckMat()[n][0][j];
							else
								mat2[n][0][j] = (Math.random() - 0.5) / 2;
							if ((int) (Math.random() * 100) > 2)
								mat3[n][0][j] = targetPlayer.getsupplyMat()[n][0][j];
							else
								mat3[n][0][j] = (Math.random() - 0.5) * 2;
						}
						if ((int) (Math.random() * 100) > 2)
							mat4[n][0][0] = targetPlayer.getDeckCntMat()[n][0][0];
						else
							mat4[n][0][0] = (Math.random() - 0.5) / 5;
						if ((int) (Math.random() * 100) > 2)
							mat5[n][0][0] = targetPlayer.getPositionMat()[n][0][0];
						else
							mat5[n][0][0] = (Math.random() - 0.5) / 2;
						if ((int) (Math.random() * 100) > 2)
							mat6[n][0][0] = targetPlayer.getEndTurnMat()[n][0][0];
						else
							mat6[n][0][0] = (Math.random() - 0.5) / 6;
						if ((int) (Math.random() * 100) > 2)
							mat7[n][0][0] = targetPlayer.getVictryDiffMat()[n][0][0];
						else
							mat7[n][0][0] = (Math.random() - 0.5) / 5;
					}
					break;
				//中点
				case 26:
				case 27:
				case 28:
				case 29:
					int targetPlayerNum1 = (int) (Math.random() * Params.GA_PARENT_NUM);
					int targetPlayerNum2 = (int) (Math.random() * Params.GA_PARENT_NUM);
					GaSysPlayer targetPlayer1 = ((GaSysPlayer) parentList.get(targetPlayerNum1));
					GaSysPlayer targetPlayer2 = ((GaSysPlayer) parentList.get(targetPlayerNum2));

					for (n = 0; n < Params.CARD_MAX_NUM; n++) {
						for (j = 0; j < Params.CARD_MAX_NUM; j++) {
							mat1[n][0][j] = (targetPlayer1.getOthersDeckMat()[n][0][j] +
									targetPlayer2.getOthersDeckMat()[n][0][j]) / 2;

							mat2[n][0][j] = (targetPlayer1.getMyDeckMat()[n][0][j] +
									targetPlayer2.getMyDeckMat()[n][0][j]) / 2;

							mat3[n][0][j] = (targetPlayer1.getsupplyMat()[n][0][j] +
									targetPlayer2.getsupplyMat()[n][0][j]) / 2;

						}
						/*	
						mat4[n][0][0] = targetPlayer.getDeckCntMat()[n][0][0];
						mat5[n][0][0] = targetPlayer.getAllCntMat()[n][0][0];
						mat6[n][0][0] = targetPlayer.getEndTurnMat()[n][0][0];
						mat7[n][0][0] = targetPlayer.getVictryDiffMat()[n][0][0];
						template */
						mat4[n][0][0] = (targetPlayer1.getDeckCntMat()[n][0][0] + targetPlayer2.getDeckCntMat()[n][0][0]) / 2;
						mat5[n][0][0] = (targetPlayer1.getPositionMat()[n][0][0] + targetPlayer2.getPositionMat()[n][0][0]) / 2;
						mat6[n][0][0] = (targetPlayer1.getEndTurnMat()[n][0][0] + targetPlayer2.getEndTurnMat()[n][0][0]) / 2;
						mat7[n][0][0] = (targetPlayer1.getVictryDiffMat()[n][0][0] + targetPlayer2.getVictryDiffMat()[n][0][0]) / 2;
					}
					break;
				//交雑
				case 5:
				case 6:
				case 7:
				case 8:
				case 20:
				case 21:
					targetPlayerNum1 = (int) (Math.random() * Params.GA_PARENT_NUM);
					targetPlayerNum2 = (int) (Math.random() * Params.GA_PARENT_NUM);
					int targetPlayerNum3 = (int) (Math.random() * Params.GA_PARENT_NUM);
					targetPlayer1 = ((GaSysPlayer) parentList.get(targetPlayerNum1));
					targetPlayer2 = ((GaSysPlayer) parentList.get(targetPlayerNum2));
					GaSysPlayer targetPlayer3 = ((GaSysPlayer) parentList.get(targetPlayerNum3));
					mat1 = targetPlayer1.getOthersDeckMat();
					mat2 = targetPlayer2.getMyDeckMat();
					mat3 = targetPlayer3.getsupplyMat();

					targetPlayerNum1 = (int) (Math.random() * Params.GA_PARENT_NUM);
					targetPlayerNum2 = (int) (Math.random() * Params.GA_PARENT_NUM);
					targetPlayerNum3 = (int) (Math.random() * Params.GA_PARENT_NUM);
					targetPlayer1 = ((GaSysPlayer) parentList.get(targetPlayerNum1));
					targetPlayer2 = ((GaSysPlayer) parentList.get(targetPlayerNum2));
					targetPlayer3 = ((GaSysPlayer) parentList.get(targetPlayerNum3));

					mat4 = targetPlayer1.getDeckCntMat();
					mat5 = targetPlayer2.getPositionMat();
					mat6 = targetPlayer3.getEndTurnMat();

					targetPlayerNum1 = (int) (Math.random() * Params.GA_PARENT_NUM);
					targetPlayer1 = ((GaSysPlayer) parentList.get(targetPlayerNum1));

					mat7 = targetPlayer1.getVictryDiffMat();
					break;

				//交雑2
				case 13:
				case 14:
				case 15:
				case 16:
				case 22:
				case 23:
				case 25:
				case 24:
					targetPlayerNum1 = (int) (Math.random() * Params.GA_PARENT_NUM);
					targetPlayerNum2 = (int) (Math.random() * Params.GA_PARENT_NUM);
					targetPlayer1 = ((GaSysPlayer) parentList.get(targetPlayerNum1));
					targetPlayer2 = ((GaSysPlayer) parentList.get(targetPlayerNum2));

					for (n = 0; n < Params.CARD_MAX_NUM; n++) {
						for (j = 0; j < Params.CARD_MAX_NUM; j++) {
							if (Math.random() > 0.5)
								targetPlayer = targetPlayer1;
							else
								targetPlayer = targetPlayer2;
							mat1[n][0][j] = targetPlayer.getOthersDeckMat()[n][0][j];
							if (Math.random() > 0.5)
								targetPlayer = targetPlayer1;
							else
								targetPlayer = targetPlayer2;
							mat2[n][0][j] = targetPlayer.getMyDeckMat()[n][0][j];
							if (Math.random() > 0.5)
								targetPlayer = targetPlayer1;
							else
								targetPlayer = targetPlayer2;
							mat3[n][0][j] = targetPlayer.getsupplyMat()[n][0][j];
						}
						if (Math.random() > 0.5)
							targetPlayer = targetPlayer1;
						else
							targetPlayer = targetPlayer2;
						mat4[n][0][0] = targetPlayer.getDeckCntMat()[n][0][0];
						if (Math.random() > 0.5)
							targetPlayer = targetPlayer1;
						else
							targetPlayer = targetPlayer2;
						mat5[n][0][0] = targetPlayer.getPositionMat()[n][0][0];
						if (Math.random() > 0.5)
							targetPlayer = targetPlayer1;
						else
							targetPlayer = targetPlayer2;
						mat6[n][0][0] = targetPlayer.getEndTurnMat()[n][0][0];
						if (Math.random() > 0.5)
							targetPlayer = targetPlayer1;
						else
							targetPlayer = targetPlayer2;
						mat7[n][0][0] = targetPlayer.getVictryDiffMat()[n][0][0];
					}
					break;
				//近傍
				case 9:
				case 10:
				case 11:
				case 12:
				case 18:
				case 19:
					targetPlayerNum = (int) (Math.random() * Params.GA_PARENT_NUM);
					targetPlayer = ((GaSysPlayer) parentList.get(targetPlayerNum));
					for (n = 0; n < Params.CARD_MAX_NUM; n++) {
						for (j = 0; j < Params.CARD_MAX_NUM; j++) {

							mat1[n][0][j] = targetPlayer.getOthersDeckMat()[n][0][j]
									+ (Math.random() - 0.5) / 200;
							mat2[n][0][j] = targetPlayer.getMyDeckMat()[n][0][j]
									+ (Math.random() - 0.5) / 40;
							mat3[n][0][j] = targetPlayer.getsupplyMat()[n][0][j]
									+ (Math.random() - 0.5) * 0.1;
						}
						mat4[n][0][0] = targetPlayer.getDeckCntMat()[n][0][0] + (Math.random() - 0.5) / 100;
						mat5[n][0][0] = targetPlayer.getPositionMat()[n][0][0] + (Math.random() - 0.5) / 40;
						mat6[n][0][0] = targetPlayer.getEndTurnMat()[n][0][0] + (Math.random() - 0.5) / 120;
						mat7[n][0][0] = targetPlayer.getVictryDiffMat()[n][0][0] + (Math.random() - 0.5) / 100;
					}
					break;
				}

				//共通突然変異
				for (n = 0; n < Params.CARD_MAX_NUM; n++) {
					for (j = 0; j < Params.CARD_MAX_NUM; j++) {
						if ((int) (Math.random() * 100) == 0)
							mat1[n][0][j] = (Math.random() - 0.5) / 10;
						if ((int) (Math.random() * 100) == 0)
							mat2[n][0][j] = (Math.random() - 0.5) / 2;
						if ((int) (Math.random() * 100) == 0)
							mat3[n][0][j] = (Math.random() - 0.5) * 2;
					}
					if ((int) (Math.random() * 100) == 0)
						mat4[n][0][0] = (Math.random() - 0.5) / 5;
					if ((int) (Math.random() * 100) == 0)
						mat5[n][0][0] = (Math.random() - 0.5) / 2;
					if ((int) (Math.random() * 100) == 0)
						mat6[n][0][0] = (Math.random() - 0.5) / 6;
					if ((int) (Math.random() * 100) == 0)
						mat7[n][0][0] = (Math.random() - 0.5) / 5;
				}

				_players[i] = new GaSysPlayer(core, mat1, mat2, mat3, mat4, mat5, mat6, mat7, false);
			}
		}
	}
}
