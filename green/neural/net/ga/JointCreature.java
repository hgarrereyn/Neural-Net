package green.neural.net.ga;

import green.neural.net.ga.GeneticTrainer.Tracker;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class JointCreature extends AbstractCreature {

	public Body top;
	public Body bottom;
	public RevoluteJoint joint;
	
	public JointCreature(){}
	
	public JointCreature(World world, float x, float y){
		BodyDef body = new BodyDef();
		body.type = BodyType.DYNAMIC;
		body.position = new Vec2(x,y);
		body.fixedRotation = false;
		body.angle = (float)Math.toRadians(0);
		//---
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.4f, 0.6f);
		//---
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 1.0f;
		fixture.friction = 0.3f;
		fixture.restitution = 0.4f;
		fixture.filter.groupIndex = -1;
		//---
		top = world.createBody(body);
		top.createFixture(fixture);
		
		bottom = world.createBody(body);
		bottom.createFixture(fixture);
		
		
		
		RevoluteJointDef jointDef = new RevoluteJointDef();
		jointDef.bodyA = top;
		jointDef.bodyB = bottom;
		jointDef.collideConnected = false;
		jointDef.localAnchorA = new Vec2(0,0.5f);
		jointDef.localAnchorB = new Vec2(0,-0.5f);
		jointDef.enableLimit = true;
		jointDef.lowerAngle = (float)Math.toRadians(-30);
		jointDef.upperAngle = (float)Math.toRadians(30);
		jointDef.enableMotor = true;
		jointDef.motorSpeed = 0.0f;
		jointDef.maxMotorTorque = 100.0f;
		joint = (RevoluteJoint)world.createJoint(jointDef);
	}
	
	@Override
	public void destroy(World world) {
		world.destroyBody(top);
		world.destroyBody(bottom);
		world.destroyJoint(joint);
	}

	@Override
	public double[] getInputs() {
		float topAngle = (float)Math.toDegrees(this.top.getAngle());
		topAngle+=180;
		while (topAngle >= 360) topAngle -= 360;
		while (topAngle < 0) topAngle += 360;
		topAngle -= 180;
		topAngle /= 180;
		
		float bottomAngle = (float)Math.toDegrees(this.bottom.getAngle());
		bottomAngle+=180;
		while (bottomAngle >= 360) bottomAngle -= 360;
		while (bottomAngle < 0) bottomAngle += 360;
		bottomAngle -= 180;
		bottomAngle /= 180;
		
		float jointAngle = (float)((Math.toDegrees(this.joint.getJointAngle())) / 30);
		
		float topTouch = this.top.getContactList()==null?-1:1;
		float bottomTouch = this.bottom.getContactList()==null?-1:1;
		
		double[] inputs = new double[]{topAngle, bottomAngle, jointAngle, topTouch, bottomTouch};
		
		return inputs;
	}

	@Override
	public void feedResults(double[] results) {
		this.joint.setMotorSpeed((float)(results[0] * 10));
	}

	@Override
	public void updateTracker(Tracker tracker) {
		tracker.data[0] = top.getPosition().x;
	}

	@Override
	public Vec2 getPosition() {
		return top.getPosition();
	}

	@Override
	public Body[] getBodies() {
		return new Body[]{top, bottom};
	}

	@Override
	public double fitness(Tracker tracker) {
		return tracker.data[0];
	}

	@Override
	public int[] getNet() {
		return new int[]{5,8,1};
	}

}
