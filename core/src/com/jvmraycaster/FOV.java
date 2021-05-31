package com.jvmraycaster;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Polygon;
import java.util.ArrayList;
public class FOV extends Entity{
	private static final long serialVersionUID = -1449654005087061697L;
	public final float angle;
	public final int width;
	public float activeColumn;
	private final ArrayList<Float> rayAngles;
	private Ray ray;
	public FOV(Player player, float angle, float distance, int width) {
		super(player.x,player.y,distance);
		this.angle = angle;
		this.width = width;
		this.rotation = player.rotation;
		this.direction = player.direction;
		this.rayAngles = getInitRayAngles();
		this.ray = new Ray(player.getCenter(), toDirection(rayAngles.get(0)));

		this.activeColumn = 0;
	}
	public FOV(Player player, int width) {
		super(player.x,player.y,1000);
		this.angle = 90;
		this.width = width;
		this.rotation = player.rotation;
		this.direction = player.direction;
		this.rayAngles = getInitRayAngles();
		this.ray = new Ray(player.getCenter(), toDirection(rayAngles.get(0)));

		this.activeColumn = 0;
	}
	public FOV(FOV fov) {
		super(fov.x, fov.y, fov.radius);
		this.angle = fov.angle;
		this.width = fov.width;
		this.rotation = fov.rotation;
		this.direction = fov.direction;
		this.rayAngles = getInitRayAngles();
		this.ray = new Ray(this.getCenter(), toDirection(this.rayAngles.get(0)));
	}
	public FOV update() {
		 return new FOV(this);
	}
	private ArrayList<Float> getInitRayAngles(){
		ArrayList<Float> rayAngles = new ArrayList<Float>();
		for (int i = 0; i <= this.width; i++) {
			rayAngles.add(calculateRayAngle(i));
		}
		return rayAngles;
	}
	public void setRayAngle(int x) {
		//float angle = ((this.angle/width)*x);
		//float angle = getNewRayAngle(x);
		this.ray.set(this.getCenter(), toDirection(this.rayAngles.get(x)+getRayTranspose()));
		this.activeColumn = x;
		//System.out.println(getNewRayAngle(x));
		//System.out.println(getDistanceScale(1000));
	}
	private float calculateRayAngle(int column) {
		return this.angle*getColumnScale(column);
	}
	private float getColumnScale(float column) {
		float x = column/this.width;
		return toAngle(x,(x*-1)+1)/90;
	}
	private float getRayTranspose() {
		return (float)((this.rotation-this.angle)+(this.angle/2))%360;
	}
	
	public float getDistanceScale() {
		return (float)Math.cos(Math.atan2(this.ray.direction.x, this.ray.direction.y)-Math.atan2(this.direction.x,this.direction.y));
	}
	
	public Ray getRay() {
		return this.ray;
	}
	@Override
	public boolean overlaps(Sector<?> p) {
		return super.overlaps(p) && overlaps(getCrop(), p);
	}
	private final Polygon getCrop() {
		final float x = this.radius * (float)Math.tan(Math.toRadians(this.angle/2));
		Polygon crop = new Polygon(new float[] {0, 0, x*-1, this.radius, x, this.radius});
		crop.setOrigin(0, 0);
		crop.setPosition(this.x, this.y);
		crop.setRotation(this.rotation-90);
		
		return crop;
	}
	public Vector3 getPointOnRay(float distance) {
		return this.ray.getEndPoint(new Vector3(), distance);
	}

}