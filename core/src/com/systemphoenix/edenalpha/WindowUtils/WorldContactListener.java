package com.systemphoenix.edenalpha.WindowUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.systemphoenix.edenalpha.Actors.ObjectActors.Arena;
import com.systemphoenix.edenalpha.Actors.ObjectActors.Enemy;
import com.systemphoenix.edenalpha.Actors.ObjectActors.Plant;
import com.systemphoenix.edenalpha.Actors.ObjectActors.PlantCollision;
import com.systemphoenix.edenalpha.Actors.ObjectActors.Pulse;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA(), b = contact.getFixtureB();

        int collision = a.getFilterData().categoryBits | b.getFilterData().categoryBits;
        Enemy enemy;
        Plant plant;
        PlantCollision plantCollision;
        Pulse pulse;
        Arena arena;
        switch (collision) {
            case CollisionBit.PATH_BOUND | CollisionBit.ENEMY:
//                Gdx.app.log("Verbose", "Path Enemy");
                enemy = a.getFilterData().categoryBits == CollisionBit.ENEMY ? ((Enemy)a.getUserData()) : ((Enemy)b.getUserData());
                enemySetDirection(enemy);
                break;
            case CollisionBit.ENDPOINT | CollisionBit.ENEMY:
//                Gdx.app.log("Verbose", "EndPoint Enemy");
                enemy = a.getFilterData().categoryBits == CollisionBit.ENEMY ? ((Enemy)a.getUserData()) : ((Enemy)b.getUserData());
                enemyDamageForest(enemy);
                break;
            case CollisionBit.ENEMY_RANGE | CollisionBit.ENEMY:
//                Gdx.app.log("Verbose", "Range Enemy");
                plant = a.getFilterData().categoryBits == CollisionBit.ENEMY_RANGE ? ((Plant)a.getUserData()) : ((Plant)b.getUserData());
                enemy = a.getFilterData().categoryBits == CollisionBit.ENEMY_RANGE ? ((Enemy)b.getUserData()) : ((Enemy)a.getUserData());
                plantAcquireTarget(plant, enemy);
                break;
            case CollisionBit.EFFECTIVE_RANGE | CollisionBit.PLANT:
                plantCollision  = a.getFilterData().categoryBits == CollisionBit.PLANT ? (PlantCollision) a.getUserData()   : (PlantCollision) b.getUserData();
                plant           = a.getFilterData().categoryBits == CollisionBit.PLANT ? (Plant) b.getUserData()            : (Plant) a.getUserData();
                plantsCollide(plantCollision, plant, true);
                break;
            case CollisionBit.ENEMY | CollisionBit.PLANT:
//                Gdx.app.log("Verbose", "Enemy Plant");
                plantCollision  = a.getFilterData().categoryBits == CollisionBit.PLANT ? (PlantCollision) a.getUserData()   : (PlantCollision) b.getUserData();
                enemy           = a.getFilterData().categoryBits == CollisionBit.PLANT ? (Enemy) b.getUserData()            : (Enemy) a.getUserData();
                plantAcquireAttacker(plantCollision, enemy);
                break;
            case CollisionBit.ENEMY | CollisionBit.PULSE:
//                Gdx.app.log("Verbose", "Pulse Enemy");
                pulse = a.getFilterData().categoryBits == CollisionBit.PULSE ? (Pulse) a.getUserData() : (Pulse) b.getUserData();
                enemy = a.getFilterData().categoryBits == CollisionBit.PULSE ? (Enemy) b.getUserData() : (Enemy) a.getUserData();
                pulseAcquireTarget(pulse, enemy);
                break;
            case CollisionBit.ENEMY | CollisionBit.ARENA:
//                Gdx.app.log("Verbose", "Arena Enemy");
                arena = a.getFilterData().categoryBits == CollisionBit.ARENA ? (Arena) a.getUserData() : (Arena) b.getUserData();
                enemy = a.getFilterData().categoryBits == CollisionBit.ARENA ? (Enemy) b.getUserData() : (Enemy) a.getUserData();
                arenaCollision(arena, enemy, true);
                break;
            default:
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA(), b = contact.getFixtureB();

        Plant plant;
        PlantCollision plantCollision;
        Pulse pulse;
        Enemy enemy;
        Arena arena;
        int collision = a.getFilterData().categoryBits | b.getFilterData().categoryBits;
        switch (collision) {
            case CollisionBit.ENEMY_RANGE | CollisionBit.ENEMY:
                plant = a.getFilterData().categoryBits == CollisionBit.ENEMY_RANGE ? ((Plant)a.getUserData()) : ((Plant)b.getUserData());
                enemy = a.getFilterData().categoryBits == CollisionBit.ENEMY_RANGE ? ((Enemy)b.getUserData()) : ((Enemy)a.getUserData());
                plantRemoveTarget(plant, enemy);
                break;
            case CollisionBit.EFFECTIVE_RANGE | CollisionBit.PLANT:
                plantCollision  = a.getFilterData().categoryBits == CollisionBit.PLANT ? (PlantCollision) a.getUserData()   : (PlantCollision) b.getUserData();
                plant           = a.getFilterData().categoryBits == CollisionBit.PLANT ? (Plant) b.getUserData()            : (Plant) a.getUserData();
                plantsCollide(plantCollision, plant, true);
                break;

            case CollisionBit.PULSE | CollisionBit.ENEMY:
                pulse = a.getFilterData().categoryBits == CollisionBit.PULSE ? (Pulse) a.getUserData() : (Pulse) b.getUserData();
                enemy = a.getFilterData().categoryBits == CollisionBit.PULSE ? (Enemy) b.getUserData() : (Enemy) a.getUserData();
                pulseRemoveTarget(pulse, enemy);
                break;
            case CollisionBit.ENEMY | CollisionBit.ARENA:
                arena = a.getFilterData().categoryBits == CollisionBit.ARENA ? (Arena) a.getUserData() : (Arena) b.getUserData();
                enemy = a.getFilterData().categoryBits == CollisionBit.ARENA ? (Enemy) b.getUserData() : (Enemy) a.getUserData();
                arenaCollision(arena, enemy, false);
            default:
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private void enemySetDirection(Enemy enemy) {
        enemy.setDirection();
    }

    private void enemyDamageForest(Enemy enemy) {
        enemy.damageForest();
    }

    private void plantAcquireTarget(Plant plant, Enemy enemy) {
        plant.acquireTarget(enemy);
    }

    private void plantRemoveTarget(Plant plant, Enemy enemy) {
        plant.removeTarget(enemy);
    }

    private void plantAcquireAttacker(PlantCollision plantCollision, Enemy enemy) {
        if(!plantCollision.isGrowing()) {
            plantCollision.getPlant().acquireAttacker(enemy);
        }
    }

    private void plantsCollide(PlantCollision plantCollision, Plant plant, boolean colliding) {
        if(colliding) {
            if(plantCollision.checkCollision(plant)) {
                plant.getPlantCollision().applyImpulse();
            }
        } else {
            if(plantCollision.removeCollision(plant)) {
                plant.getPlantCollision().applyImpulse();
            }
        }
    }

    private void pulseAcquireTarget(Pulse pulse, Enemy enemy) {
        pulse.acquireTarget(enemy);
    }

    private void pulseRemoveTarget(Pulse pulse, Enemy enemy) {
        pulse.removeTarget(enemy);
    }

    private void arenaCollision(Arena arena, Enemy enemy, boolean onContact) {
        if(onContact) arena.acquireTarget(enemy);
        else arena.removeTarget(enemy);
    }
}
