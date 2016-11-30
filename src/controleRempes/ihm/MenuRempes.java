package controleRempes.ihm;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class MenuRempes extends JMenuBar 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JMenu menuConnexion=null ;
	private JMenuItem itemAuthorisation=null ;
	private JMenuItem itemConnexion=null ;
	private JMenuItem itemDeconnexion=null ;

	
	public static String REFRESH = "Rafraichir";
	public static String ALERTE = "Alerte";
	//public static String TEST = "Test";
	
	public static String AUTORIZATION = "Authorisation Freebox";
	public static String CONNEXION = "Connexion Freebox";
	public static String DECONNEXION = "Deconnexion Freebox";

	/*menuBar.setSize(800, 100);
	menuBar.add(getmenuFile());*/

	public MenuRempes(Action logFreebox, Action action) 
	{
		super();
		
		JMenu menuAction = new JMenu("Action");
		JMenuItem itemRefresh=new JMenuItem(REFRESH);
		itemRefresh.addActionListener(action);
		menuAction.add(itemRefresh);
		
		JMenuItem itemAlerte=new JMenuItem(ALERTE);
		itemAlerte.addActionListener(action);
		menuAction.add(itemAlerte);
		
		add(menuAction);
		
		
		//--------------------------
		
		menuConnexion = new JMenu("Connexion");

		itemAuthorisation=new JMenuItem(AUTORIZATION);
		itemAuthorisation.addActionListener(logFreebox);
		menuConnexion.add(itemAuthorisation);

		itemConnexion =new JMenuItem(CONNEXION);
		itemConnexion.addActionListener(logFreebox);
		menuConnexion.add(itemConnexion);

		itemDeconnexion =new JMenuItem(DECONNEXION);
		itemDeconnexion.addActionListener(logFreebox);
		menuConnexion.add(itemDeconnexion);

		add(Box.createHorizontalGlue());// <-- horizontal glue
		add(menuConnexion);



	}
}
