package green.neural.net.train;

import green.neural.net.creatures.*;

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
