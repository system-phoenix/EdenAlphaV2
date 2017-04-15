package com.systemphoenix.edenalpha.WindowUtils;

import com.systemphoenix.edenalpha.Actors.ObjectActors.Enemy;

import java.util.Comparator;

public class PositionComparator implements Comparator<Enemy>{
    @Override
    public int compare(Enemy o1, Enemy o2) {
        if(o1.getPathCount() > o2.getPathCount()) return -1;
        if(o1.getPathCount() < o2.getPathCount()) return 1;
        return 0;
    }
}
