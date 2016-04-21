package application;

import java.io.File;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
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
	
	public boolean[][] fidelityResponses;
	public ArrayList<Comment> comments;

	public Model() {
		fidelityResponses = new boolean[10][9];
		comments = new ArrayList<Comment>();
	}
	
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
	
	public void fidelityResponse(int minute, int question, boolean response) {
		fidelityResponses[minute][question] = response;
	}

	public ObservableList<String> addComment(String startTime, String endTime, String comment, ListView list) {
		Comment com = new Comment();
		com.startTime = startTime;
		com.endTime = endTime;
		com.comment = comment;
		comments.add(com);
		ObservableList<String> items = list.getItems();
		items.add(startTime + "-" + endTime + ": " + comment);
		return items;
	}
	
	public Comment getComment(int index) {
		return comments.get(index);
	}
	
	public ObservableList<String> removeComment(int index, ListView list) {
		comments.remove(index);
		ObservableList<String> items = list.getItems();
		items.remove(index);
		return items;
	}
	
	public ObservableList<String> editComment(int index, String startTime, String endTime, String comment, ListView list) {
		comments.remove(index);
		Comment com = new Comment();
		com.startTime = startTime;
		com.endTime = endTime;
		com.comment = comment;
		comments.add(index, com);
		ObservableList<String> items = list.getItems();
		items.remove(index);
		items.add(index, startTime + "-" + endTime + ": " + comment);
		return items;
	}
}
