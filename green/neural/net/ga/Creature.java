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

public class Creature extends AbstractCreature {

	public Body cCoreBody;
	public Body cLeftLegBody;
	public Body cLeftFootBody;
	public Body cRightLegBody;
	public Body cRightFootBody;
	public RevoluteJoint cLeftLegJoint;
	public RevoluteJoint cLeftFootJoint;
	public RevoluteJoint cRightLegJoint;
	public RevoluteJoint cRightFootJoint;
	
	public Creature(){}
	
	public Creature(World world, float x, float y){
		//Core
		BodyDef cCoreDef = new BodyDef();
		cCoreDef.type = BodyType.DYNAMIC;
		cCoreDef.position = new Vec2(x,y);
		cCoreDef.fixedRotation = false;
		cCoreDef.angle = (float)Math.toRadians(0);
		//---
		PolygonShape cCore = new PolygonShape();
		cCore.setAsBox(1.6f, 0.3f);
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
		
		
		//Left leg
		BodyDef cLeftLegDef = new BodyDef();
		cLeftLegDef.type = BodyType.DYNAMIC;
		cLeftLegDef.fixedRotation = false;
		//---
		PolygonShape cLeftLeg = new PolygonShape();
		cLeftLeg.setAsBox(0.2f, 0.6f);
		//--
		FixtureDef cLeftLegFixture = new FixtureDef();
		cLeftLegFixture.shape = cLeftLeg;
		cLeftLegFixture.density = 1.0f;
		cLeftLegFixture.friction = 0.6f;
		cLeftLegFixture.restitution = 0.4f;
		cLeftLegFixture.filter.groupIndex = -1;
		//---
		cLeftLegBody = world.createBody(cLeftLegDef);
		cLeftLegBody.createFixture(cLeftLegFixture);
		
		
		//Left foot
		BodyDef cLeftFootDef = new BodyDef();
		cLeftFootDef.type = BodyType.DYNAMIC;
		cLeftFootDef.position = new Vec2(x-0.6f,y+0.15f);
		cLeftFootDef.fixedRotation = false;
		//---
		PolygonShape cLeftFoot = new PolygonShape();
		cLeftFoot.setAsBox(0.1f, 0.4f);
		//--
		FixtureDef cLeftFootFixture = new FixtureDef();
		cLeftFootFixture.shape = cLeftFoot;
		cLeftFootFixture.density = 1.0f;
		cLeftFootFixture.friction = 0.6f;
		cLeftFootFixture.restitution = 0.4f;
		cLeftFootFixture.filter.groupIndex = -1;
		//---
		//cLeftFootBody = world.createBody(cLeftFootDef);
		//cLeftFootBody.createFixture(cLeftFootFixture);
		
		//Right leg
		BodyDef cRightLegDef = new BodyDef();
		cRightLegDef.type = BodyType.DYNAMIC;
		cRightLegDef.position = new Vec2(x+0.6f,y+0.15f);
		cRightLegDef.fixedRotation = false;
		//---
		PolygonShape cRightLeg = new PolygonShape();
		cRightLeg.setAsBox(0.2f, 0.6f);
		//--
		FixtureDef cRightLegFixture = new FixtureDef();
		cRightLegFixture.shape = cRightLeg;
		cRightLegFixture.density = 1.0f;
		cRightLegFixture.friction = 0.6f;
		cRightLegFixture.restitution = 0.4f;
		cRightLegFixture.filter.groupIndex = -1;
		//---
		cRightLegBody = world.createBody(cRightLegDef);
		cRightLegBody.createFixture(cRightLegFixture);
		
		
		//Right foot
		BodyDef cRightFootDef = new BodyDef();
		cRightFootDef.type = BodyType.DYNAMIC;
		cRightFootDef.fixedRotation = false;
		//---
		PolygonShape cRightFoot = new PolygonShape();
		cRightFoot.setAsBox(0.1f, 0.4f);
		//--
		FixtureDef cRightFootFixture = new FixtureDef();
		cRightFootFixture.shape = cRightFoot;
		cRightFootFixture.density = 1.0f;
		cRightFootFixture.friction = 0.6f;
		cRightFootFixture.restitution = 0.4f;
		cRightFootFixture.filter.groupIndex = -1;
		//---
		//cRightFootBody = world.createBody(cRightFootDef);
		//cRightFootBody.createFixture(cRightFootFixture);
		
		//Left joint
		RevoluteJointDef cLeftLegJointDef = new RevoluteJointDef();
		cLeftLegJointDef.bodyA = cCoreBody;
		cLeftLegJointDef.bodyB = cLeftLegBody;
		cLeftLegJointDef.collideConnected = false;
		cLeftLegJointDef.localAnchorA = new Vec2(-1.2f,0);
		cLeftLegJointDef.localAnchorB = new Vec2(0,-0.3f);
		cLeftLegJointDef.enableLimit = true;
		cLeftLegJointDef.lowerAngle = (float)Math.toRadians(-80);
		cLeftLegJointDef.upperAngle = (float)Math.toRadians(80);
		cLeftLegJointDef.enableMotor = true;
		cLeftLegJointDef.motorSpeed = 0.0f;
		cLeftLegJointDef.maxMotorTorque = 100.0f;
		cLeftLegJoint = (RevoluteJoint)world.createJoint(cLeftLegJointDef);
		
		//Right joint
		RevoluteJointDef cRightLegJointDef = new RevoluteJointDef();
		cRightLegJointDef.bodyA = cCoreBody;
		cRightLegJointDef.bodyB = cRightLegBody;
		cRightLegJointDef.collideConnected = false;
		cRightLegJointDef.localAnchorA = new Vec2(1.2f,0);
		cRightLegJointDef.localAnchorB = new Vec2(0,-0.3f);
		cRightLegJointDef.enableLimit = true;
		cRightLegJointDef.lowerAngle = (float)Math.toRadians(-80);
		cRightLegJointDef.upperAngle = (float)Math.toRadians(80);
		cRightLegJointDef.enableMotor = true;
		cRightLegJointDef.motorSpeed = 0.0f;
		cRightLegJointDef.maxMotorTorque = 100.0f;
		cRightLegJoint = (RevoluteJoint)world.createJoint(cRightLegJointDef);
		
		//Left ankle
		RevoluteJointDef cLeftFootJointDef = new RevoluteJointDef();
		cLeftFootJointDef.bodyA = cLeftLegBody;
		cLeftFootJointDef.bodyB = cLeftFootBody;
		cLeftFootJointDef.collideConnected = false;
		cLeftFootJointDef.localAnchorA = new Vec2(0,0.4f);
		cLeftFootJointDef.localAnchorB = new Vec2(0,-0.2f);
		cLeftFootJointDef.enableLimit = true;
		cLeftFootJointDef.lowerAngle = (float)Math.toRadians(-30);
		cLeftFootJointDef.upperAngle = (float)Math.toRadians(30);
		cLeftFootJointDef.enableMotor = true;
		cLeftFootJointDef.motorSpeed = 0.0f;
		cLeftFootJointDef.maxMotorTorque = 100.0f;
		//cLeftFootJoint = (RevoluteJoint)world.createJoint(cLeftFootJointDef);
		
		//Right ankle
		RevoluteJointDef cRightFootJointDef = new RevoluteJointDef();
		cRightFootJointDef.bodyA = cRightLegBody;
		cRightFootJointDef.bodyB = cRightFootBody;
		cRightFootJointDef.collideConnected = false;
		cRightFootJointDef.localAnchorA = new Vec2(0,0.4f);
		cRightFootJointDef.localAnchorB = new Vec2(0,-0.2f);
		cRightFootJointDef.enableLimit = true;
		cRightFootJointDef.lowerAngle = (float)Math.toRadians(-30);
		cRightFootJointDef.upperAngle = (float)Math.toRadians(30);
		cRightFootJointDef.enableMotor = true;
		cRightFootJointDef.motorSpeed = 0.0f;
		cRightFootJointDef.maxMotorTorque = 100.0f;
		//cRightFootJoint = (RevoluteJoint)world.createJoint(cRightFootJointDef);
	}
	
	@Override
	public void destroy(World world) {
		world.destroyBody(cCoreBody);
		world.destroyBody(cLeftLegBody);
		world.destroyBody(cRightLegBody);
		//world.destroyBody(cLeftFootBody);
		//world.destroyBody(cRightFootBody);
		//---
		world.destroyJoint(cLeftLegJoint);
		world.destroyJoint(cRightLegJoint);
		//world.destroyJoint(cLeftFootJoint);
		//world.destroyJoint(cRightFootJoint);
	}

	@Override
	public double[] getInputs() {
		float bodyAngle = (float)Math.toDegrees(this.cCoreBody.getAngle());
		bodyAngle+=180;
		while (bodyAngle >= 360) bodyAngle -= 360;
		while (bodyAngle < 0) bodyAngle += 360;
		bodyAngle -= 180;
		bodyAngle /= 180;
		
		float leftAngle = (float)((Math.toDegrees(this.cLeftLegJoint.getJointAngle())) / 80);
		float rightAngle = (float)((Math.toDegrees(this.cRightLegJoint.getJointAngle())) / 80);
		
		float leftTouch = this.cLeftLegBody.getContactList()==null?-1:1;
		float rightTouch = this.cRightLegBody.getContactList()==null?-1:1;
		
		double[] inputs = new double[]{bodyAngle, leftAngle, rightAngle, leftTouch, rightTouch};
		
		return inputs;
	}

	@Override
	public void feedResults(double[] results) {
		this.cLeftLegJoint.setMotorSpeed((float)(results[0] * 10));
		this.cRightLegJoint.setMotorSpeed((float)(results[1] * 10));
	}

	@Override
	public void updateTracker(Tracker tracker) {
		tracker.data[0] = this.cCoreBody.getPosition().x;
	}

	@Override
	public Vec2 getPosition() {
		return this.cCoreBody.getPosition();
	}

	@Override
	public Body[] getBodies() {
		return new Body[]{cCoreBody, cLeftLegBody, cRightLegBody};
	}

	public int[] getNet(){
		return new int[]{5,8,2};
	}
	
	public double fitness(Tracker tracker){
		return tracker.data[0];
	}
	
}
