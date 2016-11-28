package controlePapa.ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;



public class StatusPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3440142521770326421L;
	private JLabel statusAutorisation = new JLabel("status autorisation");
	private JLabel statusConnexion = new JLabel("connexion en cours");
	
	public StatusPanel(JFrame frame) {
		super();
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setPreferredSize(new Dimension(frame.getWidth(), 16));
		
	    setLayout(new BorderLayout());
	    
	    add(statusAutorisation,BorderLayout.WEST);
	    add(statusConnexion,BorderLayout.EAST);

	}
	
	public void setStatusAutorisation(String statusAutorisation2) {
		statusAutorisation.setText(statusAutorisation2);
	}
	
	public void setStatusConnexion(String statusConnexion2) {
		statusConnexion.setText(statusConnexion2);
	}
}
