package org.doubango.imsdroid.Utils;

public class UserContact {
	
	private String Name;
	private String Especialty;
	private String Extension;
	private int Status;
	
	public UserContact(String N, String Esp, String Ext){
		Name = N;
		Especialty = Esp;
		Extension = Ext;
	}
	public UserContact(){
		Name = null;
		Especialty = null;
		Extension = null;
	}
	
	public String getDisplayName(){
		return Name;
	}
	
	public void setName(String name){
		this.Name = name;
	}

}
