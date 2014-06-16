package org.doubango.imsdroid.Utils;

import java.util.Date;

public class medicCase {
	
	private int CaseID;
	public int getCaseID(){
		return this.CaseID;
	}
	public void setCaseID(int ID){
		this.CaseID = ID;
	}
	private int centerID;
	public int getcenterID(){
		return this.centerID;
	}
	public void setcenterID(int ID){
		this.centerID = ID;
	}
	private String Description;
	public String getDescription(){
		return this.Description;
	}
	public void setDescription(String Desc){
		this.Description = Desc;
	}
	//private Date StartDate;
	private String StartDate;
	public String getStartDate(){
		return this.StartDate;
	}
	public void setStartDate(String ID){
		this.StartDate = ID;
	}
	//private Date EndDate;
	private String EndDate;
	public String getEndDate(){
		return this.EndDate;
	}
	public void setEndDate(String ID){
		this.EndDate = ID;
	}
}
