package controleRempes.control;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

import controleRempes.MainControleRempes;
import controleRempes.data.ConfigParental;
import controleRempes.data.Planning;
import controleRempes.data.ParamAccess.StatusAutorisation;

public class ThreadCheckAlertes implements Runnable  {

	private static int periode = 60*1000; // 1 minute

	public static void refresh() {

		(new Thread(new ThreadCheckAlertes())).start();
	}

	@Override
	public void run() {

		try {
			StatusAutorisation previousCurrentStatus = null;
			StatusAutorisation previousNextStatus = null;

			while (true) {
				long beginTime  = System.currentTimeMillis();
				System.out.println("Check Alertes begin");

				final Planning planning = MainControleRempes.getInstance().getParamAccess().getPlanning();
				Calendar calendar = Calendar.getInstance();	
				StatusAutorisation currentStatus = planning.getStatus(calendar);
				Calendar nextCalendar = (Calendar) calendar.clone();
				nextCalendar.add(Calendar.MINUTE, 30);
				StatusAutorisation nextStatus = planning.getStatus(nextCalendar);

				int minute = nextCalendar.get(Calendar.MINUTE);
				minute = (minute/30)*30;
				nextCalendar.set(Calendar.MINUTE, minute);				

				// Si un des status précédents a changés, on peut redéclencher un alarme
				if (previousCurrentStatus!=null && previousCurrentStatus != currentStatus 
						|| previousNextStatus!= null && previousNextStatus != nextStatus) {
					if (currentStatus != nextStatus) {
						long diff = (nextCalendar.getTimeInMillis() - calendar.getTimeInMillis())/1000;
						checkAlarme(nextStatus,diff);
					}
				}
				previousCurrentStatus = currentStatus;
				previousNextStatus = nextStatus;

				Thread.sleep(periode -(System.currentTimeMillis() -beginTime));
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean checkAlarme(StatusAutorisation nextStatus, long changeIn) {
		int timeBefore = 0;
		String sound = null;
		final ConfigParental config = ConfigParental.getInstance();
		boolean allowed = false;

		switch (nextStatus) {
		case allowed:
			timeBefore = config.getAlerteAutorisation().getAlerteBefore();
			sound = config.getAlerteAutorisation().getAlerteBloquageSound();
			allowed = config.getAlerteAutorisation().getAlerte();
			break;
		case denied:
			timeBefore = config.getAlerteBloquage().getAlerteBefore();
			sound = config.getAlerteBloquage().getAlerteBloquageSound();
			allowed = config.getAlerteBloquage().getAlerte();
			break;
		case webonly:
			timeBefore = config.getAlerteWebUnique().getAlerteBefore();
			sound = config.getAlerteWebUnique().getAlerteBloquageSound();
			allowed = config.getAlerteWebUnique().getAlerte();
			break;
		default:
			break;		
		}

		if (allowed && changeIn < timeBefore*60 && sound !=null) {
			Path path = Paths.get(sound);
			MP3Player.play(path);
			return true;
		}
		return false;
	}
}
