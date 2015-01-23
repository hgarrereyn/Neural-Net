package green.neural.net.ga;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.WorldManifold;
import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.dynamics.joints.WeldJointDef;

import green.neural.net.NeuralNet;
import green.neural.net.Neuron;
import green.neural.net.Neuron.Connection;

public class GeneticTrainer <T extends AbstractCreature> {
	
	JFrame frame;
	//private static int width = 1200;
	//private static int height = 800;
	private static float fps = 1.0f/60.0f;
	
	private Class<T> creature;
	private T creatureT;
	private int frames = 1800;
	private int population = 30;
	private float cutoff = 0.9f;
	private int elites = 1;
	
	private float mutationRate = 0.05f;
	
	private float offsetX = 0;
	private float offsetY = 0;
	private boolean maxSimulationSpeed = false;
	private boolean follow = false;
	private boolean drawCreatures = true;
	private int timeScale = 1;
	private int thisFrame = 0;
	private int scale = 100;
	private int generation = 0;
	private long time = 0;
	
	private float delta = 0;
	private long startTime;
	private long lastTime;
	long lastDraw;
	
	private ArrayList<Double> bestFitness;
	private ArrayList<Double> averageFitness;
	private double graphMax = 20;
	private double graphMin;
	
	public class Tracker{
		public Tracker(){
			this.data = new double[5];
		}
		
		public int index;
		public double[] data;
	}
	
	World world;
	private T[] creatures;
	private Tracker[] trackers;
	private NeuralNet[] nets;
	
	@SuppressWarnings("serial")
	private class DrawPanel extends JPanel{

		public DrawPanel(){}
		
		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
		    RenderingHints rhints = g2d.getRenderingHints();
		    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			g.setColor(new Color(0x222222));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			if (drawCreatures){
				g.setColor(new Color(0x2f2f2f));
				float w = 1f;
				float start = offsetX;
				float end = offsetX + frame.getWidth()/scale;
				for (float i = start-offsetX%w; i <= end; ++i){
					g.drawLine((int)((i - offsetX) * scale), 0, (int)((i - offsetX) * scale), frame.getHeight());
				}
				
				float start2 = offsetY;
				float end2 = offsetY + frame.getHeight()/scale;
				for (float i = start2-offsetY%w; i < end2; ++i){
					g.drawLine(0, (int)((i - offsetY) * scale), frame.getWidth(), (int)((i - offsetY) * scale));
				}
			}
			
			g.setColor(Color.white);
			g.drawString(((int)(offsetX*10))/10f + ", " + ((int)(offsetY*10))/10f + " :: " + scale, 10, 20);
			g.drawString("Frames: " + thisFrame + " / " + frames, 10, 35);
			if (maxSimulationSpeed)
				g.drawString("Max simulation speed", 160, 35);
			else
				g.drawString("Time scale: x" + timeScale, 160, 35);
			g.drawString("Duration: " + time/1000.0f + "s", 10, 50);
			g.drawString("Generation: " + generation, 10, 65);
			g.drawString("Population: " + population, 10, 80);
			g.drawString("Cutoff: " + cutoff, 10, 95);
			g.drawString("Elites: " + elites, 10, 110);
			String n = "{" + creatureT.getNet()[0];
			for (int i = 1; i < creatureT.getNet().length; ++i)
				n += ", " + creatureT.getNet()[i];
			n += "}";
			g.drawString("Net: " + n, 10, 125);
			
			g.drawString("Follow: " + (follow?"ON":"OFF"), 160, 20);
			
			if (!bestFitness.isEmpty()){
				g.drawString("Last results:", 160, 65);
				
				g.drawString("Best:", 160, 80);
				g.drawString("Average:", 160, 95);
				
				g.drawString(GeneticTrainer.truncate(bestFitness.get(bestFitness.size()-1), 4), 250, 80);
				g.drawString(GeneticTrainer.truncate(averageFitness.get(averageFitness.size()-1), 4), 250, 95);
			}
			
			int index = 0;
			for (int i = 1; i < trackers.length; ++i){
				if (creatureT.fitness(trackers[i]) > creatureT.fitness(trackers[index]))
					index = i;
			}
			
			if (follow){
				offsetX = ((float)(creatures[index].getPosition().x * 0.2f + offsetX * 0.8f));
				offsetY = ((float)(creatures[index].getPosition().y * 0.2f + offsetY * 0.8f));
			}
			
			if (drawCreatures){
				Body body = world.getBodyList();
				for (int i = 0; i < world.getBodyCount(); ++i){
					if (body == null) continue;
					g.setColor(Color.white);
					this.drawBody(body, g, false);
					body = body.getNext();
				}
				
				
				//Color the leader
				g.setColor(Color.green);
				for (int i = 0; i < creatures[index].getBodies().length; ++i)
					this.drawBody(creatures[index].getBodies()[i], g, false);
				
				
				//Draw leader's net
				int netX = frame.getWidth() * 2/3;
				int netY = frame.getHeight() * 1/3;
				
				g.setColor(new Color(0,0,0,0.2f));
				g.fillRect(netX, 0, frame.getWidth(), netY);
				
				g.setColor(Color.white);
				g.drawLine(netX, 0, netX, netY);
				g.drawLine(netX, netY, frame.getWidth(), netY);
				
				NeuralNet leaderNet = nets[trackers[index].index];
				Neuron[][] leaderNeurons = leaderNet.getNeurons();
				for (int layer = 0; layer < leaderNeurons.length; ++layer)
					for (int neuron = 0; neuron < (layer==leaderNeurons.length-1?leaderNeurons[layer].length-1:leaderNeurons[layer].length); ++neuron)
						drawNeuron(g,layer,neuron,(float)leaderNeurons[layer][neuron].getOutput(),leaderNet.getNetData(), leaderNeurons[layer][neuron].getConnections());
			} else {
				g.drawString("Not drawing creatures", 160, 50);
			}
			
			this.drawGraph(g);
		}
		
		private void drawNeuron(Graphics g2, int layer, int neuron, float value, int[] net, Connection[] connections){
			Graphics2D g = (Graphics2D)g2;
			
			Point p = getCoord(layer,neuron,net);
			
			//Draw connections
			for (int i = 0; i < connections.length; ++i){
				Point p2 = getCoord(layer+1,i,net);
				float v = (float) (connections[i].weight * value);
				v = (float) ((Math.tanh(v) + 1) / 2.0f);
				g.setColor(new Color(v,v,v));
				g.drawLine(p.x, p.y, p2.x, p2.y);
			}
			
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
			int layerX = (((frame.getWidth() - (frame.getWidth()*2/3)) / (net.length + 1)) * (layer+1)) + (frame.getWidth()*2/3);
			int neuronY = ((frame.getHeight()*1/3) / (net[layer] + (layer==net.length-1?1:2))) * (neuron+1);
			return new Point(layerX, neuronY);
		}
		
		private void drawBody(Body body, Graphics g, boolean fill){
			float xPos = body.getPosition().x - offsetX + (float)((frame.getWidth()*0.5)/scale);
			float yPos = body.getPosition().y - offsetY + (float)((frame.getHeight()*0.5)/scale);
			
			float theta = body.getAngle();
			
			Fixture fixture = body.m_fixtureList;
			for (int f = 0; f < body.m_fixtureCount; ++f){
				//Draw fixture
				switch (fixture.m_shape.m_type){
				case CIRCLE:{
					CircleShape shape = (CircleShape)fixture.m_shape;
					//TODO: draw circles
					break;
				}
				case POLYGON:{
					PolygonShape shape = (PolygonShape)fixture.m_shape;
				
					int[] x = new int[shape.m_count];
					int[] y = new int[shape.m_count];
					for (int v = 0; v < shape.m_count; ++v){
						float xp = (float) (Math.cos(theta) * shape.m_vertices[v].x - Math.sin(theta) * shape.m_vertices[v].y);
						float yp = (float) (Math.sin(theta) * shape.m_vertices[v].x + Math.cos(theta) * shape.m_vertices[v].y);
						
						x[v] = (int)((xp + xPos) * scale);
						y[v] = (int)((yp + yPos) * scale);
					}
					if (fill)
						g.fillPolygon(x, y, shape.m_count);
					else
						g.drawPolygon(x, y, shape.m_count);
					break;
				}
				default:{
					System.out.println("Couldn't render");
				}
				}
				//Next fixture
				fixture = fixture.getNext();
			}
		}
		
		private void drawGraph(Graphics g){
			int graphHeight = 80;
			
			g.setColor(new Color(0,0,0,0.2f));
			g.fillRect(0, frame.getHeight() - 120, frame.getWidth(), frame.getHeight());
			
			g.setColor(Color.white);
			g.drawLine(0, frame.getHeight() - 120, frame.getWidth(), frame.getHeight() - 120);
			
			int points = Math.min(bestFitness.size(), frame.getWidth() / 2);
			
			int[] xCoords = new int[points];
			int[] bestCoords = new int[points];
			int[] averageCoords = new int[points];
			
			for (int p = 0; p < points; p++){
				int i = Math.round((p / (float)points) * bestFitness.size());
				//System.out.println(p + " " + i);
				xCoords[p] = (int)((p/(float)(points - 1)) * frame.getWidth());
				bestCoords[p] = (int)(frame.getHeight() - 20 - (bestFitness.get(i) / graphMax) * graphHeight);
				averageCoords[p] = (int)(frame.getHeight() - 20 - (averageFitness.get(i) / graphMax) * graphHeight);
			}
			
			g.setColor(Color.green);
			g.drawPolyline(xCoords, bestCoords, points);
			
			g.setColor(Color.white);
			g.drawPolyline(xCoords, averageCoords, points);
		}
	}
	
	public class KA extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent event) {
			switch (event.getKeyCode()){
			case KeyEvent.VK_RIGHT:{
				if (!follow)
					offsetX += 20.0 / scale;
				break;
			}
			case KeyEvent.VK_LEFT:{
				if (!follow)
					offsetX -= 20.0 / scale;
				break;
			}
			case KeyEvent.VK_UP:{
				if (!follow)
					offsetY -= 20.0 / scale;
				break;
			}
			case KeyEvent.VK_DOWN:{
				if (!follow)
					offsetY += 20.0 / scale;
				break;
			}
			case KeyEvent.VK_SPACE:{
				offsetX = (float)(frame.getWidth()*0.5)/100f;
				offsetY = (float)(frame.getHeight()*0.5)/100f;
				break;
			}
			case KeyEvent.VK_0:{
				scale = Math.min(scale+10,180);
				break;
			}
			case KeyEvent.VK_9:{
				scale = Math.max(scale-10, 20);
				break;
			}
			case KeyEvent.VK_F:{
				follow = !follow;
				break;
			}
			case KeyEvent.VK_8:{
				maxSimulationSpeed = !maxSimulationSpeed;
				break;
			}
			case KeyEvent.VK_7:{
				timeScale = Math.min(timeScale*2, 16);
				break;
			}
			case KeyEvent.VK_6:{
				timeScale = Math.max(timeScale/2, 1);
				break;
			}
			case KeyEvent.VK_5:{
				timeScale = 1;
				maxSimulationSpeed = false;
				break;
			}
			case KeyEvent.VK_S:{
				//TODO:Save data to file
			}
			case KeyEvent.VK_D:{
				//TODO:Log best creature data
				/*
				
				NeuralNet best = nets[(int)trackers[0][0]];
				double[] data = best.getData();
				System.out.println("===");
				System.out.print(data[0])
				for (int i = 1; i < data.length; ++i)
					System.out.print(", " + data[i]);
				System.out.println("\n===");
				
				 */
			}
			case KeyEvent.VK_U:{
				drawCreatures = !drawCreatures;
				break;
			}
			case KeyEvent.VK_1:{
				GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
				if (gd.getFullScreenWindow() == null){
					gd.setFullScreenWindow(frame);
				} else {
					gd.setFullScreenWindow(null);
				}
			}
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			super.keyReleased(arg0);
		}
		
	}
	
	public void start(){
		//FRAME INIT
		DrawPanel panel = new DrawPanel();
		KA keyAdapter = new KA();
		
		frame = new JFrame("Genetic evolution of neural networks");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 800);
		frame.add(panel);
		frame.addKeyListener(keyAdapter);
		frame.setVisible(true);
		frame.setResizable(true);
		
		//INIT
		nets = new NeuralNet[population];
		trackers = new GeneticTrainer.Tracker[population];
		creatures = (T[]) new AbstractCreature[population];
		bestFitness = new ArrayList<Double>();
		averageFitness = new ArrayList<Double>();
		
		Vec2 gravity = new Vec2(0,8);
		world = new World(gravity);
		world.setAllowSleep(false);
		
		
		//CAMERA
		offsetX = 0;//(float)(frame.getWidth()*0.5)/100f;
		offsetY = 0;//(float)(frame.getHeight()*0.5)/100f;
		
		creatureT.createWorld(world);
		
		//INIT EACH
		float x = 0;//(frame.getWidth() / scale) * 0.5f;
		float y = -5;
		
		for (int i = 0; i < population; ++i){
			try {
				nets[i] = new NeuralNet(creatureT.getNet());
			} catch (Exception e){
				e.printStackTrace();
				System.exit(-1);
			}
			trackers[i] = new Tracker();
			trackers[i].index = i;
			try {
				creatures[i] = creature.getDeclaredConstructor(World.class, float.class, float.class).newInstance(world,x,y);
			} catch (Exception e){
				e.printStackTrace();
				System.exit(-1);
			}
		}

		//MAIN LOOP
		delta = 0;
		startTime = System.nanoTime();
		lastTime = startTime;
		time = 0;
		thisFrame = 0;
		lastDraw = lastTime;
		
		while (frame.isVisible()){
			delta = (System.nanoTime() - lastTime) / 1000000.0f;
			if (maxSimulationSpeed || delta > (fps/timeScale) * 1000){
				thisFrame++;
				
				for (int i = 0; i < population; ++i){
					double[] inputData = creatures[i].getInputs();
					nets[i].feedForward(inputData);
					
					double[] results = nets[i].getResults();
					creatures[i].feedResults(results);
					
					creatures[i].updateTracker(trackers[i]);
				}
				
				time += delta;
				lastTime = System.nanoTime();
				
				world.step(fps, 20, 10);
				
				if ((System.nanoTime() - lastDraw) / 1000000.0f > fps){
					panel.repaint();
					lastDraw = System.nanoTime();
				}
				
				if (thisFrame >= frames){
					//Pick new generation
					Arrays.sort(trackers, new Comparator(){
						public int compare(final Object o, final Object o2) {
							final Tracker t1 = (Tracker)o;
							final Tracker t2 = (Tracker)o2;
							return Double.compare(creatureT.fitness(t2), creatureT.fitness(t1));
						}
					});
					
					NeuralNet[] nextGen = new NeuralNet[population];
					GeneticTrainer.Tracker[] nextTrackers = new GeneticTrainer.Tracker[population];
					
					for (int i = 0; i < population; ++i){
						if (i < elites){
							nextGen[i] = new NeuralNet(nets[trackers[i].index].getNetData(),nets[trackers[i].index].getData());
							Tracker t = new Tracker();
							t.index = i;
							nextTrackers[i] = t;
						} else {
							int index1 = weightedRandom((int)(population * (1 - cutoff)), -1);
							int index2 = weightedRandom((int)(population * (1 - cutoff)), index1);
							
							System.out.println(index1 + " " + index2);
							
							NeuralNet net1 = nets[trackers[index1].index];
							NeuralNet net2 = nets[trackers[index2].index];
							nextGen[i] = net1.childOf(net2, mutationRate);
							
							Tracker t = new Tracker();
							t.index = i;
							nextTrackers[i] = t;
						}
						
						creatures[i].destroy(world);
						try {
							creatures[i] = creature.getDeclaredConstructor(World.class, float.class, float.class).newInstance(world,x,y);
						} catch (Exception e){
							e.printStackTrace();
							System.exit(-1);
						}
					}
					
					
					
					float sum = 0;
					for (int i = 0; i < trackers.length; ++i)
						sum += creatureT.fitness(trackers[i]);
					double average = sum / trackers.length;
					
					System.out.println("Best: " + creatureT.fitness(trackers[0]));
					System.out.println("Average: " + average);
					
					bestFitness.add(creatureT.fitness(trackers[0]));
					graphMax = Math.max(graphMax, creatureT.fitness(trackers[0]));
					
					averageFitness.add(average);
					graphMax = Math.max(graphMax, average);
					
					nets = nextGen.clone();
					trackers = nextTrackers.clone();
					
					generation++;
					thisFrame = 0;
					time = 0;
				}
			}
		}
	}
	
	public GeneticTrainer(Class<T> creature) throws InstantiationException, IllegalAccessException{
		this.creature = creature;
		
		try {
			this.creature.getConstructor();
			this.creature.getConstructor(World.class, float.class, float.class);
		} catch (NoSuchMethodException e){
			System.out.println("Class: <" + creature.getCanonicalName() + "> must have the following constructors:");
			System.out.println("public " + creature.getSimpleName() + "(){...}");
			System.out.println("public " + creature.getSimpleName() + "(World w, float x, float y){...}");
			System.exit(-1);
		}
		
		this.creatureT = creature.newInstance();
	}
	
	public int weightedRandom(int num, int not){
		float total = 0.0f;
		for (int i = 1; i <= num; ++i)
			total += 1.0f / i;
		
		float rand = (float)(Math.random() * total);

		for (int i = 0; i < num; ++i){
			if (i==not)
				rand -= 1.0f / not;
			else {
				rand -= 1f/(i+1);
				if (rand < 0)
					return i;
			}
		}
		return num;
	}
	
	public static String truncate(double num, int decimalPlaces){
		long factor = (long) Math.pow(10, decimalPlaces);
	    num = num * factor;
	    long tmp = (long) num;
	    return ((double) tmp / factor) + "";
	}
}
