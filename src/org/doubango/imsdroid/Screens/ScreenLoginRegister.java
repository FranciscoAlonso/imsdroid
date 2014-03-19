package org.doubango.imsdroid.Screens;

import org.doubango.imsdroid.NativeService;
import org.doubango.imsdroid.R;
import org.doubango.ngn.events.NgnEventArgs;
import org.doubango.ngn.events.NgnRegistrationEventArgs;
import org.doubango.ngn.services.INgnConfigurationService;
import org.doubango.ngn.services.INgnSipService;
import org.doubango.ngn.utils.NgnConfigurationEntry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ScreenLoginRegister extends BaseScreen{
	private static String TAG = ScreenPresence.class.getCanonicalName();
	
	private final INgnConfigurationService mConfigurationService;
	private final INgnSipService mSipService;
	
	private EditText mUserName;
	private EditText mPassword; //change for password type field
	private Button mLoginButton;
	
	private BroadcastReceiver mBroadCastRecv;
	private BroadcastReceiver mSipBroadCastRecv;
	
	public ScreenLoginRegister() {
		super(SCREEN_TYPE.LOGIN_REGISTER, ScreenLoginRegister.class.getCanonicalName());
		
		mConfigurationService = getEngine().getConfigurationService();
		mSipService = getEngine().getSipService();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_login_register);
        
        mUserName = (EditText)findViewById(R.id.Username_editText);
        mPassword = (EditText)findViewById(R.id.password_editText); //change for password type field      
        mLoginButton = (Button)findViewById(R.id.login_button);
        
        mLoginButton.setOnClickListener(mBtLogIn_OnClickListener); //set the onclick listener 
        
        /*** sip Bcast receiver ***/
		mSipBroadCastRecv = new BroadcastReceiver() { //recibir eventos de registro SIP
			@Override
			public void onReceive(Context context, Intent intent) {
				final String action = intent.getAction();
				
				//registration event
				if(NgnRegistrationEventArgs.ACTION_REGISTRATION_EVENT.equals(action)){
					NgnRegistrationEventArgs args = intent.getParcelableExtra(NgnEventArgs.EXTRA_EMBEDDED);
					if(args == null){
						Log.e(TAG, "Invalid event args");
						return;
					}
					switch(args.getEventType()){
						case REGISTRATION_NOK:
							break;
						case UNREGISTRATION_OK:
							break;
						case REGISTRATION_OK:
							Toast.makeText(context, "audio Call: OK", Toast.LENGTH_SHORT).show();
							
							break;
						case REGISTRATION_INPROGRESS:
							break;
						case UNREGISTRATION_INPROGRESS:
							break;
						case UNREGISTRATION_NOK:
							break;
					}
				}
			}
        };
        
	}
	
	private OnClickListener mBtLogIn_OnClickListener = new OnClickListener(){ 
		public void onClick(View v) {//set the onclick method for login in and sip registration
			//login compare username and password againts BD data 
			//if success proceed with register attempt (get the extension from bd) 
			String usrname = mUserName.getText().toString();
			String password = mPassword.getText().toString();
			if(usrname.equals("bob") && password.equals("bob123")){
				// Set credentials (get them from SOS BD or sip server data)
				//192.168.1.120 home
				//192.168.2.13 tony's VM at work
				mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_IMPI, 
						"6002");
				mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_IMPU, 
						"sip:6002@192.168.2.13");
				mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_PASSWORD,
						"bob123");
				mConfigurationService.putString(NgnConfigurationEntry.NETWORK_PCSCF_HOST,
						"192.168.2.13");
				mConfigurationService.putInt(NgnConfigurationEntry.NETWORK_PCSCF_PORT,
						5060);
				mConfigurationService.putString(NgnConfigurationEntry.NETWORK_REALM,
						"192.168.2.13");
				// VERY IMPORTANT: Commit changes
				mConfigurationService.commit();
				// register (log in)
				mSipService.register(ScreenLoginRegister.this);
				
			}
		}
	};
	
	@Override
	protected void onPause() {
		
	}

}