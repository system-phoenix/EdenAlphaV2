package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.systemphoenix.edenalpha.EdenAlpha;

public class TopHud extends AbsoluteHud {

    public TopHud(EdenAlpha game) {
        super(game);

        Table temp = new Table();
        temp.top();
        temp.setFillParent(true);

        temp.add(message).expandX().padTop(10);

        stage.addActor(temp);
    }
}
