package green.neural.net.ga;

import green.neural.net.Neuron.Connection;
import green.neural.net.vectorized.VectorizedNeuralNet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class NeuralNetViewer extends JPanel {

	public void setNet(VectorizedNeuralNet net){
		this.selectedNet = net;
	}
	
	private JFrame frame;
	private VectorizedNeuralNet selectedNet;
	
	public void init(){
		frame = new JFrame("Neural Net Viewer");
		frame.setSize(400,400);
		frame.setResizable(true);
		
		frame.add(this);
	}
	
	public void show(boolean visible){
		frame.setVisible(visible);
	}
	
	public void paintComponent(Graphics g) {
		if (selectedNet == null) return;
		
		int netX = 0;
		int netY = 0;
		
		g.setColor(new Color(0,0,0,0.2f));
		g.fillRect(netX, 0, frame.getWidth(), netY);
		
		g.setColor(Color.white);
		g.drawLine(netX, 0, netX, netY);
		g.drawLine(netX, netY, frame.getWidth(), netY);
		
		for (int layer = 0; layer < selectedNet.getTopology().length; ++layer)
			for (int neuron = 0; neuron < selectedNet.getTopology()[layer]; ++neuron)
				drawNeuron(g,layer,neuron,selectedNet.getOutputMatrices()[layer].get(neuron),selectedNet.getTopology());
	}
	
	private void drawNeuron(Graphics g2, int layer, int neuron, double value, int[] net){
		Graphics2D g = (Graphics2D)g2;
		
		Point p = getCoord(layer,neuron,net);
		
		//Draw connections
		/*
		for (int i = 0; i < connections.length; ++i){
			Point p2 = getCoord(layer+1,i,net);
			float v = (float) (connections[i].weight * value);
			v = (float) ((Math.tanh(v) + 1) / 2.0f);
			g.setColor(new Color(v,v,v));
			g.drawLine(p.x, p.y, p2.x, p2.y);
		}
		*/
		
		g.setColor(Color.white);
		//g.setStroke(new BasicStroke(3));
		g.fillOval(p.x-12, p.y-12, 24, 24);
		g.setStroke(new BasicStroke(1));
		
		if (neuron == net[layer]){
			g.setColor(Color.green);
		} else {
			float v2 = (float) ((Math.tanh(value) + 1) / 2.0f);
			g.setColor(new Color(v2,v2,v2));
		}
		
		g.fillOval(p.x-10, p.y-10, 20, 20);
	}
	
	private Point getCoord(int layer, int neuron, int[] net){
		int layerX = (frame.getWidth() / net.length) * layer;
		int neuronY = (frame.getHeight() / net[neuron]) * neuron;
		return new Point(layerX, neuronY);
	}
	
}
