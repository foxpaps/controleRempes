package controleRempes.ihm;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import controleRempes.data.ParamAccess;

public class TableSemaine extends AbstractTableModel  {


	/**
	 * 
	 */
	private static final long serialVersionUID = -7120881414463444403L;

	private ParamAccess _parametre;
	
	private final String[] entetes = { "Jours", "0", "", "1", "", "2", "", "3", "", "4", "", "5", "", "6", "", "7", "", "8", "", "9", "",
			"10", "", "11", "", "12", "", "13", "", "14", "", "15", "", "16", "", "17", "", "18", "",
			"19", "",  "20", "", "21", "", "22", "", "23", "" };

	public TableSemaine(ParamAccess param) {
		super();
		_parametre = param;
	}

	@Override
	public int getColumnCount() {
		return entetes.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return entetes[columnIndex];
	}

	@Override
	public int getRowCount() {
		return 8;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		if (columnIndex==0) {
			return _parametre.getPlanning().getDay(rowIndex).getNom();
		}
		return _parametre.getPlanning().getDay(rowIndex).getAutorisation(columnIndex-1);

	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex==0) {
			return String.class;
		}
		return ParamAccess.StatusAutorisation.class;

	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
	}

}
