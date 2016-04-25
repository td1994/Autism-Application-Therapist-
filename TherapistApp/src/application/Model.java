package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
	 * 
	 * @param file
	 *            the file to be read
	 * @throws Exception 
	 */
	public void openVideo(File file) throws Exception {
		videoPath = "file://"
				+ file.getCanonicalPath().replace(" ", "%20").replace("\\", "/").replaceAll("^.:", "");
		if(!videoPath.contains(".mp4")) {
			throw new Exception();
		}
		showMessage("Decrypting Video: Please be patient");
		EncryptFile encryptor = new EncryptFile();
		encryptor.encrypt(file.getCanonicalPath(), 4096);
		encryptor = null;
		showMessage("Decryption Complete!");
	}

	public void closeVideo() {
		try {
			showMessage("Encrypting Video: Please be patient");
			EncryptFile encryptor = new EncryptFile();
			encryptor.encrypt(toCanonicalPath(videoPath), 4096);
			encryptor = null;
			showMessage("Encryption Complete!");
		} catch (Exception e) {
			showMessage("Error: File Encryption Failed");
			e.printStackTrace();
		}
	}

	// ----------------------------------------------------------
	/**
	 * Opens a text file for review
	 * 
	 * @param file
	 *            the file to be read
	 * @throws Exception 
	 */
	public ObservableList<String> openReview(File file, ListView list) throws Exception {
		ObservableList<String> items = list.getItems();
		items.clear();
		filePath = file.getCanonicalPath();
		if(!filePath.contains(".txt")) {
			throw new Exception();
		}
		FileInputStream fstream = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		videoPath = br.readLine();
		videoPath = videoPath.substring(videoPath.indexOf(':') + 1);
		if(!videoPath.contains(".mp4")) {
			throw new Exception();
		}
		showMessage("Decrypting Video: Please be patient");
		EncryptFile encryptor = new EncryptFile();
		encryptor.encrypt(toCanonicalPath(videoPath), 4096);
		showMessage("Decryption Complete!");
		encryptor = null;
		for (int i = 0; i < fidelityResponses.length; i++) {
			String input = br.readLine();
			input = input.substring(input.indexOf(':') + 1);
			String[] fmString = input.split(",");
			for (int j = 0; j < fmString.length; j++) {
				fidelityResponses[i][j] = Boolean.valueOf(fmString[j]);
			}
		}
		comments = new ArrayList<Comment>();
		String startTime = br.readLine();
		while (startTime != null) {
			startTime = startTime.substring(startTime.indexOf(':') + 1);
			String endTime = br.readLine();
			endTime = endTime.substring(endTime.indexOf(':') + 1);
			String comment = br.readLine();
			comment = comment.substring(comment.indexOf(':') + 1);
			Comment newComment = new Comment();
			newComment.startTime = startTime;
			newComment.endTime = endTime;
			newComment.comment = comment;
			comments.add(newComment);
			items.add(startTime + "-" + endTime + ": " + comment);
			startTime = br.readLine();
		}
		return items;
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

	public ObservableList<String> editComment(int index, String startTime, String endTime, String comment,
			ListView list) {
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

	public void printComments(File file) throws FileNotFoundException, IOException {
		Comment[] commentArray = new Comment[comments.size()];
		comments.toArray(commentArray);
		PrintWriter writer = new PrintWriter(file.getCanonicalPath() + ".doc");
		writer.println("Comments for video " + videoPath);
		writer.println();
		for (int i = 0; i < commentArray.length; i++) {
			writer.println("From: " + commentArray[i].startTime);
			writer.println("To: " + commentArray[i].endTime);
			writer.println("Comment: " + commentArray[i].comment + "\n");
			writer.println();
		}
		writer.close();
	}

	public void showMessage(String message) {
		MessageDialog dialog = new MessageDialog(message);
		dialog.show();
	}

	public void saveReview() throws FileNotFoundException {
		Comment[] commentArray = new Comment[comments.size()];
		comments.toArray(commentArray);
		PrintWriter writer = new PrintWriter(filePath);
		writer.println("VideoPath:" + videoPath);
		for (int i = 0; i < fidelityResponses.length; i++) {
			writer.print("FidelityMinute" + i + ":");
			for (int j = 0; j < fidelityResponses[i].length; j++) {
				writer.print(fidelityResponses[i][j] + ",");
			}
			writer.println();
		}
		for (int i = 0; i < commentArray.length; i++) {
			writer.println("StartTime" + i + ":" + commentArray[i].startTime);
			writer.println("EndTime" + i + ":" + commentArray[i].endTime);
			writer.println("Comment" + i + ":" + commentArray[i].comment);
		}
		writer.close();
	}

	public void saveReviewAs(File file) throws IOException {
		filePath = file.getCanonicalPath() + ".txt";
		saveReview();
	}

	public String toCanonicalPath(String filePath) {
		return filePath.replace("file://", "C:").replace("/", "\\");
	}
	
	public void clearData() {
		videoPath = null;
		filePath = null;
		for(int i = 0; i < fidelityResponses.length; i++) {
			for(int j = 0; j < fidelityResponses[i].length; j++) {
				fidelityResponses[i][j] = false;
			}
		}
		comments.clear();
	}
}

class MessageDialog extends Stage {

	/**
	 * Creates an instance of the <code>MessageDialog</code> class. Use the
	 * <code>show</code> to diaply the dialog.
	 *
	 * @param message
	 *            The message to be displayed.
	 */
	public MessageDialog(String message) {
		super(StageStyle.DECORATED);
		setResizable(false);
		initModality(Modality.APPLICATION_MODAL);
		setTitle("Processsing");

		BorderPane dialogRoot = new BorderPane();
		dialogRoot.getStyleClass().add("message-dialog");

		Scene dialogScene = new Scene(dialogRoot, 400, 300);
		// dialogScene.getStylesheets().add(getClass().getResource("hw2.css").toExternalForm());
		setScene(dialogScene);

		Label mesLabel = new Label(message);
		mesLabel.setWrapText(true);
		dialogRoot.setCenter(mesLabel);

		Button button = new Button("OK");
		button.setOnAction(event -> close());
		HBox hbtn = new HBox();
		hbtn.setStyle("-fx-background-color: lightyellow");
		hbtn.setAlignment(Pos.CENTER);
		hbtn.getChildren().add(button);
		hbtn.setMargin(button, new Insets(10, 10, 10, 10));
		dialogRoot.setBottom(hbtn);
	}
}
