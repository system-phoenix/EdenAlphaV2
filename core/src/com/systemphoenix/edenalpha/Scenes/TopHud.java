package com.systemphoenix.edenalpha.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.systemphoenix.edenalpha.Actors.ObjectActors.Plant;
import com.systemphoenix.edenalpha.Codex.ButtonCodex;
import com.systemphoenix.edenalpha.Eden;
import com.systemphoenix.edenalpha.Screens.GameScreen;

public class TopHud extends AbsoluteHud implements Disposable {

    private Label waveStats, timeStats, readyPlant, loadingMessage, seedStats, waterStats, sunlightStats;
    private com.systemphoenix.edenalpha.Actors.StageActors.ButtonActor pauseButton = null;
    private HudBackground hudBg;
    private GameScreen gameScreen;

    public TopHud(Eden game, GameScreen gameScreen) {
        super(game);
        this.gameScreen = gameScreen;

        hudBg = new HudBackground(0, gameScreen.getWorldHeight() - 74);
        stage.addActor(hudBg);

        Color border = Color.BLACK, fontColor = Color.WHITE;
        BitmapFont tempFont = font;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arcon.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        parameter.borderColor = border;
        parameter.borderWidth = 3;
        font = generator.generateFont(parameter);

        float paramX = 32, paramY = gameScreen.getWorldHeight() - 60, paramW = 100, paramH = 50;

        Table temp = new Table();
        temp.center();
        temp.setFillParent(true);
        readyPlant = new Label("", new Label.LabelStyle(font, fontColor));
        temp.add(readyPlant).expandX().expandY();

        stage.addActor(temp);

        parameter.size = 16;
        parameter.borderColor = border;
        parameter.borderWidth = 2;
        font = generator.generateFont(parameter);

        temp = new Table();
        temp.setBounds(paramX + paramW, paramY, paramW, paramH);
        temp.top();

        message.setText("");
        timeStats = new Label("", new Label.LabelStyle(font, fontColor));
        waveStats = new Label("", new Label.LabelStyle(font, fontColor));
        seedStats = new Label("", new Label.LabelStyle(font, fontColor));
        waterStats = new Label("", new Label.LabelStyle(font, fontColor));
        sunlightStats = new Label("--", new Label.LabelStyle(font, fontColor));
        loadingMessage = new Label("Loading...", new Label.LabelStyle(font, fontColor));

        temp.top();
        temp.add(message);
        temp.row();
        temp.add(timeStats);
        temp.row();
        temp.add(waveStats);

        stage.addActor(temp);

        temp = new Table();
        temp.setBounds(gameScreen.getWorldWidth() - paramX - paramW, paramY, paramW, paramH);
        temp.top();
        temp.add(seedStats);
        temp.row();
        temp.add(waterStats);
        temp.row();
        temp.add(sunlightStats);

        stage.addActor(temp);

        temp = new Table();
        temp.center();
        temp.setFillParent(true);
        temp.add(loadingMessage).expandX().expandY();
        stage.addActor(temp);

        font = tempFont;
    }

    @Override
    public void dispose() {
        hudBg.dispose();
    }

    public void update() {
        if(Plant.getSelectedPlant() != null) {
            sunlightStats.setText("" + (int)Plant.getSelectedPlant().getSunlight());
        } else {
            sunlightStats.setText("--");
        }
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

    public void setSeedStatMessage(String message) {
        seedStats.setText(message);
    }

    public void setWaterStatMessage(String message) {
        waterStats.setText(message);
    }

    public void setLoadingMessage(String message) {
        loadingMessage.setText(message);
    }

    public void createLabels() {
        Color border = Color.BLACK, fontColor = Color.WHITE;
        BitmapFont tempFont = font;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arcon.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.borderColor = border;
        parameter.borderWidth = 3;
        font = generator.generateFont(parameter);

        Table temp;

        temp = new Table();
        float paramX = 32, paramY = gameScreen.getWorldHeight() - 60, paramW = 100, paramH = 50;
        temp.setBounds(paramX, paramY, paramW, paramH);
        temp.top();

        Label tempLabel;

        tempLabel = new Label("Life:", new Label.LabelStyle(font, fontColor));
        temp.add(tempLabel);
        temp.row();
        tempLabel = new Label("Time:", new Label.LabelStyle(font, fontColor));
        temp.add(tempLabel);
        temp.row();
        tempLabel = new Label("Wave:", new Label.LabelStyle(font, fontColor));
        temp.add(tempLabel);

        stage.addActor(temp);

        temp = new Table();
        temp.setBounds(gameScreen.getWorldWidth() - paramX - 200, paramY, paramW, paramH);
        temp.top();
        tempLabel = new Label("Seeds:", new Label.LabelStyle(font, fontColor));
        temp.add(tempLabel);
        temp.row();
        tempLabel = new Label("Water:", new Label.LabelStyle(font, fontColor));
        temp.add(tempLabel);
        temp.row();
        tempLabel = new Label("Sunlight:", new Label.LabelStyle(font, fontColor));
        temp.add(tempLabel);

        stage.addActor(temp);
        font = tempFont;
    }

    public void setPauseButtonCanDraw(boolean drawable) {
        if(pauseButton == null) {
            pauseButton = new com.systemphoenix.edenalpha.Actors.StageActors.ButtonActor(ButtonCodex.PAUSE, gameScreen, (gameScreen.getWorldWidth() / 2) - 32, gameScreen.getWorldHeight() - 64, 64, true, false);
            stage.addActor(pauseButton);
        }
        this.pauseButton.setDrawable(drawable);

    }

}
