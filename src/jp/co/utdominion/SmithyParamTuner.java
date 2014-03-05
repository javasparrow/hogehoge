package jp.co.utdominion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SmithyParamTuner {
	public void execute() {
		File file = new File("smithy.txt");
		PrintWriter pw = null;

		try {
			file.createNewFile();
			pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		int i;
		double params0 = 0;
		double params1 = 0;
		double params2 = 0;
		double params3 = 0;
		double params4 = 0;
		double bestAveParams[] = null;
		double bestMaxParams[] = null;
		double bestAve = 0;
		double bestMax = 0;
		System.out.println("finalTurn:" + 14);
		pw.println("finalTurn:" + 14);
		for (params0 = 2.4; params0 < 3.7; params0 += 0.2) {
			for (params1 = 0.6; params1 < 1.5; params1 += 0.2) {
				for (params2 = 1.2; params2 < 2.5; params2 += 0.2) {
					for (params3 = 0; params3 < 0.9; params3 += 0.2) {
						for (params4 = 1; params4 < 1.1; params4 += 0.5) {
							int point = 0;
							int point_2 = 0;
							int maxPoint = 0;
							int minPoint = 100;
							double params[] = { params0, params1, params2, params3, params4 };
							for (i = 0; i < 100000; i++) {
								DomCore core = new DomCore();
								Player player = new TestPlayer(core, params);
								player.init(Params.HAND_RANDOM, 1);
								Player[] pl = { player };
								Result re = core.executeGame(pl, 14, Params.HAND_34, null);
								//re.printResult();
								point += re.getFinalVictry()[0];
								point_2 += re.getFinalVictry()[0] * re.getFinalVictry()[0];
								if (re.getFinalVictry()[0] > maxPoint)
									maxPoint = re.getFinalVictry()[0];
								if (re.getFinalVictry()[0] < minPoint)
									minPoint = re.getFinalVictry()[0];
							}
							double ave = (double) point / 100000;
							double bunsan = (double) point_2 / 100000 - (double) ave * (double) ave;
							System.out.println("params:" + params0 + " " + params1 + " " + params2 + " " + params3
									+ " " + params4 + " ");
							System.out.println("ave:" + ave);
							System.out.println("hyoujyun:" + Math.sqrt(bunsan));
							System.out.println("maxVP:" + maxPoint);
							System.out.println("minVP:" + minPoint);
							pw.println("params:" + params0 + " " + params1 + " " + params2 + " " + params3 + " "
									+ params4 + " ");
							pw.println("ave:" + ave);
							pw.println("hyoujyun:" + Math.sqrt(bunsan));
							pw.println("maxVP:" + maxPoint);
							pw.println("minVP:" + minPoint);
							pw.flush();
							if (bestAve < ave) {
								bestAve = ave;
								bestAveParams = params;
							}
							if (bestMax < ave + Math.sqrt(bunsan)) {
								bestMax = ave + Math.sqrt(bunsan);
								bestMaxParams = params;
							}
						}
					}
				}
			}
		}
		System.out.println("maxAveParams:" + bestAveParams[0] + " " + bestAveParams[1] + " " + bestAveParams[2] + " "
				+ bestAveParams[3] + " " + bestAveParams[4] + " ");
		pw.println("maxAveParams:" + bestAveParams[0] + " " + bestAveParams[1] + " " + bestAveParams[2] + " "
				+ bestAveParams[3] + " " + bestAveParams[4] + " ");
		System.out.println("maxMaxParams:" + bestMaxParams[0] + " " + bestMaxParams[1] + " " + bestMaxParams[2] + " "
				+ bestMaxParams[3] + " " + bestMaxParams[4] + " ");
		pw.println("maxMaxParams:" + bestMaxParams[0] + " " + bestMaxParams[1] + " " + bestMaxParams[2] + " "
				+ bestMaxParams[3] + " " + bestMaxParams[4] + " ");
		pw.close();
	}
}
