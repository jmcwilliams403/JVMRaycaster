package com.jvmraycaster;

public abstract class AbstractWall implements Wall<AbstractWall> {
	private float topHeight;
	private float bottomHeight;
	public AbstractWall(float bottomHeight, float topHeight) {
		this.topHeight = topHeight;
		this.bottomHeight = bottomHeight;
	}
	public AbstractWall(Wall<?> wall){
		this.topHeight = wall.getTopHeight();
		this.bottomHeight = wall.getBottomHeight();
	}
	@Override
	public AbstractWall setTopHeight(float topHeight) {
		this.topHeight = topHeight;
		return this;
	}

	@Override
	public AbstractWall setBottomHeight(float bottomHeight) {
		this.bottomHeight = bottomHeight;
		return this;
	}
	@Override
	public float getTopHeight() {
		return this.topHeight;
	}
	@Override
	public float getBottomHeight() {
		return this.bottomHeight;
	}
}
