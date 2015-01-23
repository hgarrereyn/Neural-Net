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

public class FourLegCreature extends AbstractCreature {

	Body core;
	Body leftLeg1;
	Body leftFoot1;
	Body leftLeg2;
	Body leftFoot2;
	Body rightLeg1;
	Body rightFoot1;
	Body rightLeg2;
	Body rightFoot2;
	
	RevoluteJoint leftLegJoint1;
	RevoluteJoint leftLegJoint2;
	RevoluteJoint leftFootJoint1;
	RevoluteJoint leftFootJoint2;
	RevoluteJoint rightLegJoint1;
	RevoluteJoint rightLegJoint2;
	RevoluteJoint rightFootJoint1;
	RevoluteJoint rightFootJoint2;
	
	public FourLegCreature(){}
	
	public FourLegCreature(World world, float x, float y){
		BodyDef coreDef = new BodyDef();
		coreDef.type = BodyType.DYNAMIC;
		coreDef.position = new Vec2(x,y);
		coreDef.fixedRotation = false;
		coreDef.angle = (float)Math.toRadians(0);
		//---
		PolygonShape coreShape = new PolygonShape();
		coreShape.setAsBox(1.2f, 0.2f);
		//---
		FixtureDef coreFixture = new FixtureDef();
		coreFixture.shape = coreShape;
		coreFixture.density = 1.0f;
		coreFixture.friction = 0.3f;
		coreFixture.restitution = 0.4f;
		coreFixture.filter.groupIndex = -1;
		//---
		core = world.createBody(coreDef);
		core.createFixture(coreFixture);
		
		
		
		BodyDef legDef = new BodyDef();
		legDef.type = BodyType.DYNAMIC;
		legDef.position = new Vec2(x,y);
		legDef.fixedRotation = false;
		legDef.angle = (float)Math.toRadians(0);
		//---
		PolygonShape legShape = new PolygonShape();
		legShape.setAsBox(0.1f, 0.5f);
		//---
		FixtureDef legFixture = new FixtureDef();
		legFixture.shape = legShape;
		legFixture.density = 1.0f;
		legFixture.friction = 0.3f;
		legFixture.restitution = 0.4f;
		legFixture.filter.groupIndex = -1;
		
		leftLeg1 = world.createBody(legDef);
		leftLeg1.createFixture(legFixture);
		
		leftLeg2 = world.createBody(legDef);
		leftLeg2.createFixture(legFixture);
		
		rightLeg1 = world.createBody(legDef);
		rightLeg1.createFixture(legFixture);
		
		rightLeg2 = world.createBody(legDef);
		rightLeg2.createFixture(legFixture);
		
		
		
		BodyDef footDef = new BodyDef();
		footDef.type = BodyType.DYNAMIC;
		footDef.position = new Vec2(x,y);
		footDef.fixedRotation = false;
		footDef.angle = (float)Math.toRadians(0);
		//---
		PolygonShape footShape = new PolygonShape();
		footShape.setAsBox(0.1f, 0.3f);
		//---
		FixtureDef footFixture = new FixtureDef();
		footFixture.shape = footShape;
		footFixture.density = 1.0f;
		footFixture.friction = 0.8f;
		footFixture.restitution = 0.4f;
		footFixture.filter.groupIndex = -1;
		
		leftFoot1 = world.createBody(footDef);
		leftFoot1.createFixture(footFixture);
		
		leftFoot2 = world.createBody(footDef);
		leftFoot2.createFixture(footFixture);
		
		rightFoot1 = world.createBody(footDef);
		rightFoot1.createFixture(footFixture);
		
		rightFoot2 = world.createBody(footDef);
		rightFoot2.createFixture(footFixture);
		
		
		
		RevoluteJointDef leftLegJoint1Def = new RevoluteJointDef();
		leftLegJoint1Def.bodyA = core;
		leftLegJoint1Def.bodyB = leftLeg1;
		leftLegJoint1Def.collideConnected = false;
		leftLegJoint1Def.localAnchorA = new Vec2(-1.1f,0.1f);
		leftLegJoint1Def.localAnchorB = new Vec2(0,-0.4f);
		leftLegJoint1Def.enableLimit = true;
		leftLegJoint1Def.lowerAngle = (float)Math.toRadians(-30);
		leftLegJoint1Def.upperAngle = (float)Math.toRadians(30);
		leftLegJoint1Def.enableMotor = true;
		leftLegJoint1Def.motorSpeed = 0.0f;
		leftLegJoint1Def.maxMotorTorque = 40.0f;
		leftLegJoint1 = (RevoluteJoint)world.createJoint(leftLegJoint1Def);
		
		RevoluteJointDef leftLegJoint2Def = new RevoluteJointDef();
		leftLegJoint2Def.bodyA = core;
		leftLegJoint2Def.bodyB = leftLeg2;
		leftLegJoint2Def.collideConnected = false;
		leftLegJoint2Def.localAnchorA = new Vec2(-1.1f,0.1f);
		leftLegJoint2Def.localAnchorB = new Vec2(0,-0.4f);
		leftLegJoint2Def.enableLimit = true;
		leftLegJoint2Def.lowerAngle = (float)Math.toRadians(-30);
		leftLegJoint2Def.upperAngle = (float)Math.toRadians(30);
		leftLegJoint2Def.enableMotor = true;
		leftLegJoint2Def.motorSpeed = 0.0f;
		leftLegJoint2Def.maxMotorTorque = 40.0f;
		leftLegJoint2 = (RevoluteJoint)world.createJoint(leftLegJoint2Def);
		
		RevoluteJointDef rightLegJoint1Def = new RevoluteJointDef();
		rightLegJoint1Def.bodyA = core;
		rightLegJoint1Def.bodyB = rightLeg1;
		rightLegJoint1Def.collideConnected = false;
		rightLegJoint1Def.localAnchorA = new Vec2(1.1f,0.1f);
		rightLegJoint1Def.localAnchorB = new Vec2(0,-0.4f);
		rightLegJoint1Def.enableLimit = true;
		rightLegJoint1Def.lowerAngle = (float)Math.toRadians(-30);
		rightLegJoint1Def.upperAngle = (float)Math.toRadians(30);
		rightLegJoint1Def.enableMotor = true;
		rightLegJoint1Def.motorSpeed = 0.0f;
		rightLegJoint1Def.maxMotorTorque = 40.0f;
		rightLegJoint1 = (RevoluteJoint)world.createJoint(rightLegJoint1Def);
		
		RevoluteJointDef rightLegJoint2Def = new RevoluteJointDef();
		rightLegJoint2Def.bodyA = core;
		rightLegJoint2Def.bodyB = rightLeg2;
		rightLegJoint2Def.collideConnected = false;
		rightLegJoint2Def.localAnchorA = new Vec2(1.1f,0.1f);
		rightLegJoint2Def.localAnchorB = new Vec2(0,-0.4f);
		rightLegJoint2Def.enableLimit = true;
		rightLegJoint2Def.lowerAngle = (float)Math.toRadians(-30);
		rightLegJoint2Def.upperAngle = (float)Math.toRadians(30);
		rightLegJoint2Def.enableMotor = true;
		rightLegJoint2Def.motorSpeed = 0.0f;
		rightLegJoint2Def.maxMotorTorque = 40.0f;
		rightLegJoint2 = (RevoluteJoint)world.createJoint(rightLegJoint2Def);
		
		
		
		RevoluteJointDef leftFootJoint1Def = new RevoluteJointDef();
		leftFootJoint1Def.bodyA = leftLeg1;
		leftFootJoint1Def.bodyB = leftFoot1;
		leftFootJoint1Def.collideConnected = false;
		leftFootJoint1Def.localAnchorA = new Vec2(0,0.4f);
		leftFootJoint1Def.localAnchorB = new Vec2(0,-0.2f);
		leftFootJoint1Def.enableLimit = true;
		leftFootJoint1Def.lowerAngle = (float)Math.toRadians(-30);
		leftFootJoint1Def.upperAngle = (float)Math.toRadians(30);
		leftFootJoint1Def.enableMotor = true;
		leftFootJoint1Def.motorSpeed = 0.0f;
		leftFootJoint1Def.maxMotorTorque = 10.0f;
		leftFootJoint1 = (RevoluteJoint)world.createJoint(leftFootJoint1Def);
		
		RevoluteJointDef leftFootJoint2Def = new RevoluteJointDef();
		leftFootJoint2Def.bodyA = leftLeg2;
		leftFootJoint2Def.bodyB = leftFoot2;
		leftFootJoint2Def.collideConnected = false;
		leftFootJoint2Def.localAnchorA = new Vec2(0,0.4f);
		leftFootJoint2Def.localAnchorB = new Vec2(0,-0.2f);
		leftFootJoint2Def.enableLimit = true;
		leftFootJoint2Def.lowerAngle = (float)Math.toRadians(-30);
		leftFootJoint2Def.upperAngle = (float)Math.toRadians(30);
		leftFootJoint2Def.enableMotor = true;
		leftFootJoint2Def.motorSpeed = 0.0f;
		leftFootJoint2Def.maxMotorTorque = 10.0f;
		leftFootJoint2 = (RevoluteJoint)world.createJoint(leftFootJoint2Def);
		
		RevoluteJointDef rightFootJoint1Def = new RevoluteJointDef();
		rightFootJoint1Def.bodyA = rightLeg1;
		rightFootJoint1Def.bodyB = rightFoot1;
		rightFootJoint1Def.collideConnected = false;
		rightFootJoint1Def.localAnchorA = new Vec2(0,0.4f);
		rightFootJoint1Def.localAnchorB = new Vec2(0,-0.2f);
		rightFootJoint1Def.enableLimit = true;
		rightFootJoint1Def.lowerAngle = (float)Math.toRadians(-30);
		rightFootJoint1Def.upperAngle = (float)Math.toRadians(30);
		rightFootJoint1Def.enableMotor = true;
		rightFootJoint1Def.motorSpeed = 0.0f;
		rightFootJoint1Def.maxMotorTorque = 10.0f;
		rightFootJoint1 = (RevoluteJoint)world.createJoint(rightFootJoint1Def);
		
		RevoluteJointDef rightFootJoint2Def = new RevoluteJointDef();
		rightFootJoint2Def.bodyA = rightLeg2;
		rightFootJoint2Def.bodyB = rightFoot2;
		rightFootJoint2Def.collideConnected = false;
		rightFootJoint2Def.localAnchorA = new Vec2(0,0.4f);
		rightFootJoint2Def.localAnchorB = new Vec2(0,-0.2f);
		rightFootJoint2Def.enableLimit = true;
		rightFootJoint2Def.lowerAngle = (float)Math.toRadians(-30);
		rightFootJoint2Def.upperAngle = (float)Math.toRadians(30);
		rightFootJoint2Def.enableMotor = true;
		rightFootJoint2Def.motorSpeed = 0.0f;
		rightFootJoint2Def.maxMotorTorque = 10.0f;
		rightFootJoint2 = (RevoluteJoint)world.createJoint(rightFootJoint2Def);
	}
	
	@Override
	public void destroy(World world) {
		world.destroyBody(core);
		world.destroyBody(leftLeg1);
		world.destroyBody(leftFoot1);
		world.destroyBody(leftLeg2);
		world.destroyBody(leftFoot2);
		world.destroyBody(rightLeg1);
		world.destroyBody(rightFoot1);
		world.destroyBody(rightLeg2);
		world.destroyBody(rightFoot2);
		
		world.destroyJoint(leftLegJoint1);
		world.destroyJoint(leftLegJoint2);
		world.destroyJoint(leftFootJoint1);
		world.destroyJoint(leftFootJoint2);
		world.destroyJoint(rightLegJoint1);
		world.destroyJoint(rightLegJoint2);
		world.destroyJoint(rightFootJoint1);
		world.destroyJoint(rightFootJoint2);
	}

	@Override
	public double[] getInputs() {
		float bodyAngle = (float)Math.toDegrees(this.core.getAngle());
		bodyAngle += 180;
		while (bodyAngle >= 360) bodyAngle -= 360;
		while (bodyAngle < 0) bodyAngle += 360;
		bodyAngle -= 180;
		bodyAngle /= 180;
		
		float leftLeg1Angle = (float)((Math.toDegrees(this.leftLegJoint1.getJointAngle())) / 30);
		float leftLeg2Angle = (float)((Math.toDegrees(this.leftLegJoint2.getJointAngle())) / 30);
		float leftFoot1Angle = (float)((Math.toDegrees(this.leftFootJoint1.getJointAngle())) / 30);
		float leftFoot2Angle = (float)((Math.toDegrees(this.leftFootJoint2.getJointAngle())) / 30);
		
		float rightLeg1Angle = (float)((Math.toDegrees(this.rightLegJoint1.getJointAngle())) / 30);
		float rightLeg2Angle = (float)((Math.toDegrees(this.rightLegJoint2.getJointAngle())) / 30);
		float rightFoot1Angle = (float)((Math.toDegrees(this.rightFootJoint1.getJointAngle())) / 30);
		float rightFoot2Angle = (float)((Math.toDegrees(this.rightFootJoint2.getJointAngle())) / 30);
		
		float leftTouch1 = this.leftFoot1.getContactList()==null?-1:1;
		float leftTouch2 = this.leftFoot2.getContactList()==null?-1:1;
		float rightTouch1 = this.rightFoot1.getContactList()==null?-1:1;
		float rightTouch2 = this.rightFoot2.getContactList()==null?-1:1;
		
		double[] inputs = new double[]{bodyAngle, 
				leftLeg1Angle, leftLeg2Angle, leftFoot1Angle, leftFoot2Angle,
				rightLeg1Angle, rightLeg2Angle, rightFoot1Angle, rightFoot2Angle,
				leftTouch1, leftTouch2, rightTouch1, rightTouch2};
		
		return inputs;
	}

	@Override
	public void feedResults(double[] results) {
		this.leftLegJoint1.setMotorSpeed((float)(results[0] * 5));
		this.leftLegJoint2.setMotorSpeed((float)(results[1] * 5));
		this.leftFootJoint1.setMotorSpeed((float)(results[2] * 5));
		this.leftFootJoint2.setMotorSpeed((float)(results[3] * 5));
		
		this.rightLegJoint1.setMotorSpeed((float)(results[4] * 5));
		this.rightLegJoint2.setMotorSpeed((float)(results[5] * 5));
		this.rightFootJoint1.setMotorSpeed((float)(results[6] * 5));
		this.rightFootJoint2.setMotorSpeed((float)(results[7] * 5));
	}

	@Override
	public void updateTracker(Tracker tracker) {
		tracker.data[0] = core.getPosition().x;
	}

	@Override
	public Vec2 getPosition() {
		return core.getPosition();
	}

	@Override
	public Body[] getBodies() {
		return new Body[]{leftLeg1, leftLeg2, leftFoot1, leftFoot2, core, rightLeg1, rightLeg2, rightFoot1, rightFoot2};
	}

	@Override
	public double fitness(Tracker tracker) {
		return tracker.data[0];
	}

	@Override
	public int[] getNet() {
		return new int[]{13,8,8};
	}

}
