package com.jvmraycaster;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
public  class Room extends ClosedSector {
	Room(float[] vertices){
		super(vertices);
	}
	Room(float[] vertices, float height){
		super(vertices, height);
	}
	Room(float[] vertices, float bottomHeight, float topHeight){
		super(vertices, bottomHeight, topHeight);
	}
	Room(Vector2[] vertices){
		super(vertices);
	}
	Room(Vector2[] vertices, float height){
		super(vertices, height);
	}
	Room(Vector2[]vertices, float bottomHeight, float topHeight){
		super(vertices, bottomHeight, topHeight);
	}
	@Override
	public boolean contains(Vector3 point) {
		return !super.contains(point);
	}
	@Override
	public boolean contains(Vector2 point) {
		return !super.contains(point);
	}
	@Override
	public boolean contains(float x, float y) {
		return !super.contains(x, y);
	}
	@Override
	public boolean lineIntersects(Vector2 a, Vector2 b) {
		return this.getIntersection(a, b)!=null;
	}
}
