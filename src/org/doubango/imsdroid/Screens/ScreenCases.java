package org.doubango.imsdroid.Screens;

import org.doubango.imsdroid.R;
import org.doubango.imsdroid.Screens.BaseScreen.SCREEN_TYPE;
import org.doubango.ngn.services.INgnConfigurationService;
import org.doubango.ngn.services.INgnSipService;

import android.os.Bundle;

public class ScreenCases extends BaseScreen{
	
	private static String TAG = ScreenPresence.class.getCanonicalName();
	
	private final INgnConfigurationService mConfigurationService;
	private final INgnSipService mSipService;
	
	public ScreenCases() {
		super(SCREEN_TYPE.CASES, ScreenLoginRegister.class.getCanonicalName());
		
		mConfigurationService = getEngine().getConfigurationService();
		mSipService = getEngine().getSipService();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_cases);
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
