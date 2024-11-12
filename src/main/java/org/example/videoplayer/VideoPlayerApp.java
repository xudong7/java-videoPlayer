package org.example.videoplayer;
// 23331150 徐栋 视频播放器 作业
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VideoPlayerApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("VideoPlayer.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Video Player");
        primaryStage.setScene(scene);

        // 获取控制器并绑定MediaView到场景
        VideoPlayerController controller = loader.getController();
        controller.bindMediaViewToScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
