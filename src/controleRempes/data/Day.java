package controleRempes.data;

import controleRempes.data.ParamAccess.StatusAutorisation;

public class Day {
	
	private String _nom;
	
	private StatusAutorisation[] _autorize = new StatusAutorisation[48];
	
	Day() {
		for (int i = 0 ; i<48; i++) {
			_autorize [i] = StatusAutorisation.undefine;
		}
	}
	
	public Day(String nom) {
		_nom = nom;
		for (int i = 0 ; i<48; i++) {
			_autorize [i] = StatusAutorisation.undefine;
		}
	}

	public void setAutorisation(int pos, StatusAutorisation val) {
		_autorize [pos]= val;
	}
	
	public StatusAutorisation getAutorisation(int pos) {
		if (pos==48) {
			System.out.println("");
		}
		return _autorize [pos];
	}
	
	public String getNom() {
		return _nom;
	}

	public void setNom(String _nom) {
		this._nom = _nom;
	}
	
}
