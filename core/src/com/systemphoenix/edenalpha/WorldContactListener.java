package com.systemphoenix.edenalpha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.systemphoenix.edenalpha.Actors.Enemy;
import com.systemphoenix.edenalpha.Actors.Plant;
import com.systemphoenix.edenalpha.Actors.PlantCollision;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA(), b = contact.getFixtureB();

        int collision = a.getFilterData().categoryBits | b.getFilterData().categoryBits;
        switch (collision) {
            case CollisionBit.PATHBOUND | CollisionBit.ENEMY:
                if (a.getFilterData().categoryBits == CollisionBit.ENEMY) {
                    ((Enemy) a.getUserData()).setDirection();
                } else {
                    ((Enemy) b.getUserData()).setDirection();
                }
                break;
            case CollisionBit.ENDPOINT | CollisionBit.ENEMY:
                if(a.getFilterData().categoryBits == CollisionBit.ENEMY) {
                    ((Enemy) a.getUserData()).damageForest();
                } else {
                    ((Enemy) b.getUserData()).damageForest();
                }
                break;
            case CollisionBit.ENEMYRANGE | CollisionBit.ENEMY:
                if(a.getFilterData().categoryBits == CollisionBit.ENEMYRANGE) {
                    ((Plant) a.getUserData()).acquireTarget((Enemy) b.getUserData());
                } else {
                    ((Plant) b.getUserData()).acquireTarget((Enemy) a.getUserData());
                }
                break;
            case CollisionBit.EFFECTIVERANGE | CollisionBit.PLANT:
                if(a.getFilterData().categoryBits == CollisionBit.PLANT) {
                    if(((PlantCollision) a.getUserData()).checkCollision((Plant) b.getUserData())) {
                        ((Plant) b.getUserData()).getPlantCollision().applyImpulse();
                    }
                } else {
                    if(((PlantCollision) b.getUserData()).checkCollision((Plant) a.getUserData())) {
                        ((Plant) a.getUserData()).getPlantCollision().applyImpulse();
                    }
                }
                break;
            case CollisionBit.ENEMY | CollisionBit.PLANT:
                if(a.getFilterData().categoryBits == CollisionBit.PLANT) {
                    if(!((PlantCollision) a.getUserData()).isGrowing()) {
                        ((PlantCollision) a.getUserData()).getPlant().acquireAttacker((Enemy) b.getUserData());
                    }
                } else {
                    if(!((PlantCollision) b.getUserData()).isGrowing()) {
                        ((PlantCollision) b.getUserData()).getPlant().acquireAttacker((Enemy) a.getUserData());
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA(), b = contact.getFixtureB();

        int collision = a.getFilterData().categoryBits | b.getFilterData().categoryBits;
        switch (collision) {
            case CollisionBit.ENEMYRANGE | CollisionBit.ENEMY:
                if(a.getFilterData().categoryBits == CollisionBit.ENEMYRANGE) {
                    ((Plant) a.getUserData()).removeTarget((Enemy) b.getUserData());
                } else {
                    ((Plant) b.getUserData()).removeTarget((Enemy) a.getUserData());
                }
            case CollisionBit.ENEMY | CollisionBit.PLANT:
                if(a.getFilterData().categoryBits == CollisionBit.ENEMY) {
//                    ((Enemy) a.getUserData()).removeStun();
                } else {
//                    ((Enemy) b.getUserData()).removeStun();
                }
                break;
            case CollisionBit.EFFECTIVERANGE | CollisionBit.PLANT:
                if(a.getFilterData().categoryBits == CollisionBit.PLANT) {
                    if(((PlantCollision) a.getUserData()).removeCollision((Plant) b.getUserData())) {
                        ((Plant) b.getUserData()).getPlantCollision().applyImpulse();
                    }
                } else {
                    if(((PlantCollision) b.getUserData()).removeCollision((Plant) a.getUserData())) {
                        ((Plant) a.getUserData()).getPlantCollision().applyImpulse();
                    }
                }
                break;
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
}
