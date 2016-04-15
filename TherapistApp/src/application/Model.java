package application;

import java.io.File;

import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class Model {
	public String videoPath;
	public String filePath;
	private Media media;
	private MediaPlayer mediaPlayer;
	private MediaView mediaView;
	private StackPane mediaPane; 
	private MediaControl control;
	public int pos;
	
	//opens a video file and has it playing in a media player
	MediaControl openVideo(File file, double width, double height){
		try {
			videoPath = "file://" + file.getCanonicalPath().replace(" ", "%20").replace("\\", "/").replaceAll("^.:", "");
			//Video View
			
			mediaPane = new StackPane();
			mediaPane.setId("mediaPane");
			media = new Media(videoPath);
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.setAutoPlay(true);
			control = new MediaControl(mediaPlayer, width, height);
			pos = 0;
			return control;
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	public void openReview(File file){
		try {
			filePath = "file://" + file.getCanonicalPath().replace(" ", "%20").replace("\\", "/").replaceAll("^.:", "");
			//TODO: After setting up how the file will be saved, figure out how to load it
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void moveToSection() {
		System.out.println(pos);
		control.goToMinute(pos);
	}
}
