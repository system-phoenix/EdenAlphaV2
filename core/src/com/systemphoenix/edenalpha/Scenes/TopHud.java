package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.systemphoenix.edenalpha.EdenAlpha;

public class TopHud extends AbsoluteHud {

    private Label camStats, touchStats, timeStats;

    public TopHud(EdenAlpha game) {
        super(game);

        Color border = Color.BLACK, fontColor = Color.WHITE;
        BitmapFont tempFont = font;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/neuropol.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.borderColor = border;
        parameter.borderWidth = 3;
        font = generator.generateFont(parameter);

        Table temp = new Table();
        temp.top();
        temp.setFillParent(true);
        timeStats = new Label("--:--", new Label.LabelStyle(font, fontColor));

        temp.add(message).expandX().padTop(10);
        temp.row();
        temp.add(timeStats);

        stage.addActor(temp);

        temp = new Table();
        temp.bottom();
        temp.setFillParent(true);

        camStats = new Label("--", new Label.LabelStyle(font, fontColor));
        touchStats = new Label("--", new Label.LabelStyle(font, fontColor));

        temp.add(camStats);
        temp.row();
        temp.add(touchStats).padBottom(20);
        stage.addActor(temp);

        font = tempFont;
    }

    public void setTimeStats(String timeStats) {
        this.timeStats.setText(timeStats);
    }

    public void setCamStatMessage(String message) {
        camStats.setText(message);
    }

    public void setTouchStatsMessage(String message) {
        touchStats.setText(message);
    }
}
