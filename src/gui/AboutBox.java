package gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import myHTTPProxy.AllConstants;

public class AboutBox extends JFrame implements ActionListener, AllConstants {

	private static final long serialVersionUID = 1L;
	

	public AboutBox(String title) {
		super(title);
		initFrame();
		initComponents();
		//show window
		setVisible(true);
	}
	
	private void initFrame(){
		setSize(ABOUT_WIDTH, ABOUT_HEIGHT);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize(); //Получаем размеры текущего дисплея.
		setLocation((screen.width - getSize().width)/2, (screen.height - getSize().width)/2); //Размещаем окно по середине дисплея.
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); //Завершение работы приложения вместе с закрытием окна. Задание параметров внешнего листнера.
		setResizable(false); //allow resize window
	}
	
	private void initComponents(){

		Container pane = getContentPane();
		pane.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
				
		/*
		 * Logotype section
		 */
		JLabel logo = new JLabel(new ImageIcon(LOGO_SOURCE));
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weightx = 0.1;
		c.weighty = 1;
		c.gridheight = 6;
		pane.add(logo, c);
		
		/*
		 * Application Name section
		 */
		JLabel appName = new JLabel(APPLICATION_NAME);		
		appName.setFont(appName.getFont().deriveFont(appName.getFont().getStyle() | Font.BOLD, appName.getFont().getSize() + 4));
		c.weightx = 1.5;
		c.gridheight = 1;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 0;
		c.weighty = 0.1;
		c.anchor = GridBagConstraints.LINE_START;
		pane.add(appName, c);
		
		/*
		 * Application description section
		 */
		JLabel description = new JLabel(DESCRIPTION);
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		pane.add(description, c);
		
		/*
		 * author section
		 */
		JLabel authorH = new JLabel("Author:");
		authorH.setFont(authorH.getFont().deriveFont(authorH.getFont().getStyle() | Font.BOLD));
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 0.05;
		pane.add(authorH, c);
		
		JLabel author = new JLabel(AUTHOR_NAME);
		c.gridx = 2;
		pane.add(author, c);
		
		
		/*
		 * Version section
		 */
		JLabel versionH = new JLabel("Product version:");
		versionH.setFont(authorH.getFont());
		c.gridy = 2;
		c.gridx = 1;
		pane.add(versionH, c);
		
		JLabel version = new JLabel(VERSION);
		c.gridx = 2;
		pane.add(version, c);
		
		/*
		 * Homepage section
		 */
		JLabel homepageH = new JLabel("Project homepage:");
		homepageH.setFont(authorH.getFont());
		c.gridx = 1;
		c.gridy = 4;
		pane.add(homepageH, c);
		
		JLabel homepage = new JLabel(HOMEPAGE_URL);
		c.gridx = 2;
		pane.add(homepage, c);
		
		/*
		 * CloseButton section
		 */
		JButton closeButton = new JButton();
		closeButton.setText("close");
		closeButton.setActionCommand(CLOSE_WND);
		closeButton.addActionListener(this);
		closeButton.setFocusable(false);
		c.gridy = 5;
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.insets = new Insets(0, 0, 20, 20);
		pane.add(closeButton, c);
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().contentEquals(CLOSE_WND))
			this.dispose();

	}

}
