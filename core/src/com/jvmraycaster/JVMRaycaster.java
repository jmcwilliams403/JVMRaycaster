package com.jvmraycaster;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class JVMRaycaster extends Game {
	public PolygonSpriteBatch batch;
	public BitmapFont font;
	@Override
	public void create () {
		batch = new PolygonSpriteBatch();
		font = new BitmapFont();
		
		this.setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		this.getScreen().dispose();
	}
}
