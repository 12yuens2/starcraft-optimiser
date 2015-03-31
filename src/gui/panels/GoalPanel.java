package gui.panels;

import gui.SCPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class GoalPanel extends JPanel {
	
	protected String unitName;
	protected GridBagConstraints c;
	protected JComponent inputComponent;
	protected JLabel pic;
	protected JLabel  name;
	protected boolean hasError;
	protected BufferedImage image;
	protected static final String iconLocation = "img/";
	
	public abstract int getNumber();
	
	public GoalPanel(String goalName){
		this.unitName = goalName;
		try {
			image = ImageIO.read(new File(iconLocation + unitName + ".jpeg" ));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pic = new JLabel(new ImageIcon(image));	

		name = new JLabel(unitName);
		name.setForeground(Color.white);
		
		setLayout(new GridBagLayout());
		
		c = new GridBagConstraints();
		
		c.weightx = SCPanel.DEFAULT_WEIGHT;		
		c.weighty = SCPanel.DEFAULT_WEIGHT;				
		
		c.gridx =0;
		c.gridy=0;
		c.gridheight=2;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		add(pic,c);
		
		c.gridx=1;
		c.gridheight=1;
		c.fill = GridBagConstraints.BOTH;
		add(name,c);
		
	}
		
	public void paintComponent(Graphics g){
		g.setColor(Color.black);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
	
	public String getUnitName(){
		return unitName;
	}
	
}
