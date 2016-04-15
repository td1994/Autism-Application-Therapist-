package application;

import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaMarkerEvent;
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
    private boolean mediaActive = false;
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
			HBox analysisView = new HBox(), commentView = new HBox();
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

			//Video View

			/*Placing Video (Needs Video Selected before Uploading
			mediaPane = new StackPane();
			mediaPane.setId("mediaPane");
			media = new Media(model.videoPath);
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
			root.setLeft(hbVideo);*/

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

	        TextField childAttnGrade = new TextField("Grade");
	        grid.add(childAttnGrade, 1, 0);
	        TextField clearOppGrade = new TextField("Grade");
	        grid.add(clearOppGrade, 1, 1);
	        TextField totalGrade = new TextField("Grade");
	        grid.add(totalGrade, 1, 2);
	        TextField maintenanceGrade = new TextField("Grade");
	        grid.add(maintenanceGrade, 1, 3);
	        TextField childChoiceGrade = new TextField("Grade");
	        grid.add(childChoiceGrade, 1, 4);
	        TextField sharedControlGrade = new TextField("Grade");
	        grid.add(sharedControlGrade, 1, 5);
	        TextField contingentGrade = new TextField("Grade");
	        grid.add(contingentGrade, 1, 6);
	        TextField naturalGrade = new TextField("Grade");
	        grid.add(naturalGrade, 1, 7);
	        TextField attemptsGrade = new TextField("Grade");
	        grid.add(attemptsGrade, 1, 8);
	        root.setRight(grid);

	        //All code at this point creates the Comments View
	        GridPane grid2 = new GridPane();
			grid2.setAlignment(Pos.CENTER);
	        grid2.setHgap(10);
	        grid2.setVgap(10);
	        grid2.setScaleShape(true);
	        grid2.setPadding(new Insets(25, 25, 25, 25));

	        ListView<String> list = new ListView<String>();
	        ObservableList<String> items =FXCollections.observableArrayList (
	            "Single", "Double", "Suite", "Family App");
	        list.setItems(items);
	        grid2.add(list, 0, 0);

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
					rewind.setDisable(false);
					foreward.setDisable(false);
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
