package gui.panels;

import gui.SCPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
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
	BufferedImage image;
	
	public UpgradePanel(String unitName) {
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.weightx = SCPanel.DEFAULT_WEIGHT;		
		c.weighty = SCPanel.DEFAULT_WEIGHT;		
		
		
		this.unitName = unitName;
		Random r = new Random();
		this.c = Color.black;
		
		
		try {
			System.out.println(unitName);
			image = ImageIO.read(new File(UnitPanel.iconLocation + unitName + ".jpeg" ));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		getUpgrade = new JCheckBox();
		getUpgrade.setBackground(Color.black);
		pic = new JLabel(new ImageIcon(image));		
		name = new JLabel(unitName);
		name.setForeground(Color.white);
		
		c.gridx =0;
		c.gridy=0;
		c.gridheight=2;
		c.anchor = GridBagConstraints.LINE_START;
		add(pic,c);
		
		c.gridx=1;
		c.gridheight=1;
		add(name,c);
		
		c.gridx=1;
		c.gridy=1;
		c.weighty = SCPanel.DEFAULT_WEIGHT/4;
		c.anchor = GridBagConstraints.LINE_END;
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
