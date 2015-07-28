package green.neural.net.ga;

import green.neural.net.vectorized.VectorizedNeuralNet;

import java.util.ArrayList;

import org.json.JSONObject;

public class TrainingInstance<T extends AbstractCreature> {
	
	private String creatureName;
	
	private ArrayList<Double> bestFitness;
	private ArrayList<Double> averageFitness;
	
	private int generation;
	private VectorizedNeuralNet[] nets;
	
	private int population;
	private double cutoff;
	private int elites;
	private double mutationRate;
	
	public TrainingInstance(){
		this.creatureName = "";
		this.bestFitness = new ArrayList<Double>();
		this.averageFitness = new ArrayList<Double>();
		this.generation = 0;
		this.nets = new VectorizedNeuralNet[0];
		this.population = 0;
		this.cutoff = 0;
		this.elites = 0;
		this.mutationRate = 0;
	}
	
	public void createFile(){
		JSONObject object = new JSONObject();
		object.put("creatureName", creatureName);
		object.put("bestFitness", bestFitness);
		object.put("averageFitness", averageFitness);
		object.put("generation", generation);
		object.put("nets", nets);
		object.put("population", population);
		object.put("cutoff", cutoff);
		object.put("elites", elites);
		object.put("mutationRate", mutationRate);
		System.out.println(object);
	}

	public void setCreatureName(String creatureName) {
		this.creatureName = creatureName;
	}

	public void setBestFitness(ArrayList<Double> bestFitness) {
		this.bestFitness = bestFitness;
	}

	public void setAverageFitness(ArrayList<Double> averageFitness) {
		this.averageFitness = averageFitness;
	}

	public void setGeneration(int generation) {
		this.generation = generation;
	}

	public void setNets(VectorizedNeuralNet[] nets) {
		this.nets = nets;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public void setCutoff(double cutoff) {
		this.cutoff = cutoff;
	}

	public void setElites(int elites) {
		this.elites = elites;
	}

	public void setMutationRate(double mutationRate) {
		this.mutationRate = mutationRate;
	}
}
