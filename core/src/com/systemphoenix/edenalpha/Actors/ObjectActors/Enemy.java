package com.systemphoenix.edenalpha.Actors.ObjectActors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Codex.EnemyCodex;
import com.systemphoenix.edenalpha.WindowUtils.CollisionBit;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class Enemy extends Sprite implements Disposable {
    protected float size, velX, velY, damage, pastX, pastY;
    protected float life, maxLife, stateTime, speed = 30, maxSpeed, waterDrop, stackSlowRate = 0, damageCounter = 0, damagePart;
    protected int level = 0, id, pathCount = 0;
    protected long lastDirectionChange, deathTimer, damageTimer, slowTimer, attackSpeed, lastAttackTime, receiveDamageTick;
    protected boolean spawned = false, moving = false, drawable = false, disposable = false, directionSquares[][], drawHpBar, slowed = false, dead, stunned, attacking, receivingDamage, spirit = false;
    protected enum Direction {NORTH, SOUTH, EAST, WEST}
    protected Direction direction = Direction.SOUTH, opDirection = Direction.NORTH;

    protected Texture spriteSheet;
    protected Sprite greenLifeBar, redLifeBar;
    protected Animation<TextureRegion> northAnimation, southAnimation, eastAnimation, westAnimation;

    protected GameScreen gameScreen;
    protected Rectangle hitBox;

    protected Body body;

    protected Plant plantTarget;

    public Enemy(GameScreen screen, int waveIndex, int level, float x, float y, float size, int id) {
        this.gameScreen = screen;
        this.id = id;
        this.size = size;
        this.velX = 0;
        this.velY = 0;
        this.level = level;
        this.damage = EnemyCodex.damage[level];
        this.speed = this.maxSpeed = EnemyCodex.speed[level];
        this.life = this.maxLife = EnemyCodex.getHP(level, waveIndex, gameScreen.getRegion().getMapIndex());
        this.waterDrop = EnemyCodex.waterDrop[level];
        this.attackSpeed = EnemyCodex.attackSpeed[level];
        this.directionSquares = new boolean[screen.getDirectionSquares().length][screen.getDirectionSquares()[0].length];
        this.setBounds(x, y, 32f, 32f);

        for(int i = 0; i < directionSquares.length; i++) {
            for(int j = 0; j < directionSquares[i].length; j++) {
                this.directionSquares[i][j] = screen.getDirectionSquares()[i][j];
            }
        }

        initialize(x, y);
        lastAttackTime = gameScreen.getCentralTimer();
        lastDirectionChange = 0;

        hitBox = new Rectangle(this.getX() + this.getWidth() / 4, this.getY() + this.getHeight() / 4, this.getWidth() / 2, this.getHeight() / 2);

        greenLifeBar = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
        greenLifeBar.setBounds(this.getX(), this.getY(), 32f, 2f);

        redLifeBar = new Sprite(new Texture(Gdx.files.internal("utilities/redLife.png")));
        redLifeBar.setBounds(this.getX(), this.getY(), 32f, 2f);
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
        fixtureDef.filter.maskBits = CollisionBit.PATH_BOUND | CollisionBit.ENDPOINT | CollisionBit.ENEMY_RANGE | CollisionBit.PLANT | CollisionBit.PULSE | CollisionBit.ARENA;

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
            drawable = true;
        } catch (Exception e) {
            Gdx.app.log("Verbose", "Loading baddies: " + e.getMessage());
        }
    }

    public void update(float delta) {
        if((dead && gameScreen.getCentralTimer() - deathTimer >= 100) || life <= 0) {
            disposable = true;
            gameScreen.updateWater(waterDrop);
        }
        stateTime += delta;
        if(moving) {
            int x = getX() - (int)getX() < 0.5f ? (int)getX() : (int)getX() + 1;
            int y = getY() - (int)getY() < 0.5f ? (int)getY() : (int)getY() + 1;
            if(x != pastX) {
                if(x % 32 == 0) {
                    pathCount++;
                    pastX = x;
                }
            } else if(y != pastY) {
                if(y % 32 == 0) {
                    pathCount++;
                    pastY = y;
                }
            }
            if(!attacking && (slowed && gameScreen.getCentralTimer() - slowTimer > 500)) {
                speed = maxSpeed;
                slowed = false;
            } else if(attacking) {
                if(gameScreen.getCentralTimer() - lastAttackTime > attackSpeed) {
                    plantTarget.receiveDamage(damage);
                    lastAttackTime = gameScreen.getCentralTimer();
                }
            }
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
//            Gdx.app.log("Verbose", "moveSpeed: " + speed + ", velX, velY:" + velX + ", " + velY);
            body.setLinearVelocity(velX, velY);
            setPosition(body.getPosition().x - size / 2, body.getPosition().y - size / 2);
            float multiplier = life >= 0 ? ((float)life / (float)maxLife) : 0;
            greenLifeBar.setBounds(this.getX(), this.getY(), 32f * multiplier, 2f);
            redLifeBar.setBounds(this.getX(), this.getY(), 32f, 2f);
            if(drawHpBar) {
                if(gameScreen.getCentralTimer() - damageTimer >= 5000) {
                    drawHpBar = false;
                }
            }
            hitBox.x = this.getX();
            hitBox.y = this.getY();
//            setRegion(getFrame(delta));
            setRegion(spriteSheet);
        } else {
            velX = velY = 0;
        }

//        if(receivingDamage) {
//            if(gameScreen.getCentralTimer() - receiveDamageTick > 5) {
//                receiveDamageTick = gameScreen.getCentralTimer();
//                damagePart = damageCounter / 2f;
//                damageCounter -= damagePart;
//                receiveDamage();
//                if(damageCounter <= 0.49) {
//                    damageCounter = 0;
//                    receivingDamage = false;
//                }
//            }
//        }
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
            if(drawHpBar) {
                redLifeBar.draw(batch);
                greenLifeBar.draw(batch);
            }
        }
    }

    public void decrementPathCount() {
        pathCount--;
    }

    public void damageForest() {
        gameScreen.damageForest(1);
        disposable = true;
    }

    public void receiveDamage(float damage) {
        if(spawned) {
            if(!drawHpBar) {
                damageCounter = 0;
            }
            damageCounter += damage;
            life -= damage;
            drawHpBar = true;
            damageTimer = gameScreen.getCentralTimer();
            if(life <= 0.49) {
                dead = true;
                deathTimer = gameScreen.getCentralTimer();
            }
        }
    }

    public void receiveDamage() {
        life -= damagePart;
    }

    public void stackSlow(float percentage) {
        if(spawned) {
            stackSlowRate += percentage;
            if(speed > 10) {
                speed -= maxSpeed * stackSlowRate;
            } else {
                speed = 10;
            }
            slowed = true;
            slowTimer = gameScreen.getCentralTimer();
        }
    }

    public void slow(float percentage) {
        if(spawned) {
            if(!slowed) {
                speed -= maxSpeed * percentage;
            }
            slowed = true;
            slowTimer = gameScreen.getCentralTimer();
        }
    }

    public void stun() {
        if(!stunned) {
            speed -= maxSpeed;
        }
        stunned = true;
    }

    public void attack(Plant plantTarget) {
        if(!attacking) {
            speed -= maxSpeed;
            this.plantTarget = plantTarget;
        }

        attacking = true;
    }

    public void removeStun() {
        stunned = false;
        speed = maxSpeed;
    }

    public void stopAttacking() {
        attacking = false;
        speed = maxSpeed;
    }

    public void setDirection() {
        if(gameScreen.getCentralTimer() - lastDirectionChange >= 100) {
            this.opDirection = direction;
            boolean loopBreak = false;

//            for(int i = -1; i <= 1; i++) {
//                Gdx.app.log("Verbose", "[" + directionSquares[(int) body.getPosition().y / 32 + i][(int) body.getPosition().x / 32 + -1] + "]\t[" + directionSquares[(int) body.getPosition().y / 32 + i][(int) body.getPosition().x / 32] + "]\t[" + directionSquares[(int) body.getPosition().y / 32 + i][(int) body.getPosition().x / 32 + 1] + "]");
//            }

            for (int i = -1; i <= 1 && !loopBreak; i++) {
                for (int j = -1; j <= 1 && !loopBreak; j++) {
                    if((i != j && i + j != 0)) {
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
            lastDirectionChange = gameScreen.getCentralTimer();
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

    public boolean isDisposable() {
        return disposable;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean isSpirit() {
        return spirit;
    }

    public boolean isDead() {
        return dead;
    }

    public float getLife() {
        return life;
    }

    public int getLevel() {
        return level;
    }

    public int getPathCount() {
        return pathCount;
    }

    public Body getBody() {
        return body;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void setPathCount(int pathCount) {
        this.pathCount = pathCount;
    }
}
