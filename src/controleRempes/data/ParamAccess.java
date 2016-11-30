package controleRempes.data;


public class ParamAccess {

	public enum StatusAutorisation { allowed, webonly, denied , undefine };
	public enum SchedulingMode {forced, temporary, planning, Indefini};
	
	private String _periferique;
	private SchedulingMode schedulingMode = SchedulingMode.Indefini;
	
	private Planning planning;
	// for tmp mode
	private StatusAutorisation tmpMode;
	private int tmpModeExpire;
	
	public String getPeriferique() {
		return _periferique;
	}

	public void setPeriferique(String _periferique) {
		this._periferique = _periferique;
	}

	public Planning getPlanning() {
		return planning;
	}

	public void setPlanning(Planning _semaine) {
		this.planning = _semaine;
	}

	public SchedulingMode getSchedulingMode() {
		return schedulingMode;
	}

	public void setSchedulingMode(SchedulingMode schedulingMode) {
		this.schedulingMode = schedulingMode;
	}

	public StatusAutorisation getTmpMode() {
		return tmpMode;
	}

	public void setTmpMode(StatusAutorisation tmpMode) {
		this.tmpMode = tmpMode;
	}

	public int getTmpModeExpire() {
		return tmpModeExpire;
	}

	public void setTmpModeExpire(int tmpModeExpire) {
		this.tmpModeExpire = tmpModeExpire;
	}

	static public String GetStatusNameAutorisation(StatusAutorisation status) {
		String name = "Status de connection : Indéfini";
		switch (status)  {
		case allowed:
			 name = "Status de connection : Autorisé";
			break;
		case denied:
			 name = "Status de connection : Interdit";
			break;
		case webonly:
			 name = "Status de connection : Web seulemnt";
			break;
		default:
			break;
		}
		return name;
	}
	
}
