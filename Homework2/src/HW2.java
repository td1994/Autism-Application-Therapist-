import java.io.File;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * @author Thomas
 * Homework 2
 */
public class HW2 extends Application {
	
	private StackPane mediaPane;
	private MediaView mediaView = null;
	private Media media = null;
	private Scene startScene = null, mediaScene = null, pollScene = null;
	private HW2Model model;
	private String choice;
	private MediaPlayer mediaPlayer;
	/**
	 * @author Thomas
	 * start method - runs the view of program
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			//inital setup for window and other settings
			BorderPane root = new BorderPane();
			startScene = new Scene(root,800,600);
			startScene.getStylesheets().add(getClass().getResource("hw2.css").toExternalForm());
			model = new HW2Model();
			mediaPane = new StackPane();
			mediaPane.setId("mediaPane");
			choice = null;
			
			//displays the Menu Bar "File" option with all options and hotkeys
			MenuBar menuBar = new MenuBar();
			Menu menuFile = new Menu("File");
			MenuItem open = new MenuItem("Open");
			open.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
			MenuItem close = new MenuItem("Close");
			close.setDisable(true);
			close.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
			MenuItem pref = new MenuItem("Preferences");
			pref.setDisable(true);
			pref.setAccelerator(KeyCombination.keyCombination("Ctrl+,"));
			MenuItem quit = new MenuItem("Quit");
			quit.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
			
			//displays the Menu Bar "Navigate" option with all options and hotkeys
			Menu menuNavigate = new Menu("Navigate");
			MenuItem start = new MenuItem("Start Study");
			start.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
			start.setDisable(true);
			MenuItem proceed = new MenuItem("Proceed");
			proceed.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
			proceed.setDisable(true);
			MenuItem next = new MenuItem("Next Movie");
			next.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
			next.setDisable(true);
			MenuItem end = new MenuItem("End Study");
			end.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
			end.setDisable(true);
			
			//displays the Menu Bar "Help" option with all options and hotkeys
			Menu menuHelp = new Menu("Help");
			MenuItem about = new MenuItem("About");
			
			//sets up radio buttons for test (css not working for radiobuttons for some reason)
			ToggleGroup choices = new ToggleGroup();
			RadioButton anger = new RadioButton("anger");
			anger.getStyleClass().add("radio-button");
			anger.setStyle("-fx-text-fill: white;\n-fx-font-size: 32pt;");
			anger.setToggleGroup(choices);
			RadioButton disgust = new RadioButton("disgust");
			disgust.getStyleClass().add("radio-button");
			disgust.setStyle("-fx-text-fill: white;\n-fx-font-size: 32pt;");
			disgust.setToggleGroup(choices);
			RadioButton fear = new RadioButton("fear");
			fear.getStyleClass().add("radio-button");
			fear.setStyle("-fx-text-fill: white;\n-fx-font-size: 32pt;");
			fear.setToggleGroup(choices);
			RadioButton happy = new RadioButton("happy");
			happy.getStyleClass().add("radio-button");
			happy.setStyle("-fx-text-fill: white;\n-fx-font-size: 32pt;");
			happy.setToggleGroup(choices);
			RadioButton sad = new RadioButton("sad");
			sad.getStyleClass().add("radio-button");
			sad.setStyle("-fx-text-fill: white;\n-fx-font-size: 32pt;");
			sad.setToggleGroup(choices);
			RadioButton surprise = new RadioButton("surprise");
			surprise.getStyleClass().add("radio-button");
			surprise.setStyle("-fx-text-fill: white;\n-fx-font-size: 32pt;");
			surprise.setToggleGroup(choices);
			VBox vbChoice = new VBox(10);
			vbChoice.setAlignment(Pos.CENTER);
			vbChoice.setStyle("-fx-background-color: black");
			vbChoice.getChildren().addAll(anger, disgust, fear, happy, sad, surprise);
			anger.setOnAction(event -> {//when button is selected, text of choice is added to choice
	        	choice = anger.getText();
	        	proceed.setDisable(false);
			});
			disgust.setOnAction(event -> {
	        	choice = disgust.getText();
	        	proceed.setDisable(false);
			});
			fear.setOnAction(event -> {
	        	choice = fear.getText();
	        	proceed.setDisable(false);
			});
			happy.setOnAction(event -> {
	        	choice = happy.getText();
	        	proceed.setDisable(false);
			});
			sad.setOnAction(event -> {
	        	choice = sad.getText();
	        	proceed.setDisable(false);
			});
			surprise.setOnAction(event -> {
	        	choice = surprise.getText();
	        	proceed.setDisable(false);
			});
			
			open.setOnAction(event -> {//when open is selected, gets info from selected csv file and enables test options
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Media File");
				File file = fileChooser.showOpenDialog(primaryStage);
				model.setup(file);
				open.setDisable(true);
				close.setDisable(false);
				pref.setDisable(false);
				start.setDisable(false);
			});
			close.setOnAction(event -> {//when close is selected, all data retrieved will be deleted, set to main menu
				root.setCenter(null);
				model.close();
				open.setDisable(false);
				close.setDisable(true);
				pref.setDisable(true);
				start.setDisable(true);
				proceed.setDisable(true);
				next.setDisable(true);
				end.setDisable(true);
			});
			pref.setOnAction(event -> {//Preference window is displayed
				model.preferences();
			}); 
			quit.setOnAction(event -> System.exit(0)); //exits program
			
			menuFile.getItems().addAll(open, close, pref, quit);
			
			start.setOnAction(event -> {//starts test - shows video
				media = new Media(model.getMediaPath(false));
				mediaPlayer = new MediaPlayer(media);
				mediaPlayer.setAutoPlay(true);
				mediaView = new MediaView(mediaPlayer);
				mediaView.setPreserveRatio(true);
				mediaPane.getChildren().clear();
				mediaPane.getChildren().add(mediaView);
				mediaView.setFitWidth(0.8 * mediaPane.getWidth());
				HBox hbVideo = new HBox();
				hbVideo.setStyle("-fx-background-color: black;");
				hbVideo.setAlignment(Pos.CENTER);
				hbVideo.getChildren().add(mediaPane);
				root.setCenter(hbVideo);
				start.setDisable(true);
				proceed.setDisable(false);
				next.setDisable(false);
				end.setDisable(false);
				System.out.println("Study starts.");
			});
			proceed.setOnAction(event -> {//continues to next area of test - ends test when out of videos
				if(choice == null)
				{
					root.setCenter(vbChoice);
					proceed.setDisable(true);
				}
				else
				{
					String answer = model.checkAnswer(choice);
					System.out.print("Movie " + model.getMedia() + ", answer is " + answer);
					if(model.isCorrect())
					{
						System.out.println(" (CORRECT).");
					}
					else
					{
						System.out.println(" (INCORRECT).");
					}
					choice = null;
					if(model.testFinished())
					{
						System.out.println("Study ends.");
						root.setCenter(null);
						start.setDisable(false);
						next.setDisable(true);
						end.setDisable(true);
					}
					else
					{
						media = new Media(model.getMediaPath(false));
						mediaPlayer = new MediaPlayer(media);
						mediaPlayer.setAutoPlay(true);
						mediaView = new MediaView(mediaPlayer);
						mediaView.setPreserveRatio(true);
						mediaPane.getChildren().clear();
						mediaPane.getChildren().add(mediaView);
						HBox hbVideo = new HBox();
						hbVideo.setStyle("-fx-background-color: black;");
						hbVideo.setAlignment(Pos.CENTER);
						hbVideo.getChildren().add(mediaPane);
						root.setCenter(hbVideo);
						proceed.setDisable(false);
					}
				}
			});
			next.setOnAction(event -> {//starts next movie, ends when there's no more movies
				if(choice == null)
				{
					System.out.println("Movie skipped.");
					media = new Media(model.getMediaPath(true));
					mediaPlayer = new MediaPlayer(media);
					mediaPlayer.setAutoPlay(true);
					mediaView = new MediaView(mediaPlayer);
					mediaView.setPreserveRatio(true);
					mediaPane.getChildren().clear();
					mediaPane.getChildren().add(mediaView);
					HBox hbVideo = new HBox();
					hbVideo.setStyle("-fx-background-color: black;");
					hbVideo.setAlignment(Pos.CENTER);
					hbVideo.getChildren().add(mediaPane);
					root.setCenter(hbVideo);
					proceed.setDisable(false);
				}
				else
				{
					String answer = model.checkAnswer(choice);
					System.out.print("Movie " + model.getMedia() + ", answer is " + answer);
					if(model.isCorrect())
					{
						System.out.println(" (CORRECT).");
					}
					else
					{
						System.out.println(" (INCORRECT).");
					}
					choice = null;
					if(model.testFinished())
					{
						System.out.println("Study ends.");
						root.setCenter(null);
						start.setDisable(false);
						next.setDisable(true);
						end.setDisable(true);
					}
					else
					{
						media = new Media(model.getMediaPath(false));
						mediaPlayer = new MediaPlayer(media);
						mediaPlayer.setAutoPlay(true);
						mediaView = new MediaView(mediaPlayer);
						mediaView.setPreserveRatio(true);
						mediaPane.getChildren().clear();
						mediaPane.getChildren().add(mediaView);
						HBox hbVideo = new HBox();
						hbVideo.setStyle("-fx-background-color: black;");
						hbVideo.setAlignment(Pos.CENTER);
						hbVideo.getChildren().add(mediaPane);
						root.setCenter(hbVideo);
						proceed.setDisable(false);
					}
				}
			});
			end.setOnAction(event -> {//ends test
				System.out.println("Study ends.");
				root.setCenter(null);
				start.setDisable(false);
				proceed.setDisable(true);
				next.setDisable(true);
				end.setDisable(true);
			});
			
			menuNavigate.getItems().addAll(start, proceed, next, end);
			
			about.setOnAction(event -> {//shows about window
				model.about();
			});   
			menuHelp.getItems().addAll(about);

			menuBar.getMenus().addAll(menuFile, menuNavigate, menuHelp);
			root.setTop(menuBar);
			root.setCenter(mediaPane);
			primaryStage.setTitle("HW2: tdowey3");
			primaryStage.setScene(startScene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args
	 * Main Method - starts program
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
