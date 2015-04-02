package gui;

import game.GameThread;
import game.UnitIs;
import gui.panels.GoalPanel;
import gui.panels.UnitPanel;
import gui.panels.UpgradePanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import data.Datasheet;
import data.UnitNumbers;
import data.UnitData;

/**
 * The main panel of the optimiser.
 */
public class SCPanel extends JPanel {
	
	private static final int UNIT_COLUMNS = 3;
	private static final int UNIT_ROWS = 14;
	
	public static final double DEFAULT_WEIGHT = 0.36;
	
	GameThread gameThread;
	
	public ArrayList<GoalPanel> goalPanels;
	JTextPane buildOutput;

	JScrollPane scrollPane;
	
	JLabel counter;
	
	JButton startButton, stopButton;
	private boolean isStarted;
	
	public SCPanel() {
		setBackground(Color.black);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		goalPanels = new ArrayList<>();
		
		for (UnitData data : Datasheet.unitData){
			if (UnitIs.Unit(data.getName())){
				goalPanels.add(new UnitPanel(data.getName()));				
			}
			
			if (UnitIs.Upgrade(data.getName())){
				goalPanels.add(new UpgradePanel(data.getName()));				
			}
		}

		buildOutput = new JTextPane();
		buildOutput.setEditable(false);
		buildOutput.setMinimumSize(new Dimension(300,300));
		
		startButton = new JButton("Start");
		startButton.addActionListener(new StartButtonListener(this));
		
		
		counter = new JLabel("-");
		counter.setOpaque(true);
		counter.setHorizontalAlignment(JLabel.CENTER);
		counter.setBackground(Color.white);
		
		scrollPane = new JScrollPane(buildOutput);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setMinimumSize(new Dimension(200,200));
		
		isStarted = false;
		
		/**
		 * GridBagConstraints to add all the components.
		 */
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = DEFAULT_WEIGHT;
		c.weighty = DEFAULT_WEIGHT;

		
		for (GoalPanel panel : goalPanels){
			add(panel,c);
			c.gridx++;
			if (c.gridx > UNIT_COLUMNS - 1){
				c.gridx = 0;
				c.gridy++;
			}
		}
		
		c.gridx= UNIT_COLUMNS + 1;
		c.gridheight = UNIT_ROWS + 1;
		c.gridy = 0;
		c.weightx = DEFAULT_WEIGHT*2;
		c.anchor = GridBagConstraints.EAST;
		add(scrollPane,c);
		
		c.gridy = UNIT_ROWS + 2;
		c.gridheight = 1;
		c.gridwidth = UNIT_COLUMNS;
		c.gridx= 0;
		c.weighty = DEFAULT_WEIGHT/50;
		add(startButton,c);
		
		c.gridx= UNIT_COLUMNS;
		add(counter,c);
		
	}

	public void setUnitCount(String unitName, int count){
		for (GoalPanel panel : goalPanels){
			if (panel.getUnitName().equals(unitName)){
				if (panel instanceof UnitPanel){
					((UnitPanel)panel).setCount(count);
				} else if (panel instanceof UpgradePanel){
					((UpgradePanel)panel).setCount(count);
				}
			}
		}
	}
	
	public boolean isStarted() {
		return isStarted;
	}
	
	/**
	 * Starts the simulation on a new thread.
	 */
	public void start(){
		this.isStarted = true;
		UnitNumbers goal = new UnitNumbers();
		for (GoalPanel panel : goalPanels){
			String name = panel.getUnitName();
			int number = panel.getNumber();
			if (number > 0){
				goal.put(name, number);
			}
		}
		startButton.setText("Searching... (Click to stop)");
		this.gameThread = new GameThread(buildOutput,counter, goal);
		gameThread.start();
	}
	
	public void stop(){
		this.isStarted = false;
		startButton.setText("Start");
		if (gameThread != null){
			gameThread.askToStop();
			try {
				//Safe since askToStop is called.
				gameThread.join();
			} catch (InterruptedException e) {
				System.err.println("Unexpected Interruption.");
			}
		}
	}
}
