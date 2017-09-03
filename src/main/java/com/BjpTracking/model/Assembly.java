package com.BjpTracking.model;

import java.util.List;

public class Assembly {
	
	int district_id;
	String assembly_name;
	int assembly_id;
	List<Integer> id;
	List<KeyValueModel> keyValue;
 	int status;
	List<KeyValueModel> associateBlock;
	List<KeyValueModel> assemblyList;
 
 
	
	/**
	 * @return the assemblyList
	 */
	public List<KeyValueModel> getAssemblyList() {
		return assemblyList;
	}
	/**
	 * @param assemblyList the assemblyList to set
	 */
	public void setAssemblyList(List<KeyValueModel> assemblyList) {
		this.assemblyList = assemblyList;
	}
	/**
	 * @return the associateBlock
	 */
	public List<KeyValueModel> getAssociateBlock() {
		return associateBlock;
	}
	/**
	 * @param associateBlock the associateBlock to set
	 */
	public void setAssociateBlock(List<KeyValueModel> associateBlock) {
		this.associateBlock = associateBlock;
	}
	/**
	 * @return the id
	 */
	public List<Integer> getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(List<Integer> id) {
		this.id = id;
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
	 * @return the assembly_name
	 */
	public String getAssembly_name() {
		return assembly_name;
	}
	/**
	 * @param assembly_name the assembly_name to set
	 */
	public void setAssembly_name(String assembly_name) {
		this.assembly_name = assembly_name;
	}
	/**
	 * @return the assembly_id
	 */
	public int getAssembly_id() {
		return assembly_id;
	}
	/**
	 * @param assembly_id the assembly_id to set
	 */
	public void setAssembly_id(int assembly_id) {
		this.assembly_id = assembly_id;
	}
	
}
