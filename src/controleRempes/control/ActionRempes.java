package controleRempes.control;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.Action;
import javax.swing.JFrame;

import controleRempes.MainControleRempes;
import controleRempes.data.ConfigParental;
import controleRempes.data.Day;
import controleRempes.data.ParamAccess;
import controleRempes.data.Planning;
import controleRempes.ihm.AlerteDialogue;
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
		}			 	 
	} 

	private void alerte() {
		final JFrame alerteFrame = MainControleRempes.mainFrame;
		new AlerteDialogue(alerteFrame,ConfigParental.getInstance());
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
