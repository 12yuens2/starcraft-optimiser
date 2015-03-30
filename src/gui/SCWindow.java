package gui;

import javax.swing.JFrame;

import data.Datasheet;

public class SCWindow extends JFrame {
	
	SCPanel panel;
	
	public SCWindow(){
		
		setTitle("Starcraft 2 Build Optimizer");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.panel = new SCPanel();
		
		add(panel);
		pack();
		setVisible(true);
		
	}
	
	public static void main(String[] args) {
		Datasheet.init();
		new SCWindow();
	}
	
}
