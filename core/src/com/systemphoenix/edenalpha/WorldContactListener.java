package com.systemphoenix.edenalpha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.systemphoenix.edenalpha.Actors.Enemy;
import com.systemphoenix.edenalpha.Actors.Plant;

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
