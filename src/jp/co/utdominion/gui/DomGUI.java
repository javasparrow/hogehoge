package jp.co.utdominion.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DomGUI extends Application {

    public void execute() {
        // JavaFX の実行
        Application.launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        // hello.fxml の読み込み
        Parent root = FXMLLoader.load(getClass().getResource("/guiwindow.fxml"));

        // Scene の作成・登録
        Scene scene = new Scene(root);
        stage.setScene(scene);

        // 表示
        stage.show();
    }
}