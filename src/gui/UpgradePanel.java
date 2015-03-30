package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Random;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UpgradePanel extends GoalPanel{

	JCheckBox getUpgrade;
	String unitName;
	Color c;
	JLabel pic;
	JLabel  name;
	boolean hasError;
	
	public UpgradePanel(String unitName) {
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		
		this.unitName = unitName;
		Random r = new Random();
		this.c = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
		
		getUpgrade = new JCheckBox();
		pic = new JLabel("{{PICTURE HERE}}");		
		name = new JLabel(unitName);
		
		c.gridx =0;
		c.gridy=0;
		c.gridheight=2;
		add(pic,c);
		
		c.gridx=1;
		c.gridheight=1;
		add(name,c);
		
		c.gridx=1;
		c.gridy=1;
		add(getUpgrade,c);
		
		setPreferredSize(new Dimension(150,50));
	}

	public void paintComponent(Graphics g){
		g.setColor(c);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
	
	public String getUnitName(){
		return unitName;
	}
	
	public int getNumber() {
	
		if (getUpgrade.isSelected()){
			return 1;
		} else {
			return 0;
		}
	}
	
	
}
