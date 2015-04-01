package gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import data.Goal;

public class SCMenuBar extends JMenuBar{
	
	SCWindow window;
	
	public SCMenuBar(SCWindow window){
		this.window = window;
		
		JMenu basicMenu = new JMenu("Basic Goals");
		JMenu advancedMenu = new JMenu("Advanced Goals");
		
		JMenuItem basic1 = new JMenuItem("4 Zealots");
		Goal goal1 = new Goal();
		goal1.put("Zealot", 4);
		basic1.addActionListener(new MenuGoalListener(window.panel, goal1));
		
		JMenuItem basic2 = new JMenuItem("10 Zealots");
		Goal goal2 = new Goal();
		goal2.put("Zealot", 10);
		basic2.addActionListener(new MenuGoalListener(window.panel, goal2));
		
		JMenuItem basic3 = new JMenuItem("25 Zealots");
		Goal goal3 = new Goal();
		goal3.put("Zealot", 25);
		basic3.addActionListener(new MenuGoalListener(window.panel, goal3));
		
		JMenuItem basic4 = new JMenuItem("10 Stalkers");
		Goal goal4 = new Goal();
		goal4.put("Stalker", 10);
		basic4.addActionListener(new MenuGoalListener(window.panel, goal4));

		JMenuItem basic5 = new JMenuItem("2 Zealots, 6 Stalkers");
		Goal goal5 = new Goal();
		goal5.put("Zealot", 2);
		goal5.put("Stalker", 6);
		basic5.addActionListener(new MenuGoalListener(window.panel, goal5));
		
		JMenuItem basic6 = new JMenuItem("2 Zealots, 4 Void Rays");
		Goal goal6 = new Goal();
		goal6.put("Zealot", 2);
		goal6.put("Void Ray", 4);
		basic6.addActionListener(new MenuGoalListener(window.panel, goal6));
		
		JMenuItem basic7 = new JMenuItem("3 Zealots, 4 Satlkers, 2 Oracles");
		Goal goal7 = new Goal();
		goal7.put("Zealot", 3);
		goal7.put("Stalker", 4);
		goal7.put("Oracle", 2);
		basic7.addActionListener(new MenuGoalListener(window.panel, goal7));
		
		JMenuItem basic8 = new JMenuItem("3 Zealots, 5 Stalkers, 2 Immortals");
		Goal goal8 = new Goal();
		goal8.put("Zealot", 3);
		goal8.put("Stalker", 5);
		goal8.put("Immortal", 2);
		basic8.addActionListener(new MenuGoalListener(window.panel, goal8));
		
		JMenuItem basic9 = new JMenuItem("4 Zealots, 8 Stalkers, 2 Immortals, 4 Phoenix");
		Goal goal9 = new Goal();
		goal9.put("Zealot", 4);
		goal9.put("Stalker", 8);
		goal9.put("Immortal", 2);
		goal9.put("Phoenix", 4);
		basic9.addActionListener(new MenuGoalListener(window.panel, goal9));
		
		
		JMenuItem advanced1 = new JMenuItem("1 Zealots, 4 Stalkers, 2 Immortals, 2 Colossi");
		Goal goal1a = new Goal();
		goal1a.put("Zealot", 1);
		goal1a.put("Stalker", 4);
		goal1a.put("Immortal", 2);
		goal1a.put("Colossus", 2);
		advanced1.addActionListener(new MenuGoalListener(window.panel, goal1a));
		
		JMenuItem advanced2 = new JMenuItem("6 Zealots, 2 Stalkers, 3 Sentries, 4 Void Rays");
		Goal goal2a = new Goal();
		goal2a.put("Zealot", 6);
		goal2a.put("Stalker", 2);
		goal2a.put("Sentry", 3);
		goal2a.put("Void Ray", 4);
		advanced2.addActionListener(new MenuGoalListener(window.panel, goal2a));
		
		JMenuItem advanced3 = new JMenuItem("2 Zealots, 2 Stalkers, 1 Sentries, 2 Colossi, 5 Phoenix");
		Goal goal3a = new Goal();
		goal3a.put("Zealot", 2);
		goal3a.put("Stalker", 2);
		goal3a.put("Sentry", 1);
		goal3a.put("Colossus", 2);
		goal3a.put("Phoenix", 5);
		advanced3.addActionListener(new MenuGoalListener(window.panel, goal3a));
		
		JMenuItem advanced4 = new JMenuItem("10 Zealots, 7 Stalkers, 2 Sentries, 3 High Templar");
		Goal goal4a = new Goal();
		goal4a.put("Zealot", 10);
		goal4a.put("Stalker", 7);
		goal4a.put("Sentry", 2);
		goal4a.put("High Templar", 3);
		advanced4.addActionListener(new MenuGoalListener(window.panel, goal4a));
		
		JMenuItem advanced5 = new JMenuItem("8 Zealots, 10 Stalkers, 2 Sentries, 1 Immortal, 1 Observer, 3 Carriers, 2 Dark Templar");
		Goal goal5a = new Goal();
		goal5a.put("Zealot", 8);
		goal5a.put("Stalker", 10);
		goal5a.put("Sentry", 2);
		goal5a.put("Immortal", 1);
		goal5a.put("Observer", 1);
		goal5a.put("Carrier", 3);
		goal5a.put("Dark Templar", 2);
		advanced5.addActionListener(new MenuGoalListener(window.panel, goal5a));

		basicMenu.add(basic1);
		basicMenu.add(basic2);
		basicMenu.add(basic3);
		basicMenu.add(basic4);
		basicMenu.add(basic5);
		basicMenu.add(basic6);
		basicMenu.add(basic7);
		basicMenu.add(basic8);
		basicMenu.add(basic9);
		
		advancedMenu.add(advanced1);
		advancedMenu.add(advanced2);
		advancedMenu.add(advanced3);
		advancedMenu.add(advanced4);
		advancedMenu.add(advanced5);
		
		add(basicMenu);
		add(advancedMenu);
		
	}
	
}
