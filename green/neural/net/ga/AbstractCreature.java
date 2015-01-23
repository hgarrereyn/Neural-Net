package green.neural.net.ga;


import green.neural.net.ga.GeneticTrainer.Tracker;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public abstract class AbstractCreature {
	public abstract void destroy(World w);
	public abstract double[] getInputs();
	public abstract void feedResults(double[] results);
	public abstract void updateTracker(Tracker tracker);
	public abstract Vec2 getPosition();
	public abstract Body[] getBodies();
	public abstract double fitness(Tracker tracker);
	public abstract int[] getNet();
	
	//optional methods
	public void createWorld(World world){
		BodyDef floorDef = new BodyDef();
		floorDef.type = BodyType.KINEMATIC;
		floorDef.position = new Vec2(0,0);
				
		PolygonShape floorShape = new PolygonShape();
		floorShape.setAsBox(500, 0.1f);
				
		FixtureDef floorFixture = new FixtureDef();
		floorFixture.shape = floorShape;
		floorFixture.density = 100.0f;
		floorFixture.friction = 0.3f;
		floorFixture.filter.groupIndex = 1;
				
		Body floor = world.createBody(floorDef);
		floor.createFixture(floorFixture);
	}
}
