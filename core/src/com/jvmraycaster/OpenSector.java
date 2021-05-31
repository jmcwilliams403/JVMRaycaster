package com.jvmraycaster;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
public class OpenSector extends Polyline implements Sector<OpenSector>{

	private SectorWall wall;
	public OpenSector(float[] vertices) {
		super(vertices);
		this.wall = new SectorWall(0,0);
	}
	public OpenSector(float[] vertices, float height) {
		super(vertices);
		this.wall = new SectorWall(0,height);
	}
	public OpenSector(float[] vertices, float bottomHeight, float topHeight) {
		super(vertices);
		this.wall = new SectorWall(bottomHeight,topHeight);
	}
	public OpenSector(Vector2[] vertices) {
		super(Sector.toFloatArray(vertices));
		this.wall = new SectorWall(0,0);
	}
	
	public OpenSector(Vector2[] vertices, float height) {
		super(Sector.toFloatArray(vertices));
		this.wall = new SectorWall(0,height);
	}
	public OpenSector(Vector2[]vertices, float bottomHeight, float topHeight) {
		super(Sector.toFloatArray(vertices));
		this.wall = new SectorWall(bottomHeight,topHeight);
	}
	
	@Override
	public OpenSector setTopHeight(float topHeight) {
		this.wall.setTopHeight(topHeight);
		return this;
	}

	@Override
	public OpenSector setBottomHeight(float bottomHeight) {
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

	/*
	@Override
	public OpenSector getTranslated(float x, float y) {
		this.translate(x, y);
		return this;
	}

	@Override
	public OpenSector getTranslatedX(float x) {
		this.translate(x, 0);
		return this;
	}

	@Override
	public OpenSector getTranslatedY(float y) {
		this.translate(0, y);
		return this;
	}
	*/
	/*
	@Override
	public boolean lineIntersects(Vector2 a, Vector2 b) {
		return getIntersection(a,b)!=null;
	}
	*/
	/*
	@Override
	public Intersection getIntersection(Vector2 a, Vector2 b) {
		Intersection intersection = null;
		final float[]vertices = this.getTransformedVertices();
		final int size = vertices.length-2;
		float nearest = Float.POSITIVE_INFINITY;
		for (int i = 0; i < size; i+=2) {
			Vector2 point = new Vector2();
			Vector2 a2 = new Vector2(vertices[i], vertices[i+1]);
			Vector2 b2 = new Vector2(vertices[i+2],vertices[i+3]);
			if(Intersector.intersectSegments(a,b,a2,b2,point)){
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
