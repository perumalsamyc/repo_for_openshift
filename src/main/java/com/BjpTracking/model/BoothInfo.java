package com.BjpTracking.model;

import java.util.List;

public class BoothInfo {
	List<BoothAssociation> boothList;
	
	List<KeyValueModel> unAssociatedWard;

	int id;
	
	int status;
	
	
	
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
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
	 * @return the unAssociatedWard
	 */
	public List<KeyValueModel> getUnAssociatedWard() {
		return unAssociatedWard;
	}

	/**
	 * @param unAssociatedWard the unAssociatedWard to set
	 */
	public void setUnAssociatedWard(List<KeyValueModel> unAssociatedWard) {
		this.unAssociatedWard = unAssociatedWard;
	}

	 
	
	
	
}
