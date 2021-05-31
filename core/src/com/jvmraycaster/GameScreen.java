package com.jvmraycaster;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import space.earlygrey.shapedrawer.ShapeDrawer;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashSet;
import java.util.Iterator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;

public class GameScreen implements Screen{
	final JVMRaycaster game;
	Player player;
	Viewport viewport;
	HashSet<Sector<?>> sectors;
	int WIDTH;
	int HEIGHT;
	final int BASE_WIDTH;
	final int BASE_HEIGHT;
	
	final int DEPTH;
	final float FIDELITY;
	final float GRID_SPACE;
	ShapeDrawer shapeDrawer;
	static Texture texture;
	Boolean miniMap;
	
	public GameScreen(final JVMRaycaster game) {
		this.game = game;
		BASE_WIDTH = Gdx.graphics.getWidth();
		BASE_HEIGHT = Gdx.graphics.getHeight();
		viewport = new FitViewport(BASE_WIDTH, BASE_HEIGHT);
		player = new Player(viewport.getCamera());
		DEPTH = (int)player.fov.radius;
		miniMap = false;
		FIDELITY = 1f;
		GRID_SPACE = DEPTH/(DEPTH*FIDELITY);
		sectors = new HashSet<Sector<?>>();
		sectors.add(new ClosedSector(new float[]{0,0,0,100,200,100,200,0}, 0, 150f).getTranslated(-100, 100));
		sectors.add(new ClosedSector(new float[]{0,0,0,50,50,50,50,0}, 0, 75f));
		sectors.add(new ClosedSector(new float[]{0,0,0,25,25,25,25,0},0,100f).getTranslated(75, 0));
		sectors.add(new OpenSector(new float[]{0,0,100,0,100,50},0,100).getTranslated(-150,0));
	}
	public static TextureRegion textureRegion() {
		Pixmap pixmap = new Pixmap(1,1,Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.drawPixel(0,0);
		texture = new Texture(pixmap);
		pixmap.dispose();
		return new TextureRegion(texture, 0,0,1,1);
	}
	@Override
	public void show() {
		Gdx.input.setCursorCatched(true);
	}
	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0, 1);
		WIDTH = (int)viewport.getCamera().viewportWidth;
		HEIGHT = (int)viewport.getCamera().viewportHeight;
		game.batch.setProjectionMatrix(viewport.getCamera().combined);
		shapeDrawer = new ShapeDrawer(game.batch, textureRegion());
		game.batch.begin();
		
		

		drawBackground();
		raycast();
		drawMap();
		
		
		game.batch.end();
		checkControls();
	}
	public void drawMap() {
		if(miniMap) {
			shapeDrawer.filledRectangle(WIDTH/-2, HEIGHT/-2, WIDTH, HEIGHT, Color.valueOf("00000088"));
			shapeDrawer.filledCircle(player.x,player.y, player.radius);
			shapeDrawer.sector(player.x, player.y, (float)DEPTH, (float)Math.toRadians(player.rotation-(player.fov.angle/2)), (float)Math.toRadians(player.fov.angle), Color.valueOf("ffffff44"), Color.valueOf("ffffff44"));
			//shapeDrawer.polygon(player.fov.getCrop());
			for (Sector<?> sector: sectors) {
				
				shapeDrawer.polygon(sector.getTransformedVertices());
			}
		}
	}

	public void checkControls() {		
		if(Gdx.input.getX() != 0) {
			player.rotate(Gdx.input.getX()*Gdx.graphics.getDeltaTime()*8);
			Gdx.input.setCursorPosition(0, 0);
		}
		final float speed = ((Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)^player.autoSprint)? player.speed*player.sprintRate: player.speed);
		if(Gdx.input.isKeyJustPressed(Keys.CAPS_LOCK)) player.toggleAutoSprint();
		
		if(Gdx.input.isKeyPressed(Keys.A)) player.strafe(speed*Gdx.graphics.getDeltaTime());
		if(Gdx.input.isKeyPressed(Keys.E)) player.strafe(-1f*speed*Gdx.graphics.getDeltaTime());
		if(Gdx.input.isKeyPressed(Keys.COMMA)) player.moveForward(speed*Gdx.graphics.getDeltaTime());
		if(Gdx.input.isKeyPressed(Keys.O)) player.moveForward(-1f*speed*Gdx.graphics.getDeltaTime());
		
		if(Gdx.input.isKeyPressed(Keys.SPACE)) player.altitude += (speed * Gdx.graphics.getDeltaTime());
		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) player.altitude -= (speed * Gdx.graphics.getDeltaTime());

		if (Gdx.input.isKeyJustPressed(Keys.TAB)) miniMap = !miniMap;
		
		if (Gdx.input.isKeyJustPressed(Keys.F11)) toggleFullscreen();
		
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) Gdx.app.exit();
	}
	public void raycast() {
		final HashSet<Sector<?>> liveSectors = getLiveSectors();
		if (!liveSectors.isEmpty()){
			for(int x = 0; x <= WIDTH; x++) {
				player.fov.setRayAngle(x);
				HashSet<WallSlice> workingSet = getWorkingSet(liveSectors);
				if(!workingSet.isEmpty()) {
					drawStack(x, getStack(workingSet));	
				}
			}
		}
	}
	
	public HashSet<WallSlice> getWorkingSet(HashSet<Sector<?>> liveSectors){
		HashSet<WallSlice> output = new HashSet<WallSlice>();
		for (Sector<?> sector: liveSectors) {
			if (sector.lineIntersects(player.fov.getRay(), DEPTH)) {
				//float[] textureMap = sector.getIntersectionTextureMap(player.fov.getRay(), DEPTH);
				//float z = textureMap[0]*player.fov.getDistanceScale();
				//float textureX = textureMap[1];
				Sector.Intersection point = sector.getIntersection(player.fov.getRay(), DEPTH);
				float z = point.getOriginDistance()*player.fov.getDistanceScale();
				WallSlice wall = new WallSlice(getLineHeight(z, sector.getBottomHeight()),getLineHeight(z,sector.getTopHeight()), z, point.getSurfaceDistance());
				if(wall.isDrawable()) {
					output.add(wall);
				}
			}
		}
		
		return output;
	}
	
	public HashSet<Sector<?>> getLiveSectors(){
		HashSet<Sector<?>> liveSectors = new HashSet<Sector<?>>();
		for(Sector<?> sector: this.sectors) {
			if(player.fov.overlaps(sector)) {
				liveSectors.add(sector);
				//System.out.println(sector.getHeight());
			}
		}
		return liveSectors;
	}
	public LinkedList<WallSlice> getStack(HashSet<WallSlice> working){
		final HashSet<WallSlice> raw = new HashSet<WallSlice>(working);
		for (WallSlice reference: raw) {
			if(working.contains(reference)) {
				Iterator<WallSlice> i = working.iterator();
				while (i.hasNext()) {
					WallSlice current = i.next();
					if(reference.obscures(current)) {
						i.remove();
					}
				}
			}
		}
		LinkedList<WallSlice> out = new LinkedList<WallSlice>(working);
		Collections.sort(out, new Comparator<WallSlice>() {
			@Override
			public int compare(WallSlice o1, WallSlice o2) {
				if (o1.z == o2.z) return 0;
				else return (o1.z > o2.z)? 1:-1;
			}
		});
		//System.out.print(out.size()+"\n");
		return out;
	}
	public void drawStack(final int x, final LinkedList<WallSlice> currentStack) {
		Iterator<WallSlice> i = currentStack.descendingIterator();
		while(i.hasNext()) {
			WallSlice wall = i.next();
			shapeDrawer.line(x-(WIDTH/2), wall.getBottomHeight(), x-(WIDTH/2), wall.getTopHeight(), new Color((0.8f/((wall.z+1)/100f))+0.2f, 0, 0, 1));
		}
	}

	public float getLineHeight(float z, float wallHeight) {
		//final float temp = (float)((HEIGHT/2)*(wallHeight-player.getEyeLevel()))/Math.round(Math.min(z+1f,DEPTH));
		float lineHeight = ((float)(((HEIGHT/2)*(wallHeight-player.getEyeLevel()))/Math.min(z+1f,DEPTH)));
		return (float)((Math.abs(lineHeight) <= (HEIGHT/2))? lineHeight : (float)Math.copySign((HEIGHT/2),lineHeight));
		//return (Math.abs(temp) <= (HEIGHT/2))? temp : (float)Math.copySign((HEIGHT/2),temp);
	}

	public void drawBackground() {
		shapeDrawer.filledRectangle(WIDTH/-2, 0, WIDTH, HEIGHT/2, Color.SKY, Color.SKY, Color.WHITE, Color.WHITE);
		shapeDrawer.filledRectangle(WIDTH/-2, 0, WIDTH, HEIGHT/-2, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.SLATE, Color.SLATE);
	}
	public void toggleFullscreen() {
		if(!Gdx.graphics.isFullscreen()) Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		else Gdx.graphics.setWindowedMode(BASE_WIDTH, BASE_HEIGHT);
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		player.updateCamera(viewport.getCamera());
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		texture.dispose();
		//shapeRenderer.dispose();
	}
}
