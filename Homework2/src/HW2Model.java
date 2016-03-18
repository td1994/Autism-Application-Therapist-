import java.io.File;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 * @author Thomas
 * HW2Model - Model for program
 */
public class HW2Model {
	public String name;
	public String[] video_files;
	public String[] answers;
	public String csvPath;
	public String mediaPath;
	private int loc;
	boolean[] correct;
	
	/**
	 * @param file - location of file
	 * setup - sets up the test by reading .csv file
	 */
	public void setup(File file)
	{
		try {
			csvPath = "file://" + file.getCanonicalPath().replace(" ", "%20").replace("\\", "/").replaceAll("^.:", "");
			mediaPath = csvPath.substring(0, csvPath.lastIndexOf("/") + 1) + "media/";
			Scanner scanner = new Scanner(file);
			name = scanner.nextLine();
			answers = scanner.nextLine().split(",");
			video_files = scanner.nextLine().split(",");
			correct = new boolean[answers.length];
			scanner.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * @param answer - answer received from view
	 * @return the correct answer
	 * checkAnswer - checks if the user was correct
	 */
	public String checkAnswer(String answer)
	{
		loc++;
		if(video_files[loc - 1].contains(answer))
		{
			correct[loc - 1] = true;
			return answer;
		}
		else
		{
			correct[loc - 1] = false;
			String theAnswer = "";
			for(int i = 0; i < answers.length; i++)
			{
				if(video_files[loc - 1].contains(answers[i]))
				{
					theAnswer = answers[i];
				}
			}
			return theAnswer;
		}
	}
	
	/**
	 * @return the user's correctness
	 * isCorrect - tells whether the user was correct in the last answer
	 */
	public boolean isCorrect()
	{
		return correct[loc - 1];
	}
	
	/**
	 * @return if test was finished
	 * testFinished - checks whether all video files were viewed
	 */
	public boolean testFinished()
	{
		return loc == answers.length;
	}
	
	/**
	 * close - sets all data to initial conditions
	 */
	public void close()
	{
		video_files = null;
		answers = null;
		loc = 0;
		correct = null;
		csvPath = null;
		mediaPath = null;
	}
	
	/**
	 * preferences - shows the preferences window
	 */
	public void preferences()
	{
		Preferences preferences = new Preferences(this);
		preferences.show();
	}
	
	/**
	 * about - shows the about window
	 */
	public void about()
	{
		MessageDialog dialog = new MessageDialog("Conducts a study where the user is presented with a sequence of media files.");
		dialog.show();
	}
	
	/**
	 * @param skipped - tells if the question was skipped
	 * @return the filePath of the video
	 */
	public String getMediaPath(boolean skipped)
	{
		if(skipped)
		{
			loc++;
		}
		return mediaPath + video_files[loc] + ".mp4";
	}
	
	/**
	 * @return the name of the video
	 * getMedia - gets the name of the video
	 */
	public String getMedia()
	{
		return video_files[loc - 1];
	}
}

class MessageDialog extends Stage {

	/**
	 * Creates an instance of the <code>MessageDialog</code> class.
	 * Use the <code>show</code> to diaply the dialog.
	 * 
	 * @param message The message to be displayed.
	 */
	public MessageDialog(String message) {
		super(StageStyle.DECORATED);
		setResizable(false);
		initModality(Modality.APPLICATION_MODAL);
		setTitle("About Homework 2");

		BorderPane dialogRoot = new BorderPane();
		dialogRoot.getStyleClass().add("message-dialog");

		Scene dialogScene = new Scene(dialogRoot, 400, 300);
		dialogScene.getStylesheets().add(getClass().getResource("hw2.css").toExternalForm());
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
		hbtn.setMargin(button, new Insets(10,10,10,10));
		dialogRoot.setBottom(hbtn);
	}
}

/**
 * @author Thomas
 * Preferences class - shows the Preferences Window
 */
class Preferences extends Stage
{
	public Preferences(HW2Model model)
	{
		super(StageStyle.DECORATED);
		setResizable(false);
		initModality(Modality.APPLICATION_MODAL);
		setTitle("About Homework 2");
		
		GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setScaleShape(true);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.getStyleClass().add("preferences");
        
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        grid.getColumnConstraints().addAll(col1, col2);
        
        Scene prefScene = new Scene(grid, 640, 480);
		prefScene.getStylesheets().add(getClass().getResource("hw2.css").toExternalForm());
		setScene(prefScene);
        
        Label name = new Label("Name");
        HBox hbName = new HBox();
        hbName.getChildren().add(name);
        Label loc = new Label("Media Location");
        HBox hbLoc = new HBox();
        hbLoc.getChildren().add(loc);
        Label choices = new Label("Choices");
        HBox hbChoices = new HBox();
        hbChoices.getChildren().add(choices);
        Label videos = new Label("Videos");
        HBox hbVideos = new HBox();
        hbVideos.getChildren().add(videos);
		
		grid.add(hbName, 0, 0);
		grid.add(hbLoc, 0, 1);
		grid.add(hbChoices, 0, 2);
		grid.add(hbVideos, 0, 3);
		
		TextField nameText = new TextField();
		nameText.setText(model.name);
		nameText.setEditable(false);
		grid.add(nameText, 1, 0);
		
		TextField locText = new TextField();
		locText.setText(model.mediaPath);
		locText.setEditable(false);
		grid.add(locText, 1, 1);
		
		Button cancel = new Button("Cancel");
		cancel.setOnAction(event -> close());
		Button ok = new Button("OK");
		ok.setOnAction(event -> close());
		HBox hbox = new HBox(10);
		hbox.setAlignment(Pos.BOTTOM_LEFT);
		hbox.getChildren().add(cancel);
		hbox.getChildren().add(ok);
		grid.add(hbox, 1, 4);
		
		ComboBox<String> cbChoice = new ComboBox<String>();
		cbChoice.getItems().addAll(model.answers);
		cbChoice.getStyleClass().add(".combo-box");
		cbChoice.setStyle("-fx-background-color: lightblue;");
		grid.add(cbChoice, 1, 2);
		
		ComboBox<String> cbVideo = new ComboBox<String>();
		cbVideo.getItems().addAll(model.video_files);
		cbVideo.getStyleClass().add("combo-box");
		cbVideo.setStyle("-fx-background-color: lightblue;");
		grid.add(cbVideo, 1, 3);
	}
}
