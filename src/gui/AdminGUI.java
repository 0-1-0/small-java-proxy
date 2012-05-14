package gui;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import myHTTPProxy.AllConstants;
import myHTTPProxy.Main;
import util.ClientInfoBank;


/**
 * Admin's control panel
 */
public class AdminGUI extends JFrame implements ActionListener, WindowListener, AllConstants {
	
	private static final long serialVersionUID = 1L;	

	/**
	 * Создание окна по его заколовку.
	 * @param title - заголовок окна.
	 */
	public AdminGUI(String title) {
		super(title);
		initFrame();
		initComponetes();
		setVisible(true);//show window
	}
	
	/**
	 * Настройка параметров окна.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	protected void initFrame(){
		
		/*
		 * loon'n'feel section
		 */	
		
		//setting windows(vista/7) look'n'feel
		try {
			UIManager.setLookAndFeel(DEFAULT_LOOK_N_FEEL);
		} catch (Exception e) {
			System.err.println("look'n'feel loading problem");
		}		
		
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT); //Задаем размеры окна.
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize(); //Получаем размеры текущего дисплея.
		setLocation((screen.width - getSize().width)/2, (screen.height - getSize().width)/2); //Размещаем окно по середине дисплея.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); //Завершение работы приложения вместе с закрытием окна. Задание параметров внешнего листнера.
		setResizable(true); //allow resize window
		this.addWindowListener(this);
	}
	
	HashSet<String> selected;
	
	/*
	 * some global components
	 */
	JPanel hostsPanel;
	JPanel hostsPanelBorder;
	JButton stopServMenu;
	JButton startServMenu;
	JMenu hostsAction;
	JComboBox showGroup;
	JScrollPane sp;
	GridBagConstraints hpc = new GridBagConstraints();//hpc means hostsPanelConstraints
	
	/**
	 * initializing frame components
	 */	
	protected void initComponetes() {
		selected = new HashSet<String>();
		
		Container c = getContentPane();		
		GridBagConstraints c1 = new GridBagConstraints();
		
		/*TOP menu
		 * 
		 */
		JMenuBar mainMenu = new JMenuBar();	
		mainMenu.setSize(DEFAULT_WIDTH, 10);
		
			JMenu optionsMenu = new JMenu();
			optionsMenu.setText("");
			optionsMenu.setIcon(new ImageIcon(MENU_SETTINGS_PICTURE_SOURCE));
			mainMenu.add(optionsMenu);
				
				
				JMenuItem optAbout = new JMenuItem();
				optAbout.setText("About");
				optAbout.setActionCommand(SHOW_ABOUT);
				optAbout.addActionListener(this);
				optionsMenu.add(optAbout);	
				
				JMenuItem optOptions = new JMenuItem();
				optOptions.setText("Options");
				optOptions.setActionCommand(SHOW_OPT);
				optOptions.addActionListener(this);
				optionsMenu.add(optOptions);
				
				JMenuItem optImport = new JMenuItem();
				optImport.setText("Import from XML");
				optImport.setActionCommand(IMPORT);
				optImport.addActionListener(this);
				optionsMenu.add(optImport);
				
				JMenuItem optExport = new JMenuItem();
				optExport.setText("Export to XML");
				optExport.setActionCommand(EXPORT);
				optExport.addActionListener(this);
				optionsMenu.add(optExport);
				
				JMenuItem optExit = new JMenuItem();
				optExit.setText("Exit");
				optExit.setActionCommand(EXIT);
				optExit.addActionListener(this);
				optionsMenu.add(optExit);		
			
			/*
			 * Button to start server
			 */
			startServMenu = new JButton();
			startServMenu.setText("start Server");
			
			//this sets button's to look like label
			startServMenu.setBorderPainted(false);
			startServMenu.setContentAreaFilled(false);
			startServMenu.setFocusable(false);//this takes off ugly border
			
			mainMenu.add(startServMenu);
			startServMenu.setActionCommand(START_SERVER);
			startServMenu.addActionListener(this);	
			
			
			/*
			 * Button to shutdown server
			 */
			stopServMenu = new JButton();
			stopServMenu.setText("stop Server");
			stopServMenu.setEnabled(false);
			
			//this helps us to make button appearence like label
			stopServMenu.setBorderPainted(false);
			stopServMenu.setContentAreaFilled(false);
			stopServMenu.setFocusable(false);
			
			mainMenu.add(stopServMenu);
			stopServMenu.setActionCommand(STOP_SERVER);
			stopServMenu.addActionListener(this);
			
			/*
			 * Actions menu
			 */	
			hostsAction = new JMenu("actions");			
				JMenuItem ban = new JMenuItem("ban");
				ban.setActionCommand(BAN);
				ban.addActionListener(this);
				hostsAction.add(ban);
				
				JMenuItem unBan = new JMenuItem("unBan");
				unBan.setActionCommand(UNBAN);
				unBan.addActionListener(this);
				hostsAction.add(unBan);
				
				mainMenu.add(hostsAction);
		
		c.add(mainMenu, BorderLayout.NORTH);
			
		/*
		 * The panel that will contains hostPanel, showGroup comboBox
		 */
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());
		c.add(pane);
		
		/*
		 * showGroup label
		 */
		JLabel showLabel = new JLabel();
		showLabel.setText("show:");
		c1.anchor = GridBagConstraints.LAST_LINE_START;
		c1.gridx = 0;
		c1.gridy = 0;
		c1.weightx = 0.03;
		c1.weighty = 0.01;
		c1.insets = new Insets(10, 10, 0, 0);
		pane.add(showLabel, c1);
		
		/*
		 * showGroup combo
		 */
		showGroup = new JComboBox(CLIENT_GROUPS);
		showGroup.setFocusable(false);
		showGroup.setActionCommand(GROUP_CHANGED);
		showGroup.addActionListener(this);
		//showGroup
		c1.gridy = 1;
		c1.weighty = 1.8;
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.insets = new Insets(0, 10, 0, 10);
		c1.anchor = GridBagConstraints.FIRST_LINE_START;
		pane.add(showGroup, c1);
		
		/*
		 * Hosts panel
		 */
		hostsPanelBorder = new JPanel();
		hostsPanelBorder.setLayout(new GridBagLayout());
		hpc.anchor = GridBagConstraints.NORTH;
		hpc.fill = GridBagConstraints.BOTH;
		hpc.gridx = 0;
		hpc.weightx = 0.1;
		hpc.gridy = 0;
		hpc.weighty = 0.1;
		
		HashSet<String> h1 = new HashSet<String>();
//		h1.add("sdsd");
//		h1.add("192.168.1.1");	
		hostsPanel = new ClientViewFactory().getClientsPanel(h1, this);
		sp = new JScrollPane(hostsPanel);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		hostsPanelBorder.add(sp, hpc);		
		hostsPanelBorder.setSize(3*DEFAULT_WIDTH/4, 3*DEFAULT_HEIGHT/4);
		hostsPanelBorder.setBorder(new TitledBorder("clients' hosts:"));
		
		c1.insets = new Insets(0, 0, 0, 0);
		c1.anchor = GridBagConstraints.FIRST_LINE_START;
		c1.gridx = 1;
		c1.gridy = 0;
		c1.gridheight = 2;
		c1.weightx = 1;
		c1.fill = GridBagConstraints.BOTH;
		pane.add(hostsPanelBorder, c1);	
		
		Timer t = new Timer(TIMER_DELAY, this);
		t.setActionCommand(REFRESH_HOSTS);
		t.start();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String c = e.getActionCommand();
		
		if(IMPORT.contentEquals(c)){
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("select the file with XML data");
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int res = fc.showOpenDialog(hostsAction);
			if(res == JFileChooser.APPROVE_OPTION)
				try{
					ClientInfoBank.getInstance().loadXMLData(fc.getSelectedFile().getAbsolutePath());
				}catch(Exception ee){
					ee.printStackTrace();
				}
			
		}
		
		if(EXPORT.contentEquals(c)){
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("select the file to save XML data");
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int res = fc.showSaveDialog(hostsAction);
			if(res == JFileChooser.APPROVE_OPTION)
				try{
					ClientInfoBank.getInstance().saveXMLData(fc.getSelectedFile().getAbsolutePath());
				}catch(Exception ee){
					ee.printStackTrace();
				}
		}
		
		if(EXIT.contentEquals(c))
			System.exit(0);
		
		if(START_SERVER.contentEquals(c)){
			Main.startServer();				
			startServMenu.setEnabled(false);
			stopServMenu.setEnabled(true);
		}
		
		if(STOP_SERVER.contentEquals(c)){
			Main.stopServer();
			stopServMenu.setEnabled(false);
			startServMenu.setEnabled(true);
		}
		
		if(SHOW_ABOUT.contentEquals(c))
			new AboutBox(APPLICATION_NAME + " v." + VERSION);
		
		if(GROUP_CHANGED.contentEquals(c)){
			hostsAction.setEnabled(showGroup.getSelectedIndex() != GROUP_CONNECTED);	
		}
		
		if(REFRESH_HOSTS.contentEquals(c)){
			HashSet<String> h1 = ClientInfoBank.getInstance().getClientsGroup(showGroup.getSelectedIndex());
			HashSet<String> h2 = new HashSet<String>();
			
			for(Component comp:hostsPanel.getComponents())
				h2.add(comp.getName());
			
			boolean flag = false;
			
			for(String c1:h1)
				if(!h2.contains(c1)){
					flag = true;
					break;
				}
			
			if(!flag)
			for(String c1:h2)
				if(!EMPTY_BOX_MARKER.contentEquals(c1) && !h1.contains(c1)){
					flag = true;
					break;
				}
			
			if(flag){			
				hostsPanel = new ClientViewFactory().getClientsPanel(h1, this);
				sp = new JScrollPane(hostsPanel);
				sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				hostsPanelBorder.removeAll();
				hostsPanelBorder.add(sp, hpc);
				hostsPanelBorder.revalidate();
			}
		}
		
		if(BAN.contentEquals(c)){
			for(Component pane:hostsPanel.getComponents()){
				String s = pane.getName();
				if(!EMPTY_BOX_MARKER.contentEquals(s) && pane instanceof JPanel){
					for(Component c2:((JPanel)pane).getComponents())
						if(c2 instanceof JCheckBox)
							if(((JCheckBox)c2).isSelected() && ClientInfoBank.getInstance().hasClient(s))
							ClientInfoBank.getInstance().getClientByAddress(s).setBanned(true);
				}
			}
		}
		//evil copy'n'paste
		if(UNBAN.contentEquals(c)){
			for(Component pane:hostsPanel.getComponents()){
				String s = pane.getName();
				if(!EMPTY_BOX_MARKER.contentEquals(s) && pane instanceof JPanel){
					for(Component c2:((JPanel)pane).getComponents())
						if(c2 instanceof JCheckBox)
							if(((JCheckBox)c2).isSelected() && ClientInfoBank.getInstance().hasClient(s))
							ClientInfoBank.getInstance().getClientByAddress(s).setBanned(false);
				}
			}
		}
		
		if(SHOW_OPT.contentEquals(c))
			new OptBox("Options");
		
		if(c.startsWith(STAT_PREFIX)){
			new StatisticBox(c.substring(STAT_PREFIX.length()));
		}
		
	}
	
	@Override
	public void windowClosing(WindowEvent arg0) {
		Main.stopServer();
		System.exit(0);			
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}	
	
}
