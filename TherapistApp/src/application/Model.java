package application;

import java.io.File;

import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class Model {
	/**
	 * The path of the video being reviewed
	 */
	public String videoPath;
	/**
	 * The path of the files being used
	 */
	public String filePath;

	// ----------------------------------------------------------
	/**
	 * Opens a video from file
	 * @param file the file to be read
	 */
	public void openVideo(File file){
		try {
			videoPath = "file://" + file.getCanonicalPath().replace(" ", "%20").replace("\\", "/").replaceAll("^.:", "");
			//System.out.println("video path is " + videoPath);
			//TODO: have video loaded into media player.
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

	// ----------------------------------------------------------
	/**
	 * Opens a text file for review
	 * @param file the file to be read
	 */
	public void openReview(File file){
		try {
			filePath = "file://" + file.getCanonicalPath().replace(" ", "%20").replace("\\", "/").replaceAll("^.:", "");
			//TODO: After setting up how the file will be saved, figure out how to load it
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
}
