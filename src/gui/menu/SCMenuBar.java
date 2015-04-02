package gui.menu;

import gui.SCWindow;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import data.UnitNumbers;

/**
 * Menu Bar holding the builds listed in the specification.
 */
public class SCMenuBar extends JMenuBar{
	
	SCWindow window;
	
	public SCMenuBar(SCWindow window){
		this.window = window;
		
		JMenu basicMenu = new JMenu("Basic Goals");
		JMenu advancedMenu = new JMenu("Advanced Goals");
		JMenu ourMenu = new JMenu("Our Goals");
		
		JMenuItem basic1 = new JMenuItem("4 Zealots");
		UnitNumbers goal1 = new UnitNumbers();
		goal1.put("Zealot", 4);
		basic1.addActionListener(new MenuGoalListener(window.panel, goal1));
		
		JMenuItem basic2 = new JMenuItem("10 Zealots");
		UnitNumbers goal2 = new UnitNumbers();
		goal2.put("Zealot", 10);
		basic2.addActionListener(new MenuGoalListener(window.panel, goal2));
		
		JMenuItem basic3 = new JMenuItem("25 Zealots");
		UnitNumbers goal3 = new UnitNumbers();
		goal3.put("Zealot", 25);
		basic3.addActionListener(new MenuGoalListener(window.panel, goal3));
		
		JMenuItem basic4 = new JMenuItem("10 Stalkers");
		UnitNumbers goal4 = new UnitNumbers();
		goal4.put("Stalker", 10);
		basic4.addActionListener(new MenuGoalListener(window.panel, goal4));

		JMenuItem basic5 = new JMenuItem("2 Zealots, 6 Stalkers");
		UnitNumbers goal5 = new UnitNumbers();
		goal5.put("Zealot", 2);
		goal5.put("Stalker", 6);
		basic5.addActionListener(new MenuGoalListener(window.panel, goal5));
		
		JMenuItem basic6 = new JMenuItem("2 Zealots, 4 Void Rays");
		UnitNumbers goal6 = new UnitNumbers();
		goal6.put("Zealot", 2);
		goal6.put("Void Ray", 4);
		basic6.addActionListener(new MenuGoalListener(window.panel, goal6));
		
		JMenuItem basic7 = new JMenuItem("3 Zealots, 4 Satlkers, 2 Oracles");
		UnitNumbers goal7 = new UnitNumbers();
		goal7.put("Zealot", 3);
		goal7.put("Stalker", 4);
		goal7.put("Oracle", 2);
		basic7.addActionListener(new MenuGoalListener(window.panel, goal7));
		
		JMenuItem basic8 = new JMenuItem("3 Zealots, 5 Stalkers, 2 Immortals");
		UnitNumbers goal8 = new UnitNumbers();
		goal8.put("Zealot", 3);
		goal8.put("Stalker", 5);
		goal8.put("Immortal", 2);
		basic8.addActionListener(new MenuGoalListener(window.panel, goal8));
		
		JMenuItem basic9 = new JMenuItem("4 Zealots, 8 Stalkers, 2 Immortals, 4 Phoenix");
		UnitNumbers goal9 = new UnitNumbers();
		goal9.put("Zealot", 4);
		goal9.put("Stalker", 8);
		goal9.put("Immortal", 2);
		goal9.put("Phoenix", 4);
		basic9.addActionListener(new MenuGoalListener(window.panel, goal9));
		
		
		JMenuItem advanced1 = new JMenuItem("1 Zealots, 4 Stalkers, 2 Immortals, 2 Colossi");
		UnitNumbers goal1a = new UnitNumbers();
		goal1a.put("Zealot", 1);
		goal1a.put("Stalker", 4);
		goal1a.put("Immortal", 2);
		goal1a.put("Colossus", 2);
		advanced1.addActionListener(new MenuGoalListener(window.panel, goal1a));
		
		JMenuItem advanced2 = new JMenuItem("6 Zealots, 2 Stalkers, 3 Sentries, 4 Void Rays");
		UnitNumbers goal2a = new UnitNumbers();
		goal2a.put("Zealot", 6);
		goal2a.put("Stalker", 2);
		goal2a.put("Sentry", 3);
		goal2a.put("Void Ray", 4);
		advanced2.addActionListener(new MenuGoalListener(window.panel, goal2a));
		
		JMenuItem advanced3 = new JMenuItem("2 Zealots, 2 Stalkers, 1 Sentries, 2 Colossi, 5 Phoenix");
		UnitNumbers goal3a = new UnitNumbers();
		goal3a.put("Zealot", 2);
		goal3a.put("Stalker", 2);
		goal3a.put("Sentry", 1);
		goal3a.put("Colossus", 2);
		goal3a.put("Phoenix", 5);
		advanced3.addActionListener(new MenuGoalListener(window.panel, goal3a));
		
		JMenuItem advanced4 = new JMenuItem("10 Zealots, 7 Stalkers, 2 Sentries, 3 High Templar");
		UnitNumbers goal4a = new UnitNumbers();
		goal4a.put("Zealot", 10);
		goal4a.put("Stalker", 7);
		goal4a.put("Sentry", 2);
		goal4a.put("High Templar", 3);
		advanced4.addActionListener(new MenuGoalListener(window.panel, goal4a));
		
		JMenuItem advanced5 = new JMenuItem("8 Zealots, 10 Stalkers, 2 Sentries, 1 Immortal, 1 Observer, 3 Carriers, 2 Dark Templar");
		UnitNumbers goal5a = new UnitNumbers();
		goal5a.put("Zealot", 8);
		goal5a.put("Stalker", 10);
		goal5a.put("Sentry", 2);
		goal5a.put("Immortal", 1);
		goal5a.put("Observer", 1);
		goal5a.put("Carrier", 3);
		goal5a.put("Dark Templar", 2);
		advanced5.addActionListener(new MenuGoalListener(window.panel, goal5a));

		
		JMenuItem our1 = new JMenuItem("12 Zealots, 8 Stalkers, 4 Sentries, 3 Colossus, 1 Observer, "
				+ "2 High Templar, 3 Archons, Ground Weapons 2, Ground Armor 2, Blink , Charge, "
				+ "Extended Thermal Lance, Psionic Storm");
		UnitNumbers goal1b = new UnitNumbers();
		goal1b.put("Zealot",12);
		goal1b.put("Stalker",8);
		goal1b.put("Sentry",4);
		goal1b.put("Colossus",3);
		goal1b.put("Observer",1);
		goal1b.put("High Templar",2);
		goal1b.put("Archon",3);
		goal1b.put("Ground Weapons 2",1);
		goal1b.put("Ground Armor 2",1);
		goal1b.put("Blink", 1);
		goal1b.put("Charge", 1);
		goal1b.put("Extended Thermal Lance",1);
		goal1b.put("Psionic Storm",1);
		our1.addActionListener(new MenuGoalListener(window.panel, goal1b));
		
		JMenuItem our2 = new JMenuItem("20 Stalkers, Blink, Ground Weapons 1, Ground Armor 1");
		UnitNumbers goal2b = new UnitNumbers();
		goal2b.put("Stalker", 20);
		goal2b.put("Blink", 1);
		goal2b.put("Ground Weapons 1", 1);
		goal2b.put("Ground Armor 1", 1);
		our2.addActionListener(new MenuGoalListener(window.panel, goal2b));
		
		JMenuItem our3 = new JMenuItem("5 Phoenix, 12 Void Rays, 3 Carriers, Air Weapons 2, Air Armor 2");
		UnitNumbers goal3b = new UnitNumbers();
		goal3b.put("Phoenix", 5);
		goal3b.put("Void Ray", 12);
		goal3b.put("Carrier", 3);
		goal3b.put("Air Weapons 2", 1);
		goal3b.put("Air Armor 2", 1);
		our3.addActionListener(new MenuGoalListener(window.panel, goal3b));
		
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
		
		ourMenu.add(our1);
		ourMenu.add(our2);
		ourMenu.add(our3);
		
		add(basicMenu);
		add(advancedMenu);
		add(ourMenu);
	}
	
}
