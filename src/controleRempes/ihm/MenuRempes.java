package controleRempes.ihm;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuRempes extends JMenuBar 
{
	/** */
	private static final long serialVersionUID = 1L;
	private JMenu menuConnexion=null ;
	private JMenuItem itemAuthorisation=null ;
	private JMenuItem itemConnexion=null ;
	private JMenuItem itemDeconnexion=null ;
	private JMenuItem itemHelpConnexion=null ;
	
	public static String REFRESH = MainPanel.MESSAGES_BUNDLE.getString("REFRESH");
	public static String ALERTE = MainPanel.MESSAGES_BUNDLE.getString("ALERTE");
	
	public static String AUTORIZATION = MainPanel.MESSAGES_BUNDLE.getString("AUTORIZATION");
	public static String CONNECTION_FREE = MainPanel.MESSAGES_BUNDLE.getString("CONNECTION_FREE");
	public static String DECONNECTION = MainPanel.MESSAGES_BUNDLE.getString("DECONNECTION_FREE");
	public static String CONNECION_HELP = MainPanel.MESSAGES_BUNDLE.getString("CONNECION_HELP");

	public MenuRempes(Action action) 
	{
		super();
		
		JMenu menuAction = new JMenu(MainPanel.MESSAGES_BUNDLE.getString("MENU_ACTION"));
		JMenuItem itemRefresh = new JMenuItem(REFRESH);
		itemRefresh.addActionListener(action);
		menuAction.add(itemRefresh);
		
		JMenuItem itemAlerte = new JMenuItem(ALERTE);
		itemAlerte.addActionListener(action);
		menuAction.add(itemAlerte);
		
		add(menuAction);		
		//--------------------------
		
		menuConnexion = new JMenu(MainPanel.MESSAGES_BUNDLE.getString("CONNECTION"));

		itemAuthorisation = new JMenuItem(AUTORIZATION);
		itemAuthorisation.addActionListener(action);
		menuConnexion.add(itemAuthorisation);

		itemConnexion = new JMenuItem(CONNECTION_FREE);
		itemConnexion.addActionListener(action);
		menuConnexion.add(itemConnexion);

		itemDeconnexion = new JMenuItem(DECONNECTION);
		itemDeconnexion.addActionListener(action);
		menuConnexion.add(itemDeconnexion);

		itemHelpConnexion = new JMenuItem(CONNECION_HELP);
		itemHelpConnexion.addActionListener(action);
		menuConnexion.add(itemHelpConnexion);

		add(Box.createHorizontalGlue());// <-- horizontal glue
		add(menuConnexion);



	}
}
