package green.neural.net;

import java.util.Random;

/**
 * A class to hold individual neuron data.
 */
public class Neuron {

	/**
	 * A class to hold a single connection from a neuron to a neuron in the next layer
	 * 
	 * Holds a weight and a weight delta
	 */
	public class Connection{
		public double weight;
		public double weightDelta;
		
		public Connection(){
			this.weight = (Math.random() * 2) - 1;
			this.weightDelta = 0.0;
		}
	}
	
	private NeuralNet parent;
	
	private Connection[] connections;
	private int index;
	private double output;
	private double gradient;
	
	/**
	 * Runs the transfer function on the input value.
	 * 
	 * This transfer function is tanh and produces outputs in the range (-1,1)
	 * 
	 * @param val a value to run the transfer function on
	 * @return the output of the transfer function
	 */
	private static double transferFunction(double val){
		return Math.tanh(val);
	}
	
	/**
	 * Runs the transfer function derivative on the input value. This is useful for backpropagation.
	 * 
	 * This transfer function derivative is (1 - tanh(x))(1 + tanh(x))
	 * 
	 * @param val a value to run the transfer function on
	 * @return the output of the transfer function
	 */
	private static double transferFunctionDerivative(double val){
		return (1 - Math.tanh(val)) * (1 + Math.tanh(val));
	}
	
	/**
	 * Creates a new neuron with a given index and a known number of connections
	 * 
	 * @param numConnections the number of connections to make to the next layer (do not include bias neuron)
	 * @param index the index of this neuron in the whole neural network
	 */
	public Neuron(NeuralNet parent, int numConnections, int index){
		this.connections = new Connection[numConnections];
		for (int i = 0; i < numConnections; ++i)
			this.connections[i] = new Connection();
		this.index = index;
		this.parent = parent;
	}
	
	/**
	 * gets the neurons output (use this after calling feedForward)
	 * @return the neurons output
	 */
	public double getOutput(){
		return this.output;
	}
	
	/**
	 * Set the neurons output value.
	 * Don't call this unless you are setting the input neuron values.
	 * @param output an output value
	 */
	public void setOutput(double output){
		this.output = output;
	}
	
	/**
	 * gets an array of all the connections this neuron has (in order)
	 * @return a Connection[] containing all the connections to the next layer in order
	 */
	public Connection[] getConnections(){
		return this.connections;
	}
	
	public double getGradient(){
		return this.gradient;
	}
	
	public void feedForward(Neuron[] previousLayer){
		double sum = 0.0;
		for (int i = 0; i < previousLayer.length; ++i)
			sum += previousLayer[i].getOutput() * previousLayer[i].getConnections()[this.index].weight;
		this.output = Neuron.transferFunction(sum);
	}
	
	public void calculateOutputLayerGradient(double[] targetData){
		this.gradient = (targetData[this.index] - this.output) * Neuron.transferFunctionDerivative(this.output);
	}
	
	public void calculateHiddenLayerGradient(Neuron[] nextLayer){
		double sum = 0.0;
		for (int i = 0; i < nextLayer.length - 1; ++i)
			sum += this.connections[i].weight * nextLayer[i].getGradient();
		this.gradient = sum * Neuron.transferFunctionDerivative(this.output);
	}
	
	public void updateInputWeights(Neuron[] previousLayer){
		for (int i = 0; i < previousLayer.length; ++i){
			Neuron n = previousLayer[i];
			
			double oldDeltaWeight = n.getConnections()[this.index].weightDelta;
			double newDeltaWeight = (parent.eta * n.getOutput() * this.gradient) + (parent.alpha * oldDeltaWeight);
			
			n.getConnections()[this.index].weight += newDeltaWeight;
			n.getConnections()[this.index].weightDelta = newDeltaWeight;
		}
	}
	
	/**
	 * this function pseudo-randomly mutates each connection's weight individually with the given chance between 0 and 1 
	 * 
	 * @param mutationRate the chance an individual connection will be mutated
	 */
	public void mutate(float mutationRate){
		if (this.connections.length == 0) return;
		
		Random r = new Random();
		for (int i = 0; i < this.connections.length; ++i){
			if (r.nextFloat() < mutationRate){
				System.out.println("mutate");
				this.connections[i].weight = (r.nextFloat()*2)-1;
			}
		}
	}
}
