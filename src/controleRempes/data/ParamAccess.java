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
		String name = ConfigParental.MESSAGES_BUNDLE.getString("UNDEFINED_STATUS_CONNECTION");
		switch (status)  {
		case allowed:
			 name = ConfigParental.MESSAGES_BUNDLE.getString("AUTHORIZED_STATUS");
			break;
		case denied:
			 name = ConfigParental.MESSAGES_BUNDLE.getString("FORBIDEN_STATUS_CONNECTION");
			break;
		case webonly:
			 name = ConfigParental.MESSAGES_BUNDLE.getString("WEB_ONLY_STATUS_CONNECTION");
			break;
		default:
			break;
		}
		return name;
	}
}
