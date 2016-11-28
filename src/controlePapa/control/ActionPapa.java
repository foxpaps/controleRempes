package controlePapa.control;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JFrame;

import controlePapa.MainControlePapa;
import controlePapa.data.ConfigParental;
import controlePapa.data.Day;
import controlePapa.data.ParamAccess;
import controlePapa.data.Planning;
import controlePapa.ihm.AlerteDialogue;
import controlePapa.ihm.MenuPapa;

public class ActionPapa implements Action {

	private static ActionPapa actionPapa = null;

	public static ActionPapa getInstance() 
	{
		if (actionPapa == null) {
			actionPapa =  new ActionPapa();
		} 
		return actionPapa;
	}

	public ParamAccess initData() {
		ParamAccess param = new ParamAccess();

		param.setPeriferique("mon ordinateur");
		Planning semaine = new Planning();

		Day day1= new Day ("Lundi");
		Day day2= new Day ("Mardi");
		Day day3= new Day ("Mercredi");
		Day day4= new Day ("Jeudi");
		Day day5= new Day ("Vendredi");
		Day day6= new Day ("Samedi");
		Day day7= new Day ("Dimanche");
		Day day8= new Day ("Special");

		semaine.setDay(0, day1);
		semaine.setDay(1, day2);
		semaine.setDay(2, day3);
		semaine.setDay(3, day4);
		semaine.setDay(4, day5);
		semaine.setDay(5, day6);
		semaine.setDay(6, day7);
		semaine.setDay(7, day8);

		param.setPlanning(semaine);
		return param;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals(MenuPapa.REFRESH)) {
			System.out.println("Demande refresh");		
			ThreadRefreshAccess.refreshParamAcces();
		}	
		else if (e.getActionCommand().equals(MenuPapa.ALERTE)) {
			System.out.println("Demande Alerte");		
			alerte();
		}			 	 
	} 

	private void alerte() {
		final JFrame alerteFrame = MainControlePapa.mainFrame;
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
