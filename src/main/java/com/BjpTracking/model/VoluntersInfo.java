package com.BjpTracking.model;

import java.util.ArrayList;
import java.util.List;

public class VoluntersInfo {

	List<Volunteer> volunteersList =
			new ArrayList<Volunteer>();

	
	String habitation_name;
	
	List<BoothAssociation> boothList;
	
	int status; 
  
	/**
	 * @return the boothList
	 */
	public List<BoothAssociation> getBoothList() {
		return boothList;
	}


	/**
	 * @param boothList the boothList to set
	 */
	public void setBoothList(List<BoothAssociation> boothList) {
		this.boothList = boothList;
	}


	/**
	 * @return the volunteersList
	 */
	public List<Volunteer> getVolunteersList() {
		return volunteersList;
	}

	
	/**
	 * @return the habitation_name
	 */
	public String getHabitation_name() {
		return habitation_name;
	}


	/**
	 * @param habitation_name the habitation_name to set
	 */
	public void setHabitation_name(String habitation_name) {
		this.habitation_name = habitation_name;
	}


	/**
	 * @param volunteersList the volunteersList to set
	 */
	public void setVolunteersList(List<Volunteer> volunteersList) {
		this.volunteersList = volunteersList;
	}


	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
 
	
}
