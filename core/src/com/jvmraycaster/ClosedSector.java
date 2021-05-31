package com.jvmraycaster;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Intersector;
public class ClosedSector extends Polygon implements Sector<ClosedSector>{

	private SectorWall wall;
	public ClosedSector(float[] vertices) {
		super(vertices);
		this.wall = new SectorWall(0,0);
	}
	public ClosedSector(float[] vertices, float height) {
		super(vertices);
		this.wall = new SectorWall(0,height);
	}
	public ClosedSector(float[] vertices, float bottomHeight, float topHeight) {
		super(vertices);
		this.wall = new SectorWall(bottomHeight,topHeight);
	}
	public ClosedSector(Vector2[] vertices) {
		super(Sector.toFloatArray(vertices));
		this.wall = new SectorWall(0,0);
	}
	public ClosedSector(Vector2[] vertices, float height) {
		super(Sector.toFloatArray(vertices));
		this.wall = new SectorWall(0,height);
	}
	public ClosedSector(Vector2[]vertices, float bottomHeight, float topHeight) {
		super(Sector.toFloatArray(vertices));
		this.wall = new SectorWall(bottomHeight,topHeight);
	}
	
	public static ClosedSector rectangle(float width, float depth) {
		return new ClosedSector(new float[] {0,0,width,0,width,depth,0,depth});
	}
	public static ClosedSector rectangle(float width, float depth, float height) {
		return new ClosedSector(new float[] {0,0,width,0,width,depth,0,depth},height);
	}
	public static ClosedSector rectangle(float width, float depth, float bottomHeight, float topHeight) {
		return new ClosedSector(new float[] {0,0,width,0,width,depth,0,depth},bottomHeight, topHeight);
	}
	public static ClosedSector square(float width) {
		return new ClosedSector(new float[] {0,0,width,0,width,width,0,width});
	}
	public static ClosedSector square(float width, float height) {
		return new ClosedSector(new float[] {0,0,width,0,width,width,0,width},height);
	}
	public static ClosedSector cube(float size) {
		return new ClosedSector(new float[] {0,0,size,0,size,size,0,size},size);
	}
	@Override
	public ClosedSector setTopHeight(float topHeight) {
		this.wall.setTopHeight(topHeight);
		return this;
	}
	@Override
	public ClosedSector setBottomHeight(float bottomHeight) {
		this.wall.setBottomHeight(bottomHeight);
		return this;
	}

	@Override
	public float getTopHeight() {
		return this.wall.getTopHeight();
	}
	@Override
	public float getBottomHeight() {
		return this.wall.getBottomHeight();
	}

	@Override
	public boolean lineIntersects(Vector2 a, Vector2 b) {
		return Intersector.intersectSegmentPolygon(a, b, this);
	}
	
	/*
	@Override
	public Intersection getIntersection(Vector2 a, Vector2 b) {
		Intersection intersection = null;
		float[] vertices = this.getTransformedVertices();
		final int size = vertices.length;
		float nearest = Float.POSITIVE_INFINITY;
		for(int i = 0; i < size; i+=2) {
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
	*/
}
