package com.BjpTracking.model;

import java.util.List;

public class Parliment {
	
	int district_id;
	String parliment_name;
	int id;
	List<Integer> associateBlock;
	List<KeyValueModel> keyValue;
	int status;
	List<KeyValueModel> parlimentList;
	List<Integer> ids;
 
	
	
	
	
	
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
	 * @return the ids
	 */
	public List<Integer> getIds() {
		return ids;
	}
	/**
	 * @param ids the ids to set
	 */
	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}
	/**
	 * @return the associateBlock
	 */
	public List<Integer> getAssociateBlock() {
		return associateBlock;
	}
	/**
	 * @param associateBlock the associateBlock to set
	 */
	public void setAssociateBlock(List<Integer> associateBlock) {
		this.associateBlock = associateBlock;
	}
	 
	/**
	 * @return the keyValue
	 */
	public List<KeyValueModel> getKeyValue() {
		return keyValue;
	}
	/**
	 * @param keyValue the keyValue to set
	 */
	public void setKeyValue(List<KeyValueModel> keyValue) {
		this.keyValue = keyValue;
	}
	/**
	 * @return the parlimentList
	 */
	public List<KeyValueModel> getParlimentList() {
		return parlimentList;
	}
	/**
	 * @param parlimentList the parlimentList to set
	 */
	public void setParlimentList(List<KeyValueModel> parlimentList) {
		this.parlimentList = parlimentList;
	}
	/**
	 * @return the district_id
	 */
	public int getDistrict_id() {
		return district_id;
	}
	/**
	 * @param district_id the district_id to set
	 */
	public void setDistrict_id(int district_id) {
		this.district_id = district_id;
	}
	/**
	 * @return the parliment_name
	 */
	public String getParliment_name() {
		return parliment_name;
	}
	/**
	 * @param parliment_name the parliment_name to set
	 */
	public void setParliment_name(String parliment_name) {
		this.parliment_name = parliment_name;
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
