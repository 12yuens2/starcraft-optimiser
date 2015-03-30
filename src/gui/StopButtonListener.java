package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StopButtonListener implements ActionListener {

	SCPanel panel;
	
	public StopButtonListener(SCPanel panel) {
		this.panel = panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (panel.isStarted()){
			panel.stop();
		}
	}

}
