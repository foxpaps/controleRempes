package controleRempes.ihm;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class HelpConnectionDialog extends JDialog  implements ActionListener {

	/**	 */
	private static final long serialVersionUID = 1L;

	static private final String HELP_FILE = "/properties/ihm/helpConnection.txt";

	public HelpConnectionDialog  (JFrame parent) {
		super (parent);
		setModal(true);
		setTitle(MainPanel.MESSAGES_BUNDLE.getString("TITLE_HELP_CONNECTION"));
		setLocation(parent.getX()+50, parent.getY()+50);

		JPanel panel = new JPanel();	
		BoxLayout gbc = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(gbc);


		JPanel textArea = getTextArea();
		textArea.setAlignmentX(Component.CENTER_ALIGNMENT);		
		panel.add(textArea);

		JButton okButton = new  JButton("OK");
		okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		okButton.addActionListener(this);
		panel.add(okButton);

		getContentPane().add(panel);
		setSize(620, 520);

		this.setResizable(false);
		//pack();
		setVisible(true);
		parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private JPanel getTextArea() {
		JTextArea text = new JTextArea();

		final URL urlHelpFile = HelpConnectionDialog.class.getResource(HELP_FILE);

		//This is where a real application would open the file.
		try (Reader reader = new BufferedReader(new InputStreamReader(urlHelpFile.openStream()))) {
			text.read(reader, "reading file");
		}
		catch (Exception e) {
			System.err.println(e);
		}
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setEditable(false);
		//text.setSize(400, 400);

		JScrollPane listScroller = new JScrollPane(text,
	            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		listScroller.setPreferredSize(new Dimension(550, 450));

		JPanel panel = new JPanel();
		panel.add(listScroller);
		return panel;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		this.dispose();
	}


}
