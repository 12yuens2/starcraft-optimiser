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
	
	public UpgradePanel(String unitName) {
		super(unitName);
		getUpgrade = new JCheckBox();
		getUpgrade.setBackground(Color.black);
		
		c.gridx=2;
		c.gridy=0;
		c.weighty = SCPanel.DEFAULT_WEIGHT/4;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(getUpgrade,c);

	}
	
	public int getNumber() {
		if (getUpgrade.isSelected()){
			return 1;
		} else {
			return 0;
		}
	}
	
}
