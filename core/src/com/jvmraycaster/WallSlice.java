package com.jvmraycaster;

public class WallSlice extends AbstractWall {

	public float z;
	public float textureX;

	public WallSlice(float bottomHeight, float topHeight, float z) {
		super(bottomHeight, topHeight);
		this.z = z;
		this.textureX = 0;
	}
	public WallSlice(Sector<?> sector, float z) {
		super(sector);
		this.z = z;
		this.textureX = 0;
	}
	public WallSlice(float bottomHeight, float topHeight, float z, float textureX) {
		super(bottomHeight, topHeight);
		this.z = z;
		this.textureX = textureX;
	}
	public WallSlice(Sector<?> sector, float z, float textureX) {
		super(sector);
		this.z = z;
		this.textureX = textureX;
	}

	public boolean isDrawable() {
		return this.getTopHeight() != this.getBottomHeight();
	}
	public boolean obscures(WallSlice wall) {
		return (this.getTopHeight() >= wall.getTopHeight() && this.getBottomHeight() <= wall.getBottomHeight() && this.z < wall.z);
	}

}
