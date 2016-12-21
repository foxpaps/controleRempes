package controleRempes;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import controleRempes.control.ActionRempes;
import controleRempes.control.GuiControlRempes;
import controleRempes.control.ThreadCheckAlertes;
import controleRempes.control.ThreadRefreshAccess;
import controleRempes.control.freebox.LogFreebox;
import controleRempes.data.ParamAccess;
import controleRempes.data.ParamAccess.StatusAutorisation;
import controleRempes.data.Planning;
import controleRempes.ihm.AlerteDialogue;
import controleRempes.ihm.MainPanel;
import controleRempes.ihm.MenuRempes;
import controleRempes.ihm.StatusPanel;

public class MainControleRempes extends JFrame implements GuiControlRempes
{

	private static final long serialVersionUID = 5161193878470839549L;

	/** only french */
	public static final Locale locale_FR = new Locale("fr","FR");

	public static final String  PARENTS_ICON = "/images/parents.png";
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

		paramAccess = ActionRempes.getInstance(this).initData(); // init des données par defaut
		menuBar = new MenuRempes( LogFreebox.getInstance(this),ActionRempes.getInstance(this));
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
	
	public void showError(final String message) {
		JOptionPane.showMessageDialog(mainFrame, message, "Erreur",
				JOptionPane.ERROR_MESSAGE);
	}
	public void showInfo(final String message) {
		JOptionPane.showMessageDialog(mainFrame, message, "Dialog",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public void refreshGui() {
		this.repaint();
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				mainFrame =new MainControleRempes();
				mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) ) ;
				final URL url_open = AlerteDialogue.class.getResource(PARENTS_ICON);
				try {
					Image openImage = ImageIO.read(url_open);
					mainFrame.setIconImage(openImage);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mainFrame.setVisible(true);

				LogFreebox.getInstance().connexion();
				ThreadRefreshAccess.refresh(mainFrame);
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
