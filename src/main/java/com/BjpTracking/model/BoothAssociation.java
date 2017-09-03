package com.BjpTracking.model;

import java.util.List;

public class BoothAssociation extends Booth {

	List<Ward> wardList;

	int habitation_id;
	 
	/**
	 * @return the habitation_id
	 */
	public int getHabitation_id() {
		return habitation_id;
	}

	/**
	 * @param habitation_id the habitation_id to set
	 */
	public void setHabitation_id(int habitation_id) {
		this.habitation_id = habitation_id;
	}

	/**
	 * @return the wardList
	 */
	public List<Ward> getWardList() {
		return wardList;
	}

	/**
	 * @param wardList
	 *            the wardList to set
	 */
	public void setWardList(List<Ward> wardList) {
		this.wardList = wardList;
	}

}
