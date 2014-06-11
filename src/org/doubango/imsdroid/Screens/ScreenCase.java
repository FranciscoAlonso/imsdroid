package org.doubango.imsdroid.Screens;

import org.doubango.imsdroid.R;
import org.doubango.imsdroid.Screens.BaseScreen.SCREEN_TYPE;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ScreenCase extends BaseScreen{
	
	private Button saveButton;
	private Button opinionButton;
	private Button cancelButton;
	
	private static String TAG = ScreenPresence.class.getCanonicalName();
	
	public ScreenCase() {
		super(SCREEN_TYPE.CASE, ScreenCase.class.getCanonicalName());
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_case);
        
        saveButton = (Button)findViewById(R.id.saveCase);
        opinionButton = (Button)findViewById(R.id.askOpinion);
        cancelButton = (Button)findViewById(R.id.cancelEditCase);
        
        saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/* Si el caso es nuevo: escribir el caso a bd y lanzar la pantalla de contactos filtrados por especialidad (cambiar texto a salvar y anadir opinion)
				 * Si el caso no es nuevo salvar cambios solamente
				 * */
				mScreenService.show(ScreenTabContacts.class);
			}
		});
        
        opinionButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//salvar y lanzar la pantalla de contactos filtrados por especialidad
				mScreenService.show(ScreenTabContacts.class);
			}
		});
        
        cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//llamar pantalla home y descartar cambios
				mScreenService.show(ScreenHome.class);
			}
		});
        
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
