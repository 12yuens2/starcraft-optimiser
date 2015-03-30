package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartButtonListener implements ActionListener {

	SCPanel panel;
	
	public StartButtonListener(SCPanel panel) {
		this.panel = panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!panel.isStarted()){
			panel.start();
		}
	}

}
