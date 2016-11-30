package controlePapa.data;

import java.io.File;
import java.net.MalformedURLException;

public class Alerte {

	private String alerteName;
	private Boolean alerte=false;
	private int	alerteBefore=0;
	private int	alerteRepeat=0;

	private  String alerteSound="";
	
	public Alerte(String alerteName) {
		this.alerteName = alerteName;
	}

	public String getAlerteName() {
		return alerteName;
	}

	public Boolean getAlerte() {
		return alerte;
	}

	public void setAlerte(Boolean alerte) {
		this.alerte = alerte;
	}

	public int getAlerteBefore() {
		return alerteBefore;
	}

	public void setAlerteBloquageBefore(int alerteBloquageBefore) {
		this.alerteBefore = alerteBloquageBefore;
	}

	public String getAlerteBloquageSound() {
		return alerteSound;
	}
	
	public String getAlerteBloquageSoundPath() throws MalformedURLException {
		File file = new File (alerteSound);
		return file.toURI().getPath();
	}
	
	public void setAlerteBloquageSound(String alerteBloquageSound) {
		this.alerteSound = alerteBloquageSound;
	}

	public int getAlerteRepeat() {
		return alerteRepeat;
	}

	public void setAlerteRepeat(int alerteRepeat) {
		this.alerteRepeat = alerteRepeat;
	}

	public void setDefault(String sound) {
		alerte = true;
		alerteBefore = 5;
		alerteSound = sound;
	}

}
