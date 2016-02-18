package br.com.sitecom.warkings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import br.com.sitecom.warkings.br.com.sitecom.warkings.utils.StageUtils;

public class WorldMap {

    private int sizeX;
    private int sizeY;
    private int tileX = 64;
    private int tileY = 32;
    private int numTilesX = 30;
    private int numTilesY = 30;

    private OrthographicCamera fullMapCamera;
    private TiledMap worldMap;
    private IsometricTiledMapRenderer renderer;
    private Stage stage;

    public class Button extends Actor {
        Texture texture = new Texture("faixa_com_pena.png");
        public boolean clicked = false;
        private float posX;
        private float posY;
        private int sizeAdaptiveX = 46; //%
        private int sizeAdaptiveY; //proportional to X definition

        public Button (float posX, float posY) {
            this.posX = posX;
            this.posY = posY;
        }

        @Override
        public void draw(Batch batch, float alpha) {
            setName("barrapena");
            int sizeX = Gdx.graphics.getWidth();
            int sizeY = Gdx.graphics.getHeight();
            setWidth(StageUtils.getAdaptiveWidth(this.sizeAdaptiveX));
            setHeight(StageUtils.percentualToPixel(this.sizeAdaptiveX,texture.getHeight()));

            setTouchable(Touchable.enabled);
            if (!clicked) batch.draw(texture,posX,posY);
        }

        @Override
        public Actor hit (float x, float y, boolean touchable) {
//            Gdx.app.log("PAN", "actor "+getName() + " x-orig:"+getOriginX() + "/ x-width:"+getWidth() +
//                    "/ y-orig:" + getOriginY() + "/ y-height:" + getHeight());
            if (x >= posX && x <= getWidth() && y >= posY && y <= getHeight()){
                return this;
            } else
                return null;
        }

        @Override
        public void act(float delta) {
            clicked = true;
        }
    }


    public class ElementArea extends Actor {
        Texture texture = new Texture("faixa_superior.png");
        private float posX;
        private float posY;
        private int sizeAdaptiveX = 100; //%
        private int sizeAdaptiveY; //proportional to X definition

        public ElementArea (float posX, float posY) {
            this.posX = posX;
            this.posY = posY;
        }

        @Override
        public void draw(Batch batch, float alpha){
            setName("barrasuperior");
            int sizeX = Gdx.graphics.getWidth();
            int sizeY = Gdx.graphics.getHeight();
            this.setWidth(StageUtils.getAdaptiveWidth(this.sizeAdaptiveX));
            this.setHeight(StageUtils.percentualToPixel(this.sizeAdaptiveX, texture.getHeight()));

            setTouchable(Touchable.enabled);
            batch.draw(texture, posX, posY);
        }

        @Override
        public Actor hit (float x, float y, boolean touchable) {
//            Gdx.app.log("PAN", "actor "+getName() + " x-orig:"+getOriginX() + "/ x-width:"+getWidth() +
//                    "/ y-orig:" + getOriginY() + "/ y-height:" + getHeight());

            if (x >= getOriginX() && x < getWidth() && y >= getOriginY() && y < getHeight()){
                return this;
            } else
                return null;
        }
    }

    public WorldMap() {
        sizeX = Gdx.graphics.getWidth();
        sizeY = Gdx.graphics.getHeight();
        TiledMapTileLayer.Cell cell;

        worldMap = new TiledMap();
        MapLayers layers = worldMap.getLayers();

        //Building the base
        Texture grassTexture = new Texture("gramado_grande.png");
        TextureRegion[][] splitTilesBg = TextureRegion.split(grassTexture, 1920, 960);
        TiledMapTileLayer baseLayer = new TiledMapTileLayer(numTilesX, numTilesY, tileX, tileY);
        cell = new Cell();
        cell.setTile(new StaticTiledMapTile(splitTilesBg[0][0]));
        cell.getTile().setOffsetY(-464); //Realign to middle
        baseLayer.setCell(0, 0, cell);
        layers.add(baseLayer);

        //Vegetation
        Texture tilesTexture = new Texture("terrain_v1.png");
        TextureRegion[][] splitTilesFiller = TextureRegion.split(tilesTexture, 64, 64);
        TiledMapTileLayer obstaclesLayer = new TiledMapTileLayer(numTilesX, numTilesY, tileX, tileY);

        for (int x = 0; x < 50; x++)
            for (int y = 0; y < 50; y++) {
                if (Math.random() > 0.8) {
                    cell = new Cell();
                    cell.setTile(new StaticTiledMapTile(splitTilesFiller[0][1]));
                    obstaclesLayer.setCell(x, y, cell);
                }
            }

        layers.add(obstaclesLayer);

        renderer = new IsometricTiledMapRenderer(worldMap);


        fullMapCamera = new OrthographicCamera(sizeX, sizeY);
        fullMapCamera.position.set((tileX * numTilesX) / 2f, 0, 0);
        fullMapCamera.update();


        stage = new Stage();

        Button myActor = new Button(0,0); //position down-up
        myActor.setTouchable(Touchable.enabled);
        stage.addActor(myActor);

        ElementArea upperRibbon = new ElementArea(0,sizeY-75); //position down-up
        upperRibbon.setTouchable(Touchable.enabled);
        stage.addActor(upperRibbon);
    }

    public OrthographicCamera getCamera () {
        return this.fullMapCamera;
    }

    public Stage getStage () {
        return this.stage;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void render(SpriteBatch batch) {
        renderer.setView(fullMapCamera);
        renderer.render();

        stage.draw();
    }

    public void dispose() {
        worldMap.dispose();
        renderer.dispose();
        stage.dispose();
    }
}
