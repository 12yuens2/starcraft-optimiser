package gui.panels;

import gui.SCPanel;

import java.awt.GridBagConstraints;
import javax.swing.JTextField;

/**
 * @see GoalPanel
 */
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

	public void setCount(int count) {
		input.setText(""+count);
	}
	
}
