package controleRempes.ihm;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import controleRempes.control.MP3Player;
import controleRempes.data.Alerte;
import controleRempes.data.ConfigParental;

public class AlerteDialogue extends JDialog  implements ActionListener {

	/** */
	private static final long serialVersionUID = 1L;

	private static final String TITLE_ALERTE = "TITLE_ALERTE";
	private static final String PREVIENT = "PREVIENT";
	private static final String AUDIO_FILE = "AUDIO_FILE";
	public static final String VALID_DIALOG = "VALID_DIALOG";
	public static final String UNDO_DIALOG = "UNDO_DIALOG";
	public static final String SOUND_FILE = "SOUND_FILE";
	public static final String TITLE_BORDER = "TITLE_BORDER";
	public static final String DEFAULT_VALUE = "DEFAULT_VALUE";
	
	public static final String  OPEN_ICON = "/images/folder-open-document-music-icon.png";
	public static final String  MUSIC_ICON = "/images/music-icon.png";

	JCheckBox activateBloquage = new JCheckBox();
	JCheckBox activateWebUnique = new JCheckBox();
	JCheckBox activateAutorisation = new JCheckBox();

	JTextField timeBeforeBlocage = new JTextField(4);
	JTextField timeBeforeWebUnique = new JTextField(4);
	JTextField timeBeforeAutorisation = new JTextField(4);

	JTextField timeRepeatBlocage = null;
	JTextField timeRepeatWebUnique = null;
	JTextField timeRepeatAutorisation = null;

	JTextField soundBlocage = new JTextField(30);
	JTextField soundWebUnique = new JTextField(30);
	JTextField soundAutorisation = new JTextField(30);

	JButton choicesound1 = new JButton(OPEN_BUTTON_ICON);
	JButton choicesound2 = new JButton(OPEN_BUTTON_ICON);
	JButton choicesound3 = new JButton(OPEN_BUTTON_ICON);

	JButton testsound1 = new JButton(TEST_BUTTON_ICON);
	JButton testsound2 = new JButton(TEST_BUTTON_ICON);
	JButton testsound3 = new JButton(TEST_BUTTON_ICON);

	JButton defaultBlocage = new JButton(MainPanel.MESSAGES_BUNDLE.getString(DEFAULT_VALUE));
	JButton defaultWebUnique = new JButton(MainPanel.MESSAGES_BUNDLE.getString(DEFAULT_VALUE));
	JButton defaultAutorisation = new JButton(MainPanel.MESSAGES_BUNDLE.getString(DEFAULT_VALUE));

	JButton valider = null;
	JButton annuler = null;

	static private final Icon OPEN_BUTTON_ICON;
	static private final Icon TEST_BUTTON_ICON ;

	static {
		Icon openButtonIcon = null;
		Icon testButtonIcon = null;

		try {			
			final URL url_open = AlerteDialogue.class.getResource(OPEN_ICON);
			final Image openImage = ImageIO.read(url_open);
			openButtonIcon = new ImageIcon ( openImage);

			final URL url_test = AlerteDialogue.class.getResource(MUSIC_ICON);
			final Image testImage = ImageIO.read(url_test);
			testButtonIcon = new ImageIcon ( testImage);

		} catch (IOException e) {
			e.printStackTrace();
		}
		OPEN_BUTTON_ICON = openButtonIcon;
		TEST_BUTTON_ICON = testButtonIcon;
		}

	public AlerteDialogue(JFrame parent, ConfigParental config) {
		super(parent);		
		setModal(true);

		setTitle(MainPanel.MESSAGES_BUNDLE.getString(TITLE_ALERTE));
		setLocation(parent.getX()+50, parent.getY()+50);

		JPanel panel = new JPanel();		
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		JPanel ctrlAlerteDialogue = createAlerteDialogue(config);
		panel.add(ctrlAlerteDialogue);
		JPanel ctrlDialogue = createAlerteCtrlDialogue();
		//Dimension preferredSize = new Dimension(500,50);
		//ctrlDialogue.setSize(preferredSize );
		panel.add(ctrlDialogue);

		getContentPane().add(panel);

		setSize(650, 550);
		//setResizable(false);
		//pack();
		setVisible(true);
		parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	private JPanel createAlerteDialogue(ConfigParental config) {
		JPanel ctrlAlerteDialogue = new JPanel();	
		ctrlAlerteDialogue.setLayout(new BoxLayout(ctrlAlerteDialogue, BoxLayout.PAGE_AXIS));

		ctrlAlerteDialogue.add(createAlerteBloquage(activateBloquage, timeBeforeBlocage, soundBlocage, choicesound1,testsound1, defaultBlocage, config.getAlerteBloquage()));
		ctrlAlerteDialogue.add(createAlerteBloquage(activateWebUnique, timeBeforeWebUnique, soundWebUnique, choicesound2,testsound2,defaultWebUnique, config.getAlerteWebUnique()));
		ctrlAlerteDialogue.add(createAlerteBloquage(activateAutorisation, timeBeforeAutorisation, soundAutorisation, choicesound3,testsound3, defaultAutorisation, config.getAlerteAutorisation()));

		// get data to ihm
		getData2IhmBloquage(config);
		getData2IhmWebUnique(config);
		getData2IhmAutorisation(config);



		return ctrlAlerteDialogue;
	}

	private void getData2IhmBloquage(ConfigParental config) {
		activateBloquage.setSelected(config.getAlerteBloquage().getAlerte());
		timeBeforeBlocage.setText(String.valueOf(config.getAlerteBloquage().getAlerteBefore()));
		soundBlocage.setText(config.getAlerteBloquage().getAlerteBloquageSound());
	}

	private void getData2IhmWebUnique(ConfigParental config) {
		activateWebUnique.setSelected(config.getAlerteWebUnique().getAlerte());
		timeBeforeWebUnique.setText(String.valueOf(config.getAlerteWebUnique().getAlerteBefore()));
		soundWebUnique.setText(config.getAlerteWebUnique().getAlerteBloquageSound());		
	}

	private void getData2IhmAutorisation(ConfigParental config) {
		activateAutorisation.setSelected(config.getAlerteAutorisation().getAlerte());
		timeBeforeAutorisation.setText(String.valueOf(config.getAlerteAutorisation().getAlerteBefore()));
		soundAutorisation.setText(config.getAlerteAutorisation().getAlerteBloquageSound());
	}

	private JPanel createAlerteBloquage(JCheckBox activate, JTextField timeBefore, JTextField sound, JButton choicesound, JButton testsound, JButton defaultAlerte, Alerte alerte) {
		JPanel panelAlerte = new JPanel();	
		panelAlerte.setLayout(new GridBagLayout());	

		Border border = BorderFactory.createLineBorder(Color.black);
		TitledBorder title = BorderFactory.createTitledBorder(border,MainPanel.MESSAGES_BUNDLE.getString(TITLE_BORDER) + alerte.getAlerteName());
		panelAlerte.setBorder(title);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.WEST ;
		gbc.anchor = GridBagConstraints.BASELINE_LEADING;
		//gbc.anchor = (x == 0) ? GridBagConstraints.WEST
		gbc.insets = new Insets(4, 4, 4, 4);

		gbc.gridx = 0;	gbc.gridy = 0;
		activate.setText(alerte.getAlerteName());
		panelAlerte.add(activate,gbc);

		gbc.gridx = 0;	gbc.gridy++;
		panelAlerte.add(new JLabel(MainPanel.MESSAGES_BUNDLE.getString(PREVIENT)),gbc);
		gbc.gridx++;
		//timeBefore.setSize(50, 80);
		panelAlerte.add(timeBefore,gbc);

		gbc.gridx=0;gbc.gridy++;
		panelAlerte.add(new JLabel(MainPanel.MESSAGES_BUNDLE.getString(AUDIO_FILE)),gbc);
		gbc.gridx++;
		panelAlerte.add(sound,gbc);
		gbc.gridx++;
		choicesound.addActionListener(this);
		panelAlerte.add(choicesound,gbc);
		gbc.gridx++;
		testsound.addActionListener(this);
		panelAlerte.add(testsound,gbc);

		gbc.gridx=0;gbc.gridy++;
		defaultAlerte.setFont(new Font(Font.DIALOG, Font.PLAIN, 10));
		defaultAlerte.addActionListener(this);
		panelAlerte.add(defaultAlerte, gbc);

		return panelAlerte;

	}
	private JPanel createAlerteCtrlDialogue() {
		JPanel ctrlDialogue = new JPanel();	
		ctrlDialogue.setLayout(new GridLayout(1,2));
		valider = new JButton(MainPanel.MESSAGES_BUNDLE.getString(VALID_DIALOG));
		valider.addActionListener(this);
		ctrlDialogue.add(valider);
		annuler = new JButton(MainPanel.MESSAGES_BUNDLE.getString(UNDO_DIALOG));	
		annuler.addActionListener(this);
		ctrlDialogue.add(annuler);
		return ctrlDialogue;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ConfigParental config = ConfigParental.getInstance();
		if (e.getSource() == valider) {
			validAlerte();
			config.saveAlerte();
			this.dispose();
		} else if (e.getSource() == annuler) {
			this.dispose();
		} else if (e.getSource() == testsound1) {
			testSound(soundBlocage.getText());
		}
		else if (e.getSource() == testsound2) {
			testSound(soundWebUnique.getText());
		}
		else if (e.getSource() == testsound3) {
			testSound(soundAutorisation.getText());
		} else if (e.getSource() == defaultBlocage) {
			config.getAlerteBloquage().setDefault("Evil-laugh.mp3");
			getData2IhmBloquage(config);
		} else if (e.getSource() == defaultWebUnique) {
			config.getAlerteWebUnique().setDefault("Fire-truck-with-siren-passing-by.mp3");
			getData2IhmWebUnique(config);
		} else if (e.getSource() == defaultAutorisation) {
			config.getAlerteAutorisation().setDefault("Old-fashioned-alarm-clock.mp3");
			getData2IhmAutorisation(config);
		} else {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(MainPanel.MESSAGES_BUNDLE.getString(SOUND_FILE), "mp3", "wav","wma"));
			fileChooser.setAcceptAllFileFilterUsed(false);
			int returnVal = fileChooser.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (e.getSource() == choicesound1) {
					soundBlocage.setText(file.getAbsolutePath());
				}
				else if (e.getSource() == choicesound2) {
					soundWebUnique.setText(file.getAbsolutePath());
				}
				else if (e.getSource() == choicesound3) {
					soundAutorisation.setText(file.getAbsolutePath());
				}
			}
		}
	}

	private void testSound(String strPath) {
		Path path = Paths.get(strPath);
		testSound(path);
	}

	private void testSound(Path soundPath) {
		//System.out.println("sound path : " + soundPath.toString() );

		if (soundPath.toString().endsWith(".wma")) {
			playWma (soundPath);
		} if (soundPath.toString().endsWith(".mp3")) {
			MP3Player.play(soundPath);
			//playMp3 (soundPath);
		} 
	}

	private void playWma(Path soundPath) {
		try {

			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundPath.toFile());
			AudioFormat audioFormat = audioInputStream.getFormat();
			
			// TODO
			System.out.println(audioFormat);

			/*DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
			SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			new PlayThread().start();

			/*InputStream in = new FileInputStream(soundPath.toString());
			AudioStream as =    new AudioStream(in); //line 26
			AudioPlayer.player.start(as);*/
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void validAlerte() {

		ConfigParental config =  ConfigParental.getInstance();

		config.getAlerteBloquage().setAlerte(activateBloquage.isSelected());
		config.getAlerteWebUnique().setAlerte(activateWebUnique.isSelected());
		config.getAlerteAutorisation().setAlerte(activateAutorisation.isSelected());

		config.getAlerteBloquage().setAlerteBloquageBefore(Integer.parseInt(timeBeforeBlocage.getText()));
		config.getAlerteWebUnique().setAlerteBloquageBefore(Integer.parseInt(timeBeforeWebUnique.getText()));
		config.getAlerteAutorisation().setAlerteBloquageBefore(Integer.parseInt(timeBeforeAutorisation.getText()));

		config.getAlerteBloquage().setAlerteBloquageSound(soundBlocage.getText());
		config.getAlerteWebUnique().setAlerteBloquageSound(soundWebUnique.getText());
		config.getAlerteAutorisation().setAlerteBloquageSound(soundAutorisation.getText());


	}
}
