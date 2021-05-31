package com.jvmraycaster;
import com.badlogic.gdx.graphics.Camera;
public class Player extends Mob {
	private static final long serialVersionUID = -4493537962100332146L;
	public FOV fov;
	public Player(Camera camera) {
		fov = new FOV(this, (int)camera.viewportWidth);
		this.height = 50;
	}
	public void updateCamera(Camera camera) {
		fov = new FOV(this, (int)camera.viewportWidth);
	}
	/*
	public void setRay(float angle) {
		fov.ray.set(this.getCenter(), getDirection((this.rotation+(angle+(90-(this.fov.angle/2)))-90)%360));
	}
	*/
	@Override
	public void moveForward(float distance) {
		super.moveForward(distance);
		moveFOV();
	}
	@Override
	public void strafe(float distance) {
		super.strafe(distance);
		moveFOV();
	}
	
	@Override
	public void rotate(float angle) {
		super.rotate(angle);
		rotateFOV();
		//System.out.println(this.direction.direction);
	}
	
	private void moveFOV() {
		this.fov.setPosition(this.x, this.y);
	}
	private void rotateFOV() {
		this.fov.rotation = this.rotation;
		this.fov.direction = this.direction;
	}
}
