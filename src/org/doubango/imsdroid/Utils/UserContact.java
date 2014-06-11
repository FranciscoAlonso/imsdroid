package org.doubango.imsdroid.Utils;

public class UserContact {
	
	private String Name;
	private String Especialty;
	private String Extension;
	private String PhoneNumber;
	private int Status;
	
	public UserContact(String N, String Esp, String Ext, String Pnumber){
		Name = N;
		Especialty = Esp;
		Extension = Ext;
		PhoneNumber = Pnumber;
		Status = 0;
	}
	public UserContact(){
		Name = null;
		Especialty = null;
		Extension = null;
		PhoneNumber = null;
		Status = 0;		
	}
	
	public String getDisplayName(){
		return Name;
	}
	
	public void setName(String name){
		this.Name = name;
	}

	public String getPrimaryNumber(){
		return this.Extension;
	}
	
	public String getPhoneNumber(){
		return this.PhoneNumber;
	}
	
	public void setPhoneNumber(String number){
		this.PhoneNumber = number;
	}	
	
	public void setExtension(String number){
		this.Extension = number;
	}
	
	public String getExtension(){
		return this.Extension;
	}
}
