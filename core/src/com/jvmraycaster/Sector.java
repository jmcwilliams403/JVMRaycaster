package com.jvmraycaster;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Segment;
import com.badlogic.gdx.math.collision.Ray;
public interface Sector<T> extends Wall<T>{
	public class Intersection{
		public Segment surface;
		public Vector2 origin;
		public Vector2 point;
		public Intersection(Vector2 origin, Vector2 point, Segment surface) {
			this.point = point;
			this.surface = surface;
			this.origin = origin;
		}
		public Intersection(Vector2 origin, Vector2 point, Vector2 a, Vector2 b) {
			this.point = point;
			this.surface = new Segment(new Vector3(a,0), new Vector3(b,0));
			this.origin = origin;
		}
		public Intersection(Vector2 origin, Vector2 point, Vector3 a, Vector3 b) {
			this.point = point;
			this.surface = new Segment(a.x,a.y,0,b.x,b.y,0);
			this.origin = origin;
		}
		public Intersection(Vector2 origin, Vector3 point, Vector3 a, Vector3 b) {
			this.point = new Vector2(point.x, point.y);
			this.surface = new Segment(a.x,a.y,0,b.x,b.y,0);
			this.origin = origin;
		}
		public float getOriginDistance() {
			return this.origin.dst(this.point);
		}
		public float getSurfaceDistance() {
			return surface.a.dst(new Vector3(this.point,0))/surface.len();
		}
	}
	public class SectorWall extends AbstractWall{
		public SectorWall(float bottomHeight, float topHeight) {
			super(bottomHeight, topHeight);
		}
	}
	public static float[] toFloatArray(Vector2[] vertices) {
		final int size = vertices.length;
		float[] output = new float[size*2];
		for (int i = 0; i < size; i++) {
			output[i*2] = vertices[i].x;
			output[(i*2)+1] = vertices[i].y;
		}
		return output;
	}

	public default int getNumVertices() {
		return this.getVertices().length/2;
	}
	
	public default Sector<T> getTranslated(float x, float y){
		this.translate(x,y);
		return this;
	}
	public default Sector<T> getTranslatedX(float x){
		this.translate(x, this.getY());
		return this;
	}
	public default Sector<T> getTranslatedY(float y){
		this.translate(this.getX(), y);
		return this;
	}
	
	public boolean contains(float x, float y);
	public boolean contains(Vector2 point);
	public default boolean contains(Vector3 point) {
		return this.contains(point.x, point.y);
	}
	
	public default boolean lineIntersects(Vector2 a, Vector2 b) {
		return getIntersection(a,b)!=null;
	}
	public default boolean lineIntersects(float a1, float a2, float b1, float b2) {
		return this.lineIntersects(new Vector2(a1,a2), new Vector2(b1,b2));
	}
	public default boolean lineIntersects(Vector3 a, Vector3 b) {
		return this.lineIntersects(a.x,a.y,b.x,b.y);
	}
	public default boolean lineIntersects(Ray ray, float distance) {
		return this.lineIntersects(ray.origin, ray.getEndPoint(new Vector3(), distance));
	}
	public default Intersection getIntersection (Vector2 a, Vector2 b) {
		Intersection intersection = null;
		float[] vertices = this.getTransformedVertices();
		final int size = vertices.length;
		final int end = ((this instanceof ClosedSector)?size:size-2);
		float nearest = Float.POSITIVE_INFINITY;
		for(int i = 0; i < end; i+=2) {
			Vector2 point = new Vector2();
			Vector2 a2 = new Vector2(vertices[i], vertices[i+1]);
			Vector2 b2 = new Vector2(vertices[(i+2)%size], vertices[(i+3)%size]);
			if(Intersector.intersectSegments(a, b, a2, b2, point)) {
				float distance = a.dst(point);
				if (distance < nearest) {
					nearest = distance;
					intersection = new Intersection(a, point, a2,b2);
				}
			}
		}
		return intersection;
	}
	//public Intersection getIntersection(Vector2 a, Vector2 b);
	public default Intersection getIntersection(float a1, float a2, float b1, float b2) {
		return this.getIntersection(new Vector2(a1,a2), new Vector2(b1,b2));
	}
	public default Intersection getIntersection(Vector3 a, Vector3 b) {
		return this.getIntersection(a.x,a.y,b.x,b.y);
	}
	public default Intersection getIntersection(Ray ray, float distance) {
		return this.getIntersection(ray.origin, ray.getEndPoint(new Vector3(), distance));
	}
	
	public float[] getVertices();
	public float[] getTransformedVertices();
	public float getX();
	public float getY();
	public void translate(float x, float y);
}
