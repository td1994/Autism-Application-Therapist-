package application;

import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;
import javafx.geometry.Insets;


public class Main extends Application {
	private Model model;
	private Media media = null;
	private MediaPlayer mediaPlayer;
	private MediaView mediaView;
	private StackPane mediaPane;
    private final boolean repeat = false;
    private boolean stopRequested = false;
    private boolean atEndOfMedia = false;
    private Duration duration;
    private Slider timeSlider;
    private Label playTime;
    private Slider volumeSlider;
    private boolean willPause = false;
    private boolean boolAutoPause = true;


	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,1024,768);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			model = new Model();
	        mediaPane = new StackPane();
	        mediaPane.setId("mediaPane");

	        //media bar stuff
	        HBox mediaBar = new HBox();
	        mediaBar.setAlignment(Pos.CENTER);
	        mediaBar.setPadding(new Insets(5, 10, 5, 10));
	        root.setBottom(mediaBar);

	        final Button playButton = new Button("||");
	        final Button align30 = new Button("<<30");

	        playButton.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent e) {
	                Status status = mediaPlayer.getStatus();

	                if (status == Status.UNKNOWN || status == Status.HALTED) {
	                    // don't do anything in these states
	                    return;
	                }

	                if (status == Status.PAUSED
	                        || status == Status.READY
	                        || status == Status.STOPPED) {
	                    // rewind the movie if we're sitting at the end
	                    if (atEndOfMedia) {
	                        mediaPlayer.seek(mediaPlayer.getStartTime());
	                        atEndOfMedia = false;
	                    }
	                    mediaPlayer.play();
	                } else {
	                    mediaPlayer.pause();
	                }
	            }
	        });

	        align30.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent e) {
	                Duration current = mediaPlayer.getCurrentTime();
	                double timeInSeconds = current.toSeconds();
	                if (Math.floor(timeInSeconds) % 30 == 0 && timeInSeconds >= 30) {
	                    timeInSeconds -= 30;
	                }
	                Duration newTime = new Duration( Math.floor(timeInSeconds / 30) * 30000);
	                willPause = false;
	                mediaPlayer.seek(newTime);
	            }
	        });


	        mediaBar.getChildren().add(playButton);
	        // Add spacer
	        Label spacer = new Label("   ");
	        mediaBar.getChildren().add(spacer);

	        mediaBar.getChildren().add(align30);

	        Label spacer2 = new Label("   ");
            mediaBar.getChildren().add(spacer2);

	        // Add Time label
	        Label timeLabel = new Label("Time: ");
	        mediaBar.getChildren().add(timeLabel);

	        // Add time slider
	        timeSlider = new Slider();
	        HBox.setHgrow(timeSlider, Priority.ALWAYS);
	        timeSlider.setMinWidth(50);
	        timeSlider.setShowTickMarks(true);
	        timeSlider.setMin(0);
	        timeSlider.setMax(100);
	        timeSlider.setMajorTickUnit(10);
	        timeSlider.setMinorTickCount(1);
	        timeSlider.setMaxWidth(Double.MAX_VALUE);
	        timeSlider.valueProperty().addListener(new InvalidationListener() {
	            public void invalidated(Observable ov) {
	                if (timeSlider.isValueChanging() || timeSlider.isPressed()) {
	                    // multiply duration by percentage calculated by slider position
	                    mediaPlayer.seek(duration.multiply(timeSlider.getValue() / 100.0));
	                }
	            }
	        });

	        mediaBar.getChildren().add(timeSlider);

	        // Add Play label
	        playTime = new Label();
	        playTime.setPrefWidth(130);
	        playTime.setMinWidth(50);
	        mediaBar.getChildren().add(playTime);

	        // Add the volume label
	        Label volumeLabel = new Label("Vol: ");
	        mediaBar.getChildren().add(volumeLabel);

	        // Add Volume slider
	        volumeSlider = new Slider();
	        volumeSlider.setPrefWidth(70);
	        volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
	        volumeSlider.setMinWidth(30);
	        volumeSlider.valueProperty().addListener(new InvalidationListener() {
	            public void invalidated(Observable ov) {
	                if (volumeSlider.isValueChanging()) {
	                    mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
	                }
	            }
	        });
	        mediaBar.getChildren().add(volumeSlider);

	        mediaBar.setVisible(false);

			//All code at this point creates the Fidelity View
			GridPane grid = new GridPane();
			grid.setAlignment(Pos.CENTER);
	        grid.setHgap(10);
	        grid.setVgap(10);
	        grid.setScaleShape(true);
	        grid.setPadding(new Insets(25, 25, 25, 25));

	        HBox childAttn = new HBox();
	        childAttn.setAlignment(Pos.TOP_LEFT);
	        childAttn.getChildren().add(new Label("Child Attention:"));
	        HBox clearOpp = new HBox();
	        clearOpp.setAlignment(Pos.TOP_LEFT);
	        clearOpp.getChildren().add(new Label("Clear Opportunity:"));
	        HBox total = new HBox();
	        total.setAlignment(Pos.TOP_LEFT);
	        total.getChildren().add(new Label("Total:"));
	        HBox maintenance = new HBox();
	        maintenance.setAlignment(Pos.TOP_LEFT);
	        maintenance.getChildren().add(new Label("Maintenance Tasks/Task Variation:"));
	        HBox childChoice = new HBox();
	        childChoice.setAlignment(Pos.TOP_LEFT);
	        childChoice.getChildren().add(new Label("Child Choice/Follow Child's Lead:"));
	        HBox sharedControl = new HBox();
	        sharedControl.setAlignment(Pos.TOP_LEFT);
	        sharedControl.getChildren().add(new Label("Shared Control:"));
	        HBox contingent = new HBox();
	        contingent.setAlignment(Pos.TOP_LEFT);
	        contingent.getChildren().add(new Label("Contigent:"));
	        HBox natural = new HBox();
	        natural.setAlignment(Pos.TOP_LEFT);
	        natural.getChildren().add(new Label("Natural:"));
	        HBox attempts = new HBox();
	        attempts.setAlignment(Pos.TOP_LEFT);
	        attempts.getChildren().add(new Label("Attempts:"));
	        HBox Time = new HBox();
            Time.setAlignment(Pos.CENTER);
            Label outOfTenLabel = new Label("No Video Open");
            Time.getChildren().add(outOfTenLabel);

	        //adds HBoxes to the grid
	        grid.add(childAttn, 0, 0);
	        grid.add(clearOpp, 0, 1);
	        grid.add(total, 0, 2);
	        grid.add(maintenance, 0, 3);
	        grid.add(childChoice, 0, 4);
	        grid.add(sharedControl, 0, 5);
	        grid.add(contingent, 0, 6);
	        grid.add(natural, 0, 7);
	        grid.add(attempts, 0, 8);
	        grid.add(outOfTenLabel, 1, 9);

	        ToggleGroup childAttnGrade = new ToggleGroup();
	        RadioButton caNeg = new RadioButton("-");
	        caNeg.setToggleGroup(childAttnGrade);
	        RadioButton caPos = new RadioButton("+");
	        caPos.setToggleGroup(childAttnGrade);
	        HBox hca = new HBox(10);
	        hca.getChildren().addAll(caNeg, caPos);
	        grid.add(hca, 1, 0);
	        
	        ToggleGroup clearOppGrade = new ToggleGroup();
	        RadioButton coNeg = new RadioButton("-");
	        coNeg.setToggleGroup(clearOppGrade);
	        RadioButton coPos = new RadioButton("+");
	        coPos.setToggleGroup(clearOppGrade);
	        HBox hco = new HBox(10);
	        hco.getChildren().addAll(coNeg, coPos);
	        grid.add(hco, 1, 1);
	        
	        ToggleGroup totalGrade = new ToggleGroup();
	        RadioButton tNeg = new RadioButton("-");
	        tNeg.setToggleGroup(totalGrade);
	        RadioButton tPos = new RadioButton("+");
	        tPos.setToggleGroup(totalGrade);
	        HBox ht = new HBox(10);
	        ht.getChildren().addAll(tNeg, tPos);
	        grid.add(ht, 1, 2);
	        
	        ToggleGroup maintenanceGrade = new ToggleGroup();
	        RadioButton mNeg = new RadioButton("-");
	        mNeg.setToggleGroup(maintenanceGrade);
	        RadioButton mPos = new RadioButton("+");
	        mPos.setToggleGroup(maintenanceGrade);
	        HBox hm = new HBox(10);
	        hm.getChildren().addAll(mNeg, mPos);
	        grid.add(hm, 1, 3);
	        
	        ToggleGroup childChoiceGrade = new ToggleGroup();
	        RadioButton ccNeg = new RadioButton("-");
	        ccNeg.setToggleGroup(childChoiceGrade);
	        RadioButton ccPos = new RadioButton("+");
	        ccPos.setToggleGroup(childChoiceGrade);
	        HBox hcc = new HBox(10);
	        hcc.getChildren().addAll(ccNeg, ccPos);
	        grid.add(hcc, 1, 4);
	        
	        ToggleGroup sharedControlGrade = new ToggleGroup();
	        RadioButton scNeg = new RadioButton("-");
	        scNeg.setToggleGroup(sharedControlGrade);
	        RadioButton scPos = new RadioButton("+");
	        scPos.setToggleGroup(sharedControlGrade);
	        HBox hsc = new HBox(10);
	        hsc.getChildren().addAll(scNeg, scPos);
	        grid.add(hsc, 1, 5);
	        
	        ToggleGroup contingentGrade = new ToggleGroup();
	        RadioButton cNeg = new RadioButton("-");
	        cNeg.setToggleGroup(contingentGrade);
	        RadioButton cPos = new RadioButton("+");
	        cPos.setToggleGroup(contingentGrade);
	        HBox hc = new HBox(10);
	        hc.getChildren().addAll(cNeg, cPos);
	        grid.add(hc, 1, 6);
	        
	        ToggleGroup naturalGrade = new ToggleGroup();
	        RadioButton nNeg = new RadioButton("-");
	        nNeg.setToggleGroup(naturalGrade);
	        RadioButton nPos = new RadioButton("+");
	        nPos.setToggleGroup(naturalGrade);
	        HBox hn = new HBox(10);
	        hn.getChildren().addAll(nNeg, nPos);
	        grid.add(hn, 1, 7);
	        
	        ToggleGroup attemptsGrade = new ToggleGroup();
	        RadioButton aNeg = new RadioButton("-");
	        aNeg.setToggleGroup(attemptsGrade);
	        RadioButton aPos = new RadioButton("+");
	        aPos.setToggleGroup(attemptsGrade);
	        HBox ha = new HBox(10);
	        ha.getChildren().addAll(aNeg, aPos);
	        grid.add(ha, 1, 8);
	        
	        caNeg.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent e)
	        	{
	        		if(model.videoPath != null) {
	        			model.fidelityResponse((int)(Math.floor(mediaPlayer.getCurrentTime().toMinutes())), 0, false);
	        		}
	        	}
	        });
	        caPos.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent e)
	        	{
	        		if(model.videoPath != null) {
	        			model.fidelityResponse((int)(Math.floor(mediaPlayer.getCurrentTime().toMinutes())), 0, true);
	        		}
	        	}
	        });
	        
	        coNeg.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent e)
	        	{
	        		if(model.videoPath != null) {
	        			model.fidelityResponse((int)(Math.floor(mediaPlayer.getCurrentTime().toMinutes())), 1, false);
	        		}
	        	}
	        });
	        coPos.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent e)
	        	{
	        		if(model.videoPath != null) {
	        			model.fidelityResponse((int)(Math.floor(mediaPlayer.getCurrentTime().toMinutes())), 1, true);
	        		}
	        	}
	        });
	        
	        tNeg.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent e)
	        	{
	        		if(model.videoPath != null) {
	        			model.fidelityResponse((int)(Math.floor(mediaPlayer.getCurrentTime().toMinutes())), 2, false);
	        		}
	        	}
	        });
	        tPos.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent e)
	        	{
	        		if(model.videoPath != null) {
	        			model.fidelityResponse((int)(Math.floor(mediaPlayer.getCurrentTime().toMinutes())), 2, true);
	        		}
	        	}
	        });
	        
	        mNeg.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent e)
	        	{
	        		if(model.videoPath != null) {
	        			model.fidelityResponse((int)(Math.floor(mediaPlayer.getCurrentTime().toMinutes())), 3, false);
	        		}
	        	}
	        });
	        mPos.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent e)
	        	{
	        		if(model.videoPath != null) {
	        			model.fidelityResponse((int)(Math.floor(mediaPlayer.getCurrentTime().toMinutes())), 3, true);
	        		}
	        	}
	        });
	        
	        ccNeg.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent e)
	        	{
	        		if(model.videoPath != null) {
	        			model.fidelityResponse((int)(Math.floor(mediaPlayer.getCurrentTime().toMinutes())), 4, false);
	        		}
	        	}
	        });
	        ccPos.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent e)
	        	{
	        		if(model.videoPath != null) {
	        			model.fidelityResponse((int)(Math.floor(mediaPlayer.getCurrentTime().toMinutes())), 4, true);
	        		}
	        	}
	        });
	        
	        scNeg.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent e)
	        	{
	        		if(model.videoPath != null) {
	        			model.fidelityResponse((int)(Math.floor(mediaPlayer.getCurrentTime().toMinutes())), 5, false);
	        		}
	        	}
	        });
	        scPos.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent e)
	        	{
	        		if(model.videoPath != null) {
	        			model.fidelityResponse((int)(Math.floor(mediaPlayer.getCurrentTime().toMinutes())), 5, true);
	        		}
	        	}
	        });
	        
	        cNeg.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent e)
	        	{
	        		if(model.videoPath != null) {
	        			model.fidelityResponse((int)(Math.floor(mediaPlayer.getCurrentTime().toMinutes())), 6, false);
	        		}
	        	}
	        });
	        cPos.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent e)
	        	{
	        		if(model.videoPath != null) {
	        			model.fidelityResponse((int)(Math.floor(mediaPlayer.getCurrentTime().toMinutes())), 6, true);
	        		}
	        	}
	        });
	        
	        nNeg.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent e)
	        	{
	        		if(model.videoPath != null) {
	        			model.fidelityResponse((int)(Math.floor(mediaPlayer.getCurrentTime().toMinutes())), 7, false);
	        		}
	        	}
	        });
	        nPos.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent e)
	        	{
	        		if(model.videoPath != null) {
	        			model.fidelityResponse((int)(Math.floor(mediaPlayer.getCurrentTime().toMinutes())), 7, true);
	        		}
	        	}
	        });
	        
	        aNeg.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent e)
	        	{
	        		if(model.videoPath != null) {
	        			model.fidelityResponse((int)(Math.floor(mediaPlayer.getCurrentTime().toMinutes())), 8, false);
	        		}
	        	}
	        });
	        aPos.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent e)
	        	{
	        		if(model.videoPath != null) {
	        			model.fidelityResponse((int)(Math.floor(mediaPlayer.getCurrentTime().toMinutes())), 8, true);
	        		}
	        	}
	        });
	        
	        root.setRight(grid);

	        //All code at this point creates the Comments View
	        GridPane grid2 = new GridPane();
			grid2.setAlignment(Pos.CENTER);
	        grid2.setHgap(10);
	        grid2.setVgap(10);
	        grid2.setScaleShape(true);
	        grid2.setPadding(new Insets(25, 25, 25, 25));

	        ListView<String> list = new ListView<String>();
	        /*ObservableList<String> items =FXCollections.observableArrayList (
	            "Single", "Double", "Suite", "Family App");

	        list.setItems(items);*/
	        grid2.add(list, 0, 0);
	        grid2.setPrefWidth(root.getWidth() * .5);

	        Button deleteComment = new Button("Delete Comment");
	        Button printComments = new Button("Print Comments");
	        HBox buttons = new HBox(10);
	        buttons.getChildren().addAll(deleteComment, printComments);
	        buttons.setAlignment(Pos.TOP_LEFT);
	        grid2.add(buttons, 0, 1);

	        TextField fromTime = new TextField("0:00");
	        TextField toTime = new TextField("0:00");
	        HBox time = new HBox(10);
	        time.setAlignment(Pos.TOP_LEFT);
	        time.getChildren().addAll(new Label("FROM:"), fromTime, new Label("TO:"), toTime);
	        grid2.add(time, 0, 2);

	        TextArea comments = new TextArea("Write comments here...");
	        grid2.add(comments, 0, 3);
	        Button add = new Button("Add Comment");
	        grid2.add(add, 0, 4);

	      //displays the Menu Bar "File" option with all options and hotkeys
			MenuBar menuBar = new MenuBar();
			Menu menuFile = new Menu("File");
			MenuItem openVideo = new MenuItem("Open Video");
			openVideo.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
			MenuItem openReview = new MenuItem("Open Review");
			openReview.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+O"));
			openReview.setDisable(true);
			MenuItem close = new MenuItem("Close Project");
			close.setDisable(true);
			close.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
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
			prev.setAccelerator(KeyCombination.keyCombination("Ctrl+K"));
			prev.setDisable(true);
			MenuItem next = new MenuItem("Next Section");
			next.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));
			next.setDisable(true);
			MenuItem rewind = new MenuItem("Rewind 30 Seconds");
			rewind.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
			rewind.setDisable(true);
			MenuItem foreward = new MenuItem("Fast Forward 30 Seconds");
			foreward.setAccelerator(KeyCombination.keyCombination("Ctrl+T"));
			foreward.setDisable(true);
	        MenuItem autoPause = new MenuItem("Toggle Auto-Pause [Enabled]");

			//displays the Menu Bar "Help" option with all options and hotkeys
			Menu menuHelp = new Menu("Help");
			MenuItem about = new MenuItem("About");

			autoPause.setOnAction(event -> {
			    if(boolAutoPause) {
			        boolAutoPause = false;
			        autoPause.setText("Toggle Auto-Pause [Disabled]");
			    }
			    else {
			        boolAutoPause = true;
			        autoPause.setText("Toggle Auto-Pause [Enabled]");
			    }
			});

			close.setOnAction(event -> {
			    if (mediaPlayer != null) mediaPlayer.dispose();
			    outOfTenLabel.setText("No Video Open");
                mediaBar.setVisible(false);
                close.setDisable(true);
                saveReview.setDisable(false);
                saveReviewAs.setDisable(false);
                comment.setDisable(false);
                next.setDisable(true);
                prev.setDisable(true);
                rewind.setDisable(true);
                foreward.setDisable(true);
			});


			openVideo.setOnAction(event -> {//when open is selected, gets info from selected csv file and enables test options

			    FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Video File");
				File file = fileChooser.showOpenDialog(primaryStage);
				if(file != null) {
				    if (mediaPlayer != null) mediaPlayer.dispose();
					model.openVideo(file);
					close.setDisable(false);
					saveReview.setDisable(false);
					saveReviewAs.setDisable(false);
					comment.setDisable(false);
					next.setDisable(false);
					prev.setDisable(false);
					rewind.setDisable(false);
					foreward.setDisable(false);
					
					caNeg.setSelected(false);
					caPos.setSelected(false);
					coNeg.setSelected(false);
					coPos.setSelected(false);
					tNeg.setSelected(false);
					tPos.setSelected(false);
					mNeg.setSelected(false);
					mPos.setSelected(false);
					ccNeg.setSelected(false);
					ccPos.setSelected(false);
					scNeg.setSelected(false);
					scPos.setSelected(false);
					cNeg.setSelected(false);
					cPos.setSelected(false);
					nNeg.setSelected(false);
					nPos.setSelected(false);
					aNeg.setSelected(false);
					aPos.setSelected(false);
					
					media =  new Media(model.videoPath);
					mediaPlayer = new MediaPlayer(media);
					mediaPlayer.setAutoPlay(true);
		            mediaView = new MediaView(mediaPlayer);
		              mediaView.setPreserveRatio(true);
		                mediaPane.getChildren().clear();
		                mediaPane.getChildren().add(mediaView);
		                mediaView.setFitWidth(0.5 * root.getWidth());
		                HBox hbVideo = new HBox();
		                hbVideo.setStyle("-fx-background-color: black;");
		                hbVideo.setAlignment(Pos.CENTER);
		                hbVideo.getChildren().add(mediaPane);
		                root.setCenter(hbVideo);
		                mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
		                    public void invalidated(Observable ov) {
		                        updateValues();
		                    }
		                });
		                mediaPlayer.currentTimeProperty().addListener((observable) ->{
		                    if (boolAutoPause) {
		                        if (Math.floor(mediaPlayer.getCurrentTime().toSeconds()) % 60 == 0) {
		                            if (willPause) {
		                                mediaPlayer.pause();
		                                willPause = false;
		                            }
		                        }
		                        else {
		                            willPause = true;
		                        }
		                    }
		                    int timeSlot = (int)(Math.floor(mediaPlayer.getCurrentTime().toMinutes()) + 1);
		                    String oldText = outOfTenLabel.getText();
		                    outOfTenLabel.setText(timeSlot + " of 10 minutes");
		                    if (!(oldText.equals(outOfTenLabel.getText()))) {
		                    	caNeg.setSelected(false);
		    					caPos.setSelected(false);
		    					coNeg.setSelected(false);
		    					coPos.setSelected(false);
		    					tNeg.setSelected(false);
		    					tPos.setSelected(false);
		    					mNeg.setSelected(false);
		    					mPos.setSelected(false);
		    					ccNeg.setSelected(false);
		    					ccPos.setSelected(false);
		    					scNeg.setSelected(false);
		    					scPos.setSelected(false);
		    					cNeg.setSelected(false);
		    					cPos.setSelected(false);
		    					nNeg.setSelected(false);
		    					nPos.setSelected(false);
		    					aNeg.setSelected(false);
		    					aPos.setSelected(false);
		                    }
                        });
		                mediaPlayer.setOnPlaying(new Runnable() {
		                    public void run() {
		                        if (stopRequested) {
		                            mediaPlayer.pause();
		                            stopRequested = false;
		                        } else {
		                            playButton.setText("||");
		                        }
		                    }
		                });

		                mediaPlayer.setOnPaused(new Runnable() {
		                    public void run() {
		                       // System.out.println("onPaused");
		                        playButton.setText(">");
		                    }
		                });

		                mediaPlayer.setOnReady(new Runnable() {
		                    public void run() {
		                        duration = mediaPlayer.getMedia().getDuration();
		                        updateValues();
		                    }
		                });

		                mediaPlayer.setCycleCount(repeat ? MediaPlayer.INDEFINITE : 1);
		                mediaPlayer.setOnEndOfMedia(new Runnable() {
		                    public void run() {
		                        if (!repeat) {
		                            playButton.setText(">");
		                            stopRequested = true;
		                            atEndOfMedia = true;
		                        }
		                    }
		                });
		                mediaBar.setVisible(true);
				}
			});

			openReview.setOnAction(event -> {//when open is selected, gets info from selected csv file and enables test options
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Text File");
				File file = fileChooser.showOpenDialog(primaryStage);
				if(file != null) {
					model.openVideo(file);
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
				root.setRight(grid);
				analyze.setDisable(true);
				comment.setDisable(false);
			});

			comment.setOnAction(event -> {//change view to show the analysis screen
				root.setRight(grid2);
				analyze.setDisable(false);
				comment.setDisable(true);
			});

			rewind.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent e) {
	                Duration current = mediaPlayer.getCurrentTime();
	                double timeInSeconds = current.toSeconds();
	                if (Math.floor(timeInSeconds) % 30 == 0 && timeInSeconds >= 30) {
	                    timeInSeconds -= 30;
	                }
	                Duration newTime = new Duration( Math.floor(timeInSeconds / 30) * 30000);
	                willPause = false;
	                mediaPlayer.seek(newTime);
	            }
	        });

			foreward.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent e) {
	                Duration current = mediaPlayer.getCurrentTime();
	                double timeInSeconds = current.toSeconds();
	                timeInSeconds += 30 - Math.floor(timeInSeconds) % 30;
	                Duration newTime = new Duration( Math.floor(timeInSeconds / 30) * 30000);
	                willPause = false;
	                mediaPlayer.seek(newTime);
	            }
	        });

            prev.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    Duration current = mediaPlayer.getCurrentTime();
                    double timeInSeconds = current.toSeconds();
                    if (Math.floor(timeInSeconds) % 60 == 0 && timeInSeconds >= 60) {
                        timeInSeconds -= 60;
                    }
                    Duration newTime = new Duration( Math.floor(timeInSeconds / 60) * 60000);
                    willPause = false;
                    mediaPlayer.seek(newTime);
                }
            });

	          next.setOnAction(new EventHandler<ActionEvent>() {
	                public void handle(ActionEvent e) {
	                    Duration current = mediaPlayer.getCurrentTime();
	                    double timeInSeconds = current.toSeconds();
	                    timeInSeconds += 60 - Math.floor(timeInSeconds) % 60;
	                    Duration newTime = new Duration( Math.floor(timeInSeconds / 60) * 60000);
	                    willPause = false;
	                    mediaPlayer.seek(newTime);
	                }
	            });

			menuFile.getItems().addAll(openVideo, openReview, saveReview, saveReviewAs, close, quit);
			menuNavigate.getItems().addAll(analyze, comment, prev, next, rewind, foreward, autoPause);
			menuHelp.getItems().addAll(about);
			menuBar.getMenus().addAll(menuFile, menuNavigate, menuHelp);
			root.setTop(menuBar);

	        //Loads application
			primaryStage.setTitle("Austism Video Analyzer");
			primaryStage.setScene(scene);
			primaryStage.setResizable(true);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    protected void updateValues() {
        if (playTime != null && timeSlider != null && volumeSlider != null) {
            Platform.runLater(new Runnable() {
                public void run() {
                    Duration currentTime = mediaPlayer.getCurrentTime();
                    playTime.setText(formatTime(currentTime, duration));
                    timeSlider.setDisable(duration.isUnknown());
                    if (!timeSlider.isDisabled()
                            && duration.greaterThan(Duration.ZERO)
                            && !timeSlider.isValueChanging()) {
                        timeSlider.setValue(currentTime.divide(duration).toMillis()
                                * 100.0);
                    }
                    if (!volumeSlider.isValueChanging()) {
                        volumeSlider.setValue((int) Math.round(mediaPlayer.getVolume()
                                * 100));
                    }
                }
            });
        }
    }

    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60
                    - durationMinutes * 60;
            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d", elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }

	public static void main(String[] args) {
		launch(args);
	}
}
