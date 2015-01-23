package green.neural.net.main;

import green.neural.net.ga.*;

public class Main {

	public static void main(String[] args) {
		try {
			GeneticTrainer<JointCreature> trainer = new GeneticTrainer<JointCreature>(JointCreature.class);
			trainer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
