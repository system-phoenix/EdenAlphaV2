package com.systemphoenix.edenalpha.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.systemphoenix.edenalpha.EdenAlpha;
import com.systemphoenix.edenalpha.Region;
import com.systemphoenix.edenalpha.Scenes.TopHud;

public class GameScreen extends AbsoluteScreen {
    private Region region;

    private String message;

    private Viewport viewport;
    private TopHud topHud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private float pastZoomDistance;

    public GameScreen(EdenAlpha game, Region region) {
        super(game);
        this.region = region;
        this.viewport = new FitViewport(screenWidth, screenHeight, cam);
        this.topHud = new TopHud(game);
        initialize();
    }

    private void initialize() {

        try {
            mapLoader = new TmxMapLoader();
            map = mapLoader.load("levels/test.tmx");
            renderer = new OrthogonalTiledMapRenderer(map);
            Gdx.app.log("Verbose", "Successfully loaded level: " + worldWidth + " x " + worldHeight);
            world = new World(new Vector2(0, 0), true);
            debugRenderer = new Box2DDebugRenderer();

            BodyDef bodyDef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            FixtureDef fixtureDef = new FixtureDef();
            Body body;

            for(MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2 );

                body = world.createBody(bodyDef);

                shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
                fixtureDef.shape = shape;
                body.createFixture(fixtureDef);
            }
        } catch(Exception e) {
            Gdx.app.log("Verbose", "level " + e.getMessage());
        }

        cam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        message = "Camera | X: -- ,     Y: -- ,     Zoom: -- ";
        topHud.setCamStatMessage(message);
        topHud.setMessage(region.getCode() + ": " + region.getName());

    }

    public void update(float delta) {
        cam.update();
//        gameGraphics.setProjectionMatrix(cam.combined);
        renderer.setView(cam);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0.95f, 0.95f, 0.95f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
        debugRenderer.render(world, cam.combined);

        gameGraphics.begin();
        gameGraphics.end();

        gameGraphics.setProjectionMatrix(topHud.getStage().getCamera().combined);
        topHud.getStage().draw();
    }

//  Touch methods
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 touchPos = new Vector3();
        touchPos.set(x, y, 0);
        cam.unproject(touchPos);
        touchPos.x = touchPos.x - (int)touchPos.x  > 0.5 ? (int) touchPos.x + 1 : (int) touchPos.x;
        touchPos.y = touchPos.y - (int)touchPos.y  > 0.5 ? (int) touchPos.y + 1 : (int) touchPos.y;
        topHud.setTouchStatsMessage("Touch | Projected (x, y): (" + x + ", " + y + ") Un-projected (x, y): (" + touchPos.x + ", " + touchPos.y + ")");
        return true;
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
    public boolean pan(float x, float y, float deltaX, float deltaY) {
//        float limit = 5;
//        if(Math.abs(deltaX) < limit) {
//            deltaX = 0;
//        }
//
//        if(Math.abs(deltaY) < limit) {
//            deltaY = 0;
//        }

        Vector3 touchPos = new Vector3();
        touchPos.set(x, y, 0);
        cam.unproject(touchPos);

        cam.position.x -= deltaX;
        cam.position.y += deltaY;
        boundCamera();
        message = "Camera | (x, y): (" + cam.position.x + ", " + cam.position.y + ")\t\t\tZoom: " + cam.zoom;
        topHud.setCamStatMessage(message);
        topHud.setTouchStatsMessage("Touch | (x, y): (" + touchPos.x + ", " + touchPos.y + ")");
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if(pastZoomDistance < distance) {
            cam.zoom -= 0.01f;
        } else if(pastZoomDistance > distance) {
            cam.zoom += 0.01f;
        }
        pastZoomDistance = distance;
        cam.zoom = MathUtils.clamp(cam.zoom, 0.3f, worldWidth/screenWidth);
        boundCamera();

        message = "Camera | (x, y): (" + cam.position.x + ", " + cam.position.y + ")\t\t\tZoom: " + cam.zoom;
        topHud.setCamStatMessage(message);
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

//  Screen methods
    @Override
    public void show() {
        super.show();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        topHud.dispose();
        map.dispose();
        world.dispose();
        debugRenderer.dispose();
    }
}
