package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.systemphoenix.edenalpha.EdenAlpha;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class TopHud extends AbsoluteHud {

    private Label waveStats, timeStats, readyPlant, loadingMessage;
    private HudBackground hudBg;

    public TopHud(EdenAlpha game, GameScreen gameScreen) {
        super(game);

        hudBg = new HudBackground(0, gameScreen.getWorldHeight() - 74);
        stage.addActor(hudBg);

        Color border = Color.BLACK, fontColor = Color.WHITE;
        BitmapFont tempFont = font;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/neuropol.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        parameter.borderColor = border;
        parameter.borderWidth = 3;
        font = generator.generateFont(parameter);

        Table temp = new Table();
        temp.center();
        temp.setFillParent(true);
        readyPlant = new Label("", new Label.LabelStyle(font, fontColor));
        temp.add(readyPlant).expandX().expandY();

        stage.addActor(temp);

        font = tempFont;
        temp = new Table();
        temp.top();
        temp.setFillParent(true);

        message.setText("");
        timeStats = new Label("", new Label.LabelStyle(font, fontColor));
        waveStats = new Label("", new Label.LabelStyle(font, fontColor));
        loadingMessage = new Label("Loading...", new Label.LabelStyle(font, fontColor));

        temp.add(message).expandX().padTop(10);
        temp.row();
        temp.add(timeStats);
        temp.row();
        temp.add(waveStats);

        stage.addActor(temp);

        temp = new Table();
        temp.center();
        temp.setFillParent(true);
        temp.add(loadingMessage).expandX().expandY();
        stage.addActor(temp);

    }

    public void setTimeStats(String timeStats) {
        this.timeStats.setText(timeStats);
    }

    public void setWaveStatMessage(String message) {
        waveStats.setText(message);
    }

    public void setReadyPlantMessage(String message) {
        readyPlant.setText(message);
    }

    public void setLoadingMessage(String message) {
        loadingMessage.setText(message);
    }

}
