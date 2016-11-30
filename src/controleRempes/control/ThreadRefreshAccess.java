package controleRempes.control;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONObject;

import controleRempes.MainControleRempes;
import controleRempes.data.ConfigParental;
import controleRempes.data.Day;
import controleRempes.data.ParamAccess;
import controleRempes.data.Planning;
import controleRempes.data.ParamAccess.SchedulingMode;
import controleRempes.data.ParamAccess.StatusAutorisation;
import controleRempes.data.Planning.TypeFiltrage;

public class ThreadRefreshAccess implements Runnable  {


	public static void refresh() {
		(new Thread(new ThreadRefreshAccess())).start();
	}

	@Override
	public void run() {

		try {

			int periode = ConfigParental.getInstance().getFrequenceRefresh()*60*1000;
			while (true) {
				long beginTime  = System.currentTimeMillis();
				System.out.println("RefreshAccess begin");
				refreshParamAcces();

				Thread.sleep(periode -(System.currentTimeMillis() -beginTime));
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static public void refreshParamAcces() {
		String hostName = "monPc"; // default name
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e1) {
			MainControleRempes.showError(e1.getMessage()); 
			e1.printStackTrace();
		}	
		hostName = "Matth-PC"; //TODO remove it

		ParamAccess param = MainControleRempes.getInstance().getParamAccess();		
		param.setPeriferique(hostName);

		JSONObject filter = GetFilters.getCurrentFilter(hostName);
		if (filter!=null) {
			int id = filter.getInt("id");

			SchedulingMode schedulingMode = filter.getEnum(SchedulingMode.class, "scheduling_mode");
			param.setSchedulingMode(schedulingMode);
			switch (schedulingMode) {
			case planning :
				refreshPlaning(param,id);
				param.getPlanning().setFiltrage(TypeFiltrage.Planification);
				MainControleRempes.getInstance().updatePlanning(param);			
				break;
			case temporary :
				refreshPlaning(param,id);
				MainControleRempes.getInstance().updatePlanning(param);			
				System.out.println("temporary");
				break;
			case forced :
				refreshPlaning(param,id);
				refreshForcedMode(param,filter);
				MainControleRempes.getInstance().updatePlanning(param);	
				System.out.println("forced");
				break;
			case Indefini:
				break;
			default:
				break;
			}

			//todo invokelater
			refreshTemporary(param,filter);
			MainControleRempes.getInstance().updateTmpMode(param);
		}
		MainControleRempes.getInstance().updateStatus();

		MainControleRempes.getInstance().repaint();
	}


	static private void refreshForcedMode(ParamAccess param, JSONObject filter) {
		StatusAutorisation forcedMode = filter.getEnum(StatusAutorisation.class,"forced_mode");
		switch  (forcedMode) {
		case allowed:
			param.getPlanning().setFiltrage(TypeFiltrage.Autorise);
			break;
		case denied:
			param.getPlanning().setFiltrage(TypeFiltrage.Bloque);
			break;
		case webonly:
			param.getPlanning().setFiltrage(TypeFiltrage.AccesWeb);
			break;			
		default:
			param.getPlanning().setFiltrage(TypeFiltrage.Indefini);
			break;

		}
	}

	static private void refreshTemporary(ParamAccess param, JSONObject filter) {

		StatusAutorisation tmpMode = filter.getEnum(StatusAutorisation.class,"tmp_mode");
		int tmpModeExpire = filter.getInt("tmp_mode_expire");
		param.setTmpMode(tmpMode);
		param.setTmpModeExpire(tmpModeExpire);

	}

	static private void refreshPlaning(ParamAccess param, int id) {
		JSONObject planning = null; 
		JSONObject resultPlanning = null;

		planning = GetFilters.getaPlaning(id);
		if (planning.getBoolean("success")) {
			resultPlanning = planning.getJSONObject("result");				
			Planning semaine = param.getPlanning();

			JSONArray mapping = resultPlanning.getJSONArray("mapping");			
			int len = mapping.length();
			int dayCount=0;
			for (int i=0; i<len ; i++) {
				Day day = semaine.getDay(dayCount);
				ParamAccess.StatusAutorisation enumSatus = mapping.getEnum(ParamAccess.StatusAutorisation.class, i);
				day.setAutorisation(i - dayCount*48, enumSatus);
				if ( (i+1) % 48==0) {
					dayCount++;
				}
			}
		}
	}

}
