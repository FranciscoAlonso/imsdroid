package org.doubango.imsdroid.Screens;

import org.doubango.imsdroid.QuickAction.ActionItem;
import org.doubango.imsdroid.QuickAction.QuickAction;
import org.doubango.ngn.media.NgnMediaType;
import org.doubango.ngn.model.NgnContact;
import org.doubango.ngn.services.INgnSipService;

import android.view.View;

public class ScreenMedicList extends BaseScreen{
	private static String TAG = ScreenMedicList.class.getCanonicalName();
	
	private final INgnSipService mSipService;
	private final ActionItem mAItemVoiceCall;
	private final ActionItem mAItemVideoCall;
	
	private NgnContact mSelectedContact;
	private QuickAction mLasQuickAction;
	
	public ScreenMedicList(){
		super(SCREEN_TYPE.MEDIC_LIST, TAG);
		mSipService = getEngine().getSipService();
		
		/*Botones al hacer click sobre contacto*/ 
		mAItemVoiceCall = new ActionItem();
		mAItemVoiceCall.setTitle("Audio");
		mAItemVoiceCall.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mSelectedContact != null){
					ScreenAV.makeCall("6001", NgnMediaType.Audio);
					if(mLasQuickAction != null){
						mLasQuickAction.dismiss();
					}
				}
			}
		});
		
		mAItemVideoCall = new ActionItem();
		mAItemVideoCall.setTitle("Video");
		mAItemVideoCall.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mSelectedContact != null){
					ScreenAV.makeCall("6001", NgnMediaType.AudioVideo);
					if(mLasQuickAction != null){
						mLasQuickAction.dismiss();
					}
				}
			}
		});
		
	}
}
