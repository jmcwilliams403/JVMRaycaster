package com.jvmraycaster;

public interface Wall<T> {
	public Wall<T> setTopHeight(float topHeight);
	public Wall<T> setBottomHeight(float bottomHeight);
	
	public float getTopHeight();
	public float getBottomHeight();
	
	public default Wall<T> setHeight(float bottomHeight, float topHeight){
		setTopHeight(topHeight);
		setBottomHeight(bottomHeight);
		return this;
	}
	public default Wall<T> setHeight(float height) {
		float bottomHeight = this.getBottomHeight();
		float topHeight = bottomHeight;
		if (height > 0) {
			topHeight += height;
		}
		else if(height < 0){
			bottomHeight += height;
		}
		this.setHeight(bottomHeight, topHeight);
		return this;
	}
	public default float getHeight() {
		final boolean sameSign = (Math.signum(this.getTopHeight())==Math.signum(this.getBottomHeight()));
		final float topHeight = sameSign?(float)Math.abs(this.getTopHeight()):this.getTopHeight();
		final float bottomHeight = sameSign?(float)Math.abs(this.getBottomHeight()):this.getBottomHeight();
		return (float)(Math.max(topHeight, bottomHeight)-Math.min(topHeight, bottomHeight));
	}
}
