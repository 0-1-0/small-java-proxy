package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import myHTTPProxy.AllConstants;


public class StatisticBox extends JFrame implements ActionListener, AllConstants {

	private static final long serialVersionUID = 1L;
	private String host;
	private JPanel jj;	
	private GridBagConstraints gjj;
	private JComboBox period;

	public StatisticBox(String title) {
		super(title);
		initFrame();
		initComponents();
		//show window
		setVisible(true);
	}
	
	private void initFrame(){
		host = this.getTitle();
		setSize(STAT_WIDTH, STAT_HEIGHT); //Задаем размеры окна.
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize(); //Получаем размеры текущего дисплея.
		setLocation((screen.width - getSize().width)/2, (screen.height - getSize().width)/2); //Размещаем окно по середине дисплея.
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); //Завершение работы приложения вместе с закрытием окна. Задание параметров внешнего листнера.
		setResizable(true); //allow resize window
	}
	
	private void initComponents(){

		Container pane = getContentPane();
		pane.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0.9;
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 3;
		c.insets = new Insets(20, 10, 20, 10);
		
		JPanel j = (new StatisticsPanelFactory()).getPanel(host, new GregorianCalendar(), ST_MONTH);
		
		jj = new JPanel();
		jj.setLayout(new GridBagLayout());
		gjj = new GridBagConstraints();
		gjj.weightx = 0.1;
		gjj.weighty = 0.1;
		gjj.fill = GridBagConstraints.BOTH;
		jj.add(j, gjj);
		
		pane.add(jj, c);
		
		/*
		 * Choose date ComboBox section
		 */
		period = new JComboBox(DATE_PERIODS);
		period.setFocusable(false);
		period.setActionCommand(PERIOD_CHANGED);
		period.addActionListener(this);
		c.gridx = 1;
		c.gridheight = 1;
		c.weightx = 0.001;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(20, 0, 20, 10);
		pane.add(period, c);
		
		/*
		 * Color panel section
		 */
		JPanel colorPane = new JPanel();
		
		JLabel u = new JLabel("Upload");
		u.setForeground(UPLOADBAR_COLOR);		
		colorPane.add(u, BorderLayout.NORTH);
		
		JLabel d = new JLabel("Download");
		d.setForeground(DOWNLOADBAR_COLOR);	
		colorPane.add(d, BorderLayout.SOUTH);	
		
		c.gridy = 1;
		pane.add(colorPane, c);
		
		/*
		 * CloseButton section
		 */
		JButton closeButton = new JButton();
		closeButton.setText("close");
		closeButton.setActionCommand(CLOSE_WND);
		closeButton.addActionListener(this);
		closeButton.setFocusable(false);
		c.gridy = 2;
		c.anchor = GridBagConstraints.SOUTHEAST;		
		pane.add(closeButton, c);		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String c = e.getActionCommand();
		
		if(CLOSE_WND.contentEquals(c))
			this.dispose();
		
		if(PERIOD_CHANGED.contentEquals(c)){
			jj.removeAll();
			JPanel j = (new StatisticsPanelFactory()).getPanel(host, new GregorianCalendar(), period.getSelectedIndex());
			jj.add(j, gjj);
			jj.revalidate();
		}

	}

}
