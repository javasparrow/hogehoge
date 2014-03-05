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

public class LogLearning {
	//プレイヤー
	LogStudyPlayer[] _players = new LogStudyPlayer[Params.GA_TOTAL_NUM];
	//世代数
	int _generation = 0;
	//試合数
	long _totalMatch = 0;

	private static final boolean DEBUG = false;
	private static final boolean LOADFROMFILE = true;
	private static final String LOADFILEDIR = "C:\\Users\\denjo\\Documents\\domiAI\\domAI.txt";

	PrintWriter pw = null;

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

	ArrayList<DominionLog> logs = new ArrayList<DominionLog>();

	void execute() {

		File dir = new File("dominionLogs");
		File[] logDirs = dir.listFiles();
		for (int i = 0; i < logDirs.length; i++) {
			if (logDirs[i].isDirectory()) {
				File[] files = logDirs[i].listFiles();
				for (int n = 0; n < files.length; n++) {
					logs.add(new DominionLog(files[n].getAbsolutePath()));
				}
			}
		}
		File file = new File("C:/dominion/logLearning.txt");

		try {
			file.createNewFile();
			pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		int i, n, j, k;

		HashSet<Integer> disableSet = new HashSet<Integer>();

		for (i = 0; i < 1000; i++) {
			disableSet = new HashSet<Integer>();
			_generation++;
			makePlayers(null);
			System.out.println("generation" + i);
			pw.println("generation" + i);

			Thread thread1 = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int j = 0; j < Params.GA_TOTAL_NUM_1; j++) {
						for (int n = 0; n < logs.size(); n++) {
							double dis = _players[j].calculateDiffFromLog(logs.get(n));
							_players[j].addDest(dis);
							_players[j].calculateDiffFromLog(logs.get(n));
						}
						System.out.println("player" + j + " score:" + _players[j].getDest());
						pw.println("player" + j + " score:" + _players[j].getDest());
					}
				}
			});
			Thread thread2 = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int j = Params.GA_TOTAL_NUM_1; j < Params.GA_TOTAL_NUM_2; j++) {
						for (int n = 0; n < logs.size(); n++) {
							double dis = _players[j].calculateDiffFromLog(logs.get(n));
							_players[j].addDest(dis);
							_players[j].calculateDiffFromLog(logs.get(n));
						}
						System.out.println("player" + j + " score:" + _players[j].getDest());
						pw.println("player" + j + " score:" + _players[j].getDest());
					}
				}
			});
			Thread thread3 = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int j = Params.GA_TOTAL_NUM_2; j < Params.GA_TOTAL_NUM_3; j++) {
						for (int n = 0; n < logs.size(); n++) {
							double dis = _players[j].calculateDiffFromLog(logs.get(n));
							_players[j].addDest(dis);
							_players[j].calculateDiffFromLog(logs.get(n));
						}
						System.out.println("player" + j + " score:" + _players[j].getDest());
						pw.println("player" + j + " score:" + _players[j].getDest());
					}
				}
			});
			Thread thread4 = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int j = Params.GA_TOTAL_NUM_3; j < Params.GA_TOTAL_NUM_4; j++) {
						for (int n = 0; n < logs.size(); n++) {
							double dis = _players[j].calculateDiffFromLog(logs.get(n));
							_players[j].addDest(dis);
							_players[j].calculateDiffFromLog(logs.get(n));
						}
						System.out.println("player" + j + " score:" + _players[j].getDest());
						pw.println("player" + j + " score:" + _players[j].getDest());
					}
				}
			});
			thread1.start();
			thread2.start();
			thread3.start();
			thread4.start();
			try {
				thread1.join();
				thread2.join();
				thread3.join();
				thread4.join();
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

		ArrayList<LogStudyPlayer> parentList = new ArrayList<LogStudyPlayer>();

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
				_players[i] = new LogStudyPlayer(mat1, mat2, mat3, mat4, mat5, mat6, mat7);
			}
		}
		else {

			if (_generation == 1 && LOADFROMFILE == true) {
				GaSysPlayer p = GaUtils.createPlayerFromFile(LOADFILEDIR, false);
				for (int i = 0; i < 2; i++) {
					parentList.add(new LogStudyPlayer(p.getOthersDeckMat(), p.getMyDeckMat(), p.getsupplyMat()
							, p.getDeckCntMat(), p.getPositionMat(), p.getEndTurnMat(), p.getVictryDiffMat()));
				}

				//親の初期化
				//初期収束を避けるため優秀な奴で埋めるのはやめておく
				int i, n, j;
				for (i = 1; i < Params.GA_ELETE_NUM; i++) {
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
					parentList.add(new LogStudyPlayer(mat1, mat2, mat3, mat4, mat5, mat6, mat7));
				}

				//永遠の命を与えられるエリート
				for (i = 0; i < Params.GA_GO_NEXT_NUM; i++) {
					_players[i] = new LogStudyPlayer(
							p.getOthersDeckMat(), p.getMyDeckMat(), p.getsupplyMat(),
							p.getDeckCntMat(), p.getPositionMat(), p.getEndTurnMat(), p.getVictryDiffMat());
				}
			}

			else {

				class MyComparator implements Comparator<LogStudyPlayer> {
					@Override
					public int compare(LogStudyPlayer o1, LogStudyPlayer o2) {
						if (o1.getDest() == o2.getDest())
							return 0;
						return o1.getDest() < o2.getDest() ? -1 : 1;
					}
				}
				Arrays.sort(_players, new MyComparator());

				int i, n, j;

				//必ず生き残る個体
				for (i = 0; i < Params.GA_ELETE_NUM - Params.GA_ALIVE_NUM; i++) {
					System.out.println("elete" + i + " score:" + _players[i].getDest());
					pw.println("elete" + i + " score:" + _players[i].getDest());

					LogStudyPlayer p = ((LogStudyPlayer) _players[i]);
					parentList.add(new LogStudyPlayer(p.getOthersDeckMat(), p.getMyDeckMat(), p.getsupplyMat()
							, p.getDeckCntMat(), p.getPositionMat(), p.getEndTurnMat(), p.getVictryDiffMat()));

					String str = new String();
					System.out.println("othersDeckMat");
					pw.println("othersDeckMat");
					for (n = 0; n < Params.CARD_MAX_NUM; n++) {
						str = "";
						for (j = 0; j < Params.CARD_MAX_NUM; j++) {
							str += ((LogStudyPlayer) _players[i]).getOthersDeckMat()[n][0][j] + " ";
						}
						System.out.println(str);
						pw.println(str);
					}
					System.out.println("MyDeckMat");
					pw.println("MyDeckMat");
					for (n = 0; n < Params.CARD_MAX_NUM; n++) {
						str = "";
						for (j = 0; j < Params.CARD_MAX_NUM; j++) {
							str += ((LogStudyPlayer) _players[i]).getMyDeckMat()[n][0][j] + " ";
						}
						System.out.println(str);
						pw.println(str);
					}
					System.out.println("supplyMat");
					pw.println("supplyMat");
					for (n = 0; n < Params.CARD_MAX_NUM; n++) {
						str = "";
						for (j = 0; j < Params.CARD_MAX_NUM; j++) {
							str += ((LogStudyPlayer) _players[i]).getsupplyMat()[n][0][j] + " ";
						}
						System.out.println(str);
						pw.println(str);
					}
					System.out.println("deckMat");
					pw.println("deckCntMat");
					str = "";
					for (j = 0; j < Params.CARD_MAX_NUM; j++) {
						str += ((LogStudyPlayer) _players[i]).getDeckCntMat()[j][0][0] + " ";
					}
					System.out.println(str);
					pw.println(str);

					System.out.println("posMat");
					pw.println("posMat");
					str = "";
					for (j = 0; j < Params.CARD_MAX_NUM; j++) {
						str += ((LogStudyPlayer) _players[i]).getPositionMat()[j][0][0] + " ";
					}
					System.out.println(str);
					pw.println(str);

					System.out.println("endMat");
					pw.println("endMat");
					str = "";
					for (j = 0; j < Params.CARD_MAX_NUM; j++) {
						str += ((LogStudyPlayer) _players[i]).getEndTurnMat()[j][0][0] + " ";
					}
					System.out.println(str);
					pw.println(str);

					System.out.println("vDiffMat");
					pw.println("vDiffMat");
					str = "";
					for (j = 0; j < Params.CARD_MAX_NUM; j++) {
						str += ((LogStudyPlayer) _players[i]).getVictryDiffMat()[j][0][0] + " ";
					}
					System.out.println(str);
					pw.println(str);
				}

				//一定確率で死ぬ個体
				int currentPlayerCursor = Params.GA_ELETE_NUM - Params.GA_ALIVE_NUM;
				double currentProbability = Params.GA_FIRST_PROBABILITY;
				for (i = Params.GA_ELETE_NUM - Params.GA_ALIVE_NUM; i < Params.GA_ELETE_NUM; i++) {
					while (true) {
						if (Math.random() < currentProbability) {
							/*_players[i] = new LogStudyPlayer(
									((LogStudyPlayer) _players[currentPlayerCursor]).getOthersDeckMat()
									, ((LogStudyPlayer) _players[currentPlayerCursor]).getMyDeckMat(),
									((LogStudyPlayer) _players[currentPlayerCursor]).getsupplyMat());*/

							LogStudyPlayer p = ((LogStudyPlayer) _players[currentPlayerCursor]);

							parentList.add(new LogStudyPlayer(
									p.getOthersDeckMat(), p.getMyDeckMat(), p.getsupplyMat(),
									p.getDeckCntMat(), p.getPositionMat(), p.getEndTurnMat(), p.getVictryDiffMat()));

							currentPlayerCursor++;
							currentProbability *= Params.GA_REDUCE_PROBABILITY;
							break;
						}
						currentPlayerCursor++;
						//WARNING! でっちあげ例外処理
						if (currentPlayerCursor == _players.length) {
							currentPlayerCursor = Params.GA_ELETE_NUM - Params.GA_ALIVE_NUM;
							System.out.println("warning! failed");
						}
					}
				}

				//永遠の命を与えられるエリート
				for (i = 0; i < Params.GA_GO_NEXT_NUM; i++) {
					//LogStudyPlayer p = ((LogStudyPlayer) parentList.get(i));
					LogStudyPlayer p = _players[i];
					_players[i] = new LogStudyPlayer(
							p.getOthersDeckMat(), p.getMyDeckMat(), p.getsupplyMat(),
							p.getDeckCntMat(), p.getPositionMat(), p.getEndTurnMat(), p.getVictryDiffMat());
				}

			}

			int i, n, j;

			//子供の個体
			for (i = Params.GA_GO_NEXT_NUM; i < Params.GA_TOTAL_NUM; i++) {
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
					int targetPlayerNum = (int) (Math.random() * Params.GA_ELETE_NUM);
					LogStudyPlayer targetPlayer = ((LogStudyPlayer) parentList.get(targetPlayerNum));
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
					int targetPlayerNum1 = (int) (Math.random() * Params.GA_ELETE_NUM);
					int targetPlayerNum2 = (int) (Math.random() * Params.GA_ELETE_NUM);
					LogStudyPlayer targetPlayer1 = ((LogStudyPlayer) parentList.get(targetPlayerNum1));
					LogStudyPlayer targetPlayer2 = ((LogStudyPlayer) parentList.get(targetPlayerNum2));

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
				case 21:
				case 19:
					targetPlayerNum1 = (int) (Math.random() * Params.GA_ELETE_NUM);
					targetPlayerNum2 = (int) (Math.random() * Params.GA_ELETE_NUM);
					int targetPlayerNum3 = (int) (Math.random() * Params.GA_ELETE_NUM);
					targetPlayer1 = ((LogStudyPlayer) parentList.get(targetPlayerNum1));
					targetPlayer2 = ((LogStudyPlayer) parentList.get(targetPlayerNum2));
					LogStudyPlayer targetPlayer3 = ((LogStudyPlayer) parentList.get(targetPlayerNum3));

					mat1 = targetPlayer1.getOthersDeckMat();
					mat2 = targetPlayer2.getMyDeckMat();
					mat3 = targetPlayer3.getsupplyMat();

					targetPlayerNum1 = (int) (Math.random() * Params.GA_ELETE_NUM);
					targetPlayerNum2 = (int) (Math.random() * Params.GA_ELETE_NUM);
					targetPlayerNum3 = (int) (Math.random() * Params.GA_ELETE_NUM);
					targetPlayer1 = ((LogStudyPlayer) parentList.get(targetPlayerNum1));
					targetPlayer2 = ((LogStudyPlayer) parentList.get(targetPlayerNum2));
					targetPlayer3 = ((LogStudyPlayer) parentList.get(targetPlayerNum3));

					mat4 = targetPlayer1.getDeckCntMat();
					mat5 = targetPlayer2.getPositionMat();
					mat6 = targetPlayer3.getEndTurnMat();

					targetPlayerNum1 = (int) (Math.random() * Params.GA_ELETE_NUM);
					targetPlayer1 = ((LogStudyPlayer) parentList.get(targetPlayerNum1));

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
				case 20:
					targetPlayerNum1 = (int) (Math.random() * Params.GA_ELETE_NUM);
					targetPlayerNum2 = (int) (Math.random() * Params.GA_ELETE_NUM);
					targetPlayer1 = ((LogStudyPlayer) parentList.get(targetPlayerNum1));
					targetPlayer2 = ((LogStudyPlayer) parentList.get(targetPlayerNum2));

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
					targetPlayerNum = (int) (Math.random() * Params.GA_ELETE_NUM);
					targetPlayer = ((LogStudyPlayer) parentList.get(targetPlayerNum));
					for (n = 0; n < Params.CARD_MAX_NUM; n++) {
						for (j = 0; j < Params.CARD_MAX_NUM; j++) {

							mat1[n][0][j] = targetPlayer.getOthersDeckMat()[n][0][j]
									+ (Math.random() - 0.5) / (200);
							mat2[n][0][j] = targetPlayer.getMyDeckMat()[n][0][j]
									+ (Math.random() - 0.5) / (40);
							mat3[n][0][j] = targetPlayer.getsupplyMat()[n][0][j]
									+ (Math.random() - 0.5) * (0.1);
						}
						mat4[n][0][0] = targetPlayer.getDeckCntMat()[n][0][0] + (Math.random() - 0.5)
								/ (100 /*(1 + (double)_generation/20)*/);
						mat5[n][0][0] = targetPlayer.getPositionMat()[n][0][0] + (Math.random() - 0.5) / (40);
						mat6[n][0][0] = targetPlayer.getEndTurnMat()[n][0][0] + (Math.random() - 0.5) / (120);
						mat7[n][0][0] = targetPlayer.getVictryDiffMat()[n][0][0] + (Math.random() - 0.5) / (100);
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

				_players[i] = new LogStudyPlayer(mat1, mat2, mat3, mat4, mat5, mat6, mat7);
			}
		}
	}
}
