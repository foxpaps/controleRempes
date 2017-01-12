package controleRempes.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import controleRempes.control.GuiControlRempes;
import controleRempes.data.ParamAccess.SpecialDayMode;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;

public class Vacances  implements Cloneable {

	private String description;	
	private Date debut;
	private Date fin;
	private String summary;

	public static final Map<SpecialDayMode, List<Vacances>> listeVacancesParZone = new HashMap<SpecialDayMode, List<Vacances>>();
	private static final int FREQ_DOWNLOAD = 90;
	public final static char LF  = (char) 0x0A;

	public Object clone() {
		Vacances vacances = null;
		try {
			vacances = (Vacances) super.clone();
		} catch(CloneNotSupportedException cnse) {
			cnse.printStackTrace(System.err);
		}

		// on renvoie le clone
		return vacances;
	}

	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Date getDebut() {
		return debut;
	}
	public void setDebut(Date debut) {
		this.debut = debut;
	}
	public Date getFin() {
		return fin;
	}
	public void setFin(Date fin) {
		this.fin = fin;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static void loadVacancesScolaireParZone(GuiControlRempes guiControl) {

		downloadLeCalendrierScolaire(guiControl);

		final List<Vacances> listVacances = loadVacancesScolaire();

		final List<Vacances> listVacancesA = filtreVacanceScolaire(listVacances, SpecialDayMode.fr_school_holidays_a);
		listeVacancesParZone.put(SpecialDayMode.fr_school_holidays_a, listVacancesA);
		final List<Vacances> listVacancesB = filtreVacanceScolaire(listVacances, SpecialDayMode.fr_school_holidays_b);
		listeVacancesParZone.put(SpecialDayMode.fr_school_holidays_b, listVacancesB);
		final List<Vacances> listVacancesC = filtreVacanceScolaire(listVacances, SpecialDayMode.fr_school_holidays_c);
		listeVacancesParZone.put(SpecialDayMode.fr_school_holidays_c, listVacancesC);

	}

	private static void downloadLeCalendrierScolaire(GuiControlRempes guiControl) {

		String UrlLeCalendrierScolaire = ConfigParental.getInstance().getUrlDuCalendrierScolaire();
		if (UrlLeCalendrierScolaire==null) {
			return;
		}
		System.out.println("UrlLeCalendrierScolaire = " + UrlLeCalendrierScolaire);

		String fileOutStr = UrlLeCalendrierScolaire.substring(UrlLeCalendrierScolaire.lastIndexOf("/")+1,UrlLeCalendrierScolaire.length());

		File calendrier = new  File (fileOutStr);
		if (calendrier.exists()) {  // on essaye de recharger aprés 90 jours
			Date modified = new Date(calendrier.lastModified());
			long old = (new Date()).getTime() - modified.getTime() ;
			long oldInDay = old / (1000*3600*24);

			if (oldInDay<FREQ_DOWNLOAD) {
				return;
			}
		}

		try {
			URL urlGouv = new URL(UrlLeCalendrierScolaire);

			File calendrierTmp = new  File ("tmp" + fileOutStr);

			try (ReadableByteChannel rbc = Channels.newChannel(urlGouv.openStream());
					FileOutputStream fos = new FileOutputStream(calendrierTmp);)
			{
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

				if (transformIcs(calendrierTmp,calendrier)) {
					guiControl.showInfo(ConfigParental.MESSAGES_BUNDLE.getString("INFO_COPY_VACANCES"));
				}

			} catch (IOException e) {
				e.printStackTrace();
				guiControl.showError(ConfigParental.MESSAGES_BUNDLE.getString("COPY_ERROR_VACANCES"));

			}


		}catch (MalformedURLException e) {
			if (guiControl!=null) {
				guiControl.showError(ConfigParental.MESSAGES_BUNDLE.getString("URL_MAL_FORMED"));
			}
			e.printStackTrace();
		} 
	}

	private static String ltrim(String s) {
		int i = 0;
		while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
			i++;
		}
		return s.substring(i);
	}

	private static boolean transformIcs(final File calendrierTmp,final File icsFile) throws IOException {

		// verifier si le fichier est bien un ics d'aprés la première ligne
		try (	BufferedReader br = new BufferedReader(new FileReader(calendrierTmp));) {
			String line = br.readLine();
			if (!line.contains("BEGIN:VCALENDAR")) {
				Files.delete(calendrierTmp.toPath());
				return false;
			}
		}
		
		// Le fichiers ics générés ajout des lf à la position 75,
		// il le faut les supprimer
		try (	FileWriter writer = new FileWriter(icsFile, false);
				BufferedReader br = new BufferedReader(new FileReader(calendrierTmp));) {
			String line;
			String lineBuff="";
			while((line = br.readLine()) != null) {
				if (line.length()>=74) {
					lineBuff += ltrim(line);
				} else {
					writer.write(lineBuff + ltrim(line) + LF);
					lineBuff="";
					writer.flush();
				}
			}

			Files.move(calendrierTmp.toPath(), icsFile.toPath(), StandardCopyOption.REPLACE_EXISTING );
		} 
		return true;
	}

	private static List<Vacances> filtreVacanceScolaire(final List<Vacances> listVacances, final SpecialDayMode frSchoolHolidays) {

		List<Vacances> listFiltreZone = new ArrayList<Vacances>();
		String zone="";
		switch (frSchoolHolidays) {
		case fr_school_holidays_a :
			zone = "A";
			break;
		case fr_school_holidays_b :
			zone = "B";
			break;
		case fr_school_holidays_c :
			zone = "C";
			break;
		default:
			return listFiltreZone; // or null ?
		}

		for (Vacances vacances : listVacances) {
			if (vacances.getSummary().contains(zone)) {
				listFiltreZone.add(vacances);
			}
		}
		return listFiltreZone;
	}

	public static List<Vacances> loadVacancesScolaire() {

		final String PRO_START = "DTSTART";
		final String PRO_END = "DTEND";

		final String PRO_DESC = "DESCRIPTION";
		final String PRO_SUMMARY = "SUMMARY";
		final String VAC_ETE_DEB = "Vacances d'été";	
		final String VAC_ETE_FIN = "Rentrée scolaire des élèves";
		final String VACANCES = "Vacances";

		SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd"); 

		List<Vacances> lesVacances = new ArrayList<Vacances>();
		try {
			FileInputStream fiStr = new FileInputStream("Calendrier_Scolaire_Zones_A_B_C.ics");

			CalendarBuilder builder = new CalendarBuilder();
			net.fortuna.ical4j.model.Calendar calendar = builder.build(fiStr);


			Vacances vacancesEte = null;
			for (Iterator<?> i = calendar.getComponents().iterator(); i.hasNext();) {
				Component component = (Component) i.next();
				//System.out.println("Component [" + component.getName() + "]");
				if (component.getName().equals("VEVENT")) {
					boolean isVacanceEteFin = false;
					Vacances vacances = new Vacances();
					for (Iterator<?> j = component.getProperties().iterator(); j.hasNext();) {
						Property property = (Property) j.next();
						//System.out.println("Property [" + property.getName() + ", " + property.getValue() + "]");

						if (property.getName().equals(PRO_DESC)) {
							if (property.getValue().startsWith(VAC_ETE_DEB)) {
								vacances.setDescription(property.getValue());								
								vacancesEte = vacances;
								lesVacances.add(vacances);
							} else if (property.getValue().startsWith(VACANCES)) {
								vacances.setDescription(property.getValue());
								lesVacances.add(vacances);
							} else if (property.getValue().startsWith(VAC_ETE_FIN)) {
								vacances = vacancesEte;
								isVacanceEteFin=true;
							}
						} else	if (property.getName().equals(PRO_SUMMARY) && vacances!=null) {
							vacances.setSummary(property.getValue());
						} else if (property.getName().equals(PRO_START) && vacances!=null) {
							final Date debut = dt.parse(property.getValue());
							if (vacances == vacancesEte && isVacanceEteFin) {	// alors c'est la fin des vacances d'été
								vacances.setFin(debut);
								vacancesEte = null;
							} else {
								vacances.setDebut(debut);
							}
						} else if (property.getName().equals(PRO_END)) {
							final Date fin = dt.parse(property.getValue());
							vacances.setFin(fin);
						}
					}

				}
			}
		} catch (IOException | ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return lesVacances;
	}

	public static boolean isVacancesScolaire(Calendar calendarDate, SpecialDayMode frSchoolHolidays) {

		final List<Vacances> listVacances = listeVacancesParZone.get(frSchoolHolidays);

		final Date currentDate = calendarDate.getTime();
		for (Vacances vacances : listVacances) {
			final Date debut = vacances.getDebut();
			final Date fin = vacances.getFin();

			if (debut!=null && fin!=null&& 
					debut.compareTo(currentDate) <=0 &&
					currentDate.compareTo(fin)<=0) {
				return true;
			}
		}
		return false;
	}
}
