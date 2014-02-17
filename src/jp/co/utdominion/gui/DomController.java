package jp.co.utdominion.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import javax.swing.JOptionPane;

import jp.co.utdominion.Card;
import jp.co.utdominion.CardData;
import jp.co.utdominion.DomCore;
import jp.co.utdominion.GaSysPlayer;
import jp.co.utdominion.GaUtils;
import jp.co.utdominion.HumanPlayer;
import jp.co.utdominion.Params;
import jp.co.utdominion.Player;
import jp.co.utdominion.Result;
import jp.co.utdominion.Supply;

public class DomController implements Initializable {

	@FXML
	private Button supply1;
	@FXML
	private Button supply2;
	@FXML
	private Button supply3;
	@FXML
	private Button supply4;
	@FXML
	private Button supply5;
	@FXML
	private Button supply6;
	@FXML
	private Button supply7;
	@FXML
	private Button supply8;
	@FXML
	private Button supply9;
	@FXML
	private Button supply10;
	@FXML
	private Button supply11;
	@FXML
	private Button supply12;
	@FXML
	private Button supply13;
	@FXML
	private Button supply14;
	@FXML
	private Button supply15;
	@FXML
	private Button supply16;
	@FXML
	private Button supply17;
	@FXML
	private TextArea text_1;
	@FXML
	private TextArea text_2;
	@FXML
	private TextArea text_3;
	@FXML
	private TextArea text_4;
	@FXML
	private GridPane hand_pane;
	@FXML
	private GridPane play_area;
	@FXML
	private GridPane lastbuy2;
	@FXML
	private GridPane lastbuy3;
	@FXML
	private GridPane lastbuy4;
	@FXML
	private Text supply_text1;
	@FXML
	private Text supply_text2;
	@FXML
	private Text supply_text3;
	@FXML
	private Text supply_text4;
	@FXML
	private Text supply_text5;
	@FXML
	private Text supply_text6;
	@FXML
	private Text supply_text7;
	@FXML
	private Text supply_text8;
	@FXML
	private Text supply_text9;
	@FXML
	private Text supply_text10;
	@FXML
	private Text supply_text11;
	@FXML
	private Text supply_text12;
	@FXML
	private Text supply_text13;
	@FXML
	private Text supply_text14;
	@FXML
	private Text supply_text15;
	@FXML
	private Text supply_text16;
	@FXML
	private Text supply_text17;


	//プレイヤー
	GaSysPlayer[] _players = new GaSysPlayer[4];

	HumanPlayer _human = new HumanPlayer(null);

	private int cnt;

	@FXML
	public void onSupplyButtonClicked(ActionEvent event) {
		_human.buyCard(Integer.valueOf(((Button) event.getSource()).getId().substring(7)) - 1);
	}

	@FXML
	public void goNextPhase(ActionEvent event) {
		_human.goNextPhase();
	}

	@Override
	public void initialize(URL url, ResourceBundle bundle) {

		class coreThread extends Thread {
			//(2)runメソッドをオーバーライド
			public void run() {

				DomCore core = new DomCore();

				for (int n = 0; n < 4; n++) {
	/*				_players[n] = new GaSysPlayer(core,
							GaUtils.createMatFromFile("othersDeckMat.txt"),
							GaUtils.createMatFromFile("myDeckMat.txt"),
							GaUtils.createMatFromFile("supplyMat.txt"),
							GaUtils.createMatFromFileSmall("deckCntMat.txt"),
							GaUtils.createMatFromFileSmall("positionMat.txt"),
							GaUtils.createMatFromFileSmall("endTurnMat.txt"),
							GaUtils.createMatFromFileSmall("victryDiffMat.txt"));
*/
					_players[n] = GaUtils.createPlayerFromFile("C:\\Users\\denjo\\Documents\\domiAI\\domAI.txt");
					_players[n].setCore(core);
				}

				_human.setCore(core);
				//Player[] pl = { new HumanPlayer(core), _players[1], _players[2], _players[3] };
				int posRnd = (int) (Math.random() * 4);
				Player[] pl = null;
				switch (posRnd) {
				case 0:
					pl = new Player[] { _human, _players[1], _players[2], _players[3] };
					break;
				case 1:
					pl = new Player[] { _players[3], _human, _players[1], _players[2] };
					break;
				case 2:
					pl = new Player[] { _players[2], _players[3], _human, _players[1] };
					break;
				case 3:
					pl = new Player[] { _players[1], _players[2], _players[3], _human };
					break;
				}
				Result re = core.executeGame(pl, 100, Params.HAND_RANDOM, DomController.this);
				re.printResult();
				saveLog();
				JOptionPane.showMessageDialog(null, "あなたは" + (posRnd + 1) + "番手で" + re.getPosition()[posRnd] + "位です");
				JOptionPane.showMessageDialog(null, re.getResult());

			}
		}

		new coreThread().start();
	}

	void saveLog() {
		ArrayList<String> logs = null;
		for (int i = 1; i < 4; i++) {
			if (_players[i].getAveScore() == 6) {
				logs = _players[i].getLogString();
			}
		}
		if (_human.getAveScore() == 6)
			logs = _human.getLogString();
		if (logs == null)
			return;
		Date date1 = new Date(); //(1)Dateオブジェクトを生成

		//(2)SimpleDateFormatオブジェクトを生成
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");

		File newfile = new File("dominionLogs/" + sdf1.format(date1));
		if (newfile.mkdirs()) {
			System.out.println(newfile.getAbsolutePath());
		} else {
			System.out.println("ディレクトリの作成に失敗しました");
		}

		int cnt = 1;
		for (String log : logs) {
			PrintWriter pw = null;
			File file = new File(newfile.getAbsolutePath() + "/log" + cnt + ".txt");
			cnt++;
			try {
				file.createNewFile();
				pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			pw.print(log);
			pw.flush();
			pw.close();
		}
	}

	DomCore _core;

	EventHandler<ActionEvent> handHandler = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			int id = Integer.valueOf(((Button) event.getSource()).getId());
			_human.useCard(id);
		}
	};

	public void refresh(DomCore core) {

		_core = core;

		Platform.runLater(new Runnable() {
			public void run() {
				Supply[] supply = _core.getSupply();
				CardData data = CardData.getInstence();

				supply_text1.setText(String.valueOf((supply[10].getNum())));
				supply1.setGraphic(makeCardImage(data.getCardData(supply[10].getCard()).getImagePath()));
				supply_text2.setText(String.valueOf((supply[11].getNum())));
				supply2.setGraphic(makeCardImage(data.getCardData(supply[11].getCard()).getImagePath()));
				supply_text3.setText(String.valueOf((supply[12].getNum())));
				supply3.setGraphic(makeCardImage(data.getCardData(supply[12].getCard()).getImagePath()));
				supply_text4.setText(String.valueOf((supply[13].getNum())));
				supply4.setGraphic(makeCardImage(data.getCardData(supply[13].getCard()).getImagePath()));
				supply_text5.setText(String.valueOf((supply[14].getNum())));
				supply5.setGraphic(makeCardImage(data.getCardData(supply[14].getCard()).getImagePath()));
				supply_text6.setText(String.valueOf((supply[15].getNum())));
				supply6.setGraphic(makeCardImage(data.getCardData(supply[15].getCard()).getImagePath()));
				supply_text7.setText(String.valueOf((supply[16].getNum())));
				supply7.setGraphic(makeCardImage(data.getCardData(supply[16].getCard()).getImagePath()));


				supply_text8.setText(String.valueOf((supply[0].getNum())));
				supply8.setGraphic(makeCardImage(data.getCardData(supply[0].getCard()).getImagePath()));
				supply_text9.setText(String.valueOf((supply[1].getNum())));
				supply9.setGraphic(makeCardImage(data.getCardData(supply[1].getCard()).getImagePath()));
				supply_text10.setText(String.valueOf((supply[2].getNum())));
				supply10.setGraphic(makeCardImage(data.getCardData(supply[2].getCard()).getImagePath()));
				supply_text11.setText(String.valueOf((supply[3].getNum())));
				supply11.setGraphic(makeCardImage(data.getCardData(supply[3].getCard()).getImagePath()));
				supply_text12.setText(String.valueOf((supply[4].getNum())));
				supply12.setGraphic(makeCardImage(data.getCardData(supply[4].getCard()).getImagePath()));
				supply_text13.setText(String.valueOf((supply[5].getNum())));
				supply13.setGraphic(makeCardImage(data.getCardData(supply[5].getCard()).getImagePath()));
				supply_text14.setText(String.valueOf((supply[6].getNum())));
				supply14.setGraphic(makeCardImage(data.getCardData(supply[6].getCard()).getImagePath()));
				supply_text15.setText(String.valueOf((supply[7].getNum())));
				supply15.setGraphic(makeCardImage(data.getCardData(supply[7].getCard()).getImagePath()));
				supply_text16.setText(String.valueOf((supply[8].getNum())));
				supply16.setGraphic(makeCardImage(data.getCardData(supply[8].getCard()).getImagePath()));
				supply_text17.setText(String.valueOf((supply[9].getNum())));
				supply17.setGraphic(makeCardImage(data.getCardData(supply[9].getCard()).getImagePath()));

				if (_players[0] == null)
					return;
				text_1.setText(_human.getPlayerInfoString());
				text_2.setText(_players[1].getPlayerInfoString());
				text_3.setText(_players[2].getPlayerInfoString());
				text_4.setText(_players[3].getPlayerInfoString());

				int index = 0;

				hand_pane.getChildren().clear();

				for (Card hand : _human.getHand()) {
					ImageView img = makeCardImage(hand.getImagePath());

					Button btn = new Button("", img);
					btn.setOnAction(handHandler);
					btn.setId(String.valueOf(index));
					//btn.setFont(new Font("SansSerif", 15));
					//btn.setTextOverrun(OverrunStyle.CLIP);

					hand_pane.add(btn, index, 0);
					index++;
				}

				index = 0;

				play_area.getChildren().clear();
				lastbuy2.getChildren().clear();
				lastbuy3.getChildren().clear();
				lastbuy4.getChildren().clear();

				for (Card hand : _core.getOldDuration()) {
					Image img = new Image(hand.getImagePath(), false);
					ImageView imv = new ImageView();
					imv.setImage(img);
					imv.setFitWidth(50);
					imv.setPreserveRatio(true);

					play_area.add(imv, index, 0);
					index++;
				}
				for (Card hand : _core.getPlayArea()) {
					Image img = new Image(hand.getImagePath(), false);
					ImageView imv = new ImageView();
					imv.setImage(img);
					imv.setFitWidth(50);
					imv.setPreserveRatio(true);

					play_area.add(imv, index, 0);
					index++;
				}
				for (Card hand : _core.getNewDuration()) {
					Image img = new Image(hand.getImagePath(), false);
					ImageView imv = new ImageView();
					imv.setImage(img);
					imv.setFitWidth(50);
					imv.setPreserveRatio(true);

					play_area.add(imv, index, 0);
					index++;
				}

				index = 0;

				for (Card buy : _players[1].getLastBuy()) {
					Image img = new Image(buy.getImagePath(), false);
					ImageView imv = new ImageView();
					imv.setImage(img);
					imv.setFitWidth(50);
					imv.setPreserveRatio(true);

					lastbuy2.add(imv, index, 0);
					index++;
				}

				index = 0;

				for (Card buy : _players[2].getLastBuy()) {
					Image img = new Image(buy.getImagePath(), false);
					ImageView imv = new ImageView();
					imv.setImage(img);
					imv.setFitWidth(50);
					imv.setPreserveRatio(true);

					lastbuy3.add(imv, index, 0);
					index++;
				}

				index = 0;

				for (Card buy : _players[3].getLastBuy()) {
					Image img = new Image(buy.getImagePath(), false);
					ImageView imv = new ImageView();
					imv.setImage(img);
					imv.setFitWidth(50);
					imv.setPreserveRatio(true);

					lastbuy4.add(imv, index, 0);
					index++;
				}
			}
		});

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private ImageView makeCardImage(String path){
		Image img = new Image(path, false);
		ImageView imv = new ImageView();
		imv.setImage(img);
		imv.setFitWidth(50);
		imv.setPreserveRatio(true);
		return imv;
	}
}