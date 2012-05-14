package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import myHTTPProxy.AllConstants;
import util.ClientInfoBank;
import util.Traffic;

public class StatisticsPanelFactory implements AllConstants{
	
	public JPanel getPanel(String host, Calendar c, int period){
		JPanel j = new JPanel();
		j.setBorder(BorderFactory.createEtchedBorder());
		j.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		j.setBackground(STAT_BCK);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 5, 0, 5);
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.weightx = 0.1;
		gbc.weighty = 0.1;
		
		Traffic[] traf = ClientInfoBank.getInstance().getClientByAddress(host).getStatisticsByDay(c);
		if(period == ST_MONTH)		
			traf = ClientInfoBank.getInstance().getClientByAddress(host).getStatisticsByMonth(c);
		if(period == ST_YEAR)
			traf = ClientInfoBank.getInstance().getClientByAddress(host).getStatisticsByYear(c);		
		
		long max = 1, maxU = 0, maxD = 0;
		for(int i = 0; i < traf.length; i++){
			if(traf[i].getU() > maxU)maxU = traf[i].getU();
			if(traf[i].getD() > maxD)maxD = traf[i].getD();			
		}
		if(maxU + maxD > max)max = maxU + maxD;
		
		for(int i = 0;i < traf.length; i++){
			//System.out.println(traf[i].getD() + " " + traf[i].getU());
			JPanel m = new JPanel();
			m.setLayout(new GridBagLayout());
			m.setBackground(D_BLUE);
			
			GridBagConstraints p = new GridBagConstraints();
			p.fill = GridBagConstraints.BOTH;
			p.gridx = 0;
			p.gridy = 0;
			p.weightx = 0.1;
			
			JPanel empty = new JPanel();
			empty.setBackground(m.getBackground());
			p.weighty = max - traf[i].getD() - traf[i].getU();
			m.add(empty, p);
			
			JPanel u = new JPanel();
			u.setBackground(UPLOADBAR_COLOR);
			p.gridy = 1;
			p.weighty = traf[i].getU();
			m.add(u, p);
			
			JPanel d = new JPanel();
			d.setBackground(DOWNLOADBAR_COLOR);
			p.gridy = 2;
			p.weighty = traf[i].getD();
			m.add(d, p);
			
			j.add(m, gbc);	
			gbc.gridx += 1;
		}
		
		return j;
	}	

}
