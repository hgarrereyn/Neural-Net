package green.neural.net.bp;
import java.util.Random;

import green.neural.net.*;

public class NetTrainer {

	public static void main(String[] args) {
		/*
		 * Create NeuralNet with topology: 2-3-1
		 */
		NeuralNet net = new NeuralNet(new int[]{ 2,3,1 });
		net.setParameters(0.1, 0.5);
		
		Random r = new Random(); //Random generator used to generate training data
		
		for (int i = 0; i < 1000; ++i){
			/*
			 * Input data
			 */
			int xa = r.nextBoolean()?1:0;
			int xb = r.nextBoolean()?1:0;
			
			double[] inputData = { xa,xb };
			
			
			/*
			 * Target data
			 */
			int ya = xa ^ xb; //XOR operator
			
			double[] targetData = { ya };
			
			/*
			 * Feed forward
			 */
			net.feedForward(inputData);
			
			/*
			 * Get results and error
			 */
			double[] resultData = net.getResults();
			double error = net.getLastError();
			double recentError = net.getRecentError();
			
			/*
			 * Train net using back propogation
			 */
			net.backPropagate(targetData);
			
			/*
			 * Log results
			 */
			//System.out.println(truncate(recentError*100,4));
			
			System.out.println("-----" + i + "-----\n"
					+ "> " + truncate(recentError*100, 4) + "% recent \n"
					+ truncate(error*100, 4) + "% this\n"
					+ "Target: " + targetData[0] + "\n"
					+ "Result: " + truncate(resultData[0], 4) + "\n");
		}
	}
	
	public static String truncate(double num, int decimalPlaces){
		long factor = (long) Math.pow(10, decimalPlaces);
	    num = num * factor;
	    long tmp = (long) num;
	    return ((double) tmp / factor) + "";
	}
}
