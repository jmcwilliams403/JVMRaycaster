package com.jvmraycaster;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
public abstract class Mob extends Entity{

	private static final long serialVersionUID = 1274174916406296711L;
	public float speed;
	public float sprintRate;
	public boolean autoSprint;
	public Mob() {
		this.speed = 200;
		this.sprintRate = 2;
		autoSprint = false;
	}
	public void moveForward(float distance) {
		this.setPosition(new Ray(this.getCenter(), toDirection(this.rotation)).getEndPoint(new Vector3(), distance));
		//this.y = new Ray(this.getCenter(), toDirection(this.rotation)).getEndPoint(new Vector3(), distance).y;
		//this.direction.origin.set(this.getCenter());
	}
	public void strafe(float distance) {
		this.setPosition(new Ray(this.getCenter(), toDirection(this.rotation+90)).getEndPoint(new Vector3(), distance));
		//this.y = new Ray(this.getCenter(), toDirection(this.rotation-90)).getEndPoint(new Vector3(), distance).y;
		//this.direction.origin.set(this.getCenter());
	}
	public float getEyeLevel() {
		return this.altitude + this.height;
	}
	public void toggleAutoSprint(){
		this.autoSprint = !this.autoSprint;
	}
}
