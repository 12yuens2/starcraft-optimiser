package gui;

import game.GameThread;
import game.UnitIs;
import game.tree.TimeState;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import data.Datasheet;
import data.UnitData;

public class SCPanel extends JPanel {
	
	private static final int UNIT_COLUMNS = 3;
	private static final int UNIT_ROWS = 14;
	
	private static final double DEFAULT_WEIGHT = 0.36;
	
	GameThread gameThread;
	
	ArrayList<GoalPanel> unitPanels;
	JTextPane buildOutput;
	JButton startButton, stopButton;
	private boolean isStarted;
	
	public SCPanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		unitPanels = new ArrayList<>();
		
		for (UnitData data : Datasheet.unitData){
			if (UnitIs.Unit(data.getName())){
				unitPanels.add(new UnitPanel(data.getName()));				
			}
			
			if (UnitIs.Upgrade(data.getName())){
				unitPanels.add(new UpgradePanel(data.getName()));				
			}
		}

		buildOutput = new JTextPane();
		buildOutput.setPreferredSize(new Dimension(400,400));
		//buildOutput.setEditable(false);
		
		startButton = new JButton("Start");
		startButton.addActionListener(new StartButtonListener(this));
		
		stopButton = new JButton("Stop");
		stopButton.addActionListener(new StopButtonListener(this));
		
		isStarted = false;
		
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = DEFAULT_WEIGHT;
		c.weighty = DEFAULT_WEIGHT;

		
		for (GoalPanel panel : unitPanels){
			add(panel,c);
			c.gridx++;
			if (c.gridx > UNIT_COLUMNS - 1){
				c.gridx = 0;
				c.gridy++;
			}
		}
		c.gridx= UNIT_COLUMNS;
		c.gridheight = UNIT_ROWS + 1;
		c.gridy = 0;
		add(buildOutput,c);
		
		c.gridy = UNIT_ROWS + 2;
		c.gridheight = 1;
		c.gridwidth = UNIT_COLUMNS;
		c.gridx= 0;
		add(startButton,c);
		
		c.gridx= UNIT_COLUMNS;
		add(stopButton,c);
		
		revalidate();
	}

	public boolean isStarted() {
		return isStarted;
	}
	
	public void start(){
		this.isStarted = true;
		HashMap<String, Integer> goal = new HashMap<>();
		for (GoalPanel panel : unitPanels){
			String name = panel.getUnitName();
			int number = panel.getNumber();
			if (number > 0){
				goal.put(name, number);
			}
		}
		for (Entry<String, Integer> unitGoal : goal.entrySet()){
			System.out.println(unitGoal.getKey() + " , " + unitGoal.getValue());
		}
		this.gameThread = new GameThread(buildOutput, goal);
		gameThread.start();
		gameThread.run();
	}
	
	public void stop(){
		this.isStarted = false;
	}
	
}
