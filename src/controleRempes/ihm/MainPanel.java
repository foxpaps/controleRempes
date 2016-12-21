package controleRempes.ihm;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import controleRempes.data.ParamAccess;

public class MainPanel extends JPanel {

	/** */
	private static final long serialVersionUID = 1L;

	public static final ResourceBundle MESSAGES_BUNDLE = ResourceBundle.getBundle("properties.ihm.messages", Locale.getDefault());

	
	private JLabel peripherique = null;
	private JLabel authorizationTmp = new JLabel(MESSAGES_BUNDLE.getString("ALLOW_TMP"));

	private JCheckBox cb1 = new JCheckBox(MESSAGES_BUNDLE.getString("ALWAYS_PLANNING"));
	private JCheckBox cb2 = new JCheckBox(MESSAGES_BUNDLE.getString("ALWAYS_LOCK"));
	private JCheckBox cb3 = new JCheckBox(MESSAGES_BUNDLE.getString("ALWAYS_WEB_ACCES"));
	private JCheckBox cb4 = new JCheckBox(MESSAGES_BUNDLE.getString("ALWAYS_ALLOW_ACCES"));


	public MainPanel(ParamAccess param) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JPanel perif = createPerifPanel();
		perif.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(perif);

		JPanel filtrage =  createTypeFiltragePanel();
		filtrage.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(filtrage);

		authorizationTmp.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(authorizationTmp);

		JPanel planing = createPlaningPanel(param);
		planing.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(planing);	
	}

	private JPanel createPerifPanel () {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
		panel.add(new JLabel(MESSAGES_BUNDLE.getString("DEVICE")));
		peripherique = new JLabel(MESSAGES_BUNDLE.getString("DEFAULT_DEVICE"));
		panel.add(peripherique);

		return panel;
	}

	private JPanel createTypeFiltragePanel() {

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,2));
		panel.add(cb1);
		panel.add(cb2);
		panel.add(cb3);
		panel.add(cb4);

		cb1.setEnabled(false);
		cb2.setEnabled(false);
		cb3.setEnabled(false);
		cb4.setEnabled(false);

		Border border = BorderFactory.createLineBorder(Color.black);
		TitledBorder title = BorderFactory.createTitledBorder(border, MESSAGES_BUNDLE.getString("FILTER_TYPE"));
		panel.setBorder(title);
		return panel;	
	}

	private JPanel createPlaningPanel(ParamAccess param) {

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		TableModel modele = new TableSemaine(param);

		JTable table = new JTable(modele);
		table.setDefaultRenderer(ParamAccess.StatusAutorisation.class, new AutorisationCellRenderer());

		table.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		table.getColumnModel().getColumn(0).sizeWidthToFit();
		for (int i= 1;i<49;i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(10);
		}

		JScrollPane jscrollPane = new javax.swing.JScrollPane();
		jscrollPane.setViewportView(table);

		jscrollPane .setPreferredSize(new Dimension(798,160));
		panel.add(jscrollPane);

		return panel;
	}

	public void initializeData(ParamAccess param) {
	}

	public void updateData(ParamAccess param) {

		peripherique.setText(param.getPeriferique());

		switch (param.getSchedulingMode()) {
		case planning :
			updatePlaning(param);
			break;
		case Indefini:
			break;
		case forced:
			updatePlaning(param);
			break;
		case temporary:
			break;
		default:
			break;
		}
	}

	private void updatePlaning(ParamAccess param) {
		cb1.setEnabled(true);
		cb2.setEnabled(true);
		cb3.setEnabled(true);
		cb4.setEnabled(true);

		cb1.setSelected(false);
		cb2.setSelected(false);
		cb3.setSelected(false);
		cb4.setSelected(false);

		switch (param.getPlanning().getFiltrage()) {
		case  Planification:
			cb1.setSelected(true);
			break;
		case Bloque :
			cb2.setSelected(true);
			break;
		case AccesWeb:
			cb3.setSelected(true);
			break;
		case Autorise:
			cb4.setSelected(true);
			break;
		default:
			break;
		}

		cb1.setEnabled(false);
		cb2.setEnabled(false);
		cb3.setEnabled(false);
		cb4.setEnabled(false);		
	}

	public void updateTmpMode(ParamAccess param) {
		String texte;
		int expire = param.getTmpModeExpire();

		switch (param.getTmpMode())  {
		case allowed:
			texte =  MESSAGES_BUNDLE.getString("ALLOW_TMP");
			texte += expire;
			authorizationTmp.setForeground(Color.green);
			break;
		case denied:
			texte =  MESSAGES_BUNDLE.getString("PROHIBIT_TMP");
			texte += expire;
			authorizationTmp.setForeground(Color.red);
			break;
		case webonly:
			texte = "Web temporaire : ";
			texte += expire;
			authorizationTmp.setForeground(Color.blue);
			break;
		default:
			texte =  MESSAGES_BUNDLE.getString("NO_ALLOW_TMP");
			authorizationTmp.setForeground(Color.black);
			break;

		}		
		if (expire==0) {
			texte =  MESSAGES_BUNDLE.getString("NO_ALLOW_TMP");
			authorizationTmp.setForeground(Color.black);	
		}  
		authorizationTmp.setText(texte);

	}

}
