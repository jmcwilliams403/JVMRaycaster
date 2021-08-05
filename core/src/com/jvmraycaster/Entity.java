package com.jvmraycaster;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Intersector;
public abstract class Entity extends Circle{

	private static final long serialVersionUID = -9068456646371734579L;
	private static final Circle DEFAULT = new Circle(0,0,10);
	public Vector3 direction;
	public float rotation;
	public float altitude;
	public float height;
	public Entity() {
		super(DEFAULT);
		init();
	}
	
	public Entity(float radius) {
		super(DEFAULT.x, DEFAULT.y, radius);
		init();
	}
	
	public Entity(float x, float y) {
		super(x,y,DEFAULT.radius);
		init();
	}
	
	public Entity(float x, float y, float radius) {
		super(x, y, radius);
		init();
	}
	
	public Vector3 getCenter() {
		return new Vector3(this.x, this.y,0);
	}
	
	public void rotate(float angle) {
		setRotation((this.rotation - angle));
		refreshDirection();
	}
	
	public void setPosition(Vector3 position) {
		super.setPosition(position.x, position.y);
	}
	
	protected static Vector3 toDirection(float angle) {
		return new Vector3((float)Math.cos(Math.toRadians(angle%360)),(float)Math.sin(Math.toRadians(angle%360)),0);
	}
	protected static float toAngle(float x, float y) {
		return (float)Math.toDegrees(Math.atan2(y, x));
	}
	public static float toAngle(Vector3 direction) {
		return toAngle(direction.x, direction.y);
		//return (float)Math.toDegrees(Math.atan2(direction.y, direction.x));
	}
	private void setRotation(float angle) {
		this.rotation = angle % 360;
		refreshDirection();
	}
	private void refreshDirection() {
		direction.set(toDirection(this.rotation%360));
	}
	private void init() {
		this.direction = new Vector3();
		setRotation(90);
		altitude = 0;
		height = 0;
	}
	public boolean overlaps (Sector<?> p) {
		final float[] vertices = p.getTransformedVertices();
		final int size = vertices.length;
		final int end = (p instanceof ClosedSector)? size : size-2;
		for(int i = 0; i < end; i+=2) {
			if(Intersector.intersectSegmentCircle(new Vector2(vertices[i], vertices[i+1]), new Vector2(vertices[(i+2)%size], vertices[(i+3)%size]), this, null)) return true;
		}
		return false;
	}
	/*
	protected static <T extends Shape2D> boolean overlaps(T base, Sector<?> p) {
		final float[] vertices = p.getTransformedVertices();
		final float size = vertices.length;
		for(int i = 0; i < size; i+=2) {
			if(base.contains(vertices[i], vertices[i+1])) {
				return true;
			}
		}
		return false;
	}
	*/
	public static Vector2 toVector2(Vector3 vector) {
		return new Vector2(vector.x, vector.y);
	}
	public static Vector3 toVector3(Vector2 vector) {
		return new Vector3(vector, 0);
	}
}
