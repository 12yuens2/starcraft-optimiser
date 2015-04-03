package gui;

import gui.menu.SCMenuBar;

import java.awt.Dimension;

import javax.swing.JFrame;

import utils.Timer;
import data.Datasheet;

/**
 * The window for the optimiser.
 */
public class SCWindow extends JFrame {
	
	public SCPanel panel;
	
	public SCWindow(){
		setTitle("Starcraft 2 Build Optimizer");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.panel = new SCPanel();
		
		SCMenuBar menu = new SCMenuBar(this);
		
		setJMenuBar(menu);
		
		add(panel);
		pack();
		setVisible(true);
		setPreferredSize(new Dimension(1200,800));
		pack();
	}	
}
