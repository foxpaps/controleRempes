package controlePapa;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.Calendar;

import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.json.JSONObject;

import controlePapa.control.ActionPapa;
import controlePapa.control.LogFreebox;
import controlePapa.control.ThreadCheckAlertes;
import controlePapa.control.ThreadRefreshAccess;
import controlePapa.data.ParamAccess;
import controlePapa.data.Planning;
import controlePapa.data.ParamAccess.StatusAutorisation;
import controlePapa.ihm.MainPanel;
import controlePapa.ihm.MenuPapa;
import controlePapa.ihm.StatusPanel;

public class MainControlePapa extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static MainControlePapa mainFrame = null;

	private MainPanel mainPanCommand =null ;	// panel principal
	private MenuPapa menuBar = null ;
	private StatusPanel statusPanel = null; 
	
	private ParamAccess paramAccess = null;

	public static MainControlePapa getInstance() {
		if (mainFrame==null) {
			mainFrame = new MainControlePapa();
		}	
		return mainFrame;
	}

	private MainControlePapa()
	{
		super();

		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		paramAccess = ActionPapa.getInstance().initData(); // init des données par defaut
		menuBar = new MenuPapa( LogFreebox.getInstance(),ActionPapa.getInstance());
		mainPanCommand = new MainPanel(paramAccess);
		mainPanCommand.setPreferredSize(new Dimension(800,300));

		this.setTitle("Dad is watching you");
		this.setBounds ( 120 , 120 , 900 , 400 );
		this.setJMenuBar(menuBar);
		
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(new JScrollPane (mainPanCommand),BorderLayout.CENTER);
		
		statusPanel = new StatusPanel(this);
		contentPane.add(statusPanel, BorderLayout.SOUTH);
		
		this.setContentPane(contentPane);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
	}

	public ParamAccess getParamAccess() {
		return paramAccess;
	}

	public void updatePlanning(ParamAccess param) {
		mainPanCommand.updateData(param);
	}
	public void updateTmpMode(ParamAccess param) {
		mainPanCommand.updateTmpMode(param);
	}
	
	public void updateStatus() {
		
		Calendar calendar = Calendar.getInstance();	
		final Planning planning = MainControlePapa.getInstance().getParamAccess().getPlanning();
		StatusAutorisation currentStatus = planning.getStatus(calendar);
		String statusAutorisation = ParamAccess.GetStatusNameAutorisation(currentStatus);
		statusPanel.setStatusAutorisation(statusAutorisation);
	}
	
	public MainPanel getMainPanel()
	{
		return mainPanCommand;
	}

	public MenuPapa getMenu()
	{
		return menuBar;
	}
	
	public StatusPanel getStatusPanel() {
		return statusPanel;
	}
	
	public static void showError(String message) {
		JOptionPane.showMessageDialog(mainFrame, message, "Dialog",
				JOptionPane.ERROR_MESSAGE);
	}
	public static void showInfo(String message) {
		JOptionPane.showMessageDialog(mainFrame, message, "Dialog",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				mainFrame =new MainControlePapa();
				mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) ) ;
				Image icone = Toolkit.getDefaultToolkit().getImage("parents.png");
				mainFrame.setIconImage(icone);

				mainFrame.setVisible(true);

				LogFreebox.getInstance().connexion();
				ThreadRefreshAccess.refresh();
				ThreadCheckAlertes.refresh();
				mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)) ;
			}
		});

		
		// create the status bar panel and shove it down the bottom of the frame;
		
		//frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) ) ;

		// login automatique

		//frame.setVisible(true);


		//frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)) ;

	}
}
