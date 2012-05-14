package gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import util.JProxy;

import myHTTPProxy.AllConstants;

public class OptBox extends JFrame implements ActionListener, AllConstants {

	private static final long serialVersionUID = 1L;
	JTextField t;
	

	public OptBox(String title) {
		super(title);
		initFrame();
		initComponents();
		//show window
		setVisible(true);
	}
	
	private void initFrame(){
		setSize(OPT_WIDTH, OPT_HEIGHT); //Задаем размеры окна.
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize(); //Получаем размеры текущего дисплея.
		setLocation((screen.width - getSize().width)/2, (screen.height - getSize().width)/2); //Размещаем окно по середине дисплея.
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); //Завершение работы приложения вместе с закрытием окна. Задание параметров внешнего листнера.
		setResizable(false); //allow resize window
	}
	
	private void initComponents(){

		Container pane = getContentPane();
		pane.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.1;
		c.weighty = 0.1;
		
		/*
		 * Port section		
		 */		
		JLabel l = new JLabel();
		l.setText("<html><b>Port:</b></html>");
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(5, 10, 5, 5);
		pane.add(l, c);		
		
		t = new JTextField();
		t.setInputVerifier(new PortVerifier());
		t.setText(String.valueOf(JProxy.getInstance().getPort()));
		if(JProxy.getInstance().isRunning())t.setEnabled(false);
		c.gridx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		pane.add(t, c);
		
		JSeparator s = new JSeparator();
		c.gridy += 1;
		c.gridx = 0;
		c.gridwidth = 2;
		c.insets = new Insets(0, 0, 0, 0);
		c.anchor = GridBagConstraints.SOUTH;
		pane.add(s, c);
		
		/*
		 * CloseButton section
		 */
		JButton closeButton = new JButton();
		closeButton.setText("close");
		closeButton.setActionCommand(CLOSE_WND);
		closeButton.addActionListener(this);
		closeButton.setFocusable(false);
		c.gridy = c.gridy + 1;
		c.gridx = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 10, 5, 5);
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.SOUTHEAST;
		pane.add(closeButton, c);	
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().contentEquals(CLOSE_WND)){
			if(veryfyPort(t.getText()))
				JProxy.getInstance().setPort(Integer.parseInt(t.getText()));
			
			this.dispose();
		}

	}
	
	private boolean veryfyPort(String port){
		Pattern p = Pattern.compile("\\d{1,4}");
		Matcher m = p.matcher(port);
		return m.matches();
	}
	
	private class PortVerifier extends InputVerifier {
		@Override
		public boolean verify(JComponent input) {
			JTextField t = (JTextField)input;			
			return veryfyPort(t.getText());
		}
    }

}
