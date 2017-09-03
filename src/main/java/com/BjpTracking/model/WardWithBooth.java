package com.BjpTracking.model;

import java.util.ArrayList;
import java.util.List;

public class WardWithBooth extends Booth {
	
	
	int ward_id;
	
	String ward_name;
	
	List<Ward> wardList;
 
	
	/**
	 * @return the wardList
	 */
	public List<Ward> getWardList() {
		return wardList;
	}

	/**
	 * @param wardList the wardList to set
	 */
	public void setWardList(List<Ward> wardList) {
		this.wardList = wardList;
	}

	/**
	 * @return the ward_id
	 */
	public int getWard_id() {
		return ward_id;
	}

	/**
	 * @param ward_id the ward_id to set
	 */
	public void setWard_id(int ward_id) {
		this.ward_id = ward_id;
	}

	/**
	 * @return the ward_name
	 */
	public String getWard_name() {
		return ward_name;
	}

	/**
	 * @param ward_name the ward_name to set
	 */
	public void setWard_name(String ward_name) {
		this.ward_name = ward_name;
	}

	 

}
