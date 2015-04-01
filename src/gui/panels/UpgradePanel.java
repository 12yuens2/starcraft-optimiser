package gui.panels;

import gui.SCPanel;

import java.awt.Color;
import java.awt.GridBagConstraints;

import javax.swing.JCheckBox;

/**
 * @see GoalPanel
 */
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

	public void setCount(int count) {
		if (count == 1){
			getUpgrade.setSelected(true);
		} else {
			getUpgrade.setSelected(false);
		}
	}
	
}
