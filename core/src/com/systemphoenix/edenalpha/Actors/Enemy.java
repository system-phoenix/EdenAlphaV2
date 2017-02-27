package com.systemphoenix.edenalpha.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Codex.EnemyCodex;
import com.systemphoenix.edenalpha.CollisionBit;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class Enemy extends Sprite implements Disposable {
    protected float size, velX, velY, damage;
    protected float stateTime, speed = 50;
    protected int level = 0, id;
    protected long lastDirectionChange;
    protected boolean spawned = false, moving = true, canDraw = false, canDispose = false, directionSquares[][];
    protected enum Direction {NORTH, SOUTH, EAST, WEST}
    protected Direction direction = Direction.SOUTH, opDirection = Direction.NORTH;

    protected Texture spriteSheet;
    protected Animation<TextureRegion> northAnimation, southAnimation, eastAnimation, westAnimation;

    protected GameScreen gameScreen;

    protected Body body;

    public Enemy(GameScreen screen, int level, float x, float y, float size, int id) {
        this.gameScreen = screen;
        this.id = id;
        this.size = size;
        this.velX = 0;
        this.velY = 0;
        this.level = level;
        this.damage = EnemyCodex.damage[level];
        this.directionSquares = new boolean[screen.getDirectionSquares().length][screen.getDirectionSquares()[0].length];

        for(int i = 0; i < directionSquares.length; i++) {
            for(int j = 0; j < directionSquares[i].length; j++) {
                this.directionSquares[i][j] = screen.getDirectionSquares()[i][j];
            }
        }

        initialize(x, y);
        lastDirectionChange = 0;
    }

    protected void initialize(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x + size / 2, y + size / 2);

        body = gameScreen.getWorld().createBody(bodyDef);


        CircleShape shape = new CircleShape();
        shape.setRadius(size / 2);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = CollisionBit.ENEMY;
        fixtureDef.filter.maskBits = CollisionBit.PATHBOUND | CollisionBit.ENDPOINT | CollisionBit.ENEMYRANGE;

        body.createFixture(fixtureDef).setUserData(this);

        try {
            spriteSheet = new Texture(Gdx.files.internal("enemies/baddies" + (level / 8) + ".png"));
            TextureRegion[][] temp = TextureRegion.split(spriteSheet, 32, 32);
            Array<TextureRegion> textureRegionsNorth = new Array<TextureRegion>();
            Array<TextureRegion> textureRegionsSouth = new Array<TextureRegion>();
            Array<TextureRegion> textureRegionsEast = new Array<TextureRegion>();
            Array<TextureRegion> textureRegionsWest = new Array<TextureRegion>();

            Vector2[] points = EnemyCodex.getSpriteLocation(level);
            for (int i = (int) points[0].y; i <= (int) points[1].y; i++) {
                for (int j = (int) points[0].x; j <= (int) points[1].x; j++) {
                    if (i - (int) points[0].y == 0) {
                        textureRegionsSouth.add(temp[i][j]);
                    } else if (i - (int) points[0].y == 1) {
                        textureRegionsWest.add(temp[i][j]);
                    } else if (i - (int) points[0].y == 2) {
                        textureRegionsEast.add(temp[i][j]);
                    } else {
                        textureRegionsNorth.add(temp[i][j]);
                    }
                }
            }

            float frameDuration = 0.2f;
            northAnimation = new Animation<TextureRegion>(frameDuration, textureRegionsNorth);
            southAnimation = new Animation<TextureRegion>(frameDuration, textureRegionsSouth);
            eastAnimation = new Animation<TextureRegion>(frameDuration, textureRegionsEast);
            westAnimation = new Animation<TextureRegion>(frameDuration, textureRegionsWest);
            canDraw = true;
            Gdx.app.log("Verbose", "Created enemy!");
        } catch (Exception e) {
            Gdx.app.log("Verbose", "Loading baddies: " + e.getMessage());
        }
    }

    public void update(float delta) {
        stateTime += delta;
        if(moving) {
            switch (direction) {
                case EAST:
                    velX = speed;
                    velY = 0;
                    break;
                case WEST:
                    velX = -speed;
                    velY = 0;
                    break;
                case NORTH:
                    velX = 0;
                    velY = speed;
                    break;
                case SOUTH:
                    velX = 0;
                    velY = -speed;
                    break;
            }
            body.setLinearVelocity(velX, velY);
            setPosition(body.getPosition().x - size / 2, body.getPosition().y - size / 2);
//            setRegion(getFrame(delta));
            setRegion(spriteSheet);
        } else {
            velX = velY = 0;
        }
    }

    protected TextureRegion getFrame() {
        TextureRegion temp = southAnimation.getKeyFrame(stateTime, true);
        switch (direction) {
            case NORTH:
                temp = northAnimation.getKeyFrame(stateTime, true);
                break;
            case SOUTH:
                temp = southAnimation.getKeyFrame(stateTime, true);
                break;
            case EAST:
                temp = eastAnimation.getKeyFrame(stateTime, true);
                break;
            case WEST:
                temp = westAnimation.getKeyFrame(stateTime, true);
                break;
        }

        return temp;
    }

    @Override
    public void draw(Batch batch) {
//        super.draw(batch);
        if(spawned) {
            batch.draw(getFrame(), this.getX(), this.getY());
        }
    }

    public void damageForest() {
        gameScreen.getRegion().damageForest(this.damage);
        canDispose = true;
    }

    public void setDirection() {
        if(System.currentTimeMillis() - lastDirectionChange >= 250) {
            this.opDirection = direction;
            boolean loopBreak = false;
            for (int i = -1; i <= 1 && !loopBreak; i++) {
                for (int j = -1; j <= 1 && !loopBreak; j++) {
                    if (i == j || i + j == 0);
                    else {
                        if (directionSquares[(int) body.getPosition().y / 32 + i][(int) body.getPosition().x / 32 + j]) {
                            if (i == -1 && j == 0) {
                                this.direction = Direction.SOUTH;
                            } else if (i == 0 && j == 1) {
                                this.direction = Direction.EAST;
                            } else if (i == 1 && j == 0) {
                                this.direction = Direction.NORTH;
                            } else if (i == 0 && j == -1) {
                                this.direction = Direction.WEST;
                            }
                            directionSquares[(int) body.getPosition().y / 32 + i][(int) body.getPosition().x / 32 + j] = false;
                            if(direction != opDirection) {
                                loopBreak = true;
                            }
                        }
                    }
                }
            }
            lastDirectionChange = System.currentTimeMillis();
        }
    }

    public void spawn() {
        this.moving = true;
        this.spawned = true;
    }

    @Override
    public void dispose() {
        spriteSheet.dispose();
        gameScreen.getWorld().destroyBody(body);
    }

    public boolean canDispose() {
        return canDispose;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }
}
