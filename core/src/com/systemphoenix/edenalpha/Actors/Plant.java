package com.systemphoenix.edenalpha.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Codex.PlantCodex;
import com.systemphoenix.edenalpha.CollisionBit;
import com.systemphoenix.edenalpha.PlantSquares.PlantSquare;
import com.systemphoenix.edenalpha.Screens.GameScreen;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

public class Plant extends Actor implements Disposable {
    private static Plant selectedPlant = null;
    private static boolean selectAllPlants = false;

    private GameScreen gameScreen;

    private Sound sound;

    private Sprite sprite, rangeSprite = null, effectiveRangeSprite = null, redLifeBar, greenLifeBar, downgradeSprite, upgradeSprite;

    private Body body;
    private Array<Enemy> targets, attackers;
    private Array<Bullet> bullets;
    private Array<Pulse> pulses;
    private Array<Slash> slashes;
    private Array<Root> roots;

    private PlantCollision plantCollision;

    private boolean selected, growing, canDispose = false, hit = false, damaged = false;

    private Vector2 damage;
    private long attackSpeed, lastAttackTime, growthTimer, lastHitTime, lastAcquisition;
    private int plantIndex, upgradeIndex = 0, downGradeIndex = 0;
    private float size = 32f, hp = 0f, targetHp = 50f, growthRate = 1, seedRate, effectiveRange, upgradeCostSunlight, upgradeCostWater, actualRange, sunlightAccumulation, sunlight;
    private boolean hasTarget = false, upgradable = false;

    private Random rand = new Random();

    public Plant(GameScreen gameScreen, Stage gameStage, TextureRegion sprite, PlantSquare square, int plantIndex, float x, float y, float sunlightAccumulation, Sound sound) {
        this.plantIndex = plantIndex;
        this.gameScreen = gameScreen;
        this.sprite = new Sprite(sprite);
        this.damage = PlantCodex.dmgStats[PlantCodex.DMG[plantIndex]];
        this.attackSpeed = PlantCodex.asStats[PlantCodex.AS[plantIndex]];
        this.targetHp = PlantCodex.hpStats[PlantCodex.maxHP[plantIndex]];
        this.growthRate = targetHp / (PlantCodex.growthTime[plantIndex] * 5);
        this.seedRate = PlantCodex.seedRateStats[PlantCodex.seedProduction[plantIndex]];

        this.sunlightAccumulation = sunlightAccumulation;

        this.upgradeCostSunlight = PlantCodex.cost[plantIndex];
        this.upgradeCostWater = PlantCodex.cost[plantIndex] / 2;

        this.sound = sound;

        this.redLifeBar = new Sprite(new Texture(Gdx.files.internal("utilities/redLife.png")));
        this.redLifeBar.setBounds(x + size / 2, y + size / 4 + size / 2, size, size / 16);
        this.greenLifeBar = new Sprite(new Texture(Gdx.files.internal("utilities/greenLife.png")));
        this.greenLifeBar.setBounds(x + size / 2, y + size / 4 + size / 2, 1f, size / 16);

        this.downgradeSprite = new Sprite(new Texture(Gdx.files.internal("misc/rank0.png")));
        this.downgradeSprite.setBounds(x, y, size * 2, size * 2);
        this.upgradeSprite = new Sprite(new Texture(Gdx.files.internal("misc/rank0.png")));
        this.upgradeSprite.setBounds(x, y, size * 2, size * 2);

        this.setBounds(x, y, size * 2, size * 2);
        this.setTouchable(Touchable.enabled);
        this.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return Plant.this.triggerAction();
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }
        });

        rangeSprite = new Sprite(new Texture(Gdx.files.internal("plantRange/rangeSprite.png")));
        effectiveRangeSprite = new Sprite(new Texture(Gdx.files.internal("plantRange/effectiveRangeSprite.png")));
        initialize();
        this.targets = new Array<Enemy>();
        this.attackers = new Array<Enemy>();
        this.bullets = new Array<Bullet>();
        this.pulses = new Array<Pulse>();
        this.slashes = new Array<Slash>();
        this.roots = new Array<Root>();

        gameStage.addActor(this);
        lastAttackTime = System.currentTimeMillis();
        lastAcquisition = System.currentTimeMillis();
        growthTimer = System.currentTimeMillis();
        growing = true;

        if((square.getType() & PlantCodex.typeBit[plantIndex]) != 0) {
            downGrade(-1);
        }
        plantCollision = new PlantCollision(gameScreen, this, size);
    }

    public void initialize() {
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(this.getX() + size, this.getY() + size);

        body = gameScreen.getWorld().createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        actualRange = PlantCodex.rangeStats[PlantCodex.range[plantIndex] + downGradeIndex + upgradeIndex];
        float computedRange = size + 32f * actualRange;
        effectiveRange = PlantCodex.effectiveRange[plantIndex];
        circleShape.setRadius(computedRange);

        sprite.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        rangeSprite.setBounds(this.getX() - (32f * actualRange), this.getY() - (32f * actualRange), (32f * actualRange * 2) + this.getWidth(), (32f * actualRange * 2) + this.getHeight());
        effectiveRangeSprite.setBounds(this.getX() - (32f * (effectiveRange)), this.getY() - (32f * (effectiveRange)), (32f * (effectiveRange) * 2) + this.getWidth(), (32f * (effectiveRange) * 2) + this.getHeight());

        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = CollisionBit.ENEMYRANGE;
        fixtureDef.filter.maskBits = CollisionBit.ENEMY;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef).setUserData(this);

        circleShape = new CircleShape();
        circleShape.setRadius(size + 32f * effectiveRange);

        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = CollisionBit.EFFECTIVERANGE;
        fixtureDef.filter.maskBits = CollisionBit.PLANT | CollisionBit.ENEMY;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef).setUserData(this);
    }

    public boolean triggerAction() {
        selected = !selected;
        if(selected) {
            if(selectedPlant != this && selectedPlant != null) {
                selectedPlant.selected = false;
                selectedPlant = this;
            } else {
                selectedPlant = this;
            }
            gameScreen.resetHud();
            gameScreen.getGameHud().setDrawable(0);
            gameScreen.getGameHud().setPlantStatsData();
            gameScreen.getGameHud().setCanDraw(true);
            selectAllPlants = false;
        } else {
            nullSelectedPlant();
        }
        return true;
    }

    public void acquireTarget(Enemy enemy) {
        targets.add(enemy);
    }

    public void acquireAttacker(Enemy enemy) {
        enemy.attack(this);
        attackers.add(enemy);
    }

    public void removeTarget(Enemy enemy) {
        targets.removeValue(enemy, true);
    }

    public void removeAttacker(Enemy enemy) {
        attackers.get(attackers.indexOf(enemy, true)).stopAttacking();
        attackers.removeValue(enemy, true);
    }

    public void upgrade() {
        if(upgradeIndex < 3) {
            upgradeIndex++;
            sunlight -= upgradeCostSunlight;
            upgradeCostSunlight *= 2;
            gameScreen.updateWater(-upgradeCostWater);
            upgradeCostWater *= 2;
            upgradeSprite.getTexture().dispose();
            upgradeSprite = new Sprite(new Texture(Gdx.files.internal("misc/uprank" + upgradeIndex + ".png")));
            upgradeSprite.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
            switch (plantIndex) {
                case 0:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                    if(PlantCodex.maxHP[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        targetHp = PlantCodex.hpStats[PlantCodex.maxHP[plantIndex] + upgradeIndex + downGradeIndex];
                        damaged = true;
                    }
                    if(PlantCodex.AS[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        attackSpeed = PlantCodex.asStats[PlantCodex.AS[plantIndex] + upgradeIndex + downGradeIndex];
                    }
                    if(PlantCodex.DMG[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        damage = PlantCodex.dmgStats[PlantCodex.DMG[plantIndex] + upgradeIndex + downGradeIndex];
                    }
                    break;
                case 1:
                    if(PlantCodex.maxHP[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        targetHp = PlantCodex.hpStats[PlantCodex.maxHP[plantIndex] + upgradeIndex + downGradeIndex];
                        damaged = true;
                    }
                    if(PlantCodex.AS[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        attackSpeed = PlantCodex.asStats[PlantCodex.AS[plantIndex] + upgradeIndex + downGradeIndex];
                    }
                    if(PlantCodex.seedProduction[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        seedRate = PlantCodex.seedRateStats[PlantCodex.seedProduction[plantIndex] + upgradeIndex + downGradeIndex];
                        gameScreen.updateSeedRate();
                    }
                    break;
                case 2:
                case 12:
                    if(PlantCodex.maxHP[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        targetHp = PlantCodex.hpStats[PlantCodex.maxHP[plantIndex] + upgradeIndex + downGradeIndex];
                        damaged = true;
                    }
                    if(PlantCodex.DMG[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        damage = PlantCodex.dmgStats[PlantCodex.DMG[plantIndex] + upgradeIndex + downGradeIndex];
                    }
                    if(PlantCodex.seedProduction[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        seedRate = PlantCodex.seedRateStats[PlantCodex.seedProduction[plantIndex] + upgradeIndex + downGradeIndex];
                        gameScreen.updateSeedRate();
                    }
                    break;
                case 3:
                    if(PlantCodex.AS[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        attackSpeed = PlantCodex.asStats[PlantCodex.AS[plantIndex] + upgradeIndex + downGradeIndex];
                    }
                    if(PlantCodex.DMG[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        damage = PlantCodex.dmgStats[PlantCodex.DMG[plantIndex] + upgradeIndex + downGradeIndex];
                    }
                    if(PlantCodex.range[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        gameScreen.getWorld().destroyBody(body);
                        initialize();
                    }
                    break;
                case 9:
                    if(PlantCodex.maxHP[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        targetHp = PlantCodex.hpStats[PlantCodex.maxHP[plantIndex] + upgradeIndex + downGradeIndex];
                        damaged = true;
                    }
                    if(PlantCodex.DMG[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        damage = PlantCodex.dmgStats[PlantCodex.DMG[plantIndex] + upgradeIndex + downGradeIndex];
                    }
                    if(PlantCodex.range[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        gameScreen.getWorld().destroyBody(body);
                        initialize();
                    }
                    break;
                case 10:
                    if(PlantCodex.DMG[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        damage = PlantCodex.dmgStats[PlantCodex.DMG[plantIndex] + upgradeIndex + downGradeIndex];
                    }
                    break;
                case 11:
                    if(PlantCodex.AS[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        attackSpeed = PlantCodex.asStats[PlantCodex.AS[plantIndex] + upgradeIndex + downGradeIndex];
                    }
                    break;
                case 13:
                    if(PlantCodex.maxHP[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        targetHp = PlantCodex.hpStats[PlantCodex.maxHP[plantIndex] + upgradeIndex + downGradeIndex];
                        damaged = true;
                    }
                    if(PlantCodex.range[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        gameScreen.getWorld().destroyBody(body);
                        initialize();
                    }
                    break;
                case 14:
                    if(PlantCodex.AS[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        attackSpeed = PlantCodex.asStats[PlantCodex.AS[plantIndex] + upgradeIndex + downGradeIndex];
                    }
                    if(PlantCodex.DMG[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        damage = PlantCodex.dmgStats[PlantCodex.DMG[plantIndex] + upgradeIndex + downGradeIndex];
                    }
                    if(PlantCodex.seedProduction[plantIndex] + upgradeIndex + downGradeIndex < PlantCodex.ABS_HIGHEST) {
                        seedRate = PlantCodex.seedRateStats[PlantCodex.seedProduction[plantIndex] + upgradeIndex + downGradeIndex];
                        gameScreen.updateSeedRate();
                    }

            }
            gameScreen.getGameHud().setPlantStatsData();
        }
    }


    public void downGrade(float update) {
        if(downGradeIndex <= 0 && downGradeIndex >= -3) {
            downGradeIndex += update;

            downgradeSprite.getTexture().dispose();
            if(downGradeIndex == 0) {
                downgradeSprite = new Sprite(new Texture(Gdx.files.internal("misc/rank0.png")));
            } else {
                downgradeSprite = new Sprite(new Texture(Gdx.files.internal("misc/downrank" + (int)Math.abs(downGradeIndex) + ".png")));
            }
            downgradeSprite.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());

            if(PlantCodex.DMG[plantIndex] + downGradeIndex + upgradeIndex >= 0) {
                damage = PlantCodex.dmgStats[PlantCodex.DMG[plantIndex] + downGradeIndex + upgradeIndex];
            }

            if(PlantCodex.AS[plantIndex] + downGradeIndex + upgradeIndex > 0) {
                attackSpeed = PlantCodex.asStats[PlantCodex.AS[plantIndex] + downGradeIndex + upgradeIndex];
            }

            if(PlantCodex.seedProduction[plantIndex] + upgradeIndex + downGradeIndex > 0) {
                seedRate = PlantCodex.seedRateStats[PlantCodex.seedProduction[plantIndex] + upgradeIndex + downGradeIndex];
                gameScreen.updateSeedRate();
            }

            if(update < 0) {
                sunlightAccumulation /= 2;
            } else if(update > 0) {
                sunlightAccumulation *= 2;
            }
        }
    }

    public void update() {
        if(!growing) {
            if(targets.size > 0 && System.currentTimeMillis() - lastAttackTime >= attackSpeed) {
                sound.play();
                try{
                    int dmgRange = (int)(damage.y - damage.x);
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
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    Gdx.app.log("Verbose", "Error in plant attack: " + sw.toString());
                }
            }

            if(damaged) {
                if(System.currentTimeMillis() - growthTimer >= 200) {
                    hp += growthRate;
                    float multiplier = hp / targetHp;
                    greenLifeBar.setBounds(greenLifeBar.getX(), greenLifeBar.getY(), size * multiplier, size / 16);
                    if(hp >= targetHp) {
                        damaged = false;
                    }
                    growthTimer = System.currentTimeMillis();
                }
            }

            for(int i = 0; i < targets.size; i++) {
                if(targets.get(i).getLife() <= 0) {
                    targets.removeIndex(i);
                }
            }

            upgradable = (gameScreen.getWater() - upgradeCostWater >= 0 && sunlight - upgradeCostSunlight >= 0);
        } else {
            if(System.currentTimeMillis() - growthTimer >= 200) {
                hp += growthRate;
                float multiplier = hp / targetHp;
                greenLifeBar.setBounds(greenLifeBar.getX(), greenLifeBar.getY(), size * multiplier, size / 16);
                if(hp >= targetHp) {
                    if(growing) {
                        growing = false;
                        redLifeBar.setY(redLifeBar.getY() - size / 2);
                        greenLifeBar.setY(greenLifeBar.getY() - size / 2);
                        lastAttackTime = System.currentTimeMillis();
                    } else if(damaged) {
                        damaged = false;
                    }
                }
                growthTimer = System.currentTimeMillis();
            }
        }
        if(System.currentTimeMillis() - lastAcquisition > 1000) {
            lastAcquisition = System.currentTimeMillis();
            sunlight += sunlightAccumulation;
        }
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if(!growing && hp <= 0) {
            canDispose = true;
        } else {
            update();
            if(selected || selectAllPlants) {
                float a = 0.5f;
                rangeSprite.draw(batch);
                effectiveRangeSprite.setColor(effectiveRangeSprite.getColor().r, effectiveRangeSprite.getColor().g, effectiveRangeSprite.getColor().b, a);
                effectiveRangeSprite.draw(batch);
            }
            if(!growing) {
                sprite.draw(batch);
                upgradeSprite.draw(batch);

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
            if(growing || selected || hit) {
                redLifeBar.draw(batch);
                greenLifeBar.draw(batch);
            }

            if(hit && System.currentTimeMillis() - lastHitTime > 5000) {
                hit = false;
                damaged = true;
            }

            downgradeSprite.draw(batch);
        }
    }

    @Override
    public void dispose() {
        nullSelectedPlant();
        rangeSprite.getTexture().dispose();
        effectiveRangeSprite.getTexture().dispose();

        downgradeSprite.getTexture().dispose();
        upgradeSprite.getTexture().dispose();

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

        for(int i = 0; i < attackers.size; i++) {
            attackers.get(i).stopAttacking();
        }

        plantCollision.dispose();
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

    public float getSize() {
        return size;
    }

    public float getEffectiveRange() {
        return effectiveRange;
    }

    public float getRange() {
        return actualRange;
    }

    public float getSunlight() {
        return sunlight;
    }

    public float getUpgradeCostSunlight() {
        return upgradeCostSunlight;
    }

    public float getUpgradeCostWater() {
        return upgradeCostWater;
    }

    public float getTargetHp() {
        return targetHp;
    }

    public boolean isUpgradable() {
        return upgradable;
    }

    public boolean isGrowing() {
        return growing;
    }

    public boolean canDispose() {
        return canDispose;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public PlantCollision getPlantCollision() {
        return plantCollision;
    }

    public Body getBody() {
        return body;
    }

    public int getUpgradeIndex() {
        return upgradeIndex;
    }

    public long getAttackSpeed() {
        return attackSpeed;
    }

    public Vector2 getDamage() {
        return damage;
    }

    public void setPlantCollision(PlantCollision plantCollision) {
        this.plantCollision = plantCollision;
    }

    public void setHasTarget(boolean hasTarget) {
        this.hasTarget = hasTarget;
    }

    public void receiveDamage(float damage) {
        this.hp -= damage;
        float multiplier = hp / targetHp;
        greenLifeBar.setBounds(greenLifeBar.getX(), greenLifeBar.getY(), size * multiplier, greenLifeBar.getHeight());
        hit = true;
        damaged = false;
        lastHitTime = System.currentTimeMillis();
    }

    public static Plant getSelectedPlant() {
        return selectedPlant;
    }
}
