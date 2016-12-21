package controleRempes.control;

import controleRempes.control.freebox.GuiControl;
import controleRempes.data.ParamAccess;

public interface GuiControlRempes extends GuiControl {

	public ParamAccess getParamAccess();
	public void updatePlanning(final ParamAccess param);
	public void updateTmpMode(final ParamAccess param);	
	public void updateStatus();

	public void refreshGui();
	
}
