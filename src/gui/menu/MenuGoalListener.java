package gui.menu;

import gui.SCPanel;
import gui.panels.GoalPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;

import data.UnitNumbers;

/**
 * Action Listener for Menu Items to add goals listed in the specification.
 */
public class MenuGoalListener implements ActionListener {

	SCPanel panel;
	UnitNumbers goal;
	
	public MenuGoalListener(SCPanel panel,UnitNumbers goal ) {
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
