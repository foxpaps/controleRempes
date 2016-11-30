package controleRempes;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Calendar;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import controleRempes.control.ActionRempes;
import controleRempes.control.LogFreebox;
import controleRempes.control.ThreadCheckAlertes;
import controleRempes.control.ThreadRefreshAccess;
import controleRempes.data.ParamAccess;
import controleRempes.data.ParamAccess.StatusAutorisation;
import controleRempes.data.Planning;
import controleRempes.ihm.MainPanel;
import controleRempes.ihm.MenuRempes;
import controleRempes.ihm.StatusPanel;

public class MainControleRempes extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static MainControleRempes mainFrame = null;

	private MainPanel mainPanCommand =null ;	// panel principal
	private MenuRempes menuBar = null ;
	private StatusPanel statusPanel = null; 
	
	private ParamAccess paramAccess = null;

	public static MainControleRempes getInstance() {
		if (mainFrame==null) {
			mainFrame = new MainControleRempes();
		}	
		return mainFrame;
	}

	private MainControleRempes()
	{
		super();

		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		paramAccess = ActionRempes.getInstance().initData(); // init des données par defaut
		menuBar = new MenuRempes( LogFreebox.getInstance(),ActionRempes.getInstance());
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
		final Planning planning = MainControleRempes.getInstance().getParamAccess().getPlanning();
		StatusAutorisation currentStatus = planning.getStatus(calendar);
		String statusAutorisation = ParamAccess.GetStatusNameAutorisation(currentStatus);
		statusPanel.setStatusAutorisation(statusAutorisation);
	}
	
	public MainPanel getMainPanel()
	{
		return mainPanCommand;
	}

	public MenuRempes getMenu()
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
				mainFrame =new MainControleRempes();
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
