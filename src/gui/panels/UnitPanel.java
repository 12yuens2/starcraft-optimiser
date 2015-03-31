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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UnitPanel extends GoalPanel {
	
	JTextField input;
	
	public static final String iconLocation = "img/";
	
	public UnitPanel(String unitName){
		super(unitName);
		input = new JTextField();

		c.gridx=2;
		c.gridy=0;
		c.weighty = SCPanel.DEFAULT_WEIGHT/4;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(input,c);
				
	}

	public int getNumber() {
		int number = 0;
		try {
			String text = input.getText();
			number = Integer.parseInt(text);
		}catch (NumberFormatException | NullPointerException e){
			this.hasError = true;
		}
		return number;
	}
	
}
