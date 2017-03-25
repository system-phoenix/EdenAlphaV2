package com.systemphoenix.edenalpha.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Codex.PlantCodex;
import com.systemphoenix.edenalpha.CollisionBit;
import com.systemphoenix.edenalpha.Screens.GameScreen;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

public class Plant extends Actor implements InputProcessor, Disposable {
    private static Plant selectedPlant = null;
    private static boolean selectAllPlants = false;

    private GameScreen gameScreen;

    private Vector2 coord;
    private int pastScreenX, pastScreenY;

    private Sprite sprite, rangeSprite, effectiveRangeSprite, redLifeBar, greenLifeBar;
    private Stage gameStage;

    private Body body;
    private Array<Enemy> targets;
    private Array<Bullet> bullets;
    private Array<Pulse> pulses;
    private Array<Slash> slashes;
    private Array<Root> roots;


    private boolean selected, growing;

    private Vector2 damage;
    private long attackSpeed, lastAttackTime, growthTimer;
    private int plantIndex, upgradeIndex = 0;
    private float size = 32f, hp = 0f, targetHp = 50f, growthRate = 1, seedRate;
    private boolean hasTarget = false;

    private Random rand = new Random();

    public Plant(GameScreen gameScreen, Stage gameStage, TextureRegion sprite, int plantIndex, float x, float y) {
        this.plantIndex = plantIndex;
        this.gameScreen = gameScreen;
        this.gameStage = gameStage;
        this.sprite = new Sprite(sprite);
        this.damage = PlantCodex.dmgStats[PlantCodex.DMG[plantIndex]];
        this.attackSpeed = PlantCodex.asStats[PlantCodex.AS[plantIndex]];
        this.targetHp = PlantCodex.hpStats[PlantCodex.maxHP[plantIndex]];
        this.growthRate = targetHp / (PlantCodex.growthTime[plantIndex] * 5);
        this.seedRate = PlantCodex.seedRateStats[PlantCodex.seedProduction[plantIndex]];

        this.redLifeBar = new Sprite(new Texture(Gdx.files.internal("utilities/redLife.png")));
        this.redLifeBar.setBounds(x + size / 2, y + size / 4 + size / 2, size, size / 16);
        this.greenLifeBar = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
        this.greenLifeBar.setBounds(x + size / 2, y + size / 4 + size / 2, 1f, size / 16);

        this.setBounds(x, y, size * 2, size * 2);

        initialize();
        this.targets = new Array<Enemy>();
        this.bullets = new Array<Bullet>();
        this.pulses = new Array<Pulse>();
        this.slashes = new Array<Slash>();
        this.roots = new Array<Root>();

        gameStage.addActor(this);
        lastAttackTime = System.currentTimeMillis();
        growthTimer = System.currentTimeMillis();
        growing = true;
    }

    public void initialize() {
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(this.getX() + size, this.getY() + size);

        body = gameScreen.getWorld().createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        float actualRange = PlantCodex.rangeStats[PlantCodex.range[plantIndex]];
        float computedRange = size + 32f * actualRange;
        float effectiveRange = PlantCodex.effectiveRange[plantIndex];
        circleShape.setRadius(computedRange);

        rangeSprite = new Sprite(new Texture(Gdx.files.internal("plantRange/rangeSprite.png")));
        effectiveRangeSprite = new Sprite(new Texture(Gdx.files.internal("plantRange/effectiveRangeSprite.png")));

        sprite.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        rangeSprite.setBounds(this.getX() - (32f * actualRange), this.getY() - (32f * actualRange), (32f * actualRange * 2) + this.getWidth(), (32f * actualRange * 2) + this.getHeight());
        effectiveRangeSprite.setBounds(this.getX() - (32f * (effectiveRange)), this.getY() - (32f * (effectiveRange)), (32f * (effectiveRange) * 2) + this.getWidth(), (32f * (effectiveRange) * 2) + this.getHeight());

        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = CollisionBit.ENEMYRANGE;
        fixtureDef.filter.maskBits = CollisionBit.ENEMY;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef).setUserData(this);

    }

    public void acquireTarget(Enemy enemy) {
        targets.add(enemy);
    }

    public void removeTarget(Enemy enemy) {
        targets.removeValue(enemy, true);
    }

//    public void upgrade() {
//        upgradeIndex++;
//        switch (plantIndex) {
//            case 0:
//            case 4:
//            case 5:
//            case 6:
//            case 7:
//            case 8:
//                targetHp = PlantCodex.hpStats[PlantCodex.maxHP[plantIndex] + upgradeIndex];
//                attackSpeed = PlantCodex.asStats[PlantCodex.AS[plantIndex] + upgradeIndex];
//                damage = PlantCodex.dmgStats[PlantCodex.DMG[plantIndex] + upgradeIndex];
//                break;
//            case 1:
//
//        }
//    }

    public void update() {
        if(!growing) {
            if(targets.size > 0 && System.currentTimeMillis() - lastAttackTime >= attackSpeed) {
                try{
                    int dmgRange = (int)(damage.y - damage.x);
    //                targets.get(0).receiveDamage(rand.nextInt(dmgRange) + (int) damage.x);
                    if(PlantCodex.projectileSize[plantIndex] > 0) {
                        bullets.add(new Bullet(gameScreen, this, targets.get(0), rand.nextInt(dmgRange) + (int) damage.x));
                    } else {
                        switch (plantIndex) {
                            case 4:
                                pulses.add(new Pulse(gameScreen, this, rand.nextInt(dmgRange) + (int) damage.x, targets));
                                break;
                            case 5:
                                roots.add(new Root(gameScreen, this, rand.nextInt(dmgRange) + (int) damage.x, targets, false));
                                break;
                            case 11:
                                slashes.add(new Slash(gameScreen, this, rand.nextInt(dmgRange) + (int) damage.x, targets));
                                break;
                            case 13:
                                if(!hasTarget) {
                                    Gdx.app.log("verbose","root");
                                    hasTarget = true;
                                    roots.add(new Root(gameScreen, this, 0, targets, true));
                                }
                                break;
                        }
                    }
                    lastAttackTime = System.currentTimeMillis();
                } catch (Exception e) {
//                    Gdx.app.log("Verbose", "Error in plant attack: " + e.getMessage());
//                    Log.e("Eden", "exception", e);
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    Gdx.app.log("Verbose", "Error in plant attack: " + sw.toString());
                }
            }

            for(int i = 0; i < targets.size; i++) {
                if(targets.get(i).getLife() <= 0) {
                    targets.removeIndex(i);
                }
            }
        } else {
            if(System.currentTimeMillis() - growthTimer >= 200) {
                hp += growthRate;
                float multiplier = hp / targetHp;
                greenLifeBar.setBounds(greenLifeBar.getX(), greenLifeBar.getY(), size * multiplier, size / 16);
                if(hp >= targetHp) {
                    lastAttackTime = System.currentTimeMillis();
                    growing = false;
                    redLifeBar.setY(redLifeBar.getY() - size / 2);
                    greenLifeBar.setY(greenLifeBar.getY() - size / 2);
                }
                growthTimer = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void draw(Batch batch, float alpha) {
        update();
        if(selected || selectAllPlants) {
            float a = 0.5f;
            rangeSprite.draw(batch);
            effectiveRangeSprite.setColor(effectiveRangeSprite.getColor().r, effectiveRangeSprite.getColor().g, effectiveRangeSprite.getColor().b, a);
            effectiveRangeSprite.draw(batch);
        }
        if(!growing) {
            sprite.draw(batch);
            for(int i = 0; i < bullets.size; i++) {
                bullets.get(i).render(batch);
            }

            for(int i = 0; i < pulses.size; i++) {
                pulses.get(i).render(batch, Gdx.graphics.getDeltaTime());
            }
            
            for(int i = 0; i < slashes.size; i++) {
                slashes.get(i).render(batch, Gdx.graphics.getDeltaTime());
            }

            for(int i = 0; i < roots.size; i++) {
                roots.get(i).render(batch);
            }

            for(int i = 0; i < bullets.size; i++) {
                if(bullets.get(i).canDispose()) {
                    bullets.get(i).dispose();
                    bullets.removeIndex(i);
                }
            }

            for(int i = 0; i < pulses.size; i++) {
                if(pulses.get(i).canDispose()) {
                    pulses.get(i).dispose();
                    pulses.removeIndex(i);
                }
            }

            for(int i = 0; i < slashes.size; i++) {
                if(slashes.get(i).canDispose()) {
                    slashes.get(i).dispose();
                    slashes.removeIndex(i);
                }
            }

            for(int i = 0; i < roots.size; i++) {
                if(roots.get(i).canDispose()) {
                    roots.get(i).dispose();
                    roots.removeIndex(i);
                }
            }
        }
        if(growing || selected) {
            redLifeBar.draw(batch);
            greenLifeBar.draw(batch);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if((pastScreenX == -1 && pastScreenY == -1) || !(pastScreenX == screenX && pastScreenY == screenY)) {
            coord = gameStage.screenToStageCoordinates(new Vector2((float)screenX, (float)screenY));
            pastScreenX = screenX;
            pastScreenY = screenY;
        }
        Actor hitActor = gameStage.hit(coord.x, coord.y, true);

        if(hitActor == this) {
            selected = !selected;
            if(selectedPlant != this && selectedPlant != null) {
                selectedPlant.selected = false;
                selectedPlant = this;
            } else {
                selectedPlant = this;
            }
            gameScreen.resetHud();
            gameScreen.getGameHud().setDrawable(0);
            gameScreen.getGameHud().setCanDraw(true);
            selectAllPlants = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if((pastScreenX == -1 && pastScreenY == -1) || !(pastScreenX == screenX && pastScreenY == screenY)) {
            coord = gameStage.screenToStageCoordinates(new Vector2((float)screenX, (float)screenY));
            pastScreenX = screenX;
            pastScreenY = screenY;
        }
        Actor hitActor = gameStage.hit(coord.x, coord.y, true);
        return hitActor == this;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void dispose() {
//        sprite.getTexture().dispose();
        rangeSprite.getTexture().dispose();
        effectiveRangeSprite.getTexture().dispose();
        gameScreen.getWorld().destroyBody(body);

        redLifeBar.getTexture().dispose();
        greenLifeBar.getTexture().dispose();

        for(int i = 0; i < bullets.size; i++) {
            bullets.get(i).dispose();
            bullets.removeIndex(i);
        }

        for(int i = 0; i < pulses.size; i++) {
            pulses.get(i).dispose();
            pulses.removeIndex(i);
        }

        for(int i = 0; i < slashes.size; i++) {
            slashes.get(i).dispose();
            slashes.removeIndex(i);
        }

        for(int i = 0; i < roots.size; i++) {
            roots.get(i).dispose();
            roots.removeIndex(i);
        }
    }

    public static void nullSelectedPlant() {
        if(selectedPlant != null) {
            selectedPlant.selected = false;
            selectedPlant = null;
        }
    }

    public static void setSelectAllPlants(boolean selectPlants) {
        selectAllPlants = selectPlants;
    }

    public int getPlantIndex() {
        return plantIndex;
    }

    public float getSeedRate() {
        return seedRate;
    }

    public void setHasTarget(boolean hasTarget) {
        this.hasTarget = hasTarget;
    }

    public static Plant getSelectedPlant() {
        return selectedPlant;
    }
}
