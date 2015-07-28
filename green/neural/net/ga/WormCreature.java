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

public class WormCreature extends AbstractCreature {

	public Body cCoreBody;
	public Body cLeftSeg1;
	public Body cLeftSeg2;
	public Body cLeftSeg3;
	public Body cRightSeg1;
	public Body cRightSeg2;
	public Body cRightSeg3;
	
	public RevoluteJoint cLeftJoint1;
	public RevoluteJoint cLeftJoint2;
	public RevoluteJoint cLeftJoint3;
	public RevoluteJoint cRightJoint1;
	public RevoluteJoint cRightJoint2;
	public RevoluteJoint cRightJoint3;
	
	public WormCreature(){}
	
	public WormCreature(World world, float x, float y){
		//Core
		BodyDef cCoreDef = new BodyDef();
		cCoreDef.type = BodyType.DYNAMIC;
		cCoreDef.position = new Vec2(x,y);
		cCoreDef.fixedRotation = false;
		cCoreDef.angle = (float)Math.toRadians(0);
		//---
		PolygonShape cCore = new PolygonShape();
		cCore.setAsBox(0.8f, 0.3f);
		//---
		FixtureDef cCoreFixture = new FixtureDef();
		cCoreFixture.shape = cCore;
		cCoreFixture.density = 1.0f;
		cCoreFixture.friction = 0.3f;
		cCoreFixture.restitution = 0.4f;
		cCoreFixture.filter.groupIndex = -1;
		//---
		cCoreBody = world.createBody(cCoreDef);
		cCoreBody.createFixture(cCoreFixture);
		
		cLeftSeg1 = world.createBody(cCoreDef);
		cLeftSeg1.createFixture(cCoreFixture);
		
		cLeftSeg2 = world.createBody(cCoreDef);
		cLeftSeg2.createFixture(cCoreFixture);
		
		cLeftSeg3 = world.createBody(cCoreDef);
		cLeftSeg3.createFixture(cCoreFixture);
		
		cRightSeg1 = world.createBody(cCoreDef);
		cRightSeg1.createFixture(cCoreFixture);
		
		cRightSeg2 = world.createBody(cCoreDef);
		cRightSeg2.createFixture(cCoreFixture);
		
		cRightSeg3 = world.createBody(cCoreDef);
		cRightSeg3.createFixture(cCoreFixture);
		
		//Left 1
		RevoluteJointDef cLeftJoint1Ref = new RevoluteJointDef();
		cLeftJoint1Ref.bodyA = cCoreBody;
		cLeftJoint1Ref.bodyB = cLeftSeg1;
		cLeftJoint1Ref.collideConnected = false;
		cLeftJoint1Ref.localAnchorA = new Vec2(-0.7f,0);
		cLeftJoint1Ref.localAnchorB = new Vec2(0.7f,0);
		cLeftJoint1Ref.enableLimit = true;
		cLeftJoint1Ref.lowerAngle = (float)Math.toRadians(-30);
		cLeftJoint1Ref.upperAngle = (float)Math.toRadians(30);
		cLeftJoint1Ref.enableMotor = true;
		cLeftJoint1Ref.motorSpeed = 0.0f;
		cLeftJoint1Ref.maxMotorTorque = 100.0f;
		cLeftJoint1 = (RevoluteJoint)world.createJoint(cLeftJoint1Ref);
		
		//Left 2
		RevoluteJointDef cLeftJoint2Ref = new RevoluteJointDef();
		cLeftJoint2Ref.bodyA = cLeftSeg1;
		cLeftJoint2Ref.bodyB = cLeftSeg2;
		cLeftJoint2Ref.collideConnected = false;
		cLeftJoint2Ref.localAnchorA = new Vec2(-0.7f,0);
		cLeftJoint2Ref.localAnchorB = new Vec2(0.7f,0);
		cLeftJoint2Ref.enableLimit = true;
		cLeftJoint2Ref.lowerAngle = (float)Math.toRadians(-30);
		cLeftJoint2Ref.upperAngle = (float)Math.toRadians(30);
		cLeftJoint2Ref.enableMotor = true;
		cLeftJoint2Ref.motorSpeed = 0.0f;
		cLeftJoint2Ref.maxMotorTorque = 100.0f;
		cLeftJoint2 = (RevoluteJoint)world.createJoint(cLeftJoint2Ref);
		
		//Left 3
		RevoluteJointDef cLeftJoint3Ref = new RevoluteJointDef();
		cLeftJoint3Ref.bodyA = cLeftSeg2;
		cLeftJoint3Ref.bodyB = cLeftSeg3;
		cLeftJoint3Ref.collideConnected = false;
		cLeftJoint3Ref.localAnchorA = new Vec2(-0.7f,0);
		cLeftJoint3Ref.localAnchorB = new Vec2(0.7f,0);
		cLeftJoint3Ref.enableLimit = true;
		cLeftJoint3Ref.lowerAngle = (float)Math.toRadians(-30);
		cLeftJoint3Ref.upperAngle = (float)Math.toRadians(30);
		cLeftJoint3Ref.enableMotor = true;
		cLeftJoint3Ref.motorSpeed = 0.0f;
		cLeftJoint3Ref.maxMotorTorque = 100.0f;
		cLeftJoint3 = (RevoluteJoint)world.createJoint(cLeftJoint3Ref);
		
		//Right 1
		RevoluteJointDef cRightJoint1Ref = new RevoluteJointDef();
		cRightJoint1Ref.bodyA = cCoreBody;
		cRightJoint1Ref.bodyB = cRightSeg1;
		cRightJoint1Ref.collideConnected = false;
		cRightJoint1Ref.localAnchorA = new Vec2(0.7f,0);
		cRightJoint1Ref.localAnchorB = new Vec2(-0.7f,0);
		cRightJoint1Ref.enableLimit = true;
		cRightJoint1Ref.lowerAngle = (float)Math.toRadians(-30);
		cRightJoint1Ref.upperAngle = (float)Math.toRadians(30);
		cRightJoint1Ref.enableMotor = true;
		cRightJoint1Ref.motorSpeed = 0.0f;
		cRightJoint1Ref.maxMotorTorque = 100.0f;
		cRightJoint1 = (RevoluteJoint)world.createJoint(cRightJoint1Ref);
		
		//Right 2
		RevoluteJointDef cRightJoint2Ref = new RevoluteJointDef();
		cRightJoint2Ref.bodyA = cRightSeg1;
		cRightJoint2Ref.bodyB = cRightSeg2;
		cRightJoint2Ref.collideConnected = false;
		cRightJoint2Ref.localAnchorA = new Vec2(0.7f,0);
		cRightJoint2Ref.localAnchorB = new Vec2(-0.7f,0);
		cRightJoint2Ref.enableLimit = true;
		cRightJoint2Ref.lowerAngle = (float)Math.toRadians(-30);
		cRightJoint2Ref.upperAngle = (float)Math.toRadians(30);
		cRightJoint2Ref.enableMotor = true;
		cRightJoint2Ref.motorSpeed = 0.0f;
		cRightJoint2Ref.maxMotorTorque = 100.0f;
		cRightJoint2 = (RevoluteJoint)world.createJoint(cRightJoint2Ref);
		
		//Right 3
		RevoluteJointDef cRightJoint3Ref = new RevoluteJointDef();
		cRightJoint3Ref.bodyA = cRightSeg2;
		cRightJoint3Ref.bodyB = cRightSeg3;
		cRightJoint3Ref.collideConnected = false;
		cRightJoint3Ref.localAnchorA = new Vec2(0.7f,0);
		cRightJoint3Ref.localAnchorB = new Vec2(-0.7f,0);
		cRightJoint3Ref.enableLimit = true;
		cRightJoint3Ref.lowerAngle = (float)Math.toRadians(-30);
		cRightJoint3Ref.upperAngle = (float)Math.toRadians(30);
		cRightJoint3Ref.enableMotor = true;
		cRightJoint3Ref.motorSpeed = 0.0f;
		cRightJoint3Ref.maxMotorTorque = 100.0f;
		cRightJoint3 = (RevoluteJoint)world.createJoint(cRightJoint3Ref);
	}
	
	@Override
	public void destroy(World world){
		world.destroyBody(cCoreBody);
		world.destroyBody(cLeftSeg1);
		world.destroyBody(cLeftSeg2);
		world.destroyBody(cLeftSeg3);
		world.destroyBody(cRightSeg1);
		world.destroyBody(cRightSeg2);
		world.destroyBody(cRightSeg3);
		
		world.destroyJoint(cLeftJoint1);
		world.destroyJoint(cLeftJoint2);
		world.destroyJoint(cLeftJoint3);
		world.destroyJoint(cRightJoint1);
		world.destroyJoint(cRightJoint2);
		world.destroyJoint(cRightJoint3);
	}

	@Override
	public double[] getInputs() {
		float bodyAngle = (float)Math.toDegrees(this.cCoreBody.getAngle());
		while (bodyAngle >= 360) bodyAngle -= 360;
		while (bodyAngle < 0) bodyAngle += 360;
		bodyAngle -= 180;
		bodyAngle /= 180;
		
		
		float leftAngle = (float)((Math.toDegrees(this.cLeftJoint1.getJointAngle())) / 30);
		float leftAngle2 = (float)((Math.toDegrees(this.cLeftJoint2.getJointAngle())) / 30);
		float leftAngle3 = (float)((Math.toDegrees(this.cLeftJoint3.getJointAngle())) / 30);
		float rightAngle = (float)((Math.toDegrees(this.cRightJoint1.getJointAngle())) / 30);
		float rightAngle2 = (float)((Math.toDegrees(this.cRightJoint2.getJointAngle())) / 30);
		float rightAngle3 = (float)((Math.toDegrees(this.cRightJoint3.getJointAngle())) / 30);
		
		
		float leftTouch = this.cLeftSeg2.getContactList()==null?-1:1;
		float leftTouch2 = this.cLeftSeg1.getContactList()==null?-1:1;
		float leftTouch3 = this.cLeftSeg1.getContactList()==null?-1:1;
		float rightTouch = this.cRightSeg1.getContactList()==null?-1:1;
		float rightTouch2 = this.cRightSeg2.getContactList()==null?-1:1;
		float rightTouch3 = this.cRightSeg2.getContactList()==null?-1:1;
		
		double[] inputs = new double[]{bodyAngle,leftAngle,leftAngle2,leftAngle3,rightAngle,rightAngle2,rightAngle3, leftTouch, leftTouch2, leftTouch3, rightTouch, rightTouch2, rightTouch3};
		
		return inputs;
	}

	@Override
	public void feedResults(double[] results) {
		this.cLeftJoint3.setMotorSpeed((float)(results[0] * 10));
		this.cLeftJoint2.setMotorSpeed((float)(results[1] * 10));
		this.cLeftJoint1.setMotorSpeed((float)(results[2] * 10));
		this.cRightJoint1.setMotorSpeed((float)(results[3] * 10));
		this.cRightJoint2.setMotorSpeed((float)(results[4] * 10));
		this.cRightJoint3.setMotorSpeed((float)(results[5] * 10));
	}

	@Override
	public void updateTracker(Tracker tracker) {
		tracker.data[0] = cCoreBody.getPosition().x;
	}

	@Override
	public Vec2 getPosition() {
		return cCoreBody.getPosition();
	}

	@Override
	public Body[] getBodies() {
		return new Body[]{cLeftSeg3,cLeftSeg2,cLeftSeg1,cCoreBody,cRightSeg1,cRightSeg2,cRightSeg3};
	}

	@Override
	public double fitness(Tracker tracker) {
		return tracker.data[0];
	}

	@Override
	public int[] getNet() {
		// TODO Auto-generated method stub
		return new int[]{13,20,6};
	}

}
