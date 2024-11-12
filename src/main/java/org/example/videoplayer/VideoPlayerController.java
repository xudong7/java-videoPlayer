package org.example.videoplayer;
// 23331150 徐栋 视频播放器 作业
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;

public class VideoPlayerController {

    @FXML
    private MediaView mediaView;
    @FXML
    private Button playButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button openButton;
    @FXML
    private Button rewindButton;
    @FXML
    private Button forwardButton;
    @FXML
    private Slider progressSlider;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Label volumeLabel;  // 用来显示音量数值

    private MediaPlayer mediaPlayer;

    @FXML
    private void initialize() {
        volumeSlider.setValue(0.5);  // 默认音量为50%

        // 设置音量控制
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newValue.doubleValue());
            }
            // 更新音量Label
            updateVolumeLabel(newValue.doubleValue());
        });

        // 设置进度条拖动控制
        progressSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging && mediaPlayer != null) {
                mediaPlayer.seek(Duration.seconds(progressSlider.getValue()));
            }
        });
    }

    @FXML
    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择视频文件");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4"));

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            // 先清理当前播放的媒体
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }

            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            // 设置音量和进度条
            initVolume(mediaPlayer);

            // 捕获播放错误
            mediaPlayer.setOnError(() -> {
                System.out.println("Error occurred: " + mediaPlayer.getError().getMessage());
            });

            // 更新进度条
            mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                if (!progressSlider.isValueChanging()) {
                    progressSlider.setValue(newTime.toSeconds());
                }
            });

            // 确保在视频准备好时才开始播放
            mediaPlayer.setOnReady(() -> {
                progressSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
                mediaPlayer.play();  // 播放
            });

            // 监听MediaPlayer的状态变化
            mediaPlayer.statusProperty().addListener((obs, oldStatus, newStatus) -> {
                System.out.println("MediaPlayer status changed: " + newStatus);
            });
        }
    }

    @FXML
    private void handlePlay() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    @FXML
    private void handlePause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @FXML
    private void handleStop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    @FXML
    private void handleRewind() {
        if (mediaPlayer != null) {
            Duration currentTime = mediaPlayer.getCurrentTime();
            mediaPlayer.seek(currentTime.subtract(Duration.seconds(10)));  // 回退10秒
        }
    }

    @FXML
    private void handleForward() {
        if (mediaPlayer != null) {
            Duration currentTime = mediaPlayer.getCurrentTime();
            mediaPlayer.seek(currentTime.add(Duration.seconds(10)));  // 前进10秒
        }
    }

    // 设置媒体播放器的音量
    public void initVolume(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        // 初始化音量
        mediaPlayer.setVolume(volumeSlider.getValue());
        // 更新音量显示
        updateVolumeLabel(volumeSlider.getValue());
    }

    // 更新音量Label
    private void updateVolumeLabel(double volumeValue) {
        int volumePercentage = (int) (volumeValue * 100);  // 将音量值转换为百分比
        volumeLabel.setText(volumePercentage + "%");
    }

    // 在场景加载时绑定MediaView的宽高，使其随着窗口大小变化
    public void bindMediaViewToScene(Scene scene) {
        mediaView.fitWidthProperty().bind(scene.widthProperty());
        mediaView.fitHeightProperty().bind(scene.heightProperty().subtract(100));  // 保证播放器底部控制栏高度不被覆盖
    }
}
