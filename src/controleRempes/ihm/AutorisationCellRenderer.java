package controleRempes.ihm;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Calendar;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import controleRempes.MainControleRempes;
import controleRempes.data.ParamAccess;
import controleRempes.data.ParamAccess.StatusAutorisation;

public class AutorisationCellRenderer extends DefaultTableCellRenderer {


	/** */
	private static final long serialVersionUID = 1621885528494598241L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,	row, column);

		if (value==null) {
			return this;
		}
		if (column==0) {
			setText(value.toString());
			setBackground(Color.gray);
		} else {
			StatusAutorisation status = (StatusAutorisation) value;
			setText("");

			final ParamAccess paramAcces = MainControleRempes.getInstance().getParamAccess();
			Calendar calendar = Calendar.getInstance();
			
			int numDay = calendar.get(Calendar.DAY_OF_WEEK);
			numDay = (numDay+5)%7 ;
			if ((numDay==row && !paramAcces.isSpecial(calendar))  ||
				(row==7 && paramAcces.isSpecial(calendar))) {
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				int posInDay = hour*2 + (minute<29 ? 0 : 1);
				if (posInDay+1==column) {
					setForeground(Color.white);
					setFont(new Font(Font.DIALOG, Font.BOLD, 12));
					setText("X");
				}
			}
			
			switch (status) {
			case allowed:
				setBackground(Color.green);
				break;
			case webonly:
				setBackground(Color.blue);
				break;
			case denied:
				setBackground(Color.red);
				break;
			case undefine :
				setBackground(Color.gray);
				break;
			}
		}
		return this;
	}
}
