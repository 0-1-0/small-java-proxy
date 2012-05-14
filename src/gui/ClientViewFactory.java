package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import myHTTPProxy.AllConstants;

public class ClientViewFactory implements AllConstants{

	private boolean blue = true;
	private GridBagLayout ll;
	private static ImageIcon st = new ImageIcon(STATISTIC_BTN_PICTURE);
	
	
	public ClientViewFactory(){
		ll = new GridBagLayout();
	}
	
	private JPanel getClientView(String hostname, boolean selected,ActionListener x){		
		JPanel j = new JPanel();
		j.setLayout(ll);
		j.setName(hostname);		
		
		blue = ! blue;
		if(blue)
			j.setBackground(D_BLUE);
		else
			j.setBackground(Color.WHITE);		
		
		JCheckBox ch = new JCheckBox();
		ch.setForeground(Color.GRAY);
		ch.setSelected(selected);
		ch.setBackground(j.getBackground());
		ch.setText(hostname);
		ch.setFocusable(false);
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.gridy = 0;
		c.gridx = 0;
		c.weightx = 0.1;
		c.weighty = 0.1;
		c.insets.left = (int)(Math.PI*Math.E);// the secret of interface
		j.add(ch, c);	
		
		JButton b = new JButton();
		b.setSize(20, 8);
		b.setFocusable(false);
		b.setIcon(st);
		b.setContentAreaFilled(false);
		b.setActionCommand(STAT_PREFIX + hostname);
		b.addActionListener(x);
		c.gridx = 1;
		c.anchor = GridBagConstraints.EAST;
		j.add(b,c);
		
		return j;
	}
	
	private JPanel getEmptyBox(){
		JPanel j = new JPanel();
		JLabel l = new JLabel("0");
		blue = ! blue;
		if(blue)
			j.setBackground(D_BLUE);
		else
			j.setBackground(Color.WHITE);
		l.setForeground(j.getBackground());
		j.add(l);
		j.setName(EMPTY_BOX_MARKER);
		
		return j;
	}
	
	public JPanel getClientsPanel(Collection<String> cls, ActionListener x){
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 0.1;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		blue = true;
		for(String ia:cls){
			pane.add(this.getClientView(ia, false, x), gbc);
			gbc.gridy += 1;
		}
		
		for(int i = 0; i < 50; i++){
			pane.add(this.getEmptyBox(), gbc);
			gbc.gridy += 1;
		}
		
		return pane;
	}

}
