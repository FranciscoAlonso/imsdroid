package org.doubango.imsdroid.Screens;

import org.doubango.imsdroid.R;
import org.doubango.imsdroid.Screens.BaseScreen.SCREEN_TYPE;

import android.os.Bundle;

public class ScreenCase extends BaseScreen{
	
	private static String TAG = ScreenPresence.class.getCanonicalName();
	
	public ScreenCase() {
		super(SCREEN_TYPE.CASE, ScreenCase.class.getCanonicalName());
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_case);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
    @Override
    protected void onDestroy() {
            super.onDestroy();
    }

}
