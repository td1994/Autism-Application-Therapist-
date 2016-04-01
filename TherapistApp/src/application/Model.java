package application;

import java.io.File;

public class Model {
	public String videoPath;
	public String filePath;
	
	public void openVideo(File file){
		try {
			videoPath = "file://" + file.getCanonicalPath().replace(" ", "%20").replace("\\", "/").replaceAll("^.:", "");
			//TODO: have video loaded into media player.
		}
		catch (Exception e) {
			System.out.println(e);
		}
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
}
