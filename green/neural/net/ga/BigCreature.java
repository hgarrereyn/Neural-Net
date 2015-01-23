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

public class BigCreature extends AbstractCreature {

	Body square;
	Body leftLeg;
	Body rightLeg;
	
	RevoluteJoint leftJoint;
	RevoluteJoint rightJoint;
	
	public BigCreature(){}
	
	public BigCreature(World world, float x, float y){
		BodyDef boxDef = new BodyDef();
		boxDef.type = BodyType.DYNAMIC;
		boxDef.position = new Vec2(x,y);
		boxDef.fixedRotation = false;
		boxDef.angle = (float)Math.toRadians(0);
		//---
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(1, 1);
		//---
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.shape = boxShape;
		boxFixture.density = 1.0f;
		boxFixture.friction = 0.3f;
		boxFixture.restitution = 0.4f;
		boxFixture.filter.groupIndex = -1;
		//--
		square = world.createBody(boxDef);
		square.createFixture(boxFixture);
		
		BodyDef legDef = new BodyDef();
		legDef.type = BodyType.DYNAMIC;
		legDef.position = new Vec2(x,y);
		legDef.fixedRotation = false;
		legDef.angle = (float)Math.toRadians(0);
		//---
		PolygonShape legShape = new PolygonShape();
		legShape.setAsBox(0.1f, 0.2f);
		//---
		FixtureDef legFixture = new FixtureDef();
		legFixture.shape = legShape;
		legFixture.density = 1.0f;
		legFixture.friction = 0.3f;
		legFixture.restitution = 0.4f;
		legFixture.filter.groupIndex = -1;
		//--
		leftLeg = world.createBody(legDef);
		leftLeg.createFixture(legFixture);
		
		rightLeg = world.createBody(legDef);
		rightLeg.createFixture(legFixture);
		
		RevoluteJointDef leftJointDef = new RevoluteJointDef();
		leftJointDef.bodyA = square;
		leftJointDef.bodyB = leftLeg;
		leftJointDef.collideConnected = false;
		leftJointDef.localAnchorA = new Vec2(-0.95f,0.95f);
		leftJointDef.localAnchorB = new Vec2(0,-0.09f);
		leftJointDef.enableLimit = true;
		leftJointDef.lowerAngle = (float)Math.toRadians(-60);
		leftJointDef.upperAngle = (float)Math.toRadians(60);
		leftJointDef.enableMotor = true;
		leftJointDef.motorSpeed = 0.0f;
		leftJointDef.maxMotorTorque = 100.0f;
		leftJoint = (RevoluteJoint) world.createJoint(leftJointDef);
		
		RevoluteJointDef rightJointDef = new RevoluteJointDef();
		rightJointDef.bodyA = square;
		rightJointDef.bodyB = rightLeg;
		rightJointDef.collideConnected = false;
		rightJointDef.localAnchorA = new Vec2(0.95f,0.95f);
		rightJointDef.localAnchorB = new Vec2(0,-0.09f);
		rightJointDef.enableLimit = true;
		rightJointDef.lowerAngle = (float)Math.toRadians(-60);
		rightJointDef.upperAngle = (float)Math.toRadians(60);
		rightJointDef.enableMotor = true;
		rightJointDef.motorSpeed = 0.0f;
		rightJointDef.maxMotorTorque = 100.0f;
		rightJoint = (RevoluteJoint) world.createJoint(rightJointDef);
	}
	
	@Override
	public void destroy(World world) {
		world.destroyBody(square);
		world.destroyBody(leftLeg);
		world.destroyBody(rightLeg);
		
		world.destroyJoint(leftJoint);
		world.destroyJoint(rightJoint);
	}

	@Override
	public double[] getInputs() {
		float bodyAngle = (float)Math.toDegrees(this.square.getAngle());
		while (bodyAngle >= 360) bodyAngle -= 360;
		while (bodyAngle < 0) bodyAngle += 360;
		bodyAngle -= 180;
		bodyAngle /= 180;
		
		float leftAngle = (float)((Math.toDegrees(this.leftJoint.getJointAngle())) / 60);
		float rightAngle = (float)((Math.toDegrees(this.rightJoint.getJointAngle())) / 60);
		
		double[] inputs = new double[]{bodyAngle, leftAngle, rightAngle};
		
		return inputs;
	}

	@Override
	public void feedResults(double[] results) {
		this.leftJoint.setMotorSpeed((float)(results[0]*10));
		this.rightJoint.setMotorSpeed((float)(results[1]*10));
	}

	@Override
	public void updateTracker(Tracker tracker) {
		tracker.data[0] = this.square.getPosition().x;
	}

	@Override
	public Vec2 getPosition() {
		return square.getPosition();
	}

	@Override
	public Body[] getBodies() {
		return new Body[]{square, leftLeg, rightLeg};
	}
	
	public double fitness(Tracker tracker) {
		return tracker.data[0];
	}

	public int[] getNet(){
		return new int[]{3,8,2};
	}
}
