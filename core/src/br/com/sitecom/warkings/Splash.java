package br.com.sitecom.warkings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Splash {

    private int sizeX, sizeY;

    private Texture splashBG;
    private Sprite splashSprite;


    public Splash() {
        sizeX = Gdx.graphics.getWidth();
        sizeY = Gdx.graphics.getHeight();
        splashBG = new Texture("comemoration_50pc.jpg");
        splashSprite = new Sprite(splashBG);
        splashSprite.setSize(sizeX, sizeY);
    }

    public void render(SpriteBatch batch) {
        splashSprite.draw(batch); //e8ede9
    }

    public void dispose() {
        splashBG.dispose();
    }
}
