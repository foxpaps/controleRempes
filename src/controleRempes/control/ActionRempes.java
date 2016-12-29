package controleRempes.control;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.Action;

import org.json.JSONObject;

import controleRempes.MainControleRempes;
import controleRempes.control.freebox.LogFreebox;
import controleRempes.data.ConfigParental;
import controleRempes.data.Day;
import controleRempes.data.ParamAccess;
import controleRempes.data.Planning;
import controleRempes.ihm.AlerteDialog;
import controleRempes.ihm.HelpConnectionDialog;
import controleRempes.ihm.MenuRempes;

public class ActionRempes implements Action {

	private static ActionRempes actionRempes = null;
	private GuiControlRempes guiControl = null;

	public static ActionRempes getInstance(final GuiControlRempes guiControl) 
	{
		if (actionRempes == null) {
			actionRempes =  new ActionRempes();
			actionRempes.guiControl = guiControl;
		} 
		return actionRempes;
	}

	public ParamAccess initData() {
		final ParamAccess param = new ParamAccess();

		param.setPeriferique("mon ordinateur");
		Planning semaine = new Planning();

		final DateFormatSymbols dfs = new DateFormatSymbols(Locale.getDefault());
		String weekdays[] = dfs.getWeekdays();

		semaine.setDay(0, new Day (weekdays[Calendar.MONDAY]));
		semaine.setDay(1, new Day (weekdays[Calendar.TUESDAY]));
		semaine.setDay(2, new Day (weekdays[Calendar.WEDNESDAY]));
		semaine.setDay(3, new Day (weekdays[Calendar.THURSDAY]));
		semaine.setDay(4, new Day (weekdays[Calendar.THURSDAY]));
		semaine.setDay(5, new Day (weekdays[Calendar.SATURDAY]));
		semaine.setDay(6,  new Day (weekdays[Calendar.SUNDAY]));
		semaine.setDay(7, new Day ("Special"));

		param.setPlanning(semaine);
		return param;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals(MenuRempes.REFRESH)) {
			System.out.println("Demande refresh");		
			ThreadRefreshAccess.getInstance(guiControl).refreshParamAcces();
		}	
		else if (e.getActionCommand().equals(MenuRempes.ALERTE)) {
			System.out.println("Demande Alerte");		
			alerte();
		} else if (e.getActionCommand().equals(MenuRempes.CONNECION_HELP)) {
			System.out.println("Demande Aide");
			help();		
		} 
		
		if (e.getActionCommand().equals(MenuRempes.AUTORIZATION)) {
			System.out.println("Demande autorisation");			
			JSONObject resultAutorise = LogFreebox.getInstance().authorize();

			if (resultAutorise.getBoolean("success")) {
				guiControl.showInfo(LogFreebox.MESSAGES_BUNDLE.getString("AUTHORIZE_OK"));
			} else {
				guiControl.showInfo(LogFreebox.MESSAGES_BUNDLE.getString("AUTHORIZE_KO"));
			}			 
		} else if (e.getActionCommand().equals(MenuRempes.CONNECTION_FREE)) {
			System.out.println("Demande connexion");
			LogFreebox.getInstance().connexion();

		} else if (e.getActionCommand().equals(MenuRempes.DECONNECTION)) {
			System.out.println("Demande deconnexion");
			//JSONObject result =  
			LogFreebox.getInstance().logout();
			LogFreebox.getInstance().setCurrentSession(null);
		}  
	}

	private void help() {
		//HelpConnectionDialog help = 
		new HelpConnectionDialog(MainControleRempes.mainFrame);		

	}

	private void alerte() {
		new AlerteDialog(MainControleRempes.mainFrame,ConfigParental.getInstance());
	}

	@Override
	public Object getValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putValue(String key, Object value) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setEnabled(boolean b) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub
	}

}
