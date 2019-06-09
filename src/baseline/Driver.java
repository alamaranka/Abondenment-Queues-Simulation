package baseline;

import simulation.Simulator;

public class Driver {

	public static void main(String[] args){	
		Simulator simulation = new Simulator(Baseline.simulationDuration, Baseline.capacity, Baseline.goodArrival, Baseline.goodAbandonment, Baseline.badArrival, Baseline.badAbandonment, Baseline.service);
		simulation.run(10);
		GUI gui = new GUI(simulation);
		gui.run();
	}
	
}
