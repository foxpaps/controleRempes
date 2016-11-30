package controleRempes.control;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MP3Player implements Runnable  {

	private static Path soundFilePath;
	
	public static void play(Path soundPath) {
		soundFilePath = soundPath;
		 (new Thread(new MP3Player())).start();
	}
	
	@Override
	public void run() {
		playMp3(soundFilePath);
	}
	
	private static void playMp3(Path soundPath) {
		try {
			URL url = soundPath.toFile().toURI().toURL();
			Player mp3player = null;
			BufferedInputStream in = null;
			in = new BufferedInputStream(url.openStream());
			mp3player = new Player(in);
			mp3player.play();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JavaLayerException e) {
			e.printStackTrace();
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}


}
