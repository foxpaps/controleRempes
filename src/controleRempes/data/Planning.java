package controleRempes.data;

import java.util.Calendar;

import controleRempes.data.ParamAccess.StatusAutorisation;

public class Planning {

	public enum TypeFiltrage { Planification, Bloque, AccesWeb, Autorise, Indefini};
	private TypeFiltrage _filtrage;
	
	private Day[] _days = new Day[8];
	
	
	public TypeFiltrage getFiltrage() {
		return _filtrage;
	}

	public void setFiltrage(TypeFiltrage _filtrage) {
		this._filtrage = _filtrage;
	}
	
	public Day getDay(int pos) {
		return _days[pos];
	}
	
	public void setDay(int pos,Day day) {
		_days[pos] = day;
	}
	
	public StatusAutorisation getStatus(Calendar calendar) {
		StatusAutorisation status = null;
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int posInDay = hour*2 + (minute<29 ? 0 : 1);
		int numDay = calendar.get(Calendar.DAY_OF_WEEK);
		numDay = (numDay+5)%7 ;
		Day day = getDay(numDay);
		status = day.getAutorisation(posInDay);
		return status;
	}
	
	public void setStatus(Calendar calendar, StatusAutorisation status) {
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int posInDay = hour*2 + (minute<29 ? 0 : 1);
		int numDay = calendar.get(Calendar.DAY_OF_WEEK);
		numDay = (numDay+5)%7 ;
		Day day = getDay(numDay);
		day.setAutorisation(posInDay,status);

	}
	
}
