/* Copyright (C) 2010-2011, Mamadou Diop.
*  Copyright (C) 2011, Doubango Telecom.
*
* Contact: Mamadou Diop <diopmamadou(at)doubango(dot)org>
*	
* This file is part of imsdroid Project (http://code.google.com/p/imsdroid)
*
* imsdroid is free software: you can redistribute it and/or modify it under the terms of 
* the GNU General Public License as published by the Free Software Foundation, either version 3 
* of the License, or (at your option) any later version.
*	
* imsdroid is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
* See the GNU General Public License for more details.
*	
* You should have received a copy of the GNU General Public License along 
* with this program; if not, write to the Free Software Foundation, Inc., 
* 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package org.doubango.imsdroid.Screens;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.doubango.imsdroid.Engine;
import org.doubango.imsdroid.R;
import org.doubango.imsdroid.QuickAction.ActionItem;
import org.doubango.imsdroid.QuickAction.QuickAction;
import org.doubango.imsdroid.Utils.SeparatedListAdapter;
import org.doubango.imsdroid.Utils.UserContact;
import org.doubango.ngn.media.NgnMediaType;
import org.doubango.ngn.model.NgnContact;
import org.doubango.ngn.services.INgnContactService;
import org.doubango.ngn.services.INgnSipService;
import org.doubango.ngn.utils.NgnGraphicsUtils;
import org.doubango.ngn.utils.NgnObservableList;
import org.doubango.ngn.utils.NgnStringUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;


/*Se puede usar tanto para contactos como para casos u otra lista, tiene unos actionitems 
 * para proporcionar distintas acciones sobre el elemento seleccionado 
 */
 
public class ScreenTabContacts extends BaseScreen {
	private static String TAG = ScreenTabContacts.class.getCanonicalName();
	private static final int SELECT_CONTENT = 1;
	  
	@SuppressWarnings("unused")
	private final INgnContactService mContactService;
	private final INgnSipService mSipService;
	private MySeparatedListAdapter mAdapter;
	private ListView mListView;
	
	private final ActionItem mAItemVoiceCall;
	private final ActionItem mAItemVideoCall;
	private final ActionItem mAItemChat;
	private final ActionItem mAItemSMS;
	private final ActionItem mAItemShare;
	
	private UserContact mSelectedContact;
	private QuickAction mLasQuickAction;
	
	public ScreenTabContacts() {
		super(SCREEN_TYPE.TAB_CONTACTS, TAG);
		
		mContactService = getEngine().getContactService();
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
		
		mAItemChat = new ActionItem();
		mAItemChat.setTitle("Chat");
		mAItemChat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//ScreenChat.startChat(mSelectedContact.getPrimaryNumber(), false);
				if(mLasQuickAction != null){
					mLasQuickAction.dismiss();
				}
			}
		});
		
		mAItemSMS = new ActionItem();
		mAItemSMS.setTitle("SMS");
		mAItemSMS.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ScreenChat.startChat(mSelectedContact.getPhoneNumber(), true);
				if(mLasQuickAction != null){
					mLasQuickAction.dismiss();
				}
			}
		});
		
		mAItemShare = new ActionItem();
		mAItemShare.setTitle("Compartir");
		mAItemShare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mSelectedContact != null){
					Intent intent = new Intent();
                    intent.setType("*/*")
                    	.addCategory(Intent.CATEGORY_OPENABLE)
                    	.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select content"), SELECT_CONTENT);   //compartir contenido, revisar para transferencia de archivos 
					if(mLasQuickAction != null){
						mLasQuickAction.dismiss();
					}
				}
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_tab_contacts);
		//se configura la lista 
		mListView = (ListView) findViewById(R.id.screen_tab_contacts_listView);
	    mAdapter = new MySeparatedListAdapter(this);
	    
	    mListView.setAdapter(mAdapter);
	    mListView.setOnItemClickListener(mOnItemListViewClickListener);
	    mListView.setOnItemLongClickListener(mOnItemListViewLongClickListener);
	    registerForContextMenu(mListView);
	    //se le ponen los iconos a cada actionItem
	    mAItemVoiceCall.setIcon(getResources().getDrawable(R.drawable.voice_call_25));
		mAItemVideoCall.setIcon(getResources().getDrawable(R.drawable.visio_call_25));
		//mAItemChat.setIcon(getResources().getDrawable(R.drawable.chat_25));
		mAItemSMS.setIcon(getResources().getDrawable(R.drawable.sms_25));
		//mAItemShare.setIcon(getResources().getDrawable(R.drawable.image_gallery_25));  
	}
	//TRANSFERENCIA DE ARCHIVOS
	/*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { //transferencia de archivos
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case SELECT_CONTENT:
					if (mSelectedContact != null) {
						Uri selectedContentUri = data.getData();
						String selectedContentPath = super.getPath(selectedContentUri);
						ScreenFileTransferView.sendFile(mSelectedContact.getPrimaryNumber(), selectedContentPath);
					}
					break;
			}
		}
	}*/
	private final OnItemClickListener mOnItemListViewClickListener = new OnItemClickListener() { //onclick sobre un elemento de la lista obtiene la posicion y muestra las opciones
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mSelectedContact = (UserContact)parent.getItemAtPosition(position);
			if(mSelectedContact != null){
				mLasQuickAction = new QuickAction(view);
				if(!NgnStringUtils.isNullOrEmpty("6001")){ // se agrega cada opcion (originalmente verificaba si el contacto tiene numero primario)*** CAMBIAR A NMERO DEL CONTACTO SELECCIONADO****
					mLasQuickAction.addActionItem(mAItemVoiceCall);
					mLasQuickAction.addActionItem(mAItemVideoCall);
					//mLasQuickAction.addActionItem(mAItemChat);
					mLasQuickAction.addActionItem(mAItemSMS);
					mLasQuickAction.addActionItem(mAItemShare);
				}
				mLasQuickAction.setAnimStyle(QuickAction.ANIM_AUTO); //tipo de animacion del menu
				mLasQuickAction.show(); //muestra el menu
			}
		}
	};
	
	private final OnItemLongClickListener mOnItemListViewLongClickListener = new OnItemLongClickListener(){ //hace lo mismo que el click corto
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			if(!mSipService.isRegistered()){ 
				Log.e(TAG, "Not registered yet");
				return true;
			}
			
			mSelectedContact = (UserContact)parent.getItemAtPosition(position);
			if(mSelectedContact != null){
				mLasQuickAction = new QuickAction(view);
				if(!NgnStringUtils.isNullOrEmpty("6001")){
					mLasQuickAction.addActionItem(mAItemVoiceCall);
					mLasQuickAction.addActionItem(mAItemVideoCall);
					mLasQuickAction.addActionItem(mAItemChat);
					mLasQuickAction.addActionItem(mAItemSMS);
					mLasQuickAction.addActionItem(mAItemShare);
				}
				mLasQuickAction.setAnimStyle(QuickAction.ANIM_AUTO);
				mLasQuickAction.show();
			}
			return true;
		}
	};
	
	/**
	 * MySeparatedListAdapter
	 */
	//el servicio de contactos no lo veo util, usa contactos locales del telefono, se podria usar la lista observable (NgnObservableList) y llenarla con contactos de la bd
	static class MySeparatedListAdapter extends SeparatedListAdapter implements Observer{ //obsevador para los contactos, usa el servicio de contactos, 
		private final LayoutInflater mInflater;
		private final Context mContext;
		private final Handler mHandler;
		private NgnObservableList<UserContact> mContacts;
		
		
		private MySeparatedListAdapter(Context context){
			super(context);
			mContext = context;
			mHandler = new Handler();
			mInflater = LayoutInflater.from(mContext);

			updateSections();
			notifyDataSetChanged();
		}
		
		@Override
		protected void finalize() throws Throwable {
			Engine.getInstance().getContactService().getObservableContacts().deleteObserver(this);
			super.finalize();
		}
		
		
		
		private void updateSections(){  //Se agregan los contactos 
			clearSections();
			String test = "Test String";
			//synchronized(mContacts){
				//List<UserContact> contacts = mContacts.getList(); //lista de contactos en el telefono
				/***/
				//Obtener los contactos
				List<UserContact> contacts = new ArrayList<UserContact>(); //lista de contactos en el telefono
				UserContact user = new UserContact();
				UserContact user2 = new UserContact();
				user.setName("TEST NAME");
				contacts.add(user);
				contacts.add(user);
				contacts.add(user);
				user2.setName("W TEST NAME");
				user2.setPhoneNumber("5804125774961");
				contacts.add(user2);
				contacts.add(user2);
				/***/
				String lastGroup = "$", displayName;
				ScreenTabContactsAdapter lastAdapter = null;
				
				for(UserContact contact : contacts){
					displayName = contact.getDisplayName();
					if(NgnStringUtils.isNullOrEmpty(displayName)){
						continue;
					}
					final String group = displayName.substring(0, 1).toUpperCase();
					if(!group.equalsIgnoreCase(lastGroup)){
						lastGroup = group;
						lastAdapter = new ScreenTabContactsAdapter(mContext, lastGroup);
						addSection(lastGroup, lastAdapter);
					}
					
					if(lastAdapter != null){
						lastAdapter.addContact(contact); //Se agrega string del nombre de contacto
					}
				}
			//}
		}
		
		@Override
		protected View getHeaderView(int position, View convertView, ViewGroup parent, final Adapter adapter) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.view_list_header, parent, false);
			}
			TextView tvDisplayName = (TextView)convertView.findViewById(R.id.view_list_header_title);
			tvDisplayName.setText(((ScreenTabContactsAdapter)adapter).getSectionText());
			return convertView;
		}

		@Override
		public void update(Observable observable, Object data) {
			//if(Thread.currentThread() == Looper.getMainLooper().getThread()){
			//	updateSections();
			//	notifyDataSetChanged();
			//}
			//else{
				mHandler.post(new Runnable(){
					@Override
					public void run() {
						updateSections();
						notifyDataSetChanged();
					}
				});
			//}
		}
	}
	
	/**
	 * ScreenTabContactsAdapter
	 */
	static class ScreenTabContactsAdapter extends BaseAdapter {
		private final LayoutInflater mInflater;
		
		private final Context mContext;
		private List<UserContact> mContacts;
		//private List<String> mContacts;
		private final String mSectionText;
		
		private ScreenTabContactsAdapter(Context context, String sectionText) {
			mContext = context;
			mSectionText = sectionText;
			mInflater = LayoutInflater.from(mContext);
		}

		public String getSectionText(){
			return mSectionText;
		}
		
		public void addContact(UserContact contact){
			if(mContacts == null){
				mContacts = new ArrayList<UserContact>();
				//mContacts = new ArrayList<String>();
			}
			mContacts.add(contact);
		}
		
		@Override
		public int getCount() {
			return mContacts==null ? 0: mContacts.size();
		}

		@Override
		public Object getItem(int position) {
			if(mContacts != null && mContacts.size()>position){
				return mContacts.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			
			if (view == null) {
				view = mInflater.inflate(R.layout.screen_tab_contacts_contact_item, null);
			}
			final UserContact contact = (UserContact)getItem(position);
			//final String contact = (String)getItem(position);
			
			if(contact != null){
				//final ImageView ivAvatar = (ImageView) view.findViewById(R.id.screen_tab_contacts_item_imageView_avatar);
				//if(ivAvatar != null){
					final TextView tvDisplayName = (TextView) view.findViewById(R.id.screen_tab_contacts_item_textView_displayname);
					final TextView tvDescription = (TextView) view.findViewById(R.id.screen_tab_contacts_item_textView_description);
					
					//tvDisplayName.setText(contact); //Nombre de contacto
					tvDisplayName.setText(contact.getDisplayName()); //Nombre de contacto
					tvDisplayName.setText(contact.getDisplayName());
					/*Se puede sustituir por un icono que indicque presencia*/
					//final Bitmap avatar = contact.getPhoto(); // foto de contacto no se va a usar 
					//if(avatar == null){
						//ivAvatar.setImageResource(R.drawable.avatar_48);
					//}
					//else{
					//	ivAvatar.setImageBitmap(NgnGraphicsUtils.getResizedBitmap(avatar, NgnGraphicsUtils.getSizeInPixel(48), NgnGraphicsUtils.getSizeInPixel(48)));
					//}
				//}
			}
			
			return view;
		}
	}
}