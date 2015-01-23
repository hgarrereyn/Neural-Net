package green.neural.net.creatures;

import green.neural.net.train.GeneticTrainer.Tracker;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class BipedCreature extends AbstractCreature {

	Body top;
	Body middle;
	Body leg1;
	Body leg2;
	Body foot1;
	Body foot2;
	
	RevoluteJoint midJoint;
	RevoluteJoint hip1;
	RevoluteJoint hip2;
	RevoluteJoint knee1;
	RevoluteJoint knee2;
	
	public BipedCreature(){}
	
	public BipedCreature(World world, float x, float y){
		BodyDef boxDef = new BodyDef();
		boxDef.type = BodyType.DYNAMIC;
		boxDef.position = new Vec2(x,y);
		boxDef.fixedRotation = false;
		boxDef.angle = (float)Math.toRadians(0);
		//---
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(0.4f, 0.6f);
		//---
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.shape = boxShape;
		boxFixture.density = 1.0f;
		boxFixture.friction = 0.3f;
		boxFixture.restitution = 0.4f;
		boxFixture.filter.groupIndex = -1;
		//---
		
		top = world.createBody(boxDef);
		top.createFixture(boxFixture);
		
		middle = world.createBody(boxDef);
		middle.createFixture(boxFixture);
		
		BodyDef legDef = new BodyDef();
		legDef.type = BodyType.DYNAMIC;
		legDef.fixedRotation = false;
		legDef.angle = (float)Math.toRadians(0);
		//---
		PolygonShape legShape = new PolygonShape();
		legShape.setAsBox(0.3f, 0.6f);
		//---
		FixtureDef legFixture = new FixtureDef();
		legFixture.shape = legShape;
		legFixture.density = 1.0f;
		legFixture.friction = 0.3f;
		legFixture.restitution = 0.4f;
		legFixture.filter.groupIndex = -1;
		//---
		
		leg1 = world.createBody(legDef);
		leg1.createFixture(legFixture);
		
		leg2 = world.createBody(legDef);
		leg2.createFixture(legFixture);
		
		foot1 = world.createBody(legDef);
		foot1.createFixture(legFixture);
		
		foot2 = world.createBody(legDef);
		foot2.createFixture(legFixture);
		
		RevoluteJointDef midJointDef = new RevoluteJointDef();
		midJointDef.bodyA = top;
		midJointDef.bodyB = middle;
		midJointDef.collideConnected = false;
		midJointDef.localAnchorA = new Vec2(0,-0.5f);
		midJointDef.localAnchorB = new Vec2(0,0.5f);
		midJointDef.enableLimit = true;
		midJointDef.lowerAngle = (float)Math.toRadians(-20);
		midJointDef.upperAngle = (float)Math.toRadians(20);
		midJointDef.enableMotor = true;
		midJointDef.motorSpeed = 0.0f;
		midJointDef.maxMotorTorque = 100.0f;
		midJoint = (RevoluteJoint) world.createJoint(midJointDef);
		
		RevoluteJointDef hip1Def = new RevoluteJointDef();
		hip1Def.bodyA = middle;
		hip1Def.bodyB = leg1;
		hip1Def.collideConnected = false;
		hip1Def.localAnchorA = new Vec2(0,-0.5f);
		hip1Def.localAnchorB = new Vec2(0,0.5f);
		hip1Def.enableLimit = true;
		hip1Def.lowerAngle = (float)Math.toRadians(-100);
		hip1Def.upperAngle = (float)Math.toRadians(10);
		hip1Def.enableMotor = true;
		hip1Def.motorSpeed = 0.0f;
		hip1Def.maxMotorTorque = 100.0f;
		hip1 = (RevoluteJoint) world.createJoint(hip1Def);
		
		RevoluteJointDef hip2Def = new RevoluteJointDef();
		hip2Def.bodyA = middle;
		hip2Def.bodyB = leg2;
		hip2Def.collideConnected = false;
		hip2Def.localAnchorA = new Vec2(0,-0.5f);
		hip2Def.localAnchorB = new Vec2(0,0.5f);
		hip2Def.enableLimit = true;
		hip2Def.lowerAngle = (float)Math.toRadians(-100);
		hip2Def.upperAngle = (float)Math.toRadians(10);
		hip2Def.enableMotor = true;
		hip2Def.motorSpeed = 0.0f;
		hip2Def.maxMotorTorque = 100.0f;
		hip2 = (RevoluteJoint) world.createJoint(hip2Def);
		
		RevoluteJointDef knee1Def = new RevoluteJointDef();
		knee1Def.bodyA = leg1;
		knee1Def.bodyB = foot1;
		knee1Def.collideConnected = false;
		knee1Def.localAnchorA = new Vec2(0,-0.5f);
		knee1Def.localAnchorB = new Vec2(0,0.5f);
		knee1Def.enableLimit = true;
		knee1Def.lowerAngle = (float)Math.toRadians(0);
		knee1Def.upperAngle = (float)Math.toRadians(100);
		knee1Def.enableMotor = true;
		knee1Def.motorSpeed = 0.0f;
		knee1Def.maxMotorTorque = 100.0f;
		knee1 = (RevoluteJoint) world.createJoint(knee1Def);
		
		RevoluteJointDef knee2Def = new RevoluteJointDef();
		knee2Def.bodyA = leg2;
		knee2Def.bodyB = foot2;
		knee2Def.collideConnected = false;
		knee2Def.localAnchorA = new Vec2(0,-0.5f);
		knee2Def.localAnchorB = new Vec2(0,0.5f);
		knee2Def.enableLimit = true;
		knee2Def.lowerAngle = (float)Math.toRadians(0);
		knee2Def.upperAngle = (float)Math.toRadians(100);
		knee2Def.enableMotor = true;
		knee2Def.motorSpeed = 0.0f;
		knee2Def.maxMotorTorque = 100.0f;
		knee2 = (RevoluteJoint) world.createJoint(knee2Def);
	}
	
	@Override
	public void destroy(World world){
		world.destroyBody(top);
		world.destroyBody(middle);
		world.destroyBody(leg1);
		world.destroyBody(leg2);
		world.destroyBody(foot1);
		world.destroyBody(foot2);
		
		world.destroyJoint(midJoint);
		world.destroyJoint(hip1);
		world.destroyJoint(hip2);
		world.destroyJoint(knee1);
		world.destroyJoint(knee2);
	}
	
	@Override
	public double[] getInputs() {
		float bodyAngle = (float)Math.toDegrees(this.middle.getAngle());
		while (bodyAngle >= 360) bodyAngle -= 360;
		while (bodyAngle < 0) bodyAngle += 360;
		bodyAngle -= 180;
		bodyAngle /= 180;
		
		float angle0 = (float)((Math.toDegrees(this.midJoint.getJointAngle())) / 10);
		float angle1 = (float)(((Math.toDegrees(this.hip1.getJointAngle())) + 10) / 55) - 1;
		float angle2 = (float)(((Math.toDegrees(this.hip2.getJointAngle())) + 10) / 55) - 1;
		float angle3 = (float)(((Math.toDegrees(this.knee1.getJointAngle())) + 100) / 50) - 1;
		float angle4 = (float)(((Math.toDegrees(this.knee2.getJointAngle())) + 100) / 50) - 1;
		
		float touch1 = (this.foot1.getContactList() == null)?-1:1;
		float touch2 = (this.foot2.getContactList() == null)?-1:1;
		
		double[] inputs = new double[]{bodyAngle, angle0, angle1, angle2, angle3, angle4, touch1, touch2};
		
		return inputs;
	}

	@Override
	public void feedResults(double[] results) {
		this.midJoint.setMotorSpeed((float)(results[0]*10));
		this.hip1.setMotorSpeed((float)(results[1]*5));
		this.hip2.setMotorSpeed((float)(results[2]*5));
		this.knee1.setMotorSpeed((float)(results[3]*3));
		this.knee2.setMotorSpeed((float)(results[4]*3));
	}

	@Override
	public void updateTracker(Tracker tracker) {
		//tracker.data[0] = this.middle.getPosition().x;
		tracker.data[0] += this.top.getPosition().y * -1;
		tracker.data[1] = Math.max(tracker.data[1], this.top.getPosition().y * -1);
	}
	
	@Override
	public Vec2 getPosition() {
		return middle.getPosition();
	}
	
	@Override
	public Body[] getBodies(){
		return new Body[]{top,middle,leg1,leg2,foot1,foot2};
	}

	public double fitness(Tracker tracker) {
		return tracker.data[0];
	}

	public int[] getNet(){
		return new int[]{8,8,5};
	}

}
