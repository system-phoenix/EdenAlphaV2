package com.systemphoenix.edenalpha;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.systemphoenix.edenalpha.EdenAlpha;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer	 = false;
        config.useCompass		 = false;
        config.useGyroscope		 = false;
        config.useImmersiveMode	 = true;
        config.useWakelock		 = true;
        config.hideStatusBar	 = true;
		initialize(new EdenAlpha(), config);
	}
}
