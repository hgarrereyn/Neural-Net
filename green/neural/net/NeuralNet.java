package green.neural.net;

import green.neural.net.Neuron.Connection;

import java.util.Random;

public class NeuralNet {
	/** Array of neurons arranged by [layer][index] */
	private Neuron[][] neurons;
	
	/** Topology */
	private int[] netData;
	
	/** Last percent error calculated in backpropagation */
	private double error;
	
	/** Average recent error calculated over <code>errorReach</code> results */
	private double recentError;
	
	/** The amount of results to calculate the <code>recentError</code> over. Initial value: 100. */
	private int errorReach = 100; 
	
	/** The net's learning rate [0 .. 1] */
	protected double eta;
	
	/** the net's weight momentum [0 .. 1]*/
	protected double alpha;
	
	/**
	 * Constructs a new neural net with given topology and weights.
	 * 
	 * @param netData an int array containing the net topology not including bias neurons (eg. {2,2,1} would have 2 input neurons, 1 hidden layer with 2 neurons and 1 output neuron)
	 * @param data a double array containing individual neuron weights sorted in ascending order by: layer, neuron, connection
	 */
	public NeuralNet(int[] netData, double[] data){
		this.netData = netData;
		neurons = new Neuron[netData.length][];
		int dIndex = 0;
		for (int i = 0; i < netData.length; ++i){
			neurons[i] = new Neuron[netData[i] + 1];
			for (int n = 0; n <= netData[i]; ++n){
				neurons[i][n] = new Neuron(this,(i==netData.length-1?0:netData[i+1]),n); //Output layer has no connections
				neurons[i][n].setOutput(1.0);
				for (int c = 0; c < neurons[i][n].getConnections().length; ++c){
					neurons[i][n].getConnections()[c].weight = data[dIndex];
					dIndex++;
				}
			}
		}
	}
	
	/**
	 * Constructs a neural net with the given topology and random weights between -1 and 1 exclusive
	 * 
	 * @param netData an int array containing the net topology not including bias neurons (eg. {2,2,1} would have 2 input neurons, 1 hidden layer with 2 neurons and 1 output neuron)
	 */
	public NeuralNet(int[] netData){
		this.netData = netData;
		neurons = new Neuron[netData.length][];
		for (int i = 0; i < netData.length; ++i){
			neurons[i] = new Neuron[netData[i] + 1];
			for (int n = 0; n <= netData[i]; ++n){
				neurons[i][n] = new Neuron(this,(i==netData.length-1?0:netData[i+1]),n); //Output layer has no connections
				neurons[i][n].setOutput(1.0);
			}
		}
		recentError = 1.0;
	}
	
	/**
	 * returns the neuron weight values contained in this net
	 * 
	 * @return a double[] containing the neuron weight values sorted in ascending order by: layer, neuron, connection
	 */
	public double[] getData(){
		int length = 0;
		for (int i = 0; i < this.netData.length - 1; ++i){
			length += (int)((this.netData[i]+1) * this.netData[i+1]);
		}
		double[] data = new double[length];
		int dIndex = 0;
		for (int i = 0; i < this.neurons.length; ++i)
			for (int n = 0; n < (i==this.neurons.length-1?this.neurons[i].length-1:this.neurons[i].length); n++)
				for (int c = 0; c < this.neurons[i][n].getConnections().length; ++c){
					data[dIndex] = this.neurons[i][n].getConnections()[c].weight;
					dIndex++;
				}
		
		return data;
	}
	
	/**
	 * returns the topology
	 * 
	 * @return an int[] containing the net's topology
	 */
	public int[] getNetData(){
		return this.netData;
	}
	
	/**
	 * Returns the neurons
	 * 
	 * @return a Neuron[][] containing the net's neurons arranged by [layer][neuron] 
	 */
	public Neuron[][] getNeurons(){
		return this.neurons;
	}
	
	/**
	 * Set the eta and alpha values used in backpropagation
	 * 
	 * @param eta learning rate [0 .. 1]
	 * @param alpha weight momentum [0 .. 1]
	 */
	public void setParameters(double eta, double alpha){
		this.eta = eta;
		this.alpha = alpha;
	}
	
	/**
	 * Feeds the input data through the neural net and updates each neuron's output
	 * 
	 * @param inputData a double[] containing a list of input values for the input neurons
	 */
	public void feedForward(double[] inputData){
		
		for (int i = 0; i < inputData.length; ++i){
	        neurons[0][i].setOutput(inputData[i]);
	    }
	    
	    for (int i = 1; i < neurons.length; ++i){
	        Neuron[] previousLayer = neurons[i-1];
	        for (int n = 0; n < neurons[i].length - 1; ++n){
	            Neuron neuron = neurons[i][n];
	            neuron.feedForward(previousLayer);
	        }
	    }
	}
	
	/**
	 * Calculates the net's error to the target data and updates all the neuron weights using the eta and alpha values.
	 * 
	 * @param targetData a double[] containing the expected output values
	 */
	public void backPropagate(double[] targetData){
		
		/*
		 * Calculate error
		 */
	    Neuron[] outputLayer = neurons[neurons.length-1];
	    this.error = 0.0;
	    for (int i = 0; i < outputLayer.length - 1; ++i){
	        this.error += Math.pow(outputLayer[i].getOutput() - targetData[i], 2);
	    }
	    this.error /= outputLayer.length - 1;
	    this.error = Math.sqrt(this.error);

	    this.recentError = (this.recentError * this.errorReach + this.error) / (this.errorReach + 1.0);
	    
	    /*
	     * Calculate gradients
	     */
	    for (int i = 0; i < outputLayer.length - 1; ++i){
	        outputLayer[i].calculateOutputLayerGradient(targetData); //Output layer gradients
	    }
	    
	    for (int i = neurons.length - 2; i > 0; --i){
	        Neuron[] layer = neurons[i];
	        Neuron[] nextLayer = neurons[i+1];
	        
	        for (int n = 0; n < layer.length; ++n){
	            layer[n].calculateHiddenLayerGradient(nextLayer); //Hidden layer gradients
	        }
	    }
	    
	    
	    /*
	     * Update input weights
	     */
	    for (int i = neurons.length - 1; i > 0; --i) {
	        Neuron[] layer = neurons[i];
	        Neuron[] previousLayer = neurons[i-1];
	        
	        for (int n = 0; n < layer.length - 1; ++n) {
	            layer[n].updateInputWeights(previousLayer);
	        }
	    }
	}
	
	/**
	 * get the output layer neurons' values
	 * 
	 * @return a double[] containing the output layer neurons' values
	 */
	public double[] getResults(){
		Neuron[] outputLayer = neurons[neurons.length-1];
		double[] results = new double[outputLayer.length-1]; //exclude bias neuron
		
		for (int i = 0; i < outputLayer.length - 1; ++i)
			results[i] = outputLayer[i].getOutput();
		
		return results;
	}
	
	/**
	 * @return the last error percentage
	 */
	public double getLastError(){
		return error;
	}
	
	/**
	 * @return the recent error percentage over <code>errorReach</code> results
	 */
	public double getRecentError(){
		return recentError;
	}
	
	/**
	 * set the number of results to calculate the average recent error
	 * 
	 * @param errorReach
	 */
	public void setErrorReach(int errorReach){
		
		this.errorReach = errorReach;
	}
	
	/**
	 * Create and return a child net with this net and <code>net2</code> as parents, a mutation rate and parent bias weight. 
	 * 
	 * @param net2 the other net to use as a parent
	 * @param mutationRate a float in the range [0, 1) that is applied to each neuron connection to check whether to mutate
	 * @return a new, child NeuralNet
	 */
	public NeuralNet childOf(NeuralNet net2, float mutationRate){
		NeuralNet child = new NeuralNet(this.netData);
		
		Random r = new Random();
		for (int i = 0; i < this.neurons.length; ++i){
			for (int j = 0; j < this.neurons[i].length; ++j){
				for (int c = 0; c < (child.neurons[i][j].getConnections().length); ++c){
					Neuron.Connection connection = child.neurons[i][j].new Connection();
					connection.weight = r.nextBoolean()?this.neurons[i][j].getConnections()[c].weight:net2.neurons[i][j].getConnections()[c].weight;
					child.neurons[i][j].getConnections()[c] = connection;
				}
				child.neurons[i][j].mutate(mutationRate);
			}
		}
		return child;
	}
}
