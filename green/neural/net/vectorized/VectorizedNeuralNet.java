package green.neural.net.vectorized;

import java.util.ArrayList;
import java.util.Random;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.RandomMatrices;
import org.ejml.simple.SimpleEVD;
import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.UtilSimpleMatrix;

public class VectorizedNeuralNet {
	
	private int[] topology;
	private int dataLength;
	
	private SimpleMatrix[] w;
	private SimpleMatrix[] b;
	private SimpleMatrix[] a;
	private SimpleMatrix[] z;
	
	public VectorizedNeuralNet(int[] topology, double[] data){
		this(topology);
		
		int index = 0;
		
		for (int i = 1; i < topology.length ; ++i){
			for (int j = 0; j < this.w[i-1].getNumElements(); ++j)
				this.w[i-1].set(j, data[j+index]);
			index += this.w[i-1].getNumElements();
			
			for (int j = 0; j < this.b[i-1].getNumElements(); ++j)
				this.b[i-1].set(j, data[j+index]);
			index += this.b[i-1].getNumElements();
		}
	}
	
	public VectorizedNeuralNet(int[] topology){
		this.topology = topology;
		this.dataLength = 0;
		
		//Initialize matrices
		this.w = new SimpleMatrix[this.topology.length - 1];
		this.b = new SimpleMatrix[this.topology.length - 1];
		this.a = new SimpleMatrix[this.topology.length];
		this.z = new SimpleMatrix[this.topology.length];
		
		for (int i = 1; i < topology.length ; ++i){
			int prev = topology[i-1];
			int curr = topology[i];
			
			this.dataLength += (curr * prev) + curr;
			
			Random r = new Random();
			
			this.w[i-1] = SimpleMatrix.wrap(RandomMatrices.createRandom(curr, prev, -1, 1, r));
			this.b[i-1] = SimpleMatrix.wrap(RandomMatrices.createRandom(curr, 1, -1, 1, r));
			this.a[i-1] = new SimpleMatrix(prev,1);
			this.z[i-1] = new SimpleMatrix(prev,1);
		}
		
		this.a[this.topology.length - 1] = new SimpleMatrix(topology[topology.length - 1], 1);
		this.z[this.topology.length - 1] = new SimpleMatrix(topology[topology.length - 1], 1);
	}
	
	public void propagate(double[] inputs){
		for (int i = 0; i < inputs.length; ++i)
			this.a[0].set(i, inputs[i]);
		
		for (int i = 1; i < this.topology.length; ++i){
			this.z[i] = (this.w[i-1].mult(this.a[i-1])).plus(this.b[i-1]);
			transfer(this.z[i], this.a[i]);
		}
	}
	
	public double[] getOutput(){
		double[] output = new double[this.a[this.topology.length - 1].getNumElements()];
		for (int i = 0; i < output.length; ++i)
			output[i] = this.a[this.topology.length - 1].get(i);
		return output;
	}
	
	public double[] getData(){
		double[] data = new double[this.dataLength];
		int index = 0;
		
		for (int i = 0; i < topology.length - 1; ++i){
			double[] newData = this.w[i].getMatrix().data;
			System.arraycopy(newData, 0, data, index, newData.length);
			index += newData.length;
			
			newData = this.b[i].getMatrix().data;
			System.arraycopy(newData, 0, data, index, newData.length);
			index += newData.length;
		}
		
		return data;
	}
	
	public int[] getBinaryData(){
		double[] data = this.getData();
		int[] binaryData = new int[data.length * 32];
		
		for (int i = 0; i < data.length; ++i){
			data[i] = (int)data[i];
			for (int b = 0; b < 32; ++b){
				binaryData[i*32 + b] = (int)data[i] & 1;
				data[i] = (int)data[i] >> 1;
			}
		}
		
		return binaryData;
	}
	
	public double[] binaryToDouble(int[] bits){
		double[] data = new double[bits.length / 32];
		
		for (int i = 0; i < data.length; ++i){
			data[i] = 0;
			for (int b = 0; b < 32; ++b)
				data[i] += bits[i*32 + b] * Math.pow(2,b);
		}
		
		return data;
	}
	
	public int[] getTopology(){
		return this.topology;
	}
	
	public SimpleMatrix[] getOutputMatrices(){
		return a;
	}
	
	public VectorizedNeuralNet childOf(VectorizedNeuralNet p2, double mutationRate){
		double[] d1 = this.getData();
		double[] d2 = p2.getData();
		
		double[] d3 = new double[d1.length];
		
		int source = (int)(Math.random() * d1.length);
		for (int i = 0; i < d1.length; ++i)
			d3[i] = Math.random() > mutationRate ? (Math.random() > 0.1 ? d1[i] : d2[i]) : (Math.random() * 2) - 1;
			
		return new VectorizedNeuralNet(this.topology, d3);
		
		
		/*
		int[] d1 = this.getBinaryData();
		int[] d2 = p2.getBinaryData();
		int[] d3 = new int[d1.length];
		
		for (int i = 0; i < d3.length; ++i){
			d3[i] = Math.random() > mutationRate ? (Math.random() > 1 ? d1[i] : d2[i]) : (d3[i]==1?0:1);
		}
		
		return new VectorizedNeuralNet(this.topology, this.binaryToDouble(d3));*/
	}
	
	private static void transfer(SimpleMatrix z, SimpleMatrix a){
		for (int i = 0; i < z.getNumElements(); ++i)
			//a.set(i, 1.0 / (1.0 + Math.exp(z.get(i))));
			a.set(i, Math.tanh(z.get(i)));
	}
}
