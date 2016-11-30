package controleRempes.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class ConfigParental {

	private static final String FILE_INIT_NAME = "ControlPapa.init";

	private static final String FREQUENCE_REFRESH = "frequenceRefresh";

	private static final String BLOQUAGE = "AlerteBloquage";	
	private static final String BLOQUAGE_BEFORE = "AlerteBloquageBefore";
	private static final String BLOQUAGE_SOUND = "AlerteBloquageSound";

	private static final String WEB_UNIQUE =  "AlerteWebUnique";
	private static final String WEB_UNIQUE_BEFORE = "AlerteWebUniqueBefore";
	private static final String WEB_UNIQUE_SOUND = "AlerteWebUniqueSound";

	private static final String AUTORISATION =  "AlerteAutorisation";
	private static final String AUTORISATION_BEFORE = "AlerteAutorisationBefore";
	private static final String AUTORISATION_SOUND = "AlerteAutorisationSound";

	private static final String newline = System.getProperty("line.separator");

	private static  ConfigParental _instance = null;

	// frequence de rafraichissement des donn�es du control parental; 5 minutes par defaut
	private int frequenceRefresh = 5*60*1000; 

	//private String listeAlerte[] = {"Alerte Bloquage", "Alerte Web Unique", "Alerte Autorisation"};
	private Alerte alerteBloquage = null;
	private Alerte alerteWebUnique = null;
	private Alerte alerteAutorisation = null;

	public static ConfigParental getInstance() {
		if (_instance==null) {
			_instance = new ConfigParental();
		}
		return _instance;
	}

	private ConfigParental() {
		Properties properties = new Properties();
		try (FileInputStream input = new FileInputStream(FILE_INIT_NAME)) {		
			properties.load(input);

			frequenceRefresh = Integer.parseInt(properties.getProperty(FREQUENCE_REFRESH,String.valueOf(frequenceRefresh)));

			alerteBloquage = new Alerte("Alerte Bloquage");
			alerteBloquage.setAlerte(Boolean.valueOf(properties.getProperty(BLOQUAGE,"false")));
			alerteBloquage.setAlerteBloquageBefore(Integer.valueOf(properties.getProperty(BLOQUAGE_BEFORE,"0")));
			
			String str = properties.getProperty(BLOQUAGE_SOUND,"");
			File file = new File(str);
			file.getPath();
			//String srt = file.getCanonicalPath();
			
			alerteBloquage.setAlerteBloquageSound(new File(properties.getProperty(BLOQUAGE_SOUND,"")).getPath());

			alerteWebUnique = new Alerte("Alerte Web Unique");
			alerteWebUnique.setAlerte(Boolean.valueOf(properties.getProperty(WEB_UNIQUE,"false")));
			alerteWebUnique.setAlerteBloquageBefore(Integer.valueOf(properties.getProperty(WEB_UNIQUE_BEFORE,"0")));
			alerteWebUnique.setAlerteBloquageSound(new File(properties.getProperty(WEB_UNIQUE_SOUND,"")).getPath());

			alerteAutorisation = new Alerte("Alerte Autorisation");
			alerteAutorisation.setAlerte(Boolean.valueOf(properties.getProperty(AUTORISATION,"false")));
			alerteAutorisation.setAlerteBloquageBefore(Integer.valueOf(properties.getProperty(AUTORISATION_BEFORE,"0")));
			alerteAutorisation.setAlerteBloquageSound(new File(properties.getProperty(AUTORISATION_SOUND,"")).getPath());

		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public int getFrequenceRefresh() {
		return frequenceRefresh;
	}

	public void setFrequenceRefresh(int frequenceRefresh) {
		this.frequenceRefresh = frequenceRefresh;
	}

	public Alerte getAlerteBloquage() {
		return alerteBloquage;
	}

	public void setAlerteBloquage(Alerte alerteBloquage) {
		this.alerteBloquage = alerteBloquage;
	}

	public Alerte getAlerteWebUnique() {
		return alerteWebUnique;
	}

	public void setAlerteWebUnique(Alerte alerteWebUnique) {
		this.alerteWebUnique = alerteWebUnique;
	}

	public Alerte getAlerteAutorisation() {
		return alerteAutorisation;
	}

	public void setAlerteAutorisation(Alerte alerteAutorisation) {
		this.alerteAutorisation = alerteAutorisation;
	}

	public void saveAlerte() {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_INIT_NAME),"UTF-8"))) {

			writer.write ("; frequence de rafraichissement des donn�es du control parental en minutes" + newline);
			writer.write (FREQUENCE_REFRESH + "=" + frequenceRefresh + newline);			
			writer.write (newline);

			writer.write (";Alerte Bloquage" + newline);
			writer.write (BLOQUAGE + "=" + alerteBloquage.getAlerte().toString() + newline);
			writer.write (BLOQUAGE_BEFORE + "=" + alerteBloquage.getAlerteBefore() + newline);
			writer.write (BLOQUAGE_SOUND + "=" + alerteBloquage.getAlerteBloquageSoundPath() + newline);
			writer.write (newline);

			writer.write (";Alerte Web Unique" + newline);
			writer.write (WEB_UNIQUE + "=" + alerteWebUnique.getAlerte().toString() + newline);
			writer.write (WEB_UNIQUE_BEFORE + "=" + alerteWebUnique.getAlerteBefore() + newline);
			writer.write (WEB_UNIQUE_SOUND + "=" + alerteWebUnique.getAlerteBloquageSoundPath() + newline);
			writer.write (newline);

			writer.write (";Alerte Autorisation" + newline);
			writer.write (AUTORISATION + "=" + alerteAutorisation.getAlerte().toString() + newline);
			writer.write (AUTORISATION_BEFORE + "=" + alerteAutorisation.getAlerteBefore() + newline);
			writer.write (AUTORISATION_SOUND + "=" + alerteAutorisation.getAlerteBloquageSoundPath() + newline);	
			writer.write (newline);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
