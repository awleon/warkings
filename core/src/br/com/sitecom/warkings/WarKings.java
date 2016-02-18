package br.com.sitecom.warkings;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class WarKings extends ApplicationAdapter implements GestureListener {

	private SpriteBatch batch;
	private Splash splashScreen;
	private BitmapFont title;
	private WorldMap worldMap;

	private float timePassed = 0;

	//Eh o inicializador
	@Override
	public void create() {

		batch = new SpriteBatch();

		splashScreen = new Splash();
		worldMap = new WorldMap();

		//TODO mover para o splash
		title = new BitmapFont();
		title.setColor(Color.ORANGE);
		title.getData().scale(5); // padrao eh 15px


		InputMultiplexer im = new InputMultiplexer();
		GestureDetector gd  = new GestureDetector(this);
		im.addProcessor(gd);
		im.addProcessor(worldMap.getStage());
		Gdx.input.setInputProcessor(gd);
	}

	//É como uma thread, continuamente chamado (30-80x/s)
	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0); //black. white is 1111
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		timePassed += Gdx.graphics.getDeltaTime();

		if (timePassed < 3) {
			batch.begin();
			splashScreen.render(batch);
			title.draw(batch, "War Kings", 100, 400);
			batch.end();
		} else {
//			testButtonSprite.setSize(100, 100);

			batch.setProjectionMatrix(worldMap.getCamera().combined);
			batch.begin();
			worldMap.render(batch);
			batch.end();
		}
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		Boolean hitSomeActor = false;

		float yCorrected = Math.abs(y - worldMap.getSizeY()); //Y is inverted in GestureListener
		Gdx.app.log("PAN", "click x:" + x + "/ yCorr:" +yCorrected + "/ y:" +y);

		for (Actor tempActor: worldMap.getStage().getActors() ) {
			Gdx.app.log("PAN", "ator:" + tempActor.getName());

			if(tempActor.hit(x, yCorrected, true) != null) {
				tempActor.act(timePassed);
				hitSomeActor = true;
			}
		}

		if (!hitSomeActor) {
			worldMap.getCamera().translate(-deltaX, deltaY);
			worldMap.getCamera().update();
		}
		return false;
	}


	@Override
	public boolean zoom(float initialDistance, float distance) {

		//Calculate pinch to zoom
		float ratio = initialDistance / distance;

		Gdx.app.log("ZOOM", "ratio:" + ratio);

		//Clamp range and set zoom
		worldMap.getCamera().zoom = MathUtils.clamp(ratio * 1.0f, 0.1f, 1.0f);

		return false;
	}

	//Encerrador de render.
	@Override
	public void dispose() {
		batch.dispose();
		title.dispose();
		splashScreen.dispose();
		worldMap.dispose();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}
}
