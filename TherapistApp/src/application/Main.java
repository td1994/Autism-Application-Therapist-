package application;
	
import java.io.File;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;


public class Main extends Application {
	private Model model;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			Scene scene = new Scene(root, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			model = new Model();
			
			//All code at this point creates the Fidelity View
			GridPane analysisView = new GridPane();
			analysisView.setAlignment(Pos.CENTER);
	        analysisView.setHgap(10);
	        analysisView.setVgap(10);
	        analysisView.setScaleShape(true);
	        analysisView.setPadding(new Insets(25, 25, 25, 25));
	        
	        HBox childAttn = new HBox();
	        childAttn.setAlignment(Pos.TOP_RIGHT);
	        childAttn.getChildren().add(new Label("Child Attention:"));
	        HBox clearOpp = new HBox();
	        clearOpp.setAlignment(Pos.TOP_RIGHT);
	        clearOpp.getChildren().add(new Label("Clear Opportunity:"));
	        HBox total = new HBox();
	        total.setAlignment(Pos.TOP_RIGHT);
	        total.getChildren().add(new Label("Total:"));
	        HBox maintenance = new HBox();
	        maintenance.setAlignment(Pos.TOP_RIGHT);
	        maintenance.getChildren().add(new Label("Maintenance Tasks/Task Variation:"));
	        HBox childChoice = new HBox();
	        childChoice.setAlignment(Pos.TOP_RIGHT);
	        childChoice.getChildren().add(new Label("Child Choice/Follow Child's Lead:"));
	        HBox sharedControl = new HBox();
	        sharedControl.setAlignment(Pos.TOP_RIGHT);
	        sharedControl.getChildren().add(new Label("Shared Control:"));
	        HBox contingent = new HBox();
	        contingent.setAlignment(Pos.TOP_RIGHT);
	        contingent.getChildren().add(new Label("Contigent:"));
	        HBox natural = new HBox();
	        natural.setAlignment(Pos.TOP_RIGHT);
	        natural.getChildren().add(new Label("Natural:"));
	        HBox attempts = new HBox();
	        attempts.setAlignment(Pos.TOP_RIGHT);
	        attempts.getChildren().add(new Label("Attempts:"));
	        
	        //adds HBoxes to the analysisView
	        analysisView.add(childAttn, 0, 0);
	        analysisView.add(clearOpp, 0, 1);
	        analysisView.add(total, 0, 2);
	        analysisView.add(maintenance, 0, 3);
	        analysisView.add(childChoice, 0, 4);
	        analysisView.add(sharedControl, 0, 5);
	        analysisView.add(contingent, 0, 6);
	        analysisView.add(natural, 0, 7);
	        analysisView.add(attempts, 0, 8);
	        
	        TextField text1 = new TextField("Grade");
	        analysisView.add(text1, 1, 0);
	        TextField text2 = new TextField("Grade");
	        analysisView.add(text2, 1, 1);
	        TextField text3 = new TextField("Grade");
	        analysisView.add(text3, 1, 2);
	        TextField text4 = new TextField("Grade");
	        analysisView.add(text4, 1, 3);
	        TextField text5 = new TextField("Grade");
	        analysisView.add(text5, 1, 4);
	        TextField text6 = new TextField("Grade");
	        analysisView.add(text6, 1, 5);
	        TextField text7 = new TextField("Grade");
	        analysisView.add(text7, 1, 6);
	        TextField text8 = new TextField("Grade");
	        analysisView.add(text8, 1, 7);
	        TextField text9 = new TextField("Grade");
	        analysisView.add(text9, 1, 8);
	        
	        HBox navigation = new HBox(10);
	        TextField pos = new TextField(Integer.toString(model.pos));
	        navigation.getChildren().addAll(new Label("Minute"), pos, new Label("/10"));
	        analysisView.add(navigation, 0, 9);
	        Button goLeft = new Button("<");
	        goLeft.setMaxWidth(50);
	        Button goRight = new Button(">");
	        goRight.setMaxWidth(50);
	        HBox movingControl = new HBox(5);
	        movingControl.getChildren().addAll(goLeft, goRight);
	        analysisView.add(movingControl, 1, 9);
	        root.setRight(analysisView);
	        
	        //All code at this point creates the Comments View
	        GridPane commentsView = new GridPane();
			commentsView.setAlignment(Pos.CENTER);
	        commentsView.setHgap(10);
	        commentsView.setVgap(10);
	        commentsView.setScaleShape(true);
	        commentsView.setPadding(new Insets(25, 25, 25, 25));
	        
	        ListView<String> list = new ListView<String>();
	        ObservableList<String> items =FXCollections.observableArrayList (
	            "Single", "Double", "Suite", "Family App");
	        list.setItems(items);
	        commentsView.add(list, 0, 0);
	        
	        Button deleteComment = new Button("Delete Comment");
	        Button printComments = new Button("Print Comments");
	        HBox buttons = new HBox(10);
	        buttons.getChildren().addAll(deleteComment, printComments);
	        buttons.setAlignment(Pos.TOP_LEFT);
	        commentsView.add(buttons, 0, 1);
	        
	        TextField fromTime = new TextField("0:00");
	        TextField toTime = new TextField("0:00");
	        HBox time = new HBox(10);
	        time.setAlignment(Pos.TOP_LEFT);
	        time.getChildren().addAll(new Label("FROM:"), fromTime, new Label("TO:"), toTime);
	        commentsView.add(time, 0, 2);
	        
	        TextArea comments = new TextArea("Write comments here...");
	        commentsView.add(comments, 0, 3);
	        Button add = new Button("Add Comment");
	        commentsView.add(add, 0, 4);
	        
	        //displays the Menu Bar "File" option with all options and hotkeys
			MenuBar menuBar = new MenuBar();
			Menu menuFile = new Menu("File");
			MenuItem openVideo = new MenuItem("Open Video");
			openVideo.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
			MenuItem openReview = new MenuItem("Open Review");
			openReview.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+O"));
			MenuItem close = new MenuItem("Close Project");
			close.setDisable(true);
			close.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
			MenuItem saveReview = new MenuItem("Save Review");
			saveReview.setDisable(true);
			saveReview.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
			MenuItem saveReviewAs = new MenuItem("Save Review As");
			saveReviewAs.setDisable(true);
			saveReviewAs.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+S"));
			MenuItem quit = new MenuItem("Quit");
			quit.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
			
			//displays the Menu Bar "Navigate" option with all options and hotkeys
			Menu menuNavigate = new Menu("Navigate");
			MenuItem analyze = new MenuItem("Make Analysis");
			analyze.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
			analyze.setDisable(true);
			MenuItem comment = new MenuItem("Make Comments");
			comment.setAccelerator(KeyCombination.keyCombination("Ctrl+M"));
			comment.setDisable(true);
			MenuItem prev = new MenuItem("Previous Section");
			prev.setAccelerator(KeyCombination.keyCombination("Ctrl+B"));
			prev.setDisable(true);
			MenuItem next = new MenuItem("Next Section");
			next.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
			next.setDisable(true);
			MenuItem rewind = new MenuItem("Rewind 30 Seconds");
			rewind.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
			rewind.setDisable(true);
			MenuItem foreward = new MenuItem("Fast Forward 30 Seconds");
			foreward.setAccelerator(KeyCombination.keyCombination("Ctrl+T"));
			foreward.setDisable(true);
			
			//displays the Menu Bar "Help" option with all options and hotkeys
			Menu menuHelp = new Menu("Help");
			MenuItem about = new MenuItem("About");
			
			//TODO: Set video to 
			openVideo.setOnAction(event -> {//when open is selected, gets info from selected csv file and enables test options
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Video File");
				File file = fileChooser.showOpenDialog(primaryStage);
				if(file != null) {
					root.setLeft(model.openVideo(file, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight()));
					
					close.setDisable(false);
					saveReview.setDisable(false);
					saveReviewAs.setDisable(false);
					comment.setDisable(false);
					next.setDisable(false);
					rewind.setDisable(false);
					foreward.setDisable(false);
				}
			});
			
			openReview.setOnAction(event -> {//when open is selected, gets info from selected csv file and enables test options
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Text File");
				File file = fileChooser.showOpenDialog(primaryStage);
				if(file != null) {
					model.openVideo(file, scene.getWidth(), scene.getHeight());
					close.setDisable(false);
					saveReview.setDisable(false);
					saveReviewAs.setDisable(false);
					comment.setDisable(false);
					next.setDisable(false);
					rewind.setDisable(false);
					foreward.setDisable(false);
				}
			});
			
			analyze.setOnAction(event -> {//change view to show the analysis screen
				root.setRight(analysisView);
				analyze.setDisable(true);
				comment.setDisable(false);
			});
			
			comment.setOnAction(event -> {//change view to show the analysis screen
				root.setRight(commentsView);
				analyze.setDisable(false);
				comment.setDisable(true);
			});
			
			prev.setOnAction(event -> {
				model.pos--;
				model.moveToSection();
				next.setDisable(false);
				if(model.pos == 0) {
					prev.setDisable(true);
				}
			});
			
			next.setOnAction(event -> {
				model.pos++;
				model.moveToSection();
				prev.setDisable(false);
				if(model.pos == 9) {
					next.setDisable(true);
				}
			});
			
			menuFile.getItems().addAll(openVideo, openReview, saveReview, saveReviewAs, close, quit);
			menuNavigate.getItems().addAll(analyze, comment, prev, next, rewind, foreward);
			menuHelp.getItems().addAll(about);
			menuBar.getMenus().addAll(menuFile, menuNavigate, menuHelp);
			root.setTop(menuBar);
	        
	        //Loads application
			primaryStage.setX(primaryScreenBounds.getMinX());
	        primaryStage.setY(primaryScreenBounds.getMinY());
	        primaryStage.setWidth(primaryScreenBounds.getWidth());
	        primaryStage.setHeight(primaryScreenBounds.getHeight());
			primaryStage.setTitle("Austism Video Analyzer");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
