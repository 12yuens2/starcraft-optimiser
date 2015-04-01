package gui;

import gui.panels.GoalPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map.Entry;

public class MenuGoalListener implements ActionListener {

	SCPanel panel;
	HashMap<String,Integer> goal;
	
	public MenuGoalListener(SCPanel panel,HashMap<String,Integer> goal ) {
		this.panel = panel;
		this.goal = goal;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		for (GoalPanel goalPanel : panel.goalPanels){
			goalPanel.setCount(0);
		}
		for (Entry<String,Integer> unitCount : goal.entrySet()){
			panel.setUnitCount(unitCount.getKey(), unitCount.getValue());
		}
	}

}
