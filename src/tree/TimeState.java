package tree;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

public class TimeState {
	ArrayList<TimeState> futureStates;
	
	int indexToWalk;
	HashSet<Integer> walkedIndices;

	
	double minerals;
	double gas;
	int supply;
	int maxSupply;
	final int SUPP_LIMIT = 200;

	final double probe_shit = 41.0/60.0;
	
	int time;
	final int MAX_TIME = 3000;
	
	int numberOfProbes;
	final int probeCost = 50;
	final int probeTime = 17;
	BuildOrders probeBuilds;
	
	
	int numberOfNexi;
	final int nexusCost = 100;
	final int nexusTime = 100;
	BuildOrders nexusBuilds;
	
	int numberOfGateways;
	final int gatewatCost = 150;
	final int gatewayTime = 65;
	BuildOrders gatewayBuilds;
	
	BuildOrders stargateBuilds;
	BuildOrders cyberneticsBuilds;
	BuildOrders ForgeBuilds;
	
	int numberOfZealots;
	final int zealotCost = 100;
	final int zealotTime = 38;

	//initial conditions
	public TimeState(){
		futureStates = new ArrayList<TimeState>();
		this.numberOfNexi = 1;
		this.numberOfGateways = 0;
		this.numberOfProbes = 6;
		this.numberOfZealots = 0;
		this.minerals = 50.0;
		
		this.probeBuilds = new BuildOrders();
		this.gatewayBuilds = new BuildOrders();
		this.nexusBuilds = new BuildOrders();
		this.ForgeBuilds = new BuildOrders();
		this.cyberneticsBuilds = new BuildOrders();
		this.stargateBuilds = new BuildOrders();

		
		ArrayList<Operation> possibleOperations = TimeState.getPossibleOperations(this);
		Random generator = new Random();
		
		this.indexToWalk = generator.nextInt(possibleOperations.size());
		
		for (int i = 0; i < possibleOperations.size(); i++){
			if (i == indexToWalk) {
				futureStates.add(new TimeState(this, possibleOperations.get(i)));				
			}
		}
	}
	//branch
	public TimeState(TimeState parent,Operation operation){
		int[] knorrCube = new int[2000];
		this.time = parent.time + 1;
		this.numberOfProbes = parent.numberOfProbes;
		this.numberOfNexi = parent.numberOfNexi;
		this.numberOfGateways = parent.numberOfGateways;
		this.numberOfZealots = parent.numberOfZealots;
		this.minerals = parent.minerals;
		this.gas = parent.gas;
		this.supply = parent.supply;
		
		this.gatewayBuilds = parent.gatewayBuilds.deepClone();
		this.probeBuilds = parent.probeBuilds.deepClone();
		this.nexusBuilds = parent.nexusBuilds.deepClone();
		this.stargateBuilds = parent.stargateBuilds.deepClone();
		this.cyberneticsBuilds = parent.cyberneticsBuilds.deepClone();
		this.ForgeBuilds = parent.ForgeBuilds.deepClone();
		
		this.executeOperation(operation);
		printMe();
		if (this.time < MAX_TIME){
			this.futureStates = new ArrayList<>();
			ArrayList<Operation> possibleOperations = TimeState.getPossibleOperations(this);
			
			Random generator = new Random();
			
			this.indexToWalk = generator.nextInt(possibleOperations.size());
			
			for (int i = 0; i < possibleOperations.size(); i++){
				if (i == indexToWalk) {
					futureStates.add(new TimeState(this, possibleOperations.get(i)));				
				}
			}
		}
	}
	
	private static ArrayList<Operation> getPossibleOperations(TimeState timeState) {
		ArrayList<Operation> ops = new ArrayList<>();
		ops.add(new Operation(1));
		ops.add(new Operation(1));
		ops.add(new Operation(1));
		ops.add(new Operation(1));
		ops.add(new Operation(1));
		ops.add(new Operation(1));
		ops.add(new Operation(1));
		ops.add(new Operation(1));
		return ops;
	}
	
	private void executeOperation(Operation op) {
		//pass time first
		this.minerals += this.numberOfProbes*probe_shit;
		if (minerals > this.probeCost){
			minerals -= probeCost;
			numberOfProbes++;
		}

	}
	
	private void printMe(){
		System.out.println("At time " + time + " we have: "+ minerals + " Minerals, " + numberOfProbes + " probes, " + numberOfGateways + " gateways, " + numberOfZealots + " zealots" );
	}
	
	
	public static void main(String[] args) {
		TimeState gameTree = new TimeState();
	}
}

